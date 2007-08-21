package client.admin.user; //
/////////////////////////////

import javax.swing.*;

import java.awt.event.*;

import data.Data;
import basisklassen.Person;

/////////////////////////////////////////////////////////////////////////////////////////////////
// EditUserControl // EditUserControl // EditUserControl // EditUserControl // EditUserControl //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class EditUserControl implements ActionListener {
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

	public EditUserControl(JFrame f) {
		parentFrame = f;
		isAdmin = true;

		editAllUser = new EditAllUser();
		editAllUser.addActionListener(this);
	}

	public EditUserControl(Person person) {
		isAdmin = false;

		editOneUser = new EditOneUser();
		editOneUser.addActionListener(this);

		// Start
		editOneUser.fill(person);
	}

	// GUI
	public JInternalFrame getGUI() {
		if (isAdmin) {
			return editAllUser.getGUI();
		} else {
			return (JInternalFrame) editOneUser.getGUI();
		}
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private void createUser(Person person) {
		Data.personen.create(person);
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private void updateUser(Person person) {
		Data.personen.update(person);
	}

	// delete // delete // delete // delete // delete // delete // delete //
	// delete // delete //
	private void deleteUser(Person person) {
		Data.personen.delete(person);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		// //////////////////////////////////////////////////////////////////////////////////////////
		// EditAllUser // EditAllUser // EditAllUser // EditAllUser //
		// EditAllUser // EditAllUser //
		// //////////////////////////////////////////////////////////////////////////////////////////

		if (c.equals("new")) {
			edit = false;

			editOneUser = new EditOneUser(parentFrame);
			editOneUser.addActionListener(this);

			editOneUser.start();
		}

		else if (c.equals("edit")) {
			edit = true;

			if ((person = editAllUser.getSelectedPerson()) != null) {
				editOneUser = new EditOneUser(parentFrame);
				editOneUser.addActionListener(this);

				editOneUser.fill(person);
				editOneUser.start();
			}
		}

		else if (c.equals("delete")) {
			if ((person = editAllUser.getSelectedPerson()) != null) {
				deleteUser(person);

				// update
				editAllUser.update();
			}
		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// EditOneUser // EditOneUser // EditOneUser // EditOneUser //
		// EditOneUser // EditOneUser //
		// //////////////////////////////////////////////////////////////////////////////////////////

		else if (c.equals("Abbrechen")) {
			// Dialog schlieﬂen
			if (isAdmin) {
				editOneUser.closeDialog();
			} else {
				editOneUser.closeFrame();
			}
		}

		else if (c.equals("OK")) {
			if (isAdmin) {
				if (editOneUser.checkInput()) {
					if (edit) {
						updateUser(editOneUser.getPerson());
					} else {
						createUser(editOneUser.getPerson());
					}

					// Dialog schlieﬂen
					editOneUser.closeDialog();

					// update
					editAllUser.update();
				}

			} else {
				updateUser(editOneUser.getPerson());
				editOneUser.closeFrame();
			}
		}
	}
}
