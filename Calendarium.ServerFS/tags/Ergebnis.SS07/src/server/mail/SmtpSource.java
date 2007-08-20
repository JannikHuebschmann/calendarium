package server.mail; //
///////////////////////

/**
 * @author unknown, edited by K. Vahling
 * This public interface-class defines the public function-decla-
 * ration 'respond'. That function gets the parameter code (int).
 */
public interface SmtpSource 
{
	public void respond(int code);
}