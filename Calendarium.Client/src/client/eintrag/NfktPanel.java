package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import basisklassen.Notifikation;
import client.gui.Zahlenfeld;

/////////////////////////////////////////////////////////////////////////////////////////////
// NfktPanel // NfktPanel // NfktPanel // NfktPanel // NfktPanel // NfktPanel // NfktPanel //
/////////////////////////////////////////////////////////////////////////////////////////////

class NfktPanel implements ItemListener, data.Shared {
	// graphical Representation
	private JPanel gui = new JPanel();

	// Controls
	private JRadioButton[] typ = new JRadioButton[4];

	private Zahlenfeld zeit;

	private JComboBox zeitEinheit;

	private JLabel status;

	// NfktNummer
	private int nummer;

	NfktPanel(int nr) {
		nummer = nr;

		gui.setLayout(new BoxLayout(gui, BoxLayout.X_AXIS));
		gui.setBorder(new EmptyBorder(0, 5, 0, 5));

		create();
	}

	void create() {
		ImageIcon statusGif;

		statusGif = client.Client.loadImageIcon("b" + nummer + "0.gif");
		status = new JLabel(statusGif);

		typ[0] = new JRadioButton("keine");
		typ[0].setAlignmentY((float) 0.5);
		typ[0].addItemListener(this);
		typ[1] = new JRadioButton(NFKT_TYP[0]);
		typ[1].setAlignmentY((float) 0.5);
		typ[2] = new JRadioButton(NFKT_TYP[1]);
		typ[2].setAlignmentY((float) 0.5);
		typ[3] = new JRadioButton(NFKT_TYP[2]);
		typ[3].setAlignmentY((float) 0.5);

		ButtonGroup mode = new ButtonGroup();
		mode.add(typ[0]);
		mode.add(typ[1]);
		mode.add(typ[2]);
		mode.add(typ[3]);

		zeit = new Zahlenfeld(2);
		zeitEinheit = new JComboBox();

		zeitEinheit.addItem(ZE_TYP[0]);
		zeitEinheit.addItem(ZE_TYP[1]);
		zeitEinheit.addItem(ZE_TYP[2]);

		gui.add(status);
		gui.add(Box.createRigidArea(new Dimension(10, 0)));
		gui.add(typ[0]);
		gui.add(typ[1]);
		gui.add(typ[2]);
		gui.add(typ[3]);
		gui.add(Box.createRigidArea(new Dimension(10, 0)));
		gui.add(zeit);
		gui.add(Box.createRigidArea(new Dimension(5, 0)));
		gui.add(zeitEinheit);
		gui.add(Box.createRigidArea(new Dimension(5, 0)));
		gui.add(new JLabel("vorher"));

		zeit.setEnabled(false);

		zeitEinheit.setEnabled(false);
		zeitEinheit.setSelectedIndex(0);

		typ[0].setSelected(true);
	}

	JPanel getGUI() {
		return gui;
	}

	boolean isActivated() {
		return zeit.isEnabled();
	}

	void setNotifikation(Notifikation nfkt) {
		// Notifikationstyp
		typ[nfkt.getTyp() + 1].setSelected(true);

		// Vorwarnzeit
		zeit.setText(String.valueOf(nfkt.getZeit()));

		// Zeiteinheit
		zeitEinheit.setSelectedIndex(nfkt.getZeitEinheit());
	}

	Notifikation getNotifikation() {
		if (zeit.isEnabled()) {
			int count = 0;
			while (!typ[count + 1].isSelected())
				count++;

			return new Notifikation(count, zeitEinheit.getSelectedIndex(),
					Integer.valueOf(zeit.getText()).intValue());

		} else
			return null;
	}

	// ItemListener // ItemListener // ItemListener // ItemListener //
	// ItemListener
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			zeit.setEnabled(false);
			zeitEinheit.setEnabled(false);
			status.setIcon(client.Client.loadImageIcon("b" + nummer + "1.gif"));

		} else {
			zeit.setEnabled(true);
			zeitEinheit.setEnabled(true);
			status.setIcon(client.Client.loadImageIcon("b" + nummer + "0.gif"));
		}
	}
}
