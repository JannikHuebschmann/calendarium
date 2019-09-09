package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import javax.swing.*;
//import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

//import basisklassen.Serie;
//
//import client.gui.SpinZahlenfeld;
import swtkal.swing.ListenerForActions;
//import client.utility.darstellung.ButtonPanel;
//import basisklassen.Datum;

////////////////////////////////////////////////////////////////////////////////////////////
// SerienPanel // SerienPanel // SerienPanel // SerienPanel // SerienPanel // SerienPanel //
////////////////////////////////////////////////////////////////////////////////////////////

class SerienPanel extends ListenerForActions // implements data.Shared
{
	// Teilnehmer
//	private Serie serie;
//
	// graphical Representation
	private JPanel gui = new JPanel();

//	// ParentFrame
//	private JFrame parentFrame;
//
//	// Beginn-, Endedatum
//	private DatePanel[] datePanel = new DatePanel[2];
//
//	// Controls
//	private JRadioButton typ[] = new JRadioButton[4];
//
//	private JCheckBox dyBox[] = new JCheckBox[7];
//
//	private JRadioButton mtArt[] = new JRadioButton[2];
//
//	private SpinZahlenfeld spin[] = new SpinZahlenfeld[4];
//
//	private JRadioButton wkArt[] = new JRadioButton[2];
//
//	// Buttons
//	private ButtonPanel buttons;
//
//	// Serientyp
//	private int selectedTyp = -1;

	// changed by daniela esberger (13. juni 2005) - JFrame f hinzugefuegt
	// damit enddatum mittels kalender (icon) einstellbar ist
	SerienPanel(JFrame f)
	{
//		parentFrame = f;
		gui.setLayout(new BorderLayout());
		create();
	}

	void create() {
//		JPanel pane = new JPanel();
//		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
//		pane.setBorder(new EmptyBorder(10, 10, 10, 10));
//		gui.add("North", pane);
//		gui.add("Center", createBottom());

		JLabel label = new JLabel("Serientermine derzeit ungenutzt");
		label.setEnabled(false);
		gui.add("Center", label);
	}

	JPanel getGUI()
	{
		return gui;
	}

//	// Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum //
//	// Datum // Datum //
//	JPanel createDate() {
//		JPanel datum = new JPanel();
//		datum.setLayout(new GridLayout(1, 2, 10, 0));
//		datum.setPreferredSize(new Dimension(500, 55));
//
//		JPanel date1 = new JPanel();
//		date1.setLayout(new BoxLayout(date1, BoxLayout.Y_AXIS));
//		date1.setBorder(new TitledBorder("Beginn"));
//
//		date1.add((datePanel[0] = new DatePanel(parentFrame)).getGUI());
//		datePanel[0].getDatumsfeld().setEnabled(false);
//		datePanel[0].getCalendarButton().setVisible(false);
//
//		JPanel date2 = new JPanel();
//		date2.setLayout(new BoxLayout(date2, BoxLayout.Y_AXIS));
//		date2.setBorder(new TitledBorder("Ende"));
//
//		date2.add((datePanel[1] = new DatePanel(parentFrame)).getGUI());
//
//		datum.add(date1);
//		datum.add(date2);
//
//		return datum;
//	}
//
//	JPanel createSerienTypen() {
//		JPanel serienTypen = new JPanel();
//		serienTypen.setLayout(new BoxLayout(serienTypen, BoxLayout.Y_AXIS));
//		serienTypen.setBorder(new TitledBorder("Serientyp"));
//
//		ButtonGroup mode = new ButtonGroup();
//
//		typ[0] = new JRadioButton("täglich");
//		typ[0].setMnemonic('t');
//
//		typ[1] = new JRadioButton("wöchentlich");
//		typ[1].setMnemonic('w');
//		typ[2] = new JRadioButton("monatlich");
//		typ[2].setMnemonic('m');
//		typ[3] = new JRadioButton("jährlich");
//		typ[3].setMnemonic('j');
//
//		for (int i = 0; i < 4; i++) {
//			typ[i].setPreferredSize(new Dimension(100, 20));
//			typ[i].setAlignmentY((float) 0.5);
//			typ[i].setActionCommand(typ[i].getText());
//			typ[i].addActionListener(this);
//
//			mode.add(typ[i]);
//
//			spin[i] = new SpinZahlenfeld(2);
//			spin[i].setPreferredSize(new Dimension(35, 22));
//			spin[i].setMaximumSize(new Dimension(35, 22));
//			spin[i].setValue(1);
//		}
//
//		serienTypen.add(createTyp1());
//		serienTypen.add(createTyp2());
//		serienTypen.add(createTyp3());
//		serienTypen.add(createTyp4());
//
//		return serienTypen;
//	}
//
//	JPanel createTyp1() {
//		JPanel typPane = new JPanel();
//		typPane.setLayout(new BoxLayout(typPane, BoxLayout.X_AXIS));
//		typPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 5, 5),
//				new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 5,
//						5))));
//
//		JLabel l1 = new JLabel("jeden/alle");
//		l1.setPreferredSize(new Dimension(60, 20));
//		l1.setAlignmentY((float) 0.5);
//
//		JLabel l2 = new JLabel("Tag(e)");
//		l2.setPreferredSize(new Dimension(60, 20));
//		l2.setAlignmentY((float) 0.5);
//
//		typPane.add(typ[0]);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(l1);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(spin[0]);
//		typPane.add(Box.createHorizontalStrut(10));
//		typPane.add(l2);
//		typPane.add(Box.createHorizontalGlue());
//
//		return typPane;
//	}
//
//	JPanel createTyp2() {
//		JPanel typPane = new JPanel();
//		typPane.setLayout(new BoxLayout(typPane, BoxLayout.X_AXIS));
//		typPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 5, 5),
//				new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 5,
//						5))));
//
//		JPanel rechts = new JPanel();
//		rechts.setLayout(new BoxLayout(rechts, BoxLayout.Y_AXIS));
//
//		JPanel zeile1 = new JPanel();
//		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));
//
//		JLabel l1 = new JLabel("jede/alle");
//		l1.setPreferredSize(new Dimension(60, 20));
//		l1.setAlignmentY((float) 0.5);
//
//		JLabel l2 = new JLabel("Woche(n)");
//		l2.setPreferredSize(new Dimension(60, 20));
//		l2.setAlignmentY((float) 0.5);
//
//		zeile1.add(l1);
//		zeile1.add(Box.createHorizontalStrut(5));
//		zeile1.add(spin[1]);
//		zeile1.add(Box.createHorizontalStrut(10));
//		zeile1.add(l2);
//		zeile1.add(Box.createHorizontalGlue());
//
//		JPanel zeile2 = new JPanel();
//		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));
//
//		JPanel pane = new JPanel();
//		pane.setLayout(new GridLayout(2, 4, 10, 0));
//
//		JPanel dayPane;
//
//		for (int i = 0; i < 7; i++) {
//			dayPane = new JPanel();
//			dayPane.setLayout(new BorderLayout());
//
//			l1 = new JLabel(DAYNAMESSHORT[i]);
//			l1.setAlignmentY((float) 0.5);
//
//			dyBox[i] = new JCheckBox();
//			dyBox[i].setAlignmentY((float) 0.5);
//			dyBox[i].setSelected(false);
//
//			dayPane.add("Center", l1);
//			dayPane.add("East", dyBox[i]);
//
//			pane.add(dayPane);
//		}
//
//		zeile2.add(pane);
//		zeile2.add(Box.createHorizontalGlue());
//
//		rechts.add(zeile1);
//		rechts.add(Box.createVerticalStrut(5));
//		rechts.add(zeile2);
//
//		typPane.add(typ[1]);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(rechts);
//		typPane.add(Box.createHorizontalGlue());
//
//		return typPane;
//	}
//
//	JPanel createTyp3() {
//		JPanel typPane = new JPanel();
//		typPane.setLayout(new BoxLayout(typPane, BoxLayout.X_AXIS));
//		typPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 5, 5),
//				new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 5,
//						5))));
//
//		JPanel rechts = new JPanel();
//		rechts.setLayout(new BoxLayout(rechts, BoxLayout.Y_AXIS));
//
//		JPanel zeile1 = new JPanel();
//		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));
//
//		JLabel l1 = new JLabel("jedes/alle");
//		l1.setPreferredSize(new Dimension(60, 20));
//		l1.setAlignmentY((float) 0.5);
//
//		JLabel l2 = new JLabel("Monat(e)");
//		l2.setPreferredSize(new Dimension(60, 20));
//		l2.setAlignmentY((float) 0.5);
//
//		zeile1.add(l1);
//		zeile1.add(Box.createHorizontalStrut(5));
//		zeile1.add(spin[2]);
//		zeile1.add(Box.createHorizontalStrut(10));
//		zeile1.add(l2);
//		zeile1.add(Box.createHorizontalGlue());
//
//		JPanel zeile2 = new JPanel();
//		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));
//
//		JPanel pane = new JPanel();
//		pane.setLayout(new GridLayout(2, 1));
//
//		mtArt[0] = new JRadioButton();
//		mtArt[1] = new JRadioButton();
//
//		ButtonGroup group = new ButtonGroup();
//		group.add(mtArt[0]);
//		group.add(mtArt[1]);
//
//		pane.add(mtArt[0]);
//		pane.add(mtArt[1]);
//
//		zeile2.add(pane);
//		zeile2.add(Box.createHorizontalGlue());
//
//		rechts.add(zeile1);
//		rechts.add(Box.createVerticalStrut(5));
//		rechts.add(zeile2);
//
//		typPane.add(typ[2]);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(rechts);
//		typPane.add(Box.createHorizontalGlue());
//
//		return typPane;
//	}
//
//	JPanel createTyp4() {
//		JPanel typPane = new JPanel();
//		typPane.setLayout(new BoxLayout(typPane, BoxLayout.X_AXIS));
//		typPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 5, 5),
//				new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 5,
//						5))));
//
//		JLabel l1 = new JLabel("jedes/alle");
//		l1.setPreferredSize(new Dimension(60, 20));
//		l1.setAlignmentY((float) 0.5);
//
//		JLabel l2 = new JLabel("Jahr(e)");
//		l2.setPreferredSize(new Dimension(60, 20));
//		l2.setAlignmentY((float) 0.5);
//
//		typPane.add(typ[3]);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(l1);
//		typPane.add(Box.createHorizontalStrut(5));
//		typPane.add(spin[3]);
//		typPane.add(Box.createHorizontalStrut(10));
//		typPane.add(l2);
//		typPane.add(Box.createHorizontalGlue());
//
//		return typPane;
//	}
//
//	// Bottom // Bottom // Bottom // Bottom // Bottom // Bottom // Bottom //
//	// Bottom //
//	JPanel createBottom() {
//		JPanel bottom = new JPanel();
//		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
//		bottom.setBorder(new EmptyBorder(15, 12, 0, 12));
//
//		JPanel left = new JPanel();
//		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
//
//		wkArt[0] = new JRadioButton("nur Werktags");
//		wkArt[0].setMnemonic('w');
//		wkArt[0].setSelected(true);
//		wkArt[1] = new JRadioButton("auch So + Feiertage");
//		wkArt[1].setMnemonic('s');
//
//		ButtonGroup group = new ButtonGroup();
//		group.add(wkArt[0]);
//		group.add(wkArt[1]);
//
//		left.add(wkArt[0]);
//		left.add(wkArt[1]);
//		left.add(Box.createVerticalGlue());
//
//		buttons = new ButtonPanel(false);
//		buttons.addActionListener(this);
//
//		JPanel right = new JPanel();
//		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
//
//		right.add(Box.createVerticalGlue());
//		right.add(buttons);
//
//		bottom.add(left);
//		bottom.add(Box.createHorizontalGlue());
//		bottom.add(right);
//
//		return bottom;
//	}
//
//	void setEnabled(int t, boolean status) {
//		spin[t].setEnabled(status);
//
//		if (t == 1) // wöchentlich
//		{
//			for (int i = 0; i < 7; i++)
//				dyBox[i].setEnabled(status);
//		} else if (t == 2) // monatlich
//		{
//			for (int i = 0; i < 2; i++)
//				mtArt[i].setEnabled(status);
//		}
//	}
//
//	void setBeginnText(String date) {
//		// Beginn setzen
//		Datum d = serie.getBeginn();
//
//		if (!d.getDate().equals(date)) {
//			d.setDatum(date);
//			datePanel[0].getDatumsfeld().setDate(date);
//
//			mtArt[0].setText("jeden " + d.getDay() + ". des Monats");
//			mtArt[1].setText("jeden " + d.getDayRelativ() + " des Monats");
//
//			if (selectedTyp != 1) {
//				dyBox[d.getWeekDay()].setSelected(true);
//			}
//		}
//	}
//
//	void setDyBoxes(int w) {
//		int count = 0;
//		w >>= 2;
//
//		while (w > 0) {
//			if (w % 2 == 1)
//				dyBox[count].setSelected(true);
//			else
//				dyBox[count].setSelected(false);
//
//			w >>= 1;
//			count++;
//		}
//	}
//
//	int getDyBoxes() {
//		int w = 0;
//
//		for (int i = 0; i < 7; i++) {
//			if (dyBox[i].isSelected())
//				w += Math.pow(2, i + 2);
//		}
//
//		return w;
//	}
//
//	void fill(Serie s) {
//		serie = s;
//		int sTyp = serie.getTyp();
//
//		// defaults
//		mtArt[0].setSelected(true);
//
//		if (sTyp >= 0) {
//			datePanel[0].getDatumsfeld().setDate(serie.getBeginn().getDate());
//			datePanel[1].getDatumsfeld().setDate(serie.getEnde().getDate());
//
//			if (serie.getWerktags()) {
//				wkArt[0].setSelected(true);
//			} else {
//				wkArt[1].setSelected(true);
//			}
//
//			switch (sTyp) {
//			case 0: // täglich
//
//				selectedTyp = 0;
//				break;
//
//			case 1: // monatlich absolut
//			case 2: // monatlich relativ
//
//				selectedTyp = 2;
//
//				if (sTyp == 1) {
//					mtArt[0].setSelected(true);
//				} else {
//					mtArt[1].setSelected(true);
//				}
//
//				break;
//
//			case 3: // jährlich
//
//				selectedTyp = 3;
//				break;
//
//			default: // wöchentlich
//
//				selectedTyp = 1;
//				setDyBoxes(sTyp);
//			}
//
//			for (int i = 0; i < 4; i++)
//				if (i != selectedTyp)
//					setEnabled(i, false);
//			typ[selectedTyp].setSelected(true);
//			spin[selectedTyp].setValue(serie.getFrequenz());
//
//			mtArt[0].setText("jeden " + serie.getBeginn().getDay()
//					+ ". des Monats");
//			mtArt[1].setText("jeden " + serie.getBeginn().getDayRelativ()
//					+ " des Monats");
//		}
//	}
//
//	void getAttribute() {
//		int t = 0;
//
//		// Ende
//
//		serie.getEnde().setDatum(datePanel[1].getDatumsfeld().getDate());
//
//		switch (selectedTyp) {
//		case 0: // täglich
//			t = 0;
//			break;
//
//		case 1: // wöchentlich
//			t = getDyBoxes();
//			break;
//
//		case 2: // monatlich
//			t = mtArt[0].isSelected() ? 1 : 2;
//			break;
//
//		case 3: // jährlich
//			t = 3;
//
//		default:
//			JTextArea area = new JTextArea(
//					"Sie müssen bitte in der Karteikarte \"Serie\" alles ausfüllen!");
//			area.setBackground(SystemColor.control);
//			area.setEditable(false);
//			JOptionPane optionPane = new JOptionPane(area,
//					JOptionPane.WARNING_MESSAGE);
//			JDialog dialogPane = optionPane.createDialog(parentFrame,
//					"Fehlermeldung");
//			dialogPane.show();
//			dialogPane.dispose();
//		}
//
//		// Typ
//		serie.setTyp(t);
//
//		// Frequenz
//		if (selectedTyp >= 0 && selectedTyp < 4) {
//			serie.setFrequenz((int) spin[selectedTyp].getValue());
//		}
//		// Werktags
//		serie.setWerktags(wkArt[0].isSelected());
//	}
//
//	boolean isSelected() {
//		return (selectedTyp >= 0);
//	}
//
//	Serie getSerie() {
//		return serie;
//	}
//
//	Rectangle getRectOK() {
//		return buttons.getScreenLocation(0);
//	}
//
//	Rectangle getRectAbbr() {
//		return buttons.getScreenLocation(1);
//	}
//
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e)
	{
//		String c = e.getActionCommand();
//		int i;
//
//		for (i = 0; i < 4; i++)
//		{
//			if (c.equals(typ[i].getText()))
//			{
//				if (selectedTyp < 0)
//					for (int j = 0; j < 4; j++)
//						setEnabled(j, false);
//				else
//					setEnabled(selectedTyp, false);
//
//				setEnabled(i, true);
//				selectedTyp = i;
//
//				break;
//			}
//		}
//
//		if (i == 4)
//			super.actionPerformed(e);
	}
}
