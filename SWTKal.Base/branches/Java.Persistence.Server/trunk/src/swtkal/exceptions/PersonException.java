/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			swtUser			intial version
 *
 */
package swtkal.exceptions;


/*****************************************************************************************************
 * This subclass of SWTKalException is used for exceptions in connection
 * with managing Person objects within the application.
 * 
 * @author swtUser
 */
public class PersonException extends SWTKalException
{
	private static final long serialVersionUID = 1074657912548031985L;
	
   public PersonException()
	{
		super();
	}

   public PersonException(String message)
	{
		super(message);
	}
}
