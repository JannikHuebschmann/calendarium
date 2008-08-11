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
 *	01.06.2008			ejbUser			new update method and extended search functionality
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
 * Class JPAServer realizes is a multi-user JPA-based server.
 * 
 * This server accesses a relational database according to the specifications in
 * persistence.xml and orm.xml.
 * 
 * @author ejbUser
 */
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
						
			//two simple test dates for today
			insert(new Termin(p, "1. Testtermin", "Dies ist der Langtext zum 1. Testtermin",
					new Datum(new Date()), new Datum(new Date()).addDauer(1)));
			insert(new Termin(p, "2. Testtermin", "Dies ist der Langtext zum 2. Testtermin",
						new Datum(new Date()).addDauer(1.5), new Datum(new Date()).addDauer(2.5)));
		}
		catch (PersonException e) {}	// ADM is already known - nothing to do
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void insert(Person p, String passwort) throws PersonException
	{
		logger.fine("Insertion of person " + p + " with a password");
		
		if (isPersonKnown(p))
				throw new PersonException("Userid is already used!");
		
		tx.begin();
			manager.persist(new Passwort(p.getKuerzel(),passwort));
			manager.persist(p);
		tx.commit();
	}

	public void delete(Person p) throws PersonException
	{
		logger.fine("Deletion of person " + p);
		
		if (!isPersonKnown(p))
				throw new PersonException("Userid unknown!");
		
		tx.begin();
			// delete Passwort and Person objects
			manager.remove(manager.find(Passwort.class, p.getKuerzel()));
			manager.remove(manager.find(Person.class, p.getKuerzel()));
			// deletes in associations besitzer and teilnehmer
			// are automatically propagated by cascading rules within the DB schema
		tx.commit();
	}
	
	public void update(Person p) throws PersonException
	{
		logger.fine("Update of person " + p);
		
		if (!isPersonKnown(p))
				throw new PersonException("Userid unknown!");
		
		tx.begin();
			p = manager.merge(p);
		tx.commit();
	}

	public void updatePasswort(Person p, String passwort) throws PersonException
	{
		logger.fine("Update of password of person " + p);
		
		if (!isPersonKnown(p))
				throw new PersonException("Userid unknown!");
		
		tx.begin();
			manager.merge(new Passwort(p.getKuerzel(), passwort));
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
			manager.clear();							// clear persistence context since
			                                            // changing entity ids is dangerous
			Passwort pass = manager.find(Passwort.class, oldKuerzel);
			manager.remove(pass);
			manager.persist(new Passwort(neuKuerzel, pass.getPasswort()));
			
			Query update = manager.createNativeQuery("update person set kuerzel=:neuKuerzel where kuerzel=:oldKuerzel");
			update.setParameter("neuKuerzel", neuKuerzel);
			update.setParameter("oldKuerzel", oldKuerzel);
			update.executeUpdate();
			// updates in associations besitzer and teilnehmer
			// are automatically propagated by cascading rules within the DB schema
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

	public boolean isPersonKnown(String kuerzel)
	{
		Passwort pass = manager.find(Passwort.class, kuerzel);
		return pass!=null;
	}

	protected boolean isPersonKnown(Person p)
	{
		return isPersonKnown(p.getKuerzel());
	}
	
	public Person findPerson(String kuerzel) throws PersonException
	{
		logger.fine("Find person with userid " + kuerzel);

		Person p = manager.find(Person.class, kuerzel);
		if (p==null)
			throw new PersonException("Userid unknown!");
		else
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
		if (!isPersonKnown(termin.getBesitzer()))
			throw new TerminException("Userid unknown!");
		// check whether teilnehmer are known
		Collection<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p))
				throw new TerminException("Userid unknown!");
		}
		
		tx.begin();
			manager.persist(termin);
		tx.commit();
	}
	
	public void delete(Termin termin) throws TerminException
	{
		logger.fine("Deletion of date " + termin);
		
		// check whether "Besitzer" is known
		if (!isPersonKnown(termin.getBesitzer()))
			throw new TerminException("Userid unknown!");

		// check whether "Teilnehmer" are known
		Collection<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p))
				throw new TerminException("Userid unknown!");
		}
		
		tx.begin();
			try
			{
				manager.remove(manager.find(Termin.class, termin.getId()));
			}
			catch (IllegalArgumentException exp)
			{
				// Termin does not exist - nothing to do!
			}
		tx.commit();
	}
	
	public void deleteTermin(int id) throws TerminException
	{
		logger.fine("Delete of date with ID: " + id);
		
		tx.begin();
			try
			{
				manager.remove(manager.find(Termin.class, id));
			}
			catch (IllegalArgumentException exp)
			{
				// Termin does not exist - nothing to do!
			}
		tx.commit();
	}

	public void update(Termin termin) throws TerminException 
	{
		logger.fine("Update of appointment " + termin);

		// check whether "Besitzer" is known
		if (!isPersonKnown(termin.getBesitzer()))
			throw new TerminException("Userid unknown!");

		// check whether "Teilnehmer" are known
		Collection<Person> teilnehmer = termin.getTeilnehmer();
		for (Person p : teilnehmer)
		{
			if (!isPersonKnown(p))
				throw new TerminException("Userid unknown!");
		}
		
		// check if date interval is correct 
		if (termin.getBeginn().isGreater(termin.getEnde())==1)
			throw new TerminException("Incorrect date interval!");
		
		tx.begin();
			termin = manager.merge(termin);
		tx.commit();
	}

	public Termin getTermin(int id) throws TerminException
	{
		logger.fine("Method getTermin called for ID " + id);

		tx.begin();
			Termin result = manager.find(Termin.class, id);
		tx.commit();
		
		return result;
	}

	public Vector<Termin> getTermineVom(Datum dat, Person tn)
		throws TerminException
	{
		// truncate vonDat and bisDat
		Datum tagesAnfang = new Datum(dat.getDateStr());
		Datum tagesEnde   = new Datum(dat.getDateStr() + " 23:59");

		return getTermineVonBis(tagesAnfang, tagesEnde, tn);
	}

	@SuppressWarnings("unchecked")
	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Person tn)
		throws TerminException
	{
		logger.fine("Method getTermineVonBis called from " + vonDat + " to " + bisDat);

		if (!isPersonKnown(tn))
			throw new TerminException("Userid unknown!");
		if (vonDat.isGreater(bisDat)==1)
			throw new TerminException("Incorrect date interval!");

		tx.begin();
			Query query = manager.createQuery("select t from Termin t " +
					                          "where t.ende>=:vonDat and :bisDat>=t.beginn " +
					                                 "and (:tn MEMBER OF t.teilnehmer)");
			query.setParameter("vonDat", (Calendar) vonDat);
			query.setParameter("bisDat", (Calendar) bisDat);
			query.setParameter("tn", tn);
			List<Termin>  results = (List<Termin>) query.getResultList();
		tx.commit();

		return new Vector<Termin>(results);
	}
	
	public Vector<Termin> getTermineVom(Datum dat, Vector<Person> teilnehmer)
		throws TerminException
	{
		// truncate vonDat and bisDat
		Datum tagesAnfang = new Datum(dat.getDateStr());
		Datum tagesEnde   = new Datum(dat.getDateStr() + " 23:59");

		return getTermineVonBis(tagesAnfang, tagesEnde, teilnehmer);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Termin> getTermineVonBis(Datum vonDat, Datum bisDat, Vector<Person> teilnehmer)
		throws TerminException
	{
		logger.fine("Method getTermineVonBis called from " + vonDat + " to " + bisDat);
		
		for(Person p : teilnehmer)
		{
			if (!isPersonKnown(p))
			{
				throw new TerminException("Userid unknown!");
			}
		}
		if (vonDat.isGreater(bisDat)==1)
			throw new TerminException("Incorrect date interval!");
		
		tx.begin();
			Set<Termin> results = new HashSet<Termin>();
			for(Person p : teilnehmer) {
				Query query = manager.createQuery("SELECT t FROM Termin t " +
												  "WHERE t.ende>=:vonDat and :bisDat>=t.beginn " +
														 "AND (:tn MEMBER OF t.teilnehmer)");
	
				query.setParameter("vonDat", (Calendar) vonDat);
				query.setParameter("bisDat", (Calendar) bisDat);
				query.setParameter("tn", p);
				
				results.addAll((List<Termin>) query.getResultList());
			}
		tx.commit();
		
		return new Vector<Termin>(results);
	}
	
	public Vector<Termin> getBesitzerTermineVom(Datum dat, Person besitzer)
		throws TerminException 
	{
		Datum tagesAnfang = new Datum(dat.getDateStr());
		Datum tagesEnde   = new Datum(dat.getDateStr() + " 23:59");

		return getBesitzerTermineVonBis(tagesAnfang, tagesEnde, besitzer);
	}

	@SuppressWarnings("unchecked")
	public Vector<Termin> getBesitzerTermineVonBis(Datum vonDat, Datum bisDat, Person besitzer)
		throws TerminException 
	{
		logger.fine("Method getBesitzerTermineVonBis called from " + vonDat + " to " + bisDat);
		
		if (!isPersonKnown(besitzer))
			throw new TerminException("Userid unknown!");
		if (vonDat.isGreater(bisDat)==1)
			throw new TerminException("Incorrect date interval!");
		
		tx.begin();
			Query query = manager.createQuery("SELECT t FROM Termin t " +
											  "WHERE t.ende>=:vonDat and :bisDat>=t.beginn " +
													 "AND :bz = t.besitzer");
			
			query.setParameter("vonDat", (Calendar) vonDat);
			query.setParameter("bisDat", (Calendar) bisDat);
			query.setParameter("bz", besitzer);
			
			List<Termin>  results = (List<Termin>) query.getResultList();
		tx.commit();		
		
		return new Vector<Termin>(results);
	}

	public boolean isPersonAvailable(Datum vonDat, Datum bisDat, Person teilnehmer)
		throws TerminException 
	{
		logger.fine("Method isPersonAvailable called from " + vonDat + " to " + bisDat);
		
		if (!isPersonKnown(teilnehmer))
			throw new TerminException("Userid unknown!");
		if (vonDat.isGreater(bisDat)==1)
			throw new TerminException("Incorrect date interval!");
		
		tx.begin();
			Query query = manager.createQuery("SELECT t FROM Termin t " +
											  "WHERE t.ende>=:vonDat AND :bisDat>=t.beginn " +
													 "AND (:tn MEMBER OF t.teilnehmer)");

			query.setParameter("vonDat", vonDat);
			query.setParameter("bisDat", bisDat);
			query.setParameter("tn", teilnehmer);
		tx.commit();
		
		return query.getResultList().isEmpty();
	}
}
