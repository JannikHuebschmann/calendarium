package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import basisklassen.*;

////////////////////////////////////////////////////////////////////////////////////////////
// RightSetRemote // RightSetRemote // RightSetRemote // RightSetRemote // RightSetRemote //
////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the RightSetRemote-class.
 */
public class RightSetRemote extends UnicastRemoteObject implements interfaces.RightSetInterface
{
	private static final long serialVersionUID = 2213132678786761889L;

	// Server
    private Server server;

	// Daten
	private Hashtable grantAtGroups = new Hashtable();
    private Hashtable grantAtPersons = new Hashtable();

    /**
     * Constructor of RightSetRemote-class
     * @param s
     * @param name
     * @throws RemoteException
     */
    public RightSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {
        	Naming.bind(name + "RightSetRemote", this);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }

        load();
    }

	///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
	/**
	 * This private function loads the 'grantAtGroups' and the
	 * 'grantAtPersons' from file "...\rechte.dat".
	 */
    private void load()
    {
        try
		{
        	FileInputStream istream = new FileInputStream("data/files/rechte.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			grantAtGroups = (Hashtable) s.readObject();
            grantAtPersons = (Hashtable) s.readObject();

            istream.close();

        }
        catch(Exception e)
		{
			grantAtGroups = new Hashtable();
			grantAtPersons = new Hashtable();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	/**
	 * This private function stores 'grantAtGroups' and
	 * 'grantAtPersons' in file "...\rechte.dat".
	 */
    private synchronized void save()
    {
        try
		{
        	FileOutputStream ostream = new FileOutputStream("data/files/rechte.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(grantAtGroups);
			p.writeObject(grantAtPersons);
			p.flush();

			ostream.close();
		}
        catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

	// User löschen
    /**
     * This public function eradicates the specified person from
     * 'grantAtGroups' and 'grantAtPersons'.
     * @param person (Person object)
     */
    public void deletePerson(Person person)
    {
        Long id = new Long(person.getID());
        boolean changed = false;

        if(grantAtGroups != null)
        {
            if(grantAtGroups.containsKey(id))
            {
            	grantAtGroups.remove(id);
                changed = true;
            }
        }

        if(grantAtPersons != null)
        {
            if(grantAtPersons.containsKey(id))
            {
            	grantAtPersons.remove(id);
                changed = true;
            }
        }

        Enumeration e = grantAtPersons.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Hashtable userHash = (Hashtable) grantAtPersons.get(pid);

            if(userHash.containsKey(id))
            {
                userHash.remove(id);
                if(userHash.size() == 0)
                	grantAtPersons.remove(pid);
                changed = true;
            }
        }

        if(changed)
        	save();
	}

	// Gruppe löschen
    /**
     * This public function eradicates the specified group from
     * 'grantAtGroups'.
     * @param gruppe (Gruppe object)
     */
	public void deleteGruppe(Gruppe gruppe)
    {
        Long id = new Long(gruppe.getID());
        boolean changed = false;

        Enumeration e = grantAtGroups.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Hashtable userHash = (Hashtable) grantAtGroups.get(pid);

            if(userHash.containsKey(id))
            {
                userHash.remove(id);
                if(userHash.size() == 0)
                	grantAtGroups.remove(pid);
                changed = true;

                if(!gruppe.getAdminFlag())
                	break;
            }
        }

        if(changed)
        	save();
	}

	// EintragsTyp löschen
	/**
	 * This public function deletes the entry-type (EintragsTyp).
	 * @param typ (EintragsTyp object)
	 */
	public void deleteEintragsTyp(EintragsTyp typ)
	{
	    Enumeration e, enumer;

	    // Gruppen
	    e = grantAtGroups.keys();
	    while(e.hasMoreElements())
	    {
	        Long personID = (Long) e.nextElement();
	        Hashtable groupHash = (Hashtable) grantAtGroups.get(personID);

	        enumer = groupHash.keys();
	        while(enumer.hasMoreElements())
	        {
	            Long id = (Long) enumer.nextElement();
	            Vector rechte = (Vector) groupHash.get(id);

	            int count = 0;
	            while(count < rechte.size())
	            {
	                Recht recht = (Recht) rechte.elementAt(count);

	                if(recht.getEintragsTyp().getID() == typ.getID())
	                {
	                	rechte.removeElement(recht);
	                }
	                else
	                	count++;
	            }
	            if(rechte.size() == 0)
	            	groupHash.remove(id);
	        }
	        if(groupHash.size() == 0)
	        	grantAtGroups.remove(personID);
	    }

	    // Personen
	    e = grantAtPersons.keys();
	    while(e.hasMoreElements())
	    {
	        Long personID = (Long) e.nextElement();
	        Hashtable personHash = (Hashtable) grantAtPersons.get(personID);

	        enumer = personHash.keys();
	        while(enumer.hasMoreElements())
	        {
	            Long id = (Long) enumer.nextElement();
	            Vector rechte = (Vector) personHash.get(id);

	            int count = 0;
	            while(count < rechte.size())
	            {
	                Recht recht = (Recht) rechte.elementAt(count);

	                if(recht.getEintragsTyp().getID() == typ.getID())
	                {
	                	rechte.removeElement(recht);
	                }
	                else
	                	count++;
	            }
	            if(rechte.size() == 0)
	            	personHash.remove(id);
	        }
	        if(personHash.size() == 0)
	        	grantAtPersons.remove(personID);
	    }

	    save();
	}

    //////////////////////////////////////////////////////////////////////////////////
    // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // fuer Gruppen vergebene Rechte
	/**
	 * get-function
	 * @param sender (Person object)
	 * @return Hashtable
	 * @throws RemoteException
	 */
    public Hashtable getGroupGrants(Person sender) throws RemoteException
    {
        Long id = new Long(sender.getID());

        if(grantAtGroups != null)
        {
        	return (Hashtable) grantAtGroups.get(id);
        }
        else
        	return null;
    }

    // fuer Personen vergebene Rechte
    /**
     * get-function
     * @param sender (Person object)
     * @return Hashtable
     * @throws RemoteException
     */
    public Hashtable getPersonGrants(Person sender) throws RemoteException
    {
        Long id = new Long(sender.getID());

        if(grantAtPersons != null)
        {
        	return (Hashtable) grantAtPersons.get(id);
        }
        else
        	return null;
    }

    // fuer eine Gruppe vergebene Rechte
    /**
     * get-function
     * @param sender (Person object)
     * @param empfänger (Gruppe object)
     * @return Vector
     * @throws RemoteException
     */
	public Vector getGrantsAt(Person sender, Gruppe empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    if(grantAtGroups != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

	        if(userGrant != null)
	        {
	        	return (Vector) userGrant.get(idE);
	        }
	    }

	    return null;
	}

    // fuer eine Person vergebene Rechte
	/**
	 * get-function
	 * @param sender (Person object)
	 * @param empfänger (Person object)
	 * @return Vector
	 * @throws RemoteException
	 */
	public Vector getGrantsAt(Person sender, Person empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    if(grantAtPersons != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

	        if(userGrant != null)
	        {
	        	return (Vector) userGrant.get(idE);
	        }
	    }

	    return null;
	}

    // Rechte einer Person erweitern
	@SuppressWarnings("unchecked")
	/**
	 * This public function extends the rights of a person.
	 * @param sender (Person object)
	 * @param empfänger (Person object)
	 * @param recht (Recht object)
	 * @throws RemoteException
	 */
	public void addGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

	    Vector rechte = (Vector) userGrant.get(idE);
	    rechte.addElement(recht);

	    save();
	}

    // Rechte einer Gruppe erweitern
	@SuppressWarnings("unchecked")
	/**
	 * This public function extends the rights of a group.
	 * @param sender (Person object)
	 * @param empfänger (Gruppe object)
	 * @param recht (Recht object)
	 * @throws RemoteException
	 */
	public void addGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

	    Vector rechte = (Vector) userGrant.get(idE);
	    rechte.addElement(recht);

	    save();
	}

    // Person ein Recht entziehen
	/**
	 * This public function deprives a person of its rights.
	 * @param sender (Person object)
	 * @param empfänger (Person object)
	 * @param recht (Recht object)
	 * @throws RemoteException
	 */
	public void retractGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException
    {
        Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    Vector rechte = (Vector) userGrant.get(idE);

	    Enumeration e = rechte.elements();
	    Recht r;

	    while(e.hasMoreElements())
	    {
	        r = (Recht) e.nextElement();

	        if(r.equals(recht))
	        {
	            rechte.removeElement(r);
	            break;
	        }
	    }

	    save();
    }

	// Gruppe ein Recht entziehen
	/**
	 * This public function deprives a group of its rights.
	 * @param sender (Person object)
	 * @param empfänger (Gruppe object)
	 * @param recht (Recht object)
	 * @throws RemoteException
	 */
	public void retractGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    Vector rechte = (Vector) userGrant.get(idE);

	    Enumeration e = rechte.elements();
	    Recht r;

	    while(e.hasMoreElements())
	    {
	        r = (Recht) e.nextElement();

	        if(r.equals(recht))
	        {
	            rechte.removeElement(r);
	            break;
	        }
	    }

	    save();
	}

	// Person berechtigen
	@SuppressWarnings("unchecked")
	/**
	 * This public function authorizes a person for something.
	 * @param sender (Person object)
	 * @param empfänger (Person object)
	 * @param rechte (Vector object)
	 * @throws RemoteException
	 */
	public void addReceiver(Person sender, Person empfänger, Vector rechte) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    if(userGrant == null)
	    {
	        userGrant = new Hashtable();
	        userGrant.put(idE, rechte);

	        grantAtPersons.put(idS, userGrant);

	    }
	    else
	    {
	        userGrant.put(idE, rechte);
	    }

	    save();
	}

    // Gruppe berechtigen
	@SuppressWarnings("unchecked")
	/**
	 * This public function authorizes a group for something.
	 * @param sender (Person object)
	 * @param empfänger (Gruppe object)
	 * @param rechte (Vector object)
	 * @throws RemoteException
	 */
	public void addReceiver(Person sender, Gruppe empfänger, Vector rechte) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    if(userGrant == null)
	    {
	        userGrant = new Hashtable();
	        userGrant.put(idE, rechte);

	        grantAtGroups.put(idS, userGrant);

	    }
	    else
	    {
	        userGrant.put(idE, rechte);
	    }

	    save();
	}

	// Person entfernen
	/**
	 * This public function removes a person, more precisely the
	 * receiver.
	 * @param sender (Person object)
	 * @param empfänger (Person object)
	 * @throws RemoteException
	 */
	public void deleteReceiver(Person sender, Person empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    userGrant.remove(idE);

	    if(userGrant.size() == 0)
	    {
	    	grantAtPersons.remove(idS);
	    }

	    save();
	}

    // Gruppe entfernen
	/**
	 * This public function deletes a group, more precisely the
	 * receiving group.
	 * @param sender (Person object)
	 * @param empfänger (Gruppe object)
	 * @throws RemoteException
	 */
	public void deleteReceiver(Person sender, Gruppe empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    userGrant.remove(idE);

	    if(userGrant.size() == 0)
	    {
	    	grantAtGroups.remove(idS);
	    }

	    save();
	}

	// Hashtable von Personen
	@SuppressWarnings("unchecked")
	/**
	 * get-function
	 * @param sender (Person object)
	 * @return Hashtable
	 * @throws RemoteException
	 */
	public Hashtable getGrantPersons(Person sender) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Hashtable result = new Hashtable();

	    if(grantAtPersons != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

    	    if(userGrant != null)
    	    {
    	        Enumeration e = userGrant.keys();
    	        while(e.hasMoreElements())
    	        {
    	            long id = ((Long) e.nextElement()).longValue();
    	            result.put(new Long(id), server.getPersonSetRemote().getByID(id));
    	        }
    	    }
	    }

	    return result;
	}

    // Hashtable von Gruppen
	@SuppressWarnings("unchecked")
	/**
	 * get-function
	 * @param sender (Person object)
	 * @return Hashtable
	 * @throws RemoteException
	 */
	public Hashtable getGrantGroups(Person sender) throws RemoteException
	{
		Long idS = new Long(sender.getID());
	    Hashtable result = new Hashtable();

	    if(grantAtGroups != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

    	    if(userGrant != null)
    	    {
    	        Enumeration e = userGrant.keys();
    	        while(e.hasMoreElements())
    	        {
    	            long id = ((Long) e.nextElement()).longValue();
    	            result.put(new Long(id), server.getGroupSetRemote().getByID(id));
    	        }
    	    }
	    }

	    return result;
	}

    //////////////////////////////////////////////////////////////////////////////////
    // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // Sämtliche erhaltene Rechte
    @SuppressWarnings("unchecked")
    /**
     * get-function
     * @param empfänger (Person object)
     * @result Hashtable
     * @throws RemoteException
     */
	public Hashtable getRechte(Person empfänger) throws RemoteException
    {
        Long receiverID = new Long(empfänger.getID());
		Hashtable getByPersons = new Hashtable();

		if(grantAtPersons != null)
		{
			// Rechte als Person
			Enumeration e = grantAtPersons.keys();

			while(e.hasMoreElements())
			{
				Long granterID = (Long) e.nextElement();

				if(granterID != receiverID)
				{
					Hashtable hash = (Hashtable) grantAtPersons.get(granterID);

					if(hash.containsKey(receiverID))
					{
						Vector rechte = (Vector) hash.get(receiverID);
						Person granter = server.getPersonSetRemote().getByID(granterID.longValue());

						getByPersons.put(granter, rechte);
					}
				}
			}
		}

		if(grantAtGroups != null)
		{
			// Rechte als Gruppenmitglied
			Enumeration e = grantAtGroups.keys();

			while(e.hasMoreElements())
			{
				Long granterID = (Long) e.nextElement();

				if(granterID != receiverID)
				{
					Hashtable hash = (Hashtable) grantAtGroups.get(granterID);

					Enumeration enumer = hash.keys();
					while(enumer.hasMoreElements())
					{
						Long gruppenID = (Long) enumer.nextElement();

						if(server.getGroupSetRemote().getByID(gruppenID.longValue()).isMember(empfänger))
						{
							Vector rechte = (Vector) hash.get(gruppenID);
							Person granter = server.getPersonSetRemote().getByID(granterID.longValue());

							if(!getByPersons.containsKey(granter))
							{
								getByPersons.put(granter, rechte);

							}
							else
							{
								Vector granted = (Vector) getByPersons.get(granter);

								Enumeration rechteEnum = rechte.elements();
								while(rechteEnum.hasMoreElements())
								{
									Recht recht = (Recht) rechteEnum.nextElement();
									boolean found = false;

									Enumeration grantedEnum = granted.elements();
									while(grantedEnum.hasMoreElements())
									{
										if(recht.equals((Recht) grantedEnum.nextElement()))
										{
											found = true;
											break;
										}
									}

									if(!found) granted.addElement(recht);
								}
							}
						}
					}
				}
			}
		}

		return getByPersons;
    }

    // Ist jemand bei jemanden berechtigt?
    /**
     * This public function checks if somebody is authorized at
     * someone else.
     * @param jemand (Person object)
     * @param bei (Person object)
     * @param recht (Recht object)
     * @return boolean
     * @throws RemoteException
     */
	public boolean isBerechtigt(Person jemand, Person bei, Recht recht) throws RemoteException
	{
	    Long jemandID = new Long(jemand.getID());
	    Long beiID = new Long(bei.getID());

        // Personen
	    if(grantAtPersons != null)
        {
            Hashtable personGrants = (Hashtable) grantAtPersons.get(beiID);

            if(personGrants != null)
            {
                Vector rechte = (Vector) personGrants.get(jemandID);

                if(rechte != null)
                {
                    Enumeration e = rechte.elements();
                    while(e.hasMoreElements())
                    {
                        Recht r = (Recht) e.nextElement();

                        // Rechtshierachie
                        if(recht.getEintragsTyp().getID() == r.getEintragsTyp().getID() &&
                           recht.getRechtsIndex() <= r.getRechtsIndex())

                        return true;
                    }
                }
            }
        }

        // Gruppen
        if(grantAtGroups != null)
        {
            Hashtable groupGrants = (Hashtable) grantAtGroups.get(beiID);

            if(groupGrants != null)
            {
                Enumeration e = groupGrants.keys();
                while(e.hasMoreElements())
                {
                    Long id = (Long) e.nextElement();

                    if(server.getGroupSetRemote().getByID(id.longValue()).isMember(jemand))
    			    {
    				    Vector rechte = (Vector) groupGrants.get(id);

    				    if(rechte != null)
                        {
                            Enumeration enumer = rechte.elements();
                            while(enumer.hasMoreElements())
                            {
                                Recht r = (Recht) enumer.nextElement();
                                if(r.equals(recht))
                                	return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
	}
}