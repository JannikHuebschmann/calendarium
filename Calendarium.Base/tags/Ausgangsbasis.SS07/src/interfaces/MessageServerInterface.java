package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

import basisklassen.Person;
import event.CalendariumListener;

public interface MessageServerInterface extends Remote
{
    // Add Listener
    public void addCalendariumListenerTo(CalendariumListener l, Person p) throws RemoteException;
    
    // Connect
    public Object connect(String kuerzel, String pwd) throws RemoteException;

	// Disconnect
    public void disconnect(Person person) throws RemoteException;
    
    // AdminMessage
    public void sendAdminMessage(String text) throws RemoteException;
    
    // ShutDown
    public void shutDown() throws RemoteException;
    
	// Logged Users
    public Hashtable getConnections() throws RemoteException;
}