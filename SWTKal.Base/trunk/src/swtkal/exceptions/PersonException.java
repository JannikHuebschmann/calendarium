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
 * This PersonException ......
 * 
 * @author calendarium project
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
