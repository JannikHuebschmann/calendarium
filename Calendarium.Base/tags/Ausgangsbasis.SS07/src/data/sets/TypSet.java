package data.sets; //
/////////////////////

import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.TypSetInterface;

////////////////////////////////////////////////////////////////////////////////////////////
// TypSet // TypSet // TypSet // TypSet // TypSet // TypSet // TypSet // TypSet // TypSet //
////////////////////////////////////////////////////////////////////////////////////////////

public class TypSet
{
    private TypSetInterface ti;
    private Data daten;

    public TypSet(Data d, String h)
    {
        daten = d;
        try
        {   ti = (TypSetInterface) Naming.lookup(h + "TypSetRemote");

        } catch(Exception e)
		    {   e.printStackTrace();
      			daten.showDialog();
        }
	  }


    // Hashtable ausgeben
    public Hashtable getDefaultTypen()
    {   try
        {   return ti.getDefaultTypen();

        } catch(RemoteException e)
        {   e.printStackTrace();
      			daten.showDialog();
        }

        return null;
    }

    // Hashtable fuer User ausgeben
    public Hashtable getUserTypen()
    {   try
        {   return ti.getUserTypen(Data.user);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Eintragstyp ausgeben
    public EintragsTyp getDefaultTypByID(long id)
    {   try
        {   return ti.getDefaultTypByID(id);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Eintragstyp ausgeben
    public EintragsTyp getUserTypByID(long id)
    {   try
        {   return ti.getUserTypByID(Data.user, id);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

	// Eintragstyp ausgeben
    public EintragsTyp getUserTyp(EintragsTyp typ)
    {   try
        {   return ti.getUserTyp(Data.user, typ);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }

        return null;
    }

    // Usertyp setzen
    public void setUserTyp(EintragsTyp t)
    {   try
        {   ti.setUserTyp(Data.user, t);

        } catch(RemoteException e)
        {   e.printStackTrace();
			      daten.showDialog();
        }
    }

    // Defaulttyp erstellen
    public void create(EintragsTyp t)
    {   try
        {
            ti.create(Data.user.getKuerzel(),t);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Defaulttyp löschen
    public void delete(EintragsTyp t)
    {   try
        {   ti.delete(Data.user.getKuerzel(),t);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }

    // Defaulttyp ändern
    public void update(EintragsTyp t)
    {   try
        {   ti.update(Data.user.getKuerzel(),t);

        } catch(RemoteException e)
        {   e.printStackTrace();
			daten.showDialog();
        }
    }
}
