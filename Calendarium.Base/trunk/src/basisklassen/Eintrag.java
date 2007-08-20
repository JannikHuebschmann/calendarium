package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/////////////////////////////////////////////////////////////////////////////////////////////////////
// Eintrag // Eintrag // Eintrag // Eintrag // Eintrag // Eintrag // Eintrag // Eintrag // Eintrag //
/////////////////////////////////////////////////////////////////////////////////////////////////////

public class Eintrag extends dblayer.PersistentObject
{
    private static final long serialVersionUID = 1512834359503621709L;
	Person owner;
    String kurzText = "", langText = "", ort = "", hyperlink = "";
    EintragsTyp typ;
    Notifikation[] nfkt;
    Vector teilnehmer;
    Datum nfktRelevant;
	Serie serie;

    Eintrag(long i)
    {   super(i);
    }

    @SuppressWarnings("unchecked")
	Eintrag(Person ow)
    {
        super();
        owner = ow;                                 // Owner
        nfktRelevant = new Datum();

        // compounded
        typ = null;                                 // Eintragstyp
        nfkt = null;                                // Notifikationen
        serie = null;								// Serie

        teilnehmer = new Vector();                  // Teilnehmer
        teilnehmer.addElement(new Teilnehmer(ow));
    }

    // Ansicht
    Eintrag(long id, Person ow, Datum nr, String txt, EintragsTyp t, Vector tn)
    {
        super(id);      // Identifier
        owner = ow;         // Owner
        nfktRelevant = nr;  // Nfkt-relevanter Zeitpunkt
	    kurzText = txt;     // Kurztext

        // compounded
        typ = t;            // Termintyp
        teilnehmer = tn;    // Teilnehmer

        nfkt = null;
        serie = null;
    }

	Eintrag(long id, Person ow, Datum nr, String ktxt, String ltxt, EintragsTyp t,
            String o, String hlk, Notifikation[] nf, Vector tn, Serie sn)
	{
	    super(id);              // Identifier
	    owner = ow;             // Owner
	    nfktRelevant = nr;		// Nfkt-relevanter Zeitpunkt
		kurzText = ktxt;        // Kurztext
	    langText = ltxt;        // Langtext
	    ort = o;                // Ort
	    hyperlink = hlk;        // Hyperlink

	    // compounded
	    typ = t;                // Eintragstyp
	    nfkt = nf;              // Notifikationen
	    teilnehmer = tn;        // Teilnehmer = Liste vom Typ Teilnehmer
	    serie = sn;             // zugeordnete Serie
    }

    @SuppressWarnings("unchecked")
	private void addToPersNfkt(boolean[] nfkt, Gruppe gruppe, Hashtable persNfkt)
    {
        Hashtable ps = (Hashtable) gruppe.getPersonen();

        Enumeration e = ps.elements();
        while(e.hasMoreElements())
        {
            Person person = (Person) e.nextElement();
            Long id = new Long(person.getID());

            if(!persNfkt.containsKey(id))
            {   persNfkt.put(id, new Teilnehmer(person, nfkt));

            } else
            {
                Teilnehmer t = (Teilnehmer) persNfkt.get(id);
                t.setNotifikationen(getAllNfkts(t.getNotifikationen(), nfkt));
            }
        }

        Hashtable gr = (Hashtable) gruppe.getGruppen();

        e = gr.elements();
        while(e.hasMoreElements())
            addToPersNfkt(nfkt, (Gruppe) e.nextElement(), persNfkt);
    }

    private boolean[] getAllNfkts(boolean[] nf1, boolean[] nf2)
    {
        boolean[] nf = new boolean[nf1.length];

        for(int i = 0; i < nf1.length; i++)
        {   nf[i] = nf1[i] || nf2[i];
        }

        return nf;
    }

    @SuppressWarnings("unchecked")
	private Hashtable getPersonHash()
    {
        Hashtable personHash = new Hashtable();

        Enumeration e = teilnehmer.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();
            if(tn.getTeilnehmer() instanceof Person)
            {
                Person person = (Person) tn.getTeilnehmer();
                Long id = new Long(person.getID());

                if(!personHash.containsKey(id))
                {   personHash.put(id, person);
                }

            } else addToPersonHash((Gruppe) tn.getTeilnehmer(), personHash);
        }

        return personHash;
    }

    @SuppressWarnings("unchecked")
	private void addToPersonHash(Gruppe gruppe, Hashtable personHash)
    {
        Hashtable ps = (Hashtable) gruppe.getPersonen();

        Enumeration e = ps.elements();
        while(e.hasMoreElements())
        {
            Person person = (Person) e.nextElement();
            Long id = new Long(person.getID());

            if(!personHash.containsKey(id)) personHash.put(id, person);
        }

        Hashtable gr = (Hashtable) gruppe.getGruppen();

        e = gr.elements();
        while(e.hasMoreElements())
            addToPersonHash((Gruppe) e.nextElement(), personHash);
    }

	//////////////////////////////////////////////////////////////////////////////////////////
	// Attribute ausgeben // Attribute ausgeben // Attribute ausgeben // Attribute ausgeben //
	//////////////////////////////////////////////////////////////////////////////////////////

	public Person getOwner()
	{   return owner;
	}

    public Datum getNfktRelevant()
    {   return nfktRelevant;
    }

	public String getKurzText()
	{	return kurzText;
	}

	public String getLangText()
	{   return langText;
	}

	public String getOrt()
	{   return ort;
	}

	public String getHyperlink()
	{   return hyperlink;
	}

	public EintragsTyp getTyp()
	{   return typ;
	}

	public Notifikation[] getNotifikationen()
    {   return nfkt;
    }

    public Vector getTeilnehmer()
    {   return teilnehmer;
    }

    public Serie getSerie()
    {   return serie;
    }

    public boolean isEmpty()
    {   return (typ == null);
    }

    // Hashtable der teilnehmenden Personen, mit ihren Notifikationen
    @SuppressWarnings("unchecked")
	public Hashtable getAllPersonsWithNfkt()
    {
        Hashtable persNfkt = new Hashtable();

        Enumeration e = teilnehmer.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();
            if(tn.getTeilnehmer().getClass().equals(Person.class))
            {
                Person person = (Person) tn.getTeilnehmer();
                Long id = new Long(person.getID());

                if(!persNfkt.containsKey(id))
                {   persNfkt.put(id, new Teilnehmer(person, tn.getNotifikationen()));
                }

            } else addToPersNfkt(tn.getNotifikationen(), (Gruppe) tn.getTeilnehmer(), persNfkt);
        }

        return persNfkt;
    }

    // Hashtable der teilnehmenden Personen
    public Hashtable getAllPersons()
    {   return getPersonHash();
    }

    // Filterung
    public boolean isRelevantFor(Vector persons)
    {
        Hashtable personHash = getPersonHash();

        Enumeration e = persons.elements();
        while(e.hasMoreElements())
        {
            Person person = (Person) e.nextElement();
            Long id = new Long(person.getID());

            if(personHash.containsKey(id)) return true;
        }

        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Attribute setzen // Attribute setzen // Attribute setzen // Attribute setzen //
	//////////////////////////////////////////////////////////////////////////////////

    public void setOwner(Person p)
    {   owner = p;
    }

	public void setKurzText(String t)
	{	kurzText = t;
	}

	public void setLangText(String t)
	{   langText = t;
	}

	public void setOrt(String o)
	{   ort = o;
	}

	public void setHyperlink(String h)
	{   hyperlink = h;
	}

	public void setTyp(EintragsTyp t)
	{   typ = t;
	}

	public void setNotifikationen(Notifikation[] n)
	{   nfkt = n;
	}

	public void setTeilnehmer(Vector tn)
	{   teilnehmer = tn;
	}

	public void setSerie(Serie s)
	{   serie = s;
	}

	public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("kurzText")) return kurzText;
        if (attribute.equals("langText")) return langText;
        if (attribute.equals("ort")) return ort;
        if (attribute.equals("hyperlink")) return hyperlink;
        if (attribute.equals("owner")) return new Long(owner.getID());
        if (attribute.equals("typ")) return new Long(typ.getID());
        if (attribute.equals("nfktRelevant")) return new Long(nfktRelevant.getDatum());
        if (attribute.equals("serie"))
        {
            if (serie != null) return new Long(serie.getID());
            else return new Long(0);
        }
        return null;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            kurzText = res.getString(map.getAttribute("kurzText").getColumn());
            langText = res.getString(map.getAttribute("langText").getColumn());
            ort = res.getString(map.getAttribute("ort").getColumn());
            hyperlink = res.getString(map.getAttribute("hyperlink").getColumn());
            if (hyperlink == null) hyperlink = "";
            nfktRelevant = new Datum(new java.util.Date(res.getLong(map.getAttribute("nfktRelevant").getColumn())));
        }catch (SQLException s){System.out.println(s);}
    }

}