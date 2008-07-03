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
		testSuite.addTest(new TermineIFTest("testUpdateTermin"));
		testSuite.addTest(new TermineIFTest("testIsPersonAvailable"));
		return testSuite;
	}

	public TermineIFTest(String name) { super(name); }

	Server server;
	Datum  d;
	Person p;
	Termin t;
	
	protected void setUp() throws Exception
	{
		server = Server.getServer();
		server.startServer();
		d = new Datum(new Date()).addDauer(7);
		p = new Person("Max", "Mustermann", "MM");
		server.insert(p, "pass");
		t = new Termin(p, "Testtermin", "Dies ist der Langtext zum Testtermin", d, d.addDauer(1));
		server.insert(t);
	}

	protected void tearDown() throws Exception
	{
		server.delete(t);
		server.delete(p);
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
		assertTrue(server.getTermineVom(d, p).size()==1);
		assertTrue(server.getTermineVom(d, p).contains(t));

		int id = t.getId();
		assertEquals(t, server.getTermin(id));
	}
	
	public void testGetTermineVomForPersons() throws Exception
	{
		Datum datum = new Datum(d.getDateStr());
		datum.add(1);
		
		Person p2 = new Person("Frieda", "Fraumuster", "FF");
		server.insert(p2, "abc");
		
		Vector<Person> teilnehmer = new Vector<Person>();
		teilnehmer.add(p);
		teilnehmer.add(p2);
		
		assertTrue(server.getTermineVom(datum, teilnehmer).size() == 0);
		
		server.delete(p2);
		
		//mehr Fälle durchtesten?
		// bspw. beide Personen haben einen Termin
		// eine Person ist unbekannt
	}
	
	@SuppressWarnings("unchecked")
	public void testGetTermineVonBisForPersons() throws Exception
	{
		Datum von = d;
		Datum bis = new Datum(von.getDateStr(), von.getTimeStr());
		bis.add(20);
		
		Person p2 = new Person("Frieda", "Fraumuster", "FF");
		server.insert(p2, "abc");
		
		Vector teilnehmer = new Vector();
		teilnehmer.add(p);
		teilnehmer.add(p2);
		
		assertTrue(server.getTermineVonBis(von, bis, p).size()==1);
		
		server.delete(p2);
		//mehr Fälle durchtesten?
	}
	
	public void testGetBesitzerTermineVom() throws Exception
	{
		Person teilnehmer = new Person("Frieda", "Fraumuster", "FF");
		server.insert(teilnehmer, "ffff");
		
		assertTrue(server.getBesitzerTermineVom(d, p).size() == 1);
		assertTrue(server.getBesitzerTermineVom(d, teilnehmer).size() == 0);
		
		server.delete(teilnehmer);
		// was soll passieren, wenn der Besitzer unbekannt ist (a la getTermineVom)
	}
	
	public void testGetBesitzerTermineVonBis() throws Exception
	{
		Person teilnehmer = new Person("Frieda", "Fraumuster", "FF");
		server.insert(teilnehmer, "ffff");
		
		assertTrue(server.getBesitzerTermineVonBis(d, d.addDauer(1), p).size() == 1);
		assertTrue(server.getBesitzerTermineVonBis(d, d.addDauer(1), teilnehmer).size() == 0);
		
		server.delete(teilnehmer);
		// zusätzliche Tests mit einer größeren Zeitspanne (a la getTermineVonBis)
	}
	
	public void testUpdateTermin() throws Exception
	{
		t.setKurzText("ge-updated Kurztext");
		server.update(t);
		assertTrue(server.getTermin(t.getId()).getKurzText() == "ge-updated Kurztext");
		
		// kann man noch mehr updaten?
		// gibt es irgendetwas, das man nicht machen darf?
	}
	
	public void testIsPersonAvailable() throws Exception
	{
		Datum vonDat = new Datum("15.01.2020");
		Datum bisDat = new Datum("16.01.2020");
		
		Person teilnehmer = new Person("Frieda", "Fraumuster", "FF");
		server.insert(teilnehmer, "ffff");
		
		assertTrue(server.isPersonAvailable(vonDat, bisDat, teilnehmer));
		
		server.delete(teilnehmer);
		// weitere Testfälle, sodass Person verfügbar und nicht verfügbar ist
		// wann soll eine Exception geworfen werden?
	}
}