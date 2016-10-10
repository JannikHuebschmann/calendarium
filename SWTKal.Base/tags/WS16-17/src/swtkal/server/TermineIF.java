/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007		swtUser			initial version
 *	01.06.2008		swtUser			new update method and extended search functionality
 *
 */
package swtkal.server;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.TerminException;

import java.util.Vector;

/*****************************************************************************************************
 * Interface TermineIF specifies those services of the server interface that
 * deal with Termin objects.
 *
 * @author	swtUser
 */
public interface TermineIF
{
	/**
	 * Inserts an appointment into the server.
	 * @param termin the appointment to be inserted
	 * @throws TerminException
	 */
	public void insert(Termin termin) throws TerminException;

	/**
	 * Deletes an appointment from the server.
	 * @param termin the appointment to be deleted
	 * @throws TerminException
	 */
	public void delete(Termin termin) throws TerminException;
	
	
	/**
	 * Deletes an appointment by ID from the server.
	 * @param terminID of the appointment to be deleted
	 * @throws TerminException
	 */
	public void delete(int terminID) throws TerminException;

	/**
	 * Updates an appointment on the server.
	 * @param termin the appointment to be updated
	 * @throws TerminException
	 */
	public void update(Termin termin) throws TerminException;

	/**
	 * Finds an appointment with a given internal id.
	 * @param terminId the internal id of the appointment
	 * @return Termin the retrieved appointment
	 * @throws TerminException
	 */
	public Termin getTermin(int terminId) throws TerminException;

	/**
	 * Finds all appointments of a person as a participant for a given day.
	 * @param dat the date of the appointments
	 * @param teilnehmer the person for whom appointments are retrieved
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVom(Datum dat, Person teilnehmer) throws TerminException;

	/**
	 * Finds all appointments of a person as a participant for a given period of time.
	 * @param vonDat the start date of the period
	 * @param bisDat the end date of the period
	 * @param teilnehmer the person for whom appointments are retrieved
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person teilnehmer) throws TerminException;

	/**
	 * Finds all appointments for a given day such that at least one person of the given group is participating.
	 * @param dat the date of the appointments
	 * @param teilnehmer the persons for whom appointments are retrieved
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVom(Datum dat, Vector<Person> teilnehmer) throws TerminException;

	/**
	 * Finds all appointments for a given period of time such that at least one person of the given group is participating.
	 * @param vonDat the start date of the period
	 * @param bisDat the end date of the period
	 * @param teilnehmer the persons for whom appointments are retrieved
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Vector<Person> teilnehmer) throws TerminException;

	/**
	 * Finds all appointments for a given day and a given owner.
	 * @param dat the date of the appointments
	 * @param besitzer the person who owns the appointments
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getBesitzerTermineVom(Datum dat, Person besitzer) throws TerminException;

	/**
	 * Finds all appointments for a given period of time and a given owner.
	 * @param vonDat the start date of the period
	 * @param bisDat the end date of the period
	 * @param besitzer the person who owns the appointments
	 * @return Vector<Termin> a vector of retrieved appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getBesitzerTermineVonBis(Datum vonDat, Datum bisDat, Person besitzer) throws TerminException;

	/**
	 * Checks if person is available for a given period of time.
	 * @param vonDat the start date of the period
	 * @param bisDat the end date of the period
	 * @param teilnehmer the person for whom appointment are retrieved
	 * @return boolean a boolean if person is available
	 * @throws TerminException
	 */
	public boolean isPersonAvailable(Datum vonDat, Datum bisDat, Person teilnehmer) throws TerminException;

//	/**
//	 * Finds all common appointments of persons
//	 * @author tuncay
//	 * @param teilnehmer persons with common appointments
//	 * @return Vector<Termin> a vector of retrieved appointments
//	 * @throws TerminException
//	 */
//	public Vector<Termin> getGemeinsameTermine(Vector<Person> teilnehmer) throws TerminException;

//	/**
//	 * Finds all common appointments of persons for a given period of time
//	 * @author tuncay
//	 * @param vonDat the start date of the period
//	 * @param bisDat the end date of the period
//	 * @param teilnehmer the persons for whom appointments are retrieved
//	 * @return Vector<Termin> a vector of retrieved appointments
//	 */
//	public Vector<Termin> getGemeinsameTerminVonBis(Datum vonDat, Datum bisDat, Vector<Person> teilnehmer);

//	/**
//	 * Finds all free appointments of persons for a given period of time
//	 * @author tuncay
//	 * @param vonDat the start date of the period
//	 * @param bisDat the end date of the period
//	 * @param teilnehmer the persons for whom appointments are retrieved
//	 * @return Vector<Termin> a vector of retrieved appointments
//	 */
//	public Vector<Termin> getFreieTermineVonBis(Datum vonDat, Datum bisDat, Vector<Person> teilnehmer);

//	weitere Methoden aus Calendarium, die beim weiteren Ausbau noch benötigt werden könnten	
//	/** konfliktfreie Intervalle ausgeben
//	* 
//	* @param persons
//	* @param bgn
//	* @param end
//	* @param wk
//	* @return Vector
//	* @throws RemoteException
//	*/
//	public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end,
//	boolean wk) throws RemoteException;

//	/** Daten säubern
//	* 
//	* @param person
//	* @param bis
//	* @throws RemoteException
//	*/
//	public void deleteUntilDate(Person person, Datum bis) throws RemoteException;
}