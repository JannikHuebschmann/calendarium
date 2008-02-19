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
package swtkal.domain;

import javax.persistence.*;

/*****************************************************************************************************
 * Class Person represents users of SWTKal
 * 
 * @author swtUser
 */
@Entity
public class Person
{
	@Id							// kuerzel is used to uniquely identify
	protected String kuerzel;	// each Person object within the server
	protected String vorname;
	protected String nachname;

	public Person(String v, String n, String k)
	{
		vorname = v;
		nachname = n;
		kuerzel = k;
	}

	public String toString()
	{
		return new String("Name: " + nachname + " Kuerzel: " + kuerzel);
	}

	public String getVorname()
	{
		return vorname;
	}

	public String getNachname()
	{
		return nachname;
	}

	public String getName()
	{
		return vorname + " " + nachname;
	}

	public String getKuerzel()
	{
		return kuerzel;
	}

	public void setVorname(String v)
	{
		vorname = v;
	}

	public void setNachname(String n)
	{
		nachname = n;
	}

	public void setKuerzel(String k)
	{
		kuerzel = k;
	}
}