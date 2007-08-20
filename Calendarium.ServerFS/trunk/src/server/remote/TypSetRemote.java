package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import basisklassen.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
// TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote //
//////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the TypSetRemote-class.
 */
public class TypSetRemote extends UnicastRemoteObject implements interfaces.TypSetInterface
{
	private static final long serialVersionUID = -4064383403550404487L;

	// Server
    private Server server;

	// Daten
	private Hashtable defaultTyps;
    private Hashtable userTyps;
	private long lastID;

	/**
	 * Constructor of TypSetRemote-class
	 * @param s (Server object)
	 * @param name (String)
	 * @throws RemoteException
	 */
    public TypSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "TypSetRemote", this);

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
	 * This private function loads the last ID, 'defaultTyps' and
	 * 'userTyps' from file "...\typen.dat".
	 */
    private void load()
    {
        try
		{   FileInputStream istream = new FileInputStream("data/files/typen.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastID = ((Long) s.readObject()).longValue();
            defaultTyps = (Hashtable) s.readObject();
            userTyps = (Hashtable) s.readObject();

            istream.close();

        }
        catch(Exception e)
		{
			lastID = 0;
			defaultTyps = new Hashtable();
			userTyps = new Hashtable();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	/**
	 * This private function stores the last ID, 'defaultTyps' and
	 * 'userTyps' into file "...\typen.dat".
	 */
    private synchronized void save()
    {
        try
		{	FileOutputStream ostream = new FileOutputStream("data/files/typen.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastID));
			p.writeObject(defaultTyps);
			p.writeObject(userTyps);
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
     * This public function removes the specified person from
     * database.
     * @param person (Person object)
     */
    public void deletePerson(Person person)
    {
        Long id = new Long(person.getID());

        if(userTyps != null)
        {
            if(userTyps.containsKey(id))
            {
                userTyps.remove(id);
                save();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Hashtable ausgeben
    /**
     * get-function
     * @return Hashtable
     * @throws RemoteException
     */
    public Hashtable getDefaultTypen() throws RemoteException
    {
    	return defaultTyps;
    }

    // Hashtable fuer User ausgeben
    @SuppressWarnings("unchecked")
    /**
     * get-function
     * @param p (Person object)
     * @return Hashtable
     * @throws RemoteException
     */
	public Hashtable getUserTypen(Person p) throws RemoteException
    {
        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            Enumeration enumer = defaultTyps.keys();
            Hashtable typen = new Hashtable();

            while(enumer.hasMoreElements())
            {
                Long id = (Long) enumer.nextElement();

                if(!userHash.containsKey(id))
                {
                	typen.put(id, defaultTyps.get(id));
                }
                else
                {
                	typen.put(id, userHash.get(id));
                }
            }

            return typen;

        }
        else
        	return defaultTyps;
    }

    // Eintragstyp ausgeben
    /**
     * get-function
     * @param id (a variable of type 'long')
     * @return EintragsTyp
     * @throws RemoteException
     */
    public EintragsTyp getDefaultTypByID(long id) throws RemoteException
    {
        return (EintragsTyp) defaultTyps.get(new Long(id));
    }

    // Eintragstyp ausgeben
    /**
     * get-function
     * @param p (Person object)
     * @param i (a variable of type 'long')
     * @return EintragsTyp
     * @throws RemoteException
     */
    public EintragsTyp getUserTypByID(Person p, long i) throws RemoteException
    {
        Long id = new Long(i);

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            if(userHash.containsKey(id))
            {
            	return (EintragsTyp) userHash.get(id);
            }
        }
        return (EintragsTyp) defaultTyps.get(id);
    }

	// Eintragstyp ausgeben
    /**
     * get-function
     * @param p (Person object)
     * @param typ (EintragsTyp object)
     * @return EintragsTyp
     * @throws RemoteException
     */
    public EintragsTyp getUserTyp(Person p, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            if(userHash.containsKey(id))
            {
            	return (EintragsTyp) userHash.get(id);
            }
        }
        return (EintragsTyp) defaultTyps.get(id);
    }

    // Usertyp setzen
    @SuppressWarnings("unchecked")
    /**
     * set-function
     * @param p (Person object)
     * @param typ (EintragsTyp object)
     * @throws RemoteException
     */
	public void setUserTyp(Person p, EintragsTyp typ) throws RemoteException
    {
		Long id = new Long(typ.getID());

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash == null)
		{
			userHash = new Hashtable();
			userHash.put(id, typ);

			userTyps.put(new Long(p.getID()), userHash);

		}
        else
        	userHash.put(id, typ);

        save();
    }

    // Defaulttyp erstellen
    @SuppressWarnings("unchecked")
    /**
     * This public function creates a defaulttype with the
     * specified parameters.
     * @param kuerzel (String)
     * @param typ (EintragsTyp object)
     * @throws RemoteException
     */
	public void create(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        // LastID setzen
		lastID++;
		typ.setID(lastID);

		defaultTyps.put(new Long(lastID), typ);
        save();
    }

    // Defaulttyp löschen
    /**
     * This public function deletes the defaulttype that is
     * specified by the parameters.
     * @param kuerzel (String)
     * @param typ (EintragsTyp object)
     * @throws RemoteException
     */
    public void delete(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        Enumeration e = userTyps.elements();
        Hashtable userHash;

        while(e.hasMoreElements())
        {
            userHash = (Hashtable) e.nextElement();
            userHash.remove(id);
        }

        defaultTyps.remove(id);

        // Datenreinigung
        server.getRightSetRemote().deleteEintragsTyp(typ);

        save();
    }

    // Defaulttyp ändern
    @SuppressWarnings("unchecked")
    /**
     * This public function updates the existing defaulttype by
     * importing the new parameters.
     * @param kuerzel (String)
     * @param typ (EintragsTyp object)
     * @throws RemoteException
     */
	public void update(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        defaultTyps.put(id, typ);
		save();
    }
}