package client.admin.gruppe; //
///////////////////////////////

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import data.*;
import basisklassen.*;
import client.utility.darstellung.*;
import client.utility.darstellung.cell.*;
import client.utility.ListenerForActions;
import client.gui.CharacterTextField;

////////////////////////////////////////////////////////////////////////////////////////////////////
// EditGruppe // EditGruppe // EditGruppe // EditGruppe // EditGruppe // EditGruppe // EditGruppe //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class EditGruppe extends ListenerForActions implements ChangeListener,
		Shared {
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	// Dialog
	private JDialog dialog;

	// Pane
	private JTabbedPane tabbedPane;

	// Tabs
	private GroupData groupData;

	private GroupContent groupContent;

	// Gruppe
	private Gruppe gruppe;

	// EditFlag
	private boolean edit;

	// KuerzelOld
	private String kuerzelOld;

	public EditGruppe(JFrame f, TreePath p, Gruppe g) {
		parentFrame = f;
		gruppe = g;
		edit = gruppe.getKuerzel() != null;

		groupData = new GroupData(p);
		groupContent = new GroupContent();

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Allgemein", groupData.getGUI());
		tabbedPane.addTab("Inhalt", groupContent.getGUI());
		tabbedPane.addChangeListener(this);

		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		gui.setLayout(new BorderLayout());
		gui.add("Center", tabbedPane);
		gui.add("South", buttons);
	}

	void start() {
		if (edit) {
			dialog = new JDialog(parentFrame, "Gruppe bearbeiten", true);
		} else {
			dialog = new JDialog(parentFrame, "Gruppe neu anlegen", true);
		}

		dialog.setSize(250, 350);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add("Center", gui);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - 250) / 2,
				(screenSize.height - 350) / 2);
		dialog.show();
	}

	void close() {
		dialog.dispose();
	}

	TreePath getParentPath() {
		return groupData.selectGruppe.getTree().getSelectionPath();
	}

	TreePath getParentPathOld() {
		return groupData.pathOld;
	}

	boolean checkInput(String m) {
		String msg = "";
		int count = 0, len = 0;
		char c = 34;

		if (m != null) {
			msg = m;
			count = 1;
			len = msg.length();
		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// Checks // Checks // Checks // Checks // Checks // Checks // Checks //
		// Checks // Checks //
		// //////////////////////////////////////////////////////////////////////////////////////////

		TreePath path = getParentPath();

		// GroupData
		if (gruppe.getName().length() == 0) {
			String text = "Sie haben keinen Namen fuer die Gruppe vergeben!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}
		if (gruppe.getKuerzel().length() == 0) {
			String text = "Sie haben kein Kuerzel fuer die Gruppe vergeben!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (edit) {
			// Zyklus?
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			Gruppe grp = (Gruppe) node.getUserObject();

			if (!kuerzelOld.equals(gruppe.getKuerzel()))
				gruppe.setKuerzel(kuerzelOld);

			if (isZyklisch(gruppe, grp)) {
				String text = "Achtung Zyklus: " + c + grp.getName() + c
						+ " in " + c + gruppe.getName() + c + " enthalten!\n";
				len = Math.max(len, text.length());
				msg += text;
				count++;
			}

			gruppe.setKuerzel(groupData.kuerzelNeu.getText().toUpperCase());
		}
		if (path == null) {
			String text = "Bitte ordnen Sie die neue Gruppe einer anderen Gruppe zu!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;

		} else {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			Gruppe grp = (Gruppe) node.getUserObject();

			if (!(Data.user.getKuerzel().equals("Admin")) && grp.getAdminFlag()
					&& grp.getName().compareTo("Eigene Gruppen") != 0) {
				String text = "Sie haben keine Berechtigung eine Gruppe im Folder "
						+ c + grp.getName() + c + " anzulegen!\n";
				len = Math.max(len, text.length());
				msg += text;
				count++;
			}
		}

		if (count > 0) {
			msg = msg.substring(0, msg.length() - 1);

			JTextArea area = new JTextArea(msg);
			area.setBackground(SystemColor.control);
			area.setEditable(false);
			area.setColumns(len);
			area.setRows(count);

			JOptionPane optionPane = new JOptionPane(area,
					JOptionPane.WARNING_MESSAGE);
			JDialog dialogPane = optionPane.createDialog(parentFrame,
					"Fehlermeldungen");

			dialogPane.show();
			dialogPane.dispose();

			return false;
		}
		return true;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ChangeListener // ChangeListener // ChangeListener // ChangeListener //
	// ChangeListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void stateChanged(ChangeEvent e) {
		if (tabbedPane.getSelectedIndex() == 0) {
			dialog.setSize(250, 350);
			tabbedPane.setTitleAt(1, "Inhalt");

		} else {
			String name = groupData.nameNeu.getText();
			if (name.length() > 0) {
				if (groupData.selectGruppe.getTree().getSelectionPath() != null) {
					dialog.setSize(480, 500);
					tabbedPane.setTitleAt(1, "Inhalt der Gruppe: " + name);

				} else { // Keine Gruppenzuordnung
					String text = "Bitte treffen Sie zuerst die Gruppenzuordnung!";

					JTextArea area = new JTextArea(text);
					area.setBackground(SystemColor.control);
					area.setEditable(false);
					area.setColumns(text.length());
					area.setRows(1);

					JOptionPane optionPane = new JOptionPane(area,
							JOptionPane.WARNING_MESSAGE);
					JDialog dialogPane = optionPane.createDialog(parentFrame,
							"Fehlermeldungen");

					dialogPane.show();
					dialogPane.dispose();

					tabbedPane.setSelectedIndex(0);
				}
			} else {
				// Kein Name
				String text = "Bitte vergeben Sie zuerst einen Gruppennamen!";

				JTextArea area = new JTextArea(text);
				area.setBackground(SystemColor.control);
				area.setEditable(false);
				area.setColumns(text.length());
				area.setRows(1);

				JOptionPane optionPane = new JOptionPane(area,
						JOptionPane.WARNING_MESSAGE);
				JDialog dialogPane = optionPane.createDialog(parentFrame,
						"Fehlermeldungen");

				dialogPane.show();
				dialogPane.dispose();

				tabbedPane.setSelectedIndex(0);
			}
		}
		dialog.validate();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			// Gruppenname
			gruppe.setName(groupData.nameNeu.getText());

			// Gruppenkuerzel
			kuerzelOld = gruppe.getKuerzel();
			gruppe.setKuerzel(groupData.kuerzelNeu.getText().toUpperCase());
		}

		super.actionPerformed(e);
	}

	// isZyklisch // isZyklisch // isZyklisch // isZyklisch // isZyklisch //
	// isZyklisch //
	boolean isZyklisch(Gruppe parent, Gruppe child) {
		Gruppe gruppe;

		if (!parent.getKuerzel().equals(child.getKuerzel())) {
			Enumeration e = parent.getGruppen().elements();

			while (e.hasMoreElements()) {
				gruppe = (Gruppe) e.nextElement();

				if (gruppe == child) {
					return true;
				} else {
					isZyklisch(child, gruppe);
				}
			}
			return false;

		} else
			return true;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// GroupData // GroupData // GroupData // GroupData // GroupData //
	// GroupData // GroupData //
	// ///////////////////////////////////////////////////////////////////////////////////////////
	class GroupData {
		// graphical Representation
		private JPanel gui = new JPanel();

		// Controls
		private JTextField nameNeu;

		private CharacterTextField kuerzelNeu;

		// Liste zur Gruppenauswahl
		private SelectTreeEntry selectGruppe = new SelectTreeEntry();

		// Path
		private TreePath pathOld;

		GroupData(TreePath p) {
			pathOld = p;
			create();

			nameNeu.setText(gruppe.getName());
			kuerzelNeu.setText(gruppe.getKuerzel());

			if (pathOld != null) {
				selectGruppe.getTree().setSelectionPath(pathOld);
				selectGruppe.getTree().expandPath(pathOld);
				selectGruppe.getTree().scrollPathToVisible(pathOld);
			}
		}

		void create() {
			gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
			gui.setBorder(new EmptyBorder(10, 10, 10, 10));

			JPanel enterName = new JPanel();
			enterName.setLayout(new BoxLayout(enterName, BoxLayout.X_AXIS));

			JLabel l1 = new JLabel("Gruppenname:");
			l1.setPreferredSize(new Dimension(90, 20));
			l1.setDisplayedMnemonic('g');

			nameNeu = new JTextField();
			nameNeu.setFocusAccelerator('g');

			enterName.add(l1);
			enterName.add(nameNeu);

			JPanel enterKuerzel = new JPanel();
			enterKuerzel
					.setLayout(new BoxLayout(enterKuerzel, BoxLayout.X_AXIS));

			JLabel l2 = new JLabel("Gruppenkuerzel:");
			l2.setPreferredSize(new Dimension(90, 20));
			l2.setDisplayedMnemonic('r');

			kuerzelNeu = new CharacterTextField();
			kuerzelNeu.setFocusAccelerator('r');

			enterKuerzel.add(l2);
			enterKuerzel.add(kuerzelNeu);

			selectGruppe.getGUI().setBorder(
					new CompoundBorder(new TitledBorder("zugeordnet zu"),
							new EmptyBorder(0, 5, 5, 5)));
			gui.add(enterName);
			gui.add(Box.createVerticalStrut(10));
			gui.add(enterKuerzel);
			gui.add(Box.createVerticalStrut(10));
			gui.add(selectGruppe.getGUI());
		}

		JPanel getGUI() {
			return gui;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// GroupContent // GroupContent // GroupContent // GroupContent //
	// GroupContent // GroupContent //
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	class GroupContent extends BasicSelection {
		GroupContent() {
			super(215);
			adjustTable();

			fill();
		}

		void adjustTable() {
			GruppenCellRenderer groupRend = new GruppenCellRenderer(
					Gruppe.class);
			GruppenCellRenderer persRend = new GruppenCellRenderer(Person.class);

			// Gruppen // Gruppen // Gruppen // Gruppen // Gruppen // Gruppen //
			// Gruppen //
			gruppenTableModel.setColumnIdentifiers(new String[] { "Kuerzel",
					"Name" });
			TableColumn col = gruppenTable.getColumn("Kuerzel");
			col.setCellRenderer(groupRend);
			col.setMaxWidth(40);

			col = gruppenTable.getColumn("Name");
			col.setCellRenderer(groupRend);
			col.setMaxWidth(170);

			// Person // Person // Person // Person // Person // Person //
			// Person // Person //
			personenTableModel.setColumnIdentifiers(new String[] { "Kuerzel",
					"Name" });
			col = personenTable.getColumn("Kuerzel");
			col.setCellRenderer(persRend);
			col.setMaxWidth(40);

			col = personenTable.getColumn("Name");
			col.setCellRenderer(persRend);
			col.setMaxWidth(170);

			// No reordering
			gruppenTable.getTableHeader().setReorderingAllowed(false);
			personenTable.getTableHeader().setReorderingAllowed(false);
		}

		void fill() {
			int i;

			// Gruppen // Gruppen // Gruppen // Gruppen // Gruppen // Gruppen //
			// Gruppen //
			String name;
			Gruppe child, g;

			Enumeration e = gruppe.getGruppen().elements();

			int count = 0;
			while (e.hasMoreElements()) {
				child = (Gruppe) e.nextElement();
				name = child.getName();

				// Sortiert einfuegen
				for (i = 0; i < count; i++) {
					g = (Gruppe) gruppenTableModel.getValueAt(i, 1);
					if (name.compareTo(g.getName()) < 0)
						break;
				}

				gruppenTableModel.insertRow(i, new Object[] { child, child });
				count++;
			}

			// Personen // Personen // Personen // Personen // Personen //
			// Personen // Personen //
			Person person, p;
			e = gruppe.getPersonen().elements();

			count = 0;
			while (e.hasMoreElements()) {
				person = (Person) e.nextElement();
				name = person.getNameLang();

				// Sortiert einfuegen
				for (i = 0; i < count; i++) {
					p = (Person) personenTableModel.getValueAt(i, 1);
					if (name.compareTo(p.getNameLang()) < 0)
						break;
				}

				personenTableModel
						.insertRow(i, new Object[] { person, person });
				count++;
			}
		}

		void addGruppe(TreePath path) {
			Gruppe g;

			if (!edit) {
				TreePath p = groupData.selectGruppe.getTree()
						.getSelectionPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) p
						.getLastPathComponent();
				g = (Gruppe) node.getUserObject();

			} else
				g = gruppe;

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			Gruppe grp = (Gruppe) node.getUserObject();

			if (!isZyklisch(grp, g)) {
				String kuerzel = grp.getKuerzel();
				String name = grp.getName();

				int count = gruppenTableModel.getRowCount();
				int i;

				// Sortiert einfuegen
				for (i = 0; i < count; i++) {
					g = (Gruppe) gruppenTableModel.getValueAt(i, 0);

					if (kuerzel.compareTo(g.getKuerzel()) != 0) {
						if (name.compareTo(g.getName()) < 0)
							break;

					} else {
						i = -1;
						break;
					}
				}

				if (i >= 0) {
					gruppenTableModel.insertRow(i, new Object[] { grp, grp });
					gruppe.addGruppe(grp);
				}

			} else {
				char c = 34;
				String text = "Achtung Zyklus: " + c + gruppe.getName() + c
						+ " in " + c + g.getName() + c + " enthalten!";
				JTextArea area = new JTextArea(text);
				area.setBackground(SystemColor.control);
				area.setEditable(false);
				area.setColumns(text.length());
				area.setRows(1);

				JOptionPane optionPane = new JOptionPane(area,
						JOptionPane.WARNING_MESSAGE);
				JDialog dialogPane = optionPane.createDialog(parentFrame,
						"Fehlermeldungen");

				dialogPane.show();
				dialogPane.dispose();
			}
		}

		void removeGruppe(int rowIndex) {
			Gruppe grp = (Gruppe) gruppenTableModel.getValueAt(rowIndex, 0);
			gruppe.removeGruppe(grp);

			gruppenTableModel.removeRow(rowIndex);
		}

		void addPerson(int index) {
			Person person = (Person) selectPerson.mModel.get(index);
			String kuerzel = person.getKuerzel();
			String name = person.getNameLang();
			Person p;

			int count = personenTableModel.getRowCount();
			int i;

			// Sortiert einfuegen
			for (i = 0; i < count; i++) {
				p = (Person) personenTableModel.getValueAt(i, 0);

				if (kuerzel.compareTo(p.getKuerzel()) != 0) {
					if (name.compareTo(p.getNameLang()) < 0)
						break;

				} else {
					i = -1;
					break;
				}
			}

			if (i >= 0) {
				gruppe.addPerson(person);
				personenTableModel
						.insertRow(i, new Object[] { person, person });
			}
		}

		void removePerson(int rowIndex) {
			Person person = (Person) personenTableModel.getValueAt(rowIndex, 0);
			gruppe.removePerson(person);

			personenTableModel.removeRow(rowIndex);
		}

		// ActionListener // ActionListener // ActionListener // ActionListener
		// // ActionListener //
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("rightArrow1")) { // add Person
				TreePath path = selectGruppe.getTree().getSelectionPath();
				if (path != null)
					addGruppe(path);
			} else if (e.getActionCommand().equals("leftArrow1")) { // remove
				// Person
				int index = gruppenTable.getSelectedRow();
				if (index >= 0)
					removeGruppe(index);
			} else if (e.getActionCommand().equals("rightArrow2")) { // add
				// Gruppe
				int index = selectPerson.mListBox.getSelectedIndex();
				if (index >= 0)
					addPerson(index);

			} else if (e.getActionCommand().equals("leftArrow2")) { // remove
				// Gruppe
				int index = personenTable.getSelectedRow();
				if (index >= 0)
					removePerson(index);

			} else {
				super.actionPerformed(e);
			}
		}
	}
}
