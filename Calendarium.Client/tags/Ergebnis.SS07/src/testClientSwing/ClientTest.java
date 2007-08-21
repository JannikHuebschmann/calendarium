package testClientSwing;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;

import client.*;

//import client.eintrag.EditTermin;
import client.Client.*;
import client.admin.Noticias;
import client.eintrag.EditTerminControl;

import basisklassen.*;
import data.Data;
import event.CalendariumListenerImpl;
import junit.framework.TestCase;

/**
 * NOTE1:
 * 		These testcases are supposed to test the functionality of the 'Calendarium' after
 * 		porting to 'mySQL'. They run through on the file-server system. Because the
 * 		functionality of file-server and mySQL-server is the same, the tests have to end
 * 		without failures. This should ensure the functionality of the mySQL-server.
 * 
 * NOTE2:
 * 		For all testcases it has to be considered that the inserted testcases can not be
 * 		detected using the 'Client.Swing'. Vice versa appointments inserted into database
 * 		by 'Client.Swing' cannot be rediscovered by these testcases. We assume that it
 * 		has something to do with the 'Data' object that probably creates an own database-
 * 		access for each 'Data' object that is created.
 * 
 * NOTE3:
 * 		Before running the testcases ensure that the following parameters are set. To set
 * 		these parameters manually feed them into the server using the 'Client.Monitor'.
 * 		Parameters:		Kuerzel("MM");
 * 						Passwort("MM");
 *						Nachname("Mustermann");
 *						Vorname("Max");
 *		If you try, you will see that this is not really necessary. We assume that
 *		it is caused either by the same fault that prevents from reading a written
 *		appointment by 'Swing.Client' or by (another) bug of the 'Calendarium.' Here
 *		we set these parameters because we are keen to program a logical order of
 *		instructions.
 */
public class ClientTest extends TestCase {

	protected Data daten;
	protected Object result;
	
	/**
	 * NOTE:
	 * 		This function is called before every single testcase and is used to
	 * 		initialise required variables.
	 */
	protected void setUp() throws Exception 
	{			
		super.setUp();
	}

	/**
	 * NOTE:
	 * 		This function is called after every single testcase and is used to delete
	 * 		allocated memory or to release/free objects. Here it is used to disconnect
	 * 		from server. Generally, a disconnection is important to prevent from memory-
	 * 		leaks.
	 */
	protected void tearDown() throws Exception 
	{
		//Disconnecting if user is existing
		if(Data.user != null)
			Data.msgServer.disconnect();
		super.tearDown();
	}
	
	/**
	 * NOTE:
	 * 		The following testcase will be executed in all other testcases, too, but
	 * 		is executed here once more as single testcase. The reason for this lies in the
	 * 		fact that a connection has to be existing for all further operations on the
	 * 		database server.	
	 */
	public final void testEstablishConnection()
	{
		// creating a 'JFrame' object
		JFrame Frame = new JFrame();
		
		// getting local host informations
		InetAddress thisMachine = null;
		try
		{
			thisMachine = InetAddress.getLocalHost();
		}
		catch (Exception e)
		{
			fail("Could not get local host informations.");
		}
		
		// setting up DefaultsProperties
		Properties defaultProps = new Properties();
		defaultProps.put("RMIServerPort", "2005");
		defaultProps.put("BrowserPath", "");
		defaultProps.put("MtPaneYears", "-1 .. +5");
		defaultProps.put("RMIServerName", thisMachine.getHostName());
		
		// defining defaultProps as userProperties
		Properties userProperties;
		userProperties = defaultProps;
		
		// setting up hostname for connection-establishment
		String hostname = "rmi://"
			+ (String) userProperties.get("RMIServerName") + ':'
			+ (String) userProperties.get("RMIServerPort") + '/';

		// creating a 'Data' object
		daten = new Data(Frame, hostname);
		
		// creation of a 'Person' object and filling it with parameters
		Person p = new Person();
		p.setKuerzel("MM");
		p.setNachname("Mustermann");
		p.setPasswort("MM");
		p.setVorname("Max");
		daten.setUser(p);
		
		// connecting to server
		result = Data.msgServer.connect("MM", "MM");
		if (result != null) 
		{
			if (result instanceof Person) 
			{
				// do nothing if reult is ok
				//System.out.println("result is OK");
			} 
			else 
			{
				// Error
				switch (((Integer) result).intValue()) 
				{
				case -1:
					fail("wrong pass");
					break;

				case -2:
					fail("You've already started the program!");
					break;
				}
			}
		} 
		else
		{
			fail("No connection to server!");
		}
	}
	
	/**
	 * NOTE:
	 * 		This function tests whether an appointment can be stored or not.
	 */
	public final void testTerminSet()
	{
		// creating a 'JFrame' object
		JFrame Frame = new JFrame();
		
		// getting local host informations
		InetAddress thisMachine = null;
		try
		{
			thisMachine = InetAddress.getLocalHost();
		}
		catch (Exception e)
		{
			fail("Could not get local host informations.");
		}
		
		// setting up DefaultsProperties
		Properties defaultProps = new Properties();
		defaultProps.put("RMIServerPort", "2005");
		defaultProps.put("BrowserPath", "");
		defaultProps.put("MtPaneYears", "-1 .. +5");
		defaultProps.put("RMIServerName", thisMachine.getHostName());
		
		// defining defaultProps as userProperties
		Properties userProperties;
		userProperties = defaultProps;
		
		// setting up hostname for connection-establishment
		String hostname = "rmi://"
			+ (String) userProperties.get("RMIServerName") + ':'
			+ (String) userProperties.get("RMIServerPort") + '/';

		// creating a 'Data' object
		daten = new Data(Frame, hostname);
		
		// creation of a 'Person' object and filling it with parameters
		Person p = new Person();
		p.setKuerzel("MM");
		p.setNachname("Mustermann");
		p.setPasswort("MM");
		p.setVorname("Max");
		daten.setUser(p);
		
		// connecting to server
		result = Data.msgServer.connect("MM", "MM");
		if (result != null) 
		{
			if (result instanceof Person) 
			{
				// do nothing if reult is ok
				//System.out.println("result is OK");
			} 
			else 
			{
				// Error
				switch (((Integer) result).intValue()) 
				{
				case -1:
					fail("wrong pass");
					break;

				case -2:
					fail("You've already started the program!");
					break;
				}
			}
		} 
		else
		{
			fail("No connection to server!");
		}
		
		// creating a 'Termin' object and a 'EintragsTyp' object
		Termin termin = new Termin(Data.user);
		EintragsTyp typ = new EintragsTyp(1);	//diese Zahl ist frei gewählt worden

		// setting up the same 'Termin' as in "testTerminSet"
		termin.getBeginn().setDatum("10.06.2007", "14:00");
		termin.getEnde().setDatum("10.06.2007", "15:00");
		termin.setOrt("hier");
		termin.setKurzText("test2");
		termin.setLangText("");
		termin.setOwner(p);
		termin.setTyp(typ);
		
		// checking if the 'Termin' is already in the database
		Vector konflikte = Data.termine.getKonflikte(termin);
		if (konflikte.size() == 0) 
		{
			// inserting 'Termin' object into the database
			Data.termine.create(termin);
			
			// checking if 'Termin' object really is in the database
			konflikte = Data.termine.getKonflikte(termin);
			if(konflikte.size() == 0)
				fail("Appointment was not created!");
		}
		else
		{
			fail("Apppointment already in database!");
		}
	}
	
	/**
	 * NOTE:
	 * 		The testcase deletes a previous stored appointment.
	 * 		This function uses the 'deleteUntilDate(...)' function instead of using the
	 * 		'delete(...)' function. This is caused by the fact that the 'delete' function
	 * 		contains an invocation of a client dialogue which expects an user-input. This
	 * 		cannot be used in an automatic testcase so the 'deleteUntilDate' function is
	 * 		is used here.
	 */
	public final void testTerminDelete()
	{		
		// creating a 'JFrame' object
		JFrame Frame = new JFrame();
		
		// getting local host informations
		InetAddress thisMachine = null;
		try
		{
			thisMachine = InetAddress.getLocalHost();
		}
		catch (Exception e)
		{
			fail("Could not get local host informations.");
		}
		
		// setting up DefaultsProperties
		Properties defaultProps = new Properties();
		defaultProps.put("RMIServerPort", "2005");
		defaultProps.put("BrowserPath", "");
		defaultProps.put("MtPaneYears", "-1 .. +5");
		defaultProps.put("RMIServerName", thisMachine.getHostName());
		
		// defining defaultProps as userProperties
		Properties userProperties;
		userProperties = defaultProps;
		
		// setting up hostname for connection-establishment
		String hostname = "rmi://"
			+ (String) userProperties.get("RMIServerName") + ':'
			+ (String) userProperties.get("RMIServerPort") + '/';

		// creating a 'Data' object
		daten = new Data(Frame, hostname);
		
		// creation of a 'Person' object and filling it with parameters
		Person p = new Person();
		p.setKuerzel("MM");
		p.setNachname("Mustermann");
		p.setPasswort("MM");
		p.setVorname("Max");
		daten.setUser(p);
		
		// connecting to server
		result = Data.msgServer.connect("MM", "MM");
		if (result != null) 
		{
			if (result instanceof Person) 
			{
				// do nothing if reult is ok
				//System.out.println("Result is OK");
			} 
			else 
			{
				// Error
				switch (((Integer) result).intValue()) 
				{
				case -1:
					fail("wrong pass");
					break;

				case -2:
					fail("You've already started the program!");
					break;
				}
			}
		} 
		else
		{
			fail("No connection to server!");
		}
		
		// creating a 'Termin' object and a 'EintragsTyp' object
		Termin termin = new Termin(Data.user);
		EintragsTyp typ = new EintragsTyp(1);	//diese Zahl ist frei gewählt worden

		// setting up the same 'Termin' as in "testTerminSet"
		termin.getBeginn().setDatum("10.06.2007", "14:00");
		termin.getEnde().setDatum("10.06.2007", "15:00");
		termin.setOrt("hier");
		termin.setKurzText("test2");
		termin.setLangText("");
		termin.setOwner(p);
		termin.setTyp(typ);
		
		// checking if the 'Termin' is already in the database
		Vector konflikte = Data.termine.getKonflikte(termin);
		if (konflikte.size() == 0) 
		{
			fail("No appointments to delete!");
		}
		else
		{
			// removing 'Termin' object
			Data.termine.deleteUntilDate(termin.getEnde());
			
			// checking if the 'Termin' object was deleted
			konflikte = Data.termine.getKonflikte(termin);
			if(konflikte.size() != 0)
				{
				fail("Appointment has not been deleted!");
				}
		}
	}
}