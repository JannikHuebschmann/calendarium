package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;

import server.Server;
import basisklassen.Gruppe;
import basisklassen.Person;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;

////////////////////////////////////////////////////////////////////////////////////////////
// GroupSetRemote // GroupSetRemote // GroupSetRemote // GroupSetRemote // GroupSetRemote //
////////////////////////////////////////////////////////////////////////////////////////////

public class GroupSetRemote extends UnicastRemoteObject implements interfaces.GroupSetInterface
{
    // RootID
    private final Long rootID = new Long(-1);

    // Server
	private Server server;
	private ClassMap map;

	// Daten
    private Hashtable groupHash = new Hashtable();    // Hashtable aller Gruppen

    public GroupSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "GroupSetRemote", this);

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
        Gruppe g;
        ResultSet res1;

        map = ClassMappings.getClass("Gruppe");

        try
        {
            PersistentCriteria pc = new RetrieveCriteria("Gruppe","Admin");
            ResultSet res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                long id = res.getLong(map.getAttribute("objectIdentifier").getColumn());
                long owner = res.getLong(map.getAttribute("owner").getColumn());

                g = null;

                if (owner == 0)
                    g = (Gruppe)groupHash.get(new Long(id));

                if (g == null)
                {
                    g = new Gruppe(id);
                    g.swap(res);
                }
                g.setKz("Admin");

                if (owner == -1)  // Admin-root
                   groupHash.put(rootID,g);
                else
                {
                    if (owner != 0)
                        groupHash.put(new Long(owner),g);   //User-root
                    else
                        groupHash.put(new Long(id),g);
                }

                //res1 = pc.perform(pc.buildForInnerTable("gruppen")+pc.addSelectEqualTo("objectIdentifier",new Long(id)));
                res1 = g.retrieveInnerTable("gruppen");
                while (res1.next())
                {
                    long id1 = res1.getLong(map.getAttribute("gruppen").getNestedAttribute("objectIdentifier").getColumn());
                    Gruppe g1 = (Gruppe)groupHash.get(new Long(id1));

                    if (g1 == null)
                    {
                        g1 = new Gruppe(id1);
                        g1.setKz("Admin");
                        g1.retrieve();
                    }

                    g.addGruppe(g1);
                    groupHash.put(new Long(id1),g1);
                }
                res1.close();

                //res1 = pc.perform(pc.buildForInnerTable("personen")+pc.addSelectEqualTo("objectIdentifier",new Long(id)));
                res1 = g.retrieveInnerTable("personen");
                while (res1.next())
                {
                    long id1 = res1.getLong(map.getAttribute("personen").getNestedAttribute("objectIdentifier").getColumn());
                    Person p1 = new Person(id1);

                    p1.setKz("Admin");
                    p1.retrieve();

                    g.addPerson(p1);
                }
                res1.close();

            }
        }catch (Exception s){}
    }


    private void deleteSubGroups(Hashtable gruppen)
	{
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe gruppe = (Gruppe) e.nextElement();

				if(!gruppe.getAdminFlag())
				{   groupHash.remove(new Long(gruppe.getID()));

                    gruppe.setKz("Admin");
                    gruppe.delete();
				}

				deleteSubGroups(gruppe.getGruppen());
			}
		}
	}

    // User löschen
    public void deletePerson(Person person)
    {
        Long personID = new Long(person.getID());

        if(groupHash != null)
        {
            // Gruppe der Person löschen
            Gruppe gruppe = (Gruppe) groupHash.get(personID);

            if(gruppe != null)
            {
                deleteSubGroups(gruppe.getGruppen());
                groupHash.remove(personID);

                gruppe.setKz("Admin");
                gruppe.delete();

            }

            // Person als Gruppenmitglied löschen
            Enumeration e = groupHash.keys();
            while(e.hasMoreElements())
            {
                Long id = (Long) e.nextElement();

                gruppe = (Gruppe) groupHash.get(id);
                Hashtable personen = gruppe.getPersonen();

                Enumeration e1 = personen.keys();
                while (e1.hasMoreElements())
                {
                    Long id1 = (Long) e1.nextElement();
                    if (id1.longValue() == person.getID())
                    {
                        gruppe.deletePerson(person);

                        gruppe.setKz("Admin");
                        gruppe.delete(person,"personen");
                    }
                }
            }

        }
    }

	///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // AdminRootGruppe ausgeben
	public Gruppe getAdminRoot() throws RemoteException
    {
        Gruppe gruppe = (Gruppe) groupHash.get(rootID);

        if(gruppe == null)
        {
            gruppe = new Gruppe(true, "ROOT", "Admin-Gruppen");
            gruppe.setOwner(rootID.longValue());
            gruppe.setKz("Admin");
            gruppe.save();

            groupHash.put(rootID, gruppe);

        }

        return gruppe;
    }

    // UserRootGruppe ausgeben
    public Gruppe getUserRoot(Person person) throws RemoteException
    {
        Long persID = new Long(person.getID());
        Gruppe gruppe = (Gruppe) groupHash.get(persID);

        if(gruppe == null)
        {
            gruppe = new Gruppe(true, "EG", "Eigene Gruppen");
            gruppe.setOwner(persID.longValue());

            groupHash.put(persID, gruppe);

            gruppe.setKz("Admin");
            gruppe.save();
        }

        return gruppe;
    }

	// Gruppe by ID
	public Gruppe getByID(long id) throws RemoteException
	{
		return (Gruppe) groupHash.get(new Long(id));
	}

    // Kuerzel vergeben?
	public boolean contains(String kuerzel) throws RemoteException
	{
	    Enumeration e = groupHash.elements();

	    while(e.hasMoreElements())
	    {
	        String k = ((Gruppe) e.nextElement()).getKuerzel();
	        if(k.equals(kuerzel)) return true;
	    }

	    return false;
	}

	// Gruppen neu verketten
	public void exchangeOrder(String kuerzel, Gruppe gruppe, Gruppe parentOld, Gruppe parentNew) throws RemoteException
	{
	    gruppe = (Gruppe) groupHash.get(new Long(gruppe.getID()));

	    parentOld = (Gruppe) groupHash.get(new Long(parentOld.getID()));
	    parentNew = (Gruppe) groupHash.get(new Long(parentNew.getID()));

	    parentOld.removeGruppe(gruppe);

	    parentOld.setKz(kuerzel);
	    parentOld.delete(gruppe,"gruppen");

	    parentNew.addGruppe(gruppe);

	    parentNew.setKz(kuerzel);
	    parentNew.save(gruppe,"gruppen");

	    //save();
	}

	// Gruppe anlegen   XXXXXXXXX
	public void createGroupIn(Person user, Gruppe gruppe, Gruppe parent)
	{
	    parent = (Gruppe) groupHash.get(new Long(parent.getID()));
	    if (parent == null)   // root-group
	        parent = (Gruppe) groupHash.get(new Long(user.getID()));

        gruppe.setKz(user.getKuerzel());
        gruppe.save();

		parent.addGruppe(gruppe);

    	groupHash.put(new Long(gruppe.getID()), gruppe);

        parent.setKz(user.getKuerzel());
        parent.save(gruppe,"gruppen");
	}

	// Gruppe löschen    XXXXXXXXXx
	public void deleteGroupOf(Person user, Gruppe gruppe, Gruppe parent)
	{
	    Long groupID = new Long(gruppe.getID());

	    parent = (Gruppe) groupHash.get(new Long(parent.getID()));
	    if (parent == null)   //root-group
	        parent = (Gruppe) groupHash.get(new Long(user.getID()));

	    parent.removeGruppe(gruppe);

        parent.setKz(user.getKuerzel());
        parent.delete(gruppe,"gruppen");

	    if(gruppe.getAdminFlag())
	    {
	        Gruppe adminGruppe = (Gruppe) groupHash.get(rootID);
	        if(!adminGruppe.isMember(gruppe))
	        {
	            Enumeration e = groupHash.keys();
                while(e.hasMoreElements())
                {
                    Long id = (Long) e.nextElement();
                    if (id.longValue() == user.getID())
                    {
                        Gruppe g = (Gruppe) groupHash.get(id);
                        g.deleteGruppe(gruppe);

                        g.setKz(user.getKuerzel());
                        g.delete(gruppe,"gruppen");
                    }
                }

                groupHash.remove(groupID);

                gruppe.setKz(user.getKuerzel());
                gruppe.delete();
            }
	    } else
	    {
            Long persID = new Long(user.getID());

            Gruppe userGruppe = (Gruppe) groupHash.get(persID);
            if(!userGruppe.isMember(gruppe))
            {
                groupHash.remove(groupID);

                gruppe.setKz(user.getKuerzel());
                gruppe.delete();
            }
	    }

	    // Datenreinigung
        server.getRightSetRemote().deleteGruppe(gruppe);

	}

	// Gruppe ändern   XXXXXXXX
	public void update(Person user, Gruppe gruppe)
	{

	    Gruppe grpOld = (Gruppe) groupHash.get(new Long(gruppe.getID()));

	    grpOld.setKz(user.getKuerzel());

	    // Name
	    if(grpOld.getName().compareTo(gruppe.getName()) != 0)
	    {
	        grpOld.setName(gruppe.getName());

	        grpOld.save();

	    }

        // Kuerzel
        if (grpOld.getKuerzel().compareTo(gruppe.getKuerzel()) != 0)
        {
            grpOld.setKuerzel(gruppe.getKuerzel());

	        grpOld.save();
        }

        // Gruppeninhalt
        Gruppe g;
        Person p;

        Hashtable gruppen = (Hashtable) gruppe.getGruppen();
        Hashtable grpnOld = (Hashtable) grpOld.getGruppen();

        Hashtable personen = (Hashtable) gruppe.getPersonen();
        Hashtable persnOld = (Hashtable) grpOld.getPersonen();

        // Gruppen
        Enumeration e = gruppen.elements();
        while(e.hasMoreElements())
        {
            g = (Gruppe) e.nextElement();

            if(!grpnOld.containsKey(new Long(g.getID())))
            {
                grpOld.addGruppe(g);

	            grpOld.save(g,"gruppen");
            }
        }

        e = grpnOld.elements();
        while(e.hasMoreElements())
        {
            g = (Gruppe) e.nextElement();

            if(!gruppen.containsKey(new Long(g.getID())))
            {
                grpOld.removeGruppe(g);

	            grpOld.delete(g,"gruppen");
            }
        }

        // Personen
        e = personen.elements();
        while(e.hasMoreElements())
        {
            p = (Person) e.nextElement();

            if(!persnOld.containsKey(new Long(p.getID())))
            {
                grpOld.addPerson(p);

	            grpOld.save(p,"personen");
            }
        }

        e = persnOld.elements();
        while(e.hasMoreElements())
        {
            p = (Person) e.nextElement();

            if(!personen.containsKey(new Long(p.getID())))
            {
                grpOld.removePerson(p);

	            grpOld.delete(p,"personen");
            }
        }

    }
}