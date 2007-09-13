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
		
		return testSuite;
	}

	public ServerTest(String name) { super(name); }

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetServer() throws Exception
	{
		assertTrue(Server.getServer()!=null);
	}
	
}
