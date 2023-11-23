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

import swtkal.server.TermineIF;

import java.util.Vector;

/*****************************************************************************************************
 * Class Termin represents appointments that are managed by the calendar server.
 * 
 * @author calendarium project
 */
public class Termin extends Eintrag
{
	protected Datum beginn;
	protected Datum ende;
	protected boolean verschiebbar;
	protected Integer id;
	
	protected Vector<Person> teilnehmer = new Vector<Person>();

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum beginn, Datum ende, boolean verschiebbar,Integer id)
	{
		super(besitzer, kurzText, langText);

		this.beginn = beginn;
		this.ende = ende;
		this.verschiebbar = verschiebbar;
		this.id = id;

		teilnehmer.add(besitzer);	// the owner is a default participant
	}

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum b, Datum e,Integer id)
	{
		this(besitzer, kurzText, langText, b, e, false,id);
	}

	public Termin(Person besitzer, Datum b, Datum e, boolean v,Integer id)
	{
		this(besitzer, "", "", b, e, v,id);
	}

	public Termin(Person besitzer, Datum b, Datum e,Integer id)
	{
		this(besitzer, "", "", b, e, false,id);
	}

	public String toString()
	{
		return kurzText + " " + besitzer.toString();
	}

	public Datum getBeginn()
	{
		return beginn;
	}

	public Datum getEnde()
	{
		return ende;
	}

	public boolean getVerschiebbar()
	{
		return verschiebbar;
	}

	public Vector<Person> getTeilnehmer()
	{
		return teilnehmer;
	}

	public void setBeginn(Datum d)
	{
		beginn = d;
	}

	public void setEnde(Datum d)
	{
		ende = d;
	}

	public void setVerschiebbar(boolean v)
	{
		verschiebbar = v;
	}
	
	public void setTeilnehmer(Vector<Person> t)
	{
		teilnehmer = t;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}