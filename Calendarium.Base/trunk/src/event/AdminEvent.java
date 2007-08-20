package event; //
/////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

////////////////////////////////////////////////////////////////////////////////////////////////////
// AdminEvent // AdminEvent // AdminEvent // AdminEvent // AdminEvent // AdminEvent // AdminEvent //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class AdminEvent extends CalendariumEvent implements EventConstants
{
	private static final long serialVersionUID = 8265703890328887583L;
	private String message;

    public AdminEvent(){}

    public AdminEvent(String m)
    {
        message = m;    // Message
    }

    public String getMessage()
    {   return message;
    }

    public int getEventID()
    {   return ADMIN_EVT;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            message = res.getString(map.getAttribute("message").getColumn());
            super.swap(res);
        }catch (SQLException s){System.out.println(s);}
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("message")) return message;
        return super.getValue(attribute);
    }

}