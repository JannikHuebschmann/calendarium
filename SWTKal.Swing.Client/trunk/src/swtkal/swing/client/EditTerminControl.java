package swtkal.swing.client;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

//import java.awt.SystemColor;
import java.awt.event.*;
import javax.swing.*;

//import java.text.SimpleDateFormat;
import java.util.Date;

import swtkal.domain.Datum;
import swtkal.domain.Termin;
import swtkal.exceptions.TerminException;
//import basisklassen.Konflikt;
//import basisklassen.Notifikation;
//import data.Data;
import swtkal.swing.elements.termin.EditTermin;

//////////////////////////////////////////////////////////////////////////////////////
// EditTerminControl // EditTerminControl // EditTerminControl // EditTerminControl //
//////////////////////////////////////////////////////////////////////////////////////

public class EditTerminControl implements ActionListener
{
	protected JFrame parentFrame;
	protected EditTermin editTermin;

	protected SwingClient client;
	protected Termin termin;
	protected boolean edit;			// Flag

//	// TerminVorschlag
//	private TerminVorschlag vorschlag = null;

	public EditTerminControl(JFrame f, SwingClient c)
	{
		this(f, c, new Datum(new Date()));
	}

	public EditTerminControl(JFrame f, SwingClient c, Datum d)
	{
		edit = false;
		parentFrame = f;
		client = c;
		editTermin = new EditTermin(f);
		editTermin.addActionListener(this);

		termin = new Termin(client.getUser(), d.addDauer(0.0), d.addDauer(1.0));
		editTermin.start(termin);
		editTermin.setTitle("Termin eintragen");
	}

	public EditTerminControl(JFrame f, SwingClient c, Termin t)
	{
		edit = false;
		parentFrame = f;
		client = c;
		editTermin = new EditTermin(f);
		editTermin.addActionListener(this);

		termin = t;
		editTermin.start(termin);
		editTermin.setTitle("Termin eintragen");
	}
//
//	// Edit Instructor
//	public EditTerminControl(JFrame f, Termin t) {
//		edit = true;
//		parentFrame = f;
//
//		editTermin = new EditTermin(f);
//		editTermin.addActionListener(this);
//
//		// Start
//		termin = t;
//
//		if (termin.getOwner().getID() == Data.user.getID()) {
//			// Owner darf den Termin editieren
//			editTermin.start(termin);
//			editTermin.setTitle("Termin editieren");
//
//		} else {
//			editTermin.start(termin);
//			editTermin.setTitle("Termin-Owner: "
//					+ termin.getOwner().getNameLang());
//
//			editTermin.disableGUI();
//		}
//	}
//
	public JInternalFrame getGUI()
	{
		return editTermin.getGUI();
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private boolean createTermin()
	{
		try
		{
			client.getServer().insert(termin);
		}
		catch (TerminException e)
		{
			e.printStackTrace();
		}
		
		return true;

//		Vector konflikte = Data.termine.getKonflikte(termin);
//
//		if (konflikte.size() == 0)
//		{
//
//			((SwingClient) parentFrame).getServer() .termine.create(termin);
//
//			return true;
//
//		}
//		else
//		{
//			String msg = "Konflikte:\n\n";
//
//			// Konflikt
//			Enumeration e = konflikte.elements();
//			while (e.hasMoreElements())
//			{
//				Konflikt konflikt = (Konflikt) e.nextElement();
//				String vb = konflikt.isVerschiebbar() ? "verschiebbar"
//						: "nicht verschiebbar";
//
//				msg += konflikt.getPerson().getNameLang() + ": "
//						+ konflikt.getBeginn() + " - " + konflikt.getEnde() + ", "
//						+ vb + '\n';
//
//			}
//
//			switch (showKonflikteDialog(msg))
//			{
//			case 0: // Zurueck
//				return false;
//
//			case 1: // Eintragen
//
//				Data.termine.create(termin);
//				return true;
//
//			default: // Suchen
//
//				vorschlag = new TerminVorschlag(parentFrame);
//				vorschlag.start(termin);
//
//				return false;
//			}
//		}
	}

//	// update // update // update // update // update // update // update //
//	// update // update //
//	private boolean updateTermin() {
//		Vector konflikte = Data.termine.getKonflikte(termin);
//
////		Notifikation[] nfkt = termin.getNotifikationen();
//
//		if (konflikte.size() == 0) {
//			Data.termine.update(termin);
//			return true;
//
//		} else {
//			String msg = "Konflikte:\n\n";
//
//			// Konflikt
//			Enumeration e = konflikte.elements();
//			while (e.hasMoreElements()) {
//				Konflikt konflikt = (Konflikt) e.nextElement();
//				String vb = konflikt.isVerschiebbar() ? "verschiebbar"
//						: "nicht verschiebbar";
//
//				msg += konflikt.getPerson().getNameLang() + ": "
//						+ konflikt.getBeginn() + " - " + konflikt.getEnde()
//						+ ", " + vb + '\n';
//
//			}
//
//			switch (showKonflikteDialog(msg)) {
//			case 0: // Zurueck
//				return false;
//
//			case 1: // Eintragen
//
//				Data.termine.update(termin);
//				return true;
//
//			default: // Suchen
//
//				vorschlag = new TerminVorschlag(parentFrame);
//				vorschlag.start(termin);
//
//				return false;
//			}
//		}
//	}
//
//	private int showKonflikteDialog(String msg) {
//		Object[] options = { "zurueck", "trotzdem eintragen", "suchen" };
//
//		JTextArea area = new JTextArea(msg);
//
//		StringTokenizer st = new StringTokenizer(msg, "\n");
//		int len = 0, count = 0;
//
//		while (st.hasMoreTokens()) {
//			len = Math.max(len, st.nextToken().length());
//			count++;
//		}
//
//		area.setBackground(SystemColor.control);
//		area.setEditable(false);
//		area.setColumns(len);
//		area.setRows(count);
//
//		JOptionPane optionPane = new JOptionPane(area,
//				JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null,
//				options, options[2]);
//
//		JDialog dialogPane = optionPane.createDialog(parentFrame,
//				"Termin eintragen");
//
//		dialogPane.show();
//		dialogPane.dispose();
//
//		Object selectedValue = optionPane.getValue();
//
//		if (selectedValue != null) {
//			if (options[1].equals(selectedValue)) {
//				return 1;
//			} else if (options[2].equals(selectedValue)) {
//				return 2;
//			}
//		}
//
//		return 0;
//	}
//
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("OK"))
		{
			// alle Eingaben vollständig?
			if (editTermin.checkInput())
			{
				if (!edit)
				{
					if (createTermin())
					{
//						Vector failed = editTermin.getFailedPersonen();
//						if (failed != null)
//						{
//							Data.termine.sendMissingRight(termin, failed);
//						}
//
						try
						{
							editTermin.getGUI().setClosed(true);
							// dafuer sorgen, dass die Tagesansicht erneuert wird
							// dazu wird ein entsprechendes ActionEvent weitergereicht
							client.actionPerformed(new ActionEvent(this, 0, "Tagesansicht"));
						}
						catch (java.beans.PropertyVetoException ex)
						{
							ex.printStackTrace();
						}
					}

				}
//				else
//				{
//					if (updateTermin())
//					{
//						Vector failed = editTermin.getFailedPersonen();
//						if (failed != null)
//						{
//							Data.termine.sendMissingRight(termin, failed);
//						}
//
//						try
//						{
//							editTermin.getGUI().setClosed(true);
//						}
//						catch (java.beans.PropertyVetoException ex)
//						{}
//					}
//				}
			}
		}
		else if (e.getActionCommand().equals("Abbrechen"))
		{
			try
			{
				editTermin.getGUI().setClosed(true);
			}
			catch (java.beans.PropertyVetoException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
