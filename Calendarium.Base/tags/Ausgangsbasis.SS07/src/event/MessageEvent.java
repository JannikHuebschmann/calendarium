package event; //
/////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

import basisklassen.Person;

//////////////////////////////////////////////////////////////////////////////////////////////////
// MessageEvent // MessageEvent // MessageEvent // MessageEvent // MessageEvent // MessageEvent //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class MessageEvent extends CalendariumEvent implements EventConstants
{
    private static final long serialVersionUID = -761391361849550887L;
	private String message;
    private String title;

    public MessageEvent(){}

    public MessageEvent(Person s, Person e, String m, String t)
    {
        sender = s;     // Sender
        empfänger = e;  // Empfänger
        message = m;    // Message
        title = t;      // Titel
    }

    public String getMessage()
    {   return message;
    }

    public String getTitle()
    {   return title;
    }

    public int getEventID()
    {   return MESSAGE_EVT;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            message = res.getString(map.getAttribute("message").getColumn());
            title = res.getString(map.getAttribute("title").getColumn());
            super.swap(res);
        }catch (SQLException s){System.out.println(s);}
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("message")) return message;
        if (attribute.equals("title")) return title;
        return super.getValue(attribute);
    }
}