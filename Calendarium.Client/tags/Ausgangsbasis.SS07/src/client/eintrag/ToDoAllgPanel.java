package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

import basisklassen.*;
import client.utility.darstellung.ButtonPanel;

///////////////////////////////////////////////////////////////////////////////////////
// ToDoAllgPanel // ToDoAllgPanel // ToDoAllgPanel // ToDoAllgPanel // ToDoAllgPanel //
///////////////////////////////////////////////////////////////////////////////////////

class ToDoAllgPanel extends AllgemeinPanel {
	// ToDo
	private ToDo toDo;

	ToDoAllgPanel(JFrame f) {
		super(f);
		create();
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));

		pane.add(createDate());
		pane.add(Box.createRigidArea(new Dimension(0, 5)));
		pane.add(createBeschreibung());
		pane.add(Box.createRigidArea(new Dimension(0, 5)));
		pane.add(createBenachrichtigung());

		gui.add("Center", pane);
		gui.add("South", createBottom());
	}

	// Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum //
	// Datum //
	JPanel createDate() {
		JPanel datum = new JPanel();
		datum.setLayout(new GridLayout(1, 2, 10, 0));
		datum.setPreferredSize(new Dimension(500, 55));

		JPanel date1 = new JPanel();
		date1.setLayout(new BoxLayout(date1, BoxLayout.Y_AXIS));
		date1.setBorder(new TitledBorder("Erinnern ab"));

		date1.add((datePanel[0] = new DatePanel(parentFrame)).getGUI());

		JPanel date2 = new JPanel();
		date2.setLayout(new BoxLayout(date2, BoxLayout.Y_AXIS));
		date2.setBorder(new TitledBorder("Fällig per"));

		date2.add((datePanel[1] = new DatePanel(parentFrame)).getGUI());

		datum.add(date1);
		datum.add(date2);

		return datum;
	}

	// Bottom // Bottom // Bottom // Bottom // Bottom // Bottom // Bottom //
	// Bottom //
	JPanel createBottom() {
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.setBorder(new EmptyBorder(5, 12, 0, 11));

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

		serienFlag = new JCheckBox("SerienTodo");
		serienFlag.setMnemonic('s');
		serienFlag.setEnabled(false); // changed by daniela esberger - 14.
		// juni 2005

		left.add(serienFlag);
		left.add(Box.createVerticalStrut(5));

		buttons = new ButtonPanel(false);
		buttons.addActionListener(this);

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

		right.add(Box.createVerticalGlue());
		right.add(buttons);

		bottom.add(left);
		bottom.add(Box.createHorizontalGlue());
		bottom.add(right);

		return bottom;
	}

	void fill(ToDo t) {
		// ToDo setzen
		toDo = t;

		if (toDo.getErinnernAb().isCorrectDate()) {
			// Erinnerungszeitpunkt
			datePanel[0].getDatumsfeld()
					.setDate(toDo.getErinnernAb().getDate());

			if (toDo.getFälligPer().isCorrectDate()) {
				// Fälligkeit
				datePanel[1].getDatumsfeld().setDate(
						toDo.getFälligPer().getDate());
			}
		}

		// Allgemein
		kurzText.setText(toDo.getKurzText());

		if (toDo.getTyp() != null) {
			for (int i = 0; i < eintragsTyp.getItemCount(); i++) {
				EintragsTyp typ = (EintragsTyp) eintragsTyp.getItemAt(i);
				if (typ.getID() == toDo.getTyp().getID()) {
					eintragsTyp.setSelectedIndex(i);
					break;
				}
			}
		} else { // eintragsTyp.setSelectedIndex(0);
		}

		langText.setText(toDo.getLangText());
		ort.setText(toDo.getOrt());
		hyperlink.setText(toDo.getHyperlink());

		// Notifikationen
		Notifikation[] nfkt = toDo.getNotifikationen();

		if (nfkt != null) {
			for (int i = 0; i < nfkt.length; i++) {
				nfktPanel[i].setNotifikation(nfkt[i]);
			}
		}

		// Serie?
		if (toDo.getSerie() != null && toDo.getSerie().getTyp() >= 0) {
			serienFlag.setSelected(true);
			serienFlag.setEnabled(true);
		}
	}

	void setErinnernAb() { // Erinnerungszeitpunkt setzen
		toDo.getErinnernAb().setDatum(datePanel[0].getDatumsfeld().getDate());
	}

	void setFälligPer() { // Fälligkeit setzen
		toDo.getFälligPer().setDatum(datePanel[1].getDatumsfeld().getDate());
	}

	void getAttribute() {
		setErinnernAb();
		setFälligPer();

		// Kurztext
		toDo.setKurzText(kurzText.getText());

		// Eintragstyp
		toDo.setTyp((EintragsTyp) eintragsTyp.getSelectedItem());

		// Langtext
		toDo.setLangText(langText.getText());

		// Ort
		toDo.setOrt(ort.getText());

		// Hyperlink
		toDo.setHyperlink(hyperlink.getText());

		// Notifikationen
		if (getNfktCount() > 0) {
			Notifikation[] nfkt = new Notifikation[getNfktCount()];

			for (int i = 0, count = 0; i < 3; i++) {
				Notifikation n = nfktPanel[i].getNotifikation();
				if (n != null) {
					nfkt[count] = n;
					count++;
				}
			}

			toDo.setNotifikationen(nfkt);
		}

		// Serie
		if (!serienFlag.isSelected()) {

			toDo.setSerie(null);
		}
	}
}
