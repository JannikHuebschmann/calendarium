package server; //
//////////////////

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import event.*;
import server.mail.*;
import basisklassen.Person;

///////////////////////////////////////////////////////////////////////////////////////
// MessageServer // MessageServer // MessageServer // MessageServer // MessageServer //
///////////////////////////////////////////////////////////////////////////////////////

public class MessageServer extends UnicastRemoteObject implements interfaces.MessageServerInterface,
                                                                  Runnable, EventConstants, SmtpSource
{   
	private static final long serialVersionUID = -523555823384707092L;

	// Server
        private Server server;

    // Daten
    private Hashtable loggedUsers = new Hashtable();
    private Hashtable eventHash = new Hashtable();

    // Thread
    private Thread thread;

    // SmtpListener
    private SmtpListener smtpListener;

    // Neue Events
    private Vector eventList = new Vector();

    public MessageServer(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "MessageServer", this);

        } catch(Exception e)
        {   e.printStackTrace();
        }

        load();

        // MailListener
        Properties props = server.getServerProperties();
        smtpListener = new SmtpClient((String) props.get("SMTPServerName"),
                                      (String) props.get("SMTPServerPort"));
        // Waiting for Messages
        thread = new Thread(this);
        thread.start();
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
    private void load()
    {
        try {
			FileInputStream istream = new FileInputStream(
					"data/files/events.dat");
			ObjectInputStream s = new ObjectInputStream(istream);

			eventHash = (Hashtable) s.readObject();
			istream.close();

		} catch (Exception e) {
			initialize();
		}
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern // Speichern // Speichern // Speichern //   Speichern //
    //////////////////////////////////////////////////////////////////////////////////
    public synchronized void save()
    {
        try
                {       FileOutputStream ostream = new FileOutputStream("data/files/events.dat");
                        ObjectOutputStream p = new ObjectOutputStream(ostream);

                        p.writeObject(eventHash);
                        p.flush();
            ostream.close();

                } catch(IOException e)
                {
                    e.printStackTrace();
                    System.exit(0);
                }
    }

    @SuppressWarnings("unchecked")
	private void initialize()
    {
        Vector persons = null;

                try
                {       persons = server.getPersonSetRemote().getOrderedList();

                } catch(RemoteException e) {}

                if(persons != null)
                {
                        Enumeration e = persons.elements();
                        while(e.hasMoreElements())
                        {
                                eventHash.put(new Long(((Person) e.nextElement()).getID()), new Vector());
                        }
                }
    }

    private Person getPerson(String kuerzel, String pwd)
        {
                Person person = null;

                try
                {       person =  server.getPersonSetRemote().getByKuerzel(kuerzel);

                } catch(RemoteException e) {}

                if(person != null)
                {
                        if(person.getPasswort().equals(pwd)) return person;
                }

                return null;
        }

    private void closeConnection(Long id, String client)
    {
                Hashtable logins = (Hashtable) loggedUsers.get(id);
                try {
					logins.remove(client);
				

			        if(logins.size() == 0)
			        {   loggedUsers.remove(id);
			        }
                } catch (RuntimeException e) {					
				}
    }

    // Person löschen
        public void deletePerson(Person person)
        {
                Long id = new Long(person.getID());

                loggedUsers.remove(id);
                eventHash.remove(id);
        }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // connect
    @SuppressWarnings("unchecked")
	public Object connect(String kuerzel, String pwd) throws RemoteException
    {
                Person person;

        // Rückgabewert -1: falsches Passwort; -2: Programm bereits gestartet

        if((person = getPerson(kuerzel, pwd)) != null)
                {
                        String client = "";
                        Long id = new Long(person.getID());

            try
                        {   client = getClientHost();

                        } catch(ServerNotActiveException e) {}

                        if(!loggedUsers.containsKey(id))
                        {
                                if(!eventHash.containsKey(id))
                                {   eventHash.put(id, new Vector());
                                }

            } else
                        {
                                Hashtable logins = (Hashtable) loggedUsers.get(id);
                                if(logins.containsKey(client))
                                {
                                        CalendariumListener listener = (CalendariumListener) logins.get(client);
                                        try
                                        {   if(listener.isActive()) return new Integer(-2);
                                        } catch(Exception e) {}
                }
                        }

                        return person;

                } else return new Integer(-1);
    }

    // disconnect
    public void disconnect(Person person) throws RemoteException
    {
        String client = "";

        try
        {   client = getClientHost();
            closeConnection(new Long(person.getID()), client);

        } catch(ServerNotActiveException e) {}
    }

    // add Listener
    @SuppressWarnings("unchecked")
	public void addCalendariumListenerTo(CalendariumListener listener, Person p) throws RemoteException
    {
        String client = "";
        Long id = new Long(p.getID());

        try
                {   client = getClientHost();
        } catch(ServerNotActiveException e) {}

        Hashtable logins;
        if(!loggedUsers.containsKey(id))
        {
            logins = new Hashtable();
            logins.put(client, listener);

            loggedUsers.put(id, logins);

        } else
        {
            logins = (Hashtable) loggedUsers.get(id);
            logins.put(client, listener);
        }

        // send Events
        Vector events = (Vector) eventHash.get(id);

        try
        {   while(events.size() > 0)
            {
                CalendariumEvent evt = (CalendariumEvent) events.firstElement();
                listener.processCalendariumEvent(evt);
                events.removeElement(evt);
            }

        } catch(Exception ex)
        {   closeConnection(id, client);
        }
    }

    @SuppressWarnings("unchecked")
	public synchronized void addEvent(CalendariumEvent evt)
    {   eventList.addElement(evt);
    }

    public void run()
    {
        while(true)
        {
            try
            {   Thread.sleep(1000);
            } catch(InterruptedException e) {}

            if(eventList.size() > 0)
            {
                CalendariumEvent evt = (CalendariumEvent) eventList.firstElement();
                sendEvent(evt);

                eventList.removeElement(evt);
            }
        }
    }

    @SuppressWarnings("unchecked")
	protected void sendEvent(CalendariumEvent evt)
    {
    	
        Person person = (Person) evt.getEmpfänger();

        switch(evt.getEventID())
        {
            case MESSAGE_EVT:
            case NFKT_EVT:

                Long id = new Long(person.getID());

                if(!loggedUsers.containsKey(id))
                {
                	//email
                	//try von johnny
                    try {
						if(evt.getEventID() == NFKT_EVT)
						{   ((NfktEvent) evt).setShowing(false);
						}

						Vector events = (Vector) eventHash.get(id);
						events.addElement(evt);
					} catch (RuntimeException e1) {
						//e1.printStackTrace();
					}

                } else
                {
                	//normale nachricht
                    Hashtable logins = (Hashtable) loggedUsers.get(id);

                    Enumeration e = logins.keys();
                    while(e.hasMoreElements())
                    {
                        String client = (String) e.nextElement();
                        CalendariumListener listener = (CalendariumListener) logins.get(client);

                                                // processEvent
                        @SuppressWarnings("unused")
						ProcessMessage pMsg = new ProcessMessage(id, client, listener, evt);
                    }
                }

                // EMail // EMail // EMail // EMail // EMail // EMail // EMail // EMail // EMail //
                if(evt.getEventID() == NFKT_EVT)
                {
                	
                    NfktEvent nEvt = (NfktEvent) evt;
                    if(nEvt.getDelivery() == data.Shared.NFKT_EMAIL)
                    {	
                        SmtpEvent smtpe = new SmtpEvent(this, server.getEMailAdresse(),
                                                        nEvt.getEmpfänger().getEmailAdresse(),
                                                        nEvt.getTitle(), nEvt.getMessage());
                                               
                        // senden
                        smtpListener.send(smtpe);
                    }

                } else
                {
                    MessageEvent mEvt = (MessageEvent) evt;
                    if(person.getVorzugsNfkt() == data.Shared.NFKT_EMAIL)
                    {
                        SmtpEvent smtpe = new SmtpEvent(this, server.getEMailAdresse(),
                                                        mEvt.getEmpfänger().getEmailAdresse(),
                                                        mEvt.getTitle(), mEvt.getMessage());
                        // senden
                        smtpListener.send(smtpe);
                    }
                }

                break;

            default:

                // Event for all logged User
                Enumeration e = loggedUsers.keys();
                while(e.hasMoreElements())
                {
                    id = (Long) e.nextElement();
                    Hashtable logins = (Hashtable) loggedUsers.get(id);

                    Enumeration enumer = logins.keys();
                    while(enumer.hasMoreElements())
                    {
                        String client = (String) enumer.nextElement();
                        CalendariumListener listener = (CalendariumListener) logins.get(client);

                        try
                        {   listener.processCalendariumEvent(evt);

                        } catch(Exception ex)
                        {   closeConnection(id, client);
                        }
                    }
                }
        }
    }

    // AdminMessage
    public void sendAdminMessage(String msg) throws RemoteException
    {   addEvent(new AdminEvent(msg));
    }

    // ShutDown
    public void shutDown() throws RemoteException
    {
        try
        {   while(eventList.size() > 0)
            {   Thread.sleep(1000);
            }
        } catch(InterruptedException e) {}

        save();
        System.exit(0);
    }

    // getConnections: fuer Monitor
    @SuppressWarnings("unchecked")
	public Hashtable getConnections() throws RemoteException
    {
        Hashtable result = new Hashtable();

        Enumeration e = loggedUsers.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Person person = server.getPersonSetRemote().getByID(pid.longValue());

            result.put(person, (Hashtable) loggedUsers.get(pid));
        }

        return result;
    }

    // SmtpEvent
    public void respond(int code)
        {   // Möglichkeit Fehlercode auszugeben!
        }

        // Thread zur MessageVerarbeitung
        class ProcessMessage implements Runnable
        {
                // Thread
                private Thread thread;

                // User
                private Long id;
                private String client;

                // Message
                private CalendariumListener listener;
                private CalendariumEvent evt;

                ProcessMessage(Long id, String client, CalendariumListener listener, CalendariumEvent evt)
                {
                        this.id = id;
                        this.client = client;

                        this.listener = listener;
                        this.evt = evt;

                        thread = new Thread(this);
                        thread.start();
                }

                public void run()
                {
                        for(int i = 0; i < 3; i++)      // 3 Versuche, 5 sec. warten nach jedem Versuch
                        {
                                try
                                {       listener.processCalendariumEvent(evt);
                                        return;

                                } catch(Exception ex)
                                {
                                        try
                                        {       Thread.sleep(5000);
                                        } catch(Exception exp) {}
                                }
                        }

                        try {
							closeConnection(id, client);
						} catch (RuntimeException e) {
							
						}
                }
        }
}
