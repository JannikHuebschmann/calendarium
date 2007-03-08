package client.utility.darstellung; //
//////////////////////////////////////

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import data.Data;
import client.admin.recht.*;

////////////////////////////////////////////////////////////////////////////////////////////
// BasicSelection // BasicSelection // BasicSelection // BasicSelection // BasicSelection //
////////////////////////////////////////////////////////////////////////////////////////////

public class BasicSelection implements ActionListener {
	// graphical Representation
	private JPanel gui = new JPanel();

	public DefaultTableModel gruppenTableModel;

	public DefaultTableModel personenTableModel;

	public CustomTable gruppenTable;

	public CustomTable personenTable;

	private int tableWidth;

	private String[] title;

	// Liste zur Personenauswahl
	public SelectListEntry selectPerson;

	// Liste zur Gruppenauswahl
	public SelectTreeEntry selectGruppe;

	public BasicSelection(int t) {
		tableWidth = t;
		title = null;
		create();
	}

	public BasicSelection(int t, String[] s) {
		tableWidth = t;
		title = s;
		create();
	}

	private void create() {
		gui.setLayout(new GridLayout(2, 1, 0, 5));
		gui.setBorder(new EmptyBorder(5, 10, 5, 10));

		gui.add(createPersonenPanel());
		gui.add(createGruppenPanel());
	}

	public JPanel getGUI() {
		return gui;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// GruppenPanel // GruppenPanel // GruppenPanel // GruppenPanel //
	// GruppenPanel // GruppenPanel //
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	JPanel createGruppenPanel() {
		JPanel gruppen = new JPanel();
		gruppen.setBorder(new CompoundBorder(new TitledBorder("Gruppen"),
				new EmptyBorder(0, 5, 5, 5)));

		// Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile //
		// Pfeile //
		Pfeile pfeile = new Pfeile(1);
		pfeile.addActionListener(this);

		// Table // Table // Table // Table // Table // Table // Table // Table
		// // Table //
		gruppenTableModel = new DefaultTableModel();
		gruppenTable = new CustomTable(gruppenTableModel);
		gruppenTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(gruppenTable);
		scrollPane.setBackground(SystemColor.window);

		JPanel tablePane = new JPanel();
		tablePane.setLayout(new BorderLayout());
		tablePane.setPreferredSize(new Dimension(tableWidth, 30));

		if (title == null) {
			tablePane.setBorder(new EmptyBorder(40, 0, 0, 0));
			tablePane.add("Center", scrollPane);
		} else {
			tablePane.add("North", createTitlePanel(0));
			tablePane.add("Center", scrollPane);
		}

		// Layout // Layout // Layout // Layout // Layout // Layout // Layout //
		// Layout //
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;

		gruppen.setLayout(gridbag);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;

		selectGruppe = new SelectTreeEntry();
		gridbag.setConstraints(selectGruppe.getGUI(), c);
		gruppen.add(selectGruppe.getGUI());

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.0;

		gridbag.setConstraints(pfeile, c);
		gruppen.add(pfeile);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.0;

		gridbag.setConstraints(tablePane, c);
		gruppen.add(tablePane);

		return gruppen;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// PersonenPanel // PersonenPanel // PersonenPanel // PersonenPanel //
	// PersonenPanel //
	// /////////////////////////////////////////////////////////////////////////////////////
	JPanel createPersonenPanel() {
		JPanel personen = new JPanel();
		personen.setBorder(new CompoundBorder(new TitledBorder("Personen"),
				new EmptyBorder(0, 5, 5, 5)));

		// Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile //
		// Pfeile //
		Pfeile pfeile = new Pfeile(2);
		pfeile.addActionListener(this);

		// Table // Table // Table // Table // Table // Table // Table // Table
		// // Table //
		personenTableModel = new DefaultTableModel();
		personenTable = new CustomTable(personenTableModel);
		personenTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(personenTable);
		scrollPane.setBackground(SystemColor.window);

		JPanel tablePane = new JPanel();
		tablePane.setLayout(new BorderLayout());
		tablePane.setPreferredSize(new Dimension(tableWidth, 30));

		if (title == null) {
			tablePane.setBorder(new EmptyBorder(40, 0, 0, 0));
			tablePane.add("Center", scrollPane);
		} else {
			tablePane.add("North", createTitlePanel(1));
			tablePane.add("Center", scrollPane);
		}

		// Layout // Layout // Layout // Layout // Layout // Layout // Layout //
		// Layout //
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;

		personen.setLayout(gridbag);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;

		selectPerson = new SelectListEntry(false);
		if (this instanceof EditRechte)
			selectPerson.removePerson(Data.user.getKuerzel());

		gridbag.setConstraints(selectPerson.getGUI(), c);
		personen.add(selectPerson.getGUI());

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.0;

		gridbag.setConstraints(pfeile, c);
		personen.add(pfeile);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.0;

		gridbag.setConstraints(tablePane, c);
		personen.add(tablePane);

		return personen;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// TitlePanel // TitlePanel // TitlePanel // TitlePanel // TitlePanel //
	// TitlePanel //
	// ////////////////////////////////////////////////////////////////////////////////////
	JPanel createTitlePanel(int index) {
		JPanel labels = new JPanel();
		labels.setLayout(new BorderLayout());
		labels.setBorder(new EmptyBorder(15, 0, 0, 0));
		labels.setPreferredSize(new Dimension(tableWidth, 40));

		JLabel l1 = new JLabel(title[index]);
		l1.setHorizontalAlignment(JLabel.CENTER);

		labels.add("North", l1);
		return labels;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
	}
}
