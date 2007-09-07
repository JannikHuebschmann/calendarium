package swtkal.exceptions;

/**
 * Common superclass for all application-oriented exceptions.
 * @author bernd
 *
 */
public class SWTKalException extends Exception
{
	private static final long serialVersionUID = 888827155985187102L;

   public SWTKalException()
	{
		super();
	}

   public SWTKalException(String message)
	{
		super(message);
	}
}
