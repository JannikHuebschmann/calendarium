package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

//////////////////////////////////////////////////////////////////////////////////////////
// ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo //
//////////////////////////////////////////////////////////////////////////////////////////

public class ToDo extends Eintrag
{
	private static final long serialVersionUID = 7978926679994844239L;
	private Datum erinnernAb;

    public ToDo(long i)
    {   super(i);
    }

    public ToDo(Person ow)
    {   super(ow);
		erinnernAb = new Datum();
    }

    // Ansicht
    public ToDo(long id, Person ow, Datum bgn, Datum end, String txt, EintragsTyp typ, Vector tn)
    {
		super(id, ow, end, txt, typ, tn);
		erinnernAb = bgn;
    }

    public ToDo(long id, Person ow, Datum bgn, Datum end, String ktxt, String ltxt, EintragsTyp t,
                String o, String hlk, Notifikation[] nf, Vector tn, Serie sn)
    {
        super(id, ow, end, ktxt, ltxt, t, o, hlk, nf, tn, sn);
		erinnernAb = bgn;

    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {
        return new String(erinnernAb.getDay() + " " + erinnernAb.getMonth() + " " +nfktRelevant.getDay() + " " +nfktRelevant.getMonth());
    }

    public Datum getErinnernAb()
    {   return erinnernAb;
    }

    public Datum getFälligPer()
	{	return nfktRelevant;
	}

	public void setErinnernAb(Datum d)
    {   erinnernAb = d;
    }

    public void setFälligPer(Datum d)
    {   nfktRelevant = d;
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("erinnernAb")) return new Long(erinnernAb.getDatum());
        return super.getValue(attribute);
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            erinnernAb = new Datum(new java.util.Date(res.getLong(map.getAttribute("erinnernAb").getColumn())));
            super.swap(res);
        }catch (SQLException s){System.out.println(s);}
    }
}