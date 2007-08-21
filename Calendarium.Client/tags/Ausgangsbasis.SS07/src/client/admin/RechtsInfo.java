package client.admin; //
////////////////////////

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.*;

import data.*;
import basisklassen.*;
import client.utility.darstellung.CustomTable;
import client.utility.darstellung.cell.*;

////////////////////////////////////////////////////////////////////////////////////////////////////
// RechtsInfo // RechtsInfo // RechtsInfo // RechtsInfo // RechtsInfo // RechtsInfo // RechtsInfo //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class RechtsInfo implements Shared {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Erhaltene Rechte", true,
			true, false, true);

	// TableModel
	private DefaultTableModel mTableModel;

	public RechtsInfo() {
		gui.setSize(500, 300);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		create();
		fill();
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.setBorder(new EmptyBorder(10, 10, 10, 10));

		GruppenCellRenderer persRend = new GruppenCellRenderer(Person.class);

		mTableModel = new DefaultTableModel();
		CustomTable mTable = new CustomTable(mTableModel);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.getTableHeader().setReorderingAllowed(false);

		mTableModel.setColumnIdentifiers(new String[] { "Kzl", "Name",
				"Termintyp", "Recht" });

		TableColumn col = mTable.getColumn("Kzl");
		col.setCellRenderer(persRend);
		col.setMinWidth(26);

		col = mTable.getColumn("Name");
		col.setCellRenderer(persRend);
		col.setMinWidth(105);

		col = mTable.getColumn("Termintyp");
		col.setCellRenderer(new TypTableCellRenderer());
		col.setMinWidth(105);

		col = mTable.getColumn("Recht");
		col.setMinWidth(105);

		JScrollPane scrollPane = new JScrollPane(mTable);
		scrollPane.setBackground(SystemColor.window);

		pane.add("Center", scrollPane);
		gui.getContentPane().add("Center", pane);
	}

	public JInternalFrame getGUI() {
		return gui;
	}

	void fill() {
		Hashtable granted = Data.rechte.getRechte();

		for (int i = 0, count = mTableModel.getRowCount(); i < count; i++) {
			mTableModel.removeRow(0);
		}

		if (granted != null) {
			Enumeration enumer = granted.keys();

			while (enumer.hasMoreElements()) {
				Person person = (Person) enumer.nextElement();
				Vector rechte = (Vector) granted.get(person);

				String name = person.getNameLang();
				int i, count;

				// Sortiert einfuegen
				for (i = 0, count = mTableModel.getRowCount(); i < count; i++) {
					Person p = (Person) mTableModel.getValueAt(i, 0);
					if (name.compareTo(p.getNameLang()) < 0)
						break;
				}

				Enumeration e = rechte.elements();
				while (e.hasMoreElements()) {
					Recht recht = (Recht) e.nextElement();
					EintragsTyp userTyp = Data.typen.getUserTyp(recht
							.getEintragsTyp());

					mTableModel.insertRow(i, new Object[] { person, person,
							userTyp, RECHTE[recht.getRechtsIndex()] });
				}
			}
		}
	}
}
