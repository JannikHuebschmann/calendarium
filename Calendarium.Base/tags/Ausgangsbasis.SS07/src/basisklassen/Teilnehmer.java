package basisklassen; //
////////////////////////

import java.sql.ResultSet;
import java.sql.SQLException;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class Teilnehmer extends dblayer.PersistentObject implements java.io.Serializable
{
    private static final long serialVersionUID = 6584918393662067460L;
	private Object teilnehmer;
    private boolean[] nfkt;

    // Teilnehmer in Rolle Gruppe
    public Teilnehmer(Gruppe g, boolean[] n)
    {   super();
        teilnehmer = g;
        nfkt = n;
    }

    // Teilnehmer in Rolle Person
    public Teilnehmer(Person p, boolean[] n)
    {   super();
        teilnehmer = p;
        nfkt = n;
    }

    public Teilnehmer(Person p)
    {   super();
        teilnehmer = p;
        nfkt = null;
    }

    public Teilnehmer(Gruppe g)
    {   super();
        teilnehmer = g;
        nfkt = null;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public String toString()
    {

        if (nfkt != null)
        {
            for (int i = 0; i < nfkt.length; i++)
            System.out.println(teilnehmer.toString()+nfkt[i]);
        }
        return new String(teilnehmer.toString());
    }

    public boolean[] getNotifikationen()
    {   return nfkt;
    }

    public void setNotifikationen(boolean[] n)
    {   nfkt = n;
    }

    public Object getTeilnehmer()
    {   return teilnehmer;
    }

    public boolean isTeilnehmer(Person person)
    {
        if(teilnehmer instanceof Person)
        {   return ((Person) teilnehmer).getID() == person.getID();

        } else
        {   return ((Gruppe) teilnehmer).isMember(person);
        }
    }

    public void swap(ResultSet res)
    {
        try
        {
            map = getMapping();
            String n = res.getString(map.getAttribute("nfkt").getColumn());
            if (n != null)
            {
                nfkt = new boolean[n.length()];
                for (int i = 0; i < n.length(); i++)
                {
                    nfkt[i] = (n.charAt(i) == '1');
                }

            }

        }catch (SQLException s) {System.out.println(s);}
    }

    public Object getValue(String attribute)
    {
        if (attribute.equals("objectIdentifier"))
        {
            if (teilnehmer.getClass().equals(Person.class))
                return new Long(((Person)teilnehmer).getID());
            else
                return new Long(((Gruppe)teilnehmer).getID());
        }

        if (attribute.equals("nfkt"))
        {
            StringBuffer buf = new StringBuffer();
            int i = 0;

            if (nfkt != null)
            {
                for (i = 0; i < nfkt.length; i++)
                    if (nfkt[i]) buf.append("1");
                    else buf.append("0");
            }
            return new String(buf);
        }
        return null;
    }
}

