/*****************************************************************************************************
 * 	Project:			SWTKal.Base
 * 	
 *  creation date:		01.08.2007
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.08.2007			calproj			transfer out of the calendarium project
 *
 */
package swtkal.exceptions;

/*****************************************************************************************************
 * Common superclass for all application-oriented exceptions.
 * 
 * @author 
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
