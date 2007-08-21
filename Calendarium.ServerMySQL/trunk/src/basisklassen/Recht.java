package basisklassen; //

////////////////////////////////////////////////////////////////////////////////////////////
// Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht // Recht //
////////////////////////////////////////////////////////////////////////////////////////////

public class Recht /*extends dblayer.PersistentObject*/ implements java.io.Serializable
{
	private static final long serialVersionUID = 1402351915235161720L;
	private EintragsTyp eintragsTyp;
    private int rechtsIndex;

    public Recht(int r)
    {   rechtsIndex = r;
    }

    public Recht(EintragsTyp t, int r)
    {
        eintragsTyp = t;    // Lokale bzw. globale Eintragstyp
        rechtsIndex = r;    // Rechtsindex
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen // Funktionen //
    //////////////////////////////////////////////////////////////////////////////////////

    public EintragsTyp getEintragsTyp()
    {   return eintragsTyp;
    }

    public int getRechtsIndex()
    {   return rechtsIndex;
    }

    public void setEintragsTyp(EintragsTyp t)
    {   eintragsTyp = t;
    }

    public boolean equals(Recht r)
    {   return r.getEintragsTyp().getID() == eintragsTyp.getID() && r.getRechtsIndex() == rechtsIndex;
    }
}
