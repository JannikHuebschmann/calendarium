package data; //
////////////////

import java.rmi.*;
import java.util.*;

import event.*;
import data.Data;
import interfaces.MessageServerInterface;

//////////////////////////////////////////////////////////////////////////////////////////
// Message // Message // Message // Message // Message // Message // Message // Message //
//////////////////////////////////////////////////////////////////////////////////////////

public class Message
{
    private MessageServerInterface mi;
    private Data daten;

    public Message(Data d, String h)
    {
        daten = d;
        try
        {   mi = (MessageServerInterface) Naming.lookup(h + "MessageServer");

        } catch(Exception e)
		{	e.printStackTrace();
		    daten.showDialog();
		}
    }

	// Connect
    public Object connect(String kuerzel, String pwd)
    {   try
        {   return mi.connect(kuerzel, pwd);

		} catch(RemoteException e)
        {   e.printStackTrace();
            daten.showDialog();
        }

        return null;
    }

	// Disconnect
    public void disconnect()
    {   try
        {   mi.disconnect(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
            daten.showDialog();
        }
    }

    // Add Listener
    public void addCalendariumListener(CalendariumListenerImpl listener)
    {   try
        {   mi.addCalendariumListenerTo(listener, Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
            daten.showDialog();
        }
    }
    
    // AdminMessage
    public void sendAdminMessage(String text)
    {   try
        {   mi.sendAdminMessage(text);

        } catch(RemoteException e)
        {   e.printStackTrace();
            daten.showDialog();
        }
    }
    
    // ShutDown
    public void shutDown()
    {   try
        {   mi.shutDown();

        } catch(RemoteException e) {}
    }
    
    // Connections
    public Hashtable getConnections()
    {   try
        {   return mi.getConnections();

        } catch(RemoteException e)
        {   e.printStackTrace();
            daten.showDialog();
        }

        return null;
    }
}
