/*
 * Created on 09.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import basisklassen.Eintrag;
import basisklassen.Person;

/**
 * @author MartiniDestroyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AntwortSetInterface extends Remote{
	
    
    void sendMessage(Eintrag eintrag,Vector vtn,String antwort)throws RemoteException;
    void sendMessage(Eintrag eintrag,Person pers,String antwort)throws RemoteException;
}
