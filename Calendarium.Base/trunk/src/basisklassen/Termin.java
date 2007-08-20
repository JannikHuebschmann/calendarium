package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

////////////////////////////////////////////////////////////////////////////////////////////
// Termin // Termin // Termin // Termin // Termin // Termin // Termin // Termin // Termin //
////////////////////////////////////////////////////////////////////////////////////////////

public class Termin extends Eintrag
{
	private static final long serialVersionUID = 4767094906709404205L;
	private boolean verschiebbar;
    private Datum ende;

    public Termin(long id)
    {   super(id);
    }

    public Termin(Person ow)
    {
		super(ow);

		ende = new Datum();
		verschiebbar = false;

    }

    // Ansicht
    public Termin(long id, Person ow, Datum bgn, Datum end, String txt, EintragsTyp typ, boolean vb, Vector tn)
    {
        super(id, ow, bgn, txt, typ, tn);

		ende = end;
    }

    public Termin(long id, Person ow, Datum bgn, Datum end, String ktxt, String ltxt, EintragsTyp t,
                  String o, String hlk, boolean vb, Notifikation[] nf, Vector tn, Serie sn)
    {
        super(id, ow, bgn, ktxt, ltxt, t, o, hlk, nf, tn, sn);

		ende = end;
		verschiebbar = vb;

    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {
        return new String(kurzText+teilnehmer.toString()+nfktRelevant.toString()+owner.toString());
    }

    public Datum getBeginn()
	{   return nfktRelevant;
	}

	public Datum getEnde()
	{   return ende;
	}

	public boolean getVerschiebbar()
	{	return verschiebbar;
	}

    public void setBeginn(Datum d)
    {   nfktRelevant = d;
    }

    public void setEnde(Datum d)
    {   ende = d;
    }

	public void setVerschiebbar(boolean v)
	{   verschiebbar = v;
	}

	public Object getValue(String attribute)
	{
	    if (attribute.equals("verschiebbar"))
	    {
	        if (verschiebbar) return new Integer(1);
	        else return new Integer(0);
	    }
	    if (attribute.equals("ende")) return new Long(ende.getDatum());
	    return super.getValue(attribute);
	}

	public void swap(ResultSet res)
	{
	    try
	    {
            map = getMapping();
	        verschiebbar = (res.getInt(map.getAttribute("verschiebbar").getColumn()) == 1);
	        ende = new Datum(new java.util.Date(res.getLong(map.getAttribute("ende").getColumn())));
	        super.swap(res);
	    }catch (SQLException s) {System.out.println(s);}
	}
}