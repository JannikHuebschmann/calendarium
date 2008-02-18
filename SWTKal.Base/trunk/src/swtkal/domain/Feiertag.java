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

// Achtung: diese Klasse wurde im Wesentlichen aus Calendarium ungeprueft uebernommen

import java.util.Calendar;
import java.util.GregorianCalendar;

/*****************************************************************************************************
 * This Feiertag ......
 * 
 * @author calendarium project
 */
public class Feiertag
{
	private GregorianCalendar date;
	private String bezeichnung;

	public Feiertag(GregorianCalendar d, String b)
	{
		super();
		date = d;
		bezeichnung = b;
	}

	// Datum ausgeben
	public GregorianCalendar getDate()
	{
		return date;
	}

	// Datum als String ausgeben
	public String getDateString()
	{
		int dy = date.get(Calendar.DATE);
		int mt = date.get(Calendar.MONTH) + 1;
		int yr = date.get(Calendar.YEAR);

		return (dy < 10 ? "0" + dy : "" + dy) + "."
				+ (mt < 10 ? "0" + mt : "" + mt) + "." + yr;
	}

	// Bezeichnung ausgeben
	public String getBezeichnung()
	{
		return bezeichnung;
	}

	// Farbe festlegen
	public void setDate(GregorianCalendar d)
	{
		date = d;
	}

	// Bezeichnung festlegen
	public void setBezeichnung(String b)
	{
		bezeichnung = b;
	}
}