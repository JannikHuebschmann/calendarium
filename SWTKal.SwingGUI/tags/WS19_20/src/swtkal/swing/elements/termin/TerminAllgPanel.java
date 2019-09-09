package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

//import swtkal.domain.EintragsTyp;
//import swtkal.domain.Notifikation;
import swtkal.domain.Termin;
import swtkal.swing.elements.TimePanel;
import swtkal.swing.elements.Zahlenfeld;
import swtkal.swing.elements.ButtonPanel;
import swtkal.swing.elements.CustomBorder;

/////////////////////////////////////////////////////////////////////////////////////////////////
// TerminAllgPanel // TerminAllgPanel // TerminAllgPanel // TerminAllgPanel // TerminAllgPanel //
/////////////////////////////////////////////////////////////////////////////////////////////////

class TerminAllgPanel extends AllgemeinPanel implements FocusListener
{
	protected Termin termin;

	// Controls
	private TimePanel[] timePanel = new TimePanel[2];
	private Zahlenfeld dauer;
	private JCheckBox verschiebbar;

	TerminAllgPanel(JFrame f)
	{
		super(f);
		create();
	}

	void create()
	{

		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));
		pane.add(createDate());// nullpointer repariert
		pane.add(createDauer());
		pane.add(Box.createRigidArea(new Dimension(0, 5)));
		pane.add(createBeschreibung());
		pane.add(Box.createRigidArea(new Dimension(0, 5)));
//		pane.add(createBenachrichtigung());
		gui.add("Center", pane);
		gui.add("South", createBottom());
	}

	// Dauer // Dauer // Dauer // Dauer // Dauer // Dauer // Dauer // Dauer //
	// Dauer //
	JPanel createDauer()
	{
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(500, 30));

		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane.setBorder(new CompoundBorder(new EmptyBorder(0, 3, 0, 3),
				new CustomBorder(SystemColor.controlHighlight, false, false, true,
						true)));

		JLabel label = new JLabel("Dauer in Stunden: ");
		label.setPreferredSize(new Dimension(110, 20));
		label.setDisplayedMnemonic('s');

		dauer = new Zahlenfeld(3, 2);
		dauer.setPreferredSize(new Dimension(40, 20));
		dauer.setFocusAccelerator('s');
		dauer.addFocusListener(this);

		pane.add(label);
		pane.add(dauer);

		return pane;
	}

	// Datum // Datum // Datum // Datum // Datum // Datum // Datum // Datum //
	// Datum // Datum //
	JPanel createDate()
	{
		JPanel datum = new JPanel();
		datum.setLayout(new GridLayout(1, 2, 10, 0));
		datum.setPreferredSize(new Dimension(500, 85));
		JPanel date1 = new JPanel();
		date1.setLayout(new BoxLayout(date1, BoxLayout.Y_AXIS));
		date1.setBorder(new TitledBorder("Beginn"));
		date1.add((datePanel[0] = new DatePanel(parentFrame)).getGUI());
		date1.add((timePanel[0] = new TimePanel()).getGUI());
		// datePanel[0].getDatumsfeld().addFocusListener(this);johnny
		// timePanel[0].getZeitfeld().addFocusListener(this);johnny

		JPanel date2 = new JPanel();
		date2.setLayout(new BoxLayout(date2, BoxLayout.Y_AXIS));
		date2.setBorder(new TitledBorder("Ende"));
		date2.add((datePanel[1] = new DatePanel(parentFrame)).getGUI());
		date2.add((timePanel[1] = new TimePanel()).getGUI());
		// datePanel[1].getDatumsfeld().addFocusListener(this);jt
		// timePanel[1].getZeitfeld().addFocusListener(this);jt

		datum.add(date1);
		datum.add(date2);

		return datum;
	}

	// Bottom // Bottom // Bottom // Bottom // Bottom // Bottom // Bottom //
	// Bottom //
	JPanel createBottom()
	{
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.setBorder(new EmptyBorder(5, 12, 0, 11));

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

		verschiebbar = new JCheckBox("verschiebbar");
		verschiebbar.setMnemonic('v');
		serienFlag = new JCheckBox("Serientermin");
		serienFlag.setMnemonic('s');
		serienFlag.setEnabled(false); // changed by daniela esberger - 14.
		// juni 2005

		left.add(verschiebbar);
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

	void fill(Termin t)
	{
		// Termin setzen
		termin = t;

		if (termin.getBeginn().isCorrectDate())
		{
			// Beginn
			datePanel[0].getDatumsfeld().setDate(termin.getBeginn().getDateStr());

			if (termin.getBeginn().isCorrectTime())
			{
				timePanel[0].getZeitfeld().setTime(termin.getBeginn().getTimeStr());
			}
		}

		if (termin.getEnde().isCorrectDate())
		{
			// Ende
			datePanel[1].getDatumsfeld().setDate(termin.getEnde().getDateStr());

			if (termin.getEnde().isCorrectTime())
			{
				timePanel[1].getZeitfeld().setTime(termin.getEnde().getTimeStr());

				if (termin.getBeginn().isCorrect())
				{ // Dauer
					dauer.setValue(termin.getEnde().getHoursBetween(termin.getBeginn()));
				}
			}
		}

		// Allgemein
		kurzText.setText(termin.getKurzText());

//		if (termin.getTyp() != null)
//		{
//			for (int i = 0; i < eintragsTyp.getItemCount(); i++)
//			{
//				EintragsTyp typ = (EintragsTyp) eintragsTyp.getItemAt(i);
//				if (typ.getID() == termin.getTyp().getID())
//				{
//					eintragsTyp.setSelectedIndex(i);
//					break;
//				}
//			}
//		}
//		else
//		{ // eintragsTyp.setSelectedIndex(0);
//
//		}

		langText.setText(termin.getLangText());
//		ort.setText(termin.getOrt());
//		hyperlink.setText(termin.getHyperlink());
		verschiebbar.setSelected(termin.getVerschiebbar());

//		// Notifikationen
//		Notifikation[] nfkt = termin.getNotifikationen();
//
//		if (nfkt != null)
//		{
//			for (int i = 0; i < nfkt.length; i++)
//			{
//				nfktPanel[i].setNotifikation(nfkt[i]);
//			}
//		}
//
//		// Serie?
//		if (termin.getSerie() != null && termin.getSerie().getTyp() >= 0)
//		{
//			serienFlag.setSelected(true);
//			serienFlag.setEnabled(true); // changed by daniela esberger - 14.
//			// juni 2005
//		}
	}

	void setBeginn()
	{ // Beginn setzen
		termin.getBeginn().setDatum(datePanel[0].getDatumsfeld().getDate(),
				timePanel[0].getZeitfeld().getTime());
	}

	void setEnde()
	{ // Ende setzen
		termin.getEnde().setDatum(datePanel[1].getDatumsfeld().getDate(),
				timePanel[1].getZeitfeld().getTime());
	}

	void getAttribute()
	{
		setBeginn();
		setEnde();

		// Kurztext
		termin.setKurzText(kurzText.getText());

		// Eintragstyp
		// termin.setTyp (new EintragsTyp(eintragsTyp.getSelectedItem()));
//		termin.setTyp((EintragsTyp) eintragsTyp.getSelectedItem());

		// Langtext
		termin.setLangText(langText.getText());

//		// Ort
//		termin.setOrt(ort.getText());
//
//		// Hyperlink
//		termin.setHyperlink(hyperlink.getText());

		// Verschiebbar
		termin.setVerschiebbar(verschiebbar.isSelected());

//		// Notifikationen
//		if (getNfktCount() > 0)
//		{
//			Notifikation[] nfkt = new Notifikation[getNfktCount()];
//
//			for (int i = 0, count = 0; i < 3; i++)
//			{
//				Notifikation n = nfktPanel[i].getNotifikation();
//				if (n != null)
//				{
//					nfkt[count] = n;
//					count++;
//				}
//			}
//
//			termin.setNotifikationen(nfkt);
//		}
//		else
//			termin.setNotifikationen(null);
//
//		// Serie
//		if (!serienFlag.isSelected())
//		{
//
//			termin.setSerie(null);
//		}
	}

	// FocusListener ////////////////////////////////////////////////////
	public void focusGained(FocusEvent e)
	{}

	public void focusLost(FocusEvent e)
	{
//		// Dauer
//		if (e.getSource() == dauer)
//		{
//			setBeginn();
//
//			if (termin.getBeginn().isCorrect())
//			{
//				termin.getEnde().setDatum(
//						termin.getBeginn().addDauer(dauer.getValue()));
//
//				datePanel[1].getDatumsfeld().setDate(termin.getEnde().getDate());
//				timePanel[1].getZeitfeld().setTime(termin.getEnde().getTime());
//			}
//
//		}
//		else
//		{
//			// Beginn / Ende
//			setBeginn();
//			setEnde();
//
//			if (termin.getBeginn().isCorrectDate()
//					&& !termin.getEnde().isCorrectDate())
//			{
//				termin.getEnde().setDatum(termin.getBeginn().getDate());
//				datePanel[1].getDatumsfeld().setDate(termin.getBeginn().getDate());
//			}
//
//			if (termin.getBeginn().isCorrect() && termin.getEnde().isCorrect())
//			{
//				dauer
//						.setValue(termin.getEnde()
//								.getHoursBetween(termin.getBeginn()));
//			}
//		}
	}
}
