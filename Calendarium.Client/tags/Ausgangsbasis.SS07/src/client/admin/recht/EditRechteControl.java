package client.admin.recht; //
//////////////////////////////

import javax.swing.*;

import java.awt.event.*;
import java.util.*;

import data.Data;
import basisklassen.*;

//////////////////////////////////////////////////////////////////////////////////////
// EditRechteControl // EditRechteControl // EditRechteControl // EditRechteControl //
//////////////////////////////////////////////////////////////////////////////////////

public class EditRechteControl implements ActionListener {
	private EditRechte editRechte;

	public EditRechteControl() {
		editRechte = new EditRechte();
		editRechte.addActionListener(this);

		// Start
		editRechte.start();
	}

	// GUI
	public JInternalFrame getGUI() {
		return editRechte.getFrame();
	}

	private Recht containsRecht(Vector rechte, Recht recht) {
		Enumeration e = rechte.elements();
		Recht r;

		while (e.hasMoreElements()) {
			r = (Recht) e.nextElement();
			if (recht.equals(r))
				return r;
		}

		return null;
	}

	// Update // Update // Update // Update // Update // Update // Update //
	// Update // Update //
	private void updateRechte() {
		Object[] entry;

		// Gruppen
		Hashtable groups = Data.rechte.getGrantGroups();

		while ((entry = editRechte.getNextGroupEntry()) != null) {
			Gruppe empf�nger = (Gruppe) entry[0];
			Vector grantsNew = (Vector) entry[1];
			Vector grantsOld = null;

			if (groups != null
					&& groups.containsKey(new Long(empf�nger.getID()))) {
				grantsOld = (Vector) Data.rechte.getGrantsAt(empf�nger);
			}

			if (grantsOld != null) {
				Enumeration e = grantsOld.elements();
				while (e.hasMoreElements()) {
					Recht r, recht = (Recht) e.nextElement();

					if ((r = containsRecht(grantsNew, recht)) == null) {
						// Recht vom Empf�nger l�schen
						Data.rechte.retractGrantAt(empf�nger, recht);

					} else
						grantsNew.removeElement(r);
				}

				e = grantsNew.elements();
				while (e.hasMoreElements()) {
					Recht recht = (Recht) e.nextElement();

					// Recht dem Empf�nger hinzufuegen
					Data.rechte.addGrantAt(empf�nger, recht);
				}

				groups.remove(new Long(empf�nger.getID()));

			} else {
				// Neuen Empf�nger mit entspr. Rechten anlegen
				Data.rechte.addReceiver(empf�nger, grantsNew);
			}
		}

		// Personen
		Hashtable persons = Data.rechte.getGrantPersons();

		while ((entry = editRechte.getNextPersonEntry()) != null) {
			Person empf�nger = (Person) entry[0];
			Vector grantsNew = (Vector) entry[1];
			Vector grantsOld = null;

			if (persons != null
					&& persons.containsKey(new Long(empf�nger.getID()))) {
				grantsOld = (Vector) Data.rechte.getGrantsAt(empf�nger);
			}

			if (grantsOld != null) {
				Enumeration e = grantsOld.elements();
				while (e.hasMoreElements()) {
					Recht r, recht = (Recht) e.nextElement();

					if ((r = containsRecht(grantsNew, recht)) == null) {
						// Recht vom Empf�nger l�schen
						Data.rechte.retractGrantAt(empf�nger, recht);

					} else
						grantsNew.removeElement(r);
				}

				e = grantsNew.elements();
				while (e.hasMoreElements()) {
					Recht recht = (Recht) e.nextElement();

					// Recht dem Empf�nger hinzufuegen
					Data.rechte.addGrantAt(empf�nger, recht);
				}

				persons.remove(new Long(empf�nger.getID()));

			} else {
				// Neuen Empf�nger mit entspr. Rechten anlegen
				Data.rechte.addReceiver(empf�nger, grantsNew);
			}
		}

		// Gruppen
		if (groups != null) {
			Enumeration e = groups.elements();
			while (e.hasMoreElements()) {
				// Empf�nger mit s�mtlichen Rechten l�schen
				Data.rechte.deleteReceiver((Gruppe) e.nextElement());
			}
		}

		// Personen
		if (persons != null) {
			Enumeration e = persons.elements();
			while (e.hasMoreElements()) {
				// Empf�nger mit s�mtlichen Rechten l�schen
				Data.rechte.deleteReceiver((Person) e.nextElement());
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")
				|| e.getActionCommand().equals("Abbrechen")) {
			if (e.getActionCommand().equals("OK")) {
				updateRechte();
			}

			try {
				getGUI().setClosed(true);
			} catch (java.beans.PropertyVetoException ex) {
			}
		}
	}
}
