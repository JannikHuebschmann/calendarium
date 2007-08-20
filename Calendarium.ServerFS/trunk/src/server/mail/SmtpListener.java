package server.mail; //
///////////////////////

import java.util.*;

/**
 * 
 * @author unknown, edited by K. Vahling
 * extends class EventListener
 * This public interface-class defines the public function-decla-
 * ration 'send'. That function gets the parameter smtpe
 * (SmtpEvent).
 */
public interface SmtpListener extends EventListener 
{
	public void send(SmtpEvent smtpe);
}
