package client.admin.recht; //
//////////////////////////////

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import data.*;
import basisklassen.*;
import client.utility.darstellung.BasicSelection;
import client.utility.darstellung.ButtonPanel;
import client.utility.darstellung.cell.*;

//////////////////////////////////////////////////////////////////////////////////////
// EditRechte // EditRechte // EditRechte // EditRechte // EditRechte // EditRechte //
//////////////////////////////////////////////////////////////////////////////////////
public class EditRechte extends BasicSelection implements ActionListener,
		Shared {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Rechte vergeben", true,
			true, false, true);

	// Events
	private EventListenerList mActionListenerList;

	private int lineNrGr = 0;

	private int lineNrPn = 0;

	// UserTypen
	private Hashtable typen = Data.typen.getUserTypen();

	public EditRechte() {
		super(348);
		mActionListenerList = new EventListenerList();

		gui.setSize(610, 485);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		adjustTable();
		create();
	}

	void create() {
		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		gui.getContentPane().add("Center", super.getGUI());
		gui.getContentPane().add("South", buttons);
	}

	JInternalFrame getFrame() {
		return gui;
	}

	void adjustTable() {
		TableColumn col;

		// Columns
		GruppenCellRenderer groupRend = new GruppenCellRenderer(Gruppe.class);
		GruppenCellRenderer persRend = new GruppenCellRenderer(Person.class);

		// EintragsTypen // EintragsTypen // EintragsTypen // EintragsTypen //
		// EintragsTypen //
		JComboBox selectTyp = new JComboBox();

		Enumeration e = typen.elements();
		while (e.hasMoreElements()) {
			selectTyp.addItem((EintragsTyp) e.nextElement());
		}

		selectTyp.setRenderer(new TypListCellRenderer());
		// selectTyp.setSelectedIndex(0);

		// Rechte // Rechte // Rechte // Rechte // Rechte // Rechte // Rechte //
		// Rechte //
		JComboBox selectRecht = new JComboBox();
		for (int i = 0; i < RECHTE.length; i++) {
			selectRecht.addItem(RECHTE[i]);
		}

		// GruppenTable // GruppenTable // GruppenTable // GruppenTable //
		// GruppenTable //
		gruppenTableModel.setColumnIdentifiers(new String[] { "Kzl", "Name",
				"Termintyp", "Recht" });

		col = gruppenTable.getColumn("Kzl");
		col.setCellRenderer(groupRend);
		col.setMaxWidth(26);

		col = gruppenTable.getColumn("Name");
		col.setCellRenderer(groupRend);
		col.setMaxWidth(105);

		col = gruppenTable.getColumn("Termintyp");
		col.setCellEditor(new DefaultCellEditor(selectTyp));
		col.setCellRenderer(new TypTableCellRenderer());
		col.setMaxWidth(105);

		col = gruppenTable.getColumn("Recht");
		col.setCellEditor(new DefaultCellEditor(selectRecht));
		col.setMaxWidth(105);

		// PersonenTable // PersonenTable // PersonenTable // PersonenTable //
		// PersonenTable //
		personenTableModel.setColumnIdentifiers(new String[] { "Kzl", "Name",
				"Termintyp", "Recht" });

		col = personenTable.getColumn("Kzl");
		col.setCellRenderer(persRend);
		col.setMaxWidth(26);

		col = personenTable.getColumn("Name");
		col.setCellRenderer(persRend);
		col.setMaxWidth(105);

		col = personenTable.getColumn("Termintyp");
		col.setCellEditor(new DefaultCellEditor(selectTyp));
		col.setCellRenderer(new TypTableCellRenderer());
		col.setMaxWidth(105);

		col = personenTable.getColumn("Recht");
		col.setCellEditor(new DefaultCellEditor(selectRecht));
		col.setMaxWidth(105);

		gruppenTable.getTableHeader().setReorderingAllowed(false);
		personenTable.getTableHeader().setReorderingAllowed(false);
	}

	void start() {
		// Gruppen
		Hashtable groups = Data.rechte.getGrantGroups();

		if (groups != null) {
			Enumeration e = groups.elements();
			while (e.hasMoreElements()) {

				Gruppe gruppe = (Gruppe) e.nextElement();
				addGruppe(gruppe, Data.rechte.getGrantsAt(gruppe));
			}
		}

		// Personen
		Hashtable persons = Data.rechte.getGrantPersons();

		if (persons != null) {
			Enumeration e = persons.elements();
			while (e.hasMoreElements()) {
				Person person = (Person) e.nextElement();
				addPerson(person, Data.rechte.getGrantsAt(person));
			}
		}

	}

	// Gruppen
	@SuppressWarnings("unchecked")
	Object[] getNextGroupEntry() {
		if (lineNrGr < gruppenTable.getRowCount()) {
			Vector rechte = new Vector();
			int i;

			do {
				String recht = (String) gruppenTable.getValueAt(lineNrGr, 3);

				for (i = 0; i < RECHTE.length; i++) {
					if (recht.equals(RECHTE[i]))
						break;
				}

				rechte.addElement(new Recht((EintragsTyp) gruppenTable
						.getValueAt(lineNrGr, 2), i));
				lineNrGr++;

			} while (lineNrGr < gruppenTable.getRowCount()
					&& ((Gruppe) gruppenTable.getValueAt(lineNrGr - 1, 0))
							.getID() == ((Gruppe) gruppenTable.getValueAt(
							lineNrGr, 0)).getID());

			return new Object[] { gruppenTable.getValueAt(lineNrGr - 1, 0),
					rechte };

		} else
			return null;
	}

	// Personen
	@SuppressWarnings("unchecked")
	Object[] getNextPersonEntry() {
		if (lineNrPn < personenTable.getRowCount()) {
			Vector rechte = new Vector();
			int i;

			do {
				String recht = (String) personenTable.getValueAt(lineNrPn, 3);

				for (i = 0; i < RECHTE.length; i++) {
					if (recht.equals(RECHTE[i]))
						break;
				}

				rechte.addElement(new Recht((EintragsTyp) personenTable
						.getValueAt(lineNrPn, 2), i));
				lineNrPn++;

			} while (lineNrPn < personenTable.getRowCount()
					&& ((Person) personenTable.getValueAt(lineNrPn - 1, 0))
							.getID() == ((Person) personenTable.getValueAt(
							lineNrPn, 0)).getID());

			return new Object[] { personenTable.getValueAt(lineNrPn - 1, 0),
					rechte };

		} else
			return null;
	}

	void addGruppe(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		Gruppe gruppe = (Gruppe) node.getUserObject();

		addGruppe(gruppe, null);
	}

	void addGruppe(Gruppe gruppe, Vector rechte) {
		Gruppe g;
		String name = gruppe.getName();

		int count = gruppenTableModel.getRowCount();
		int i;

		// Sortiert einfuegen
		for (i = 0; i < count; i++) {
			g = (Gruppe) gruppenTableModel.getValueAt(i, 0);
			if (name.compareTo(g.getName()) < 0)
				break;
		}

		if (rechte != null) {
			Enumeration e = rechte.elements();
			while (e.hasMoreElements()) {
				Recht recht = (Recht) e.nextElement();
				recht.setEintragsTyp((EintragsTyp) typen.get(new Long(recht
						.getEintragsTyp().getID()))); // XX

				gruppenTableModel.insertRow(i,
						new Object[] { gruppe, gruppe, recht.getEintragsTyp(),
								RECHTE[recht.getRechtsIndex()] });
			}
		} else {
			count = i - 1;
			Hashtable typs = (Hashtable) typen.clone();

			while (count >= 0
					&& gruppe.getID() == ((Gruppe) gruppenTableModel
							.getValueAt(count, 0)).getID()) {

				EintragsTyp typ = (EintragsTyp) gruppenTableModel.getValueAt(
						count, 2);

				typs.remove(new Long(typ.getID()));
				count--;
			}

			if (typs.size() > 0) {
				gruppenTableModel.insertRow(i, new Object[] { gruppe, gruppe,
						typs.elements().nextElement(),
						RECHTE[RECHTE.length - 1] });

				gruppenTable.setRowSelectionInterval(i, i);
				gruppenTable.scrollRectToVisible(gruppenTable.getCellRect(
						i + 1, 0, false));
			}
		}
	}

	void removeGruppe(int rowIndex) {
		gruppenTableModel.removeRow(rowIndex);
	}

	void addPerson(int index) {
		Person person = (Person) selectPerson.mModel.get(index);
		addPerson(person, null);
	}

	void addPerson(Person person, Vector rechte) {
		Person p = null;
		String name = person.getNameLang();

		int count = personenTableModel.getRowCount();
		int i;

		// Sortiert einfuegen
		for (i = 0; i < count; i++) {
			p = (Person) personenTableModel.getValueAt(i, 0);
			if (name.compareTo(p.getNameLang()) < 0)
				break;
		}

		if (rechte != null) {
			Enumeration e = rechte.elements();
			while (e.hasMoreElements()) {
				Recht recht = (Recht) e.nextElement();
				recht.setEintragsTyp((EintragsTyp) typen.get(new Long(recht
						.getEintragsTyp().getID()))); // XX

				personenTableModel.insertRow(i,
						new Object[] { person, person, recht.getEintragsTyp(),
								RECHTE[recht.getRechtsIndex()] });
			}
		} else {
			count = i - 1;
			Hashtable typs = (Hashtable) typen.clone();

			while (count >= 0
					&& person.getID() == ((Person) personenTableModel
							.getValueAt(count, 0)).getID()) {
				EintragsTyp typ = (EintragsTyp) personenTableModel.getValueAt(
						count, 2);

				typs.remove(new Long(typ.getID())); // XX
				count--;
			}

			if (typs.size() > 0) {
				personenTableModel.insertRow(i, new Object[] { person, person,
						typs.elements().nextElement(),
						RECHTE[RECHTE.length - 1] });

				personenTable.setRowSelectionInterval(i, i);
				personenTable.scrollRectToVisible(personenTable.getCellRect(
						i + 1, 0, false));
			}
		}
	}

	void removePerson(int rowIndex) {
		personenTableModel.removeRow(rowIndex);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void addActionListener(ActionListener listener) {
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener) {
		mActionListenerList.remove(ActionListener.class, listener);
	}

	protected void fireActionEvent(ActionEvent e) {
		Object[] listeners = mActionListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		if (c.equals("rightArrow1")) { // add Gruppe
			TreePath path = selectGruppe.getTree().getSelectionPath();
			if (path != null)
				addGruppe(path);
		} else if (c.equals("leftArrow1")) { // remove Gruppe
			int index = gruppenTable.getSelectedRow();
			if (index >= 0)
				removeGruppe(index);
		} else if (c.equals("rightArrow2")) { // add Person
			int index = selectPerson.mListBox.getSelectedIndex();
			if (index >= 0)
				addPerson(index);
		} else if (c.equals("leftArrow2")) { // remove Person
			int index = personenTable.getSelectedRow();
			if (index >= 0)
				removePerson(index);
		}

		else
			fireActionEvent(e);
	}
}
