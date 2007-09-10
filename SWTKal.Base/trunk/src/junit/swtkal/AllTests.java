package junit.swtkal;

import junit.framework.Test;
import junit.framework.TestSuite;

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
