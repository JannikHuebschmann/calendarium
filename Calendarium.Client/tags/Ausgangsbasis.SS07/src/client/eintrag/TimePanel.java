package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import client.gui.Zeitfeld;

/////////////////////////////////////////////////////////////////////////////////////////////
// TimePanel // TimePanel // TimePanel // TimePanel // TimePanel // TimePanel // TimePanel //
/////////////////////////////////////////////////////////////////////////////////////////////

class TimePanel {
	// graphical Representation
	private JPanel gui = new JPanel();

	// Control
	private Zeitfeld timeField;

	TimePanel() {
		gui.setLayout(new BoxLayout(gui, BoxLayout.X_AXIS));
		gui.setBorder(new EmptyBorder(0, 5, 0, 5));

		create();
	}

	void create() {
		JLabel l1 = new JLabel("Uhrzeit:");
		l1.setPreferredSize(new Dimension(50, 20));
		l1.setAlignmentY((float) 0.5);
		l1.setDisplayedMnemonic('u');
		timeField = new Zeitfeld();
		timeField.setPreferredSize(new Dimension(90, 23));
		timeField.setMaximumSize(new Dimension(500, 23));
		gui.add(l1);
		gui.add(timeField);
		gui.add(Box.createRigidArea(new Dimension(28, 23)));
	}

	JPanel getGUI() {
		return gui;
	}

	Zeitfeld getZeitfeld() {
		return timeField;
	}
}
