package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;

import basisklassen.Gruppe;
import basisklassen.Person;

public interface GroupSetInterface extends Remote
{
    // AdminRootGruppe ausgeben
	public Gruppe getAdminRoot() throws RemoteException;

    // UserRootGruppe ausgeben
    public Gruppe getUserRoot(Person person) throws RemoteException;

	// Gruppe by ID
	public Gruppe getByID(long id) throws RemoteException;

    // Kuerzel vergeben?
	public boolean contains(String kuerzel) throws RemoteException;

	// Gruppen neu verketten
	public void exchangeOrder(String kuerzel, Gruppe gruppe, Gruppe parentOld, Gruppe parentNew) throws RemoteException;

	// Gruppe anlegen
	public void createGroupIn(Person user, Gruppe gruppe, Gruppe parent) throws RemoteException;

	// Gruppe löschen
	public void deleteGroupOf(Person user, Gruppe gruppe, Gruppe parent) throws RemoteException;

	// Gruppe ändern
	public void update(Person user, Gruppe gruppe) throws RemoteException;
}