package client.eintrag; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

import basisklassen.ToDo;
import basisklassen.Serie;

//////////////////////////////////////////////////////////////////////////////////////////////////
// EditToDo // EditToDo // EditToDo // EditToDo // EditToDo // EditToDo // EditToDo // EditToDo //
//////////////////////////////////////////////////////////////////////////////////////////////////
class EditToDo extends EditEintrag {
	// ToDo
	private ToDo toDo;

	EditToDo(JFrame f) {
		parentFrame = f;

		allgemeinPane = new ToDoAllgPanel(parentFrame);
		allgemeinPane.addActionListener(this);
		allgemeinPane.addItemListener(this);

		teilnehmerPane = new TeilnehmerPanel(parentFrame);
		teilnehmerPane.addActionListener(this);

		serienPane = new SerienPanel(parentFrame); // +parentFrame (changed by
		// de - 13.06.2005)
		serienPane.addActionListener(this);
	}

	void start(ToDo t) {
		toDo = t;

		// Interface
		createGUI();

		// AllgemeinPanel
		allgemeinPane.fill(toDo);
		oldEintragsTyp = allgemeinPane.getEintragsTyp();

		// TeilnehmerPanel
		teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
		teilnehmerPane.fill(toDo.getTeilnehmer());

		// SerienPanel
		if (toDo.getSerie() == null) {
			serienPane.fill(new Serie());
		} else {
			serienPane.fill(toDo.getSerie());
		}
	}

	boolean checkInput() {
		String msg = "";
		int count = 0;
		int len = 0;
//		char c = 34;

		allgemeinPane.getAttribute();

		teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
		teilnehmerPane.getAttribute();

		if (allgemeinPane.getSerienFlag()) {
			serienPane.getAttribute();
			toDo.setSerie(serienPane.getSerie());
		}

		// //////////////////////////////////////////////////////////////////////////////////////////
		// Checks // Checks // Checks // Checks // Checks // Checks // Checks //
		// Checks // Checks //
		// //////////////////////////////////////////////////////////////////////////////////////////

		// AllgemeinPanel
		if (!toDo.getErinnernAb().isCorrectDate()) {
			String text = "Bitte geben Sie ein vollständiges und korrektes Datum an, ab dem Sie erinnert werden wollen!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (!toDo.getFälligPer().isCorrectDate()) {
			String text = "Bitte geben Sie ein vollständiges und korrektes Fälligkeitsdatum an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		// added by daniela esberger - 19. juni 2005
		if (toDo.getErinnernAb().isGreater(toDo.getFälligPer()) == 1) {
			String text = "Bitte achten Sie darauf, dass \"Erinnern ab\" vor \"Fällig per\" liegt!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;

		}

		// TeilnehmerPanel
		if (toDo.getTeilnehmer() == null) {
			String text = "Bitte geben Sie zumindest einen Teilnehmer an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		// SerienPanel
		if (allgemeinPane.getSerienFlag()) {
			if (!toDo.getSerie().getEnde().isCorrectDate()) {
				String text = "Bitte geben Sie ein vollständiges und korrektes Datum fuer das Serienende an!\n";
				len = Math.max(len, text.length());
				msg += text;
				count++;
			}

			// added by daniela esberger - 19. juni 2005
			if (toDo.getFälligPer().isGreater(toDo.getSerie().getEnde()) == 1) {
				String text = "Bitte achten Sie darauf, dass das Serienende nach dem Fälligkeitsdatum liegt!\n";
				len = Math.max(len, text.length());
				msg += text;
				count++;
			}
		}

		if (count > 0) {
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

	// ChangeListener // ChangeListener // ChangeListener // ChangeListener //
	// ChangeListener //
	public void stateChanged(ChangeEvent e) {
		if (tabs.getSelectedIndex() == 0) {
			// AllgemeinPanel
			if (serienPane.isSelected())
				allgemeinPane.setSerienFlag(true);
			else
				allgemeinPane.setSerienFlag(false);
		} else if (tabs.getSelectedIndex() == 1) {
			// TeilnehmerPanel
			allgemeinPane.datePanel[0].closeCalendar();
			allgemeinPane.datePanel[1].closeCalendar();

			teilnehmerPane.updateColumns(allgemeinPane.getNfkts());
			teilnehmerPane.setEintragsTyp(allgemeinPane.getEintragsTyp());
		} else if (tabs.getSelectedIndex() == 2) {
			// SerienPanel
			allgemeinPane.setErinnernAb();

			if (!toDo.getErinnernAb().isCorrectDate()) {
				String text = "Bitte geben Sie zuerst ein vollständiges und korrektes Datum an,\nab dem Sie erinnert werden wollen!";

				JTextArea area = new JTextArea(text);
				area.setBackground(SystemColor.control);
				area.setEditable(false);
				area.setColumns(63);
				area.setRows(2);

				JOptionPane optionPane = new JOptionPane(area,
						JOptionPane.WARNING_MESSAGE);
				JDialog dialogPane = optionPane.createDialog(parentFrame,
						"Fehlermeldungen");

				dialogPane.show();
				dialogPane.dispose();

				tabs.setSelectedIndex(oldTabIndex);

			} else {
				if (!allgemeinPane.getSerienFlag()) {
					allgemeinPane.setSerienFlag(true);
					serienPane.setBeginnText(toDo.getErinnernAb().getDate());
				}
			}
		}

		oldTabIndex = tabs.getSelectedIndex();
	}
}
