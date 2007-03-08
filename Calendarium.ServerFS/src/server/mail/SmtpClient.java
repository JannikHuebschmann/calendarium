package server.mail; //
///////////////////////

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////////////////////////
// SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient // SmtpClient //
////////////////////////////////////////////////////////////////////////////////////////////////////

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

	public SmtpClient(String h, String p) 
	{	
	    hostname = h;
		port = Integer.valueOf(p).intValue();
	}

	public String getHostname() 
	{	return hostname;
	}

	public int getPort() 
	{	return port;
	}

	public void setHostname(String hostname)
	{	this.hostname = hostname;
	}

	public void setPort(int port) 
	{	this.port = port;
	}

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

	private boolean init() 
	{
		if(!readResponse().startsWith("220")) 
		{
			smtps.respond(FAILEDHANDSHAKE);
			return false;
		}
		
		return true;
	}

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

	private void date() 
	{
		SimpleDateFormat sdf;
		
		sdf = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		writeCommand("Date: " + sdf.format(new Date()));
	}

	private void from() 
	{	writeCommand("From: " + smtpe.getSender());
	}

	private void to() 
	{	writeCommand("To: " + smtpe.getRecipient());
	}

	private void subject()
	{	writeCommand("Subject: " + smtpe.getSubject());
	}

	private void message() 
	{
		writeCommand(smtpe.getMessage());
		writeCommand(".");
	}

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

	private void writeCommand(String command) 
	{	
		pw.print(command);
		pw.print("\r\n");
		pw.flush();
	}

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


  