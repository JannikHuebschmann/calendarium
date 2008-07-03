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


/*****************************************************************************************************
 * This AllTests ......
 * 
 * @author calproj
 */
public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("junit.swtkal.server");
		suite.addTest(junit.swtkal.server.ServerTest.suite());
		suite.addTest(junit.swtkal.server.PersonenIFTest.suite());
		suite.addTest(junit.swtkal.server.TermineIFTest.suite());
		return suite;
	}
}
