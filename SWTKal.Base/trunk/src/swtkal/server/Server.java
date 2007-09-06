package swtkal.server;

public abstract class Server implements PersonenIF, TermineIF
{
	protected static Server server;				// Entwurfsmuster Singelton
	protected static boolean isServerRunning = false;
	
	public static Server getServer()				// Zugriff auf Server-Singelton
	{
		if (server==null)
		{
			try
			{
// TODO Serverklasse ueber property einlesen				
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
// TODO Logging einsetzen		
		isServerRunning = true;
	}

	public void stopServer()
	{
//	TODO Logging einsetzen		
			isServerRunning = false;
	}

	
	public boolean isServerRunning()
	{
		return isServerRunning;
	}
}
