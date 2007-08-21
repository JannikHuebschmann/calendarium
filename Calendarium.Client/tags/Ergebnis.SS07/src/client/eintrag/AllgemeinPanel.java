package client.eintrag; //
//////////////////////////

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;

import basisklassen.EintragsTyp;
import basisklassen.Termin;
import basisklassen.ToDo;
import client.utility.ListenerForActions;
import client.utility.darstellung.ButtonPanel;
import client.utility.darstellung.cell.TypCellEditor;
import client.utility.darstellung.cell.TypListCellRenderer;
import data.Data;

////////////////////////////////////////////////////////////////////////////////////////////
// AllgemeinPanel // AllgemeinPanel // AllgemeinPanel // AllgemeinPanel // AllgemeinPanel //
////////////////////////////////////////////////////////////////////////////////////////////

class AllgemeinPanel extends ListenerForActions implements ItemListener {
	// graphical Representation
	JPanel gui = new JPanel();

	// ParentFrame
	JFrame parentFrame;

	// Beginn- , Endedatum
	DatePanel[] datePanel = new DatePanel[2];

	// Eintragstyp
	JComboBox eintragsTyp;

	// Notifikationen
	NfktPanel[] nfktPanel = new NfktPanel[3];

	// weitere Attribute
	JTextField kurzText, ort, hyperlink;

	JTextArea langText;

	JCheckBox serienFlag;

	// Buttons
	ButtonPanel buttons;

	// Events
	private EventListenerList mItemListenerList;

	AllgemeinPanel(JFrame f) {
		parentFrame = f;
		mItemListenerList = new EventListenerList();

		gui.setLayout(new BorderLayout());
	}

	JPanel getGUI() {
		return gui;
	}

	// Beschreibung // Beschreibung // Beschreibung // Beschreibung //
	// Beschreibung //
	JPanel createBeschreibung() {
		JPanel beschreibung = new JPanel();
		beschreibung.setLayout(new BoxLayout(beschreibung, BoxLayout.Y_AXIS));
		beschreibung.setBorder(new TitledBorder("Beschreibung"));

		JPanel zeile1 = new JPanel();
		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));
		zeile1.setBorder(new EmptyBorder(0, 5, 5, 5));

		JPanel zeile2 = new JPanel();
		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));
		zeile2.setBorder(new EmptyBorder(0, 5, 5, 5));

		JPanel zeile3 = new JPanel();
		zeile3.setLayout(new BoxLayout(zeile3, BoxLayout.X_AXIS));
		zeile3.setBorder(new EmptyBorder(0, 5, 5, 5));

		JPanel zeile4 = new JPanel();
		zeile4.setLayout(new BoxLayout(zeile4, BoxLayout.X_AXIS));
		zeile4.setBorder(new EmptyBorder(0, 5, 5, 5));

		kurzText = new JTextField();
		kurzText.setPreferredSize(new Dimension(192, 20));

		ort = new JTextField();
		ort.setFocusAccelerator('o');

		hyperlink = new JTextField();
		hyperlink.setFocusAccelerator('h');

		JLabel l1 = new JLabel("Typ: ");

		JLabel l2 = new JLabel("Ort:");
		l2.setPreferredSize(new Dimension(60, 20));
		l2.setDisplayedMnemonic('o');

		JLabel l3 = new JLabel("Hyperlink:");
		l3.setPreferredSize(new Dimension(60, 20));
		l3.setDisplayedMnemonic('h');

		// Eintragstypen // Eintragstypen // Eintragstypen // Eintragstypen //
		// Eintragstypen //
		eintragsTyp = new JComboBox();
		eintragsTyp.setPreferredSize(new Dimension(124, 24));

		Enumeration e = Data.typen.getUserTypen().elements();
		while (e.hasMoreElements()) {
			EintragsTyp typ = (EintragsTyp) e.nextElement();
			eintragsTyp.addItem(typ);
		}

		eintragsTyp.setRenderer(new TypListCellRenderer());
		eintragsTyp.setEditor(new TypCellEditor());

		// to avoid the ClassCastException:
		// eintragsTyp.setEditable(true);
		// changed by daniela esberger, 2005/03/31

		eintragsTyp.addItemListener(this);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(1, 1));
		textPanel.setPreferredSize(new Dimension(500, 120));
		langText = new JTextArea();
		langText.setBorder(new BevelBorder(BevelBorder.LOWERED));
		textPanel.add(langText);

		zeile1.add(kurzText);
		zeile1.add(Box.createRigidArea(new Dimension(20, 0)));
		zeile1.add(l1);
		zeile1.add(Box.createRigidArea(new Dimension(5, 0)));
		zeile1.add(eintragsTyp);

		zeile2.add(textPanel);

		zeile3.add(l2);
		zeile3.add(ort);

		zeile4.add(l3);
		zeile4.add(hyperlink);

		beschreibung.add(zeile1);
		beschreibung.add(zeile2);
		beschreibung.add(zeile3);
		beschreibung.add(zeile4);

		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add("Center", beschreibung);

		return pane;
	}

	// Benachrichtigung // Benachrichtigung // Benachrichtigung //
	// Benachrichtigung //
	JPanel createBenachrichtigung() {
		JPanel benachrichtigung = new JPanel();
		benachrichtigung.setLayout(new GridLayout(3, 1, 10, 0));
		benachrichtigung.setBorder(new TitledBorder("Benachrichtigung"));

		benachrichtigung.add((nfktPanel[0] = new NfktPanel(1)).getGUI());
		benachrichtigung.add((nfktPanel[1] = new NfktPanel(2)).getGUI());
		benachrichtigung.add((nfktPanel[2] = new NfktPanel(3)).getGUI());

		return benachrichtigung;
	}

	boolean[] getNfkts() {
		return new boolean[] { nfktPanel[0].isActivated(),
				nfktPanel[1].isActivated(), nfktPanel[2].isActivated() };
	}

	int getNfktCount() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (nfktPanel[i].isActivated())
				count++;
		}

		return count;
	}

	void setSerienFlag(boolean b) {
		serienFlag.setSelected(b);
		serienFlag.setEnabled(b); // changed by daniela esberger - 14. juni
		// 2005
	}

	boolean getSerienFlag() {
		return serienFlag.isSelected();
	}

	Rectangle getRectOK() {
		return buttons.getScreenLocation(0);
	}

	Rectangle getRectAbbr() {
		return buttons.getScreenLocation(1);
	}

	EintragsTyp getEintragsTyp() {
		return (EintragsTyp) eintragsTyp.getSelectedItem();
	}

	void fill(Termin t) {
	};

	void fill(ToDo t) {
	};

	void getAttribute() {
	};

	void setBeginn() {
	};

	void setErinnernAb() {
	};

	// ItemListener // ItemListener // ItemListener // ItemListener //
	// ItemListener //
	public void addItemListener(ItemListener listener) {
		mItemListenerList.add(ItemListener.class, listener);
	}

	public void removeItemListener(ItemListener listener) {
		mItemListenerList.remove(ItemListener.class, listener);
	}

	protected void fireItemEvent(ItemEvent e) {
		Object[] listeners = mItemListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ItemListener.class) {
				((ItemListener) listeners[i + 1]).itemStateChanged(e);
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		fireItemEvent(e);
	}
}
