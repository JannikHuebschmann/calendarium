package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import server.Server;
import basisklassen.Datum;
import basisklassen.Feiertag;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;

//////////////////////////////////////////////////////////////////////////////////////
// FeiertagSetRemote // FeiertagSetRemote // FeiertagSetRemote // FeiertagSetRemote //
//////////////////////////////////////////////////////////////////////////////////////

public class FeiertagSetRemote extends UnicastRemoteObject implements interfaces.FeiertagSetInterface
{
    // Server
	private Server server;

	// Daten
    private Hashtable feiertage = new Hashtable();
    private ClassMap map;

    public FeiertagSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "FeiertagSetRemote", this);

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
        map = ClassMappings.getClass("Feiertag");
        try
        {
            PersistentCriteria pc = new RetrieveCriteria("Feiertag","Admin");
            ResultSet res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                Feiertag f = new Feiertag(res.getLong(map.getAttribute("objectIdentifier").getColumn()));
                f.swap(res);
                feiertage.put(f.getDateString(),f);

            }
        }catch (Exception s) {System.out.println(s);}
    }


	///////////////////////////////////////////////////////////////////////////////////
    // Sort // Sort //  Sort // Sort // Sort // Sort // Sort // Sort // Sort // Sort //
    ///////////////////////////////////////////////////////////////////////////////////
    private Vector sort(Vector tage)
    {
        // Selection Sort // Selection Sort // Selection Sort // Selection Sort //

        int size = tage.size(), max;
        Feiertag tag, t;

        for(int i = 0; i < size - 1; i++)
        {
            tag = (Feiertag) tage.elementAt(i);
            max = i;

            for(int j = i + 1; j < size; j++)
            {
                t = (Feiertag) tage.elementAt(j);
                if(tag.getDate().before(t.getDate()))
                {
                    tag = t;
                    max = j;
                }
            }

            if(max != i) // tauschen
            {
                tage.setElementAt(tage.elementAt(i), max);
                tage.setElementAt(tag, i);
            }
        }

        return tage;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Feiertage geordnet ausgeben
    public Vector getFeiertage() throws RemoteException
    {
        if(feiertage != null)
        {
            Vector tage = new Vector();

            Enumeration e = feiertage.elements();
            while(e.hasMoreElements())
            {
                tage.addElement((Feiertag) e.nextElement());
            }

            return sort(tage);

        } else return null;
    }

    // Hashtable ausgeben
    public Hashtable getAllFeiertage() throws RemoteException
    {   return feiertage;
    }

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(GregorianCalendar cal) throws RemoteException
    {
        String dateString = cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." +
                            cal.get(Calendar.YEAR);//original

        if(feiertage != null)
        {   return (Feiertag) feiertage.get(dateString);

        } else return null;
    }

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(Datum date) throws RemoteException
    {
        if(feiertage != null)
        {   return (Feiertag) feiertage.get(date.getDate());

        } else return null;
    }

    // Ist Feiertag?
    public boolean isFeiertag(GregorianCalendar cal) throws RemoteException
    {
        String dateString = cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." +
                            cal.get(Calendar.YEAR);

        if(feiertage != null)
        {   return feiertage.containsKey(dateString);

        } else return false;
    }

    // Feiertag anlegen
    public void create(String kuerzel, Feiertag tag) throws RemoteException
    {       
    	
        tag.setKz(kuerzel);
        tag.save();
        feiertage.put(tag.getDateString(), tag);
    }

    // Feiertag löschen
    public void delete(String kuerzel, Feiertag tag) throws RemoteException
    {
        feiertage.remove(tag.getDateString());

        tag.setKz(kuerzel);
        tag.delete();

    }

    // Feiertag updaten
    public void update(String kuerzel, Feiertag tag) throws RemoteException
    {
		Enumeration e = feiertage.elements();
		
		
        Feiertag f;
        	
        tag.setKz(kuerzel);
        tag.save();
        
        while(e.hasMoreElements())
        {
            f = (Feiertag) e.nextElement();
            if(f.getID() == tag.getID())
            {
            	feiertage.put(tag.getDateString(), tag);
                feiertage.remove(f.getDateString());
                
                break;
            }
        }
        

    }
}