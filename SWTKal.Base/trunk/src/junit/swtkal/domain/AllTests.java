package junit.swtkal.domain;

import junit.framework.*;

public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("junit.swtkal.domain");
		suite.addTest(junit.swtkal.domain.DatumTest.suite());
		return suite;
	}
}
