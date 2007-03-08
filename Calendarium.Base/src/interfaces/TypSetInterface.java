package interfaces; //
//////////////////////

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

import basisklassen.EintragsTyp;
import basisklassen.Person;

public interface TypSetInterface extends Remote
{

    // Hashtable ausgeben
    public Hashtable getDefaultTypen() throws RemoteException;

    // Hashtable fuer User ausgeben
    public Hashtable getUserTypen(Person p) throws RemoteException;

    // Eintragstyp ausgeben
    public EintragsTyp getDefaultTypByID(long id) throws RemoteException;

    // Eintragstyp ausgeben
    public EintragsTyp getUserTypByID(Person p, long id) throws RemoteException;

	// Eintragstyp ausgeben
    public EintragsTyp getUserTyp(Person p, EintragsTyp typ) throws RemoteException;

    // Usertyp setzen
    public void setUserTyp(Person p, EintragsTyp typ) throws RemoteException;

    // Defaulttyp erstellen
    public void create(String kuerzel, EintragsTyp typ) throws RemoteException;

    // Defaulttyp löschen
    public void delete(String kuerzel, EintragsTyp typ) throws RemoteException;

    // Defaulttyp ändern
    public void update(String kuerzel, EintragsTyp typ) throws RemoteException;
}