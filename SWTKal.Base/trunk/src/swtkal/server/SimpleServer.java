package swtkal.server;

import java.util.*;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.PersonException;
import swtkal.exceptions.TerminException;
import swtkal.server.Server;

/**
 * Class SimpleServer is a single-user, memory-based Server that can be
 * used to easily test the SWTKal application.
 *
 */
public class SimpleServer extends Server
{
	protected Map<String, Person> personen;
	protected Map<String, String> passwoerter;
	protected Map<String, Map<String, Vector<Termin>>> termine;
		// speichert zu jedem Personenkürzel-String eine Map
		// diese Map liefert zu jedem Datums-String einen Vector
		// dieser Vector enthaelt alle Termine zur Person am konkreten Datum

// TODO weitere Javadoc-Kommentare (evtl. aus Server oder interfaces) einfügen	
	protected SimpleServer()
	{
		personen = new HashMap<String, Person>();
		passwoerter = new HashMap<String,String>();
		termine = new HashMap<String, Map<String, Vector<Termin>>>();
		try
		{
			// administrator as initial default user 
			Person p = new Person("SWTKal", "Admin", "ADM");
			insert(p, "admin");
			//	two simple test dates for today
			insert(new Termin(p, "1. Testtermin", "Dies ist der Langtext zum 1. Testtermin",
					new Datum(new Date()), new Datum(new Date()).addDauer(1)));
			insert(new Termin(p, "2. Testtermin", "Dies ist der Langtext zum 2. Testtermin",
						new Datum(new Date()).addDauer(1.5), new Datum(new Date()).addDauer(2.5)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insert(Person p, String passwort) throws PersonException
	{
		logger.fine("Insertion of person " + p + " with a password");
		
		String kuerzel = p.getKuerzel();
		
		if (passwoerter.containsKey(kuerzel))
				throw new PersonException("Kuerzel bereits vorhanden!");
		passwoerter.put(kuerzel, passwort);
		personen.put(kuerzel, p);
	}

	public Person authenticate(String kuerzel, String passwort)
		throws PersonException
	{
		logger.fine("Authentication of userid " + kuerzel + " with a password");
		
		if (!passwoerter.containsKey(kuerzel))
		{
			logger.warning("Failed authentication for userid " + kuerzel);
			throw new PersonException("Kuerzel unbekannt!");
		}
		Person p = personen.get(kuerzel);
		if (passwort.equals(passwoerter.get(kuerzel)))
			return p;
		else
		{
			logger.warning("Wrong password for userid " + kuerzel);
			throw new PersonException("Passwort fehlerhaft!");
		}
	}

	public boolean contains(String kuerzel)
	{
		return passwoerter.containsKey(kuerzel);
	}

	public void delete(Person p) throws PersonException
	{
		logger.fine("Deletion of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(kuerzel))
				throw new PersonException("Kuerzel unbekannt!");
		passwoerter.remove(kuerzel);
		personen.remove(kuerzel);
	}

	public Person find(String kuerzel) throws PersonException
	{
		logger.finer("Find person with userid " + kuerzel);
		
		if (!passwoerter.containsKey(kuerzel))
			throw new PersonException("Kuerzel unbekannt!");
		return personen.get(kuerzel);
	}

	public Vector<Person> getOrderedVector()
	{
// FIXME Vector muss noch sortiert werden (wonach eigentlich?)
		logger.finer("Method getOrderedVector called");
		
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
		logger.fine("Insertion of date " + termin);
		
		String kuerzel = termin.getBesitzer().getKuerzel();
		if (!personen.containsKey(kuerzel))
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
		{	// zusaetzlicher Termin fuer diese Person an diesem Datum
			assert termine.get(kuerzel).get(termin.getBeginn().getDate())!=null;
			termine.get(kuerzel).get(termin.getBeginn().getDate()).add(termin);
		}
	}

	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person person)
			throws TerminException
	{
// FIXME getTermineVonBis ausprogrammieren insbesondere der bis-Teil
// durch die einzelnen Tage laufen und Termine aufsammeln
		logger.finer("Method getTermineVonBis called from " + vonDat + " to " + bisDat);
		
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
		logger.fine("Deletion of date " + termin);
		
// XXX delete-Implementierung noch überprüfen
// equal-test auf Termine im Vector stimmig?
		String kuerzel = termin.getBesitzer().getKuerzel();
		String datum = termin.getBeginn().getDate();
		
		if (!server.contains(kuerzel))
			throw new TerminException("Besitzer des Termins ist unbekannt!");

		assert termine.get(kuerzel).get(datum)!=null;
		Vector<Termin> terminVector = termine.get(kuerzel).get(datum);
		terminVector.remove(termin);
	}
}
