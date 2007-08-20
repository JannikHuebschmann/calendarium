/**
 * 
 */
package server.remote;

import java.rmi.Naming;
import java.util.Date;

import server.Server;
import junit.framework.TestCase;
import basisklassen.*;

/**
 * @author Jan-Bernd
 *
 */
public class TestTypSetRemote extends TestCase {

	/**
	 * @param name
	 */
	
//	 Server
	private static int wert = 0;
    private Server testserver;
    TypSetRemote typsetremote;
		
	public TestTypSetRemote(String name) 
	{
		super(name);
		System.out.println("TestTypSetRemote");
		if(wert == 0)
		{
			System.out.println("Start Server"+wert);
			testserver = new Server();		
			
			try
	        {  
	        	System.out.println("Init ");
//	        	typsetremote = new TypSetRemote(testserver, "think_it_baby");
	        	System.out.println("OK");
	        }
	        catch(Exception e)
	        {
	        	fail("catch an Exception: 'TypeSetRemote typesetremote'");
	        }
		}
		wert = wert+1;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		System.out.println("setUP");
       	super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		System.out.println("teatDown");
		super.tearDown();
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#TypSetRemote(server.Server, java.lang.String)}.
	 */
	public final void testTypSetRemote() 
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#deletePerson(basisklassen.Person)}.
	 */
	public final void testDeletePerson() 
	{
		// public Person(kuerzel, nachname, vorname) /*alls Strings*/
		// Anlegen einer Person
		Person testperson = new Person("HW","Hans","Wurst");
		// Anlegen eines Typs
		EintragsTyp testtyp = new EintragsTyp(555);
			
        try
        {  
        	typsetremote.create("HW",testtyp);
        	System.out.println("testDeletePerson OK");
        }
        catch(Exception e)
        {
        	System.out.println("catch an Exception: 'testDeletePerson'");
        }

		typsetremote.deletePerson(testperson);
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#getDefaultTypen()}.
	 */
	public final void testGetDefaultTypen() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#getUserTypen(basisklassen.Person)}.
	 */
	public final void testGetUserTypen() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#getDefaultTypByID(long)}.
	 */
	public final void testGetDefaultTypByID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#getUserTypByID(basisklassen.Person, long)}.
	 */
	public final void testGetUserTypByID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#getUserTyp(basisklassen.Person, basisklassen.EintragsTyp)}.
	 */
	public final void testGetUserTyp() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#setUserTyp(basisklassen.Person, basisklassen.EintragsTyp)}.
	 */
	public final void testSetUserTyp() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#create(java.lang.String, basisklassen.EintragsTyp)}.
	 */
	public final void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#delete(java.lang.String, basisklassen.EintragsTyp)}.
	 */
	public final void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link server.remote.TypSetRemote#update(java.lang.String, basisklassen.EintragsTyp)}.
	 */
	public final void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#clone()}.
	 */
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#UnicastRemoteObject()}.
	 */
	public final void testUnicastRemoteObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#UnicastRemoteObject(int)}.
	 */
	public final void testUnicastRemoteObjectInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#UnicastRemoteObject(int, java.rmi.server.RMIClientSocketFactory, java.rmi.server.RMIServerSocketFactory)}.
	 */
	public final void testUnicastRemoteObjectIntRMIClientSocketFactoryRMIServerSocketFactory() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#exportObject(java.rmi.Remote)}.
	 */
	public final void testExportObjectRemote() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#exportObject(java.rmi.Remote, int)}.
	 */
	public final void testExportObjectRemoteInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#exportObject(java.rmi.Remote, int, java.rmi.server.RMIClientSocketFactory, java.rmi.server.RMIServerSocketFactory)}.
	 */
	public final void testExportObjectRemoteIntRMIClientSocketFactoryRMIServerSocketFactory() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.UnicastRemoteObject#unexportObject(java.rmi.Remote, boolean)}.
	 */
	public final void testUnexportObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteServer#RemoteServer()}.
	 */
	public final void testRemoteServer() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteServer#RemoteServer(java.rmi.server.RemoteRef)}.
	 */
	public final void testRemoteServerRemoteRef() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteServer#getClientHost()}.
	 */
	public final void testGetClientHost() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteServer#setLog(java.io.OutputStream)}.
	 */
	public final void testSetLog() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteServer#getLog()}.
	 */
	public final void testGetLog() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#hashCode()}.
	 */
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#equals(java.lang.Object)}.
	 */
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#toString()}.
	 */
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#RemoteObject()}.
	 */
	public final void testRemoteObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#RemoteObject(java.rmi.server.RemoteRef)}.
	 */
	public final void testRemoteObjectRemoteRef() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#getRef()}.
	 */
	public final void testGetRef() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.rmi.server.RemoteObject#toStub(java.rmi.Remote)}.
	 */
	public final void testToStub() {
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
