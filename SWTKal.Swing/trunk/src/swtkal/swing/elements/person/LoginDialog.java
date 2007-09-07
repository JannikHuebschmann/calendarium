package swtkal.swing.elements.person;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.*;
import java.net.*;
import java.util.*;

import swtkal.domain.Person;
import swtkal.exceptions.PersonException;
import swtkal.server.Server;
import swtkal.swing.elements.ButtonPanel;

public class LoginDialog implements ActionListener
{
	protected JFrame parentFrame;
	protected Server server;
	protected JDialog dialog;

	private JPasswordField pwf;
	protected JTextField txt, port, host;

	protected Properties properties;			// loginProperties
	protected Person user;
	protected int missCount = 0;				// zaehlt Fehlversuche beim Login

	public LoginDialog(JFrame f, Server s)
	{
		parentFrame = f;
		server = s;

		properties = setProperties();

		dialog = new JDialog(parentFrame, "SWTKal-Anmeldung", true);
		dialog.setSize(230, 180);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.addWindowListener(new WindowEventHandler());

		centerDialog(dialog);
		createContent();
	}

	public Properties getProperties()
	{
		return properties;
	}

	public Person getUser() throws PersonException
	{
		if (user!=null)
			return user;
		else
			throw new PersonException("Benutzer ist noch nicht eingeloggt!");
	}

	/**
	 * Setzt die Parameter, die zum Verbindungsaufbau genutzt werden. Es werden
	 * Default-Parameter gesetzt, die ggf. durch Werte aus der Datei
	 * connectionProperties ueberschrieben werden.
	 * 
	 * @return Properties Gibt die genutzten Verbindungsparameter zurueck.
	 */
	protected Properties setProperties()
	{
		Properties props = new Properties();
		InetAddress thisMachine = null;

		try
		{
			thisMachine = InetAddress.getLocalHost();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			server.stopServer();
			System.exit(0);
		}

		// DefaultsProperties
		props.put("RMIServerPort", "2005");
		props.put("BrowserPath", "");
		props.put("MtPaneYears", "-1 .. +5");
		props.put("RMIServerName", thisMachine.getHostName());

		// UserProperties aus der Datei loginProperties.xml
		try
		{
			FileInputStream in = new FileInputStream("loginProperties.xml");
			props.loadFromXML(in);
			in.close();
		}
		catch (IOException e)
		{
			try
			{
				FileOutputStream out = new FileOutputStream("loginProperties.xml");
				props.storeToXML(out, "--- Client Login Settings ---");
				out.close();
			}
			catch (IOException ex)
			{}
		}

		return props;
	}

	protected void saveProperties()
	{
		if (!host.getText().equals((String) properties.get("RMIServerName"))
				|| !port.getText().equals((String) properties.get("RMIServerPort")))
		{
			properties.put("RMIServerName", host.getText());
			properties.put("RMIServerPort", port.getText());

			try
			{
				FileOutputStream out = new FileOutputStream("loginProperties.xml");
				properties.storeToXML(out, "--- Client Login Settings ---");
				out.close();
			}
			catch (IOException ex)
			{}
		}
	}

	protected void centerDialog(Component c)
	{
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

	protected void createContent()
	{
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
		l4.setDisplayedMnemonic('w');

		pwf = new JPasswordField();
		pwf.setFocusAccelerator('w');
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

		dialog.setVisible(true);
	}

	protected void showDialog(String text)
	{
		JTextArea area = new JTextArea(text);

		StringTokenizer st = new StringTokenizer(text, "\n");
		int len = 0, count = 0;

		while (st.hasMoreTokens())
		{
			len = Math.max(len, st.nextToken().length());
			count++;
		}

		area.setEditable(false);
		area.setColumns(len);
		area.setRows(count);

		JOptionPane optionPane = new JOptionPane(area,
				JOptionPane.WARNING_MESSAGE);
		JDialog dialogPane = optionPane
				.createDialog(parentFrame, "Fehlermeldung");

		dialogPane.setVisible(true);
		dialogPane.dispose();
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Abbrechen"))
		{
			server.stopServer();
			System.exit(0);
		}
		else
		{
			saveProperties();

			// anmelden
			try
			{
				this.user = server.authenticate(txt.getText().toUpperCase(), pwf.getText());
				dialog.dispose();
			}
			catch (PersonException exception)
			{
				String text = "";
				if (missCount < 2)
				{
					text = exception.getMessage();
				}
				else
				{
					text = "Falls Sie Ihr Passwort vergessen haben,\n"
							+ "rufen Sie das Monitor-Tool auf und\n"
							+ "geben Ihr Passwort erneut ein!";
				}

				missCount++;
				showDialog(text);

				if (missCount > 2)
				{
					server.stopServer();
					System.exit(0);
				}
			}
		}
	}

	class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			server.stopServer();
			System.exit(0);
		}
	}
}
