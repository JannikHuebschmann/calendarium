package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import basisklassen.Datum;
import basisklassen.Person;
import basisklassen.ToDo;

public interface ToDoSetInterface extends Remote
{
    // ToDo-Eintr�ge eines Tages ausgeben
    public Vector getToDoVom(Datum vomDat, Vector personenListe) throws RemoteException;

	// ToDo-Eintr�ge eines Zeitraums ausgeben
	public Vector[] getToDoVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

	// ein bestimmtes ToDo ausgeben
	public ToDo getToDoByID(long id) throws RemoteException;

    // Daten s�ubern
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException;

	// ToDo anlegen
	public void create(String kuerzel,ToDo toDo) throws RemoteException;

	// ToDo l�schen
	public void delete(String kuerzel,ToDo toDo, boolean serie) throws RemoteException;

	// ToDo �ndern
	public void update(String kuerzel,ToDo toDo) throws RemoteException;

	// Fehlendes Eintragsrecht
	public void sendMissingRight(ToDo toDo, Vector persons) throws RemoteException;
}