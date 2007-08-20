package basisklassen; //
////////////////////////

import java.sql.ResultSet;

//////////////////////////////////////////////////////////////////////////////////////////////////////
// Person // Person // Person // Person // Person // Person // Person // Person // Person // Person //
//////////////////////////////////////////////////////////////////////////////////////////////////////

public class Person extends dblayer.PersistentObject
{
    private static final long serialVersionUID = -4225385662334310525L;
	private String kuerzel, nachname, vorname, email_addr, faxnr, passwd;
    private int vorzugsNfkt;

    public Person()
    {
        super();
        vorzugsNfkt = data.Shared.NFKT_BEEP;
    }

    public Person(long id)
    {
        super(id);
    }

    public Person(String k, String n, String v)
    {
        super();
        kuerzel = k;			// Kuerzel
        nachname = n;       // Nachname
        vorname = v;        // Vorname

        vorzugsNfkt = data.Shared.NFKT_BEEP;
    }

    public Person(String k, String n, String v, String e, String f, String p, int vn)
    {
        super();
        kuerzel = k;         // Kuerzel
        nachname = n;       // Nachname
        vorname = v;        // Vorname

        email_addr = e;     // EmailAdresse
        faxnr = f;          // FaxNummer
        passwd = p;         // Passwort

        vorzugsNfkt = vn;   // Erreichbarkeit
    }

	public Person(long i, String k, String n, String v)
    {
        super(i);	     	// Identifier
		kuerzel = k;         // Kuerzel
        nachname = n;       // Nachname
        vorname = v;        // Vorname
		passwd = "";		// Passwort

        vorzugsNfkt = data.Shared.NFKT_BEEP;
    }

    public Person(long i, String k, String n, String v, String e, String f, String p, int vn)
    {
        super(i);			// Identifier
		kuerzel = k;         // Kuerzel
        nachname = n;       // Nachname
        vorname = v;        // Vorname

        email_addr = e;     // EmailAdresse
        faxnr = f;          // FaxNummer
        passwd = p;         // Passwort

        vorzugsNfkt = vn;   // Erreichbarkeit
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {
        return new String("name " + nachname + "kuerzel" + kuerzel);
    }

	public String getKuerzel()
    {   return kuerzel;
    }

    public String getNachname()
	{	return nachname;
	}

	public String getVorname()
	{	return vorname;
	}

	public String getNameLang()
	{	return nachname + " " + vorname;
	}

	public String getEmailAdresse()
	{   return email_addr;
	}

	public String getFaxNummer()
	{   return faxnr;
	}

	public String getPasswort()
	{   return passwd;
	}

	public int getVorzugsNfkt()
	{   return vorzugsNfkt;
	}

	public void setKuerzel(String k)
	{	kuerzel = k;
	}

	public void setNachname(String n)
	{   nachname = n;
	}

	public void setVorname(String v)
	{   vorname = v;
	}

	public void setEmailAdresse(String e)
	{   email_addr = e;
	}

	public void setFaxNummer(String f)
	{   faxnr = f;
	}

	public void setPasswort(String p)
	{   passwd = p;
	}

	public void setVorzugsNfkt(int v)
	{   vorzugsNfkt = v;
	}

    //////////////////////////////////////////////////////////////////////


    /** maps one row from db to instance of EintragsTyp
      */
    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            kuerzel = res.getString(map.getAttribute("kuerzel").getColumn());
            nachname = res.getString(map.getAttribute("nachname").getColumn());
            vorname = res.getString(map.getAttribute("vorname").getColumn());
            email_addr = res.getString(map.getAttribute("email_addr").getColumn());
            faxnr = res.getString(map.getAttribute("faxnr").getColumn());
            passwd = res.getString(map.getAttribute("passwd").getColumn());
            vorzugsNfkt = res.getInt(map.getAttribute("vorzugsNfkt").getColumn());
        }catch (Exception s){System.out.println(s);}
    }

    /** returns value of attribute 'attribute'
      */
    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("kuerzel")) return kuerzel;
        if (attribute.equals("nachname")) return nachname;
        if (attribute.equals("vorname")) return vorname;
        if (attribute.equals("email_addr")) return email_addr;
        if (attribute.equals("faxnr")) return faxnr;
        if (attribute.equals("vorzugsNfkt")) return new Integer(vorzugsNfkt);
        if (attribute.equals("passwd")) return passwd;
        return null;
    }



}