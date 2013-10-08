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


/*****************************************************************************************************
 * This ServerTest ......
 * 
 * @author calproj
 */
public class ServerTest extends TestCase
{

	public static Test suite()
	{
		TestSuite testSuite =
			new TestSuite(ServerTest.class.getName());
		
		testSuite.addTest(new ServerTest("testGetServer"));
		testSuite.addTest(new ServerTest("testStopAndStartServer"));
		
		return testSuite;
	}

	public ServerTest(String name) { super(name); }

	Server server;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		server = Server.getServer();
		server.startServer();
	}

	protected void tearDown() throws Exception
	{
		server.stopServer();
		super.tearDown();
	}

	public void testGetServer() throws Exception
	{
		assertTrue(server!=null);
		assertTrue(server.getLogger()!=null);
		assertTrue(server.getServerProperties()!=null);
	}

	public void testStopAndStartServer() throws Exception
	{
		assertTrue(server.isServerRunning());
		server.stopServer();
		assertTrue(!server.isServerRunning());
		server.startServer();
		assertTrue(server.isServerRunning());
	}

}
