package swtkal.server;

import java.util.*;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.PersonException;
import swtkal.exceptions.TerminException;
import swtkal.server.Server;

public class SimpleServer extends Server
{
	protected Map<String, Person> personen;
	protected Map<String, String> passwoerter;
	protected Map<String, Map<String, Vector<Termin>>> termine;
		// speichert zu jedem Personenkürzel-String eine Map
		// diese Map liefert zu jedem Datums-String einen Vector
		// dieser Vector enthaelt alle Termine zur Person am konkreten Datum
	
	protected SimpleServer()
	{
		personen = new HashMap<String, Person>();
		passwoerter = new HashMap<String,String>();
		termine = new HashMap<String, Map<String, Vector<Termin>>>();
		try
		{
			// Administrator als initialer Default-User 
			Person p = new Person("SWTKal", "Admin", "ADM");
			insert(p, "admin");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startServer()
	{
		if (!isServerRunning)
		{
			System.out.println("Server starten ...");
			// TODO Daten aus Datei einlesen
			isServerRunning = true;
		}
	}

	public void stopServer()
	{
		if (isServerRunning)
		{
			System.out.println("Server stoppen ...");
			// TODO Daten in Datei wegschreiben
			isServerRunning = false;
		}
	}
	
	public boolean isServerRunning()
	{
		return isServerRunning;
	}

	public void insert(Person p, String passwort) throws PersonException
	{
		String kuerzel = p.getKuerzel();
		
		if (passwoerter.containsKey(kuerzel))
				throw new PersonException("Kuerzel bereits vorhanden!");
		passwoerter.put(kuerzel, passwort);
		personen.put(kuerzel, p);
	}

	public Person authenticate(String kuerzel, String passwort)
		throws PersonException
	{
		if (!passwoerter.containsKey(kuerzel))
			throw new PersonException("Kuerzel unbekannt!");
		Person p = personen.get(kuerzel);
		if (passwort.equals(passwoerter.get(kuerzel)))
			return p;
		else
			throw new PersonException("Passwort fehlerhaft!");
	}

	public boolean contains(String kuerzel)
	{
		return passwoerter.containsKey(kuerzel);
	}

	public void delete(Person p) throws PersonException
	{
		String kuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(kuerzel))
				throw new PersonException("Kuerzel unbekannt!");
		passwoerter.remove(kuerzel);
		personen.remove(kuerzel);
	}

	public Person find(String kuerzel) throws PersonException
	{
		if (!passwoerter.containsKey(kuerzel))
			throw new PersonException("Kuerzel unbekannt!");
		return personen.get(kuerzel);
	}

	public Vector<Person> getOrderedVector()
	{
		// TODO Vector muss noch sortiert werden
		return new Vector<Person>(personen.values());
	}

	public void update(Person p) throws PersonException
	{
	// TODO Auto-generated method stub

	}

	public void update(Person p, String passwort) throws PersonException
	{
	// TODO Auto-generated method stub

	}

	public void insert(Termin termin) throws TerminException
	{
		String kuerzel = termin.getBesitzer().getKuerzel();
		if (!server.contains(kuerzel))
			throw new TerminException("Besitzer des Termins ist unbekannt!");

		if (!termine.containsKey(kuerzel))
		{	// erster Termin fuer diese Person
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);		// einziger Termin
			Map<String, Vector<Termin>> map = new HashMap<String, Vector<Termin>>();
			map.put(termin.getBeginn().getDate(), vector);
			termine.put(kuerzel, map);
		}
		else if (!termine.get(kuerzel).containsKey(termin.getBeginn().getDate()))
		{	// erster Termin fuer diese Person an diesem Datum
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);		// einziger Termin
			termine.get(kuerzel).put(termin.getBeginn().getDate(), vector);
		}
		else
		{
			termine.get(kuerzel).get(termin.getBeginn().getDate()).add(termin);
		}
	}

	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person person)
			throws TerminException
	{
		// TODO getTermineVonBis ausprogrammieren insbesondere der bis-Teil
		// durch die einzelnen Tage laufen und Termine aufsammeln
		String kuerzel = person.getKuerzel();
		if (!server.contains(kuerzel))
			throw new TerminException("Besitzer des Termins ist unbekannt!");
		
		Vector<Termin> result = new Vector<Termin>();
		
		Map<String, Vector<Termin>> map = termine.get(kuerzel);
		if (map!=null )
		{
			Vector<Termin> vector = map.get(vonDat.getDate());
			if (vector!=null)
				result = vector;
		}
		
		return result;
	}

	public void delete(Termin termin) throws TerminException
	{
		// TODO delete-Implementierung noch überprüfen
		// equal-test auf Termine im Vector stimmig?
		String kuerzel = termin.getBesitzer().getKuerzel();
		String datum = termin.getBeginn().getDate();
		
		Vector terminVector = termine.get(kuerzel).get(datum);
		terminVector.remove(termin);
	}
}
