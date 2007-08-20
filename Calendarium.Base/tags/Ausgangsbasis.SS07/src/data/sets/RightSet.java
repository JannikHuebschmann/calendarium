package data.sets; //
/////////////////////

import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.RightSetInterface;

//////////////////////////////////////////////////////////////////////////////////////////////////
// RightSet // RightSet // RightSet // RightSet // RightSet // RightSet // RightSet // RightSet //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class RightSet
{
    private RightSetInterface ri;
    private Data daten;

    public RightSet(Data d, String h)
    {
        daten = d;
        try
        {   ri = (RightSetInterface) Naming.lookup(h + "RightSetRemote");

        } catch(Exception e)
		{   e.printStackTrace();
			daten.showDialog();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // fuer Gruppen vergebene Rechte
    public Hashtable getGroupGrants()
    {   try
        {   return ri.getGroupGrants(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // fuer Personen vergebene Rechte
    public Hashtable getPersonGrants()
    {   try
        {   return ri.getPersonGrants(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // fuer eine Gruppe vergebene Rechte
	public Vector getGrantsAt(Gruppe empf�nger)
	{   try
        {   return ri.getGrantsAt(Data.user, empf�nger);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // fuer eine Person vergebene Rechte
	public Vector getGrantsAt(Person empf�nger)
    {   try
        {   return ri.getGrantsAt(Data.user, empf�nger);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Rechte einer Person erweitern
	public void addGrantAt(Person empf�nger, Recht recht)
    {   try
        {   ri.addGrantAt(Data.user, empf�nger, recht);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Rechte einer Gruppe erweitern
	public void addGrantAt(Gruppe empf�nger, Recht recht)
    {   try
        {   ri.addGrantAt(Data.user, empf�nger, recht);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Person ein Recht entziehen
	public void retractGrantAt(Person empf�nger, Recht recht)
	{   try
        {   ri.retractGrantAt(Data.user, empf�nger, recht);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

	// Gruppe ein Recht entziehen
	public void retractGrantAt(Gruppe empf�nger, Recht recht)
	{   try
        {   ri.retractGrantAt(Data.user, empf�nger, recht);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

	// Person berechtigen
	public void addReceiver(Person empf�nger, Vector rechte)
    {   try
        {   ri.addReceiver(Data.user, empf�nger, rechte);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Gruppe berechtigen
	public void addReceiver(Gruppe empf�nger, Vector rechte)
	{   try
        {   ri.addReceiver(Data.user, empf�nger, rechte);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

	// Person entfernen
	public void deleteReceiver(Person empf�nger)
    {   try
        {   ri.deleteReceiver(Data.user, empf�nger);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Gruppe entfernen
	public void deleteReceiver(Gruppe empf�nger)
    {   try
        {   ri.deleteReceiver(Data.user, empf�nger);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

	// Hashtable von Personen
	public Hashtable getGrantPersons()
	{	try
        {   return ri.getGrantPersons(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

		return null;
	}

	// Hashtable von Gruppen
	public Hashtable getGrantGroups()
	{	try
        {   return ri.getGrantGroups(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

		return null;
	}

    //////////////////////////////////////////////////////////////////////////////////
    // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // S�mtliche erhaltenen Rechte
    public Hashtable getRechte()
    {   try
        {   return ri.getRechte(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Ist jemand bei jemanden berechtigt?
	public boolean isBerechtigt(Person bei, Recht recht)
	{   try
        {   return ri.isBerechtigt(Data.user, bei, recht);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return false;
    }
}
