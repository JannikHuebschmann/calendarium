/*****************************************************************************************************
 * 	Project:			SWTKal.JPAServer
 * 	
 *  creation date:		01.03.2008
 *
 * 
 *****************************************************************************************************
 *	date			| 	author		| 	reason for change
 *****************************************************************************************************
 *	01.03.2008			ejbUser			initial version
 *
 */
package swtkal.server.javapersistence;

import javax.persistence.*;

/*****************************************************************************************************
 * Class Passwort is used to manage passwords for Person objects,
 * as they are not intended to be stored in the same class.
 * 
 * @author ejbUser
 */
@Entity
public class Passwort
{
	@Id
	protected String kuerzel;
	protected String passwort;
	
	public Passwort(String k, String p)
	{
		kuerzel  = k;
		passwort = p;
	}

	public String getKuerzel()
	{
		return kuerzel;
	}
	public void setKuerzel(String kuerzel)
	{
		this.kuerzel = kuerzel;
	}
	
	public String getPasswort()
	{
		return passwort;
	}
	public void setPasswort(String passwort)
	{
		this.passwort = passwort;
	}
}
