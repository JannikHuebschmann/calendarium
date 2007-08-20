package event; //
/////////////////

import java.sql.*;

import basisklassen.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// ToDoEvent // ToDoEvent // ToDoEvent // ToDoEvent // ToDoEvent // ToDoEvent // ToDoEvent //
/////////////////////////////////////////////////////////////////////////////////////////////

public class ToDoEvent extends CalendariumEvent implements EventConstants
{
	private static final long serialVersionUID = -2310115854338092402L;
	private ToDo toDo;

    public ToDoEvent(){}

    public ToDoEvent(Person s, Person e, ToDo t)
    {
        sender = s;     // Sender
        empfänger = e;  // Empfänger
        toDo = t;       // Termin
    }

    public ToDo getToDo()
    {   return toDo;
    }

    public void setToDo(ToDo t)
    {   toDo = t;
    }

    public int getEventID()
    {   return TODO_EVT;
    }

    public void swap(ResultSet res)
    {   super.swap(res);
    }

    public Object getValue(String attribute)
    {   return super.getValue(attribute);
    }

}