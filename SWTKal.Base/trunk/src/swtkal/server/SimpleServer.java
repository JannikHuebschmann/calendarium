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
	protected Map<String, Map<String, Vector<Termin>>> teilnehmerTermine;
		// verwaltet die Besitzer-Termin-Assoziation
		// speichert zu jedem Personenkürzel-String eine Map
		// diese Map liefert zu jedem Datums-String einen Vector
		// dieser Vector enthaelt alle Termine zur Besitzerperson am konkreten Datum
// TODO analoge Datenstruktur fuer Teilnehmer-Assoziation einfuegen	

// TODO weitere Javadoc-Kommentare (evtl. aus Server oder interfaces) einfügen	
	protected SimpleServer()
	{
		personen = new HashMap<String, Person>();
		passwoerter = new HashMap<String,String>();
		teilnehmerTermine = new HashMap<String, Map<String, Vector<Termin>>>();
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
				throw new PersonException("Userid is already used!");
		passwoerter.put(kuerzel, passwort);
		personen.put(kuerzel, p);
	}

	public void delete(Person p) throws PersonException
	{
		logger.fine("Deletion of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(kuerzel))
				throw new PersonException("Userid unknown!");
		passwoerter.remove(kuerzel);
		personen.remove(kuerzel);
	}

	public void update(Person p) throws PersonException
	{
		logger.fine("Update of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(kuerzel))
				throw new PersonException("Userid unknown!");
		personen.put(kuerzel, p);
	}

	public void updatePasswort(Person p, String passwort) throws PersonException
	{
		logger.fine("Update of password of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(kuerzel))
				throw new PersonException("Userid unknown!");
		passwoerter.put(kuerzel, passwort);
	}

	public void updateKuerzel(Person p, String neuKuerzel) throws PersonException
	{
		logger.fine("Update of userid of person " + p);
		
		String oldKuerzel = p.getKuerzel();
		if (!passwoerter.containsKey(oldKuerzel))
				throw new PersonException("Userid unknown!");
		if (passwoerter.containsKey(neuKuerzel))
			throw new PersonException("Userid is already used!");
		
		personen.remove(oldKuerzel);
		personen.put(neuKuerzel, p);
		
		passwoerter.put(neuKuerzel, passwoerter.get(oldKuerzel));
		passwoerter.remove(oldKuerzel);
	}

	public Person authenticatePerson(String kuerzel, String passwort)
		throws PersonException
	{
		logger.fine("Authentication of userid " + kuerzel + " with a password");
		
		if (!passwoerter.containsKey(kuerzel))
		{
			logger.warning("Failed authentication for userid " + kuerzel);
			throw new PersonException("Userid unknown!");
		}
		Person p = personen.get(kuerzel);
		if (passwort.equals(passwoerter.get(kuerzel)))
			return p;
		else
		{
			logger.warning("Wrong password for userid " + kuerzel);
			throw new PersonException("Wrong password!");
		}
	}

	public boolean isPersonKnown(String kuerzel)
	{
		return passwoerter.containsKey(kuerzel);
	}

	public Person findPerson(String kuerzel) throws PersonException
	{
		logger.finer("Find person with userid " + kuerzel);
		
		if (!passwoerter.containsKey(kuerzel))
			throw new PersonException("Userid unknown!");
		return personen.get(kuerzel);
	}

	public Vector<Person> getPersonVector()
	{
		logger.finer("Method getPersonVector called");
		
		return new Vector<Person>(personen.values());
	}

	public void insert(Termin termin) throws TerminException
	{
		logger.fine("Insertion of date " + termin);
		
		String kuerzel = termin.getBesitzer().getKuerzel();
		if (!personen.containsKey(kuerzel))
			throw new TerminException("Userid unknown!");

		if (!teilnehmerTermine.containsKey(kuerzel))
		{	// erster Termin fuer diese Person
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);		// einziger Termin
			Map<String, Vector<Termin>> map = new HashMap<String, Vector<Termin>>();
			map.put(termin.getBeginn().getDate(), vector);
			teilnehmerTermine.put(kuerzel, map);
		}
		else if (!teilnehmerTermine.get(kuerzel).containsKey(termin.getBeginn().getDate()))
		{	// erster Termin fuer diese Person an diesem Datum
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);		// einziger Termin
			teilnehmerTermine.get(kuerzel).put(termin.getBeginn().getDate(), vector);
		}
		else
		{	// zusaetzlicher Termin fuer diese Person an diesem Datum
			assert teilnehmerTermine.get(kuerzel).get(termin.getBeginn().getDate())!=null;
			teilnehmerTermine.get(kuerzel).get(termin.getBeginn().getDate()).add(termin);
		}
	}

	public Vector<Termin> getTermineVom(Datum dat, Person tn)
			throws TerminException
	{
		logger.finer("Method getTermineVom called for " + dat);
				
		String kuerzel = tn.getKuerzel();
		if (!server.isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");
				
		Vector<Termin> result = new Vector<Termin>();
				
		Map<String, Vector<Termin>> map = teilnehmerTermine.get(kuerzel);
		if (map!=null )
		{
			Vector<Termin> vector = map.get(dat.getDate());
			if (vector!=null)
				result = vector;
		}
	
		return result;
	}

	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person tn)
			throws TerminException
	{
		logger.finer("Method getTermineVonBis called from " + vonDat + " to " + bisDat);
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Termin termin) throws TerminException
	{
		logger.fine("Deletion of date " + termin);
		
// XXX delete-Implementierung noch überprüfen
// equal-test auf Termine im Vector stimmig?
		String kuerzel = termin.getBesitzer().getKuerzel();
		String datum = termin.getBeginn().getDate();
		
		if (!server.isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");

		assert teilnehmerTermine.get(kuerzel).get(datum)!=null;
		Vector<Termin> terminVector = teilnehmerTermine.get(kuerzel).get(datum);
		terminVector.remove(termin);
	}
}
