package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import data.Data;
import basisklassen.*;
import client.utility.darstellung.ButtonPanel;

/////////////////////////////////////////////////////////////////////////////////////////////////
// TerminVorschlag // TerminVorschlag // TerminVorschlag // TerminVorschlag // TerminVorschlag //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class TerminVorschlag implements ActionListener {
	// parentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	// Dialog
	private JDialog dialog;

	// Controls
	private DatePanel[] datePanel = new DatePanel[2];

	private TimePanel[] timePanel = new TimePanel[2];

	private JRadioButton wkArt[] = new JRadioButton[2];

	// Termin
	private Termin termin;

	public TerminVorschlag(JFrame f) {

		parentFrame = f;

		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		create();
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));

		JPanel datum = new JPanel();
		datum.setLayout(new GridLayout(2, 2, 10, 10));

		JPanel date1 = new JPanel();
		date1.setLayout(new BorderLayout());
		date1.setBorder(new TitledBorder("Suchintervall-Beginn"));
		date1.add("Center", (datePanel[0] = new DatePanel(parentFrame))
				.getGUI());

		JPanel date2 = new JPanel();
		date2.setLayout(new BorderLayout());
		date2.setBorder(new TitledBorder("Suchintervall-Ende"));
		date2.add("Center", (datePanel[1] = new DatePanel(parentFrame))
				.getGUI());

		JPanel time1 = new JPanel();
		time1.setLayout(new BorderLayout());
		time1.setBorder(new TitledBorder("täglich ab"));
		time1.add("Center", (timePanel[0] = new TimePanel()).getGUI());

		JPanel time2 = new JPanel();
		time2.setLayout(new BorderLayout());
		time2.setBorder(new TitledBorder("täglich bis"));
		time2.add("Center", (timePanel[1] = new TimePanel()).getGUI());

		datum.add(date1);
		datum.add(time1);
		datum.add(date2);
		datum.add(time2);

		JPanel wk = new JPanel();
		wk.setLayout(new GridLayout(2, 1));
		wk.setBorder(new EmptyBorder(10, 5, 0, 5));

		wkArt[0] = new JRadioButton("nur Werktags");
		wkArt[0].setMnemonic('w');
		wkArt[0].setSelected(true);
		wkArt[1] = new JRadioButton("auch So + Feiertage");
		wkArt[1].setMnemonic('s');

		ButtonGroup group = new ButtonGroup();
		group.add(wkArt[0]);
		group.add(wkArt[1]);

		wk.add(wkArt[0]);
		wk.add(wkArt[1]);

		pane.add(datum);
		pane.add(wk);

		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		gui.add("Center", pane);
		gui.add("South", buttons);
	}

	void start(Termin t) {
		termin = t;

		datePanel[0].getDatumsfeld().setDate(termin.getBeginn().getDate());
		datePanel[1].getDatumsfeld().setDate(termin.getEnde().getDate());

		timePanel[0].getZeitfeld().setTime("08:00");
		timePanel[1].getZeitfeld().setTime("18:00");

		dialog = new JDialog(parentFrame, "Suche nach konfliktfreiem Termin",
				true);

		dialog.setSize(450, 250);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add("Center", gui);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - 450) / 2,
				(screenSize.height - 250) / 2);
		dialog.show();
	}

	void showDialog(String msg) {
		JTextArea area = new JTextArea(msg);

		StringTokenizer st = new StringTokenizer(msg, "\n");
		int len = 0, count = 0;

		while (st.hasMoreTokens()) {
			len = Math.max(len, st.nextToken().length());
			count++;
		}

		area.setBackground(SystemColor.control);
		area.setEditable(false);
		area.setColumns(len);
		area.setRows(count);

		JOptionPane optionPane = new JOptionPane(area,
				JOptionPane.INFORMATION_MESSAGE);
		JDialog dialogPane = optionPane.createDialog(parentFrame,
				"Suchergebnis");

		dialogPane.show();
		dialogPane.dispose();
	}

	public void actionPerformed(ActionEvent evt) {
		String msg;

		if (evt.getActionCommand().equals("OK")) {
			Datum bgn = new Datum(datePanel[0].getDatumsfeld().getDate(),
					timePanel[0].getZeitfeld().getTime());

			Datum end = new Datum(datePanel[1].getDatumsfeld().getDate(),
					timePanel[1].getZeitfeld().getTime());

			if (end.isGreater(bgn) > 0) {
				// Abfrage
				Vector freeBlocks = Data.termine.getFreeOfKonflikte(termin
						.getAllPersons(), bgn, end, wkArt[0].isSelected());
				// Ergebnis
				if (freeBlocks.size() > 0) {
					msg = "Konfliktfreie Intervalle:\n\n";

					Enumeration e = freeBlocks.elements();
					while (e.hasMoreElements()) {
						String[] limit = (String[]) e.nextElement();
						msg += limit[0] + " - " + limit[1] + '\n';
					}
				} else
					msg = "Keine konfliktfreien Intervalle gefunden!\n";

				showDialog(msg);
			}
		} else
			dialog.dispose();
	}
}
