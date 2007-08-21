package client.admin.feiertag; //
/////////////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.*;

import data.Data;
import basisklassen.*;
import client.utility.ListenerForActions;
import client.utility.darstellung.cell.*;

///////////////////////////////////////////////////////////////////////////////////////
// EditFeiertage // EditFeiertage // EditFeiertage // EditFeiertage // EditFeiertage //
///////////////////////////////////////////////////////////////////////////////////////

public class EditFeiertage extends ListenerForActions implements
		ListSelectionListener {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Feiertage verwalten",
			true, true, false, true);

	// JList
	private DefaultListModel mModel;

	private JList mListBox;

	// Buttons
	private JButton btn[] = new JButton[3];

	public EditFeiertage() {
		gui.setSize(400, 250);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		create();
	}

	void create() {
		Dimension size;

		JPanel editPane = new JPanel();
		editPane.setLayout(new BorderLayout(10, 0));
		editPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		btn[0] = new JButton("Feiertag hinzufuegen");
		size = btn[0].getMaximumSize();
		size.width = 10000;
		btn[0].setMaximumSize(size);
		btn[0].setMnemonic('h');
		btn[0].setActionCommand("new");
		btn[0].addActionListener(this);

		btn[1] = new JButton("Feiertag löschen");
		btn[1].setMaximumSize(size);
		btn[1].setEnabled(false);
		btn[1].setMnemonic('l');
		btn[1].setActionCommand("delete");
		btn[1].addActionListener(this);

		btn[2] = new JButton("Feiertag bearbeiten");
		btn[2].setMaximumSize(size);
		btn[2].setEnabled(false);
		btn[2].setMnemonic('b');
		btn[2].setActionCommand("edit");
		btn[2].addActionListener(this);

		buttons.add(Box.createVerticalStrut(2));
		buttons.add(btn[0]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[1]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[2]);
		buttons.add(Box.createVerticalGlue());

		editPane.add("Center", createList());
		editPane.add("East", buttons);

		gui.getContentPane().add("Center", editPane);
	}

	JInternalFrame getGUI() {
		return gui;
	}

	JScrollPane createList() {
		mListBox = new JList();
		mListBox.setCellRenderer(new FeiertagListCellRenderer());
		mListBox.setVisibleRowCount(2);
		mListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mListBox.addListSelectionListener(this);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane(mListBox);

		// Model zuweisen
		createModel();

		return scrollPane;
	}

	void createModel() {
		mModel = new DefaultListModel();
		Enumeration e = Data.feiertage.getFeiertage().elements();

		while (e.hasMoreElements()) {
			mModel.addElement((Feiertag) e.nextElement());
		}

		mListBox.setModel(mModel);
		mListBox.validate();
		mListBox.repaint();
	}

	Feiertag getSelectedFeiertag() {
		if (mListBox.getSelectedIndex() >= 0) {
			return (Feiertag) mListBox.getSelectedValue();

		} else
			return null;
	}

	void update() {
		createModel();
	}

	// ListSelectionListener // ListSelectionListener // ListSelectionListener
	// // ListSelectionListener //
	public void valueChanged(ListSelectionEvent e) {
		if (mListBox.getSelectedIndex() >= 0) {
			btn[1].setEnabled(true);
			btn[2].setEnabled(true);

		} else {
			btn[1].setEnabled(false);
			btn[2].setEnabled(false);
		}
	}
}
