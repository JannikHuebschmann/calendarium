/*
 * Created on 23.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package server.remote;

import interfaces.DBSetInterface;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dblayer.DBInit;

import server.Server;

/**
 * @author Johnnys
 *
 * to make the delete and create from tables with the monitor
 * it is also used by the monitor to know which server is running (FS or DB)
 * if the monitor couldn't lookup to DBSetRemote he knows its the fileserver
 */
public class DBSetRemote extends UnicastRemoteObject implements DBSetInterface{

	boolean dbserverstartet =false;
	private Server s;
	/**
	 * @param server
	 * @param name
	 */
	public DBSetRemote(Server server, String name) throws RemoteException{
		
		s=server;
		try {
			Naming.bind(name+"DBSetRemote",this);
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
	}

	/* 
	 */
	public boolean isStarted() throws RemoteException {
		
		dbserverstartet = true;
		return dbserverstartet;
		
	}

	/* the tables in the db are new created. all datas will lost except "feiertag"
	 */
	public void createTables() throws RemoteException {
		DBInit dbInit = new DBInit();
        dbInit.createTables();
		
	}

	/* to delete the tables at the db except "feiertag" because in DBInit "feiertag is not included
	 */
	public void deleteTables() throws RemoteException {
		DBInit dbInit = new DBInit();
        dbInit.deleteTables();
	}

	
	
	

}
