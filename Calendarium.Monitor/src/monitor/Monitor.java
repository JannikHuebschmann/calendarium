package monitor; //
///////////////////

import interfaces.DBInterface;
import interfaces.MessageServerInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import server.Server;
import basisklassen.Person;
import client.admin.feiertag.EditFeiertageControl;
import client.admin.gruppe.EditGruppenControl;
import client.admin.typ.EditTypenControl;
import client.admin.user.EditUserControl;
import data.Data;
import feiertaginit.InitFeiertage;

//////////////////////////////////////////////////////////////////////////////////////////
// Monitor // Monitor // Monitor // Monitor // Monitor // Monitor // Monitor // Monitor //
//////////////////////////////////////////////////////////////////////////////////////////

public class Monitor extends JFrame implements ActionListener
{
        private static final long serialVersionUID = -5610825950818622565L;
		private final int INITIAL_WIDTH = 500;
        private final int INITIAL_HEIGHT = 400;

    // LayeredPane
    private JLayeredPane layer;

    // Hostname
    private String hostname;

    // ServerStart
    private boolean serverStart = false;

    // InternalFrames
    private Hashtable frames = new Hashtable();
    
    //to create and delete tables
    DBInterface dbi;
    
    //boolean if db server was started or not
    private boolean dbserver=false;

    // Properties
    Properties userProperties;

    public Monitor() throws RemoteException
    {
        setSize(INITIAL_WIDTH, INITIAL_HEIGHT);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation(screenSize.width/2 - INITIAL_WIDTH/2,
                                screenSize.height/2 - INITIAL_HEIGHT/2);

        setTitle("Monitor");

        // Look & Feel
        try
        {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            } catch (Exception exc)
            {   System.err.println("Error loading L&F: " + exc);
            }

        if((hostname = getHostName()) != null)
        {
            try
            {   
            	@SuppressWarnings("unused")
				MessageServerInterface mi = (MessageServerInterface) Naming.lookup(hostname + "MessageServer");
            	
            } catch(Exception e) {}
            
            
            try {
            	dbi = (DBInterface) Naming.lookup(hostname + "DBSetRemote");
            	//hier kommt man nur her wenn man das DBSetRemote Objekt gefunden hat
            	//das ist nur der fall wenn der DB Server gestartet wurde
				dbserver=true;
			} catch (Exception e1) {}
            
            

        }

        if(hostname == null)
        {
            @SuppressWarnings("unused")
			Server server = new Server();
            hostname = getHostName();
            serverStart = true;
        }

        Data daten = new Data(this, hostname);
        Person p = new Person(-1,"Admin","Administrator","Admin");
        daten.setUser(p);

        create();
    }

    private String getHostName()
    {
        String hostName = null;

        try
        {   userProperties = new Properties();

            FileInputStream in = new FileInputStream("monitor/monitorProperties");
            userProperties.load(in);
            in.close();

            hostName =  "rmi://" + (String) userProperties.get("RMIServerName")
                           + ':' + (String) userProperties.get("RMIServerPort") + '/';

        } catch(IOException e) {}

        return hostName;
    }

    private void create()
    {
        // Menue
        JMenuBar mbar = new JMenuBar();
        mbar.setBackground(Color.lightGray);
        mbar.setBorder(new EtchedBorder());

        // User Menue
        JMenu mMonitor = new JMenu("Monitoring");
        mMonitor.setMnemonic('m');
        mbar.add(mMonitor);

        JMenuItem iLogins = new JMenuItem("Logins anzeigen");
        iLogins.setMnemonic('l');
        iLogins.setActionCommand("Logins");
        iLogins.addActionListener(this);
        mMonitor.add(iLogins);

        // local oder remote?
        try
            {        	
        	InetAddress thisMachine = InetAddress.getLocalHost();
            if(hostname.indexOf(thisMachine.getHostAddress()) >= 0||
            		hostname.indexOf(thisMachine.getHostName())>=0||
					hostname.indexOf("localhost")>=0||
					hostname.indexOf("127.0.0.1")>=0)
            {
            	mMonitor.addSeparator();

                JMenuItem iClose = new JMenuItem("ShutDown Server");
                iClose.setMnemonic('s');
                iClose.setActionCommand("Close");
                iClose.addActionListener(this);
                mMonitor.add(iClose);

                JMenu mVerwalten = new JMenu("Verwalten");
                mVerwalten.setMnemonic('v');
                mbar.add(mVerwalten);

                JMenuItem iUser = new JMenuItem("Benutzer");
                iUser.setMnemonic('u');
                iUser.setActionCommand("User");
                iUser.addActionListener(this);
                mVerwalten.add(iUser);

                JMenuItem iGruppen = new JMenuItem("Gruppen");
                iGruppen.setMnemonic('g');
                iGruppen.setActionCommand("Gruppen");
                iGruppen.addActionListener(this);
                mVerwalten.add(iGruppen);

                JMenuItem iTypen = new JMenuItem("Typen");
                iTypen.setMnemonic('t');
                iTypen.setActionCommand("Typen");
                iTypen.addActionListener(this);
                mVerwalten.add(iTypen);

                JMenuItem iFeiertage = new JMenuItem("Feiertage");
                iFeiertage.setMnemonic('f');
                iFeiertage.setActionCommand("Feiertage");
                iFeiertage.addActionListener(this);
                mVerwalten.add(iFeiertage);

                JMenuItem iFeiertageInit = new JMenuItem("Feiertage initialisieren");
                iFeiertageInit.setMnemonic('i');
                iFeiertageInit.setActionCommand("Feiertage init");
                iFeiertageInit.setToolTipText("Fügt alle Feiertage aus der \"feiertaginit\\feiertage.txt\" in die Datenbank ein, der Server muss danach neu gestartet werden!");
                iFeiertageInit.addActionListener(this);
                mVerwalten.addSeparator();
                mVerwalten.add(iFeiertageInit);
                
                //nur wenn der db server gestartet wurde
                if(dbserver)
                {
                    JMenu mDatabase = new JMenu("Datenbank");
                    mDatabase.setMnemonic('d');
                    mbar.add(mDatabase);

                    JMenuItem iTabellenAnlegen = new JMenuItem("Tabellen anlegen");
                    iTabellenAnlegen.setMnemonic('a');
                    iTabellenAnlegen.setActionCommand("Tabellen anlegen");
                    iTabellenAnlegen.addActionListener(this);
                    mDatabase.add(iTabellenAnlegen);

                    JMenuItem iTabellenLöschen = new JMenuItem("Tabellen löschen");
                    iTabellenLöschen.setMnemonic('l');
                    iTabellenLöschen.setActionCommand("Tabellen löschen");
                    iTabellenLöschen.addActionListener(this);
                    mDatabase.add(iTabellenLöschen);
                    
                    
                    
                }
                else{}


            }
        } catch(Exception e) {}

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

                JPanel contentPane = (JPanel) getContentPane();
                contentPane.setLayout(new BorderLayout());

                layer = new JLayeredPane();
        layer.setBackground(contentPane.getBackground());

        contentPane.add("Center", layer);

        // WindowListener
        addWindowListener(new WindowEventHandler());
    }

    private void createTables()
    {
        Object[] options = { "Ja", "Nein" };
        String line1 = "Wollen Sie die Datenbank-Tabellen wirklich neu anlegen?";
        String line2 = "ACHTUNG: Alle gespeicherten Daten gehen dadurch verloren!";

        JTextArea area = new JTextArea(line1 + '\n' + line2);
        area.setBackground(SystemColor.control);

                JOptionPane optionPane = new JOptionPane(area, JOptionPane.QUESTION_MESSAGE,
                                                         JOptionPane.YES_NO_OPTION, null,
                                                         options, options[0]);

        JDialog dialogPane = optionPane.createDialog(this, "Tabellen anlegen");
        dialogPane.show();

                Object selectedValue = optionPane.getValue();

                if(selectedValue != null)
                {
                    if(options[0].equals(selectedValue))
                    {
                    	try {
							//                       DBInit dbInit = new DBInit();
//                       dbInit.createTables();
							dbi.createTables();
							line1 = "Operation erfolgreich durchgefuehrt!\nSie muessen den Server neu starten um die Änderungen zu uebernehmen!";
						} catch (RemoteException e) {
							line1="Operation nicht durchgefuehrt!";
							e.printStackTrace();
						}
                       

		               area = new JTextArea(line1);
		               area.setBackground(SystemColor.control);

                       optionPane = new JOptionPane(area, JOptionPane.INFORMATION_MESSAGE);

		               dialogPane = optionPane.createDialog(this, "Statusmeldung");
		               dialogPane.show();

                    }
                }

    }

    private void deleteTables()
    {
        Object[] options = { "Ja", "Nein" };
        String line1 = "Wollen Sie die Datenbank-Tabellen wirklich löschen?";
        String line2 = "ACHTUNG: Alle gespeicherten Daten gehen dadurch verloren!";

        JTextArea area = new JTextArea(line1 + '\n' + line2);
        area.setBackground(SystemColor.control);

                JOptionPane optionPane = new JOptionPane(area,JOptionPane.QUESTION_MESSAGE,
                                                         JOptionPane.YES_NO_OPTION, null,
                                                         options, options[0]);

        JDialog dialogPane = optionPane.createDialog(this, "Tabellen löschen");
        dialogPane.show();

                Object selectedValue = optionPane.getValue();

                if(selectedValue != null)
                {
                    if(options[0].equals(selectedValue));
                    {
                       try {
						dbi.deleteTables();
						line1 = "Operation erfolgreich durchgefuehrt!\nSie muessen den Server neu starten um die Änderungen zu uebernehmen!";
					} catch (RemoteException e) {
						line1="Operation nicht erfolgreich durchgefuehrt!";
						e.printStackTrace();
					}
                       


		               area = new JTextArea(line1);
		               area.setBackground(SystemColor.control);
		
		               optionPane = new JOptionPane(area, JOptionPane.INFORMATION_MESSAGE);
		               dialogPane = optionPane.createDialog(this, "Statusmeldung");
		               dialogPane.show();
//		               dialogPane.setVisible(true);
		            }
                }
    }


    private void stopServer()
    {
        JOptionPane.showMessageDialog(this, "Der Calendarium-Server wird in 2 Minuten angehalten!",
                                      "Server anhalten", JOptionPane.INFORMATION_MESSAGE);

        String msg = "Der Calendarium-Server wird in 2 Minuten angehalten,\n" +
                     "bitte beenden Sie daher fuer kurze Zeit diese Applikation!";

        Data.msgServer.sendAdminMessage(msg);

        // Beenden
        try
        {   Thread.sleep(120000);
//            Thread.sleep(1000);//für testzwecke, server wird nach 1er sekunde heruntergefahren
            Data.msgServer.shutDown();

        } catch(InterruptedException e) {}
    }

    class WindowEventHandler extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
                        if(!serverStart) System.exit(0);
        }
    }

    // Property
        public String getProperty(String s){
        	
           return (String) userProperties.get(s);
        }

    public static void main(String args[])
        {
            try
        {   Monitor m = new Monitor();
            m.show();

        } catch(Exception e)
        {   e.printStackTrace();
        }
        }

        // ActionListener // ActionListener // ActionListener // ActionListener // ActionListener //
        @SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e)
        {
            String c = e.getActionCommand();
        JInternalFrame gui;

            if(e.getSource().getClass() == JMenuItem.class)
            {
                if(c.equals("Close"))
            {   stopServer();
            }

                // Logins
                else if(c.equals("Logins"))
            {
                if(!frames.containsKey(c))
                {
                    Logins logins = new Logins();
                    gui = logins.getGUI();
                    frames.put(c, gui);
                    gui = (JInternalFrame) frames.get(c);//by johnny
                    gui.setVisible(true);//by johnny

                } else
                {
                    gui = (JInternalFrame) frames.get(c);
                    gui.setVisible(true);
                }

                try
                {   if(gui.isIcon()) gui.setIcon(false);
                    gui.setSelected(true);

                } catch(java.beans.PropertyVetoException ex) {}

                layer.add(gui, 0);
            }

            else if(c.equals("Beenden"))
            {
                System.exit(0);
            }

            // User
            else if(c.equals("User"))
            {
                if(!frames.containsKey(c))
                {
                    EditUserControl editUser = new EditUserControl(this);
                    gui = editUser.getGUI();
                    frames.put(c, gui);       
                }
                
                gui = (JInternalFrame) frames.get(c);//by johnny
                gui.setVisible(true);//by johnny
                try
                {   if(gui.isIcon()) gui.setIcon(false);
                    gui.setSelected(true);

                } catch(java.beans.PropertyVetoException ex) {}

                layer.add(gui, 0);
            }

            // Gruppen
            else if(c.equals("Gruppen"))
            {
                if(!frames.containsKey(c))
                {
                    EditGruppenControl editGruppen = new EditGruppenControl(this);
                    gui = editGruppen.getGUI();
                    frames.put(c, gui);             
                }
                gui = (JInternalFrame) frames.get(c);//by johnny
                gui.setVisible(true);//by johnny


                try
                {   if(gui.isIcon()) gui.setIcon(false);
                    gui.setSelected(true);

                } catch(java.beans.PropertyVetoException ex) {}

                layer.add(gui, 0);
            }

            // Typen
            else if(c.equals("Typen"))
            {
                if(!frames.containsKey(c))
                {
                    EditTypenControl editTypen = new EditTypenControl(this);
                    gui = editTypen.getGUI();
                    frames.put(c, gui);
                } 

                gui = (JInternalFrame) frames.get(c);//by johnny
                gui.setVisible(true);//by johnny

                try
                {   if(gui.isIcon()) gui.setIcon(false);
                    gui.setSelected(true);

                } catch(java.beans.PropertyVetoException ex) {}

                layer.add(gui, 0);
            }

            // Feiertage
            else if(c.equals("Feiertage")){
	                if(!frames.containsKey(c))
	                {
	                    EditFeiertageControl editFeiertage = new EditFeiertageControl(this);
	                    gui = editFeiertage.getGUI();
	                    frames.put(c, gui);                 
	                } 
	                gui = (JInternalFrame) frames.get(c);
	                gui.setVisible(true);
            

                try
                {   if(gui.isIcon()) gui.setIcon(false);
                    gui.setSelected(true);

                } catch(java.beans.PropertyVetoException ex) {}

                layer.add(gui, 0);
            }

            // Tabellen anlegen
            else if(c.equals("Tabellen anlegen"))
            {
                createTables();
            }
            else if(c.equals("Tabellen löschen"))
            {
                deleteTables();
            }
            else if(c.equals("Feiertage init")){
            	//fügt alles in die datenbank ein.
            	@SuppressWarnings("unused")
				InitFeiertage feier = new InitFeiertage();
            	
            }

            }
        }

}