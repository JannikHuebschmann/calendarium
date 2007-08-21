package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import data.Data;
import basisklassen.Datum;
import client.utility.darstellung.ButtonPanel;

/////////////////////////////////////////////////////////////////////////////////////////////////
// DeleteUntilDate // DeleteUntilDate // DeleteUntilDate // DeleteUntilDate // DeleteUntilDate //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class DeleteUntilDate implements ActionListener {
	// parentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Daten-Säuberung", true,
			true, false, true);

	// Controls
	private DatePanel datePanel;

	public DeleteUntilDate(JFrame f) {
		parentFrame = f;

		gui.setSize(230, 150);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		create();
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));

		JPanel datum = new JPanel();
		datum.setLayout(new BorderLayout());
		datum.setBorder(new TitledBorder("Lösche alle Einträge bis inkl."));
		datum.add("Center", (datePanel = new DatePanel(parentFrame)).getGUI());

		pane.add("Center", datum);

		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		gui.getContentPane().add("Center", pane);
		gui.getContentPane().add("South", buttons);
	}

	public JInternalFrame getGUI() {
		return gui;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			Datum date = new Datum(datePanel.getDatumsfeld().getDate());

			Data.toDo.deleteUntilDate(date);
			Data.termine.deleteUntilDate(date);

			// Message
			String text = "Termine und ToDo-Einträge erfolgreich gelöscht!";

			JTextArea area = new JTextArea(text);
			area.setBackground(SystemColor.control);
			area.setEditable(false);
			area.setColumns(text.length());
			area.setRows(1);

			JOptionPane optionPane = new JOptionPane(area,
					JOptionPane.INFORMATION_MESSAGE);
			@SuppressWarnings("unused")
			JDialog dialogPane = optionPane.createDialog(parentFrame,
					"Datensäuberung");
		}

		try {
			gui.setClosed(true);
		} catch (java.beans.PropertyVetoException ex) {
		}
	}
}
