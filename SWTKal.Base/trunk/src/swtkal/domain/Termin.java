package swtkal.domain;

import java.util.Vector;

/**
 * Class Termin represents appointments that are managed by the calendar server.
 *
 */
public class Termin extends Eintrag
{
	protected Datum beginn;
	protected Datum ende;
	protected boolean verschiebbar;
	
	protected Vector<Person> teilnehmer = new Vector<Person>();

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum b, Datum e, boolean v)
	{
		super(besitzer, kurzText, langText);

		beginn = b;
		ende = e;
		verschiebbar = v;
		
		teilnehmer.add(besitzer);
	}

	public Termin(Person besitzer, String kurzText, String langText,
			 Datum b, Datum e)
	{
		this(besitzer, kurzText, langText, b, e, false);
	}

	public Termin(Person besitzer, Datum b, Datum e, boolean v)
	{
		super(besitzer);

		beginn = b;
		ende = e;
		verschiebbar = v;
	}

	public Termin(Person besitzer, Datum b, Datum e)
	{
		this(besitzer, b, e, false);
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
}