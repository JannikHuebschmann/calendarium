/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			calproj			transfer out of the calendarium project
 *	01.07.2008			swtUser			new testcases for extended search functionality *
 */
package junit.swtkal.server;

import java.util.Date;
import java.util.Vector;

import junit.framework.*;

import swtkal.domain.*;
import swtkal.exceptions.TerminException;
import swtkal.server.Server;


/*****************************************************************************************************
 * This TermineIFTest ......
 * 
 * @author calproj
 */
public class TermineIFTest extends TestCase
{

	public static Test suite()
	{
		TestSuite testSuite =
			new TestSuite(TermineIFTest.class.getName());

		testSuite.addTest(new TermineIFTest("testInsert"));
		testSuite.addTest(new TermineIFTest("testDelete"));
		testSuite.addTest(new TermineIFTest("testGetTermineVom"));
		testSuite.addTest(new TermineIFTest("testGetTermineVonBis"));		
		testSuite.addTest(new TermineIFTest("testGetTerminByID"));
		testSuite.addTest(new TermineIFTest("testGetTermineVomForPersons"));
		testSuite.addTest(new TermineIFTest("testGetTermineVonBisForPersons"));
		testSuite.addTest(new TermineIFTest("testGetBesitzerTermineVom"));
		testSuite.addTest(new TermineIFTest("testGetBesitzerTermineVonBis"));
//		testSuite.addTest(new TermineIFTest("testUpdateTermin"));
		testSuite.addTest(new TermineIFTest("testIsPersonAvailable"));
		return testSuite;
	}

	public TermineIFTest(String name) { super(name); }

	Server server;
	Datum  d;
	Person p, p2;
	Termin t, t2, t3, t4, t5;
	Vector<Person> teilnehmer = new Vector<Person>();
	
	protected void setUp() throws Exception
	{
		server = Server.getServer();
		server.startServer();
		
		d = new Datum(new Date()).addDauer(7);
		
		p = new Person("Max", "Mustermann", "MM");
		p2 = new Person("Frieda", "Musterfrau", "FM");
		teilnehmer.add(p);
		teilnehmer.add(p2);
		server.insert(p, "pass");
		server.insert(p2, "pass");
		
		t = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d, d.addDauer(1));
		t2 = new Termin(p2, "Testtermin", "Dies ist der Langtext zum Testtermin", d, d.addDauer(25));
		t3 = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d.addDauer(600), d.addDauer(625));
		t4 = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d.addDauer(600), d.addDauer(626));
		t5 = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d.addDauer(700), d.addDauer(701));
		t5.setTeilnehmer(teilnehmer);
		
		server.insert(t);
		server.insert(t2);
		server.insert(t3);
		server.insert(t4);
		server.insert(t5);
	}

	protected void tearDown() throws Exception
	{
		server.delete(t);
		server.delete(t2);
		server.delete(t3);
		server.delete(t4);
		server.delete(t5);
		server.delete(p);
		server.delete(p2);
		server.stopServer();
	}

	public void testInsert() throws Exception
	{
		assertTrue(server.getTermineVom(d, p).size()==1);
		assertTrue(server.getTermineVom(d, p).contains(t));
		
		Termin neuTermin = new Termin(t.getBesitzer(),
                				      t.getKurzText(),
                                      t.getLangText(),
					                  t.getBeginn(),
					                  t.getEnde());
		server.insert(neuTermin);						// additional insert
		assertTrue(server.getTermineVom(d, p).size()==2);
		assertTrue(server.getTermineVom(d, p).contains(t));
		server.delete(neuTermin);
		
		Person person = new Person("Frieda", "Fraumuster", "FF");
		Termin termin = new Termin(person, "Kurztext", "Langtext", d, d.addDauer(1));
		assertTrue(!server.isPersonKnown("FF"));

		try
		{
			server.insert(termin);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}

	public void testDelete() throws Exception
	{
		assertTrue(server.getTermineVom(d, p).size()==1);
		server.delete(t);
		assertTrue(server.getTermineVom(d, p).size()==0);
		server.delete(t);				// nothing to do
		assertTrue(server.getTermineVom(d, p).size()==0);
		t = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d, d.addDauer(1));
		server.insert(t);				// needed for tearDown
		
		Termin termin = new Termin(p, "Neukurz", "Neulang", d, d.addDauer(1));
		server.delete(termin);			// nothing to do		
		
		Person person = new Person("Frieda", "Fraumuster", "FF");
		termin = new Termin(person, "Neukurz", "Neulang", d, d.addDauer(1));
		try
		{
			server.delete(termin);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}

	public void testGetTermineVom() throws Exception
	{
		Datum datum = new Datum(d.getDateStr());
		datum.add(1);					// no appointment for this datum
		assertTrue(server.getTermineVom(datum, p).size()==0);
		
		Person person = new Person("Frieda", "Fraumuster", "FF");
		try
		{
			server.getTermineVom(d, person);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}

	public void testGetTermineVonBis() throws Exception
	{
		Datum von = d;
		Datum bis = new Datum(von.getDateStr(), von.getTimeStr());
		bis.add(20);
		assertTrue(server.getTermineVonBis(von, bis, p).size()==1);
		
		Datum zwischen = new Datum(von.getDateStr(), von.getTimeStr());
		zwischen.add(10);
		Termin zwTermin = new Termin(p, "Neukurz", "Neulang", zwischen, zwischen.addDauer(1));
		server.insert(zwTermin);
		assertTrue(server.getTermineVonBis(von, bis, p).size()==2);
		server.delete(zwTermin);				// needed for tearDown
		assertTrue(server.getTermineVonBis(von, bis, p).size()==1);

		Datum nach1 = new Datum(bis.getDateStr(), bis.getTimeStr());
		nach1 = nach1.addDauer(1);
		Datum nach2 = new Datum(bis.getDateStr(), bis.getTimeStr());
		nach2.add(1);
		Termin nachTermin = new Termin(p, "Neukurz", "Neulang", nach1, nach2);
		server.insert(nachTermin);
		assertTrue(server.getTermineVonBis(von, bis, p).size()==1);
		server.delete(nachTermin);				// needed for tearDown

		try
		{	// no correct period of time
			server.getTermineVonBis(bis, von, p);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}

		Person person = new Person("Frieda", "Fraumuster", "FF");
		try
		{
			server.getTermineVonBis(von, bis, person);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
	
	public void testGetTerminByID() throws Exception
	{
		//test with empty result
		assertNull(server.getTermin(0));
		
		//test with exactly one result
		assertEquals(server.getTermin(t.getId()), t);
	}
	
	public void testGetTermineVomForPersons() throws Exception
	{
		//persons Vector Mustermann and Musterfrau
		Vector<Person> teilnehmer = new Vector<Person>();
		teilnehmer.add(p);
		teilnehmer.add(p2);
		
		//test with empty results
		Datum dtemp = d.addDauer(50);
		assertTrue(server.getTermineVom(dtemp, teilnehmer).size() == 0);
		
		//test with exactly one appointment result
		dtemp = d.addDauer(25);
		assertTrue(server.getTermineVom(dtemp, teilnehmer).size() == 1);
		assertTrue(server.getTermineVom(d.addDauer(700), teilnehmer).size() == 1);
		
		//test with exactly two appointment results
		assertTrue(server.getTermineVom(d, teilnehmer).size() == 2);
		
		//test with TerminException, cause of one unknown person "ptemp"
		try
		{
			Person ptemp = new Person("ich", "bin", "unbekannt");
			teilnehmer.add(ptemp);

			server.getTermineVom(d, teilnehmer);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
	
	@SuppressWarnings("unchecked")
	public void testGetTermineVonBisForPersons() throws Exception
	{		
		//persons Vector Mustermann and Musterfrau
		Vector<Person> teilnehmer = new Vector<Person>();
		teilnehmer.add(p);
		teilnehmer.add(p2);
		
		//test with empty results
		Datum von = d.addDauer(49);
		Datum bis = d.addDauer(70);
		assertTrue(server.getTermineVonBis(von, bis, teilnehmer).size() == 0);
		
		//test with exactly one appointment result
		von = d.addDauer(25);
		assertTrue(server.getTermineVonBis(von, bis, teilnehmer).size() == 1);
		assertTrue(server.getTermineVonBis(d.addDauer(700), d.addDauer(701), teilnehmer).size() == 1);
		
		//test with exactly two appointment results
		von = d;
		assertTrue(server.getTermineVonBis(von, bis, teilnehmer).size() == 2);
		
		//test with TerminException, cause of one unknown person "ptemp"
		try
		{
			Person ptemp = new Person("ich", "bin", "unbekannt");
			teilnehmer.add(ptemp);

			server.getTermineVonBis(von, bis, teilnehmer);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
		
		//test with TerminException, cause of no correct period of time
		try
		{
			server.getTermineVonBis(bis, von, teilnehmer);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
	
	public void testGetBesitzerTermineVom() throws Exception
	{
		//test with empty results
		assertTrue(server.getBesitzerTermineVom(d.addDauer(78), p).size() == 0);
		
		//test with exactly one appointment result
		assertTrue(server.getBesitzerTermineVom(d, p).size() == 1);
		
		//test with exactly two appointment result
		assertTrue(server.getBesitzerTermineVom(d.addDauer(601), p).size() == 2);
		
		//test with TerminException, cause of one unknown person "ptemp"
		try
		{
			Person ptemp = new Person("ich", "bin", "unbekannt");

			server.getBesitzerTermineVom(d, ptemp);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
	
	public void testGetBesitzerTermineVonBis() throws Exception
	{
		//test with empty results
		Datum von = d.addDauer(75);
		Datum bis = d.addDauer(79);
		assertTrue(server.getBesitzerTermineVonBis(von, bis, p).size() == 0);
		
		//test with exactly one appointment result
		von = d.addDauer(24);
		assertTrue(server.getBesitzerTermineVonBis(von, bis, p2).size() == 1);
		
		//test with exactly two appointment results
		von = d.addDauer(599);
		bis = d.addDauer(699);
		assertTrue(server.getBesitzerTermineVonBis(von, bis, p).size() == 2);
		
		//test with TerminException, cause of one unknown person "ptemp"
		try
		{
			Person ptemp = new Person("ich", "bin", "unbekannt");

			server.getBesitzerTermineVonBis(von, bis, ptemp);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
		
		//test with TerminException, cause of no correct period of time
		try
		{
			server.getBesitzerTermineVonBis(bis, von, p);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
	
	public void testUpdateTermin() throws Exception
	{
//		t.setKurzText("ge-updated Kurztext");
//		server.update(t);
//		assertTrue(server.getTermin(t.getId()).getKurzText() == "ge-updated Kurztext");
		
		// kann man noch mehr updaten?
		// gibt es irgendetwas, das man nicht machen darf?
	}
	
	public void testIsPersonAvailable() throws Exception
	{
		//test with available person
		assertTrue(server.isPersonAvailable(d.addDauer(500), d.addDauer(510), p));
		
		//test with unavailable person
		assertFalse(server.isPersonAvailable(d, d.addDauer(10), p2));
		
		//test with TerminException, cause of one unknown person "ptemp"
		try
		{
			Person ptemp = new Person("ich", "bin", "unbekannt");

			server.isPersonAvailable(d, d.addDauer(10), ptemp);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
		
		//test with TerminException, cause of no correct period of time
		try
		{
			server.isPersonAvailable(d.addDauer(10), d, p);
			fail("Should throw TerminException!");
		}
		catch (TerminException e)
		{}
	}
}