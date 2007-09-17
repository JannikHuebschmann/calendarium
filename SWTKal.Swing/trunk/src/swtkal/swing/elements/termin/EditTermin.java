package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import javax.swing.*;
//import javax.swing.event.*;

import swtkal.domain.Termin;
//import swtkal.domain.Serie;

////////////////////////////////////////////////////////////////////////////////////////////////////
// EditTermin // EditTermin // EditTermin // EditTermin // EditTermin // EditTermin // EditTermin //
////////////////////////////////////////////////////////////////////////////////////////////////////
public class EditTermin extends EditEintrag
{
	protected Termin termin;

	public EditTermin(JFrame f)
	{
		parentFrame = f;
		allgemeinPane = new TerminAllgPanel(parentFrame);
		allgemeinPane.addActionListener(this);
		allgemeinPane.addItemListener(this);

		teilnehmerPane = new TeilnehmerPanel(parentFrame);
		teilnehmerPane.addActionListener(this);

		serienPane = new SerienPanel(parentFrame);
		serienPane.addActionListener(this);
	}

	public void start(Termin t)
	{
		termin = t;

		createContent();

		// AllgemeinPanel
		allgemeinPane.fill(termin);
//		oldEintragsTyp = allgemeinPane.getEintragsTyp();

//		// TeilnehmerPanel
//		teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
//		teilnehmerPane.fill(termin.getTeilnehmer());
//
//		// SerienPanel
//		if (termin.getSerie() == null) {
//			serienPane.fill(new Serie());
//		} else {
//			serienPane.fill(termin.getSerie());
//		}
	}
//
	@SuppressWarnings("deprecation")
	public boolean checkInput()
	{
		String msg = "";
		int count = 0;
		int len = 0;
		// char c = 34;

		allgemeinPane.getAttribute();

//		teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
//		teilnehmerPane.getAttribute();
//
//		if (allgemeinPane.getSerienFlag())
//		{
//			serienPane.getAttribute();
//			termin.setSerie(serienPane.getSerie());
//		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// Checks // Checks // Checks // Checks // Checks // Checks // Checks //
		// Checks // Checks //
		// //////////////////////////////////////////////////////////////////////////////////////////

		// AllgemeinPanel
		if (!termin.getBeginn().isCorrectDate())
		{
			String text = "Bitte geben Sie ein vollständiges und korrektes Datum fuer den Beginn an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (!termin.getBeginn().isCorrectTime())
		{
			String text = "Bitte geben Sie eine vollständige und korrekte Zeit fuer den Beginn an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (!termin.getEnde().isCorrectDate())
		{
			String text = "Bitte geben Sie ein vollständiges und korrektes Datum fuer das Ende an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (!termin.getEnde().isCorrectTime())
		{
			String text = "Bitte geben Sie eine vollständige und korrekte Zeit fuer das Ende an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		// added by daniela esberger - 19. juni 2005
		if (termin.getBeginn().isGreater(termin.getEnde()) == 1)
		{
			String text = "Bitte achten Sie darauf, dass das Ende nach dem Beginn des Termins liegt!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		// TeilnehmerPanel
		if (termin.getTeilnehmer() == null)
		{
			String text = "Bitte geben Sie zumindest einen Teilnehmer an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

//		// SerienPanel
//		if (allgemeinPane.getSerienFlag())
//		{
//			if (!termin.getSerie().getEnde().isCorrectDate())
//			{
//				String text = "Bitte geben Sie ein vollständiges und korrektes Datum für das Serienende an!\n";
//				len = Math.max(len, text.length());
//				msg += text;
//				count++;
//			}
//
//			// added by daniela esberger - 19. juni 2005
//			if (termin.getSerie().getBeginn().isGreater(
//					termin.getSerie().getEnde()) == 1)
//			{
//				String text = "Bitte achten Sie darauf, dass das Ende der Serie nach dem Beginn liegt!\n";
//				len = Math.max(len, text.length());
//				msg += text;
//				count++;
//			}
//		}

		if (count > 0)
		{
			msg = msg.substring(0, msg.length() - 1);

			JTextArea area = new JTextArea(msg);
			area.setBackground(SystemColor.control);
			area.setEditable(false);
			area.setColumns(len);
			area.setRows(count);

			JOptionPane optionPane = new JOptionPane(area,
					JOptionPane.WARNING_MESSAGE);
			JDialog dialogPane = optionPane.createDialog(parentFrame,
					"Fehlermeldungen");

			dialogPane.show();
			dialogPane.dispose();

			return false;
		}

		return true;
	}

	//	// ChangeListener // ChangeListener // ChangeListener // ChangeListener //
//	// ChangeListener //
//	public void stateChanged(ChangeEvent e) {
//		if (tabs.getSelectedIndex() == 0) {
//			// AllgemeinPanel
//			if (serienPane.isSelected())
//				allgemeinPane.setSerienFlag(true);
//			else
//				allgemeinPane.setSerienFlag(false);
//		} else if (tabs.getSelectedIndex() == 1) {
//			// TeilnehmerPanel
//			allgemeinPane.datePanel[0].closeCalendar();
//			allgemeinPane.datePanel[1].closeCalendar();
//
//			teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
//			teilnehmerPane.setEintragsTyp(allgemeinPane.getEintragsTyp());
//		} else if (tabs.getSelectedIndex() == 2) {
//			// SerienPanel
//			allgemeinPane.setBeginn();
//
//			if (!termin.getBeginn().isCorrectDate()) {
//				String text = "Bitte geben Sie zuerst ein vollständiges und korrektes Datum\nfuer den Beginn des Termins an!";
//
//				JTextArea area = new JTextArea(text);
//				area.setBackground(SystemColor.control);
//				area.setEditable(false);
//				area.setColumns(60);
//				area.setRows(2);
//
//				JOptionPane optionPane = new JOptionPane(area,
//						JOptionPane.WARNING_MESSAGE);
//				JDialog dialogPane = optionPane.createDialog(parentFrame,
//						"Fehlermeldungen");
//
//				dialogPane.show();
//				dialogPane.dispose();
//
//				tabs.setSelectedIndex(oldTabIndex);
//
//			} else {
//				if (!allgemeinPane.getSerienFlag()) {
//					allgemeinPane.setSerienFlag(true);
//					serienPane.setBeginnText(termin.getBeginn().getDate());
//				}
//
//			}
//		}
//
//		oldTabIndex = tabs.getSelectedIndex();
//	}
}
