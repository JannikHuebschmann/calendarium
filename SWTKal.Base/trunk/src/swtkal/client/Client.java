package swtkal.client;

import java.io.*;
import java.util.Properties;

import swtkal.domain.Person;
import swtkal.server.Server;

/**
 * Abstract class Client specifies the required client interface that each
 * Client subclass is supposed to implement.
 * 
 */
public abstract class Client
{
	protected Server server = swtkal.server.Server.getServer();;
	protected Person user;
	protected Properties clientProperties;
	
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
				e.printStackTrace();
				try
				{
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
