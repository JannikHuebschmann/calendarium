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
 *
 */
package junit.swtkal.server;

import java.util.Date;

import junit.framework.*;

import swtkal.domain.*;
import swtkal.exceptions.TerminException;
import swtkal.server.Server;


/*****************************************************************************************************
 * This TermineIFTest ......
 * 
 * @author calendarium project
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
		server.delete(termin);		// nothing to do		
		
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
}
