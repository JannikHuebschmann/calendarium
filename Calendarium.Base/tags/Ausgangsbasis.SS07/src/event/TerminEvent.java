package event; //
/////////////////

import java.sql.*;

import basisklassen.*;

////////////////////////////////////////////////////////////////////////////////////////////
// TerminEvent // TerminEvent // TerminEvent // TerminEvent // TerminEvent // TerminEvent //
////////////////////////////////////////////////////////////////////////////////////////////

public class TerminEvent extends CalendariumEvent implements EventConstants
{
	private static final long serialVersionUID = -7777137836942724610L;
	private Termin termin;

    public TerminEvent(){}

    public TerminEvent(Person s, Person e, Termin t)
    {
        sender = s;     // Sender
        empfänger = e;  // Empfänger
        termin = t;     // Termin
    }

    public Termin getTermin()
    {   return termin;
    }

    public void setTermin(Termin t)
    {   termin = t;
    }

    public int getEventID()
    {   return TERMIN_EVT;
    }

    public void swap(ResultSet res)
    {   super.swap(res);
    }

    public Object getValue(String attribute)
    {   return super.getValue(attribute);
    }

}