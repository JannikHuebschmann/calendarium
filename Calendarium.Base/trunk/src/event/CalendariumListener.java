package event; //
/////////////////

import java.rmi.*;

//////////////////////////////////////////////////////////////////////////////////////////////
// CalendariumListener // CalendariumListener // CalendariumListener // CalendariumListener //
//////////////////////////////////////////////////////////////////////////////////////////////

public interface CalendariumListener extends Remote
{
    // Event Handling
    public void processCalendariumEvent(CalendariumEvent e) throws RemoteException;
    
    // isActive
    public boolean isActive() throws RemoteException;
}