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
 *  01.03.2008			ejbUser			using Collection<E> for teilnehmer association
 *
 */
package swtkal.domain;

import java.util.Collection;
import java.util.Vector;

/*****************************************************************************************************
 * Class Termin represents appointments that are managed by the swtKal server.
 * 
 * @author swtUser
 */
public class Termin extends Eintrag
{
	protected Datum beginn;
	protected Datum ende;
	protected boolean verschiebbar;
	
	protected Collection<Person> teilnehmer = new Vector<Person>();

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum b, Datum e, boolean v)
	{
		super(besitzer, kurzText, langText);

		beginn = b;
		ende = e;
		verschiebbar = v;
		
		teilnehmer.add(besitzer);	// the owner is a default participant
	}

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum b, Datum e)
	{
		this(besitzer, kurzText, langText, b, e, false);
	}

	public Termin(Person besitzer, Datum b, Datum e, boolean v)
	{
		this(besitzer, "", "", b, e, v);
	}

	public Termin(Person besitzer, Datum b, Datum e)
	{
		this(besitzer, "", "", b, e, false);
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

	public Collection<Person> getTeilnehmer()
	{
		return (Vector<Person>) teilnehmer;
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
}