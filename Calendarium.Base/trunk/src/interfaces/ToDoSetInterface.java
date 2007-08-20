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
    // ToDo-Einträge eines Tages ausgeben
    public Vector getToDoVom(Datum vomDat, Vector personenListe) throws RemoteException;

	// ToDo-Einträge eines Zeitraums ausgeben
	public Vector[] getToDoVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

	// ein bestimmtes ToDo ausgeben
	public ToDo getToDoByID(long id) throws RemoteException;

    // Daten säubern
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException;

	// ToDo anlegen
	public void create(String kuerzel,ToDo toDo) throws RemoteException;

	// ToDo löschen
	public void delete(String kuerzel,ToDo toDo, boolean serie) throws RemoteException;

	// ToDo ändern
	public void update(String kuerzel,ToDo toDo) throws RemoteException;

	// Fehlendes Eintragsrecht
	public void sendMissingRight(ToDo toDo, Vector persons) throws RemoteException;
}