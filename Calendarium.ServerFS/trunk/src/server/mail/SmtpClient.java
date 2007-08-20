package server.mail; //
///////////////////////

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////////////////////////
// SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient //
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the SmtpClient-Class.
 **/
public class SmtpClient implements Serializable, SmtpListener 
{
	private static final long serialVersionUID = -6516652901609322702L;
	public final static int SENDOK = 0;
	public final static int FAILEDCOMMUNICATION = 1;
	public final static int FAILEDHANDSHAKE = 2;
	public final static int BADSENDERADDRESS = 3;
	public final static int BADRECIPIENT = 4;
	public final static int FAILEDDATA = 5;
	public final static int FAILEDEMAIL = 6;
	public final static int FAILEDQUIT = 7;
	private String hostname;
	private int port;
	private transient PrintWriter pw;
	private transient BufferedReader br;
	private transient SmtpEvent smtpe;
	private transient SmtpSource smtps;

	/**
	 * Constructor of SmtpClient-Class
	 * @param h (String containing the hostname)
	 * @param p (String containing the port)
	 */
	public SmtpClient(String h, String p) 
	{	
	    hostname = h;
		port = Integer.valueOf(p).intValue();
	}

	/**
	 * public get-function
	 * @return String containing the current hostname
	 */
	public String getHostname() 
	{	return hostname;
	}

	/**
	 * public get-function
	 * @return int that contains the current port-value
	 */
	public int getPort() 
	{	return port;
	}

	/**
	 * public set-function
	 * @param hostname (String including a hostname)
	 */
	public void setHostname(String hostname)
	{	this.hostname = hostname;
	}

	/**
	 * public set-function
	 * @param port (int that contains a port)
	 */
	public void setPort(int port) 
	{	this.port = port;
	}

	/**
	 * This public function sends a message using the simple mail
	 * transfer protocol. If that fails, an error message will be
	 * responded.
	 * @param smtpe (SmtpEvent)
	 */
	public void send(SmtpEvent smtpe) 
	{	
		this.smtpe = smtpe;
		smtps = (SmtpSource)smtpe.getSource();
		
		try 
		{	// Create BufferedReader and PrintWriter 
			Socket socket = new Socket(hostname, port);
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br =  new BufferedReader(isr);
			OutputStream os = socket.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			pw = new PrintWriter(bos);

			// Use the SMTP protocol to send the message
			smtpProtocolHandler();

			// Close the socket
			socket.close();

		} catch(Exception ex) 
		{
			// Inform sender about problem
			smtps.respond(FAILEDCOMMUNICATION);

			// Print
			ex.printStackTrace();
		}
	}

	/**
	 * This private function responds an "OK" to the transmitter,
	 * if a correct transfer could be detected.
	 */
	private void smtpProtocolHandler() 
	{
		if(!init()) 
		{	return;
		}
		
		if(!helo()) 
		{	return;
		}
		
		if(!mailFrom()) 
		{	return;
		}

		if(!rcptTo()) 
		{	return;
		}
		
		if(!data()) 
		{	return;
		}
		
		if(!email()) 
		{	return;
		}
		
		if(!quit()) 
		{	return;
		}

		// Display result for sender
		smtps.respond(SENDOK);
	}

	/**
	 * This private function initializes the smtp-client and returns
	 * true if initialisation was successful. Otherwith false is
	 * returned.
	 * @return boolean
	 */
	private boolean init() 
	{
		if(!readResponse().startsWith("220")) 
		{
			smtps.respond(FAILEDHANDSHAKE);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends a "helo"-message and waits for
	 * response. If no response appears, false is returned;
	 * otherwise true.
	 * @return boolean
	 */
	private boolean helo() 
	{
		writeCommand("HELO" + " uni-klu.ac.at");
		
		if(!readResponse().startsWith("250")) 
		{
			smtps.respond(FAILEDHANDSHAKE);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends the "mail from"-message and waits
	 * for response. If no answer occurs, false is returned;
	 * otherwise true.
	 * @return boolean
	 */
	private boolean mailFrom() 
	{
		writeCommand("MAIL FROM:" + smtpe.getSender());
		
		if(!readResponse().startsWith("250")) 
		{
			smtps.respond(BADSENDERADDRESS);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends the "recipient"-message and
	 * waits for response. If no response is picked up, false
	 * is returned; otherwise true.
	 * @return boolean
	 */
	private boolean rcptTo() 
	{
		String recipient = smtpe.getRecipient();
		writeCommand("RCPT TO:" + recipient);
		
		if(!readResponse().startsWith("250")) 
		{
			smtps.respond(BADRECIPIENT);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends the "data"-message and waits for
	 * response. If no answer is receipt, false is returned;
	 * otherwise true.
	 * @return boolean
	 */
	private boolean data() 
	{
		writeCommand("DATA");
		if(!readResponse().startsWith("354")) 
		{
			smtps.respond(FAILEDDATA);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends an email and waits for response.
	 * If a response is received, true is returned; otherwise
	 * false.
	 * @return boolean
	 */
	private boolean email() 
	{
		date();
		from();
		to();
		subject();
		message();
		
		if(!readResponse().startsWith("250")) 
		{
			smtps.respond(FAILEDEMAIL);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function sends "date"-mail.
	 */
	private void date() 
	{
		SimpleDateFormat sdf;
		
		sdf = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		writeCommand("Date: " + sdf.format(new Date()));
	}

	/**
	 * This private function sends "from"-mail.
	 */
	private void from() 
	{	writeCommand("From: " + smtpe.getSender());
	}

	/**
	 * This private function sends "to"-message.
	 */
	private void to() 
	{	writeCommand("To: " + smtpe.getRecipient());
	}

	/**
	 * This private function sends "subject"-mail.
	 */
	private void subject()
	{	writeCommand("Subject: " + smtpe.getSubject());
	}

	/**
	 * This private function sends a message.
	 */
	private void message() 
	{
		writeCommand(smtpe.getMessage());
		writeCommand(".");
	}

	/**
	 * This private function sends a "quit"-message and waits for
	 * response. If no response is received, false is returned;
	 * otherwise true.
	 * @return boolean
	 */
	private boolean quit() 
	{
		writeCommand("QUIT");
		
		if(!readResponse().startsWith("221")) 
		{
			smtps.respond(FAILEDQUIT);
			return false;
		}
		
		return true;
	}

	/**
	 * This private function prints a command to the printwriter.
	 * @param command (String containing the command)
	 */
	private void writeCommand(String command) 
	{	
		pw.print(command);
		pw.print("\r\n");
		pw.flush();
	}

	/**
	 * This private function fetches the response-message.
	 * @return (String including the response)
	 */
	private String readResponse() 
	{
		String line = "";
		try 
		{
			line = br.readLine();

		} catch(Exception ex) {}
		
		return line;
	}
}


  