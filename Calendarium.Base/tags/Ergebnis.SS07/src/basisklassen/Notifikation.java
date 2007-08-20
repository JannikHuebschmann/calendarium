package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

//////////////////////////////////////////////////////////////////////////////////////////////////
// Notifikation // Notifikation // Notifikation // Notifikation // Notifikation // Notifikation //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Notifikation extends dblayer.PersistentObject implements data.Shared
{
    private static final long serialVersionUID = -3413623479807517164L;
	private int typ;            // Notifikationstyp
    private int zeit;           // Zeiteinheiten vorher
    private int zeitEH;         // Zeiteinheit
    private boolean erledigt;   // Erledigt Flag


    public Notifikation(long i)
    {   super(i);
    }

    public Notifikation(int t, int eh, int z)
    {
        super();
        typ = t;
        zeitEH = eh;
        zeit = z;
        erledigt = false;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public void setTyp(int t)
    {   typ = t;
    }

    public void setZeit(int z)
    {   zeit = z;
    }

    public void setZeitEinheit(int z)
    {   zeitEH = z;
    }

    public void setErledigt(boolean e)
    {   erledigt = e;
    }

    public int getTyp()
    {   return typ;
    }

    public int getZeit()
    {   return zeit;
    }

    public int getZeitEinheit()
    {   return zeitEH;
    }

    public boolean isErledigt()
    {   return erledigt;
    }

    public double getStunden()
    {
        if(zeitEH == ZE_MINUTEN)
        {   return ((double) zeit) / 60;

        } else if(zeitEH == ZE_STUNDEN)
        {   return zeit;

        } else return zeit * 24;
    }

    public boolean equals(Notifikation nfkt)
    {   return typ == nfkt.getTyp() && zeit == nfkt.getZeit() && zeitEH == nfkt.getZeitEinheit();
    }

    public String toString()
    {   return NFKT_TYP[typ] + ", " + zeit + " " + ZE_TYP[zeitEH] + " vorher";
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("typ")) return new Integer(typ);
        if (attribute.equals("zeit")) return new Integer(zeit);
        if (attribute.equals("zeitEH")) return new Integer(zeitEH);
        if (attribute.equals("erledigt"))
        {
            if (erledigt) return new Integer(1);
            else return new Integer(0);
        }
        return null;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            typ = res.getInt(map.getAttribute("typ").getColumn());
            zeit = res.getInt(map.getAttribute("zeit").getColumn());
            zeitEH = res.getInt(map.getAttribute("zeitEH").getColumn());
            erledigt = (res.getInt(map.getAttribute("erledigt").getColumn()) == 1);
        }catch (SQLException s){System.out.println(s);}

    }
}