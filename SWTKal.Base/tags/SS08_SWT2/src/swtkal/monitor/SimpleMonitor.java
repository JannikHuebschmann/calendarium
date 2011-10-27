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
package swtkal.monitor;

import java.io.IOException;
import java.util.Vector;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;

/*****************************************************************************************************
 * Class SimpleMonitor is a simple ASCII monitor that can be
 * used to test the SimpleServer implementation. It uses the
 * predefined admin user with kuerzel "ADM".
 * 
 * @author calendarium project
 */
public class SimpleMonitor extends Monitor
{
	protected static SimpleMonitor monitor = new SimpleMonitor();

	public static void main(String[] args) throws IOException
	{
		monitor.connectToServer();
		monitor.processCommands();
		monitor.disconnectFromServer();
	}

	protected void connectToServer()
	{
		System.out.println("Server initialisieren ...");
		server.startServer();
		try
		{
			user = server.findPerson("ADM");
		}
		catch (PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	protected void disconnectFromServer()
	{
		System.out.println("Server beenden ...");
		server.stopServer();
	}

	protected void processCommands() throws IOException
	{
		System.out.println("bitte geben Sie Kommandos oder \"help\" ein");

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

	protected void processCommand(String command)
	{
// TODO SimpleMonitor weiter ausbauen!		
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

	protected void help()
	{
		System.out.println("Kommandos:");
		System.out.println("\taddperson");
		System.out.println("\tdelperson");
		System.out.println("\tlistperson");
		System.out.println("");
		System.out.println("\texit");
		System.out.println("\thelp");
	}

	protected void addperson()
	{
		Person p;
		String kuerzel;
		// create a new person and insert it
		// details should be read in from System.in!!!
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

	protected void delperson()
	{
		Person p;
		// deletes a default person
		// details should be read in from System.in!!!
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

	protected void listperson()
	{
		Vector<Person> personen = server.getPersonVector();
		for (Person p : personen)
		{
			System.out.println(p.toString());
		}
	}
}
