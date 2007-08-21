package client.eintrag; //
//////////////////////////

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import basisklassen.Datum;
import basisklassen.Konflikt;
//import basisklassen.Notifikation;
import basisklassen.Termin;
import data.Data;

//////////////////////////////////////////////////////////////////////////////////////
// EditTerminControl // EditTerminControl // EditTerminControl // EditTerminControl //
//////////////////////////////////////////////////////////////////////////////////////

public class EditTerminControl implements ActionListener {
	// ParentFrame
	private JFrame parentFrame;

	// EditTermin
	private EditTermin editTermin;

	// Termin
	private Termin termin;

	// TerminVorschlag
	private TerminVorschlag vorschlag = null;

	// Flag
	private boolean edit;

	// New Instructor
	public EditTerminControl(JFrame f) {
		edit = false;
		parentFrame = f;
		editTermin = new EditTermin(f);// nullpointer 15.5.2k4
		editTermin.addActionListener(this);

		Date date = new Date();
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
		// Start
		termin = new Termin(Data.user);
		termin.getBeginn().setDatum(simpleDate.format(date));
		termin.getEnde().setDatum(simpleDate.format(date));

		editTermin.start(termin);
		editTermin.setTitle("Termin eintragen");
	}

	// New Instructor
	public EditTerminControl(JFrame f, Datum d) {
		edit = false;

		editTermin = new EditTermin(f);
		editTermin.addActionListener(this);

		// Start
		termin = new Termin(Data.user);
		termin.getBeginn().setDatum(d);

		if (!d.isCorrectTime())
			termin.getEnde().setDatum(d);
		else
			termin.getEnde().setDatum(d.addDauer(1));

		editTermin.start(termin);
		editTermin.setTitle("Termin eintragen");
	}

	// Edit Instructor
	public EditTerminControl(JFrame f, Termin t) {
		edit = true;
		parentFrame = f;

		editTermin = new EditTermin(f);
		editTermin.addActionListener(this);

		// Start
		termin = t;

		if (termin.getOwner().getID() == Data.user.getID()) {
			// Owner darf den Termin editieren
			editTermin.start(termin);
			editTermin.setTitle("Termin editieren");

		} else {
			editTermin.start(termin);
			editTermin.setTitle("Termin-Owner: "
					+ termin.getOwner().getNameLang());

			editTermin.disableGUI();
		}
	}

	// GUI
	public JInternalFrame getGUI() {
		return editTermin.getGUI();
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private boolean createTermin() {
		Vector konflikte = Data.termine.getKonflikte(termin);

		if (konflikte.size() == 0) {

			Data.termine.create(termin);

			return true;

		} else {
			String msg = "Konflikte:\n\n";

			// Konflikt
			Enumeration e = konflikte.elements();
			while (e.hasMoreElements()) {
				Konflikt konflikt = (Konflikt) e.nextElement();
				String vb = konflikt.isVerschiebbar() ? "verschiebbar"
						: "nicht verschiebbar";

				msg += konflikt.getPerson().getNameLang() + ": "
						+ konflikt.getBeginn() + " - " + konflikt.getEnde()
						+ ", " + vb + '\n';

			}

			switch (showKonflikteDialog(msg)) {
			case 0: // Zurueck
				return false;

			case 1: // Eintragen

				Data.termine.create(termin);
				return true;

			default: // Suchen

				vorschlag = new TerminVorschlag(parentFrame);
				vorschlag.start(termin);

				return false;
			}
		}
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private boolean updateTermin() {
		Vector konflikte = Data.termine.getKonflikte(termin);

//		Notifikation[] nfkt = termin.getNotifikationen();

		if (konflikte.size() == 0) {
			Data.termine.update(termin);
			return true;

		} else {
			String msg = "Konflikte:\n\n";

			// Konflikt
			Enumeration e = konflikte.elements();
			while (e.hasMoreElements()) {
				Konflikt konflikt = (Konflikt) e.nextElement();
				String vb = konflikt.isVerschiebbar() ? "verschiebbar"
						: "nicht verschiebbar";

				msg += konflikt.getPerson().getNameLang() + ": "
						+ konflikt.getBeginn() + " - " + konflikt.getEnde()
						+ ", " + vb + '\n';

			}

			switch (showKonflikteDialog(msg)) {
			case 0: // Zurueck
				return false;

			case 1: // Eintragen

				Data.termine.update(termin);
				return true;

			default: // Suchen

				vorschlag = new TerminVorschlag(parentFrame);
				vorschlag.start(termin);

				return false;
			}
		}
	}

	private int showKonflikteDialog(String msg) {
		Object[] options = { "zurueck", "trotzdem eintragen", "suchen" };

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
				JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null,
				options, options[2]);

		JDialog dialogPane = optionPane.createDialog(parentFrame,
				"Termin eintragen");

		dialogPane.show();
		dialogPane.dispose();

		Object selectedValue = optionPane.getValue();

		if (selectedValue != null) {
			if (options[1].equals(selectedValue)) {
				return 1;
			} else if (options[2].equals(selectedValue)) {
				return 2;
			}
		}

		return 0;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			// alle Eingaben vollständig?
			if (editTermin.checkInput()) {
				if (!edit) {
					if (createTermin()) {
						Vector failed = editTermin.getFailedPersonen();
						if (failed != null) {
							Data.termine.sendMissingRight(termin, failed);
						}

						try {
							editTermin.getGUI().setClosed(true);
						} catch (java.beans.PropertyVetoException ex) {
						}
					}

				} else {
					if (updateTermin()) {
						Vector failed = editTermin.getFailedPersonen();
						if (failed != null) {
							Data.termine.sendMissingRight(termin, failed);
						}

						try {
							editTermin.getGUI().setClosed(true);
						} catch (java.beans.PropertyVetoException ex) {
						}
					}
				}
			}
		} else {
			try {
				editTermin.getGUI().setClosed(true);
			} catch (java.beans.PropertyVetoException ex) {
			}
		}
	}
}
