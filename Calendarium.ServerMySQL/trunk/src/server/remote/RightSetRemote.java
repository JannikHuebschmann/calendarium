package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import server.Server;
import basisklassen.EintragsTyp;
import basisklassen.Gruppe;
import basisklassen.Person;
import basisklassen.Recht;
import basisklassen.Rechte;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.DeleteCriteria;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;

////////////////////////////////////////////////////////////////////////////////////////////
// RightSetRemote // RightSetRemote // RightSetRemote // RightSetRemote // RightSetRemote //
////////////////////////////////////////////////////////////////////////////////////////////

public class RightSetRemote extends UnicastRemoteObject implements interfaces.RightSetInterface
{
	// Server
    private Server server;

	// Daten
	private Hashtable grantAtGroups = new Hashtable();
    private Hashtable grantAtPersons = new Hashtable();

    private ClassMap map;

    public RightSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "RightSetRemote", this);

        } catch(Exception e)
        {   e.printStackTrace();
        }

        load();
    }

	///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
	private void load()
    {
        Hashtable rVergebenForGroups,rVergebenForPersons;
        ResultSet res,res1;
        long sender,empfänger,eintragsTyp;
        int rechtsIndex;
        Vector content;

        map = ClassMappings.getClass("Rechte");

        try
        {
            PersistentCriteria pc = new RetrieveCriteria("Rechte","Admin");
            res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                rVergebenForGroups = new Hashtable();
                rVergebenForPersons = new Hashtable();

                Rechte rechte = new Rechte();
                rechte.swap(res);

                eintragsTyp = rechte.getEintragsTypID();
                rechtsIndex = rechte.getRechtsIndex();

                sender = rechte.getSenderID();
                empfänger = rechte.getRec_PersonID();

                if (empfänger == 0)
                {
                    empfänger = rechte.getRec_GruppeID();
                    if (grantAtGroups.get(new Long(sender)) != null)
                         rVergebenForGroups = (Hashtable) grantAtGroups.get(new Long(sender));

                    if (!rVergebenForGroups.containsKey(new Long(empfänger)))
                    {
                        content = new Vector();
                        content.addElement(new Recht((EintragsTyp) server.getTypSetRemote().getDefaultTypen().get(new Long(eintragsTyp)),rechtsIndex));
                        rVergebenForGroups.put(new Long(empfänger),content);
                    }
                    else
                    {
                        content = (Vector)rVergebenForGroups.get(new Long(empfänger));
                        content.addElement(new Recht((EintragsTyp) server.getTypSetRemote().getDefaultTypen().get(new Long(eintragsTyp)),rechtsIndex));

                    }
                }
                else
                {

                    if (!rVergebenForPersons.containsKey(new Long(empfänger)))
                    {
                        content = new Vector();
                        content.addElement(new Recht((EintragsTyp) server.getTypSetRemote().getDefaultTypen().get(new Long(eintragsTyp)),rechtsIndex));
                        rVergebenForPersons.put(new Long(empfänger),content);
                    }
                    else
                    {
                        content = (Vector)rVergebenForPersons.get(new Long(empfänger));
                        content.addElement(new Recht((EintragsTyp) server.getTypSetRemote().getDefaultTypen().get(new Long(eintragsTyp)),rechtsIndex));

                    }
                }

		        grantAtGroups.put(new Long(sender),rVergebenForGroups);
		        grantAtPersons.put(new Long(sender),rVergebenForPersons);


            }

        }catch (Exception e){System.out.println(e+"RI");}

    }


	// User löschen
    public void deletePerson(Person person)
    {
        Long id = new Long(person.getID());

        if(grantAtGroups != null)
        {
            if(grantAtGroups.containsKey(id))
            {
                grantAtGroups.remove(id);
            }
        }

        if(grantAtPersons != null)
        {
            if(grantAtPersons.containsKey(id))
            {   grantAtPersons.remove(id);
            }
        }

        Enumeration e = grantAtPersons.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Hashtable userHash = (Hashtable) grantAtPersons.get(pid);

            if(userHash.containsKey(id))
            {
                userHash.remove(id);
                if(userHash.size() == 0) grantAtPersons.remove(pid);
            }
        }


        PersistentCriteria pc = new DeleteCriteria("Rechte","Admin");
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("sender",new Long(person.getID())) + pc.addOrCriteria() + pc.addSelectEqualTo("receiver_person",new Long(person.getID())));

	}

	// Gruppe löschen
	public void deleteGruppe(Gruppe gruppe)
    {
        Long id = new Long(gruppe.getID());

        Enumeration e = grantAtGroups.keys();
        while(e.hasMoreElements())
        {
            Long pid = (Long) e.nextElement();
            Hashtable userHash = (Hashtable) grantAtGroups.get(pid);

            if(userHash.containsKey(id))
            {
                userHash.remove(id);
                if(userHash.size() == 0) grantAtGroups.remove(pid);

                if(!gruppe.getAdminFlag()) break;
            }
        }

        PersistentCriteria pc = new DeleteCriteria("Rechte","Admin");
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("receiver_gruppe",new Long(gruppe.getID())));

	}

	// EintragsTyp löschen
	public void deleteEintragsTyp(EintragsTyp typ)
	{
	    Enumeration e, enumeration;

	    // Gruppen
	    e = grantAtGroups.keys();
	    while(e.hasMoreElements())
	    {
	        Long personID = (Long) e.nextElement();
	        Hashtable groupHash = (Hashtable) grantAtGroups.get(personID);

	        enumeration = groupHash.keys();
	        while(enumeration.hasMoreElements())
	        {
	            Long id = (Long) enumeration.nextElement();
	            Vector rechte = (Vector) groupHash.get(id);

	            int count = 0;
	            while(count < rechte.size())
	            {
	                Recht recht = (Recht) rechte.elementAt(count);

	                if(recht.getEintragsTyp().getID() == typ.getID())
	                {   rechte.removeElement(recht);

	                } else count++;
	            }
	            if(rechte.size() == 0) groupHash.remove(id);
	        }
	        if(groupHash.size() == 0) grantAtGroups.remove(personID);
	    }

	    // Personen
	    e = grantAtPersons.keys();
	    while(e.hasMoreElements())
	    {
	        Long personID = (Long) e.nextElement();
	        Hashtable personHash = (Hashtable) grantAtPersons.get(personID);

	        enumeration = personHash.keys();
	        while(enumeration.hasMoreElements())
	        {
	            Long id = (Long) enumeration.nextElement();
	            Vector rechte = (Vector) personHash.get(id);

	            int count = 0;
	            while(count < rechte.size())
	            {
	                Recht recht = (Recht) rechte.elementAt(count);

	                if(recht.getEintragsTyp().getID() == typ.getID())
	                {   rechte.removeElement(recht);

	                } else count++;
	            }
	            if(rechte.size() == 0) personHash.remove(id);
	        }
	        if(personHash.size() == 0) grantAtPersons.remove(personID);
	    }

        PersistentCriteria pc = new DeleteCriteria("Rechte","Admin");
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("eintragsTyp",new Long(typ.getID())));

	}

    //////////////////////////////////////////////////////////////////////////////////
    // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte // Vergebene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // fuer Gruppen vergebene Rechte
    public Hashtable getGroupGrants(Person sender) throws RemoteException
    {
        Long id = new Long(sender.getID());

        if(grantAtGroups != null)
        {   return (Hashtable) grantAtGroups.get(id);

        } else return null;
    }

    // fuer Personen vergebene Rechte
    public Hashtable getPersonGrants(Person sender) throws RemoteException
    {
        Long id = new Long(sender.getID());

        if(grantAtPersons != null)
        {   return (Hashtable) grantAtPersons.get(id);

        } else return null;
    }

    // fuer eine Gruppe vergebene Rechte
	public Vector getGrantsAt(Person sender, Gruppe empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    if(grantAtGroups != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

	        if(userGrant != null)
	        {   return (Vector) userGrant.get(idE);
	        }
	    }

	    return null;
	}

    // fuer eine Person vergebene Rechte
	public Vector getGrantsAt(Person sender, Person empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    if(grantAtPersons != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

	        if(userGrant != null)
	        {   return (Vector) userGrant.get(idE);
	        }
	    }

	    return null;
	}

    // Rechte einer Person erweitern
	public void addGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

	    Vector rechte = (Vector) userGrant.get(idE);
	    rechte.addElement(recht);

	    Rechte r = new Rechte(sender.getID(),0,empfänger.getID(),recht.getEintragsTyp().getID(),recht.getRechtsIndex());
	    r.setKz(sender.getKuerzel());
	    r.save();
	    //PersistentCriteria pc = new InsertCriteria("Rechte",sender.getKuerzel(),r);
	    //pc.perform(pc.buildForObject());
	}


    // Rechte einer Gruppe erweitern
	public void addGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

	    Vector rechte = (Vector) userGrant.get(idE);
	    rechte.addElement(recht);

	    Rechte r = new Rechte(sender.getID(),empfänger.getID(),0,recht.getEintragsTyp().getID(),recht.getRechtsIndex());
	    r.setKz(sender.getKuerzel());
	    r.save();
	    /*PersistentCriteria pc = new InsertCriteria("Rechte",sender.getKuerzel(),r);
	    pc.perform(pc.buildForObject());*/


	}

    // Person ein Recht entziehen
	public void retractGrantAt(Person sender, Person empfänger, Recht recht) throws RemoteException
    {
        Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    Vector rechte = (Vector) userGrant.get(idE);

	    Enumeration e = rechte.elements();
	    Recht r;

	    while(e.hasMoreElements())
	    {
	        r = (Recht) e.nextElement();

	        if(r.equals(recht))
	        {
	            rechte.removeElement(r);
	            break;
	        }
	    }

        PersistentCriteria pc = new DeleteCriteria("Rechte",sender.getKuerzel());
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("sender",new Long(sender.getID())) + pc.addAndCriteria() + pc.addSelectEqualTo("receiver_person",new Long(empfänger.getID())) + pc.addAndCriteria() +
                   pc.addSelectEqualTo("eintragsTyp",new Long(recht.getEintragsTyp().getID())) + pc.addAndCriteria() + pc.addSelectEqualTo("rechtsIndex",new Long(recht.getRechtsIndex())));

    }

	// Gruppe ein Recht entziehen
	public void retractGrantAt(Person sender, Gruppe empfänger, Recht recht) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    Vector rechte = (Vector) userGrant.get(idE);

	    Enumeration e = rechte.elements();
	    Recht r;

	    while(e.hasMoreElements())
	    {
	        r = (Recht) e.nextElement();

	        if(r.equals(recht))
	        {
	            rechte.removeElement(r);
	            break;
	        }
	    }

        PersistentCriteria pc = new DeleteCriteria("Rechte",sender.getKuerzel());
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("sender",new Long(sender.getID())) + pc.addAndCriteria() + pc.addSelectEqualTo("receiver_gruppe",new Long(empfänger.getID())) + pc.addAndCriteria() +
                   pc.addSelectEqualTo("eintragsTyp",new Long(recht.getEintragsTyp().getID())) + pc.addAndCriteria() + pc.addSelectEqualTo("rechtsIndex",new Long(recht.getRechtsIndex())));
	}

	// Person berechtigen
	public void addReceiver(Person sender, Person empfänger, Vector rechte) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    if(userGrant == null)
	    {
	        userGrant = new Hashtable();
	        userGrant.put(idE, rechte);

	        grantAtPersons.put(idS, userGrant);

	    } else
	    {
	        userGrant.put(idE, rechte);
	    }

	    int i = 0;
	    while (i < rechte.size())
	    {
    	    Rechte r = new Rechte(sender.getID(),0,empfänger.getID(),((Recht)rechte.elementAt(i)).getEintragsTyp().getID(),((Recht)rechte.elementAt(i++)).getRechtsIndex());
    	    r.setKz(sender.getKuerzel());
    	    r.save();
	        //PersistentCriteria pc = new InsertCriteria("Rechte",sender.getKuerzel(),r);
	        //pc.perform(pc.buildForObject());
	    }

	}

    // Gruppe berechtigen
	public void addReceiver(Person sender, Gruppe empfänger, Vector rechte) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    if(userGrant == null)
	    {
	        userGrant = new Hashtable();
	        userGrant.put(idE, rechte);

	        grantAtGroups.put(idS, userGrant);

	    } else
	    {
	        userGrant.put(idE, rechte);
	    }

	    int i = 0;
	    while (i < rechte.size())
	    {
    	    Rechte r = new Rechte(sender.getID(),empfänger.getID(),0,((Recht)rechte.elementAt(i)).getEintragsTyp().getID(),((Recht)rechte.elementAt(i++)).getRechtsIndex());
    	    r.setKz(sender.getKuerzel());
    	    r.save();
//	        PersistentCriteria pc = new InsertCriteria("Rechte",sender.getKuerzel(),r);
	//        pc.perform(pc.buildForObject());
	    }
	}

	// Person entfernen
	public void deleteReceiver(Person sender, Person empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);
	    userGrant.remove(idE);

	    if(userGrant.size() == 0)
	    {   grantAtPersons.remove(idS);
	    }

        PersistentCriteria pc = new DeleteCriteria("Rechte",sender.getKuerzel());
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("receiver_person",new Long(empfänger.getID())));


	}

    // Gruppe entfernen
	public void deleteReceiver(Person sender, Gruppe empfänger) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Long idE = new Long(empfänger.getID());

	    Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);
	    userGrant.remove(idE);

	    if(userGrant.size() == 0)
	    {   grantAtGroups.remove(idS);
	    }

        PersistentCriteria pc = new DeleteCriteria("Rechte",sender.getKuerzel());
        pc.perform(pc.buildForObject() + pc.addSelectEqualTo("receiver_gruppe",new Long(empfänger.getID())));
	}

	// Hashtable von Personen
	public Hashtable getGrantPersons(Person sender) throws RemoteException
	{
	    Long idS = new Long(sender.getID());
	    Hashtable result = new Hashtable();

	    if(grantAtPersons != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtPersons.get(idS);

    	    if(userGrant != null)
    	    {
    	        Enumeration e = userGrant.keys();
    	        while(e.hasMoreElements())
    	        {
    	            long id = ((Long) e.nextElement()).longValue();
    	            result.put(new Long(id), server.getPersonSetRemote().getByID(id));
    	        }
    	    }
	    }

	    return result;
	}

    // Hashtable von Gruppen
	public Hashtable getGrantGroups(Person sender) throws RemoteException
	{
		Long idS = new Long(sender.getID());
	    Hashtable result = new Hashtable();

	    if(grantAtGroups != null)
	    {
	        Hashtable userGrant = (Hashtable) grantAtGroups.get(idS);

    	    if(userGrant != null)
    	    {
    	        Enumeration e = userGrant.keys();
    	        while(e.hasMoreElements())
    	        {
    	            long id = ((Long) e.nextElement()).longValue();
    	            result.put(new Long(id), server.getGroupSetRemote().getByID(id));
    	        }
    	    }
	    }

	    return result;
	}

    //////////////////////////////////////////////////////////////////////////////////
    // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte // Erhaltene Rechte //
    //////////////////////////////////////////////////////////////////////////////////

    // Sämtliche erhaltene Rechte
    public Hashtable getRechte(Person empfänger) throws RemoteException
    {
        Long receiverID = new Long(empfänger.getID());
		Hashtable getByPersons = new Hashtable();

		if(grantAtPersons != null)
		{
			// Rechte als Person
			Enumeration e = grantAtPersons.keys();

			while(e.hasMoreElements())
			{
				Long granterID = (Long) e.nextElement();

				if(granterID != receiverID)
				{
					Hashtable hash = (Hashtable) grantAtPersons.get(granterID);

					if(hash.containsKey(receiverID))
					{
						Vector rechte = (Vector) hash.get(receiverID);
						Person granter = server.getPersonSetRemote().getByID(granterID.longValue());

						getByPersons.put(granter, rechte);
					}
				}
			}
		}

		if(grantAtGroups != null)
		{
			// Rechte als Gruppenmitglied
			Enumeration e = grantAtGroups.keys();

			while(e.hasMoreElements())
			{
				Long granterID = (Long) e.nextElement();

				if(granterID != receiverID)
				{
					Hashtable hash = (Hashtable) grantAtGroups.get(granterID);

					Enumeration enumeration = hash.keys();
					while(enumeration.hasMoreElements())
					{
						Long gruppenID = (Long) enumeration.nextElement();

						if(server.getGroupSetRemote().getByID(gruppenID.longValue()).isMember(empfänger))
						{
							Vector rechte = (Vector) hash.get(gruppenID);
							Person granter = server.getPersonSetRemote().getByID(granterID.longValue());

							if(!getByPersons.containsKey(granter))
							{
								getByPersons.put(granter, rechte);

							} else
							{
								Vector granted = (Vector) getByPersons.get(granter);

								Enumeration rechteEnum = rechte.elements();
								while(rechteEnum.hasMoreElements())
								{
									Recht recht = (Recht) rechteEnum.nextElement();
									boolean found = false;

									Enumeration grantedEnum = granted.elements();
									while(grantedEnum.hasMoreElements())
									{
										if(recht.equals((Recht) grantedEnum.nextElement()))
										{	found = true;
											break;
										}
									}

									if(!found) granted.addElement(recht);
								}
							}
						}
					}
				}
			}
		}

		return getByPersons;
    }

    // Ist jemand bei jemanden berechtigt?
	public boolean isBerechtigt(Person jemand, Person bei, Recht recht) throws RemoteException
	{
	    Long jemandID = new Long(jemand.getID());
	    Long beiID = new Long(bei.getID());

        // Personen
	    if(grantAtPersons != null)
        {
            Hashtable personGrants = (Hashtable) grantAtPersons.get(beiID);

            if(personGrants != null)
            {
                Vector rechte = (Vector) personGrants.get(jemandID);

                if(rechte != null)
                {
                    Enumeration e = rechte.elements();
                    while(e.hasMoreElements())
                    {
                        Recht r = (Recht) e.nextElement();

                        // Rechtshierachie
                        if(recht.getEintragsTyp().getID() == r.getEintragsTyp().getID() &&
                           recht.getRechtsIndex() <= r.getRechtsIndex())

                        return true;
                    }
                }
            }
        }

        // Gruppen
        if(grantAtGroups != null)
        {
            Hashtable groupGrants = (Hashtable) grantAtGroups.get(beiID);

            if(groupGrants != null)
            {
                Enumeration e = groupGrants.keys();
                while(e.hasMoreElements())
                {
                    Long id = (Long) e.nextElement();

                    if(server.getGroupSetRemote().getByID(id.longValue()).isMember(jemand))
    			    {
    				    Vector rechte = (Vector) groupGrants.get(id);

    				    if(rechte != null)
                        {
                            Enumeration enumeration = rechte.elements();
                            while(enumeration.hasMoreElements())
                            {
                                Recht r = (Recht) enumeration.nextElement();
                                if(r.equals(recht)) return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
	}
}