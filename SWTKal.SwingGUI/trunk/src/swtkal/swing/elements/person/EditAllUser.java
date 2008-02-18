package swtkal.swing.elements.person;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import swtkal.domain.Person;
import swtkal.server.Server;
import swtkal.swing.ListenerForActions;
import swtkal.swing.elements.person.SelectListEntry;

////////////////////////////////////////////////////////////////////////////////////////////
// EditAllUser // EditAllUser // EditAllUser // EditAllUser // EditAllUser // EditAllUser //
////////////////////////////////////////////////////////////////////////////////////////////

public class EditAllUser extends ListenerForActions implements
		ListSelectionListener
{
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Benutzer verwalten", true,
			true, false, true);

	// Liste zur Personenauswahl
	private SelectListEntry selectUser;

	// Buttons
	private JButton btn[] = new JButton[3];
	
	private Server server;

	public EditAllUser(Server server)
	{
		this.server = server;
		gui.setSize(370, 300);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				gui.setVisible(false);
			}
		});

		selectUser = new SelectListEntry(false, server);
		selectUser.addListSelectionListener(this);

		create();
	}

	void create()
	{
		Dimension size;

		JPanel editPane = new JPanel();
		editPane.setLayout(new BorderLayout(10, 0));
		editPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		btn[0] = new JButton("Benutzer hinzufuegen");
		size = btn[0].getMaximumSize();
		size.width = 10000;
		btn[0].setMaximumSize(size);
		btn[0].setMnemonic('h');
		btn[0].setActionCommand("new");
		btn[0].addActionListener(this);

		btn[1] = new JButton("Benutzer löschen");
		btn[1].setMaximumSize(size);
		btn[1].setEnabled(false);
		btn[1].setMnemonic('l');
		btn[1].setActionCommand("delete");
		btn[1].addActionListener(this);

		btn[2] = new JButton("Benutzer bearbeiten");
		btn[2].setMaximumSize(size);
		btn[2].setEnabled(false);
		btn[2].setMnemonic('b');
		btn[2].setActionCommand("edit");
		btn[2].addActionListener(this);

		buttons.add(Box.createVerticalStrut(60));
		buttons.add(btn[0]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[1]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[2]);
		buttons.add(Box.createVerticalGlue());

		editPane.add("Center", selectUser.getGUI());
		editPane.add("East", buttons);

		gui.getContentPane().add("Center", editPane);
	}

	JInternalFrame getGUI()
	{
		return gui;
	}

	Person getSelectedPerson()
	{
		return (Person) selectUser.mListBox.getSelectedValue();
	}

	void update()
	{
		selectUser.createModel(server);
		selectUser.mListBox.validate();
	}

	// ListSelectionListener // ListSelectionListener // ListSelectionListener
	// // ListSelectionListener //
	public void valueChanged(ListSelectionEvent e)
	{
		if (getSelectedPerson() != null)
		{
			btn[1].setEnabled(true);
			btn[2].setEnabled(true);

		}
		else
		{
			btn[1].setEnabled(false);
			btn[2].setEnabled(false);
		}
	}
}
