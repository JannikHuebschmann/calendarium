package event; //
/////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import basisklassen.Person;

//////////////////////////////////////////////////////////////////////////////////////////////////////
// CalendariumEvent // CalendariumEvent // CalendariumEvent // CalendariumEvent // CalendariumEvent //
//////////////////////////////////////////////////////////////////////////////////////////////////////

public class CalendariumEvent extends dblayer.PersistentObject implements EventInterface, java.io.Serializable
{
    private static final long serialVersionUID = 2925317584997581471L;
	private String timeStamp;
    public Person sender = null;
    public Person empf�nger = null;

    public CalendariumEvent()
    {
        super();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        timeStamp = simpleDate.format(new java.util.Date());
    }

    public int getEventID()
    {   return 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////
	// Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
	//////////////////////////////////////////////////////////////////////////////////////

    public String getTimeStamp()
    {   return timeStamp;
    }

    public Person getSender()
    {   return sender;
    }

    public Person getEmpf�nger()
    {   return empf�nger;
    }

    public void setSender(Person p)
    {   sender = p;
    }

    public void setEmpf�nger(Person p)
    {   empf�nger = p;
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            timeStamp = res.getString(map.getAttribute("timeStamp").getColumn());
        }catch (SQLException s){System.out.println(s);}
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("sender")) return new Long(sender.getID());
        if (attribute.equals("empf�nger")) return new Long(empf�nger.getID());
        if (attribute.equals("timeStamp")) return timeStamp;
        if (attribute.equals("eventID")) return new Integer(getEventID());
        return null;
    }
}