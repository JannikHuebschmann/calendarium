package swtkal.monitor;

import swtkal.server.Server;

public abstract class Monitor
{
	protected Server server = swtkal.server.Server.getServer();;
	
	public Server getServer()
	{
		return server;
	}
}
