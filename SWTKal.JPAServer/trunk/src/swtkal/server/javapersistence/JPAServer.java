/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.03.2008
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.03.2008			ejbUser			initial version
 *
 */
package swtkal.server.javapersistence;

import java.util.*;

import javax.persistence.*;
import swtkal.domain.*;
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
 * @author ejbUser
 */
// TODO javadoc Kommentar erneuern

public class JPAServer extends Server
{	
	protected EntityManager manager;
	protected EntityTransaction tx;

	/** This constructor creates an initial default user and two appointments
	 */
	public JPAServer()
	{
		EntityManagerFactory factory =
			Persistence.createEntityManagerFactory("swtkal.mysql");
		manager = factory.createEntityManager();
		tx = manager.getTransaction();
		
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
		
		tx.begin();
			manager.persist(new Passwort(kuerzel,passwort));
			manager.persist(p);
		tx.commit();
	}

	public void delete(Person p) throws PersonException
	{
		logger.fine("Deletion of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		// FIXME die Person müsste auch als Teilnehmer in allen Terminen gelöscht werden!
		
		tx.begin();
			manager.remove(manager.find(Passwort.class, kuerzel));
			manager.remove(manager.find(Person.class, kuerzel));
		tx.commit();
	}

	public void update(Person p) throws PersonException
	{
		logger.fine("Update of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		
		tx.begin();
			p = manager.merge(p);
		tx.commit();
	}

	public void updatePasswort(Person p, String passwort) throws PersonException
	{
		logger.fine("Update of password of person " + p);
		
		String kuerzel = p.getKuerzel();
		if (!isPersonKnown(kuerzel))
				throw new PersonException("Userid unknown!");
		
		tx.begin();
			manager.merge(new Passwort(kuerzel, passwort));
		tx.commit();
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
		
		tx.begin();
			manager.remove(manager.find(Person.class, oldKuerzel));
			Passwort pass = manager.find(Passwort.class, oldKuerzel);
			manager.remove(pass);
			manager.persist(p);
			pass.setKuerzel(neuKuerzel);
			manager.persist(pass);
		tx.commit();
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
		
		Passwort pass = manager.find(Passwort.class, kuerzel);
		if (passwort.equals(pass.getPasswort()))
			return manager.find(Person.class, kuerzel);
		else
		{
			logger.warning("Wrong password for userid " + kuerzel);
			throw new PersonException("Wrong password!");
		}
	}

// TODO hier noch eine andere überladene Version als Hilfsmethode mit Person als Parametertyp???
	public boolean isPersonKnown(String kuerzel)
	{
		Person p = manager.find(Person.class, kuerzel);
		return p!=null;
	}

	public Person findPerson(String kuerzel) throws PersonException
	{
		logger.fine("Find person with userid " + kuerzel);
		
		Person p = manager.find(Person.class, kuerzel);
		if (p==null)
			throw new PersonException("Userid unknown!");
		return p;
	}

	@SuppressWarnings("unchecked")
	public Vector<Person> getPersonVector()
	{
		logger.fine("Method getPersonVector called");
		
		tx.begin();
			Query query = manager.createQuery("select p from Person p");
			List<Person>  results = (List<Person>) query.getResultList();
		tx.commit();
		
		return new Vector<Person>(results);
	}

	public void insert(Termin termin) throws TerminException
	{
		logger.fine("Insertion of date " + termin);
		
		// check whether besitzer is known
		String kuerzel = termin.getBesitzer().getKuerzel();
		if (!isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");

		// check whether teilnehmer are known
		Collection<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p.getKuerzel()))
				throw new TerminException("Userid unknown!");
		}
		
		tx.begin();
			manager.persist(termin);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public Vector<Termin> getTermineVom(Datum dat, Person tn)
		throws TerminException
	{
		logger.fine("Method getTermineVom called for " + dat);
				
		String kuerzel = tn.getKuerzel();
		if (!isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");

		tx.begin();
			Query query = manager.createQuery("select t from Termin t where (:tn member of t.teilnehmer)");
			// FIXME es fehlt die Selektion auf teilnehmer!
			query.setParameter("tn", tn);
			List<Termin>  results = (List<Termin>) query.getResultList();
		tx.commit();
	
		return new Vector<Termin>(results);
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
		
		// FIXME Intervallsuche muss noch implementiert werden
		return getTermineVom(vonDat, tn);		
	}

	public void delete(Termin termin) throws TerminException
	{
		logger.fine("Deletion of date " + termin);
		
		// XXX diesen Test könnte man evtl. als Hilfsfunktion extrahieren
		// check whether besitzer is known
		String kuerzel = termin.getBesitzer().getKuerzel();
		if (!isPersonKnown(kuerzel))
			throw new TerminException("Userid unknown!");

		// check whether teilnehmer are known
		Collection<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p.getKuerzel()))
				throw new TerminException("Userid unknown!");
		}
		tx.begin();
			manager.remove(termin);
		tx.commit();
	}
}
