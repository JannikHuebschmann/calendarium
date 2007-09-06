package swtkal.client;

import swtkal.domain.Person;
import swtkal.server.Server;

public abstract class Client
// TODO Methoden aus simple client übernehmen
{
	protected Server server;
	protected Person user;

	public Server getServer()
	{
		return server;
	}
	
	public Person getUser()
	{
		return user;
	}
}
