package data.sets; //
/////////////////////

import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.Person;
import interfaces.PersonSetInterface;

/////////////////////////////////////////////////////////////////////////////////////////////
// PersonSet // PersonSet // PersonSet // PersonSet // PersonSet // PersonSet // PersonSet //
/////////////////////////////////////////////////////////////////////////////////////////////

public class PersonSet
{
    private PersonSetInterface pi;
    private Data daten;

    public PersonSet(Data d, String h)
    {
        daten = d;
        try
        {   pi = (PersonSetInterface) Naming.lookup(h + "PersonSetRemote");

        } catch(Exception e)
		{   e.printStackTrace();
			daten.showDialog();
        }
    }

    public Vector getOrderedList()
    {   try
        {   return pi.getOrderedList();

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    public Person getByKuerzel(String kuerzel)
    {   try
        {   return pi.getByKuerzel(kuerzel);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

	public Person getByID(long id)
    {   try
        {   return pi.getByID(id);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    public boolean contains(String kuerzel)
    {   try
        {   return pi.contains(kuerzel);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return false;
    }

    public void create(Person person)
    {   try
        {   pi.create(Data.user.getKuerzel(),person);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    public void update(Person person)
    {   try
        {   pi.update(Data.user.getKuerzel(),person);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    public void delete(Person person)
    {   try
        {   pi.delete(Data.user.getKuerzel(),person);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }
}
