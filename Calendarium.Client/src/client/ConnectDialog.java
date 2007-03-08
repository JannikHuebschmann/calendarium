package client; //
//////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import data.Data;
import basisklassen.Person;
import client.utility.darstellung.ButtonPanel;

///////////////////////////////////////////////////////////////////////////////////////
// ConnectDialog // ConnectDialog // ConnectDialog // ConnectDialog // ConnectDialog //
///////////////////////////////////////////////////////////////////////////////////////

class ConnectDialog implements ActionListener {
	// parentFrame
	private JFrame parentFrame;

	// Data
	private Data daten;

	// Dialog
	private JDialog dialog;

	// Controls
	private JPasswordField pwf;

	private JTextField txt, port, host;

	// Properties
	private Properties properties;

	// missCount
	private int missCount = 0;

	ConnectDialog(JFrame f) {
		parentFrame = f;

		dialog = new JDialog(parentFrame, "Calendarium-Anmeldung", true);
		dialog.setSize(230, 180);

		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.addWindowListener(new WindowEventHandler());

		properties = setProperties();

		centerDialog(dialog);
		create();
	}

	private Properties setProperties() {
		Properties defaultProps = new Properties();
		InetAddress thisMachine = null;

		try {
			thisMachine = InetAddress.getLocalHost();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// DefaultsProperties

		defaultProps.put("RMIServerPort", "2005");
		defaultProps.put("BrowserPath", "");
		defaultProps.put("MtPaneYears", "-1 .. +5");
		defaultProps.put("RMIServerName", thisMachine.getHostName());

		// UserProperties
		Properties applicationProps = (Properties) defaultProps.clone();

		try {
			FileInputStream in = new FileInputStream("client/userProperties");
			applicationProps.load(in);
			in.close();

		} catch (IOException e) {
			try {
				FileOutputStream out = new FileOutputStream(
						"client/userProperties");
				applicationProps.save(out, "--- Client Settings ---");
				out.close();

			} catch (IOException ex) {
			}
		}

		return applicationProps;
	}

	void create() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(4, 1));
		pane.setBorder(new EmptyBorder(10, 10, 0, 10));

		JPanel srv = new JPanel();
		srv.setLayout(new BoxLayout(srv, BoxLayout.X_AXIS));

		JLabel l1 = new JLabel("Hostname: ");
		l1.setPreferredSize(new Dimension(70, 20));
		l1.setDisplayedMnemonic('h');

		host = new JTextField();
		host.setText((String) properties.get("RMIServerName"));
		host.setFocusAccelerator('h');

		srv.add(l1);
		srv.add(host);

		JPanel pNr = new JPanel();
		pNr.setLayout(new BoxLayout(pNr, BoxLayout.X_AXIS));

		JLabel l2 = new JLabel("Port: ");
		l2.setPreferredSize(new Dimension(70, 20));
		l2.setDisplayedMnemonic('p');

		port = new JTextField();
		port.setText((String) properties.get("RMIServerPort"));
		port.setFocusAccelerator('p');

		pNr.add(l2);
		pNr.add(port);

		JPanel kzl = new JPanel();
		kzl.setLayout(new BoxLayout(kzl, BoxLayout.X_AXIS));

		JLabel l3 = new JLabel("Ihr Kuerzel: ");
		l3.setPreferredSize(new Dimension(70, 20));
		l3.setDisplayedMnemonic('k');

		txt = new JTextField();
		txt.setFocusAccelerator('k');

		kzl.add(l3);
		kzl.add(txt);

		JPanel pwd = new JPanel();
		pwd.setLayout(new BoxLayout(pwd, BoxLayout.X_AXIS));

		JLabel l4 = new JLabel("Passwort: ");
		l4.setPreferredSize(new Dimension(70, 20));
		l4.setDisplayedMnemonic('p');

		pwf = new JPasswordField();
		pwf.setFocusAccelerator('p');
		pwf.setActionCommand("OK");
		pwf.addActionListener(this);

		pwd.add(l4);
		pwd.add(pwf);

		pane.add(srv);
		pane.add(pNr);
		pane.add(kzl);
		pane.add(pwd);

		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		dialog.getContentPane().add("Center", pane);
		dialog.getContentPane().add("South", buttons);

		dialog.show();
	}

	void showDialog(String text) {
		JTextArea area = new JTextArea(text);

		StringTokenizer st = new StringTokenizer(text, "\n");
		int len = 0, count = 0;

		while (st.hasMoreTokens()) {
			len = Math.max(len, st.nextToken().length());
			count++;
		}

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
	}

	void centerDialog(Component c) {
		Dimension screenSize = c.getToolkit().getScreenSize();
		Dimension size = c.getSize();

		screenSize.height = screenSize.height / 2;
		screenSize.width = screenSize.width / 2;

		size.height = size.height / 2;
		size.width = size.width / 2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		c.setLocation(x, y);
	}

	class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Abbrechen")) {
			System.exit(0);

		} else {
			// Properties speichern
			saveProperties();

			if (missCount == 0) {
				String hostname = "rmi://"
						+ (String) properties.get("RMIServerName") + ':'
						+ (String) properties.get("RMIServerPort") + '/';

				daten = new Data(parentFrame, hostname);
			}

			// anmelden
			Object result = Data.msgServer.connect(
					txt.getText().toUpperCase(), pwf.getText());
			String text = "";

			if (result != null) {
				if (result instanceof Person) {
					daten.setUser((Person) result);
					dialog.dispose();

				} else {
					// Error // Error // Error // Error // Error // Error //
					// Error //
					switch (((Integer) result).intValue()) {
					case -1:
						if (missCount < 2) {
							text = "Ungueltiges Passwort!";

						} else {
							text = "Falls Sie Ihr Passwort vergessen haben,\n"
									+ "rufen Sie das Monitor-Tool auf und\n"
									+ "geben Ihr Passwort neu ein!\n";
						}

						missCount++;
						showDialog(text);

						if (missCount > 2)
							System.exit(0);
						break;

					case -2:
						showDialog("Sie haben das Programm bereits gestartet!");
						System.exit(0);
					}

				}

			} else
				showDialog("Keine Verbindung zum Server!");
		}
	}

	private void saveProperties() {
		if (!host.getText().equals((String) properties.get("RMIServerName"))
				|| !port.getText().equals(
						(String) properties.get("RMIServerPort"))) {
			properties.put("RMIServerName", host.getText());
			properties.put("RMIServerPort", port.getText());

			try {
				FileOutputStream out = new FileOutputStream(
						"client/userProperties");
				properties.save(out, "--- Client Settings ---");
				out.close();

			} catch (IOException ex) {
			}
		}
	}

	public Properties getProperties() {
		return properties;
	}
}
