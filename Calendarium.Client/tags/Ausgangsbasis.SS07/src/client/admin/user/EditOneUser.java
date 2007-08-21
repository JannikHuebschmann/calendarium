package client.admin.user; //
/////////////////////////////

import basisklassen.Person;
import client.gui.CharacterTextField;
import client.gui.Zahlenfeld;
import client.utility.ListenerForActions;
import client.utility.darstellung.ButtonPanel;
import data.Data;
import data.Shared;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

////////////////////////////////////////////////////////////////////////////////////////////
// EditOneUser // EditOneUser // EditOneUser // EditOneUser // EditOneUser // EditOneUser //
////////////////////////////////////////////////////////////////////////////////////////////

public class EditOneUser extends ListenerForActions implements Shared {
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JComponent gui;

	// Dialog
	private JDialog dialog;

	// Person
	private Person person = null;

	// Controls
	private CharacterTextField kuerzel, vorname, nachname;

	private JPasswordField[] passwort = new JPasswordField[2];

	private JComboBox bevorzugt;

	private JTextField email;

	private Zahlenfeld fax;

	public EditOneUser() {
		gui = new JInternalFrame("Persönliche Daten", true, true, false, true);

		gui.setSize(340, 370);
		gui.setPreferredSize(gui.getSize());
		((JInternalFrame) gui).getContentPane().setLayout(new BorderLayout());

		create();
	}

	public EditOneUser(JFrame f) {
		parentFrame = f;

		gui = new JPanel();
		gui.setLayout(new BorderLayout());

		create();
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));

		JPanel login = new JPanel();
		login.setBorder(new CompoundBorder(new TitledBorder("Login"),
				new EmptyBorder(0, 5, 0, 5)));
		login.setLayout(new BoxLayout(login, BoxLayout.X_AXIS));

		JPanel li = new JPanel();
		li.setLayout(new BoxLayout(li, BoxLayout.Y_AXIS));

		JPanel zeile1 = new JPanel();
		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));

		JLabel label = new JLabel("Kuerzel:");
		label.setPreferredSize(new Dimension(75, 20));
		label.setDisplayedMnemonic('K');

		kuerzel = new CharacterTextField(5);
		kuerzel.setPreferredSize(new Dimension(50, 20));
		kuerzel.setFocusAccelerator('K');

		zeile1.add(label);
		zeile1.add(kuerzel);
		zeile1.add(Box.createHorizontalGlue());

		JPanel zeile2 = new JPanel();
		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));

		label = new JLabel("Passwort:");
		label.setPreferredSize(new Dimension(75, 20));
		label.setDisplayedMnemonic('P');

		passwort[0] = new JPasswordField();
		passwort[0].setFocusAccelerator('P');

		zeile2.add(label);
		zeile2.add(passwort[0]);

		JPanel zeile3 = new JPanel();
		zeile3.setLayout(new BoxLayout(zeile3, BoxLayout.X_AXIS));

		label = new JLabel("Bestätigung:");
		label.setPreferredSize(new Dimension(75, 20));
		label.setDisplayedMnemonic('B');

		passwort[1] = new JPasswordField();
		passwort[1].setFocusAccelerator('B');

		zeile3.add(label);
		zeile3.add(passwort[1]);

		li.add(zeile1);
		li.add(Box.createVerticalStrut(5));
		li.add(zeile2);
		li.add(Box.createVerticalStrut(5));
		li.add(zeile3);
		li.add(Box.createVerticalStrut(5));

		JLabel re = new JLabel(client.Client.loadImageIcon("secure1.gif"));
		re.setPreferredSize(new Dimension(80, 45));
		re.setHorizontalAlignment(JLabel.CENTER);
		re.setVerticalAlignment(JLabel.CENTER);

		login.add(li);
		login.add(Box.createHorizontalStrut(5));
		login.add(re);

		JPanel name = new JPanel();
		name.setBorder(new CompoundBorder(new TitledBorder("Name"),
				new EmptyBorder(0, 5, 0, 5)));
		name.setLayout(new BoxLayout(name, BoxLayout.Y_AXIS));

		zeile1 = new JPanel();
		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));

		label = new JLabel("Vorname: ");
		label.setPreferredSize(new Dimension(75, 20));
		label.setDisplayedMnemonic('V');

		vorname = new CharacterTextField(20);
		vorname.setFocusAccelerator('V');

		zeile1.add(label);
		zeile1.add(vorname);

		zeile2 = new JPanel();
		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));

		label = new JLabel("Nachname: ");
		label.setPreferredSize(new Dimension(75, 20));
		label.setDisplayedMnemonic('N');

		nachname = new CharacterTextField(30);
		nachname.setFocusAccelerator('N');

		zeile2.add(label);
		zeile2.add(nachname);

		name.add(zeile1);
		name.add(Box.createVerticalStrut(5));
		name.add(zeile2);
		name.add(Box.createVerticalStrut(5));

		JPanel erreichbar = new JPanel();
		erreichbar.setBorder(new CompoundBorder(new TitledBorder(
				"Erreichbarkeit"), new EmptyBorder(0, 5, 0, 5)));
		erreichbar.setLayout(new BoxLayout(erreichbar, BoxLayout.Y_AXIS));

		zeile1 = new JPanel();
		zeile1.setLayout(new BoxLayout(zeile1, BoxLayout.X_AXIS));

		label = new JLabel("EMail-Addresse:");
		label.setPreferredSize(new Dimension(100, 20));
		label.setVerticalAlignment(JLabel.CENTER);
		label.setDisplayedMnemonic('E');

		email = new JTextField();
		email.setFocusAccelerator('E');

		zeile1.add(label);
		zeile1.add(email);

		zeile2 = new JPanel();
		zeile2.setLayout(new BoxLayout(zeile2, BoxLayout.X_AXIS));

		label = new JLabel("Fax-Nummer: ");
		label.setPreferredSize(new Dimension(100, 20));
		label.setVerticalAlignment(JLabel.CENTER);
		label.setDisplayedMnemonic('F');

		fax = new Zahlenfeld(12);
		fax.setFocusAccelerator('F');

		zeile2.add(label);
		zeile2.add(fax);

		zeile3 = new JPanel();
		zeile3.setLayout(new BoxLayout(zeile3, BoxLayout.X_AXIS));

		label = new JLabel("Bevorzugte Notifikationsform: ");
		label.setPreferredSize(new Dimension(170, 20));
		label.setVerticalAlignment(JLabel.CENTER);
		label.setDisplayedMnemonic('E');

		bevorzugt = new JComboBox();
		for (int i = 0; i < NFKT_TYP.length; i++) {
			bevorzugt.addItem(NFKT_TYP[i]);
		}

		zeile3.add(label);
		zeile3.add(bevorzugt);

		erreichbar.add(zeile1);
		erreichbar.add(Box.createVerticalStrut(5));
		erreichbar.add(zeile2);
		erreichbar.add(Box.createVerticalStrut(5));
		erreichbar.add(zeile3);
		erreichbar.add(Box.createVerticalStrut(5));

		pane.add(login);
		pane.add(Box.createVerticalStrut(5));
		pane.add(name);
		pane.add(Box.createVerticalStrut(5));
		pane.add(erreichbar);

		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		if (gui instanceof JInternalFrame) {
			((JInternalFrame) gui).getContentPane().add("Center", pane);
			((JInternalFrame) gui).getContentPane().add("South", buttons);

			kuerzel.setEditable(false);

		} else {
			((JPanel) gui).add("Center", pane);
			((JPanel) gui).add("South", buttons);
		}
	}

	void fill(Person p) {
		person = p;

		kuerzel.setText(person.getKuerzel());
		passwort[0].setText(person.getPasswort());
		passwort[1].setText(person.getPasswort());
		vorname.setText(person.getVorname());
		nachname.setText(person.getNachname());
		email.setText(person.getEmailAdresse());
		fax.setText(person.getFaxNummer());
		bevorzugt.setSelectedIndex(person.getVorzugsNfkt());
	}

	void start() {
		if (person != null) {
			dialog = new JDialog(parentFrame, "Benutzer bearbeiten", true);
		} else {
			dialog = new JDialog(parentFrame, "Benutzer neu anlegen", true);
		}

		dialog.setSize(340, 370);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add("Center", gui);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - 340) / 2,
				(screenSize.height - 370) / 2);
		dialog.show();
	}

	JComponent getGUI() {
		return gui;
	}

	void closeDialog() {
		dialog.dispose();
	}

	void closeFrame() {
		((JInternalFrame) gui).dispose();
	}

	Person getPerson() {
		if (person != null) {
			person.setKuerzel(kuerzel.getText().toUpperCase());
			person.setNachname(nachname.getText());
			person.setVorname(vorname.getText());
			person.setEmailAdresse(email.getText());
			person.setFaxNummer(fax.getText());
			person.setPasswort(passwort[0].getText());
			person.setVorzugsNfkt(bevorzugt.getSelectedIndex());

		} else {
			person = new Person(kuerzel.getText().toUpperCase(), nachname
					.getText(), vorname.getText(), email.getText(), fax
					.getText(), passwort[0].getText(), bevorzugt
					.getSelectedIndex());
		}

		return person;
	}

	boolean checkInput() {
		String kbez = kuerzel.getText().toUpperCase();

		String msg = "";
		int count = 0;
		int len = 0;
		char c = 34;

		// Kuerzel bereits vorhanden
		if ((person == null && Data.personen.contains(kbez) || (person != null
				&& Data.personen.contains(kbez) && !person.getKuerzel().equals(
				kbez)))) {
			String text = "Das Kuerzel "
					+ c
					+ kbez
					+ c
					+ " existiert bereits, bitte vergeben Sie ein anderes Kuerzel!\n";

			len = Math.max(len, text.length());
			msg += text;
			count++;
		}

		// Passwörter unterschiedlich
		if (!passwort[0].getText().equals(passwort[1].getText())) {
			String text = "Die Paßwörter sind nicht ident. Bitte tragen Sie das Paßwort nochmals in beide Textfelder ein!\n";
			passwort[0].setText("");
			passwort[1].setText("");

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
