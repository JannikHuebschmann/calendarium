package server.mail; //
///////////////////////

import java.util.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent // SmtpEvent //
/////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This is the class-declaration of the SmtpEvent-class.
 */
public class SmtpEvent extends EventObject 
{	
	private static final long serialVersionUID = 6001764237315996444L;
	private String sender;
	private String recipient;
	private String subject;
	private String message;

	/**
	 * Constructor of SmtpEvent-class
	 * @param source (Object that includes the source)
	 * @param sender (String that contains the transmitter-name)
	 * @param recipient (String containing the recipient-name)
	 * @param subject (String importing the subject-name)
	 * @param message (String implying the message)
	 */
	public SmtpEvent(Object source, String sender, String recipient, String subject, String message) 
	{
		super(source);
		this.sender = sender;
		this.recipient = recipient;
		this.subject = subject;
		this.message = message;
	}

	/**
	 * public get-function
	 * @return (String containing the current transmitter-name)
	 */
	public String getSender() 
	{	return sender;
	}

	/**
	 * public get-function
	 * @return (String containing the current recipient-name)
	 */
	public String getRecipient() 
	{	return recipient;
	}
    
	/**
	 * public get-function
	 * @return (String importing the current subject)
	 */
    public String getSubject()
    {   return subject;
    }   
    
    /**
     * public get-function
     * @return (String including the current message)
     */
	public String getMessage() 
	{	return message;
	}
}