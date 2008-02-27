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
package junit.swtkal.domain;

import junit.framework.*;
import swtkal.domain.Datum;

// TODO Junit-Test für Datum und weitere Domain-Klassen ergaenzen

/*****************************************************************************************************
 * This DatumTest ......
 * 
 * @author calendarium project
 */
public class DatumTest extends TestCase
{
	public DatumTest(String name) { super(name); }

	public static Test suite()
	{
		TestSuite testSuite =
			new TestSuite(DatumTest.class.getName());
		
		testSuite.addTest(new DatumTest("testDatumKonstruktor"));
		testSuite.addTest(new DatumTest("testGetDay"));
		testSuite.addTest(new DatumTest("testGetMonth"));
		testSuite.addTest(new DatumTest("testGetYear"));
		
		return testSuite;
	}
	
	protected Datum datum;
	protected void setUp() throws Exception
	{
		datum = new Datum("12.06.2007");
	}
	
	public void testDatumKonstruktor() throws Exception
	{
		assertTrue(datum!=null);
	}
	
	public void testGetDay() throws Exception
	{
		assertTrue(datum.getDay()==12);
	}

	public void testGetMonth() throws Exception
	{
		assertTrue(datum.getMonth()==6);
	}

	public void testGetYear() throws Exception
	{
		assertTrue(datum.getYear()==2007);
	}
}
