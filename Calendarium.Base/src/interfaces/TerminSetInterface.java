package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

import basisklassen.Datum;
import basisklassen.Person;
import basisklassen.Termin;

public interface TerminSetInterface extends Remote
{
    // Termine an einem Tag ausgeben
    public Vector getTermineVom(Datum vomDat, Vector personenListe) throws RemoteException;

	// Termine in einem Zeitraum ausgeben
	public Vector[] getTermineVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

    // Personen mit Terminen
    public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

	// einen bestimmten Termin ausgeben
	public Termin getTerminByID(long id) throws RemoteException;

    // Konflikte ausgeben
    public Vector getKonflikte(Termin termin) throws RemoteException;

    // konfliktfreie Intervalle ausgeben
    public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end, boolean wk) throws RemoteException;

    // Daten säubern
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException;

	// Termin anlegen
	public void create(String kuerzel,Termin termin) throws RemoteException;

	// Termin löschen
	public void delete(String kuerzel,Termin termin, boolean serie) throws RemoteException;

	// Termin ändern
	public void update(String kuerzel,Termin termin) throws RemoteException;

	// Fehlendes Eintragsrecht
	public void sendMissingRight(Termin termin, Vector persons) throws RemoteException;
}