/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007		swtUser			initial version
 *
 */
package swtkal.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/*****************************************************************************************************
 * Abstract class Server specifies the required server interface and realizes
 * common base functionality.
 * 
 * @author swtUser
 */
public abstract class Server implements PersonenIF, TermineIF
{
	protected static Server server;				// Design Pattern Singleton
	                                            // there is only one server object!
	protected static boolean isServerRunning;
	protected static Properties serverProperties;
	
	protected static Logger logger;				// for logging important activities
	protected static Handler handler;
	
	static 
	{
		// initialize serverProperties
		isServerRunning = false;
		serverProperties = new Properties();

		// set default properties
		serverProperties.put("ServerClass", "swtkal.server.SimpleServer");
		serverProperties.put("ServerLogLevel", "WARNING");

		// overwrite properties from swtkalServerProperties.xml if file exists
		try
		{
			FileInputStream in = new FileInputStream("swtkalServerProperties.xml");
			serverProperties.loadFromXML(in);
			in.close();
		}
		catch (IOException e)
		{
			System.err.println("File swtkalServerProperties.xml not found. Default generated!");
			try
			{	// write current properties to file (in case it does not yet exist)
				FileOutputStream out = new FileOutputStream("swtkalServerProperties.xml");
				serverProperties.storeToXML(out, "--- SWTKal Server Settings ---");
				out.close();
			}
			catch (IOException ex)
			{
				e.printStackTrace();
			}
		}
		
		// initialize logger and handler
		logger = Logger.getLogger("swtkal");
		try
		{
			handler = new FileHandler("swtkal.log", false);
			handler.setLevel(Level.parse(serverProperties.getProperty("ServerLogLevel")));
			logger.addHandler(handler);
		}
			catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This class method generates the Singleton server object if necessary and
	 * returns it. The specific class for the server is specified in the server
	 * properties.
	 * 
	 * @return the Singleton server object  
	 */
	public static Server getServer()
	{
		if (server==null)
		{
			// create server object
			try
			{
				server = (Server) Class.forName(serverProperties.getProperty("ServerClass")).newInstance();
				logger.info("Genutzte Server-Klasse: " + serverProperties.getProperty("ServerClass"));
			}
				catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		return server;
	}
		
	/**
	 *
	 * @return boolean value that indicates whether the server is running or not
	 */
	public boolean isServerRunning()
	{
		return isServerRunning;
	}
	
	/** 
	 *
	 * @return the current server properties as specified in the file swtkalServerProperties.xml
	 */
	public Properties getServerProperties()
	{
		return serverProperties;
	}
	
	/** 
	 *
	 * @return the default logger object for server activities
	 */
	public Logger getLogger()
	{
		return logger;
	}

	/** 
	 * This function starts the server if necessary and logs this.
	 */
	public void startServer()
	{
		if (!isServerRunning)
		{
			logger.info("Server gestartet");
			isServerRunning = true;
		}
	}

	/**
	 * This function stops the server if necessary and logs this.
	 */
	public void stopServer()
	{
		if (isServerRunning)
		{
			logger.info("Server gestoppt");
			isServerRunning = false;
		}
	}
}
