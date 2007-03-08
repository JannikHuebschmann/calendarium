package client.admin.typ; //
////////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.*;

import data.Data;
import basisklassen.*;
import client.utility.ListenerForActions;
import client.utility.darstellung.cell.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// EditTypen // EditTypen // EditTypen // EditTypen // EditTypen // EditTypen // EditTypen //
/////////////////////////////////////////////////////////////////////////////////////////////

public class EditTypen extends ListenerForActions implements
		ListSelectionListener {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Typen verwalten", true,
			true, false, true);

	// JList
	private DefaultListModel mModel;

	private JList mListBox;

	// Buttons
	private JButton btn[] = new JButton[3];

	private JButton button;

	// User
	private Person person = null;

	public EditTypen() {
		gui.setSize(300, 250);// 300,250
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		createI();
	}

	public EditTypen(Person p) {
		person = p;

		gui.setSize(300, 250);// 200,250
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		createII();
	}

	void createI() {
		Dimension size;

		JPanel editPane = new JPanel();
		editPane.setLayout(new BorderLayout(10, 0));
		editPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		btn[0] = new JButton("Typ hinzufuegen");
		size = btn[0].getMaximumSize();
		size.width = 10000;
		btn[0].setMaximumSize(size);
		btn[0].setMnemonic('h');
		btn[0].setActionCommand("new");
		btn[0].addActionListener(this);

		btn[1] = new JButton("Typ löschen");
		btn[1].setMaximumSize(size);
		btn[1].setEnabled(false);
		btn[1].setMnemonic('l');
		btn[1].setActionCommand("delete");
		btn[1].addActionListener(this);

		btn[2] = new JButton("Typ bearbeiten");
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

	void createII() {
		JPanel editPane = new JPanel();
		editPane.setLayout(new BorderLayout(0, 10));
		editPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		button = new JButton("Farbe ändern");
		button.setMnemonic('f');
		button.setActionCommand("changeColor");
		button.addActionListener(this);

		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalGlue());

		editPane.add("Center", createList());
		editPane.add("South", buttonPane);

		gui.getContentPane().add("Center", editPane);
	}

	JInternalFrame getGUI() {
		return gui;
	}

	JScrollPane createList() {
		mListBox = new JList();
		mListBox.setCellRenderer(new TypListCellRenderer());
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
		Enumeration e;
		mModel = new DefaultListModel();

		if (person == null) {
			e = Data.typen.getDefaultTypen().elements();

		} else {
			e = Data.typen.getUserTypen().elements();

		}

		while (e.hasMoreElements()) {
			EintragsTyp typ = (EintragsTyp) e.nextElement();
			mModel.addElement(typ);
		}

		mListBox.setModel(mModel);
		mListBox.validate();

	}

	EintragsTyp getSelectedTyp() {
		if (mListBox.getSelectedIndex() >= 0) {
			return (EintragsTyp) mListBox.getSelectedValue();

		} else
			return null;
	}

	void update(boolean rebuild) {
		if (rebuild) {
			createModel();
		} else {
			mListBox.validate();
			mListBox.repaint();
		}
	}

	// ListSelectionListener // ListSelectionListener // ListSelectionListener
	// // ListSelectionListener //
	public void valueChanged(ListSelectionEvent e) {
		if (person == null) {
			if (mListBox.getSelectedIndex() >= 0) {
				btn[1].setEnabled(true);
				btn[2].setEnabled(true);

			} else {
				btn[1].setEnabled(false);
				btn[2].setEnabled(false);
			}
		} else {
			if (mListBox.getSelectedIndex() >= 0) {
				button.setEnabled(true);
			} else {
				button.setEnabled(false);
			}
		}
	}
}
