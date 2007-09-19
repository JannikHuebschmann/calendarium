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
package swtkal.domain;

/*****************************************************************************************************
 * Class Person represents users of SWTKal
 * 
 * @author calendarium project
 */
public class Person
{
	protected String vorname, nachname, kuerzel;

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