package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import basisklassen.Person;

public interface PersonSetInterface extends Remote
{
    public Vector getOrderedList() throws RemoteException;

	public Person getByKuerzel(String kuerzel) throws RemoteException;

	public Person getByID(long id) throws RemoteException;

	public boolean contains(String kuerzel) throws RemoteException;

	public void create(String kuerzel, Person person) throws RemoteException;

	public void delete(String kuerzel, Person person) throws RemoteException;

	public void update(String kuerzel, Person person) throws RemoteException;
}