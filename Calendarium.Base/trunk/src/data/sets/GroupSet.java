package data.sets; //
/////////////////////

import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.GroupSetInterface;

//////////////////////////////////////////////////////////////////////////////////////////////////
// GroupSet // GroupSet // GroupSet // GroupSet // GroupSet // GroupSet // GroupSet // GroupSet //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class GroupSet
{
    private GroupSetInterface gi;
    private Data daten;

    public GroupSet(Data d, String h)
    {
        daten = d;
        try
        {   gi = (GroupSetInterface) Naming.lookup(h + "GroupSetRemote");

        } catch(Exception e)
		{   e.printStackTrace();
			daten.showDialog();
        }
    }

    // AdminRootGruppe ausgeben
	public Gruppe getAdminRoot()
    {   try
        {   return gi.getAdminRoot();

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // UserRootGruppe ausgeben
    public Gruppe getUserRoot()
	{   try
        {   return gi.getUserRoot(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

	// Gruppe by ID
	public Gruppe getByID(long id)
	{	try
        {   return gi.getByID(id);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
	}

    // Kuerzel vergeben?
	public boolean contains(String kuerzel)
	{   try
        {   return gi.contains(kuerzel);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return false;
    }

	// Gruppen neu verketten
	public void exchangeOrder(Gruppe gruppe, Gruppe parentOld, Gruppe parentNew)
	{   try
        {   gi.exchangeOrder(Data.user.getKuerzel(), gruppe, parentOld, parentNew);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Gruppe anlegen
    public void createGroupIn(Gruppe gruppe, Gruppe parent)
	{   try
        {   gi.createGroupIn(Data.user, gruppe, parent);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Gruppe löschen
    public void deleteGroupOf(Gruppe gruppe, Gruppe parent)
    {   try
        {   gi.deleteGroupOf(Data.user, gruppe, parent);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Gruppe ändern
    public void update(Gruppe gruppe)
    {   try
        {   gi.update(Data.user, gruppe);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }
}
