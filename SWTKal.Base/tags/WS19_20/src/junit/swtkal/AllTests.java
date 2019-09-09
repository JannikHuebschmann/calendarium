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
package junit.swtkal;

import junit.framework.Test;
import junit.framework.TestSuite;


/*****************************************************************************************************
 * This AllTests ......
 * 
 * @author calproj
 */
public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("junit.swtkal");
		suite.addTest(junit.swtkal.domain.AllTests.suite());
		suite.addTest(junit.swtkal.server.AllTests.suite());
		return suite;
	}
}
