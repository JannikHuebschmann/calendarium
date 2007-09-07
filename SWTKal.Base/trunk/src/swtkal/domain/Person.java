package swtkal.domain;

/**
 * Class Person represents users of SWTKal
 *
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