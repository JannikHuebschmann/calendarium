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

/*****************************************************************************************************
 * Superclass with common properties for Termin and ToDo
 * 
 * @author swtUser
 */
public class Eintrag
{
	protected int    id=0;			// internal id to uniquely identify
	                                // each Eintrag object within the server
	protected String kurzText = "", langText = "";	
	protected Person besitzer;

	public Eintrag()
	{
	}
	
	public Eintrag(Person b, String k, String l)
	{
		besitzer = b;
		kurzText = k;
		langText = l;
	}

	public Eintrag(Person b)
	{
		besitzer = b;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getKurzText()
	{
		return kurzText;
	}

	public void setKurzText(String k)
	{
		kurzText = k;
	}

	public String getLangText()
	{
		return langText;
	}

	public void setLangText(String l)
	{
		langText = l;
	}

	public Person getBesitzer()
	{
		return besitzer;
	}

	public void setBesitzer(Person p)
	{
		besitzer = p;
	}
}