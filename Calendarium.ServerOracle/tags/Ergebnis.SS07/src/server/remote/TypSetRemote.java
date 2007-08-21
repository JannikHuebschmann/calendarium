package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;

import server.Server;
import basisklassen.EintragsTyp;
import basisklassen.Person;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.DeleteCriteria;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;

//////////////////////////////////////////////////////////////////////////////////////////////////
// TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote // TypSetRemote //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class TypSetRemote extends UnicastRemoteObject implements interfaces.TypSetInterface
{
	// Server
    private Server server;

    private long rootID = -1;

	// Daten
  	private Hashtable defaultTyps = new Hashtable();
    private Hashtable userTyps = new Hashtable();
	private ClassMap map;

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
        map = ClassMappings.getClass("EintragsTyp");

        try
        {
            PersistentCriteria pc = new RetrieveCriteria("EintragsTyp","Admin");
            ResultSet res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                EintragsTyp t = new EintragsTyp(res.getLong(map.getAttribute("objectIdentifier").getColumn()));
                t.swap(res);

                if (t.getOwner() == -1)
                    defaultTyps.put(new Long(t.getID()),t);
                else
                {
                	Long id = new Long(t.getID());

                    Hashtable userHash = (Hashtable) userTyps.get(new Long(t.getOwner()));
                    if(userHash == null)
               	  	{
	  	                 	userHash = new Hashtable();
            		  	    userHash.put(id, t);

      			            userTyps.put(new Long(t.getOwner()), userHash);

            		    } else userHash.put(id, t);

                }
            }
        }catch (Exception s) {System.out.println(s+"T");}

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

                ClassMap map = ClassMappings.getClass("EintragsTyp");

                PersistentCriteria pc = new DeleteCriteria("EintragsTyp","Admin");
                pc.perform(pc.buildForObject() + pc.addSelectEqualTo("owner",new Long(person.getID())));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////


    // Hashtable ausgeben
    public Hashtable getDefaultTypen() throws RemoteException
    {
        return defaultTyps;
    }

    // Hashtable fuer User ausgeben
    public Hashtable getUserTypen(Person p) throws RemoteException
    {
        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            Enumeration enumeration = defaultTyps.keys();
            Hashtable typen = new Hashtable();

            while(enumeration.hasMoreElements())
            {
                Long id = (Long) enumeration.nextElement();

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
        Long id = null;
		if(typ!=null)id=new Long(typ.getID());

        Hashtable userHash = (Hashtable) userTyps.get(new Long(p.getID()));
        if(userHash != null)
        {
            if(userHash.containsKey(id))
            {   return (EintragsTyp) userHash.get(id);
            }
        }
        
        return (EintragsTyp) defaultTyps.get(id);//original
    }

    // Usertyp setzen
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

        typ.setOwner(p.getID());
        typ.setKz(p.getKuerzel());
        typ.save();
    }

    // Defaulttyp erstellen
    public void create(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        typ.setOwner(rootID);
        typ.setKz(kuerzel);
        typ.save();
        defaultTyps.put(new Long(typ.getID()), typ);        
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

        typ.setOwner(rootID);
        typ.setKz(kuerzel);
        typ.delete();

        // Datenreinigung
        server.getRightSetRemote().deleteEintragsTyp(typ);
    }

    // Defaulttyp ändern
    public void update(String kuerzel, EintragsTyp typ) throws RemoteException
    {
        Long id = new Long(typ.getID());

        

        typ.setOwner(rootID);
        typ.setKz(kuerzel);
        typ.save();
        defaultTyps.put(id, typ);
    }
}