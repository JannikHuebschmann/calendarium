package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import basisklassen.Person;
import client.utility.darstellung.SelectListEntry;
import client.utility.darstellung.cell.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// KalenderAuswahl // KalenderAuswahl // KalenderAuswahl // KalenderAuswahl // KalenderAuswahl //
/////////////////////////////////////////////////////////////////////////////////////////////////

class KalenderAuswahl implements ActionListener, ListSelectionListener,
		ListDataListener, ItemListener {
	private boolean free[] = { false, true, true, true, true, true, true, true };

	// graphical Representation
	private JPanel gui = new JPanel();

	// JDialog
	private JDialog selectPerson;

	// JList
	private DefaultListModel mModel;

	private JList mListBox;

	private JButton[] btn = new JButton[2];

	private Ansicht sicht;

	// registrierte Listener
	private EventListenerList mListDataListenerList;

	KalenderAuswahl(Ansicht s, boolean buttonsVertical) {
		sicht = s;
		mListDataListenerList = new EventListenerList();

		gui.setLayout(new BorderLayout(10, 5));
		gui.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED),
				new EmptyBorder(10, 10, 10, 10)));

		create(buttonsVertical);
	}

	void create(boolean buttonsVertical) {
		Dimension size;

		// Label
		JLabel label = new JLabel("Einträge von:");
		label.setHorizontalAlignment(JLabel.LEFT);

		// Buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		btn[0] = new JButton("hinzufuegen");
		size = btn[0].getMaximumSize();
		size.width = 10000;
		btn[0].setMaximumSize(size);
		btn[0].setMnemonic('h');
		btn[0].setActionCommand("add");
		btn[0].addActionListener(this);

		btn[1] = new JButton("löschen");
		btn[1].setMaximumSize(size);
		btn[1].setMnemonic('l');
		btn[1].setEnabled(false);
		btn[1].setActionCommand("delete");
		btn[1].addActionListener(this);

		buttons.add(btn[0]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[1]);
		buttons.add(Box.createVerticalGlue());

		gui.add("North", label);
		gui.add("Center", createList());

		if (buttonsVertical) {
			gui.add("South", buttons);
		} else {
			gui.add("East", buttons);
		}
	}

	JPanel getGUI() {
		return gui;
	}

	JScrollPane createList() {
		mModel = new DefaultListModel();

		Enumeration enumer = sicht.openCal.getEnumeration();
		while (enumer.hasMoreElements()) {
			mModel.addElement(enumer.nextElement());
		}

		mListBox = new JList(mModel);
		mListBox.setCellRenderer(new PersonListCellRenderer(sicht.openCal));
		mListBox.setBackground(Color.white);
		mListBox.setVisibleRowCount(2);
		mListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Listener
		mListBox.getModel().addListDataListener(this);
		mListBox.getSelectionModel().addListSelectionListener(this);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane(mListBox);

		return scrollPane;
	}

	public void addNotify() {
		mListBox.validate();
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("add")) {
			selectPerson = new JDialog(sicht.parentFrame, "Personenauswahl",
					true);
			selectPerson.setSize(250, 250);
			selectPerson.getContentPane().setLayout(new BorderLayout());

			SelectListEntry select = new SelectListEntry(true);
			select.addItemListener(this);

			selectPerson.getContentPane().add("Center", select.getGUI());

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			selectPerson.setLocation((screenSize.width - 250) / 2,
					(screenSize.height - 250) / 2);
			selectPerson.show();
		} else if (e.getActionCommand().equals("delete")) {
			Person person = (Person) mModel.get(mListBox.getSelectedIndex());
			free[sicht.openCal.getColorIndex(person)] = true;

			sicht.openCal.remove(person);
			mModel.removeElementAt(mListBox.getSelectedIndex());
			if (mModel.getSize() == 1)
				btn[1].setEnabled(false);
		}
	}

	// ItemSelectionListener // ItemSelectionListener // ItemSelectionListener
	// //
	public void valueChanged(ListSelectionEvent e) {
		if (mModel.getSize() > 1)
			btn[1].setEnabled(true);
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void contentsChanged(ListDataEvent e) {
	}

	public void intervalAdded(ListDataEvent e) {
		fireListDataEvent(new ListDataEvent(this, e.getType(), e.getIndex0(), e
				.getIndex1()));
	}

	public void intervalRemoved(ListDataEvent e) {
		fireListDataEvent(new ListDataEvent(this, e.getType(), e.getIndex0(), e
				.getIndex1()));
	}

	public void addListDataListener(ListDataListener listener) {
		mListDataListenerList.add(ListDataListener.class, listener);
	}

	public void removeListDataListener(ListDataListener listener) {
		mListDataListenerList.remove(ListDataListener.class, listener);
	}

	protected void fireListDataEvent(ListDataEvent e) {
		Object[] listeners = mListDataListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ListDataListener.class) {
				if (e.getType() == ListDataEvent.INTERVAL_ADDED) {
					((ListDataListener) listeners[i + 1]).intervalAdded(e);
				} else {
					((ListDataListener) listeners[i + 1]).intervalRemoved(e);
				}
			}
		}
	}

	// ItemListener // ItemListener // ItemListener // ItemListener //
	// ItemListener //
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != null) {
			Person person = (Person) e.getItem();

			Enumeration enumer = mModel.elements();
			int count = 0;

			while (enumer.hasMoreElements()) {
				if (person == (Person) enumer.nextElement())
					break;
				count++;
			}

			if (count == mModel.getSize()) {
				int i;
				for (i = 1; i < 8; i++) {
					if (free[i]) {
						sicht.openCal.add(person, i);

						mModel.addElement(person);
						mListBox.ensureIndexIsVisible(mModel.getSize() - 1);
						mListBox.validate();

						free[i] = false;
						if (i == 7)
							btn[0].setEnabled(false);

						break;
					}
				}
			}
		}

		selectPerson.dispose();
	}
}
