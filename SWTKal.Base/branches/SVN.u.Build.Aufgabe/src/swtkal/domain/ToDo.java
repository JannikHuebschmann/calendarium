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
package swtkal.domain;

/*****************************************************************************************************
 * Class ToDo represents tasks that are attached to a special date.
 * 
 * @author calendarium project
 */
public class ToDo extends Eintrag
{
	public ToDo(Person besitzer)
	{
		super(besitzer);
	}
}