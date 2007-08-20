/**
 * 
 */
package testdata;

import javax.swing.JFrame;

import data.Data;

import junit.framework.*;

/**
 * @author Jan-Bernd
 *
 */
public class TestData extends TestCase {

	/**
	 * @param name
	 */
	private Data daten;
	public TestData(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link data.Data#Data(javax.swing.JFrame, java.lang.String)}.
	 */
	public final void testData() 
	{
		JFrame f = new JFrame();
	
		//Verbindung zum Server herstellen
		String hostname = "rmi://"
					+ "127.0.0.1" + ':'
					+ "2005" + '/';
		daten = new Data(f, hostname);
		
		Object result = Data.msgServer.connect(
				"JBE", "jbe");
		
		assertTrue("es klappt",result!=null);
		
	//	Data.typen.
		//OK
		
		//Anmelden am Server mit Benutzernamen.
		
	
	}

	/**
	 * Test method for {@link data.Data#setUser(basisklassen.Person)}.
	 */
	public final void testSetUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.Data#showDialog()}.
	 */
	public final void testShowDialog() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.Data#areYouSure(java.lang.String, java.lang.String)}.
	 */
	public final void testAreYouSure() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	public final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	public final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	public final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	public final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	public final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	public final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait()}.
	 */
	public final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	public final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}

}
