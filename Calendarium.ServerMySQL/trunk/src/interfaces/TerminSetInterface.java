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
    /** Termine an einem Tag ausgeben
     * 
     * @param vomDat
     * @param personenListe
     * @return Vector
     * @throws RemoteException
     */
    public Vector getTermineVom(Datum vomDat, Vector personenListe) throws RemoteException;

	/** Termine in einem Zeitraum ausgeben
	 * 
	 * @param vonDat
	 * @param bisDat
	 * @param personenListe
	 * @return Vector[]
	 * @throws RemoteException
	 */
	public Vector[] getTermineVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

    /** Personen mit Terminen
     * 
     * @param vonDat
     * @param bisDat
     * @param personenListe
     * @return Vector[]
     * @throws RemoteException
     */
    public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException;

	/** einen bestimmten Termin ausgeben
	 * 
	 * @param id
	 * @return Termin
	 * @throws RemoteException
	 */
	public Termin getTerminByID(long id) throws RemoteException;

    /** Konflikte ausgeben
     * 
     * @param termin
     * @return Vector
     * @throws RemoteException
     */
    public Vector getKonflikte(Termin termin) throws RemoteException;

    /** konfliktfreie Intervalle ausgeben
     * 
     * @param persons
     * @param bgn
     * @param end
     * @param wk
     * @return Vector
     * @throws RemoteException
     */
    public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end, boolean wk) throws RemoteException;

    /** Daten säubern
     * 
     * @param person
     * @param bis
     * @throws RemoteException
     */
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException;

	/** Termin anlegen
	 * 
	 * @param kuerzel
	 * @param termin
	 * @throws RemoteException
	 */
	public void create(String kuerzel,Termin termin) throws RemoteException;

	/** Termin löschen
	 * 
	 * @param kuerzel
	 * @param termin
	 * @param serie
	 * @throws RemoteException
	 */
	public void delete(String kuerzel,Termin termin, boolean serie) throws RemoteException;

	/** Termin ändern
	 * 
	 * @param kuerzel
	 * @param termin
	 * @throws RemoteException
	 */
	public void update(String kuerzel,Termin termin) throws RemoteException;

	/** Fehlendes Eintragsrecht
	 * 
	 * @param termin
	 * @param persons
	 * @throws RemoteException
	 */
	public void sendMissingRight(Termin termin, Vector persons) throws RemoteException;
}