/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			swtUser			initial version
 *
 */
package swtkal.server;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;

import java.util.Vector;

/*****************************************************************************************************
 * Interface PersonenIF specifies those services of the server interface that
 * mainly deal with Person objects.
 * 
 * @author swtUser
 */
public interface PersonenIF
{
	/** This method inserts a Person object and associates it with a String password.
	 *  The person has to have a unique kuerzel property within the server by which it
	 *  can be identified.
	 *
	 * @param p the inserted person 
	 * @param passwort the given password
	 * @throws PersonException
	 */
	public void insert(Person p, String passwort) throws PersonException;
	
	/** This method deletes a Person object. The person is identified within the
	 *  server by its kuerzel property.
	 *
	 * @param p the person to be deleted
	 * @throws PersonException
	 */
	public void delete(Person p) throws PersonException;
	
	/** This method updates properties of a Person object.
	 *  The person is identified within the server by its kuerzel property.
	 *
	 * @param p the person to be updated
	 * @throws PersonException
	 */
	public void update(Person p) throws PersonException;
	
	/** This method updates the associated password of a Person object.
	 *  The person is identified within the server by its kuerzel property.
	 *
	 * @param p	the person
	 * @param passwort the new password
	 * @throws PersonException
	 */
	public void updatePasswort(Person p, String passwort) throws PersonException;
	
	/** This method updates the kuerzel property of a Person object.
	 *  The person is identified within the server by its old kuerzel property.
	 *  The new kuerzel property has to be unique within the server.
	 *
	 * @param p	the person with a new kuerzel property
	 * @param oldKuerzel the old kuerzel property
	 * @throws PersonException
	 */
	public void updateKuerzel(Person p, String oldKuerzel) throws PersonException;
	
	/** This method authenticates a person by its kuerzel and password.
	 *
	 * @param kuerzel the unique kuerzel property
	 * @param passwort the password
	 * @return the corresponding person object
	 * @throws PersonException
	 */
	public Person authenticatePerson(String kuerzel, String passwort) throws PersonException;
	
	/** This method retrieves a person by its kuerzel property.
	 *
	 * @param kuerzel the kuerzel property
	 * @return the corresponding person object
	 * @throws PersonException
	 */
	public Person findPerson(String kuerzel) throws PersonException;
	
	/** This method tests whether a Person with a given kuerzel property
	 *  is known within the server.
	 *
	 * @param kuerzel the kuerzel property
	 * @return a boolean value indicating whether the kuerzel is known or not
	 */
	public boolean isPersonKnown(String kuerzel);

	/** This method retrieves all known Person objects from the server.
	 *
	 * @return Vector of Person objects known within the server
	 */
	public Vector<Person> getPersonVector();
}