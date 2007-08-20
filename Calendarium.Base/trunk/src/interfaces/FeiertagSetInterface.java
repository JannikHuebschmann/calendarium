package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import basisklassen.Datum;
import basisklassen.Feiertag;

public interface FeiertagSetInterface extends Remote
{
    // Feiertage geordnet ausgeben
    public Vector getFeiertage() throws RemoteException;

    // Hashtable ausgeben
    public Hashtable getAllFeiertage() throws RemoteException;

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(Datum d) throws RemoteException;

    // Feiertag ausgeben
    public Feiertag getFeiertagByDate(GregorianCalendar cal) throws RemoteException;

    // Ist Feiertag?
    public boolean isFeiertag(GregorianCalendar cal) throws RemoteException;

    // Feiertag anlegen
    public void create(String kuerzel, Feiertag tag) throws RemoteException;

    // Feiertag löschen
    public void delete(String kuerzel, Feiertag tag) throws RemoteException;

    // Feiertag updaten
    public void update(String kuerzel, Feiertag tag) throws RemoteException;
}