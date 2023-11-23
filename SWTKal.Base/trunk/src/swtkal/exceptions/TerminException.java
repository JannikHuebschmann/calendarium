/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			swtUser			initial version
 *
 */
package swtkal.exceptions;


/*****************************************************************************************************
 * This subclass of SWTKalException is used for exceptions in connection
 * with managing Termin objects within the application.
 * 
 * @author swtUser
 */
public class TerminException extends SWTKalException
{
	private static final long serialVersionUID = 7727211934490050961L;
	
   public TerminException()
	{
		super();
	}

   public TerminException(String message)
	{
		super(message);
	}
}
