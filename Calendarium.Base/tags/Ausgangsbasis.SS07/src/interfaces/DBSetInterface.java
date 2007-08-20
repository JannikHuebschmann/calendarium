/*
 * Created on 23.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author MartiniDestroyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface DBSetInterface extends Remote{

	boolean isStarted() throws RemoteException;
	void createTables() throws RemoteException;
	void deleteTables() throws RemoteException;
}
