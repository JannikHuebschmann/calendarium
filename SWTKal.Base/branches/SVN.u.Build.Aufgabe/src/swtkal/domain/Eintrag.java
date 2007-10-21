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
 * Superclass with common properties for Termin and ToDo
 * 
 * @author calendarium project
 */
public class Eintrag
{
	protected String kurzText = "", langText = "";
	
	protected Person besitzer;

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

	public Person getBesitzer()
	{
		return besitzer;
	}

	public String getKurzText()
	{
		return kurzText;
	}

	public String getLangText()
	{
		return langText;
	}

	public void setBesitzer(Person p)
	{
		besitzer = p;
	}

	public void setKurzText(String k)
	{
		kurzText = k;
	}

	public void setLangText(String l)
	{
		langText = l;
	}
}