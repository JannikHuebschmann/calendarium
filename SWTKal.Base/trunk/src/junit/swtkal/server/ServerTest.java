package junit.swtkal.server;

import junit.framework.*;

import swtkal.server.Server;

// TODO JUnit-Tests zum Server realisieren
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
