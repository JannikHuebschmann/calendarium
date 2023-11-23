package swtkal.swing.client;
//Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

import swtkal.domain.*;
import swtkal.exceptions.TerminException;
import swtkal.swing.elements.MonatsObjekt;

//import client.utility.darstellung.MonatsObjekt;
//import data.Data;
//import data.Shared;

//////////////////////////////////////////////////////////////////////////////////
// Tagesansicht // Tagesansicht // Tagesansicht // Tagesansicht // Tagesansicht //
//////////////////////////////////////////////////////////////////////////////////

public class Tagesansicht extends Ansicht
{
	protected SwingClient client;

	// Panels
	protected JSplitPane splitPane;
	protected JPanel top;

// private Feiertag feiertag;
	protected MonatsObjekt mtPanel;
	protected TerminListeObjekt terminListe;
// private ToDoListeObjekt toDoListe;

	public Tagesansicht(
			JFrame frame, JLabel status, SwingClient client, Date date )
	{
		super(frame, status, client);
		this.client = client;
		
		gui.setTitle("Tagesansicht");
		gui.setSize(600, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout(10, 0));

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				gui.setVisible(false);
			}
		});

		bgnAnsicht = new Datum(date);
		endAnsicht = bgnAnsicht;

		createContent();
	}

	// public Tagesansicht(JFrame frame, JLabel status, Datum date,
	// Vector personenListe)
	// {
	// super(frame, status);
	// gui.setTitle("Tagesansicht");
	//
	// gui.setSize(600, 600);
	// gui.setPreferredSize(gui.getSize());
	// gui.getContentPane().setLayout(new BorderLayout(10, 0));
	//
	// // InternalFrameListener
	// gui.addInternalFrameListener(new InternalFrameAdapter()
	// {
	// public void internalFrameClosing(InternalFrameEvent e)
	// {
	// gui.setVisible(false);
	// }
	// });
	//
	// bgnAnsicht = date;
	// endAnsicht = date;
	//
	// // PersonenListe
	// // openCal = new OffeneKalender();
	// int count = 0;
	//
	// Enumeration e = personenListe.elements();
	// while (e.hasMoreElements())
	// {
	// // openCal.add((Person) e.nextElement(), count++);
	// }
	//
	// create();
	// }

	private void createContent()
	{
		loadDataObjects();

		// SplitPane links
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		top = new JPanel();
		top.setLayout(new GridLayout(1, 2));
		top.setMinimumSize(new Dimension(100, 50));
		top.add(createSelectDate(
				Datum.DAYNAMESSHORT[bgnAnsicht.getWeekDay()] + ". "
				+ bgnAnsicht.getDateStr(), false) );
//		top.add(toDoListe.getGUI());

		splitPane.setTopComponent(top);
		splitPane.setBottomComponent(terminListe.getGUI());

		// rechts neben dem SplitPane ein JPanel
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout(0, 20));
		
		// MonatsPanel
		String intervall = client.getClientProperties().getProperty("MtPaneYears");
		mtPanel = new MonatsObjekt(bgnAnsicht, intervall);
		mtPanel.addActionListener(this);

		right.add("North", mtPanel);
//		right.add("Center", createKalender());
		JLabel meldung = new JLabel("berücksichtigte Kalender derzeit ungenutzt");
		meldung.setEnabled(false);
		right.add("Center", meldung);

		gui.getContentPane().add("Center", splitPane);
		gui.getContentPane().add("East", right);

		setDateLabelSize(new Dimension(100, 25));
	}

	@SuppressWarnings("rawtypes")
	private void loadDataObjects()
	{
	// Vector toDo = Data.toDo
	// .getToDoVom(bgnAnsicht, openCal.getPersonenListe());
		
		Vector termine = new Vector();
		try
		{
			termine = client.getServer().getTermineVom(bgnAnsicht, client.getUser());
		}
		catch (TerminException e)
		{
			e.printStackTrace();
		}
	//
	// toDoListe = new ToDoListeObjekt(this, bgnAnsicht, toDo);
		terminListe = new TerminListeObjekt(this, bgnAnsicht, termine);
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("leftArrow")
				|| e.getActionCommand().equals("rightArrow")
				|| e.getActionCommand().equals("changeDate"))
		{
			if (e.getActionCommand().equals("leftArrow"))
			{
				bgnAnsicht.substract(1);
			}
			else if (e.getActionCommand().equals("rightArrow"))
			{
				bgnAnsicht.add(1);
			}
			else // changeDate
				bgnAnsicht.setDatum(mtPanel.getDate(), false);

			endAnsicht = bgnAnsicht;
			setDateLabel(Datum.DAYNAMESSHORT[bgnAnsicht.getWeekDay()] + ". "
					+ bgnAnsicht.getDateStr());

			updateEintraege();
		}
		else
			super.actionPerformed(e);
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void intervalAdded(ListDataEvent e)
	{
		updateEintraege();
	}

	public void intervalRemoved(ListDataEvent e)
	{
		updateEintraege();
	}

	void updateEintraege()
	{
		splitPane.remove(terminListe.getGUI());
		// top.remove(toDoListe.getGUI());

		loadDataObjects();

		splitPane.setBottomComponent(terminListe.getGUI());
		// top.add(toDoListe.getGUI());

		splitPane.validate();
		top.validate();
	}

//	public void updateTermine(Termin t)
//	// wieso spielt der Parameter t keine Rolle?
//	{
//		splitPane.remove(terminListe.getGUI());
//		
//		Vector termine = new Vector();
//		try
//		{
//			termine = server.getTermineVonBis(bgnAnsicht, endAnsicht, person);
//		}
//		catch (TerminException e)
//		{
//			e.printStackTrace();
//		}
//		terminListe = new TerminListeObjekt(this, bgnAnsicht, termine);
//		
//		splitPane.setBottomComponent(terminListe.getGUI());
//		splitPane.validate();
//	}
//
//	public void updateToDo(ToDo t)
//	{
//		// top.remove(toDoListe.getGUI());
//		//
//		// Vector toDo = Data.toDo
//		// .getToDoVom(bgnAnsicht, openCal.getPersonenListe());
//		//		toDoListe = new ToDoListeObjekt(this, bgnAnsicht, toDo);
//		//
//		//		top.add(toDoListe.getGUI());
//		// top.validate();
//	}
}
