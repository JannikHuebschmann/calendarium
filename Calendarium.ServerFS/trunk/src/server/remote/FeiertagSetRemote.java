package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.Server;
import basisklassen.*;

//////////////////////////////////////////////////////////////////////////////////////
// FeiertagSetRemote // FeiertagSetRemote // FeiertagSetRemote // FeiertagSetRemote //
//////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the FeiertagSetRemote-class.
 */
public class FeiertagSetRemote extends UnicastRemoteObject implements interfaces.FeiertagSetInterface
{
	private static final long serialVersionUID = 558189946733491705L;

	// Server
	@SuppressWarnings("unused")
	private Server server;

	// Daten
	private long lastID;
    private Hashtable feiertage;

    /**
     * Constructor of FeiertagSetRemote-class
     * @param s (Server object)
     * @param name (String)
     * @throws RemoteException
     */
    public FeiertagSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {
        	Naming.bind(name + "FeiertagSetRemote", this);
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
	 * This private function loads the last ID and the holidays from
	 * "...\feiertage.dat".
	 */
    private void load()
    {
        try
		{   FileInputStream istream = new FileInputStream("data\\files\\feiertage.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastID = ((Long) s.readObject()).longValue();
            feiertage = (Hashtable) s.readObject();

            istream.close();

        }
        catch(Exception e)
		{
			lastID = 0;
			feiertage = new Hashtable();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
    /**
     * This private function saves the last ID and the current
     * holidays to "...\feiertage.dat".
     */
    private synchronized void save()
    {
        try
		{	FileOutputStream ostream = new FileOutputStream("data\\files\\feiertage.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastID));
			p.writeObject(feiertage);
			p.flush();

			ostream.close();

		}
        catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

	///////////////////////////////////////////////////////////////////////////////////
    // Sort // Sort //  Sort // Sort // Sort // Sort // Sort // Sort // Sort // Sort //
    ///////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    /**
     * This private function sorts the given Vector 'tage' which
     * contains different days.
     * @param tage (Vector object)
     * @return Vector (a sorted Vector of days)
     */
	private Vector sort(Vector tage)
    {
        // Selection Sort // Selection Sort // Selection Sort // Selection Sort //

        int size = tage.size();
        int max;
        Feiertag tag;
        Feiertag t;

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
    @SuppressWarnings("unchecked")
    /**
     * get-function
     * @return Vector (a sorted Vector containing the holidays)
     */
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

        } 
        else
       	{
        	return null;
        }
    }

    // Hashtable ausgeben
    /**
     * get-function
     * @return Hashtable (that imports the holidays)
     */
    public Hashtable getAllFeiertage() throws RemoteException
    {   
    	return feiertage;
    }

    // Feiertag ausgeben
    /**
     * get-function
     * @param cal (GregorianCalendar object)
     * @return Feiertag (the returnvalue is a Feiertag-object if
     * 			the given date is a holiday; otherwise it's NULL)
     */
    public Feiertag getFeiertagByDate(GregorianCalendar cal) throws RemoteException
    {
        String dateString = cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." +
                            cal.get(Calendar.YEAR);

        if(feiertage != null)
        {   
        	return (Feiertag) feiertage.get(dateString);
        } 
        else 
        {
        	return null;
        }
    }

    // Feiertag ausgeben
    /**
     * get-function
     * @param date (Date object)
     @return Feiertag (the returnvalue is a Feiertag-object if
     * 			the given date is a holiday; otherwise it's NULL)
     */
    public Feiertag getFeiertagByDate(Datum date) throws RemoteException
    {
        if(feiertage != null)
        {   
        	return (Feiertag) feiertage.get(date.getDate());
        } 
        else 
        {
        	return null;
       	}
    }

    // Ist Feiertag?
    /**
     * This public function checks if the given date is a holiday.
     * @param cal (GregorianCalendar object)
     * @return boolean (true if the date is a holiday, otherwise
     * 			false is returned)
     */
    public boolean isFeiertag(GregorianCalendar cal) throws RemoteException
    {
        String dateString = cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH) + 1) + "." +
                            cal.get(Calendar.YEAR);

        if(feiertage != null)
        {   
        	return feiertage.containsKey(dateString);
        }
        else
       	{
        	return false;
        }
    }

    // Feiertag anlegen
    @SuppressWarnings("unchecked")
    /**
     * This public function attaches a new holiday in the library.
     * @param kuerzel (String)
     * @param tag (Feiertag object)
     */
	public void create(String kuerzel, Feiertag tag) throws RemoteException
    {
		// LastID setzen
		lastID++;
		tag.setID(lastID);

        feiertage.put(tag.getDateString(), tag);
        save();
    }

    // Feiertag löschen
    /**
     * This public function removes the specified holiday from the
     * library.
     * @param kuerzel (String)
     * @param tag (Feiertag object)
     */
    public void delete(String kuerzel, Feiertag tag) throws RemoteException
    {
		feiertage.remove(tag.getDateString());
        save();
    }

    // Feiertag updaten
    @SuppressWarnings("unchecked")
    /**
     * This public function checks if there is a holiday with
     * same ID as the one that is specified by 'tag'. If it is, the
     * day is removed from library and replaced by the new one.
     * @param kuerzel (String)
     * @param tag (Feiertag object)
     */
	public void update(String kuerzel, Feiertag tag) throws RemoteException
    {
		Enumeration e = feiertage.elements();
        Feiertag f;

        while(e.hasMoreElements())
        {
            f = (Feiertag) e.nextElement();
            if(f.getID() == tag.getID())
            {
                feiertage.remove(f.getDateString());
                feiertage.put(tag.getDateString(), tag);
                break;
            }
        }

        save();
    }
}