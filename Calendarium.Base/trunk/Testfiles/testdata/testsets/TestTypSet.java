/**
 * 
 */
package testdata.testsets;

import data.Data;
import data.sets.TypSet;
import junit.framework.*;

/**
 * @author Jan-Bernd
 *
 */
public class TestTypSet extends TestCase {

	/**
	 * @param name
	 */
	Data daten;
	String zeichenkette;
	private TypSet test_typeset = new TypSet(daten, zeichenkette);
	
	public TestTypSet(String name) {
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
	 * Test method for {@link data.sets.TypSet#TypSet(data.Data, java.lang.String)}.
	 */
	public final void testTypSet() 
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#getDefaultTypen()}.
	 */
	public final void testGetDefaultTypen() 
	{
		String element = test_typeset.getDefaultTypen().toString();
		fail(element); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#getUserTypen()}.
	 */
	public final void testGetUserTypen() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#getDefaultTypByID(long)}.
	 */
	public final void testGetDefaultTypByID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#getUserTypByID(long)}.
	 */
	public final void testGetUserTypByID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#getUserTyp(basisklassen.EintragsTyp)}.
	 */
	public final void testGetUserTyp() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#setUserTyp(basisklassen.EintragsTyp)}.
	 */
	public final void testSetUserTyp() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#create(basisklassen.EintragsTyp)}.
	 */
	public final void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#delete(basisklassen.EintragsTyp)}.
	 */
	public final void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link data.sets.TypSet#update(basisklassen.EintragsTyp)}.
	 */
	public final void testUpdate() {
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
