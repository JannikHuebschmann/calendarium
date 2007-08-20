/*
 * Created on 08.03.2005
 *
 */
package server.remote;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import server.Server;
import basisklassen.Eintrag;
import basisklassen.Person;
import basisklassen.Teilnehmer;
import event.MessageEvent;

///////////////////////////////////////////////////////////////////
// AntwortSetRemote // AntwortSetRemote // AntwortSetRemote ///////
///////////////////////////////////////////////////////////////////

/**
 * @author MartiniDestroyer, edited by K. Vahling
 * This is the class-declaration of the AntwortSetRemote-class.
 */
public class AntwortSetRemote extends UnicastRemoteObject implements interfaces.AntwortSetInterface{
	private static final long serialVersionUID = -4423762077611668055L;
	private String filepath="data/files/antwort.dat";
	public Vector teilnehmer;
	@SuppressWarnings("unused")
	private Eintrag eintrag;
	public String antwort;
	private Server server;
	private Hashtable terminIDs;
	
	/**
	 * Constructor of AntwortSetRemote-class
	 * @param s (server object)
	 * @param name (String)
	 * @throws RemoteException
	 */
	public AntwortSetRemote(Server s, String name)throws RemoteException
	{
		server=s;
		
		
		try 
		{
			Naming.bind(name+"AntwortSetRemote",this);
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (AlreadyBoundException e)
		{
			e.printStackTrace();
		}
		load();
		
		server.getNfktQueue().createNotifiers(terminIDs);
		
	}
	
	/**
	 * This public function loads the content of a file whose path
	 * is stored in 'filepath' and discards it to different variables.
	 */
	public void load() 
    {
        try
        {           
        	FileInputStream istream = new FileInputStream(filepath);
            ObjectInputStream s = new ObjectInputStream(istream);
            
            terminIDs=(Hashtable)s.readObject();
            teilnehmer=(Vector) s.readObject();
            antwort=(String) s.readObject();
            istream.close();

        } 
        catch(Exception e)
        {//wenn file leer is           
            terminIDs=new Hashtable();
        }
    }//load

    /**
     * This public function saves the values stored in different
     * variables to a file which is located under 'filepath'.
     */
    public synchronized void save()
    {
        try
        {       
        	FileOutputStream ostream = new FileOutputStream(filepath);
            ObjectOutputStream p = new ObjectOutputStream(ostream);

            p.writeObject(terminIDs);
            p.writeObject(teilnehmer);
            p.writeObject(antwort);
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
     * This public function sends the string named 'antwort' to the
     * server using different events. The number of the events is equal
     * to the number of vector-elements.
     * @param eintrag (Eintrag object)
     * @param vtn (Vector)
     * @param antwort (String including an answer)
     */
    public void sendMessage(Eintrag eintrag,Vector vtn,String antwort)
    {
    	Enumeration e=vtn.elements();
    	while(e.hasMoreElements())
    	{
    		Teilnehmer tn=(Teilnehmer)e.nextElement();
    		MessageEvent evt=new MessageEvent(eintrag.getOwner(),(Person)tn.getTeilnehmer(),antwort,"Rueckantwort");
    		server.getMessageServer().addEvent(evt);
    	}
    }

    /**
     * The following public function is without function because
     * it is empty and contains no functionality.
     */
	/* (non-Javadoc)
	 * @see interfaces.AntwortSetInterface#sendMessage(basisklassen.Eintrag, basisklassen.Person, java.lang.String)
	 */
	public void sendMessage(Eintrag eintrag, Person pers, String antwort) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
