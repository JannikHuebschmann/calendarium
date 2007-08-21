package client.admin.typ; //
////////////////////////////

import javax.swing.*;

import java.awt.event.*;

import data.Data;
import basisklassen.Person;
import basisklassen.EintragsTyp;

//////////////////////////////////////////////////////////////////////////////////////////////////////
// EditTypenControl // EditTypenControl // EditTypenControl // EditTypenControl // EditTypenControl //
//////////////////////////////////////////////////////////////////////////////////////////////////////

public class EditTypenControl implements ActionListener {
	// ParentWindow
	private JFrame parentFrame;

	// EditTypen
	private EditTypen editTypen;

	// EditTyp
	private EditTyp editTyp;

	// Typ
	private EintragsTyp typ;

	// Person
	private Person person = null;

	// EditFlag
	private boolean edit;

	public EditTypenControl(JFrame f) // Administrator
	{
		parentFrame = f;

		editTypen = new EditTypen();
		editTypen.addActionListener(this);
	}

	public EditTypenControl(JFrame f, Person p) // User
	{
		parentFrame = f;
		person = p;

		editTypen = new EditTypen(person);
		editTypen.addActionListener(this);
	}

	// GUI
	public JInternalFrame getGUI() {
		return editTypen.getGUI();
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private void createTyp(EintragsTyp typ) {
		Data.typen.create(typ);
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private void updateTyp(EintragsTyp typ) {
		Data.typen.update(typ);
	}

	// delete // delete // delete // delete // delete // delete // delete //
	// delete // delete //
	private void deleteTyp(EintragsTyp typ) {
		Data.typen.delete(typ);
	}

	// setUserTyp //setUserTyp //setUserTyp //setUserTyp //setUserTyp
	// //setUserTyp //setUserTyp //
	private void setUserTyp(EintragsTyp typ) {
		Data.typen.setUserTyp(typ);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		// ///////////////////////////////////////////////////////////////////////////////////////////
		// EditTypen // EditTypen // EditTypen // EditTypen // EditTypen //
		// EditTypen // EditTypen //
		// ///////////////////////////////////////////////////////////////////////////////////////////

		if (c.equals("changeColor")) {
			if ((typ = editTypen.getSelectedTyp()) != null) {
				editTyp = new EditTyp(parentFrame);
				editTyp.addActionListener(this);

				editTyp.fill(typ);
				editTyp.start(false);
			}
		}

		if (c.equals("new")) {
			edit = false;

			editTyp = new EditTyp(parentFrame);
			editTyp.addActionListener(this);

			editTyp.start(true);
		}

		else if (c.equals("edit")) {
			edit = true;

			if ((typ = editTypen.getSelectedTyp()) != null) {
				editTyp = new EditTyp(parentFrame);
				editTyp.addActionListener(this);

				editTyp.fill(typ);
				editTyp.start(true);
			}
		}

		else if (c.equals("delete")) {
			if ((typ = editTypen.getSelectedTyp()) != null) {
				deleteTyp(typ);
				editTypen.update(true);
			}
		}

		// ////////////////////////////////////////////////////////////////////////////////////////
		// EditTyp // EditTyp // EditTyp // EditTyp // EditTyp // EditTyp //
		// EditTyp // EditTyp //
		// ////////////////////////////////////////////////////////////////////////////////////////

		else if (c.equals("Abbrechen")) {
			// Dialog schlieﬂen
			editTyp.close();
		} else if (c.equals("OK")) {
			if (person == null) {
				if (edit) {
					updateTyp(editTyp.getTyp());
					editTypen.update(false);

				} else {
					createTyp(editTyp.getTyp());
					editTypen.update(true);
				}
			} else {
				setUserTyp(editTyp.getTyp());
				editTypen.update(false);
			}

			// Dialog schlieﬂen
			editTyp.close();
		}
	}
}
