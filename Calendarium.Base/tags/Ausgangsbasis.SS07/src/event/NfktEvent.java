package event; //
/////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

import basisklassen.Person;

/////////////////////////////////////////////////////////////////////////////////////////////
// NfktEvent // NfktEvent // NfktEvent // NfktEvent // NfktEvent // NfktEvent // NfktEvent //
/////////////////////////////////////////////////////////////////////////////////////////////

public class NfktEvent extends CalendariumEvent implements EventConstants
{
    private static final long serialVersionUID = 6688198918142908069L;
	private String message;
    private String title;
    private int delivery;
    private boolean showing = true;

    public NfktEvent(){}

    public NfktEvent(Person s, Person e, String m, String t, int d)
    {
        sender = s;     // Sender
        empfänger = e;  // Empfänger
        message = m;    // Message
        title = t;      // Title
        delivery = d;   // Versandart
    }

    public String getMessage()
    {   return message;
    }

    public String getTitle()
    {   return title;
    }

    public int getDelivery()
    {   return delivery;
    }

    public boolean isShowing()
    {   return showing;
    }

    public int getEventID()
    {   return NFKT_EVT;
    }

    public void setShowing(boolean s)
    {   showing = s;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            message = res.getString(map.getAttribute("message").getColumn());
            title = res.getString(map.getAttribute("title").getColumn());
            delivery = res.getInt(map.getAttribute("delivery").getColumn());
            showing = (res.getInt(map.getAttribute("showing").getColumn()) == 1);
            super.swap(res);
        }catch (SQLException s){System.out.println(s);}
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("message")) return message;
        if (attribute.equals("title")) return title;
        if (attribute.equals("delivery")) return new Integer(delivery);
        if (attribute.equals("showing"))
        {
            if (showing) return new Integer(1);
            else return new Integer(0);
        }
        return super.getValue(attribute);
    }

}