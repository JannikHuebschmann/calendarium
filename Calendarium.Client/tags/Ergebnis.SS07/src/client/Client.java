package client; //
//////////////////

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.rmi.*;

import event.*;
import data.Data;
import client.ansicht.*;
import client.eintrag.*;
import client.admin.*;
import client.admin.typ.*;
import client.admin.user.*;
import client.admin.recht.*;
import client.admin.gruppe.*;
import client.utility.darstellung.CustomBorder;

//////////////////////////////////////////////////////////////////////////////////
//Client // Client // Client // Client // Client // Client // Client // Client //
//////////////////////////////////////////////////////////////////////////////////

public class Client extends JFrame implements ActionListener, EventConstants {
	private static final long serialVersionUID = 8297828400383040229L;

	private final int INITIAL_WIDTH = 850;

	private final int INITIAL_HEIGHT = 700;

	// LayeredPane
	private JLayeredPane layer;

	// ContentPane
	private JPanel contentPane;

	// StatusLabel
	private JLabel statusLabel;

//	// MessageString
//	private String msgString;

	// CalendariumListener
	private static CalendariumListenerImpl listener;

	// Nachrichten
	private Noticias notes;

	// InternalFrames
	private Hashtable frames = new Hashtable();

	// Properties
	Properties userProperties;

	@SuppressWarnings("unchecked")
	public Client() {
		setSize(INITIAL_WIDTH, INITIAL_HEIGHT);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - INITIAL_WIDTH / 2, screenSize.height
				/ 2 - INITIAL_HEIGHT / 2);

		// Look & Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception exc) {
			System.err.println("Error loading L&F: " + exc);
		}

		// connect
		//Initialisation of the connect Dialog
		ConnectDialog connect = new ConnectDialog(this);
		//Set the UserProperties
		userProperties = connect.getProperties();

		setTitle("Calendarium - " + Data.user.getVorname() + ' '
				+ Data.user.getNachname());

		// EventListener
		try {
			listener = new CalendariumListenerImpl(this);
			listener.addObserver(notes = new Noticias());
			listener.addObserver(new Watcher());

		} catch (RemoteException exc) {
			exc.printStackTrace();
			System.exit(0);
		}

		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());

		create();

		// DefaultWindow // DefaultWindow // DefaultWindow // DefaultWindow //
		// DefaultWindow
		Tagesansicht tagesansicht = new Tagesansicht(this, statusLabel,
				new Date());
		JInternalFrame gui = tagesansicht.getGUI();

		// added by daniela esberger - 21. juni 2005
		gui.setVisible(true);
		frames.put("Tag", gui);

		// Listener
		listener.addObserver(tagesansicht);

		try {
			gui.setSelected(true);
		} catch (java.beans.PropertyVetoException ex) {
		}

		layer.add(gui, 0);
	}

	private void create() {
		// Menue
		JMenuBar mbar = new JMenuBar();
		mbar.setBackground(Color.lightGray);
		mbar.setBorder(new EtchedBorder());

		// Einträge Menue
		JMenu mEinträge = new JMenu("Eintragen");
		mEinträge.setMnemonic('E');
		mbar.add(mEinträge);

		JMenuItem iTermin = new JMenuItem("Termin eintragen");
		iTermin.setMnemonic('T');
		iTermin.setActionCommand("Termin");
		iTermin.addActionListener(this);
		mEinträge.add(iTermin);

		JMenuItem iToDo = new JMenuItem("ToDo eintragen");
		iToDo.setMnemonic('D');
		iToDo.setActionCommand("ToDo");
		iToDo.addActionListener(this);
		mEinträge.add(iToDo);

		JMenuItem iClear = new JMenuItem("Datensäuberung");
		iClear.setMnemonic('S');
		iClear.setActionCommand("Löschen");
		iClear.addActionListener(this);
		mEinträge.add(iClear);

		// Ansehen Menue
		JMenu mAnsehen = new JMenu("Ansehen");
		mAnsehen.setMnemonic('A');
		mbar.add(mAnsehen);

		JMenuItem iTag = new JMenuItem("Tag");
		iTag.setMnemonic('T');
		iTag.setActionCommand("Tag");
		iTag.addActionListener(this);
		mAnsehen.add(iTag);

		JMenuItem iWoche = new JMenuItem("Woche");
		iWoche.setMnemonic('W');
		iWoche.setActionCommand("Woche");
		iWoche.addActionListener(this);
		mAnsehen.add(iWoche);

		JMenuItem iMonat = new JMenuItem("Monat");
		iMonat.setMnemonic('M');
		iMonat.setActionCommand("Monat");
		iMonat.addActionListener(this);
		mAnsehen.add(iMonat);

		JMenuItem iJahr = new JMenuItem("Jahr");
		iJahr.setMnemonic('J');
		iJahr.setActionCommand("Jahr");
		iJahr.addActionListener(this);
		mAnsehen.add(iJahr);

		// Verwalten Menue
		JMenu mVerwalten = new JMenu("Verwalten");
		mVerwalten.setMnemonic('V');
		mbar.add(mVerwalten);

		JMenuItem iPerson = new JMenuItem("Persönliche Daten");
		iPerson.setMnemonic('P');
		iPerson.setActionCommand("User");
		iPerson.addActionListener(this);
		mVerwalten.add(iPerson);

		JMenuItem iTypen = new JMenuItem("Eintragstypen");
		iTypen.setMnemonic('E');
		iTypen.setActionCommand("Typen");
		iTypen.addActionListener(this);
		mVerwalten.add(iTypen);

		JMenuItem iGruppen = new JMenuItem("Gruppen");
		iGruppen.setMnemonic('G');
		iGruppen.setActionCommand("Gruppen");
		iGruppen.addActionListener(this);
		mVerwalten.add(iGruppen);

		JMenuItem iRechte = new JMenuItem("Berechtigungen");
		iRechte.setMnemonic('B');
		iRechte.setActionCommand("Rechte");
		iRechte.addActionListener(this);
		mVerwalten.add(iRechte);

		// Info Menue
		JMenu mInfo = new JMenu("Info");
		mInfo.setMnemonic('I');
		mbar.add(mInfo);

		JMenuItem iRechtsInfo = new JMenuItem("Erhaltene Rechte");
		iRechtsInfo.setMnemonic('E');
		iRechtsInfo.setActionCommand("RechtsInfo");
		iRechtsInfo.addActionListener(this);
		mInfo.add(iRechtsInfo);

		JMenuItem iNachrichten = new JMenuItem("Nachrichten");
		iNachrichten.setMnemonic('N');
		iNachrichten.setActionCommand("Nachrichten");
		iNachrichten.addActionListener(this);
		mInfo.add(iNachrichten);

		mInfo.addSeparator();

		JMenuItem iInfo = new JMenuItem("Info...");
		iInfo.setMnemonic('o');
		iInfo.setActionCommand("About");
		iInfo.addActionListener(this);
		mInfo.add(iInfo);

		// Beenden Menue
		JMenu mExit = new JMenu("Beenden");
		mExit.setMnemonic('B');
		mbar.add(mExit);

		JMenuItem iExit = new JMenuItem("Beenden");
		iExit.setMnemonic('b');
		iExit.setActionCommand("Beenden");
		iExit.addActionListener(this);
		mExit.add(iExit);

		// add
		getRootPane().setJMenuBar(mbar);

		// LayeredPane
		layer = new JLayeredPane();
		layer.setBackground(contentPane.getBackground());

		JPanel statusPane = new JPanel();
		statusPane.setLayout(new BorderLayout());
		statusPane.setBorder(new CustomBorder(Color.white, true, false, false,
				false));
		statusPane.setPreferredSize(new Dimension(1000, 20));

		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(JLabel.LEFT);
		statusPane.add("Center", statusLabel);

		contentPane.add("Center", layer);
		contentPane.add("South", statusPane);

		addWindowListener(new WindowEventHandler());
	}

	public JLayeredPane getLayeredPane() {
		return layer;
	}

	public CalendariumListenerImpl getListener() {
		return listener;
	}

	class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			Data.msgServer.disconnect();
			System.exit(0);
		}
	}

	public static void main(String args[]) {
		try {
			String vers = System.getProperty("java.version");
			if (vers.compareTo("1.1.2") < 0) {
				System.err.println("!!!WARNING: Swing must be run with a "
						+ "1.1.2 or higher version VM!!!");
			}

			Client c = new Client();
			c.setVisible(true);

			Data.msgServer.addCalendariumListener(listener);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		JInternalFrame gui;

		if (e.getSource().getClass() == JMenuItem.class) {

			// EditTermin --------------------------------------------------
			if (c.equals("Termin")) {
				if (!frames.containsKey(c)) {
					EditTerminControl termin = new EditTerminControl(this);// nullpointer
					// 15.5.2k4

					gui = termin.getGUI();
					frames.put(c, gui);
				} else {
					gui = (JInternalFrame) frames.get(c);
					if (gui.isClosed()) {
						EditTerminControl termin = new EditTerminControl(this);
						gui = termin.getGUI();
						frames.put(c, gui);
					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);
				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// EditToDo -----------------------------------------------------
			else if (c.equals("ToDo")) {
				if (!frames.containsKey(c)) {
					EditToDoControl toDo = new EditToDoControl(this);
					gui = toDo.getGUI();
					frames.put(c, gui);
				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						EditToDoControl toDo = new EditToDoControl(this);
						gui = toDo.getGUI();
						frames.put(c, gui);
					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);

					gui.setSelected(true);
				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// DeleteUntilDate --------------------- Datensaeuberung
			else if (c.equals("Löschen")) {
				if (!frames.containsKey(c)) {
					DeleteUntilDate del = new DeleteUntilDate(this);
					gui = del.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						DeleteUntilDate del = new DeleteUntilDate(this);
						gui = del.getGUI();
						frames.put(c, gui);

					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// Exit
			else if (c.equals("Beenden")) {
				System.exit(0);
			}

			// Tagesansicht
			else if (c.equals("Tag")) {
				if (!frames.containsKey(c)) {
					Tagesansicht tagesansicht = new Tagesansicht(this,
							statusLabel, new Date());
					gui = tagesansicht.getGUI();
					frames.put(c, gui);

					// Listener
					listener.addObserver(tagesansicht);
					gui = (JInternalFrame) frames.get(c);// by johnny
					gui.setVisible(true);// by johnny

				} else {
					gui = (JInternalFrame) frames.get(c);
					gui.setVisible(true);
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
			}

			// Wochenansicht
			else if (c.equals("Woche")) {
				if (!frames.containsKey(c)) {
					Wochenansicht wochenansicht = new Wochenansicht(this,
							statusLabel, new Date());
					gui = wochenansicht.getGUI();
					frames.put(c, gui);

					// Listener
					listener.addObserver(wochenansicht);
					gui = (JInternalFrame) frames.get(c);// by johnny
					gui.setVisible(true);// by johnny

				} else {
					gui = (JInternalFrame) frames.get(c);
					gui.setVisible(true);
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
			}

			// Monatsansicht
			else if (c.equals("Monat")) {
				if (!frames.containsKey(c)) {
					Monatsansicht monatsansicht = new Monatsansicht(this,
							statusLabel, new Date());
					gui = monatsansicht.getGUI();
					frames.put(c, gui);

					// Listener
					listener.addObserver(monatsansicht);
					gui = (JInternalFrame) frames.get(c);// by johnny
					gui.setVisible(true);// by johnny

				} else {
					gui = (JInternalFrame) frames.get(c);
					gui.setVisible(true);
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
			}

			// Jahresansicht
			else if (c.equals("Jahr")) {
				if (!frames.containsKey(c)) {
					Jahresansicht jahresansicht = new Jahresansicht(this,
							statusLabel);
					gui = jahresansicht.getGUI();
					frames.put(c, gui);

					// Listener
					listener.addObserver(jahresansicht);
					gui = (JInternalFrame) frames.get(c);// by johnny
					gui.setVisible(true);// by johnny

				} else {
					gui = (JInternalFrame) frames.get(c);
					gui.setVisible(true);
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
			}

			// EditUser
			else if (c.equals("User")) {
				if (!frames.containsKey(c)) {
					EditUserControl editUser = new EditUserControl(Data.user);
					gui = editUser.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						EditUserControl editUser = new EditUserControl(
								Data.user);
						gui = editUser.getGUI();
						frames.put(c, gui);

					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// EditTypen
			else if (c.equals("Typen")) {
				if (!frames.containsKey(c)) {
					EditTypenControl editTypen = new EditTypenControl(this,
							Data.user);
					gui = editTypen.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						EditTypenControl editTypen = new EditTypenControl(this,
								Data.user);
						gui = editTypen.getGUI();
						frames.put(c, gui);
					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);
				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// EditGruppen
			else if (c.equals("Gruppen")) {
				if (!frames.containsKey(c)) {
					EditGruppenControl editGruppen = new EditGruppenControl(
							this);
					gui = editGruppen.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						EditGruppenControl editGruppen = new EditGruppenControl(
								this);
						gui = editGruppen.getGUI();
						frames.put(c, gui);

					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// EditRechte
			else if (c.equals("Rechte")) {
				if (!frames.containsKey(c)) {
					EditRechteControl editRechte = new EditRechteControl();
					gui = editRechte.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						EditRechteControl editRechte = new EditRechteControl();
						gui = editRechte.getGUI();
						frames.put(c, gui);

					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// RechtsInfo
			else if (c.equals("RechtsInfo")) {
				if (!frames.containsKey(c)) {
					RechtsInfo rechtsInfo = new RechtsInfo();
					gui = rechtsInfo.getGUI();
					frames.put(c, gui);

				} else {
					gui = (JInternalFrame) frames.get(c);

					if (gui.isClosed()) {
						RechtsInfo rechtsInfo = new RechtsInfo();
						gui = rechtsInfo.getGUI();
						frames.put(c, gui);

					} else
						gui.moveToFront();
				}

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				gui.setVisible(true);// by johnny
			}

			// Nachrichten
			else if (c.equals("Nachrichten")) {
				gui = notes.getGUI();
				gui.setVisible(true);

				try {
					if (gui.isIcon())
						gui.setIcon(false);
					gui.setSelected(true);

				} catch (java.beans.PropertyVetoException ex) {
				}

				layer.add(gui, 0);
				gui = (JInternalFrame) frames.get(c);// by johnny
				try {
					gui.setVisible(true);// by johnny
				} catch (RuntimeException e1) {

				}

			}

			// About
			else if (c.equals("About")) {
				String text = "Calendarium Version 2.0\nAutor: Christian Stoiber\n\n";
				
				//changed for version 3.0 by daniela esberger and johannes theuermann
				text += "Calendarium Version 3.0\n" +
						"Änderungen von: \nDaniela Esberger und \nJohannes Theuermann\n" +
						"\nStand: Juni 2005\n\n" 
						+ "JDK " + System.getProperty("java.version");

				JPanel pane = new JPanel();
				pane.setBorder(new EmptyBorder(10, 10, 10, 10));
				pane.setLayout(new BorderLayout());

				DefaultStyledDocument doc = new DefaultStyledDocument();

				SimpleAttributeSet attributeSet = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attributeSet, "Dialog");
			    
				StyleConstants.setFontSize(attributeSet, 14);

				try {
					doc.insertString(0, text, attributeSet);
				} catch (BadLocationException ex) {
				}

				JTextPane textPane = new JTextPane(doc);
				textPane.setBackground(SystemColor.control); 
				textPane.setEnabled(false);

				pane.add("Center", textPane);

				JOptionPane optionPane = new JOptionPane(pane,
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.DEFAULT_OPTION,
						loadImageIcon("calendar.gif"));

				JDialog dialogPane = optionPane.createDialog(this,
						"Info ueber Calendarium");
				dialogPane.setSize(new Dimension(270, 330));

				dialogPane.show();
				dialogPane.dispose();
			}
		}
	}

	// ImageIcon
	public static ImageIcon loadImageIcon(String s) {
		return new ImageIcon("images/" + s);
	}

	// Property
	public String getProperty(String s) {
		return (String) userProperties.get(s);
	}

	// Watcher fuer Statuszeile
	class Watcher implements Observer {
		public void update(Observable obj, Object arg) {
			CalendariumEvent evt = (CalendariumEvent) arg;

			if (evt.getEventID() == MESSAGE_EVT) {
				statusLabel.setText(((MessageEvent) evt).getTitle());
			}
		}
	}
}
