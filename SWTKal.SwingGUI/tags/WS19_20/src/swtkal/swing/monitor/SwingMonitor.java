package swtkal.swing.monitor;
//Achtung: Klasse in wesentlichen Teilen aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

import swtkal.monitor.Monitor;
import swtkal.swing.elements.person.EditUserControl;

public class SwingMonitor extends Monitor implements ActionListener
{
	public static void main(String[] args)
	{
		new SwingMonitor().frame.setVisible(true);
	}

	protected JFrame frame;
	protected final int INITIAL_WIDTH  = 500;
	protected final int INITIAL_HEIGHT = 400;
	
	JLayeredPane layer = new JLayeredPane();
   // cache for internal frames
   protected Hashtable<String, JInternalFrame> frames = new Hashtable<String, JInternalFrame>();

	protected SwingMonitor()
	{
		server = swtkal.server.Server.getServer();
		server.startServer();

		// Look & Feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exc)
		{
			System.err.println("Error loading L&F: " + exc);
		}

		frame = new JFrame();
		frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width/2 - INITIAL_WIDTH/2,
				      screenSize.height/2 - INITIAL_HEIGHT/2);
		frame.setTitle("SWTKal-SwingMonitor");

		createMenu();
		createContent();

		frame.addWindowListener(new WindowEventHandler());
	}

	protected void createMenu()
	{
		JMenuBar mbar = new JMenuBar();

		JMenu mMonitor = new JMenu("Monitoring");
		mbar.add(mMonitor);

			JMenuItem iLogins = new JMenuItem("aktuelle Benutzer");
			iLogins.setActionCommand("Logins");
			iLogins.setEnabled(false);
			iLogins.addActionListener(this);
			mMonitor.add(iLogins);
	
			mMonitor.addSeparator();
	
			JMenuItem iStart = new JMenuItem("Start Server");
			iStart.setActionCommand("Start");
			iStart.addActionListener(this);
			mMonitor.add(iStart);
	
			JMenuItem iClose = new JMenuItem("Stopp Server");
			iClose.setActionCommand("Stop");
			iClose.addActionListener(this);
			mMonitor.add(iClose);

		JMenu mVerwalten = new JMenu("Verwalten");
		mbar.add(mVerwalten);

			JMenuItem iUser = new JMenuItem("Benutzer");
			iUser.setActionCommand("User");
			iUser.addActionListener(this);
			mVerwalten.add(iUser);
	
			JMenuItem iGruppen = new JMenuItem("Gruppen");
			iGruppen.setActionCommand("Gruppen");
			iGruppen.setEnabled(false);
			iGruppen.addActionListener(this);
			mVerwalten.add(iGruppen);
	
			JMenuItem iTypen = new JMenuItem("Typen");
			iTypen.setActionCommand("Typen");
			iTypen.setEnabled(false);
			iTypen.addActionListener(this);
			mVerwalten.add(iTypen);
	
			JMenuItem iFeiertage = new JMenuItem("Feiertage");
			iFeiertage.setActionCommand("Feiertage");
			iFeiertage.setEnabled(false);
			iFeiertage.addActionListener(this);
			mVerwalten.add(iFeiertage);
	
			JMenuItem iFeiertageInit = new JMenuItem("Feiertage initialisieren");
			iFeiertageInit.setActionCommand("Feiertage init");
			iFeiertageInit.setEnabled(false);
			iFeiertageInit.addActionListener(this);
			mVerwalten.addSeparator();
			mVerwalten.add(iFeiertageInit);

		JMenu mExit = new JMenu("Beenden");
		mbar.add(mExit);

			JMenuItem iExit = new JMenuItem("Beenden");
			iExit.setActionCommand("Exit");
			iExit.addActionListener(this);
			mExit.add(iExit);

		frame.getRootPane().setJMenuBar(mbar);
	}

	protected void createContent()
	{
		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		layer.setBackground(contentPane.getBackground());
		contentPane.add("Center", layer);
	}

	public void actionPerformed(ActionEvent e)
	{
		String c = e.getActionCommand();

		if (e.getSource().getClass() == JMenuItem.class)
		{
			if (c.equals("Logins"))
			{
				
			}
			else if (c.equals("Start"))
			{
				server.startServer();
			}
			else if (c.equals("Stop"))
			{
				server.stopServer();
			}
			else if (c.equals("User"))
			{
				benutzerVerwalten();
			}
			else if (c.equals("Exit"))
			{
				server.stopServer();
				System.exit(0);
			}
		}
	}
	
	protected void benutzerVerwalten()
	{
		if (!frames.containsKey("User"))
		{
			EditUserControl editUser = new EditUserControl(server, frame);
			frames.put("User", editUser.getGUI());
		}

		JInternalFrame gui = frames.get("User");
		gui.setVisible(true);
		try
		{
			if (gui.isIcon())
				gui.setIcon(false);
			gui.setSelected(true);

		}
		catch (java.beans.PropertyVetoException ex)
		{}

		layer.add(gui, 0);
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
