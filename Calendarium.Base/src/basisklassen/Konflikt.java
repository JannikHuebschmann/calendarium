package basisklassen; //
////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////
// Konflikt // Konflikt // Konflikt // Konflikt // Konflikt // Konflikt // Konflikt // Konflikt //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Konflikt implements java.io.Serializable
{
	private static final long serialVersionUID = -8821189517297696584L;
	private Person person;
    private Datum von, bis;
    private boolean verschiebbar;

    public Konflikt(Person p, Datum v, Datum b, boolean vb)
    {   
        person = p;
        von = v;
        bis = b;
        verschiebbar = vb;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public Person getPerson()
    {   return person;
    }

    public Datum getBeginn()
    {   return von;
    }

    public Datum getEnde()
    {   return bis;
    }
    
    public boolean isVerschiebbar()
    {   return verschiebbar;
    }
}
