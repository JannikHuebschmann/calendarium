/*
 * Created on 08.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
import dblayer.ClassMap;
import dblayer.ClassMappings;
import event.MessageEvent;

/**
 * @author Johnny Theuermann
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AntwortSetRemote extends UnicastRemoteObject implements interfaces.AntwortSetInterface{
	private String filepath="data/files/antwort.dat";
	private ClassMap map;
	public Vector teilnehmer;
	private Eintrag eintrag;
	public String antwort;
	private Server server;
	private Hashtable terminIDs;
	
	public AntwortSetRemote(Server s, String name)throws RemoteException{
		server=s;
		
		
		try {
			Naming.bind(name+"AntwortSetRemote",this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load();
		
		server.getNfktQueue().createNotifiers(terminIDs);
		
	}
	
	public void load() 
    {
        try{
        	map = ClassMappings.getClass("Antwort");
        	FileInputStream istream = new FileInputStream(filepath);
            ObjectInputStream s = new ObjectInputStream(istream);
            
            terminIDs=(Hashtable)s.readObject();
            teilnehmer=(Vector) s.readObject();
            antwort=(String) s.readObject();
            istream.close();

        } 
        catch(Exception e){//wenn file leer is           
            terminIDs=new Hashtable();
        }
    }//load

        
    public synchronized void save(){
        try{       
        	FileOutputStream ostream = new FileOutputStream(filepath);
            ObjectOutputStream p = new ObjectOutputStream(ostream);

            p.writeObject(terminIDs);
            p.writeObject(teilnehmer);
            p.writeObject(antwort);
            p.flush();

            ostream.close();

                
        } 
        catch(IOException e){
            e.printStackTrace();
            System.exit(0);               
        }
    }
    
    public void sendMessage(Eintrag eintrag,Vector vtn,String antwort){
    	Enumeration e=vtn.elements();
    	while(e.hasMoreElements()){
    		Teilnehmer tn=(Teilnehmer)e.nextElement();
    		MessageEvent evt=new MessageEvent(eintrag.getOwner(),(Person)tn.getTeilnehmer(),antwort,"Rueckantwort");
    		server.getMessageServer().addEvent(evt);
    	}
    }

	/* (non-Javadoc)
	 * @see interfaces.AntwortSetInterface#sendMessage(basisklassen.Eintrag, basisklassen.Person, java.lang.String)
	 */
	public void sendMessage(Eintrag eintrag, Person pers, String antwort) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
