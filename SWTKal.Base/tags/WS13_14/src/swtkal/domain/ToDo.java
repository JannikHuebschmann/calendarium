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
package swtkal.domain;

/*****************************************************************************************************
 * Class ToDo represents tasks that are attached to a special date.
 * 
 * @author swtUser
 */
public class ToDo extends Eintrag
{
	protected Datum zuErledigenBis;
	
	public ToDo(Person b, String k, String l, Datum bis)
	{
		super(b, k, l);
		zuErledigenBis = bis;
	}
	
	public ToDo(Person b, String k, String l)
	{
		super(b, k, l);
	}

	public ToDo(Person b, Datum bis)
	{
		super(b);
		zuErledigenBis = bis;
	}

	public ToDo(Person b)
	{
		super(b);
	}

	public Datum getZuErledigenBis()
	{
		return zuErledigenBis;
	}

	public void setZuErledigenBis(Datum bis)
	{
		zuErledigenBis = bis;
	}
}