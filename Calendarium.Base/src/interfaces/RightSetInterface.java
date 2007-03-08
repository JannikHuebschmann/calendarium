package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

import basisklassen.Gruppe;
import basisklassen.Person;
import basisklassen.Recht;

public interface RightSetInterface extends Remote
{   
    //////////////////////////////////////////////////////////////////////////////////
    // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte //
    //////////////////////////////////////////////////////////////////////////////////
    
    // fuer Gruppen vergebene Rechte
    public Hashtable getGroupGrants(Person sender) throws RemoteException;
    
    // fuer Personen vergebene Rechte
    public Hashtable getPersonGrants(Person sender) throws RemoteException;
    
    // fuer eine Gruppe vergebene Rechte
	public Vector getGrantsAt(Person sender, Gruppe empfänger) throws RemoteException;
    
    // fuer eine Person vergebene Rechte
	public Vector getGrantsAt(Person sender, Person empfänger) throws RemoteException;
    
    // Rechte einer Person erweitern
	public void addGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException;
    
    // Rechte einer Gruppe erweitern
	public void addGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException;
    
    // Person ein Recht entziehen
	public void retractGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException;

	// Gruppe ein Recht entziehen
	public void retractGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException;
	
	// Person berechtigen
	public void addReceiver(Person sender, Person empfänger, Vector rechte) throws RemoteException;
    
    // Gruppe berechtigen
	public void addReceiver(Person sender, Gruppe empfänger, Vector rechte) throws RemoteException;
	
	// Person entfernen
	public void deleteReceiver(Person sender, Person empfänger) throws RemoteException;
    
    // Gruppe entfernen
	public void deleteReceiver(Person sender, Gruppe empfänger) throws RemoteException;
    
	// Hashtable von Personen
	public Hashtable getGrantPersons(Person sender) throws RemoteException;

	// Hashtable von Gruppen
	public Hashtable getGrantGroups(Person sender) throws RemoteException;

    //////////////////////////////////////////////////////////////////////////////////
    // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte //
    //////////////////////////////////////////////////////////////////////////////////
    
    // Sämtliche erhaltenen Rechte
    public Hashtable getRechte(Person empfänger) throws RemoteException;
    
    // Ist jemand bei jemanden berechtigt?
	public boolean isBerechtigt(Person jemand, Person bei, Recht recht) throws RemoteException;
}