package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import server.Server;
import basisklassen.Person;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;

/////////////////////////////////////////////////////////////////////////////////////////////////
// PersonSetRemote // PersonSetRemote // PersonSetRemote // PersonSetRemote // PersonSetRemote //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class PersonSetRemote extends UnicastRemoteObject implements interfaces.PersonSetInterface
{
	// Server
    private Server server;

	// Daten
	private Hashtable persons = new Hashtable();
    private Vector orderedList = new Vector();
    private ClassMap map;

    public PersonSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "PersonSetRemote", this);

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
        map = ClassMappings.getClass("Person");
        int count = 0;

        try
        {
            PersistentCriteria pc = new RetrieveCriteria("Person","Admin");
            ResultSet res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                long id = res.getLong(map.getAttribute("objectIdentifier").getColumn());
                Person person = new Person(id);
                person.swap(res);

                String name = person.getNameLang();


                // Hashtable
                persons.put(new Long(id), person);

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
            }
        }catch (Exception e) {System.out.println(e+"p");}

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
		Enumeration enumeration = persons.elements();
		Person p;

		while(enumeration.hasMoreElements())
		{
			p = (Person) enumeration.nextElement();
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
			Enumeration enumeration = persons.elements();
			Person p;

			while(enumeration.hasMoreElements())
			{
				p = (Person) enumeration.nextElement();
				if(p.getKuerzel().equals(kuerzel)) return true;
			}
		}

		return false;
    }

    // Person anlegen
    public void create(String kuerzel, Person person) throws RemoteException
    {	
        person.setKz(kuerzel);
        person.save();
        String name = person.getNameLang();
        int count = 0;

        // Hashtable
        persons.put(new Long(person.getID()), person);

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

        person.setKz(kuerzel);
        person.delete();

    }

    // Person updaten
    public void update(String kuerzel, Person person) throws RemoteException
    {
		Long id = new Long(person.getID());

		orderedList.setElementAt(person, orderedList.indexOf(persons.get(id)));
		persons.put(id, person);

		person.setKz(kuerzel);
		person.save();
    }
}