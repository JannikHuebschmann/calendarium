package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import basisklassen.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// PersonSetRemote // PersonSetRemote // PersonSetRemote // PersonSetRemote // PersonSetRemote //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class PersonSetRemote extends UnicastRemoteObject implements interfaces.PersonSetInterface
{
	private static final long serialVersionUID = -8241668397936675246L;

	// Server
    private Server server;

	// Daten
	private Hashtable persons;
    private Vector orderedList;
    private long lastID;

    public PersonSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        { 
        		Naming.bind(name + "PersonSetRemote", this);

        } catch(Exception e)
        {   e.printStackTrace();
        }

        load();
    }

	///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
	private void load()
    {
        try
		{   FileInputStream istream = new FileInputStream("data/files/personen.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastID = ((Long) s.readObject()).longValue();
            persons = (Hashtable) s.readObject();
            orderedList = (Vector) s.readObject();

            istream.close();

		} catch(Exception e)
		{
			lastID = 0;
			persons = new Hashtable();
			orderedList = new Vector();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	public synchronized void save()
    {
        try
		{	FileOutputStream ostream = new FileOutputStream("data/files/personen.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastID));
			p.writeObject(persons);
			p.writeObject(orderedList);
			p.flush();

			ostream.close();

		} catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Liste der IDs sortiert nach Nachname
    public Vector getOrderedList() throws RemoteException
	{	return orderedList;
	}

    // Person by Kuerzel
	public Person getByKuerzel(String kuerzel) throws RemoteException
	{
		Enumeration enumer = persons.elements();
		Person p;

		while(enumer.hasMoreElements())
		{
			p = (Person) enumer.nextElement();
			if(p.getKuerzel().equals(kuerzel)) return p;
		}

		return null;
	}

	// Person by ID
	public Person getByID(long id) throws RemoteException
	{
	    return (Person) persons.get(new Long(id));
	}

	// Ist Kuerzel bekannt?
	public boolean contains(String kuerzel) throws RemoteException
	{
		if(persons != null)
		{
			Enumeration enumer = persons.elements();
			Person p;

			while(enumer.hasMoreElements())
			{
				p = (Person) enumer.nextElement();
				if(p.getKuerzel().equals(kuerzel)) return true;
			}
		}

		return false;
    }

    // Person anlegen
    @SuppressWarnings("unchecked")
	public void create(String kuerzel, Person person) throws RemoteException
    {
		// LastID setzen
		lastID++;
		person.setID(lastID);

        String name = person.getNameLang();
        int count = 0;

        // Hashtable
        persons.put(new Long(lastID), person);

        // OrderedList
        if(orderedList != null)
        {
            Enumeration e = orderedList.elements();
            while(e.hasMoreElements())
            {
                String n = ((Person) e.nextElement()).getNameLang();
                if(n.compareTo(name) > 0)
                {
                    orderedList.insertElementAt(person, count);
                    break;

                } else count++;
            }

            if(count >= orderedList.size())
            {   orderedList.addElement(person);
            }
        }

        save();
    }

    // Person löschen
    public void delete(String kuerzel, Person person) throws RemoteException
    {
		Long id = new Long(person.getID());

        persons.remove(id);

        Enumeration e = orderedList.elements();
        while(e.hasMoreElements())
        {
            Person p = (Person) e.nextElement();
            if(id.longValue() == p.getID())
            {
                orderedList.removeElement(p);
                break;
            }
        }

        // Datenreinigung
        server.getTypSetRemote().deletePerson(person);
        server.getGroupSetRemote().deletePerson(person);
        server.getRightSetRemote().deletePerson(person);
        server.getMessageServer().deletePerson(person);
        server.getTerminSetRemote().deletePerson(person);
        server.getToDoSetRemote().deletePerson(person);

        save();
    }

    // Person updaten
    @SuppressWarnings("unchecked")
	public void update(String kuerzel, Person person) throws RemoteException
    {
		Long id = new Long(person.getID());

		orderedList.setElementAt(person, orderedList.indexOf(persons.get(id)));
		persons.put(id, person);
		save();
    }
}