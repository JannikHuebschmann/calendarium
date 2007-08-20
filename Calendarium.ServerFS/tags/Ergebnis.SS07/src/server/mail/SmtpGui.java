package server.mail; //
///////////////////////

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

////////////////////////////////////////////////////////////////////
// SmtpGui // SmtpGui // SmtpGui // SmtpGui // SmtpGui // SmtpGui //
////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the SmtpGui-class.
 */
public class SmtpGui extends Frame implements ActionListener, SmtpSource, TextListener 
{
	private static final long serialVersionUID = 6762726952627215079L;
	private SmtpListener listener;
	private TextField senderTf, recipientTf, resultTf;
	private TextArea messageTa;

	/**
	 * Constructor of SmtpGui-class
	 */
	public SmtpGui() 
	{	
		setTitle("EMail");
		setSize(300, 500);
		setLayout(new BorderLayout());

		// Create GUI elements
		Label title = new Label("SMTP INTERFACE", Label.CENTER);
		Label senderLabel = new Label("Sender:");
		senderTf = new TextField(20);
		senderTf.addTextListener(this);
		Label recipientLabel = new Label("Recipient:");
		recipientTf = new TextField(20);
		recipientTf.addTextListener(this);
		Label messageLabel = new Label("Message:");
		messageTa = new TextArea(5, 30);
		messageTa.addTextListener(this);
		Button sendButton = new Button("Send");
		sendButton.addActionListener(this);
		resultTf = new TextField(20);
		
		// SmtpListener
		listener = new SmtpClient("mailsrv.uni-klu.ac.at", "25");

		Panel pane = new Panel();

		// Create and set layout manager
		GridBagLayout gbl = new GridBagLayout();
		pane.setLayout(gbl);

		// Create and initialize GridBagConstraints object
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5, 5, 5, 5);

		// Add title to the GUI
		gbc.gridy = 0;
		gbl.setConstraints(title, gbc);
		pane.add(title);

		// Add sender label to the GUI
		gbc.gridy = 1;
		gbl.setConstraints(senderLabel, gbc);
		pane.add(senderLabel);
 
		// Add sender text field to the GUI
		gbc.gridy = 2;
		gbl.setConstraints(senderTf, gbc);
		pane.add(senderTf);

		// Add recipient label to the GUI
		gbc.gridy = 3;
		gbl.setConstraints(recipientLabel, gbc);
		pane.add(recipientLabel);

		// Add recipient text field to the GUI
		gbc.gridy = 4;
		gbl.setConstraints(recipientTf, gbc);
		pane.add(recipientTf);

		// Add message label to the GUI
		gbc.gridy = 5;
		gbl.setConstraints(messageLabel, gbc);
		pane.add(messageLabel);

		// Add message text area to the GUI
		gbc.gridy = 6;
		gbl.setConstraints(messageTa, gbc);
		pane.add(messageTa);

		// Add send button to the GUI
		gbc.gridy = 7;
		gbl.setConstraints(sendButton, gbc);
		pane.add(sendButton);

		// Add result text field to the GUI
		gbc.gridy = 8;
		gbl.setConstraints(resultTf, gbc);
		pane.add(resultTf);

		// WindowListener
		addWindowListener(new WindowEventHandler());

		add("Center", pane);
	}
	
	/**
	 * @author unknown, edited by K. Vahling
	 * This is the class-declaration of the WindowEventHandler-
	 * class.
	 */
	class WindowEventHandler extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {   System.exit(0);
        }
    }

	/**
	 * This is the main-function of the Smtp-gui, but it seems as
	 * if the function is not beeing called during start of server-
	 * program.
	 * @param args (String[])
	 */
	public static void main(String[] args)
	{	
		SmtpGui gui = new SmtpGui();
		gui.show();
	}

	/**
	 * This is the public class 'paint' which is supposed to draw
	 * the window.
	 * @param g (Graphics)
	 */
	public void paint(Graphics g) 
	{
		Dimension d = getSize();
		g.drawRect(0, 0, d.width - 1, d.height - 1);
	}

	/**
	 * This public function  resets the result-textfield of the gui.
	 * @param te (TextEvent)
	 */
	public void textValueChanged(TextEvent te)
	{	
		resultTf.setText("");
	}

	/**
	 * This public function creates a new EmailEvent with the
	 * current gui's input.
	 */
	public void actionPerformed(ActionEvent ae) 
	{
		String s = senderTf.getText();
		String r = recipientTf.getText();
		String m = messageTa.getText();
		String t = "Test";
		
		SmtpEvent smtpe = new SmtpEvent(this, s, r, t, m);
		fireEmailEvent(smtpe);
	}

	/**
	 * This public function throws an exception if there is more
	 * than one listener.
	 * @param smtpl (SmtpListener)
	 * @throws TooManyListenersException
	 */
	public void addEmailListener(SmtpListener smtpl) throws TooManyListenersException 
	{
		if(listener == null) 
		{	listener = smtpl;

		} else 
		{	throw new TooManyListenersException();
		}
	}

	/**
	 * This public function nulls the listener-attribute.
	 * @param smtpl (SmtpListener)
	 */
	public void removeEmailListener(SmtpListener smtpl) 
	{	listener = null;
	}

	/**
	 * This public function sends a given SmtpEvent to the current
	 * listener.
	 * @param smtpe (SmtpEvent)
	 */
	public void fireEmailEvent(SmtpEvent smtpe) 
	{	listener.send(smtpe);
	}

	/**
	 * This public function sets the textfield appropriate to the
	 * given code.
	 * @param code (int that contains the error-/successcode)
	 */
	public void respond(int code) 
	{
		String str = "Smtp Problem";
		
		if(code == SmtpClient.SENDOK) 
		{	
			str = "Sent";
		}
		else if(code == SmtpClient.FAILEDCOMMUNICATION) 
		{
			str = "Failed Communication";
		}
		else if(code == SmtpClient.FAILEDHANDSHAKE)
		{
			str = "Failed Handshake";
		}
		else if(code == SmtpClient.BADSENDERADDRESS) 
		{
			str = "Bad Sender Address";
		}
		else if(code == SmtpClient.BADRECIPIENT) 
		{
			str = "Bad Recipient";
		}
		else if(code == SmtpClient.FAILEDDATA) 
		{
			str = "Failed Data Transfer";
		}
		else if(code == SmtpClient.FAILEDEMAIL) 
		{	str = "Failed Email";
		}
		else if(code == SmtpClient.FAILEDQUIT) 
		{
			str = "Failed Quit";
		}
		
		resultTf.setText(str);
	}	

	/**
	 * This private function clears the information-field of the
	 * gui. 
	 * @param ois (ObjectInputStream)
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		// Read non-static and non-transient information from stream
		ois.defaultReadObject();

		// Clear all information from the GUI elements
		senderTf.setText("");
		recipientTf.setText("");
		messageTa.selectAll();
		messageTa.replaceRange("", 0, messageTa.getSelectionEnd());
		resultTf.setText("");
	}
}