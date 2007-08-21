package client.eintrag; //
//////////////////////////

import javax.swing.*;

import java.awt.event.*;
import java.util.*;
import java.text.*;

import data.Data;
import basisklassen.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// EditToDoControl // EditToDoControl // EditToDoControl // EditToDoControl // EditToDoControl //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class EditToDoControl implements ActionListener {
	// EditToDo
	private EditToDo editToDo;

	// ToDo
	private ToDo toDo;

	// Flag
	private boolean edit;

	// New Instructor
	public EditToDoControl(JFrame f) {
		edit = false;

		editToDo = new EditToDo(f);
		editToDo.addActionListener(this);

		Date date = new Date();
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");

		// Start
		toDo = new ToDo(Data.user);
		toDo.getErinnernAb().setDatum(simpleDate.format(date));
		toDo.getFälligPer().setDatum(simpleDate.format(date));

		editToDo.start(toDo);
		editToDo.setTitle("ToDo eintragen");
	}

	// New Instructor
	public EditToDoControl(JFrame f, Datum d) {
		edit = false;

		editToDo = new EditToDo(f);
		editToDo.addActionListener(this);

		// Start
		toDo = new ToDo(Data.user);
		toDo.getErinnernAb().setDatum(d);
		toDo.getFälligPer().setDatum(d);

		editToDo.start(toDo);
		editToDo.setTitle("ToDo eintragen");
	}

	// Edit Instructor
	public EditToDoControl(JFrame f, ToDo t) {
		edit = true;

		editToDo = new EditToDo(f);
		editToDo.addActionListener(this);

		// Start
		toDo = t;

		if (toDo.getOwner().getID() == Data.user.getID()) {
			// Owner darf ToDo editieren
			editToDo.start(toDo);
			editToDo.setTitle("ToDo editieren");

		} else {
			editToDo.start(toDo);
			editToDo.setTitle("ToDo-Owner: " + toDo.getOwner().getNameLang());

			editToDo.disableGUI();
		}
	}

	// GUI
	public JInternalFrame getGUI() {
		return editToDo.getGUI();
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private void createToDo() {
		Data.toDo.create(toDo);
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private void updateToDo() {
		Data.toDo.update(toDo);
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			// alle Eingaben vollständig?
			if (editToDo.checkInput()) {
				if (!edit) {
					createToDo();
				} else {
					updateToDo();
				}

				Vector failed = editToDo.getFailedPersonen();
				if (failed != null) {
					Data.toDo.sendMissingRight(toDo, failed);
				}

				try {
					editToDo.getGUI().setClosed(true);
				} catch (java.beans.PropertyVetoException ex) {
				}
			}
		} else {
			try {
				editToDo.getGUI().setClosed(true);
			} catch (java.beans.PropertyVetoException ex) {
			}
		}
	}
}
