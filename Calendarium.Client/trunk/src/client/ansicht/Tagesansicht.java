package client.ansicht; //
//////////////////////////

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListDataEvent;

import basisklassen.Datum;
import basisklassen.Feiertag;
import basisklassen.Person;
import basisklassen.Termin;
import basisklassen.ToDo;
import client.Client;
import client.utility.darstellung.MonatsObjekt;
import data.Data;
import data.Shared;

//////////////////////////////////////////////////////////////////////////////////
// Tagesansicht // Tagesansicht // Tagesansicht // Tagesansicht // Tagesansicht //
//////////////////////////////////////////////////////////////////////////////////

public class Tagesansicht extends Ansicht implements Shared {
	// Panels
	private JSplitPane splitPane;

	private JPanel top;

	private Feiertag feiertag;

	// MonatsPanel
	private MonatsObjekt mtPanel;

	// Termine und ToDo
	private TerminListeObjekt terminListe;

	private ToDoListeObjekt toDoListe;

	public Tagesansicht(JFrame frame, JLabel status, Date date) {
		super(frame, status);
		gui.setTitle("Tagesansicht");

		gui.setSize(600, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout(10, 0));

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
		bgnAnsicht = new Datum(simpleDate.format(date));
		endAnsicht = bgnAnsicht;

		create();
	}

	public Tagesansicht(JFrame frame, JLabel status, Datum date,
			Vector personenListe) {
		super(frame, status);
		gui.setTitle("Tagesansicht");

		gui.setSize(600, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout(10, 0));

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		bgnAnsicht = date;
		endAnsicht = date;

		// PersonenListe
		openCal = new OffeneKalender();
		int count = 0;

		Enumeration e = personenListe.elements();
		while (e.hasMoreElements()) {
			openCal.add((Person) e.nextElement(), count++);
		}

		create();
	}

	private void create() {
		loadDataObjects();

		// MonatsPanel
		String intervall = ((Client) parentFrame).getProperty("MtPaneYears");
		mtPanel = new MonatsObjekt(bgnAnsicht, intervall);
		mtPanel.addActionListener(this);

		JPanel left = new JPanel();
		left.setLayout(new BorderLayout(0, 20));

		left.add("North", mtPanel);
		left.add("Center", createKalender());

		// SplitPane
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		top = new JPanel();
		top.setLayout(new GridLayout(1, 2));
		top.setMinimumSize(new Dimension(100, 50));

		if ((feiertag = Data.feiertage.getFeiertagByDate(bgnAnsicht)) != null
				|| (Shared.DAYNAMESSHORT[bgnAnsicht.getWeekDay()]).equals("So")) {
			top
					.add(createSelectDate(Shared.DAYNAMESSHORT[bgnAnsicht
							.getWeekDay()]
							+ ". "
							+ bgnAnsicht.getDay()
							+ "."
							+ bgnAnsicht.getMonth() + ".", String
							.valueOf(bgnAnsicht.getYear()), true, feiertag));

		} else {
			top.add(createSelectDate(Shared.DAYNAMESSHORT[bgnAnsicht
					.getWeekDay()]
					+ ". "
					+ bgnAnsicht.getDay()
					+ "."
					+ bgnAnsicht.getMonth()
					+ ".", String.valueOf(bgnAnsicht.getYear()), false,
					feiertag));
		}

		top.add(toDoListe.getGUI());

		splitPane.setTopComponent(top);
		splitPane.setBottomComponent(terminListe.getGUI());

		gui.getContentPane().add("Center", splitPane);
		gui.getContentPane().add("East", left);

		setDateLabelSize(new Dimension(100, 25));
	}

	private void loadDataObjects() {
		Vector toDo = Data.toDo.getToDoVom(bgnAnsicht, openCal
				.getPersonenListe());
		Vector termine = Data.termine.getTermineVom(bgnAnsicht, openCal
				.getPersonenListe());

		toDoListe = new ToDoListeObjekt(this, bgnAnsicht, toDo);
		terminListe = new TerminListeObjekt(this, bgnAnsicht, termine);
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		boolean isfeiertag;
		Feiertag feiertag;
		if (e.getActionCommand().equals("leftArrow")
				|| e.getActionCommand().equals("rightArrow")
				|| e.getActionCommand().equals("changeDate")) {
			if (e.getActionCommand().equals("leftArrow")) {
				bgnAnsicht.substract(1);

			} else if (e.getActionCommand().equals("rightArrow")) {
				bgnAnsicht.add(1);

			} else
				bgnAnsicht.setDatum(mtPanel.getDate(), false);

			endAnsicht = bgnAnsicht;
			if ((feiertag = Data.feiertage.getFeiertagByDate(bgnAnsicht)) != null
					|| (Shared.DAYNAMESSHORT[bgnAnsicht.getWeekDay()])
							.equals("So")) {

				isfeiertag = true;

			} else {
				isfeiertag = false;
			}
			setDateLabel(Shared.DAYNAMESSHORT[bgnAnsicht.getWeekDay()] + ". "
					+ bgnAnsicht.getDay() + "." + bgnAnsicht.getMonth() + ".",
					String.valueOf(bgnAnsicht.getYear()), isfeiertag, feiertag);

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
		splitPane.remove(terminListe.getGUI());
		top.remove(toDoListe.getGUI());

		loadDataObjects();

		splitPane.setBottomComponent(terminListe.getGUI());
		top.add(toDoListe.getGUI());

		splitPane.validate();
		top.validate();
	}

	// Overloaded
	public void updateTermine(Termin t) {
		splitPane.remove(terminListe.getGUI());

		Vector termine = Data.termine.getTermineVom(bgnAnsicht, openCal
				.getPersonenListe());
		terminListe = new TerminListeObjekt(this, bgnAnsicht, termine);

		splitPane.setBottomComponent(terminListe.getGUI());
		splitPane.validate();
	}

	public void updateToDo(ToDo t) {
		top.remove(toDoListe.getGUI());

		Vector toDo = Data.toDo.getToDoVom(bgnAnsicht, openCal
				.getPersonenListe());
		toDoListe = new ToDoListeObjekt(this, bgnAnsicht, toDo);

		top.add(toDoListe.getGUI());
		top.validate();
	}

}
