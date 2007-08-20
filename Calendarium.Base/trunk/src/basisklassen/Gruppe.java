package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

////////////////////////////////////////////////////////////////////////////////////////////
// Gruppe // Gruppe // Gruppe // Gruppe // Gruppe // Gruppe // Gruppe // Gruppe // Gruppe //
////////////////////////////////////////////////////////////////////////////////////////////

public class Gruppe extends dblayer.PersistentObject implements java.io.Serializable
{
    private static final long serialVersionUID = 3892221256089407101L;
	private boolean admin;
    private String kuerzel, name;
    private long owner;
//    private long ID;

    private Hashtable gruppen;
    private Hashtable personen;

    public Gruppe(long id)
    {
        super(id);

        gruppen = new Hashtable();     // Beinhaltete Gruppen
        personen = new Hashtable();    // Beinhaltete Personen
    }

    public Gruppe(boolean a)    // Neue Gruppe anlegen
    {
        super();
        admin = a;                     // Administratorflag
        kuerzel = null;                 // Gruppenkuerzel

        gruppen = new Hashtable();     // Beinhaltete Gruppen
        personen = new Hashtable();    // Beinhaltete Personen
    }

    public Gruppe(boolean a, String k, String n)
    {
        super();
        admin = a;                     // Administratorflag
        kuerzel = k;                    // Gruppenkuerzel
        name = n;                      // Gruppenname

        gruppen = new Hashtable();     // Beinhaltete Gruppen
        personen = new Hashtable();    // Beinhaltete Personen
    }

    public Gruppe(long i, boolean a, String k, String n)
    {
        super(i);                        // SurrogatSchluessel
        admin = a;                     // Administratorflag
        kuerzel = k;                    // Gruppenkuerzel
        name = n;                      // Gruppenname

        gruppen = new Hashtable();     // Beinhaltete Gruppen
        personen = new Hashtable();    // Beinhaltete Personen
    }

    public String toString()
    {
        return new String("admin " + admin + "kuerzel " + kuerzel + "gruppen" + gruppen.toString() + "pers" + personen.toString());
    }

    // is a person member of the group's subgroups
	private boolean isMember(Person person, Hashtable gruppen)
	{
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe g = (Gruppe) e.nextElement();
				if(g.containsPerson(person))
				{
					return true;

				} else
				{
					return isMember(person, g.getGruppen());
				}
			}
		}

		return false;
	}

    // is a group member of the group's subgroups
    private boolean isMember(Gruppe gruppe, Hashtable gruppen)
	{
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe g = (Gruppe) e.nextElement();
				if(g.containsGruppe(gruppe))
				{
					return true;

				} else
				{
					return isMember(gruppe, g.getGruppen());
				}
			}
		}

		return false;
	}

    // deletes all occurences of a person in all subgroups
	private void deletePerson(Person person, Hashtable gruppen)
    {
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe g = (Gruppe) e.nextElement();
				if(g.containsPerson(person))
				{
					g.removePerson(person);

				} else
				{
					deletePerson(person, g.getGruppen());
				}
			}
		}
	}

	// deletes all occurences of a group in all subgroups
	private void deleteGruppe(Gruppe gruppe, Hashtable gruppen)
    {
		if(gruppen != null)
		{
			Enumeration e = gruppen.elements();
			while(e.hasMoreElements())
			{
				Gruppe g = (Gruppe) e.nextElement();
				if(g.containsGruppe(gruppe))
				{
					g.removeGruppe(gruppe);

				} else
				{
					deleteGruppe(gruppe, g.getGruppen());
				}
			}
		}
	}

    //////////////////////////////////////////////////////////////////////////////////////
	// Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
	//////////////////////////////////////////////////////////////////////////////////////


	// Adminflag festlegen
	public void setAdminFlag(boolean a)
	{   admin = a;
	}

    // Name festlegen
	public void setName(String n)
	{   name = n;
	}

	// Kuerzel festlegen
    public void setKuerzel(String k)
	{   kuerzel = k;
	}

    // Besitzer der Gruppe festlegen
    public void setOwner(long o)
    {    owner = o;
    }

	// Gruppe hinzufuegen
	@SuppressWarnings("unchecked")
	public void addGruppe(Gruppe g)
	{
	    Long id = new Long(g.getID());
	    gruppen.put(id, g);
	}

	// Gruppe löschen      XXXXXXXXXXXXXX
	public void removeGruppe(Gruppe g)
	{
	    Long id = new Long(g.getID());
	    gruppen.remove(id);
	}

	// Personen hinzufuegen    XXXXXXXXX
	@SuppressWarnings("unchecked")
	public void addPerson(Person p)
	{
	    Long id = new Long(p.getID());
	    personen.put(id, p);
	}

	// Person löschen     XXXXXXXXXXX
	public void removePerson(Person p)
	{
	    Long id = new Long(p.getID());
	    personen.remove(id);
	}

	// Gruppen ausgeben
    public Hashtable getGruppen()
	{   return gruppen;
	}

	// Gruppen enthalten?
	public boolean containsGruppe(Gruppe g)
	{
	    Long id = new Long(g.getID());
	    return gruppen.containsKey(id);
	}

	// Personen ausgeben
	public Hashtable getPersonen()
	{   return personen;
	}

	// Person enthalten?
	public boolean containsPerson(Person p)
	{
	    Long id = new Long(p.getID());
	    return personen.containsKey(id);
	}

	// AdminFlag ausgeben
    public boolean getAdminFlag()
	{   return admin;
	}

	// Kuerzel ausgeben
	public String getKuerzel()
	{   return kuerzel;
	}

	// Name ausgeben
	public String getName()
	{   return name;
	}

	// Ist Gruppenmitglied?
	public boolean isMember(Person p)
	{
		if(!personen.containsKey(new Long(p.getID())))
		{	return isMember(p, this.gruppen);

		} else return true;
	}

    // Ist Gruppenmitglied?
	public boolean isMember(Gruppe g)
	{
		if(!gruppen.containsKey(new Long(g.getID())))
		{	return isMember(g, this.gruppen);

		} else return true;
	}

	// Person aus allen Untergruppen löschen
	public void deletePerson(Person p)
	{
	    if(containsPerson(p))
	    {
	        removePerson(p);
	        delete(p,"personen");
	    }
	    deletePerson(p, this.gruppen);
	}

	// Gruppe aus allen Untergruppen löschen
	public void deleteGruppe(Gruppe g)
	{   deleteGruppe(g, this.gruppen);
	}

    //////////////////////////////////////////////////////////////////////


    /** maps one row from db to instance of EintragsTyp
      */
    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            kuerzel = res.getString(map.getAttribute("kuerzel").getColumn());
            name = res.getString(map.getAttribute("name").getColumn());
            admin = (res.getLong(map.getAttribute("admin").getColumn()) == 1);
            owner = res.getLong(map.getAttribute("owner").getColumn());
        }catch (SQLException s){System.out.println(s);}
    }

    /** returns value of attribute 'attribute'
      */
    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier")) return new Long(objectIdentifier);
        if (attribute.equals("kuerzel")) return kuerzel;
        if (attribute.equals("name")) return name;
        if (attribute.equals("owner")) return new Long(owner);
        if (attribute.equals("admin"))
        {

            if (admin) return new Integer(1);
            else return new Integer(0);
        }
        return null;
    }



}