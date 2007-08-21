package client.admin.feiertag; //
/////////////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

import basisklassen.*;
import client.gui.CharacterTextField;
import client.utility.*;
import client.utility.darstellung.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
// EditFeiertag // EditFeiertag // EditFeiertag // EditFeiertag // EditFeiertag // EditFeiertag //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class EditFeiertag extends ListenerForActions {
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	// private JComponent gui = new JInternalFrame(); //by johnny
	private JPanel gui = new JPanel();

	// Dialog
	private JDialog dialog;

	// Controls
	private DatePanel datePanel;

	private CharacterTextField bezeichnung;

	// Eintragstyp
	private Feiertag tag = null;

	public EditFeiertag(JFrame f) {
		parentFrame = f;

		gui.setLayout(new BorderLayout(0, 10));
		gui.setBorder(new EmptyBorder(10, 5, 0, 5));

		create();
	}

	void create() {
		JPanel pane = new JPanel();

		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		JPanel zeile = new JPanel();
		zeile.setLayout(new BoxLayout(zeile, BoxLayout.X_AXIS));

		JLabel label = new JLabel("Bezeichnung:");
		label.setPreferredSize(new Dimension(87, 20));
		label.setDisplayedMnemonic('B');

		bezeichnung = new CharacterTextField(20);
		bezeichnung.setPreferredSize(new Dimension(50, 20));
		bezeichnung.setFocusAccelerator('B');

		zeile.add(Box.createHorizontalStrut(5));
		zeile.add(label);
		zeile.add(bezeichnung);
		zeile.add(Box.createHorizontalStrut(5));

		pane.add(zeile);
		pane.add(Box.createVerticalStrut(5));
		datePanel = new DatePanel(parentFrame);// by johnny da hat er das
		// problem
		pane.add(datePanel.getGUI());// by johnny
		// Buttons // Buttons // Buttons // Buttons // Buttons // Buttons //
		// Buttons //
		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);
		gui.add("Center", pane);
		gui.add("South", buttons);
	}

	void fill(Feiertag t) {
		tag = t;
		bezeichnung.setText(tag.getBezeichnung());
		datePanel.getDatumsfeld().setDate(tag.getDate());
	}

	void start() {
		if (tag != null) {
			dialog = new JDialog(parentFrame, "Feiertag bearbeiten", true);
		} else {
			dialog = new JDialog(parentFrame, "Feiertag neu anlegen", true);
		}

		dialog.setSize(260, 150);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add("Center", gui);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - 260) / 2,
				(screenSize.height - 150) / 2);
		dialog.show();
	}

	void close() {
		dialog.dispose();
	}

	Feiertag getFeiertag() {
		if (tag != null) {
			tag.setDate(datePanel.getDatumsfeld().getDateGreg());
			tag.setBezeichnung(bezeichnung.getText());

		} else {
			tag = new Feiertag(datePanel.getDatumsfeld().getDateGreg(),
					bezeichnung.getText());
		}

		return tag;
	}

	boolean checkInput() {
		String msg = "";
		int count = 0, len = 0;

		Datum date = new Datum(datePanel.getDatumsfeld().getDate());

		if (bezeichnung.getText().length() == 0) {
			String text = "Sie haben keinen Bezeichnung fuer den Feiertag vergeben!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		if (!date.isCorrectDate()) {
			String text = "Bitte geben Sie ein vollständiges und korrektes Datum an!\n";
			len = Math.max(len, text.length());
			msg += text;
			count++;
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
					"Fehlermeldung");

			dialogPane.show();
			dialogPane.dispose();

			return false;

		} else
			return true;
	}
}
