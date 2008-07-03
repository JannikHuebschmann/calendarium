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


/*****************************************************************************************************
 * This AllTests ......
 * 
 * @author calproj
 */
public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("junit.swtkal.domain");
		suite.addTest(junit.swtkal.domain.DatumTest.suite());
		return suite;
	}
}
