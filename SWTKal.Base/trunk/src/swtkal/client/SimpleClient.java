package swtkal.client;

import java.io.IOException;
import java.util.Vector;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;
import swtkal.server.Server;

public abstract class SimpleClient extends Client
{
	protected static Server server;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		initServer();
		processCommands();
		shutdownServer();
	}

	protected static void initServer()
	{
		// TODO Logging nutzen
		System.out.println("Server initialisieren ...");
		server = swtkal.server.SimpleServer.getServer();
	}

	protected static void shutdownServer()
	{
		System.out.println("Server beenden ...");
	}

	protected static void processCommands() throws IOException
	{
		while (true)
		{
			System.out.print("> ");

			String command = "";
			char read = '\0';

			while (read != '\r' && read != '\n')
			{
				read = (char) System.in.read();
				command = command + read;
			}
			// clear out newlines from system input
			int available = System.in.available();
			for (int i = 0; i < available; i++)
				System.in.read();

			command = command.trim();
			if (command.equals("exit"))
			{
				break;
			}
			processCommand(command);
		}
	}

	protected static void processCommand(String command)
	{
		if (command.startsWith("help"))
		{
			help();
		}
		else if (command.startsWith("addperson"))
		{
			addperson();
		}
		else if (command.startsWith("delperson"))
		{
			delperson();
		}
		else if (command.startsWith("listperson"))
		{
			listperson();
		}
		else
		{
			System.out.println("Unbekanntes Kommando - siehe auch Kommando \"help\"");
		}
	}

	protected static void help()
	{
		System.out.println("Kommandos:");
		System.out.println("\taddperson");
		System.out.println("\tdelperson");
		System.out.println("\tlistperson");
		System.out.println("");
		System.out.println("\texit");
		System.out.println("\thelp");
	}

	protected static void addperson()
	{
		Person p;
		String kuerzel;
		// TODO Personendaten interaktiv einlesen
		p = new Person("Vorname","Nachname","kuerzel");
		kuerzel = p.getKuerzel();
		try
		{
			server.insert(p, kuerzel);
		}
		catch (PersonException e)
		{
			System.out.println(e.getMessage());
		}
	}

	protected static void delperson()
	{
		Person p;
		// TODO Personendaten interaktiv einlesen
		p = new Person("Vorname","Nachname","kuerzel");
		try
		{
			server.delete(p);
		}
		catch (PersonException e)
		{
			System.out.println(e.getMessage());
		}
	}

	protected static void listperson()
	{
		Vector<Person> personen = server.getOrderedVector();
		for (Person p : personen)
		{
			System.out.println(p.toString());
		}
	}
}
