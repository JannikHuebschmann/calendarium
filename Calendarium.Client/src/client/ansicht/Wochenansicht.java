package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import data.*;
import basisklassen.*;

////////////////////////////////////////////////////////////////////////////////////////////////////////
// Wochenansicht // Wochenansicht // Wochenansicht // Wochenansicht // Wochenansicht // Wochenansicht //
////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Wochenansicht extends Ansicht implements Shared {
	// Label
	private JLabel[] dateLabel = new JLabel[7];

	private TerminListeObjekt[] terminObjekte = new TerminListeObjekt[7];

	private ToDoListeObjekt[] toDoObjekte = new ToDoListeObjekt[7];

	public Wochenansicht(JFrame frame, JLabel status, Date date) {
		super(frame, status);
		gui.setTitle("Wochenansicht");

		gui.setSize(800, 600);
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

		while (bgnAnsicht.getWeekDay() > 0) {
			bgnAnsicht.substract(1);
			endAnsicht.substract(1);
		}

		endAnsicht.add(6);
		create();
	}

	private void create() {
		Datum bgn = new Datum();
		bgn.setDatum(bgnAnsicht.getDate());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 4, 5, 5));

		Feiertag feiertag;
		boolean isfeiertag;

		loadDataObjects();

		for (int i = 0; i < 7; i++) {
			// SplitPane
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

			JPanel top = new JPanel();
			top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
			top.setMinimumSize(new Dimension(85, 50));

			if ((feiertag = Data.feiertage.getFeiertagByDate(bgn)) != null
					|| (Shared.DAYNAMESSHORT[i]).equals("So")) {

				isfeiertag = true;

			} else {
				isfeiertag = false;
			}

			dateLabel[i] = new JLabel(DAYNAMESSHORT[i] + ". " + bgn.getDay()
					+ "." + bgn.getMonth() + ".");
			dateLabel[i].setFont(new Font("Dialog", Font.BOLD, 16));
			dateLabel[i].setPreferredSize(new Dimension(85, 50));
			dateLabel[i].setMinimumSize(new Dimension(85, 50));
			dateLabel[i].setVerticalAlignment(JLabel.CENTER);
			if (isfeiertag) {
				dateLabel[i].setForeground(FEIERTAG_FOREGRD);
				if (feiertag != null) {
					dateLabel[i].setToolTipText(feiertag.getBezeichnung());
				}
			} else {
				dateLabel[i].setForeground(Color.black);
			}

			toDoObjekte[i].getGUI().setPreferredSize(new Dimension(500, 50));

			top.add(dateLabel[i]);
			top.add(toDoObjekte[i].getGUI());

			splitPane.setTopComponent(top);
			splitPane.setBottomComponent(terminObjekte[i].getGUI());

			mainPanel.add(splitPane);
			bgn.add(1);
		}

		mainPanel.add(createKalender());

		gui.getContentPane().add(
				"North",
				createSelectDate(bgnAnsicht.getWeek() + ". Woche, "
						+ bgnAnsicht.getYear(), false));
		gui.getContentPane().add("Center", mainPanel);

		setDateLabelSize(new Dimension(150, 50));
	}

	private void loadDataObjects() {
		Datum bgn = new Datum();
		bgn.setDatum(bgnAnsicht.getDate());

		Vector[] toDo = Data.toDo.getToDoVonBis(bgnAnsicht, endAnsicht, openCal
				.getPersonenListe());
		Vector[] termine = Data.termine.getTermineVonBis(bgnAnsicht,
				endAnsicht, openCal.getPersonenListe());

		for (int i = 0; i < 7; i++) {

			toDoObjekte[i] = new ToDoListeObjekt(this, bgn, toDo[i]);
			terminObjekte[i] = new TerminListeObjekt(this, bgn, termine[i]);

			bgn.add(1);
		}
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		boolean isfeiertag;
		Feiertag feiertag;
		if (e.getActionCommand().equals("leftArrow")
				|| e.getActionCommand().equals("rightArrow")) {
			if (e.getActionCommand().equals("leftArrow")) {
				endAnsicht.substract(7);

				for (int i = 6; i >= 0; i--) {
					bgnAnsicht.substract(1);
					if ((feiertag = Data.feiertage
							.getFeiertagByDate(bgnAnsicht)) != null
							|| (Shared.DAYNAMESSHORT[i]).equals("So")) {

						isfeiertag = true;

					} else {
						isfeiertag = false;
					}

					dateLabel[i].setText(DAYNAMESSHORT[i] + ". "
							+ bgnAnsicht.getDay() + "." + endAnsicht.getMonth()
							+ ".");
					if (isfeiertag) {
						dateLabel[i].setForeground(FEIERTAG_FOREGRD);
						if (feiertag != null) {
							dateLabel[i].setToolTipText(feiertag
									.getBezeichnung());
						}
					} else {
						dateLabel[i].setForeground(Color.black);
					}
				}
			} else {
				bgnAnsicht.add(7);

				for (int i = 0; i < 7; i++) {
					endAnsicht.add(1);
					if ((feiertag = Data.feiertage
							.getFeiertagByDate(endAnsicht)) != null
							|| (Shared.DAYNAMESSHORT[i]).equals("So")) {

						isfeiertag = true;

					} else {
						isfeiertag = false;
					}

					dateLabel[i].setText(DAYNAMESSHORT[i] + ". "
							+ endAnsicht.getDay() + "." + endAnsicht.getMonth()
							+ ".");
					if (isfeiertag) {
						dateLabel[i].setForeground(FEIERTAG_FOREGRD);
						if (feiertag != null) {
							dateLabel[i].setToolTipText(feiertag
									.getBezeichnung());
						}
					} else {
						dateLabel[i].setForeground(Color.black);
					}
				}
			}

			setDateLabel(bgnAnsicht.getWeek() + ". Woche, "
					+ bgnAnsicht.getYear());
			updateEinträge();

		} else
			super.actionPerformed(e);
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void intervalAdded(ListDataEvent e) {
		updateEinträge();
	}

	public void intervalRemoved(ListDataEvent e) {
		updateEinträge();
	}

	void updateEinträge() {
		JSplitPane splitPane[] = new JSplitPane[7];
		JPanel pane[] = new JPanel[7];

		for (int i = 0; i < 7; i++) {
			splitPane[i] = (JSplitPane) terminObjekte[i].getGUI().getParent();
			pane[i] = (JPanel) toDoObjekte[i].getGUI().getParent();

			splitPane[i].remove(terminObjekte[i].getGUI());
			pane[i].remove(toDoObjekte[i].getGUI());
		}

		loadDataObjects();

		for (int i = 0; i < 7; i++) {
			splitPane[i].setBottomComponent(terminObjekte[i].getGUI());
			splitPane[i].validate();

			pane[i].add(toDoObjekte[i].getGUI());
			pane[i].validate();
		}
	}

	// Overloaded
	public void updateTermine(Termin t) {
		Datum bgn = new Datum();
		bgn.setDatum(bgnAnsicht.getDate());

		for (int i = 0; i < 7; i++) {
			if (t.getBeginn().isGreater(bgn) <= 0
					&& t.getEnde().isGreater(bgn) >= 0) {
				JSplitPane splitPane = (JSplitPane) terminObjekte[i].getGUI()
						.getParent();
				splitPane.remove(terminObjekte[i].getGUI());

				Vector termine = Data.termine.getTermineVom(bgn, openCal
						.getPersonenListe());
				terminObjekte[i] = new TerminListeObjekt(this, bgn, termine);

				splitPane.setBottomComponent(terminObjekte[i].getGUI());
				splitPane.validate();
			}
			bgn.add(1);
		}
	}

	public void updateToDo(ToDo t) {
		Datum bgn = new Datum();
		bgn.setDatum(bgnAnsicht.getDate());

		for (int i = 0; i < 7; i++) {
			if (t.getErinnernAb().isGreater(bgn) <= 0
					&& t.getFälligPer().isGreater(bgn) >= 0) {
				JPanel pane = (JPanel) toDoObjekte[i].getGUI().getParent();
				pane.remove(toDoObjekte[i].getGUI());

				Vector toDo = Data.toDo.getToDoVom(bgn, openCal
						.getPersonenListe());
				toDoObjekte[i] = new ToDoListeObjekt(this, bgn, toDo);

				pane.add(toDoObjekte[i].getGUI());
				pane.validate();
			}
			bgn.add(1);
		}
	}
}
