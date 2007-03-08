package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;

import data.*;
import basisklassen.*;
import client.Client;
import client.utility.darstellung.CustomBorder;

///////////////////////////////////////////////////////////////////////////////////////
// Monatsansicht // Monatsansicht // Monatsansicht // Monatsansicht // Monatsansicht //
///////////////////////////////////////////////////////////////////////////////////////

public class Monatsansicht extends Ansicht implements Shared {
	// JPanel
	private JPanel monthPane;

	// Termine und ToDo
	private ToDoTerminListeObjekt toDoTerminObjekte[];

	public Monatsansicht(JFrame frame, JLabel status, Date date) {
		super(frame, status);
		gui.setTitle("Monatsansicht");

		gui.setSize(600, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");

		bgnAnsicht = new Datum(simpleDate.format(date));
		endAnsicht = new Datum(simpleDate.format(date));

		bgnAnsicht.setToMonthBeginn();
		endAnsicht.setToMonthEnd();

		create();
	}

	JPanel createKalender() {
		selKalender = new KalenderAuswahl(this, false);
		selKalender.addListDataListener(this);

		selKalender.getGUI().setMaximumSize(new Dimension(300, 100));
		selKalender.getGUI().setPreferredSize(new Dimension(300, 100));

		return selKalender.getGUI();
	}

	void create() {
		gui.getContentPane().add("North", createSelectPane());
		gui.getContentPane().add("Center", monthPane = createMonthPane());
	}

	JPanel createSelectPane() {
		// selectPane
		JPanel selectPane = new JPanel();
		selectPane.setLayout(new BoxLayout(selectPane, BoxLayout.X_AXIS));

		selectPane.add(Box.createHorizontalStrut(20));
		selectPane.add(createSelectDate(
				MONTHNAMESLONG[bgnAnsicht.getMonth() - 1] + " "
						+ bgnAnsicht.getYear(), false));
		selectPane.add(Box.createHorizontalStrut(20));
		selectPane.add(createKalender());

		setDateLabelSize(new Dimension(130, 50));
		return selectPane;
	}

	JPanel createMonthPane() {
		// monthPane
		JPanel monthPane = new JPanel();
		monthPane.setLayout(new BorderLayout());

		JPanel heading = new JPanel();
		heading.setLayout(new GridLayout(1, 7));
		heading.setBackground(HEADING_BACKGRD);

		for (int i = 0; i < 7; i++) {
			JLabel text = new JLabel(DAYNAMESLONG[i]);
			text.setForeground(Color.black);
			text.setFont(new Font("Dialog", Font.BOLD, 12));
			text.setHorizontalAlignment(JLabel.CENTER);
			heading.add(text);
		}

		int maxDay = endAnsicht.getDay();
		int weekDay = bgnAnsicht.getWeekDay();
		int zeilen = (maxDay + weekDay - 1) / 7 + 1;

		JPanel mtPane = new JPanel();
		mtPane.setLayout(new GridLayout(zeilen, 7));
		mtPane.setBackground(TAGE_BACKGRD);

		for (int i = 0; i < weekDay; i++) {
			JPanel leer = new JPanel();
			leer.setBorder(new CustomBorder(Color.black, false, false, true,
					false));
			leer.setBackground(TAGE_BACKGRD);
			mtPane.add(leer);
		}

		// Dateneinlesen // Dateneinlesen // Dateneinlesen
		toDoTerminObjekte = loadDataObjects();
		bgnAnsicht.substract(1);
		Feiertag feiertag;

		for (int i = 0; i < maxDay; i++) {
			boolean oben = true;
			boolean rechts = true;
			bgnAnsicht.add(1);

			if ((i + weekDay) / 7 > 0)
				oben = false;
			if ((i + weekDay) % 7 < 6)
				rechts = false;

			JPanel dayPane = new JPanel();
			dayPane.setLayout(new BorderLayout());
			dayPane.setBorder(new CustomBorder(Color.black, oben, rechts, true,
					true));
			dayPane.setBackground(TAG_BACKGRD);

			JButton datum = new JButton(" - " + (i + 1) + " - ");

			if ((feiertag = Data.feiertage.getFeiertagByDate(bgnAnsicht)) != null) {
				// Feiertag
				datum.setForeground(FEIERTAG_FOREGRD);
				datum.setToolTipText(feiertag.getBezeichnung());

			} else if ((i + weekDay) % 7 == 6) {
				// Sonntag
				datum.setForeground(FEIERTAG_FOREGRD);
			}

			datum.setMargin(new Insets(0, 0, 0, 0));
			datum.setFont(new Font("Dialog", Font.BOLD, 10));
			datum.setHorizontalAlignment(JButton.CENTER);

			datum.setActionCommand(bgnAnsicht.getDate());
			datum.addActionListener(this);

			JPanel terminPane = new JPanel();
			terminPane.setLayout(new BoxLayout(terminPane, BoxLayout.Y_AXIS));
			terminPane.setBackground(Color.white);

			dayPane.add("North", datum);
			dayPane.add("Center", toDoTerminObjekte[i].getGUI());

			mtPane.add(dayPane);
		}

		for (int i = 0; i < zeilen * 7 - weekDay - maxDay; i++) {
			JPanel leer = new JPanel();
			if (i == 0)
				leer.setBorder(new CustomBorder(Color.black, false, false,
						false, true));
			leer.setBackground(TAGE_BACKGRD);
			mtPane.add(leer);
		}

		monthPane.add("North", heading);
		monthPane.add("Center", mtPane);

		bgnAnsicht.setToMonthBeginn();
		return monthPane;
	}

	ToDoTerminListeObjekt[] loadDataObjects() {
		Vector[] toDo = Data.toDo.getToDoVonBis(bgnAnsicht, endAnsicht, openCal
				.getPersonenListe());
		Vector[] termine = Data.termine.getTermineVonBis(bgnAnsicht,
				endAnsicht, openCal.getPersonenListe());

		toDoTerminObjekte = new ToDoTerminListeObjekt[toDo.length];
		bgnAnsicht.substract(1);

		for (int i = 0; i < toDo.length; i++) {
			bgnAnsicht.add(1);
			toDoTerminObjekte[i] = new ToDoTerminListeObjekt(this, bgnAnsicht,
					toDo[i], termine[i]);
		}

		bgnAnsicht.setToMonthBeginn();

		return toDoTerminObjekte;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		if (c.equals("leftArrow") || c.equals("rightArrow")) {
			if (c.equals("leftArrow")) {
				bgnAnsicht.substract(1);
				endAnsicht.setDatum(bgnAnsicht);

				bgnAnsicht.setDatum(endAnsicht);
				bgnAnsicht.setToMonthBeginn();

			} else {
				endAnsicht.add(1);
				bgnAnsicht.setDatum(endAnsicht);

				endAnsicht.setDatum(bgnAnsicht);
				endAnsicht.setToMonthEnd();
			}

			setDateLabel(MONTHNAMESLONG[bgnAnsicht.getMonth() - 1] + " "
					+ bgnAnsicht.getYear());

			gui.getContentPane().remove(monthPane);
			gui.getContentPane().add("Center", monthPane = createMonthPane());

			gui.validate();

			// changed by daniela esberger (13. juni 2005)
			// &&c.indexOf("Hyperlink verfolgen")<0 hinzugefuegt

		} else if (c.indexOf("eintragen") < 0 && c.indexOf("bearbeiten") < 0
				&& c.indexOf("löschen") < 0 && c.indexOf("schicken") < 0
				&& c.indexOf("Hyperlink verfolgen") < 0) {
			// Tagesansicht
			Tagesansicht tagesansicht = new Tagesansicht(parentFrame,
					statusLabel, new Datum(e.getActionCommand()), openCal
							.getPersonenListe());
			// Listener
			((Client) parentFrame).getListener().addObserver(tagesansicht);

			JInternalFrame gui = tagesansicht.getGUI();

			JLayeredPane layer = ((Client) parentFrame).getLayeredPane();
			try {
				gui.setSelected(true);
			} catch (java.beans.PropertyVetoException ex) {
			}

			layer.add(gui, 0);

		} else
			super.actionPerformed(e);
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void intervalAdded(ListDataEvent e) {
		gui.getContentPane().remove(monthPane);
		gui.getContentPane().add("Center", createMonthPane());

		monthPane.validate();
	}

	public void intervalRemoved(ListDataEvent e) {
		gui.getContentPane().remove(monthPane);
		gui.getContentPane().add("Center", createMonthPane());

		monthPane.validate();
	}

	// Overloaded
	public void updateTermine(Termin t) {
		for (int i = 0; i < endAnsicht.getDay(); i++) {
			if (t.getBeginn().isGreater(bgnAnsicht) <= 0
					&& t.getEnde().isGreater(bgnAnsicht) >= 0) {
				JPanel pane = (JPanel) toDoTerminObjekte[i].getGUI()
						.getParent();
				pane.remove(toDoTerminObjekte[i].getGUI());

				Vector toDo = toDoTerminObjekte[i].getToDo();
				Vector termine = Data.termine.getTermineVom(bgnAnsicht, openCal
						.getPersonenListe());

				toDoTerminObjekte[i] = new ToDoTerminListeObjekt(this,
						bgnAnsicht, toDo, termine);

				pane.add("Center", toDoTerminObjekte[i].getGUI());
				pane.validate();
			}
			bgnAnsicht.add(1);
		}

		bgnAnsicht.substract(1);
		bgnAnsicht.setToMonthBeginn();
	}

	public void updateToDo(ToDo t) {
		for (int i = 0; i < endAnsicht.getDay(); i++) {
			if (t.getErinnernAb().isGreater(bgnAnsicht) <= 0
					&& t.getFälligPer().isGreater(bgnAnsicht) >= 0) {
				JPanel pane = (JPanel) toDoTerminObjekte[i].getGUI()
						.getParent();
				pane.remove(toDoTerminObjekte[i].getGUI());

				Vector toDo = Data.toDo.getToDoVom(bgnAnsicht, openCal
						.getPersonenListe());
				Vector termine = toDoTerminObjekte[i].getTermine();

				toDoTerminObjekte[i] = new ToDoTerminListeObjekt(this,
						bgnAnsicht, toDo, termine);

				pane.add("Center", toDoTerminObjekte[i].getGUI());
				pane.validate();
			}
			bgnAnsicht.add(1);
		}

		bgnAnsicht.substract(1);
		bgnAnsicht.setToMonthBeginn();
	}
}
