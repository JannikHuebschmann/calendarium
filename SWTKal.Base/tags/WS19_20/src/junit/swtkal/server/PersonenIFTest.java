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

import junit.framework.*;

import swtkal.server.Server;
import swtkal.domain.*;
import swtkal.exceptions.PersonException;


/*****************************************************************************************************
 * This PersonenIFTest ......
 * 
 * @author calproj
 */
public class PersonenIFTest extends TestCase
{

	public static Test suite()
	{
		TestSuite testSuite =
			new TestSuite(PersonenIFTest.class.getName());
		
		testSuite.addTest(new PersonenIFTest("testInsert"));
		testSuite.addTest(new PersonenIFTest("testDelete"));
		testSuite.addTest(new PersonenIFTest("testUpdate"));
		testSuite.addTest(new PersonenIFTest("testUpdatePasswort"));
		testSuite.addTest(new PersonenIFTest("testUpdateKuerzel"));
		testSuite.addTest(new PersonenIFTest("testGetPersonVector"));
				
		return testSuite;
	}

	public PersonenIFTest(String name) { super(name); }

	Server server;
	Person p;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		server = Server.getServer();
		server.startServer();
		p = new Person("Max", "Mustermann", "MM");
		server.insert(p, "pass");
	}

	protected void tearDown() throws Exception
	{
		server.delete(p);
		server.stopServer();
		super.tearDown();
	}

	public void testInsert() throws Exception
	{
		assertTrue(server.isPersonKnown("MM"));
		Person person = server.authenticatePerson("MM", "pass");
		assertNotNull(person);
		assertEquals(p, person);
		assertNotNull(server.findPerson("MM"));
		try
		{
			server.insert(p, "neupass");
			fail("Should throw PersonException!");
		}
		catch (PersonException e)
		{}
	}

	public void testDelete() throws Exception
	{
		server.delete(p);
		assertTrue(!server.isPersonKnown("MM"));
		try
		{
			server.delete(p);
			fail("Should throw PersonException!");
		}
		catch (PersonException e)
		{}
		server.insert(p, "MM");		// needed for tearDown
	}

	public void testUpdate() throws Exception
	{
		Person person = new Person("Max", "Musterfrau", "MM");
		server.update(person);
		assertTrue(server.isPersonKnown("MM"));
		assertEquals(person.getName(), server.findPerson("MM").getName());
		
		person.setKuerzel("XX");
		try
		{
			server.update(person);
			fail("Should throw PersonException!");
		}
		catch (PersonException e)
		{}
	}

	public void testUpdatePasswort() throws Exception
	{
		server.updatePasswort(p, "pass");			// nothing to do
		assertTrue(server.isPersonKnown("MM"));
		assertEquals(p, server.authenticatePerson("MM", "pass"));

		server.updatePasswort(p, "neupass");
		assertTrue(server.isPersonKnown("MM"));
		assertEquals(p, server.authenticatePerson("MM", "neupass"));
		
		p.setKuerzel("XX");
		try
		{
			server.updatePasswort(p, "neupass");
			fail("Should throw PersonException!");
		}
		catch (PersonException e)
		{}
		p.setKuerzel("MM");			// needed for tearDown
	}

	public void testUpdateKuerzel() throws Exception
	{
		server.updateKuerzel(p, "MM");		// nothing to do
		assertTrue(server.isPersonKnown("MM"));
		
		p.setKuerzel("XX");
		server.updateKuerzel(p, "MM");
		assertTrue(!server.isPersonKnown("MM"));
		assertTrue(server.isPersonKnown("XX"));
		assertEquals(p.getName(), server.authenticatePerson("XX", "pass").getName());
		
		try
		{
			server.updateKuerzel(p, "ZZ");
			fail("Should throw PersonException!");
		}
		catch (PersonException e)
		{}
	}

	public void testGetPersonVector() throws Exception
	{
		int alteAnzahl = server.getPersonVector().size();
		assertTrue(alteAnzahl>0);
		
		Person person = new Person("Frieda", "Fraumuster", "FF");
		server.insert(person, "pass");
		assertEquals(alteAnzahl+1, server.getPersonVector().size());
		server.delete(person);
	}

}
