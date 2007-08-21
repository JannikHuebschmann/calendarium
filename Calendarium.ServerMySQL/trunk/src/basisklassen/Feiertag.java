package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

//////////////////////////////////////////////////////////////////////////////////////////////////
// Feiertag // Feiertag // Feiertag // Feiertag // Feiertag // Feiertag // Feiertag // Feiertag //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Feiertag extends dblayer.PersistentObject
{
    private static final long serialVersionUID = 3923422558519261383L;
	private GregorianCalendar date;
    private String bezeichnung;

    public Feiertag(long i)
    {
        super(i);
        date = new GregorianCalendar();
    }

    public Feiertag(GregorianCalendar d, String b)
    {
        super();
        date = d;
        bezeichnung = b;
    }

    public Feiertag(long i, GregorianCalendar d, String b)
    {
        super(i);             // Identifier
        date = d;           // Datum
        bezeichnung = b;    // Bezeichnung
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    // Datum ausgeben
    public GregorianCalendar getDate()
    {   return date;
    }

    // Datum als String ausgeben
    public String getDateString()
    {
        int dy = date.get(Calendar.DATE);
        int mt = date.get(Calendar.MONTH) + 1;
        int yr = date.get(Calendar.YEAR);

        return (dy < 10 ? "0" + dy : "" + dy) + "." +
	           (mt < 10 ? "0" + mt : "" + mt) + "." + yr;
    }

    // Bezeichnung ausgeben
    public String getBezeichnung()
    {   return bezeichnung;
    }

    // Farbe festlegen
    public void setDate(GregorianCalendar d)
    {   date = d;
    }

    // Bezeichnung festlegen
    public void setBezeichnung(String b)
    {   bezeichnung = b;
    }


      /** maps one row from db to instance of EintragsTyp
      */
    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            date.setTime(new java.util.Date(res.getLong(map.getAttribute("date").getColumn())));
            bezeichnung = res.getString(map.getAttribute("bezeichnung").getColumn());
        }catch (SQLException s){System.out.println(s+"www");}
    }

    /** returns value of attribute 'attribute'
      */
    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("date")) return new Long(date.getTime().getTime());
        if (attribute.equals("bezeichnung")) return bezeichnung;
        return null;
    }

}