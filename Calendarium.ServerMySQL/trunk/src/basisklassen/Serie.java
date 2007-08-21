package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

////////////////////////////////////////////////////////////////////////////////////////////
// Serie // Serie // Serie // Serie // Serie // Serie // Serie // Serie // Serie // Serie //
////////////////////////////////////////////////////////////////////////////////////////////

public class Serie extends dblayer.PersistentObject implements data.Shared
{
    private static final long serialVersionUID = -8885714077906024225L;

	private Datum beginn, ende;

    private int typ;
    private int frequenz;
    private boolean werktags;

    public Serie(long i)
    {   super(i);
    }

    public Serie()
    {
        super();
        typ = -1;
        beginn = new Datum();   // Beginndatum der Serie
        ende = new Datum();     // Endedatum der Serie
    }

    public Serie(long i, Datum b, Datum e, int t, int f, boolean w)
    {
        super(i);         // Surrogat-Schluessel
        beginn = b;     // Beginndatum der Serie
        ende = e;       // Endedatum der Serie
        typ = t;        // Serientyp
        frequenz = f;   // Frequenz entsprechend dem Typ
        werktags = w;   // Werktagsserie?

        // Typen:
        // 0 ... täglich
        // 1 ... monatlich absolut
        // 2 ... monatlich relativ
        // 3 ... jährlich
        // >3... wöchentlich (2er Potenzen)
    }

    private String getWochentage()
    {
        if(typ > 3)
        {
            int w = typ >> 2;

            String text = "";
            int count = 0;

            while(w > 0)
            {
                if(w % 2 == 1) text += DAYNAMESSHORT[count] + ", ";

                w >>= 1;
                count++;
            }

            return text.substring(0, Math.max(0, text.length() - 2));

        } else return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
	// Attribute ausgeben // Attribute ausgeben // Attribute ausgeben // Attribute ausgeben //
	//////////////////////////////////////////////////////////////////////////////////////////


    public Datum getBeginn()
    {   return beginn;
    }

    public Datum getEnde()
    {   return ende;
    }

    public int getTyp()
    {   return typ;
    }

    public int getFrequenz()
    {   return frequenz;
    }

    public boolean getWerktags()
    {   return werktags;
    }

    public String toString()
    {
        String text;

        switch(typ)
        {
            case 0:     // täglich

                if(frequenz == 1)
                {   text = "jeden Tag";
                } else
                {   text = "jeden " + frequenz + ". Tag";
                }
                break;

            case 1:     // monatlich absolut

                if(frequenz == 1)
                {   text = "jedes Monat";
                } else
                {   text = "jedes " + frequenz + ". Monat";
                }

                text += " am " + beginn.getDay() + ". des Monats";
                break;

            case 2:     // monatlich relativ

                if(frequenz == 1)
                {   text = "jedes Monat";
                } else
                {   text = "jedes " + frequenz + ". Monat";
                }

                text += " am " + beginn.getDayRelativ() + ". im Monat";
                break;

            case 3:     // jährlich

                if(frequenz == 1)
                {   text = "jedes Jahr";
                } else
                {   text = "jedes " + frequenz + ". Jahr";
                }

                text += " am " + beginn.getDay() + ". " + MONTHNAMESLONG[beginn.getMonth() - 1];
                break;

            default:    // wöchentlich

                if(frequenz == 1)
                {   text = "jede Woche";
                } else
                {   text = "jede " + frequenz + ". Woche";
                }

                text += " am " + getWochentage();
                break;
        }

        return text;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Attribute setzen // Attribute setzen // Attribute setzen // Attribute setzen //
	//////////////////////////////////////////////////////////////////////////////////


    public void setBeginn(Datum b)
    {   beginn = b;
    }

    public void setEnde(Datum e)
    {   ende = e;
    }

    public void setTyp(int t)
    {   typ = t;
    }

    public void setFrequenz(int f)
    {   frequenz = f;
    }

    public void setWerktags(boolean w)
    {   werktags = w;
    }

    public boolean equals(Serie s)
    {
        return typ == s.getTyp() && frequenz == s.getFrequenz() &&
               werktags == s.getWerktags() && ende.equals(s.getEnde());
    }

    public boolean isVerlängert(Serie s)
    {
        return typ == s.getTyp() && frequenz == s.getFrequenz() &&
               werktags == s.getWerktags() && (ende.isGreater(s.getEnde()) > 0);
    }

	public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("beginn")) return new Long(beginn.getDatum());
        if (attribute.equals("ende")) return new Long(ende.getDatum());
        if (attribute.equals("typ")) return new Integer(typ);
        if (attribute.equals("frequenz")) return new Integer(frequenz);
        if (attribute.equals("werktags"))
        {
            if (werktags) return new Integer(1);
            else return new Integer(0);
        }
        return null;

    }


    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            beginn = new Datum(new java.util.Date(res.getLong(map.getAttribute("beginn").getColumn())));
            ende = new Datum(new java.util.Date(res.getLong(map.getAttribute("ende").getColumn())));
            typ = res.getInt(map.getAttribute("typ").getColumn());
            frequenz = res.getInt(map.getAttribute("frequenz").getColumn());
            werktags = (res.getInt(map.getAttribute("werktags").getColumn()) == 1);
        }catch (SQLException s){System.out.println(s);}
    }

}