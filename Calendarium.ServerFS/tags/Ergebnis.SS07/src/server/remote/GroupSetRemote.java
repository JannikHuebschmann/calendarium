package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import basisklassen.*;

////////////////////////////////////////////////////////////////////////////////////////////
// GroupSetRemote // GroupSetRemote // GroupSetRemote // GroupSetRemote // GroupSetRemote //
////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the GroupSetRemote-class.
 */
public class GroupSetRemote extends UnicastRemoteObject implements interfaces.GroupSetInterface
{
	private static final long serialVersionUID = -1807799861943338840L;

	// RootID
    private final Long rootID = new Long(0);

    // Server
	private Server server;

	// Daten
	private long lastID;
    private Hashtable groupHash;    // Hashtable aller Gruppen

    /**
     * Constructor of GroupSetRemote-class
     * @param s (Server object)
     * @param name (String)
     * @throws RemoteException
     */
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
	/**
	 * This private function loads the last ID and the group-infor-
	 * mation from a hashtable located in file "...\gruppen.dat".
	 */
    private void load()
    {
        try
		{   FileInputStream istream = new FileInputStream("data/files/gruppen.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastID = ((Long) s.readObject()).longValue();
            groupHash = (Hashtable) s.readObject();

            istream.close();

        }
        catch(Exception e)
		{
			lastID = 0;
			groupHash = new Hashtable();
		}
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	/**
	 * This private function stores the last ID and the group-infor-
	 * mation in the file "...\gruppen.dat".
	 */
    private synchronized void save()
    {
        try
		{	FileOutputStream ostream = new FileOutputStream("data/files/gruppen.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastID));
			p.writeObject(groupHash);
			p.flush();

			ostream.close();

		}
        catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

    /**
     * This private function is a recursive one. It removes the
     * subgroup-elements of the group whose AdminFlag is not set.
     * @param gruppen (Hashtable)
     */
    private void deleteSubGroups(Hashtable gruppen)
	{
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe gruppe = (Gruppe) e.nextElement();

				if(!gruppe.getAdminFlag())
				{
					groupHash.remove(new Long(gruppe.getID()));
				}

				deleteSubGroups(gruppe.getGruppen());
			}
		}
	}

    // User löschen
    /**
     * This public function removes all entries that refer to the
     * specified person.
     * @param person (Person object)
     */
    public void deletePerson(Person person)
    {
        Long personID = new Long(person.getID() * (-1));

        if(groupHash != null)
        {
            // Gruppe der Person löschen
            Gruppe gruppe = (Gruppe) groupHash.get(personID);
            if(gruppe != null)
            {
                deleteSubGroups(gruppe.getGruppen());
                groupHash.remove(personID);
            }

            // Person als Gruppenmitglied löschen
            Enumeration e = groupHash.keys();
            while(e.hasMoreElements())
            {
                Long id = (Long) e.nextElement();
                if(id.longValue() <= 0)
                {
                    gruppe = (Gruppe) groupHash.get(id);
                    gruppe.deletePerson(person);
                }
            }

            save();
        }
    }

	///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // AdminRootGruppe ausgeben
	@SuppressWarnings("unchecked")
	/**
	 * get-function
	 * @return Gruppe
	 * @throws RemoteException
	 */
	public Gruppe getAdminRoot() throws RemoteException
    {
        Gruppe gruppe = (Gruppe) groupHash.get(rootID);

        if(gruppe == null)
        {
            gruppe = new Gruppe(rootID.longValue(), true, "ROOT", "Admin-Gruppen");
            groupHash.put(rootID, gruppe);
        }

        return gruppe;
    }

    // UserRootGruppe ausgeben
    @SuppressWarnings("unchecked")
    /**
     * get-function
     * @param person (Person object)
     * @return Gruppe
     * @throws RemoteException
     */
	public Gruppe getUserRoot(Person person) throws RemoteException
    {
        Long persID = new Long(person.getID() * (-1));
        Gruppe gruppe = (Gruppe) groupHash.get(persID);

        if(gruppe == null)
        {
            gruppe = new Gruppe(persID.longValue(), true, "EG", "Eigene Gruppen");
            groupHash.put(persID, gruppe);
        }

        return gruppe;
    }

	// Gruppe by ID
    /**
     * get-function
     * @param id (a variable of type 'long')
     * @return Gruppe
     * @throws RemoteException
     */
	public Gruppe getByID(long id) throws RemoteException
	{
		return (Gruppe) groupHash.get(new Long(id));
	}

    // Kuerzel vergeben?
	/**
	 * This public function checks if the specified String is
	 * already existing in the database.
	 * @param kuerzel (String)
	 * @return boolean
	 * @throws RemoteException
	 */
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
	/**
	 * This public function resets the order of the given groups.
	 * @param kuerzel (String)
	 * @param gruppe (Gruppe object)
	 * @param parentOld (Gruppe object)
	 * @param parentNew (Gruppe object)
	 * @throws RemoteException
	 */
	public void exchangeOrder(String kuerzel, Gruppe gruppe, Gruppe parentOld, Gruppe parentNew) throws RemoteException
	{
	    gruppe = (Gruppe) groupHash.get(new Long(gruppe.getID()));

	    parentOld = (Gruppe) groupHash.get(new Long(parentOld.getID()));
	    parentNew = (Gruppe) groupHash.get(new Long(parentNew.getID()));

	    parentOld.removeGruppe(gruppe);
	    parentNew.addGruppe(gruppe);

	    save();
	}

	// Gruppe anlegen
	@SuppressWarnings("unchecked")
	/**
	 * This public function builds up a new group using the specified
	 * objects.
	 * @param user (Person object)
	 * @param gruppe (Gruppe object)
	 * @param parent (Gruppe object)
	 */
	public void createGroupIn(Person user, Gruppe gruppe, Gruppe parent)
	{
	    parent = (Gruppe) groupHash.get(new Long(parent.getID()));

	    // LastID setzen
		lastID++;
		gruppe.setID(lastID);

		groupHash.put(new Long(lastID), gruppe);

		parent.addGruppe(gruppe);
		save();
	}

	// Gruppe löschen
	/**
	 * This public function deletes the group of the person. The
	 * person can be part of the user-group or part of the admin-
	 * group.
	 * @param user (Person object)
	 * @param gruppe (Gruppe object)
	 * @param parent (Gruppe object)
	 */
	public void deleteGroupOf(Person user, Gruppe gruppe, Gruppe parent)
	{
	    Long groupID = new Long(gruppe.getID());

	    parent = (Gruppe) groupHash.get(new Long(parent.getID()));
	    parent.removeGruppe(gruppe);

	    if(gruppe.getAdminFlag())
	    {
	        Gruppe adminGruppe = (Gruppe) groupHash.get(rootID);
	        if(!adminGruppe.isMember(gruppe))
	        {
	            Enumeration e = groupHash.keys();
                while(e.hasMoreElements())
                {
                    Long id = (Long) e.nextElement();
                    if(id.longValue() < 0)
                    {
                        Gruppe g = (Gruppe) groupHash.get(id);
                        g.deleteGruppe(gruppe);
                    }
                }

                groupHash.remove(groupID);
            }
	    }
	    else
	    {
	        Long persID = new Long(user.getID() * (-1));

            Gruppe userGruppe = (Gruppe) groupHash.get(persID);
            if(!userGruppe.isMember(gruppe))
            {
                groupHash.remove(groupID);
            }
	    }

	    // Datenreinigung
        server.getRightSetRemote().deleteGruppe(gruppe);

	    save();
	}

	// Gruppe ändern
	/**
	 * This public function updates the information of the given
	 * user. Only if information changed, the information is stored.
	 * @param user (Person object)
	 * @param gruppe (Gruppe object)
	 */
	public void update(Person user, Gruppe gruppe)
	{
	    boolean changed = false;
	    Gruppe grpOld = (Gruppe) groupHash.get(new Long(gruppe.getID()));

	    // Name
	    if(grpOld!=null&&grpOld.getName().compareTo(gruppe.getName()) != 0)
	    {
	        grpOld.setName(gruppe.getName());
	        changed = true;
	    }

        // Kuerzel
        if (grpOld!=null&&grpOld.getKuerzel().compareTo(gruppe.getKuerzel()) != 0)
        {
            grpOld.setKuerzel(gruppe.getKuerzel());
            changed = true;
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
                changed = true;
            }
        }

        e = grpnOld.elements();
        while(e.hasMoreElements())
        {
            g = (Gruppe) e.nextElement();

            if(!gruppen.containsKey(new Long(g.getID())))
            {
                grpOld.removeGruppe(g);
                changed = true;
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
                changed = true;
            }
        }

        e = persnOld.elements();
        while(e.hasMoreElements())
        {
            p = (Person) e.nextElement();

            if(!personen.containsKey(new Long(p.getID())))
            {
                grpOld.removePerson(p);
                changed = true;
            }
        }

        if(changed) save();
    }
}