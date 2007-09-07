package swtkal.client;

import swtkal.domain.Person;
import swtkal.server.Server;

/**
 * Abstract class Client specifies the required client interface.
 * 
 */
public abstract class Client
{
	protected Server server = swtkal.server.Server.getServer();;
	protected Person user;

	/**
	 * 
	 * @return returns the server to which the client is connected
	 */
	public Server getServer()
	{
		return server;
	}

	/**
	 * 
	 * @return returns the client user as a person object
	 */
	public Person getUser()
	{
		return user;
	}
}
