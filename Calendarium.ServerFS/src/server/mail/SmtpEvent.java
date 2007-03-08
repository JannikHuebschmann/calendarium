package server.mail; //
///////////////////////

import java.util.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent //
/////////////////////////////////////////////////////////////////////////////////////////////

public class SmtpEvent extends EventObject 
{	
	private static final long serialVersionUID = 6001764237315996444L;
	private String sender;
	private String recipient;
	private String subject;
	private String message;

	public SmtpEvent(Object source, String sender, String recipient, String subject, String message) 
	{
		super(source);
		this.sender = sender;
		this.recipient = recipient;
		this.subject = subject;
		this.message = message;
	}

	public String getSender() 
	{	return sender;
	}

	public String getRecipient() 
	{	return recipient;
	}
    
    public String getSubject()
    {   return subject;
    }   
    
	public String getMessage() 
	{	return message;
	}
}