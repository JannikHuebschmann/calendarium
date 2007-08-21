package basisklassen; //
////////////////////////

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

////////////////////////////////////////////////////////////////////////////////////////////
// EintragsTyp // EintragsTyp // EintragsTyp // EintragsTyp // EintragsTyp // EintragsTyp //
////////////////////////////////////////////////////////////////////////////////////////////

public class EintragsTyp extends dblayer.PersistentObject
{
    private static final long serialVersionUID = -7408504963886756456L;
	private Color farbe;
    private String bezeichnung;
    private long owner;    // wird nur fuer Datenbank benötigt
    
    //konstruktor fuer uebergabe von objekten --> typecast
    //von johnny
    public EintragsTyp(Object obj){
    	super();
    	
    }

    public EintragsTyp(long i)
    {
        super(i);
    }

    public EintragsTyp(Color f, String b)
    {
        super();
        farbe = f;
        bezeichnung = b;
    }

    public EintragsTyp(long i, Color f, String b)
    {
        super(i);
        farbe = f;          // Farbe
        bezeichnung = b;    // Bezeichnung
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    // Owner-ID ausgeben
    public long getOwner()
    {   return owner;
    }

    // Bezeichnung ausgeben
    public String getBezeichnung()
    {   return bezeichnung;
    }

    // Farbe ausgeben
    public Color getFarbe()
    {   return farbe;
    }

    public void setOwner(long o)
    {   owner = o;
    }

    // Farbe festlegen
    public void setFarbe(Color f)
    {   farbe = f;
    }

    // Bezeichnung festlegen
    public void setBezeichnung(String b)
    {   bezeichnung = b;
    }

    //////////////////////////////////////////////////////////////////////


    /** maps one row from db to instance of EintragsTyp
      */
    public void swap(ResultSet res)
    {

        try
        {
            map = getMapping();
            objectIdentifier = res.getLong(map.getAttribute("objectIdentifier").getColumn());
            bezeichnung = res.getString(map.getAttribute("bezeichnung").getColumn());
            owner = res.getLong(map.getAttribute("owner").getColumn());
            farbe = new Color(res.getInt(map.getAttribute("farbe").getColumn()));

        }catch (SQLException s){System.out.println(s);}

    }

    /** returns value of attribute 'attribute'
      */
    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("bezeichnung")) return bezeichnung;
        if (attribute.equals("owner")) return new Long(owner);
        if (attribute.equals("farbe")) return new Integer(farbe.getRGB());
        return null;
    }


}