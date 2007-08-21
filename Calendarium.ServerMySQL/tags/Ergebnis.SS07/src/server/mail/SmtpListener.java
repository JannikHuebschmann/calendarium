package server.mail; //
///////////////////////

import java.util.EventListener;

public interface SmtpListener extends EventListener 
{
	public void send(SmtpEvent smtpe);
}
