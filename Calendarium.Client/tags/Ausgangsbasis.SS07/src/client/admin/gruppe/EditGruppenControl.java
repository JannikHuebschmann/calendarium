package client.admin.gruppe; //
///////////////////////////////

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.event.*;

import data.Data;
import basisklassen.Gruppe;

//////////////////////////////////////////////////////////////////////////////////////////
// EditGruppenControl // EditGruppenControl // EditGruppenControl // EditGruppenControl //
//////////////////////////////////////////////////////////////////////////////////////////

public class EditGruppenControl implements ActionListener {
	// ParentFrame
	private JFrame parentFrame;

	// EditGruppen
	private EditGruppen editGruppen;

	// EditGruppe
	private EditGruppe editGruppe;

	// EditFlag
	private boolean edit;

	// Gruppe
	private Gruppe gruppe;

	private String kuerzelOld = "";

	public EditGruppenControl(JFrame f) {
		parentFrame = f;

		editGruppen = new EditGruppen();
		editGruppen.addActionListener(this);
	}

	// GUI
	public JInternalFrame getGUI() {
		return editGruppen.getGUI();
	}

	// weitereChecks // weitereChecks // weitereChecks // weitereChecks //
	// weitereChecks //
	String weitereChecks() {
		char c = 34;

		if (!kuerzelOld.equals(gruppe.getKuerzel())
				&& Data.gruppen.contains(gruppe.getKuerzel())) {
			return "Das Kuerzel "
					+ c
					+ gruppe.getKuerzel()
					+ c
					+ " existiert bereits, bitte vergeben Sie ein anderes Kuerzel!\n";
		}

		return null;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// Gruppe anlegen // Gruppe anlegen // Gruppe anlegen // Gruppe anlegen //
	// Gruppe anlegen //
	// //////////////////////////////////////////////////////////////////////////////////////////
	void createGruppe() {
		TreePath path = editGruppe.getParentPath();

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		Gruppe parent = (Gruppe) node.getUserObject();

		Data.gruppen.createGroupIn(gruppe, parent);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// Gruppe updaten // Gruppe updaten // Gruppe updaten // Gruppe updaten //
	// Gruppe updaten //
	// //////////////////////////////////////////////////////////////////////////////////////////
	void updateGruppe() {
		// Zuordnung geändert?
		TreePath path = editGruppe.getParentPath();
		TreePath pathOld = editGruppe.getParentPathOld();

		if (path != pathOld) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) pathOld
					.getLastPathComponent();
			Gruppe parentOld = (Gruppe) node.getUserObject();

			node = (DefaultMutableTreeNode) path.getLastPathComponent();
			Gruppe parentNew = (Gruppe) node.getUserObject();

			Data.gruppen.exchangeOrder(gruppe, parentOld, parentNew);
		}

		Data.gruppen.update(gruppe);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// Gruppe löschen // Gruppe löschen // Gruppe löschen // Gruppe löschen //
	// Gruppe löschen //
	// //////////////////////////////////////////////////////////////////////////////////////////
	void deleteGruppe(Gruppe gruppe, Gruppe parent) {
		Data.gruppen.deleteGroupOf(gruppe, parent);

		// Update View
		editGruppen.update();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		// //////////////////////////////////////////////////////////////////////////////////////////
		// EditGruppen // EditGruppen // EditGruppen // EditGruppen //
		// EditGruppen // EditGruppen //
		// //////////////////////////////////////////////////////////////////////////////////////////

		if (c.equals("add")) {
			// Gruppe hinzufuegen
			edit = false;
			gruppe = new Gruppe(Data.user.getKuerzel().equals("Admin")); // new
			// Gruppe(Data.user
			// ==
			// null);

			editGruppe = new EditGruppe(parentFrame, editGruppen
					.getSelectedPath(), gruppe);

			editGruppe.addActionListener(this);
			editGruppe.start();
		} else if (c.equals("edit")) {
			// Gruppe editieren
			edit = true;
			gruppe = editGruppen.getSelected();
			kuerzelOld = gruppe.getKuerzel();

			editGruppe = new EditGruppe(parentFrame, editGruppen
					.getParentPathOfSelected(), gruppe);

			editGruppe.addActionListener(this);
			editGruppe.start();
		} else if (c.equals("delete")) {
			// Gruppe löschen
			deleteGruppe(editGruppen.getSelected(), editGruppen
					.getParentOfSelected());
		}

		// ////////////////////////////////////////////////////////////////////////////////////
		// EditGruppe // EditGruppe // EditGruppe // EditGruppe // EditGruppe //
		// EditGruppe //
		// ////////////////////////////////////////////////////////////////////////////////////

		else if (c.equals("Abbrechen")) {
			// Dialog schließen
			editGruppe.close();
		} else if (c.equals("OK")) {
			if (editGruppe.checkInput(weitereChecks())) {
				if (edit) {
					updateGruppe();
				} else {
					createGruppe();
				}

				// Dialog schließen
				editGruppe.close();

				// Update View
				editGruppen.update();
			}
		}
	}
}
