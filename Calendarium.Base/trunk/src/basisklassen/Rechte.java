package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

////////////////////////////////////////////////////////////////////////////////////////////
// Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht //
////////////////////////////////////////////////////////////////////////////////////////////

public class Rechte extends dblayer.PersistentObject implements java.io.Serializable
{
    private static final long serialVersionUID = 8522547901909295018L;
	private long sender;             // ID
    private long receiver_gruppe = 0;    // ID
    private long receiver_person = 0;    // ID
    private long eintragsTyp;        // ID
    private int rechtsIndex;

    public Rechte()
    {   super();
    }

    public Rechte(long s, long gruppe, long person, long t, int r)
    {
        super();
        sender = s;
        receiver_gruppe = gruppe;
        receiver_person = person;
        eintragsTyp = t;    // Lokale bzw. globale Eintragstyp
        rechtsIndex = r;    // Rechtsindex
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public long getSenderID()
    {   return sender;
    }

    public long getRec_GruppeID()
    {   return receiver_gruppe;
    }

    public long getRec_PersonID()
    {   return receiver_person;
    }

    public long getEintragsTypID()
    {   return eintragsTyp;
    }

    public int getRechtsIndex()
    {   return rechtsIndex;
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("sender")) return new Long(sender);
        if (attribute.equals("receiver_gruppe")) return new Long(receiver_gruppe);
        if (attribute.equals("receiver_person")) return new Long(receiver_person);
        if (attribute.equals("eintragsTyp")) return new Long(eintragsTyp);
        if (attribute.equals("rechtsIndex")) return new Integer(rechtsIndex);
        return null;
    }


    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            sender = res.getLong(map.getAttribute("sender").getColumn());
            receiver_gruppe = res.getLong(map.getAttribute("receiver_gruppe").getColumn());
            receiver_person = res.getLong(map.getAttribute("receiver_person").getColumn());
            eintragsTyp = res.getLong(map.getAttribute("eintragsTyp").getColumn());
            rechtsIndex = res.getInt(map.getAttribute("rechtsIndex").getColumn());

        }catch (SQLException s){System.out.println(s);}
    }

}
