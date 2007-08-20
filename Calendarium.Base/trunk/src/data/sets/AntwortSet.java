/*
 * Created on 25.03.2005
 *
 */
package data.sets;

import interfaces.AntwortSetInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

import basisklassen.Datum;
import basisklassen.Eintrag;
import basisklassen.Person;
import data.Data;

/**
 * @author MartiniDestroyer
 *
 */
public class AntwortSet{

	private AntwortSetInterface ti;
    private Data daten;

    public AntwortSet(Data d, String h)
    {
        daten = d;
        try
        {   ti = (AntwortSetInterface) Naming.lookup(h + "AntwortSetRemote");

        } catch(Exception e)
                {   e.printStackTrace();
                    System.out.println("<<<Error "+e.getMessage());
                        daten.showDialog();
        }
    }

    // Personen mit Terminen
    public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat, Vector personenListe)
    {   return ((AntwortSet) ti).getPersonsWithTermin(vonDat, bisDat, personenListe);
        
    }
    
    public void sendMessage(Eintrag eintrag,Vector vtn, String antwort)
    {   try
        {	ti.sendMessage(eintrag, vtn, antwort); //nachricht geschickt ueber interface 

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }
    
    public void sendMessage(Eintrag eintrag, Person pers, String antwort){
    	try {
			ti.sendMessage(eintrag,pers,antwort);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
	
}
