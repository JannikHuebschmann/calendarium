/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			calproj			transfer out of the calendarium project
 *
 */
package swtkal.server;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.TerminException;

import java.util.Vector;

/******************************************************************************************************
 * Interface TermineIF specifies those services of the server interface that
 * deal with Termin objects.
 *
 * @author	calendarium project
 */
public interface TermineIF
{
	/**
	 * Inserts an appointment into the server.
	 * @param termin Termin
	 * @throws TerminException
	 */
	public void insert(Termin termin) throws TerminException;

	/**
	 * Deletes an appointment from the server.
	 * @param termin
	 * @throws TerminException
	 */
	public void delete(Termin termin) throws TerminException;

	/**
	 * Finds all appointments of a person as a participant for a given day.
	 * @param dat the date of the appointments
	 * @param teilnehmer the Person for which are the appointments 
	 * @return Vector<Termin> - a list of appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVom(Datum dat, Person teilnehmer) throws TerminException;

	/**
	 * Finds all appointments of a person as a participant for a given period of time.
	 * @param vonDat the start date of the period
	 * @param bisDat the end date of the period
	 * @param teilnehmer the Person for which are the appointments
	 * @return Vector<Termin> - a list of appointments
	 * @throws TerminException
	 */
	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person teilnehmer) throws TerminException;

// weitere Methoden aus Calendarium, die evtl. beim weiteren Ausbau noch ben�tigt werden	
//	/** Personen mit Terminen
//	 * 
//	 * @param vonDat
//	 * @param bisDat
//	 * @param personenListe
//	 * @return Vector[]
//	 * @throws RemoteException
//	 */
//	public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat,
//			Vector personenListe) throws RemoteException;
//
//	/** Termine an einem Tag ausgeben
//	 * 
//	 * @param vomDat
//	 * @param personenListe
//	 * @return Vector
//	 * @throws RemoteException
//	 */
//	public Vector getTermineVom(Datum vomDat, Vector personenListe)
//			throws RemoteException;
//
//	/** einen bestimmten Termin ausgeben
//	 * 
//	 * @param id
//	 * @return Termin
//	 * @throws RemoteException
//	 */
//	public Termin getTerminByID(long id) throws RemoteException;
//
//	/** Konflikte ausgeben
//	 * 
//	 * @param termin
//	 * @return Vector
//	 * @throws RemoteException
//	 */
//	public Vector getKonflikte(Termin termin) throws RemoteException;
//
//	/** konfliktfreie Intervalle ausgeben
//	 * 
//	 * @param persons
//	 * @param bgn
//	 * @param end
//	 * @param wk
//	 * @return Vector
//	 * @throws RemoteException
//	 */
//	public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end,
//			boolean wk) throws RemoteException;
//
//	/** Daten s�ubern
//	 * 
//	 * @param person
//	 * @param bis
//	 * @throws RemoteException
//	 */
//	public void deleteUntilDate(Person person, Datum bis) throws RemoteException;
//	/** Termin �ndern
//	 * 
//	 * @param kuerzel
//	 * @param termin
//	 * @throws RemoteException
//	 */
//	public void update(String kuerzel, Termin termin) throws RemoteException;
//
//	/** Fehlendes Eintragsrecht
//	 * 
//	 * @param termin
//	 * @param persons
//	 * @throws RemoteException
//	 */
//	public void sendMissingRight(Termin termin, Vector persons)
//			throws RemoteException;
}