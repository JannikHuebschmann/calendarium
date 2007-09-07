package swtkal.server;

// TODO Logging hier für Server einsetzen
// TODO JUnit hier für Server einsetzen

/**
 * Abstract class Server specifies the required server interface.
 *
 */
public abstract class Server implements PersonenIF, TermineIF
{
	protected static Server server;				// Design Pattern Singelton
	protected static boolean isServerRunning = false;

// TODO Javadoc-Kommentare für die Server-Methoden einfügen	
	public static Server getServer()				// gets the Singelton object
	{
		if (server==null)
		{
			try
			{
// FIXME genutzte Serverklasse ueber property einlesen				
				server = (Server) Class.forName("swtkal.server.SimpleServer").newInstance();
			}
				catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return server;
	}
	
	public void startServer()
	{
		if (!isServerRunning)
		{
			System.out.println("Server starten ...");
			isServerRunning = true;
		}
		isServerRunning = true;
	}

	public void stopServer()
	{
		if (isServerRunning)
		{
			System.out.println("Server stoppen ...");
			isServerRunning = false;
		}
	}

	public boolean isServerRunning()
	{
		return isServerRunning;
	}
}
