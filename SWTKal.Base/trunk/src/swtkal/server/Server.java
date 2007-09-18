package swtkal.server;

import java.io.*;
import java.util.Properties;
import java.util.logging.*;

/**
 * Abstract class Server specifies the required server interface.
 *
 */
public abstract class Server implements PersonenIF, TermineIF
{
	protected static Server server;				// Design Pattern Singelton
	protected static boolean isServerRunning = false;
	
	protected static Properties serverProperties;
	
	protected static Logger logger;				// for logging important activities
	protected static Handler handler;
	
	static 
	{
		// initialize serverProperties
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
			e.printStackTrace();
			try
			{	// write current properties to file swtkalServerProperties.xml
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
	
// TODO Javadoc-Kommentare f�r die Server-Methoden einf�gen	
	public static Server getServer()				// gets the Singelton object
	{
		if (server==null)
		{
			// create server object
			try
			{
				server = (Server) Class.forName(serverProperties.getProperty("ServerClass")).newInstance();
			}
				catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		return server;
	}
	
	public boolean isServerRunning()
	{
		return isServerRunning;
	}

	public Properties getServerProperties()
	{
		return serverProperties;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public void startServer()
	{
		if (!isServerRunning)
		{
			logger.info("Server gestartet");
			isServerRunning = true;
		}
		isServerRunning = true;
	}

	public void stopServer()
	{
		if (isServerRunning)
		{
			logger.info("Server gestoppt");
			isServerRunning = false;
		}
	}
}
