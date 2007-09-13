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
		// verwaltet die Teilnehmer-Termin-Assoziation
		// speichert zu jedem Personenkürzel-String eine Map
		// diese Map liefert zu jedem Datums-String einen Vector
		// dieser Vector enthaelt alle Termine zur Teilnehmerperson am konkreten Datum
// TODO analoge Datenstruktur und Interface-Methoden fuer Besitzer-Assoziation einfuegen	
//	protected Map<String, Map<String, Vector<Termin>>> besitzerTermine;

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
		
		if (isPersonKnown(kuerzel))
				throw new PersonException("Userid is already used!");
		passwoerter.put(kuerzel, passwort);
		personen.put(kuerzel, p);
	}

	public void delete(Person p) throws PersonException
	{
		logger.fine("Deletion of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		passwoerter.remove(kuerzel);
		personen.remove(kuerzel);
// TODO Termine dieser Person auch löschen!		
	}

	public void update(Person p) throws PersonException
	{
		logger.fine("Update of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		personen.put(kuerzel, p);
	}

	public void updatePasswort(Person p, String passwort) throws PersonException
	{
		logger.fine("Update of password of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		passwoerter.put(kuerzel, passwort);
	}

	public void updateKuerzel(Person p, String neuKuerzel) throws PersonException
	{
		logger.fine("Update of userid of person " + p);
		
		String oldKuerzel = p.getKuerzel();
		if (!isPersonKnown(oldKuerzel))
				throw new PersonException("Userid unknown!");
		if (isPersonKnown(neuKuerzel))
			throw new PersonException("Userid is already used!");
		
		personen.remove(oldKuerzel);
		personen.put(neuKuerzel, p);
		
		passwoerter.put(neuKuerzel, passwoerter.get(oldKuerzel));
		passwoerter.remove(oldKuerzel);
// TODO kuerzel auch in der Terminverwaltung ändern		
	}

	public Person authenticatePerson(String kuerzel, String passwort)
		throws PersonException
	{
		logger.fine("Authentication of userid " + kuerzel + " with a password");
		
		if (!isPersonKnown(kuerzel))
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
		
		if (!isPersonKnown(kuerzel))
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
		
		// insert into teilnehmerTermine
		Vector<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p.getKuerzel()))
				throw new TerminException("Userid unknown!");
			insert(termin, p, teilnehmerTermine);
		}

//		// insert into besitzerTermine
//		String kuerzel = termin.getBesitzer().getKuerzel();
//		if (!isPersonKnown(kuerzel))
//			throw new TerminException("Userid unknown!");
//		insert(termin, termin.getBesitzer(), besitzerTermine);
	}

	private void insert(Termin termin, Person p, Map<String, Map<String, Vector<Termin>>> map)
	{
		if (!map.containsKey(p.getKuerzel()))
		{	// first appointment for this person
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);									// only one appointment
			Map<String, Vector<Termin>> dayMap = new HashMap<String, Vector<Termin>>();
			dayMap.put(termin.getBeginn().getDate(), vector);
			map.put(p.getKuerzel(), dayMap);
		}
		else if (!map.get(p.getKuerzel()).containsKey(termin.getBeginn().getDate()))
		{	// first appointment for this date
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);									// only one appointment
			map.get(p.getKuerzel()).put(termin.getBeginn().getDate(), vector);
		}
		else
		{	// additional appointment for this person and this date
			assert map.get(p.getKuerzel()).get(termin.getBeginn().getDate())!=null;
			map.get(p.getKuerzel()).get(termin.getBeginn().getDate()).add(termin);
		}
	}

	public Vector<Termin> getTermineVom(Datum dat, Person tn)
			throws TerminException
	{
		logger.finer("Method getTermineVom called for " + dat);
				
		String kuerzel = tn.getKuerzel();
		if (!isPersonKnown(kuerzel))
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
		// TODO getTermineVonBis noch ausprogrammieren
		return null;
	}

	public void delete(Termin termin) throws TerminException
	{
		logger.fine("Deletion of date " + termin);
		
		// delete from teilnehmerTermine
		Vector<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p.getKuerzel()))
				throw new TerminException("Userid unknown!");
			delete(termin, p, teilnehmerTermine);
		}

//		// delete from besitzerTermine
//		String kuerzel = termin.getBesitzer().getKuerzel();
//		if (!personen.containsKey(kuerzel))
//			throw new TerminException("Userid unknown!");
//		delete(termin, termin.getBesitzer(), besitzerTermine);
	}

	private void delete(Termin termin, Person p, Map<String, Map<String, Vector<Termin>>> map)
	{
		Map<String, Vector<Termin>> dayMap = map.get(p.getKuerzel());
		if (dayMap!=null)
		{
			Vector<Termin> vector = dayMap.get(termin.getBeginn().getDate());
			if (vector!=null)
				vector.remove(termin);
		}
	}
}
