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
			Gruppe empfänger = (Gruppe) entry[0];
			Vector grantsNew = (Vector) entry[1];
			Vector grantsOld = null;

			if (groups != null
					&& groups.containsKey(new Long(empfänger.getID()))) {
				grantsOld = (Vector) Data.rechte.getGrantsAt(empfänger);
			}

			if (grantsOld != null) {
				Enumeration e = grantsOld.elements();
				while (e.hasMoreElements()) {
					Recht r, recht = (Recht) e.nextElement();

					if ((r = containsRecht(grantsNew, recht)) == null) {
						// Recht vom Empfänger löschen
						Data.rechte.retractGrantAt(empfänger, recht);

					} else
						grantsNew.removeElement(r);
				}

				e = grantsNew.elements();
				while (e.hasMoreElements()) {
					Recht recht = (Recht) e.nextElement();

					// Recht dem Empfänger hinzufuegen
					Data.rechte.addGrantAt(empfänger, recht);
				}

				groups.remove(new Long(empfänger.getID()));

			} else {
				// Neuen Empfänger mit entspr. Rechten anlegen
				Data.rechte.addReceiver(empfänger, grantsNew);
			}
		}

		// Personen
		Hashtable persons = Data.rechte.getGrantPersons();

		while ((entry = editRechte.getNextPersonEntry()) != null) {
			Person empfänger = (Person) entry[0];
			Vector grantsNew = (Vector) entry[1];
			Vector grantsOld = null;

			if (persons != null
					&& persons.containsKey(new Long(empfänger.getID()))) {
				grantsOld = (Vector) Data.rechte.getGrantsAt(empfänger);
			}

			if (grantsOld != null) {
				Enumeration e = grantsOld.elements();
				while (e.hasMoreElements()) {
					Recht r, recht = (Recht) e.nextElement();

					if ((r = containsRecht(grantsNew, recht)) == null) {
						// Recht vom Empfänger löschen
						Data.rechte.retractGrantAt(empfänger, recht);

					} else
						grantsNew.removeElement(r);
				}

				e = grantsNew.elements();
				while (e.hasMoreElements()) {
					Recht recht = (Recht) e.nextElement();

					// Recht dem Empfänger hinzufuegen
					Data.rechte.addGrantAt(empfänger, recht);
				}

				persons.remove(new Long(empfänger.getID()));

			} else {
				// Neuen Empfänger mit entspr. Rechten anlegen
				Data.rechte.addReceiver(empfänger, grantsNew);
			}
		}

		// Gruppen
		if (groups != null) {
			Enumeration e = groups.elements();
			while (e.hasMoreElements()) {
				// Empfänger mit sämtlichen Rechten löschen
				Data.rechte.deleteReceiver((Gruppe) e.nextElement());
			}
		}

		// Personen
		if (persons != null) {
			Enumeration e = persons.elements();
			while (e.hasMoreElements()) {
				// Empfänger mit sämtlichen Rechten löschen
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
