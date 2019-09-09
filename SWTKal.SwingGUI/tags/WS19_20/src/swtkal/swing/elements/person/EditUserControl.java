package swtkal.swing.elements.person;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.event.*;
import javax.swing.*;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;
import swtkal.server.Server;

/////////////////////////////////////////////////////////////////////////////////////////////////
// EditUserControl // EditUserControl // EditUserControl // EditUserControl // EditUserControl //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class EditUserControl implements ActionListener
{
	// ParentFrame
	private JFrame parentFrame;

	// EditOneUser
	private EditOneUser editOneUser;

	// EditAllUser
	private EditAllUser editAllUser;

	// Person
	private Person person;

	// EditFlag
	private boolean edit;

	// AdminFlag
	private boolean isAdmin;
	
	// SWTKal Server
	Server server;

	public EditUserControl(Server server, JFrame f)
	{
		this.server = server;
		
		parentFrame = f;
		isAdmin = true;

		editAllUser = new EditAllUser(server);
		editAllUser.addActionListener(this);
	}

	public EditUserControl(Person person)
	{
		isAdmin = false;

		editOneUser = new EditOneUser();
		editOneUser.addActionListener(this);

		// Start
		editOneUser.fill(person);
	}

	// GUI
	public JInternalFrame getGUI()
	{
		if (isAdmin)
		{
			return editAllUser.getGUI();
		}
		else
		{
			return (JInternalFrame) editOneUser.getGUI();
		}
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private void createUser(Person person, String passwort)
	{
		try
		{
			server.insert(person, passwort);
		}
		catch (PersonException e)
		{
// XXX ausfuehrlicheres Exception Handling?
			e.printStackTrace();
		}
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private void updateUser(Person person, String neuesKuerzel, String neuesPasswort)
	{
		try
		{
			String oldKuerzel = person.getKuerzel();
			
			if (!neuesKuerzel.equalsIgnoreCase(oldKuerzel))
			{
				person.setKuerzel(neuesKuerzel);
				server.updateKuerzel(person, oldKuerzel);
			}
			
			server.updatePasswort(person, neuesPasswort);
			server.update(person);
		}
		catch (PersonException e)
		{
			// XXX ausfuehrlicheres Exception Handling?
			e.printStackTrace();
		}
	}

	// delete // delete // delete // delete // delete // delete // delete //
	// delete // delete //
	private void deleteUser(Person person)
	{
		try
		{
			server.delete(person);
		}
		catch (PersonException e)
		{
			// vernünftige Fehlerbehandlung fehlt noch
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{
		String c = e.getActionCommand();

		// //////////////////////////////////////////////////////////////////////////////////////////
		// EditAllUser // EditAllUser // EditAllUser // EditAllUser //
		// EditAllUser // EditAllUser //
		// //////////////////////////////////////////////////////////////////////////////////////////

		if (c.equals("new"))
		{
			edit = false;

			editOneUser = new EditOneUser(parentFrame);
			editOneUser.addActionListener(this);

			editOneUser.start();
		}

		else if (c.equals("edit"))
		{
			edit = true;

			if ((person = editAllUser.getSelectedPerson()) != null)
			{
				editOneUser = new EditOneUser(parentFrame);
				editOneUser.addActionListener(this);

				editOneUser.fill(person);
				editOneUser.start();
			}
		}

		else if (c.equals("delete"))
		{
			if ((person = editAllUser.getSelectedPerson()) != null)
			{
				deleteUser(person);

				// update
				editAllUser.update();
			}
		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// EditOneUser // EditOneUser // EditOneUser // EditOneUser //
		// EditOneUser // EditOneUser //
		// //////////////////////////////////////////////////////////////////////////////////////////

		else if (c.equals("Abbrechen"))
		{
			// Dialog schließen
			if (isAdmin)
			{
				editOneUser.closeDialog();
			}
			else
			{
				editOneUser.closeFrame();
			}
		}

		else if (c.equals("OK"))
		{
			if (isAdmin)
			{
				if (editOneUser.checkInput())
				{
					if (edit)
					{
						updateUser(editOneUser.getPerson(), editOneUser.getKuerzel(), editOneUser.getPasswort());
					}
					else
					{
						createUser(editOneUser.getPerson(), editOneUser.getPasswort());
					}

					// Dialog schließen
					editOneUser.closeDialog();

					// update
					editAllUser.update();
				}

			}
			else
			{
				updateUser(editOneUser.getPerson(), editOneUser.getKuerzel(), editOneUser.getPasswort());
				editOneUser.closeFrame();
			}
		}
	}
}
