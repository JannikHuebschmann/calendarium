package data.sets; //
/////////////////////

import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.FeiertagSetInterface;

////////////////////////////////////////////////////////////////////////////////////////////
// FeiertagSet // FeiertagSet // FeiertagSet // FeiertagSet // FeiertagSet // FeiertagSet //
////////////////////////////////////////////////////////////////////////////////////////////

public class FeiertagSet
{
    private FeiertagSetInterface fi;
    private Data daten;

    public FeiertagSet(Data d, String h)
    {
        daten = d;
        try
        {   fi = (FeiertagSetInterface) Naming.lookup(h + "FeiertagSetRemote");

        } catch(Exception e)
		{   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Feiertage geordnet ausgeben
    public Vector getFeiertage()
    {   try
        {   return fi.getFeiertage();

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Hashtable ausgeben
    public Hashtable getAllFeiertage()
    {   try
        {   return fi.getAllFeiertage();

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(Datum d)
    {   try
        {   return fi.getFeiertagByDate(d);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(GregorianCalendar cal)
    {   try
        {   return fi.getFeiertagByDate(cal);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Ist Feiertag?
    public boolean isFeiertag(GregorianCalendar cal)
    {   try
        {   return fi.isFeiertag(cal);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return false;
    }

    // Feiertag erstellen
    public void create(Feiertag tag)
    {   try
        {   fi.create(Data.user.getKuerzel(),tag);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Feiertag ändern
    public void update(Feiertag tag)
    {   try
        {   fi.update(Data.user.getKuerzel(),tag);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Feiertag löschen
    public void delete(Feiertag tag)
    {   try
        {   fi.delete(Data.user.getKuerzel(),tag);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }
}
