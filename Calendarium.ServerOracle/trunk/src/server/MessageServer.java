package server; //
//////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import server.mail.SmtpClient;
import server.mail.SmtpEvent;
import server.mail.SmtpListener;
import server.mail.SmtpSource;
import basisklassen.Person;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.ConnectionManager;
import dblayer.DeleteCriteria;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;
import event.AdminEvent;
import event.CalendariumEvent;
import event.CalendariumListener;
import event.EventConstants;
import event.MessageEvent;
import event.NfktEvent;
import event.TerminEvent;
import event.ToDoEvent;

///////////////////////////////////////////////////////////////////////////////////////
// MessageServer // MessageServer // MessageServer // MessageServer // MessageServer //
///////////////////////////////////////////////////////////////////////////////////////

public class MessageServer extends UnicastRemoteObject implements interfaces.MessageServerInterface,
                                                                  Runnable, EventConstants, SmtpSource
{   // Server
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

    private ClassMap map;

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
           CalendariumEvent calEvt = null;
           long empfänger, termin, todo;
           map = ClassMappings.getClass("CalendariumEvent");
           Vector events;

           initialize();
           try
           {
                PersistentCriteria pc = new RetrieveCriteria("CalendariumEvent","Admin");
                ResultSet res = pc.perform(pc.buildForObject());

                while (res.next())
                {
                    int eventID = res.getInt("eventID");
                    switch (eventID)
                    {
                        case MESSAGE_EVT: calEvt = new MessageEvent(); break;
                        case NFKT_EVT:    calEvt = new NfktEvent(); break;
                        case TERMIN_EVT:  calEvt = new TerminEvent(); break;
                        case TODO_EVT:    calEvt = new ToDoEvent(); break;
                        case ADMIN_EVT:   calEvt = new AdminEvent(); break;
                        default:
                    }

                   /* sender = res.getLong(map.getAttribute("sender").getColumn());
                    empfänger = res.getLong(map.getAttribute("empfänger").getColumn());*/

                    if (calEvt instanceof TerminEvent)
                    {
                        termin = res.getLong(map.getAttribute("termin").getColumn());
                        ((TerminEvent) calEvt).setTermin(server.getTerminSetRemote().getTerminByID(termin));
                    }
                    if (calEvt instanceof ToDoEvent)
                    {
                        todo = res.getLong(map.getAttribute("todo").getColumn());
                        ((ToDoEvent) calEvt).setToDo(server.getToDoSetRemote().getToDoByID(todo));
                    }

                    /*Person person = server.getPersonSetRemote().getByID(sender);  //empfänger
                    calEvt.setSender(person);

                    person = server.getPersonSetRemote().getByID(empfänger);
                    calEvt.setEmpfänger(person);*/

                    calEvt.setPersistence(true);
                    calEvt.swap(res);

                    empfänger = calEvt.getEmpfänger().getID();
                    events = (Vector) eventHash.get(new Long(empfänger));
                    if (events == null)
                    {
                        events = new Vector();
                        events.addElement(calEvt);
                        eventHash.put(new Long(empfänger),events);
                    }
                    else
                        events.addElement(calEvt);

                }

                pc = new DeleteCriteria("CalendariumEvent","Admin");
                pc.perform(pc.buildForObject() + pc.addSelectNotEqualTo("timeStamp","0"));

           }catch (Exception e){System.out.println(e);}

    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	public synchronized void save()
    {

	    Enumeration e = eventHash.elements();
		while(e.hasMoreElements())
		{
		    Vector event = (Vector) e.nextElement();

		    Enumeration e1 = event.elements();
			while (e1.hasMoreElements())
			{
			    CalendariumEvent evt = (CalendariumEvent) e1.nextElement();
			    evt.setKz("Admin");
			    evt.save(null,null);
			}
	    }

    }

    private void initialize()
    {
        Vector persons = null;

		try
		{	persons = server.getPersonSetRemote().getOrderedList();

		} catch(RemoteException e) {}

		if(persons != null)
		{
			Enumeration e = persons.elements();
			while(e.hasMoreElements())
			{
			    Person p = (Person) e.nextElement();
				eventHash.put(new Long(p.getID()), new Vector());
			}
		}
    }

    private Person getPerson(String kuerzel, String pwd)
	{
		Person person = null;

		try
		{	person =  server.getPersonSetRemote().getByKuerzel(kuerzel);
		} catch(RemoteException e) {}

		if(person != null)
		{
			if(person.getPasswort().equals(pwd)) return person;
		}

		return null;
	}

    private void closeConnection(Long id, String client)
    {
		Hashtable logins;
		logins = (Hashtable) loggedUsers.get(id);
		try {
			
			logins.remove(client);
		} catch (RuntimeException e) {
		}

        try
        {
    		Person p = server.getPersonSetRemote().getByID(id.longValue());
	    	ConnectionManager.closeConnection(p.getKuerzel());
	    }catch (RemoteException r) {System.out.println(r);}

        if(logins.size() == 0)
        {   loggedUsers.remove(id);
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

			ConnectionManager.databaseConnection(person.getKuerzel());


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
                    if(evt.getEventID() == NFKT_EVT)
                    {
                        ((NfktEvent) evt).setShowing(false);

                    }

                    Vector events = (Vector) eventHash.get(id);
                    events.addElement(evt);


                } else
                {
                    Hashtable logins = (Hashtable) loggedUsers.get(id);

                    Enumeration e = logins.keys();
                    while(e.hasMoreElements())
                    {
                        String client = (String) e.nextElement();

                        CalendariumListener listener = (CalendariumListener) logins.get(client);

						// processEvent
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

                    Enumeration enumeration = logins.keys();
                    while(enumeration.hasMoreElements())
                    {
                        String client = (String) enumeration.nextElement();
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
    public Hashtable getConnections() throws RemoteException
    {
        Hashtable result = new Hashtable();

        Enumeration e = loggedUsers.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Person person = server.getPersonSetRemote().getByID(pid.longValue());  //SSS

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
			for(int i = 0; i < 3; i++)	// 3 Versuche, 5 sec. warten nach jedem Versuch
			{
				try
				{	listener.processCalendariumEvent(evt);
					return;

				} catch(Exception ex)
				{
					try
					{	Thread.sleep(5000);
					} catch(Exception exp) {}
				}
			}

			closeConnection(id, client);
		}
	}
}