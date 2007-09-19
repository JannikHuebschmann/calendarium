package junit.swtkal.server;

import junit.framework.*;

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
