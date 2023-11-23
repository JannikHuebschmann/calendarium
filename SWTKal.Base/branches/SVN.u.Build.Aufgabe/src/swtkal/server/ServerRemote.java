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
package swtkal.server;

import java.rmi.Remote;

/*****************************************************************************************************
 * Abstract class ServerRemote specifies the required server interface
 * for remote references to a server.
 * 
 * @author calendarium project
 */
public abstract class ServerRemote extends Server implements Remote
{
}
