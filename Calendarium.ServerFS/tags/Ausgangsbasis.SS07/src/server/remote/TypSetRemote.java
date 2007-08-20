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

public class TypSetRemote extends UnicastRemoteObject implements interfaces.TypSetInterface
{
	private static final long serialVersionUID = -4064383403550404487L;

	// Server
    private Server server;

	// Daten
	private Hashtable defaultTyps;
    private Hashtable userTyps;
	private long lastID;

    public TypSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "TypSetRemote", this);

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
		{   FileInputStream istream = new FileInputStream("data/files/typen.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastID = ((Long) s.readObject()).longValue();
            defaultTyps = (Hashtable) s.readObject();
            userTyps = (Hashtable) s.readObject();

            istream.close();

        } catch(Exception e)
		{
			lastID = 0;
			defaultTyps = new Hashtable();
			userTyps = new Hashtable();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
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

		} catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

    // User löschen
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
    public Hashtable getDefaultTypen() throws RemoteException
    {   return defaultTyps;
    }

    // Hashtable fuer User ausgeben
    @SuppressWarnings("unchecked")
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
                {   typen.put(id, defaultTyps.get(id));
                } else
                {   typen.put(id, userHash.get(id));
                }
            }

            return typen;

        } else return defaultTyps;
    }

    // Eintragstyp ausgeben
    public EintragsTyp getDefaultTypByID(long id) throws RemoteException
    {
        return (EintragsTyp) defaultTyps.get(new Long(id));
    }

    // Eintragstyp ausgeben
    public EintragsTyp getUserTypByID(Person p, long i) throws RemoteException
    {
        Long id = new Long(i);

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            if(userHash.containsKey(id))
            {   return (EintragsTyp) userHash.get(id);
            }
        }
        return (EintragsTyp) defaultTyps.get(id);
    }

	// Eintragstyp ausgeben
    public EintragsTyp getUserTyp(Person p, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            if(userHash.containsKey(id))
            {   return (EintragsTyp) userHash.get(id);
            }
        }
        return (EintragsTyp) defaultTyps.get(id);
    }

    // Usertyp setzen
    @SuppressWarnings("unchecked")
	public void setUserTyp(Person p, EintragsTyp typ) throws RemoteException
    {
		Long id = new Long(typ.getID());

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash == null)
		{
			userHash = new Hashtable();
			userHash.put(id, typ);

			userTyps.put(new Long(p.getID()), userHash);

		} else userHash.put(id, typ);

        save();
    }

    // Defaulttyp erstellen
    @SuppressWarnings("unchecked")
	public void create(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        // LastID setzen
		lastID++;
		typ.setID(lastID);

		defaultTyps.put(new Long(lastID), typ);
        save();
    }

    // Defaulttyp löschen
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
	public void update(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        defaultTyps.put(id, typ);
		save();
    }
}