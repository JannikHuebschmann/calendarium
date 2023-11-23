package swtkal.swing.client;
// Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import java.util.*;
import swtkal.domain.Person;

////////////////////////////////////////////////////////////////////////////////////////////
// OffeneKalender // OffeneKalender // OffeneKalender // OffeneKalender // OffeneKalender //
////////////////////////////////////////////////////////////////////////////////////////////

public class OffeneKalender
{
//	private Hashtable personenTable;

	@SuppressWarnings("rawtypes")
	private Vector personenListe;

	@SuppressWarnings("rawtypes")
	public OffeneKalender()
	{
//		personenTable = new Hashtable();
		personenListe = new Vector();
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
	// Funktionen //
	// ////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("rawtypes")
	public Enumeration getEnumeration()
	{
		return personenListe.elements();
	}

	@SuppressWarnings("unchecked")
	public void add(Person person, int colorIndex)
	{
//		Long id = new Long(person.getID());
//		personenTable.put(id, new Integer(colorIndex));
		personenListe.addElement(person);
	}

	public void remove(Person person)
	{
//		Long id = new Long(person.getID());
//		personenTable.remove(id);
		personenListe.removeElement(person);
	}

	public int getColorIndex(Person person)
	{
//		Long id = new Long(person.getID());
//		return ((Integer) personenTable.get(id)).intValue();
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public Vector getPersonenListe()
	{
		return personenListe;
	}

	public int getSize()
	{
		return personenListe.size();
	}
}
