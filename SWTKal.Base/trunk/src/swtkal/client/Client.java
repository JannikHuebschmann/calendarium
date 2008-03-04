/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			swtUser			initial version
 *
 */
package swtkal.client;

import java.io.*;
import java.util.Properties;

import swtkal.domain.Person;
import swtkal.server.Server;

/*****************************************************************************************************
 * Abstract class Client specifies the required client interface that each
 * Client subclass is supposed to implement. Furthermore it realizes some
 * base functionality that can be used by each subclass.
 * 
 * @author swtUser
 */
public abstract class Client
{
	protected Server server;
	protected Person user;
	protected Properties clientProperties;
	
	/**
	 * Constructor
	 */
	protected Client()
	{
		server = swtkal.server.Server.getServer();	// as specified in swtkalServerProperties.xml
		clientProperties = getClientProperties();	// as specified in swtkalClientProperties.xml
	}
	
	/**
	 * @return the server to which the client is connected
	 */
	public Server getServer()
	{
		return server;
	}

	/**
	 * @return the client user as a person object
	 */
	public Person getUser()
	{
		return user;
	}
	
	/**
	 * Returns the client properties read from file swtkalClientProperties.xml.
	 * @return the associated client properties 
	 */
	public Properties getClientProperties()
	{
		if (clientProperties==null)
		{
			clientProperties = new Properties();
			
			// set default properties
			clientProperties.put("RMIServerPort", "2005");
			clientProperties.put("BrowserPath", "");
			clientProperties.put("MtPaneYears", "-1 .. +5");
			clientProperties.put("RMIServerName", "localhost");

			// read swtkalClientProperties.xml
			try
			{
				FileInputStream in = new FileInputStream("swtkalClientProperties.xml");
				clientProperties.loadFromXML(in);
				in.close();
			}
			catch (IOException e)
			{
				System.err.println("File swtkalClientProperties.xml not found. Default generated!");
				try
				{
					// write the current properties to a file (in case it does not yet exist)
					FileOutputStream out = new FileOutputStream("swtkalClientProperties.xml");
					clientProperties.storeToXML(out, "--- SWTKal Client Settings ---");
					out.close();
				}
				catch (IOException ex)
				{}
			}
		}
		return clientProperties;
	}
}
