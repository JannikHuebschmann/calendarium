/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007		swtUser			initial version
 *
 */
package swtkal.server;

import java.util.*;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.PersonException;
import swtkal.exceptions.TerminException;
import swtkal.server.Server;

/*****************************************************************************************************
 * Class SimpleServer is a single-user, memory-based server that can be
 * used to easily test the SWTKal application (especially its graphical
 * user interfaces for client and monitor!).
 * 
 * This simplistic implementation intensively uses Java generic collection
 * classes to realize server functionality.
 * 
 * The server is initialized with a user Admin with kuerzel "ADM" and password "admin".
 * Furthermore there are two appointments for the current date.
 * 
 * @author swtUser
 */
public class SimpleServer extends Server
{
	protected Map<String, Person> personen;
	protected Map<String, String> passwoerter;
	protected Map<String, Map<String, Vector<Termin>>> teilnehmerTermine;
		// verwaltet die Teilnehmer-Termin-Assoziationen
		// speichert zu jedem Personenkürzel-String eine Map
		// diese Map liefert zu jedem Datums-String einen Vector
		// dieser Vector enthaelt alle Termine zur Teilnehmerperson am konkreten Datum
//  TODO analoge Datenstruktur und Interface-Methoden fuer Besitzer-Assoziation einfuegen?	
//	protected Map<String, Map<String, Vector<Termin>>> besitzerTermine;

	/** This constructor creates an initial default user and two appointments
	 */
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
		teilnehmerTermine.remove(kuerzel);
		personen.remove(kuerzel);
		passwoerter.remove(kuerzel);
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

	public void updateKuerzel(Person p, String oldKuerzel) throws PersonException
	{
		logger.fine("Update of userid of person " + p);
		
		String neuKuerzel = p.getKuerzel();
		if (neuKuerzel.equals(oldKuerzel)) return;		// nothing to do
		if (!isPersonKnown(oldKuerzel))
				throw new PersonException("Userid unknown!");
		if (isPersonKnown(neuKuerzel))
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
		logger.fine("Find person with userid " + kuerzel);
		
		if (!isPersonKnown(kuerzel))
			throw new PersonException("Userid unknown!");
		return personen.get(kuerzel);
	}

	public Vector<Person> getPersonVector()
	{
		logger.fine("Method getPersonVector called");
		
		return new Vector<Person>(personen.values());
	}

	public void insert(Termin termin) throws TerminException
	{
		logger.fine("Insertion of date " + termin);
		
		// insert into teilnehmerTermine
		Collection<Person> teilnehmer = termin.getTeilnehmer();
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
			dayMap.put(termin.getBeginn().getDateStr(), vector);
			map.put(p.getKuerzel(), dayMap);
		}
		else if (!map.get(p.getKuerzel()).containsKey(termin.getBeginn().getDateStr()))
		{	// first appointment for this date
			Vector<Termin> vector = new Vector<Termin>();
			vector.add(termin);									// only one appointment
			map.get(p.getKuerzel()).put(termin.getBeginn().getDateStr(), vector);
		}
		else
		{	// additional appointment for this person and this date
			assert map.get(p.getKuerzel()).get(termin.getBeginn().getDateStr())!=null;
			map.get(p.getKuerzel()).get(termin.getBeginn().getDateStr()).add(termin);
		}
	}
	
	public void delete(Termin termin) throws TerminException
	{
		logger.fine("Deletion of date " + termin);
		
		// delete from teilnehmerTermine
		Collection<Person> teilnehmer = termin.getTeilnehmer();
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
			Vector<Termin> vector = dayMap.get(termin.getBeginn().getDateStr());
			if (vector!=null)
				vector.remove(termin);
		}
	}

	public void update(Termin termin) throws TerminException 
	{
		// TODO Auto-generated method stub
		throw new TerminException("Not yet implemented!");
	}

	public Termin getTermin(int terminId) throws TerminException
	{
		throw new TerminException("Not yet implemented!");
		// TODO Auto-generated method stub
	}
	
	public void delete(int terminID) throws TerminException
	{
		throw new TerminException("Not yet implemented!");
		// TODO Auto-generated method stub
	}

	public Vector<Termin> getTermineVom(Datum dat, Person tn)
		throws TerminException
	{
		logger.fine("Method getTermineVom called for " + dat);
				
		String kuerzel = tn.getKuerzel();
		if (!isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");
				
		Vector<Termin> result = new Vector<Termin>();
				
		Map<String, Vector<Termin>> map = teilnehmerTermine.get(kuerzel);
		if (map!=null )
		{
			Vector<Termin> vector = map.get(dat.getDateStr());
			if (vector!=null)
				result = vector;
		}
	
		return result;
	}

	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person tn)
		throws TerminException
	{
		logger.fine("Method getTermineVonBis called from " + vonDat + " to " + bisDat);

		String kuerzel = tn.getKuerzel();
		if (!isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");
		if (vonDat.isGreater(bisDat)==1)
			throw new TerminException("Incorrect date interval!");
		
		Vector<Termin> result = new Vector<Termin>();

		Map<String, Vector<Termin>> map = teilnehmerTermine.get(kuerzel);
		if (map!=null )
		{
			Datum d = new Datum(vonDat);
			while (bisDat.isGreater(d)==1)
			{
				Vector<Termin> vector = map.get(d.getDateStr());
				if (vector!=null) result.addAll(vector);
				
				d.add(1);	// next day
			}
		}
		return result;
	}
	
	public Vector<Termin> getTermineVom(Datum dat, Vector<Person> teilnehmer) 
			throws TerminException 
	{
		throw new TerminException("Not yet implemented!");
		// TODO Auto-generated method stub
	}

	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Vector<Person> teilnehmer)
		throws TerminException 
	{
		// TODO Auto-generated method stub
		throw new TerminException("Not yet implemented!");
	}
	
	public Vector<Termin> getBesitzerTermineVom(Datum dat, Person besitzer)
		throws TerminException
	{
		// TODO Auto-generated method stub
		throw new TerminException("Not yet implemented!");
	}

	public Vector<Termin> getBesitzerTermineVonBis(Datum vonDat, Datum bisDat, Person besitzer)
		throws TerminException 
	{
		// TODO Auto-generated method stub
		throw new TerminException("Not yet implemented!");
	}
	
	public boolean isPersonAvailable(Datum vonDat, Datum bisDat, Person teilnehmer) 
		throws TerminException
	{
		// TODO Auto-generated method stub
		throw new TerminException("Not yet implemented!");
	}
		
}
