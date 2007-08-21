package server.remote; //
/////////////////////////

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import server.NfktQueue;
import server.Server;
import basisklassen.Datum;
import basisklassen.Gruppe;
import basisklassen.Konflikt;
import basisklassen.Notifikation;
import basisklassen.Person;
import basisklassen.Serie;
import basisklassen.Teilnehmer;
import basisklassen.Termin;
import dblayer.ClassMap;
import dblayer.ClassMappings;
import dblayer.PersistentCriteria;
import dblayer.RetrieveCriteria;
import event.MessageEvent;
import event.TerminEvent;

/////////////////////////////////////////////////////////////////////////////////////////////////
// TerminSetRemote // TerminSetRemote // TerminSetRemote // TerminSetRemote // TerminSetRemote //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class TerminSetRemote extends UnicastRemoteObject implements interfaces.TerminSetInterface
{
	// Server
    private Server server;

	// Daten
	private Hashtable terminIDs = new Hashtable();
	private Hashtable termine = new Hashtable();
	private ClassMap map;

    // Konstanten
    private int len = 18;
    private String leerstring = "                                                              ";
    private char c = 34;

    public TerminSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "TerminSetRemote", this);

        } catch(Exception e)
        {   e.printStackTrace();
        }

        load();

        // NfktQueue
        server.getNfktQueue().createNotifiers(terminIDs);
    }

    //
	///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
	private void load()
    {
        ResultSet res,res1;
        Teilnehmer tn;
        long id,id1;

        map = ClassMappings.getClass("Termin");
        try
        {
            PersistentCriteria pc = new RetrieveCriteria("Termin","Admin");
            res = pc.perform(pc.buildForObject());

            while (res.next())
            {
                id = res.getLong(map.getAttribute("objectIdentifier").getColumn());
                Termin termin = new Termin(id);
                termin.swap(res);
                termin.setKz("Admin");

                terminIDs.put(new Long(id),termin);    // insert into hashtables
                addToTerminListe(termin);

                res1 = termin.retrieveInnerTable("teilnehmer");
                Vector teilnehmer = new Vector();
                while (res1.next())
                {
                    id1 = res1.getLong(map.getAttribute("teilnehmer").getNestedAttribute("objectIdentifier").getColumn());

                    Person person = server.getPersonSetRemote().getByID(id1);
                    if (person == null)
                    {
                        Gruppe gruppe = server.getGroupSetRemote().getByID(id1);
                        tn = new Teilnehmer(gruppe);
                        tn.swap(res1);
                        teilnehmer.addElement(tn);
                    }
                    else
                    {
                         tn = new Teilnehmer(person);
                         tn.swap(res1);
                         teilnehmer.addElement(tn);

                    }
                }
                termin.setTeilnehmer(teilnehmer);
                res1.close();

                res1 = termin.retrieveInnerTable("nfkt");
                Notifikation[] nfkt = new Notifikation[3];
                int i = 0;
                while (res1.next())
                {
                    id1 = res1.getLong(map.getAttribute("nfkt").getNestedAttribute("objectIdentifier").getColumn());
                    nfkt[i] = new Notifikation(id1);
                    nfkt[i].setKz("Admin");
                    nfkt[i++].retrieve();
                }
                res1.close();

                if (i != 0)
                {
                    Notifikation n[] = new Notifikation[i];
                    for(i = 0; i < n.length; i++)
                        n[i] = nfkt[i];

                    termin.setNotifikationen(n);
                }
                else termin.setNotifikationen(null);

                /*id = res.getLong(map.getAttribute("serie").getColumn());   // set serie
                if (id != 0)
                {
                    Serie serie = new Serie(id);
                    serie.setKz("Admin");
                    serie.retrieve(null,null);

                    termin.setSerie(serie);
                }*/

            }
        }catch (Exception e) {System.out.println(e+"TE");}

    }




    ///////////////////////////////////////////////////////////////////////////////////////
    // MessageEvents // MessageEvents // MessageEvents // MessageEvents // MessageEvents //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Message: Neuer Termin
    private void sendNewMessages(Termin termin)
    {
        Hashtable persHash = termin.getAllPersonsWithNfkt();
        String message = getTerminText(persHash, termin);

        Enumeration e = persHash.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();
            String msg = message + getNfktText(termin, tn);

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(termin.getOwner(), (Person) tn.getTeilnehmer(),
                                                msg, "Neuer Termin\n");  //??????????????
            // send
            server.getMessageServer().addEvent(evt);
        }
    }

    // Message: Terminabsage
    private void sendDeleteMessages(Termin termin, boolean allOfSerie)
    {
        Hashtable persHash = termin.getAllPersonsWithNfkt();
        String message;

        if(termin.getSerie() == null)
        {   message = "Der Termin " + c + termin.getKurzText() + c + " am " +
                      termin.getBeginn().toString() + " wurde abgesagt.";
        } else
        {   if(allOfSerie)
            {   message = "Der Serientermin " + c + termin.getKurzText() + c + " wurde geloescht.";
            } else
            {   message = "Der Termin " + c + termin.getKurzText() + c + " am " +
                          termin.getBeginn().toString() + " wurde aus der zugehoerigen Serie geloescht.";
            }
        }

        Enumeration e = persHash.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(termin.getOwner(), (Person) tn.getTeilnehmer(),
                                                message, "Terminabsage");
            // send
            server.getMessageServer().addEvent(evt);
        }
    }

    // Messages: Terminänderung
    private void sendUpdateMessages(Termin terminAlt, Termin terminNeu)
    {
        // Teilnehmer
        Hashtable persHashAlt = terminAlt.getAllPersonsWithNfkt();
        Hashtable persHashNeu = terminNeu.getAllPersonsWithNfkt();

        // Notifikationen
        Notifikation[] nfktAlt = terminAlt.getNotifikationen();
        Notifikation[] nfktNeu = terminNeu.getNotifikationen();

        String changeText = getChangeText(persHashAlt, terminAlt, persHashNeu, terminNeu);
        String nfktText = "", message = "";
        String text = "", head = "";

        if(terminAlt.getSerie() == null || terminNeu.getSerie() == null)
        {   head = "Aenderungen des Termins " + c + terminAlt.getKurzText() + c + " am " +
                   terminAlt.getBeginn().toString() + "\n\n";
        } else
        {   head = "Aenderungen des Serientermins " + c + terminAlt.getKurzText() + c + " beginnend mit " +
                   terminAlt.getBeginn().toString() + "\n\n";
        }

        head += leerstring.substring(0, len) + "Liste der Aenderungen\n" +
                leerstring.substring(0, len) + "--------------------\n";

        Enumeration e = persHashNeu.keys();
        while(e.hasMoreElements())
        {
            Long id = (Long) e.nextElement();
            Teilnehmer tn = (Teilnehmer) persHashNeu.get(id);

            if(!persHashAlt.containsKey(id))
            {

				if(terminNeu.getSerie() == null)
				{	// neuer Teilnehmer
					message = "Sie wurden dem Termin " + c + terminNeu.getKurzText() + c + " am " +
                              terminNeu.getBeginn().toString() + "\nals Teilnehmer neu hinzugefuegt.\n\n" +
                              getTerminText(persHashNeu, terminNeu) + getNfktText(terminNeu, tn);
				} else
				{	message = "Sie wurden dem Serientermin " + c + terminNeu.getKurzText() + c + " beginnend mit " +
				              terminNeu.getBeginn().toString() + "\nals Teilnehmer neu hinzugefuegt.\n\n" +
							  getTerminText(persHashNeu, terminNeu) + getNfktText(terminNeu, tn);
				}


                //////////////////////////////////////////////////////////////////////////
                // Message senden // Message senden // Message senden // Message senden //
                //////////////////////////////////////////////////////////////////////////

                MessageEvent evt = new MessageEvent(terminNeu.getOwner(),
                                                   (Person) tn.getTeilnehmer(),
                                                    message, "Neuer Termin");
                // send
                server.getMessageServer().addEvent(evt);

            } else
            {
                // Notifikationen
                boolean[] nfNeu = ((Teilnehmer) persHashNeu.get(id)).getNotifikationen();
                boolean[] nfAlt = ((Teilnehmer) persHashAlt.get(id)).getNotifikationen();

                try
                {
                    for(int i = 0; i < Math.min(nfNeu.length,nfAlt.length); i++)
                    {
                        text = "Notifikation " + (i + 1) + ": ";

                        if(nfNeu[i] && !nfAlt[i])
                        {
                            // Teilnehmer zur Nfkt hinzugefuegt
                            nfktText += leerstring.substring(0, len - text.length()) + text +
                                        nfktNeu[i].toString() + " (neu)\n";

                        } else if(!nfNeu[i] && nfAlt[i])
                        {
                            // Teilnehmer von Nfkt gelöscht
                            nfktText += leerstring.substring(0, len - text.length()) + text +
                                        nfktAlt[i].toString() + " (geloescht)\n";

                        } else if(nfNeu[i] && nfAlt[i] && !nfktNeu[i].equals(nfktAlt[i]))
                        {
                            // Nofikation geändert
                            nfktText += leerstring.substring(0, len - text.length()) + text +
                                        nfktNeu[i].toString() + '\n';
                        }
                    }

                    if(nfNeu.length > nfAlt.length)
                    {
                        for(int i = nfAlt.length; i < nfNeu.length; i++)
                        {
                            // Neue Notifikation
                            if(nfNeu[i])
                            {text = "Notifikation " + (i + 1) + ": ";
                                nfktText += leerstring.substring(0, len - text.length()) + text +
                                            nfktNeu[i].toString() + " (neu)\n";
                            }
                        }

                    } else
                    {
                        for(int i = nfNeu.length; i < nfAlt.length; i++)
                        {
                            // Notifikation gelöscht
                            if(nfAlt[i])
                            {   text = "Notifikation " + (i + 1) + ": ";
                                nfktText += leerstring.substring(0, len - text.length()) + text +
                                            nfktAlt[i].toString() + " (geloescht)\n";
                            }
                        }
                    }
                }catch (Exception ex){System.out.println(ex);}

                if(changeText.length() > 0 || nfktText.length() > 0)
                {   message = head + changeText + nfktText;
                }

                //////////////////////////////////////////////////////////////////////////
                // Message senden // Message senden // Message senden // Message senden //
                //////////////////////////////////////////////////////////////////////////

                if(message.length() > 0)
                {
                    MessageEvent evt = new MessageEvent(terminNeu.getOwner(),
                                                       (Person) tn.getTeilnehmer(),
                                                        message, "Termin-Aenderung");
                    // send
                    server.getMessageServer().addEvent(evt);
                }
            }
        }


    }



    // TerminText
    private String getTerminText(Hashtable persHash, Termin termin)
    {
        String text, msg = "";

        msg += leerstring.substring(0, len) + "Termindaten:\n" +
               leerstring.substring(0, len) + "------------\n";

        text = "Beginn: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getBeginn().toString() + '\n';

        text = "Ende: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getEnde().toString() + '\n';

        text = "Dauer: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getEnde().getHoursBetween(termin.getBeginn()) + " Stunden\n";

        text = "Typ: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getTyp().getBezeichnung() + '\n';

        text = "Kurztext: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getKurzText() + '\n';

        text = "Langtext: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getLangText() + '\n';

        text = "Hyperlink: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getHyperlink() + '\n';

        text = "Ort: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               termin.getOrt() + '\n';

        text = "Verschiebbar: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               (termin.getVerschiebbar() ? "Termin ist verschiebbar\n" :
                                           "Termin ist nicht verschiebbar\n");

        // Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer // Teilnehmer //
        msg += "\n";
        text = "Teilnehmer: ";

        Enumeration e = persHash.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();
            Person person = (Person) tn.getTeilnehmer();

            msg += leerstring.substring(0, len - text.length()) + text +
                   person.getNameLang() + '\n';

            text = "";
        }

        // Serie // Serie // Serie // Serie // Serie // Serie // Serie // Serie //
        if(termin.getSerie() != null)
        {
            msg += "\n";
            text = "Serie: ";

            msg += leerstring.substring(0, len - text.length()) + text +
                   termin.getSerie().toString() + '\n';

            if(termin.getSerie().getWerktags())
                msg += leerstring.substring(0, len) + "nur Werktags\n";
            else
                msg += leerstring.substring(0, len) + "auch Sonn- und Feiertags\n";

            msg += leerstring.substring(0, len) + "vom " +
                   termin.getSerie().getBeginn().getDate() + " bis " +
                   termin.getSerie().getEnde().getDate() + '\n';
        }

        return msg + '\n';
    }

    // NfktText
    private String getNfktText(Termin termin, Teilnehmer tn)
    {
        String text, msg = "";
        boolean[] nf = tn.getNotifikationen();

        if(termin.getNotifikationen() != null)
        {
            String h = leerstring.substring(0, len - 16) + "Sie werden erinnert durch:" + '\n' +
                       leerstring.substring(0, len - 16) + "--------------------------\n";

            for(int i = 0; i < termin.getNotifikationen().length; i++)
            {
                if(nf[i])
                {
                    msg += h;
                    h = "";

                    text = "Notifikation " + (i + 1) + ": ";
                    msg += leerstring.substring(0, len - text.length()) + text +
                           termin.getNotifikationen()[i].toString() + '\n';
                }
            }
        }
        return msg;
    }

    // ChangeText
    String getChangeText(Hashtable persHashAlt, Termin terminAlt,
                         Hashtable persHashNeu, Termin terminNeu)
    {
        String head, text, nl = "", msg = "";
        Hashtable kPersHashAlt = (Hashtable) persHashAlt.clone();
        Person p;

        //////////////////////////////////////////////////////////////////////////////////////
        // Vergleiche // Vergleiche // Vergleiche // Vergleiche // Vergleiche // Vergleiche //
        //////////////////////////////////////////////////////////////////////////////////////

        double dauer = terminNeu.getEnde().getHoursBetween(terminNeu.getBeginn());

        if(!terminNeu.getBeginn().equals(terminAlt.getBeginn()) ||
           !terminNeu.getEnde().equals(terminAlt.getEnde()))
        {
            if(!terminNeu.getBeginn().equals(terminAlt.getBeginn()))
            {
                // Terminverschiebung
                text = "Beginn: ";
                msg += leerstring.substring(0, len - text.length()) + text +
                       terminNeu.getBeginn().toString() + '\n';
            }

            if(dauer != terminAlt.getEnde().getHoursBetween(terminAlt.getBeginn()))
            {
                // Änderung der Dauer
                text = "Dauer: ";
                msg += leerstring.substring(0, len - text.length()) + text +
                       dauer + " Stunden\n";
            }
        }

        if(terminNeu.getTyp().getID() != terminAlt.getTyp().getID())
        {
            // Typänderung
            text = "Typ: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   terminNeu.getTyp().getBezeichnung() + '\n';
        }

        if(!terminNeu.getKurzText().equals(terminAlt.getKurzText()))
        {
            // Kurztext
            text = "Kurztext: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   terminNeu.getKurzText() + '\n';
        }
        if(!terminNeu.getLangText().equals(terminAlt.getLangText()))
        {
            // Langtext
            text = "Langtext: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   terminNeu.getLangText() + '\n';
        }
        if(!terminNeu.getHyperlink().equals(terminAlt.getHyperlink()))
        {
            // Hyperlink
            text = "Hyperlink: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   terminNeu.getHyperlink() + '\n';
        }

        if(!terminNeu.getOrt().equals(terminAlt.getOrt()))
        {
            // Ortsänderung
            text = "Ort: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   terminNeu.getOrt() + '\n';
        }

        if(terminNeu.getVerschiebbar() != terminAlt.getVerschiebbar())
        {
            // Änderung der Verschiebbarkeit
            text = "Verschiebbar: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   (terminNeu.getVerschiebbar() ? "Termin ist verschiebbar\n" :
                                                  "Termin ist nicht verschiebbar\n");
        }

        text = "Teilnehmer: ";
        if(msg.length() > 0) nl = "\n";

        Enumeration e = persHashNeu.keys();
        while(e.hasMoreElements())
        {
            Long id = (Long) e.nextElement();
            if(!kPersHashAlt.containsKey(id))
            {
                Person person = (Person) ((Teilnehmer) persHashNeu.get(id)).getTeilnehmer();

                // Neuer Teilnehmer
                msg += nl + leerstring.substring(0, len - text.length()) + text +
                       person.getNameLang() + " (neu)\n";

                text = "";
                nl = "";

            } else kPersHashAlt.remove(id);
        }

        e = kPersHashAlt.elements();
        while(e.hasMoreElements())
        {
            Person person = (Person) ((Teilnehmer) e.nextElement()).getTeilnehmer();

            // Gelöschter Teilnehmer
            msg += nl + leerstring.substring(0, len - text.length()) + text +
                   person.getNameLang() + " (geloescht)\n";

            text = "";
            nl = "";

            String message;

            if(terminAlt.getSerie() == null)
            {   message = "Sie wurden vom Termin " + c + terminAlt.getKurzText() + c + " am " +
                          terminAlt.getBeginn().toString() + "\nals Teilnehmer geloescht.\n";
            } else
            {   message = "Sie wurden vom Serientermin " + c + terminAlt.getKurzText() + c + " beginnend mit " +
                          terminAlt.getBeginn().toString() + "\nals Teilnehmer geloescht.\n";
            }

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(terminAlt.getOwner(), person,
                                                message, "Teilnahme abgesagt");
            // send
            server.getMessageServer().addEvent(evt);
        }

        text = "Serie: ";
        if(msg.length() > 0) nl = "\n";

        Serie serieNeu = terminNeu.getSerie();
        Serie serieAlt = terminAlt.getSerie();

        if(serieNeu != null)
        {
            if(serieAlt == null || serieNeu.getTyp() != serieAlt.getTyp() ||
                                   serieNeu.getFrequenz() != serieAlt.getFrequenz() ||
                                   serieNeu.getWerktags() != serieAlt.getWerktags())
            {
                // Serienattribut geändert
                msg += nl + leerstring.substring(0, len - text.length()) + text +
                       serieNeu.toString() + '\n';
                nl = "";

                if(serieNeu.getWerktags())
                    msg += leerstring.substring(0, len) + "nur Werktags\n";
                else
                    msg += leerstring.substring(0, len) + "auch Sonn- und Feiertags\n";
            }

			if(serieAlt == null || !serieNeu.getEnde().equals(serieAlt.getEnde()))
            {
                // Serienende geändert
                if(nl.length() > 0 || msg.length() == 0)
                {   msg += nl + leerstring.substring(0, len - text.length()) + text;
                } else
                {   msg += leerstring.substring(0, len);
                }

                msg += "vom " + serieNeu.getBeginn().getDate() + " bis " +
                       serieNeu.getEnde().getDate();
            }

            if(serieAlt == null)
            {
                // Neue Serie
                msg += " (neu)";
            }
        }

        if(msg.length() > 0) msg += "\n";

        return msg;
    }

    // Serie expandieren
    private Vector getSerienTermine(Termin termin)
    {
        Vector expanded = new Vector();
        Serie serie = termin.getSerie();

        // Beginn
        Datum bgn = new Datum();
        bgn.setDatum(termin.getBeginn());

        // Ende
        Datum end = serie.getEnde();

        // Frequenz
        int freq = serie.getFrequenz();

        // Werktags
        boolean wk = serie.getWerktags();

        switch(serie.getTyp())
        {
            case 0:     // täglich

                bgn.add(freq);
                while(end.isGreater(bgn) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null &&
                                   bgn.getWeekDay() < 6))
                        {
                            expanded.addElement(getTerminOfSerie(termin, bgn));
                        }
                    } catch(Exception e) {}

                    bgn.add(freq);
                }

                break;

            case 1:     // monatlich absolut

                bgn.addMonth(freq);
                while(end.isGreater(bgn) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null &&
                                   bgn.getWeekDay() < 6))
                        {
                            expanded.addElement(getTerminOfSerie(termin, bgn));
                        }
                    } catch(Exception e) {}

                    bgn.addMonth(freq);
                }

                break;


            case 2:     // monatlich relativ

                bgn.addMonth(serie.getBeginn(), freq);

                while(end.isGreater(bgn) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null &&
                                   bgn.getWeekDay() < 6))
                        {
                            expanded.addElement(getTerminOfSerie(termin, bgn));
                        }
                    } catch(Exception e) {}

                    bgn.addMonth(serie.getBeginn(), freq);
                }

                break;

            case 3:     // jährlich

                bgn.addYear(freq);
                while(end.isGreater(bgn) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null &&
                                   bgn.getWeekDay() < 6))
                        {
                            expanded.addElement(getTerminOfSerie(termin, bgn));
                        }
                    } catch(Exception e) {}

                    bgn.addYear(freq);
                }

                break;

            default:    // wöchentlich

                boolean[] sel = new boolean[7];
                int w = serie.getTyp() >> 2;
                int count = 0;

                while(w > 0)
                {
                    if(w % 2 == 1)
                    {   sel[count] = true;
                    } else
                    {   sel[count] = false;
                    }

                    w >>= 1;
                    count++;
                }

                // 1. Woche
                for(int i = bgn.getWeekDay() + 1; i < 7; i++)
                {
                    bgn.add(1);

                    if(end.isGreater(bgn) >= 0 && sel[i])
                    {
                        try
                        {   if(!wk || (i < 6 && server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null))
                            {   expanded.addElement(getTerminOfSerie(termin, bgn));
                            }
                        } catch(Exception e) {}
                    }
                }

                do
                {   bgn.add((freq - 1) * 7 );

                    for(int i = 0; i < 7; i++)
                    {
                        bgn.add(1);
                        if(end.isGreater(bgn) >= 0 && sel[i])
                        {
                            try
                            {   if(!wk || (i < 6 && server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null))
                                {   expanded.addElement(getTerminOfSerie(termin, bgn));
                                }
                            } catch(Exception e) {}
                        }
                    }

                } while(end.isGreater(bgn) >= 0);

        }

        return expanded;
    }

    private Termin getTerminOfSerie(Termin termin, Datum bgnNeu)
    {
        Termin terminNeu = new Termin(termin.getOwner());

        // Beginn
        terminNeu.getBeginn().setDatum(bgnNeu);

        // Ende
        double dauer = termin.getEnde().getHoursBetween(termin.getBeginn());
        terminNeu.setEnde(bgnNeu.addDauer(dauer));

        // uebrigen Attibute uebernehmen
        terminNeu.setKurzText(termin.getKurzText());
        terminNeu.setLangText(termin.getLangText());
        terminNeu.setTyp(termin.getTyp());
        terminNeu.setOrt(termin.getOrt());
        terminNeu.setHyperlink(termin.getHyperlink());
        terminNeu.setVerschiebbar(termin.getVerschiebbar());

        // Referenzen
        terminNeu.setNotifikationen(termin.getNotifikationen());
        terminNeu.setTeilnehmer(termin.getTeilnehmer());
        terminNeu.setSerie(termin.getSerie());

        return terminNeu;
    }

    // insert
    private void insertTermin(String kuerzel, Termin termin)
    {
		termin.setKz(kuerzel);
		termin.save();

        Enumeration e = termin.getTeilnehmer().elements();
        while(e.hasMoreElements())
        {
            Teilnehmer t = (Teilnehmer) e.nextElement();
            if (t.getTeilnehmer().getClass().equals(Person.class))
		        termin.save(t,"teilnehmer");
		    else
		        termin.save(t,"teilnehmer");

        }

        Notifikation nfkt[] = termin.getNotifikationen();
        int i = 0;
        if (nfkt != null)
        {
            while(i < nfkt.length)
            {
                nfkt[i].setKz(kuerzel);
        	    nfkt[i].save();

        	    termin.save(nfkt[i++],"nfkt");
	        }
	    }

        Serie serie = termin.getSerie();
        if (serie != null)
        {
            serie.setKz(kuerzel);
            serie.save();
        }

		terminIDs.put(new Long(termin.getID()),termin);
		addToTerminListe(termin);
    }

    // remove
    private void removeTermin(String kuerzel, Termin termin, boolean serie)
    {
		termin.setKz(kuerzel);
				 
		//First: delete all teilnehmers of termin.
		Vector e2 = termin.getTeilnehmer();
		Enumeration	e = e2.elements();
        while(e.hasMoreElements())
        {	
          	Teilnehmer t = (Teilnehmer) e.nextElement();
            termin.delete((Person)t.getTeilnehmer(),"teilnehmer");
        }
				
        
  
   //Then : delete all notifikation-termin relationships(nfktnested) 
       Notifikation nfkt2[] = termin.getNotifikationen();
        int i2 = 0;
        if (nfkt2 != null)
        {
            while(i2 < nfkt2.length)
            {  
            	 //   **** This function could be integrated into the existing delete of notifkations??
        	    termin.delete(nfkt2[i2++],"nfkt");
	        }
	    }
     
            
		
      //Delete the notifikations, but only if not a "Serientermin" 
        if ((!serie) && (termin.getSerie() == null))
        {
            Notifikation nfkt[] = termin.getNotifikationen();
            int i = 0;
            if (nfkt != null)
            {
                while(i < nfkt.length)
                {	
//                  **** insert nested-delete ?? tried, but didn't work correctly. 
                	//delete Relationship termin-notifikation(>>nested) 
                	//termin.delete(nfkt[i++],"nfkt");
                	//delete Notifikation
                    nfkt[i].setKz(kuerzel);
    	            nfkt[i++].delete();
	            }
	        }
	    }

//      delete the termin itself.
        termin.delete();
        
        // Hashtable
        terminIDs.remove(new Long(termin.getID()));

		// TerminListe
		removeFromTerminListe(termin);


    }

    // update
    private void updateTermin(String kuerzel, Termin terminAlt, Termin terminNeu)
    {
        Long id = new Long(terminAlt.getID());
        Enumeration e;

        // Hashtable
        terminIDs.put(id, terminNeu);

        if(!terminAlt.getBeginn().equals(terminNeu.getBeginn()) ||
           !terminAlt.getEnde().getDate().equals(terminNeu.getEnde().getDate()))
		{
		    removeFromTerminListe(terminAlt);
            addToTerminListe(terminNeu);

        } else
        {
            Datum dat = new Datum();
            dat.setDatum(terminNeu.getBeginn());

            int days = dat.getDaysBetween(terminNeu.getEnde());

            for(int i = 0; i <= days; i++)
            {
                // TerminListe
                Vector terminListe = (Vector) termine.get(dat.getDate());

                e = terminListe.elements();
                int count = 0;

                while(e.hasMoreElements())
                {
                    Termin t = (Termin) e.nextElement();
                    if(t.getID() == terminNeu.getID())
                    {
                        terminListe.setElementAt(terminNeu, count);
                        break;

                    } else count++;
                }

                dat.add(1);
            }
        }

        terminNeu.setID(terminAlt.getID());
        terminNeu.setPersistence(true);
        terminNeu.setKz(kuerzel);
        terminNeu.save();

        Vector teilnehmer = terminAlt.getTeilnehmer();
        e = teilnehmer.elements();
        while(e.hasMoreElements())
		{
            terminAlt.setKz(kuerzel);

		    Teilnehmer t = (Teilnehmer) e.nextElement();
    		if (t.getTeilnehmer().getClass().equals(Person.class))
                terminAlt.delete((Person) t.getTeilnehmer(),"teilnehmer");
            else
                terminAlt.delete((Gruppe) t.getTeilnehmer(),"teilnehmer");

        }

        teilnehmer = terminNeu.getTeilnehmer();
        e = teilnehmer.elements();
        while(e.hasMoreElements())
		{
            terminNeu.setKz(kuerzel);

		    Teilnehmer t = (Teilnehmer) e.nextElement();
    		if (t.getTeilnehmer().getClass().equals(Person.class))
                terminNeu.save(t,"teilnehmer");
            else
                terminNeu.save(t,"teilnehmer");
        }


        Serie serie = terminNeu.getSerie();
        if (serie != null)
        {
            if (terminAlt.getSerie() != null)
            {
                serie.setID(terminAlt.getSerie().getID());
                serie.setPersistence(true);
            }
            serie.setKz(kuerzel);
            serie.save();

        }

        Notifikation nfktAlt[] = terminAlt.getNotifikationen();
        int i = 0;
        if (nfktAlt != null)
        {
            while(i < nfktAlt.length)
            {
                nfktAlt[i].setKz(kuerzel);
    	        nfktAlt[i].delete();

        	    terminNeu.delete(nfktAlt[i++],"nfkt");
	        }
	    }


        Notifikation nfkt[] = terminNeu.getNotifikationen();
        if (nfkt != null)
        {
            i = 0;
            while (i < nfkt.length)
            {
                nfkt[i].setKz(kuerzel);
                nfkt[i].save();

        	    terminNeu.save(nfkt[i++],"nfkt");
            }
        }


    }

    private void addToTerminListe(Termin termin)
    {
        Datum dat = new Datum();
        dat.setDatum(termin.getBeginn());

        int days = dat.getDaysBetween(termin.getEnde());
        for(int i = 0; i <= days; i++)
        {
            // TerminListe
            Vector terminListe = (Vector) termine.get(dat.getDate());

            if(terminListe == null)
		    {
		        terminListe = new Vector();
		        terminListe.addElement(termin);

		        termine.put(dat.getDate(), terminListe);

		    } else
            {
                Enumeration e = terminListe.elements();
                int count = 0;

                while(e.hasMoreElements())
                {
                    Termin t = (Termin) e.nextElement();
                    if(t.getBeginn().isGreater(termin.getBeginn()) >= 0)
                    {
                        terminListe.insertElementAt(termin, count);
                        break;

                    } else count++;
                }

                if(count >= terminListe.size())
                {   terminListe.addElement(termin);
                }
            }

            dat.add(1);
        }
    }

    private void removeFromTerminListe(Termin termin)
    {
        Datum dat = new Datum();
        dat.setDatum(termin.getBeginn());

        int days = dat.getDaysBetween(termin.getEnde());

        for(int i = 0; i <= days; i++)
        {
            // TerminListe
            Vector terminListe = (Vector) termine.get(dat.getDate());

            Enumeration e = terminListe.elements();
            while(e.hasMoreElements())
            {
                Termin t = (Termin) e.nextElement();
                if(termin.getID() == t.getID())
                {
                    terminListe.removeElement(t);
                    break;
                }
            }

            if(terminListe.size() == 0) termine.remove(dat.getDate());
            dat.add(1);
        }
    }

    // Person löschen
    public void deletePerson(Person person)
    {
        Enumeration e = termine.keys();
        while(e.hasMoreElements())
        {
            String bgn = (String) e.nextElement();
            Vector termineAm = (Vector) termine.get(bgn);

            int count = 0;
            while(count < termineAm.size())
            {
                Termin t = (Termin) termineAm.elementAt(count);

                t.setKz("Admin");
                
                
                t.delete(person,"teilnehmer");
                if(t.getOwner()!=null&&t.getOwner().getID() == person.getID())
                {
                    termineAm.removeElement(t);
                    terminIDs.remove(new Long(t.getID()));

                    t.delete();

                } else count++;
            }

            if(termineAm.size() == 0) termine.remove(bgn);
        }

    }

    public void setErledigt(Notifikation n)
    {
        n.setKz("Admin");
        n.setPersistence(true);
        n.save();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Termine an einem Tag ausgeben
    public Vector getTermineVom(Datum vomDat, Vector personenListe) throws RemoteException
    {
        Vector termineAm = (Vector) termine.get(vomDat.getDate());
        Vector filtered = new Vector();

        if(termineAm != null)
        {
            Enumeration e = termineAm.elements();
            while(e.hasMoreElements())
            {
                Termin termin = (Termin) e.nextElement();

                if(termin.isRelevantFor(personenListe))
                {   filtered.addElement(termin);
                }
            }
        }

        return filtered;
    }

	// Termine in einem Zeitraum ausgeben
	public Vector[] getTermineVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException
	{
	    int count = vonDat.getDaysBetween(bisDat) + 1;
        Vector filtered[] = new Vector[count];

        for(int i = 0; i < count; i++)
        {
            Vector termineAm = (Vector) termine.get(vonDat.getDate());
            filtered[i] = new Vector();

            if(termineAm != null)
            {
                Enumeration e = termineAm.elements();
                while(e.hasMoreElements())
                {
                    Termin termin = (Termin) e.nextElement();

                    if(termin.isRelevantFor(personenListe))
                    {   filtered[i].addElement(termin);
                    }
                }
            }
            vonDat.add(1);
        }

        return filtered;
	}

    // Personen mit Terminen
    public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException
    {
        int count = vonDat.getDaysBetween(bisDat) + 1;
        Vector filtered[] = new Vector[count];

        for(int i = 0; i < count; i++)
        {
            Vector termineAm = (Vector) termine.get(vonDat.getDate());
            filtered[i] = new Vector();

            if(termineAm != null)
            {
                Enumeration e = termineAm.elements();
                while(e.hasMoreElements())
                {
                    Hashtable personen = ((Termin) e.nextElement()).getAllPersons();

                    Enumeration enumeration = personenListe.elements();
                    while(enumeration.hasMoreElements())
                    {
                        Person person = (Person) enumeration.nextElement();
                        if(personen.containsKey(new Long(person.getID())))
                        {
                            if(!filtered[i].contains(person))
                            {   filtered[i].addElement(person);
                            }
                        }
                    }
                }
            }
            vonDat.add(1);
        }

        return filtered;
    }

	// einen bestimmten Termin ausgeben
	public Termin getTerminByID(long id) throws RemoteException
	{   return (Termin) terminIDs.get(new Long(id));
	}

    // Konflikte ausgeben
    public Vector getKonflikte(Termin termin) throws RemoteException
    {
        // Konflikte
        Vector konflikte = new Vector();

        // bgn
        Datum bgn = new Datum();
        bgn.setDatum(termin.getBeginn());

        // end
        Datum end = termin.getEnde();

        // Teilnehmende Personen
        Hashtable persons = termin.getAllPersons();

        for(int i = 0; i <= bgn.getDaysBetween(end); i++)
        {
            Vector termineAm = (Vector) termine.get(bgn.getDate());

            if(termineAm != null)
            {
                Enumeration e = termineAm.elements();
                while(e.hasMoreElements())
                {
                    Termin t = (Termin) e.nextElement();

                    if((t.getID() != termin.getID()) &&
					   t.getBeginn().getDate().equals(bgn.getDate()) &&
                       t.getBeginn().isGreater(end) < 0 && t.getEnde().isGreater(bgn) > 0)
                    {
                        Hashtable psns = t.getAllPersons();

                        Enumeration enumeration = psns.keys();
                        while(enumeration.hasMoreElements())
                        {
                            Long id = (Long) enumeration.nextElement();

                            if(persons.containsKey(id))
                            {
                                // Konflikt
                                konflikte.addElement(new Konflikt((Person) psns.get(id),
                                                                  t.getBeginn(),
                                                                  t.getEnde(),
                                                                  t.getVerschiebbar()));
                            }
                        }
                    }
                }
            }

            bgn.add(1);
        }

        return konflikte;
    }

    // Konfliktfreie Intervalle ausgeben
    public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end, boolean wk) throws RemoteException
    {
        Vector freeOfKonflikte = new Vector();

        // untere Grenze
        Datum uGrenze = new Datum();
        uGrenze.setDatum(bgn);

        // obere Grenze
        Datum oGrenze = new Datum();
        oGrenze.setDatum(bgn.getDate(), end.getTime());

        while(end.isGreater(bgn) > 0)
        {
            if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(bgn) == null &&
                       bgn.getWeekDay() < 6))
            {
                Vector termineAm = (Vector) termine.get(bgn.getDate());

                if(termineAm != null)
                {
                    Enumeration e = termineAm.elements();
                    while(e.hasMoreElements())
                    {
                        Termin t = (Termin) e.nextElement();

                        if(t.getBeginn().isGreater(oGrenze) < 0 && t.getEnde().isGreater(uGrenze) > 0)
                        {
                            Hashtable psns = t.getAllPersons();

                            Enumeration enumeration = persons.keys();
                            while(enumeration.hasMoreElements())
                            {
                                if(psns.containsKey((Long) enumeration.nextElement()))
                                {
                                    if(t.getBeginn().isGreater(uGrenze) > 0)
                                    {
                                       freeOfKonflikte.addElement(new String[]{uGrenze.toString(),
                                                                                t.getBeginn().toString()});
                                    }

                                    uGrenze.setDatum(t.getEnde());
                                    break;
                                }
                            }
                        }
                    }

                    if(oGrenze.isGreater(uGrenze) > 0)
                    {
                        freeOfKonflikte.addElement(new String[]{uGrenze.toString(), oGrenze.toString()});
                    }
                }
            }

            if(uGrenze.getDate().equals(bgn.getDate()))
            {
                bgn.add(1);
                uGrenze.setDatum(bgn.getDate(), bgn.getTime());
                oGrenze.setDatum(bgn.getDate());

            } else
            {
                bgn.setDatum(uGrenze.getDate());
                if(bgn.isGreater(uGrenze) > 0)
                {   uGrenze.setDatum(bgn.getDate(), bgn.getTime());
                }

                oGrenze.setDatum(bgn.getDate());
            }
        }

        return freeOfKonflikte;
    }

    // Daten säubern
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException
    {
        Datum now = new Datum(new java.util.Date());

        Enumeration e = termine.keys();
        while(e.hasMoreElements())
        {
            String bgn = (String) e.nextElement();
            Vector termineAm = (Vector) termine.get(bgn);

            if(bis.isGreater(((Termin) termineAm.firstElement()).getBeginn()) >= 0)
            {
                int count = 0;
                while(count < termineAm.size())
                {
                    Termin t = (Termin) termineAm.elementAt(count);
                    if(t.getOwner().getID() == person.getID() && bis.isGreater(t.getBeginn()) >= 0)
                    {
                        //termineAm.removeElement(t);

                        Long id = new Long(t.getID());
                        if(terminIDs.containsKey(id))
                        {
                            //terminIDs.remove(id);
                            removeTermin(person.getKuerzel(),t,true);

                            if(t.getBeginn().isGreater(now) >= 0)
	                        {
	                            // NfktQueue
	                            server.getNfktQueue().removeNotifier(t);

	                            // Messages
	                            sendDeleteMessages(t, false);
	                        }

	                        // Update-Messages
		                    TerminEvent evt = new TerminEvent(t.getOwner(), null, t);
                            server.getMessageServer().addEvent(evt);
                        }

                    } else count++;
                }

                if(termineAm.size() == 0) termine.remove(bgn);

            } else break;
        }


    }

	// Termin anlegen
	public void create(String kuerzel, Termin termin) throws RemoteException
	{
	    Datum now = new Datum(new java.util.Date());
	    // Messages
        if(termin.getBeginn().isGreater(now) >= 0)
	    {   sendNewMessages(termin);
	    }
	    // insert
	    insertTermin(kuerzel,termin);


		// Notifikationen
		if(termin.getNotifikationen() != null)
		{   server.getNfktQueue().addNotifier(termin);
        }


		// Update-Messages
		TerminEvent evt = new TerminEvent(termin.getOwner(), null, termin);
        server.getMessageServer().addEvent(evt);

		// Serie
		if(termin.getSerie() != null)
		{
		    Vector serieExpanded = getSerienTermine(termin);

		    Enumeration e = serieExpanded.elements();
		    while(e.hasMoreElements())
		    {
		        Termin tSerie = (Termin) e.nextElement();
		        insertTermin(kuerzel,tSerie);

		        // Notifikationen
		        if(tSerie.getNotifikationen() != null)
		        {   server.getNfktQueue().addNotifier(tSerie);
                }

		        // Update-Messages
		        evt = new TerminEvent(tSerie.getOwner(), null, tSerie);
                server.getMessageServer().addEvent(evt);
		    }
		}

	}

	// Termin löschen
	public void delete(String kuerzel, Termin termin, boolean serie) throws RemoteException
	{
	    Datum now = new Datum(new java.util.Date());

	    if(!serie)
	    {
			// remove  XXXX
	        removeTermin(kuerzel,termin,false);

			if(termin.getBeginn().isGreater(now) >= 0)
	        {
	            // NfktQueue
	            server.getNfktQueue().removeNotifier(termin);

				// Messages
	            sendDeleteMessages(termin, false);
	        }

	        // Update-Messages
		    TerminEvent evt = new TerminEvent(termin.getOwner(), null, termin);
            server.getMessageServer().addEvent(evt);

	    } else
	    {
	        long serienID = termin.getSerie().getID();

	        Serie s = termin.getSerie();
	        s.setKz(kuerzel);
	        s.delete();

            Notifikation nfkt[] = termin.getNotifikationen();
            int i = 0;
            if (nfkt != null)
            {
                while(i < nfkt.length)
                {
                    nfkt[i].setKz(kuerzel);
    	            nfkt[i++].delete();
    	        }
	        }


	        Enumeration e = terminIDs.elements();
	        while(e.hasMoreElements())
	        {
	            Termin t = (Termin) e.nextElement();
	            if(t.getSerie() != null && t.getSerie().getID() == serienID)
	            {
	                // remove
	                removeTermin(kuerzel,t,true);

	                if(t.getBeginn().isGreater(now) >= 0)
	                {
	                    // NfktQueue
	                    server.getNfktQueue().removeNotifier(t);

	                }

	                // Update-Messages
		            TerminEvent evt = new TerminEvent(t.getOwner(), null, t);
                    server.getMessageServer().addEvent(evt);
	            }
	        }

	        // Messages
	        if(termin.getSerie().getEnde().isGreater(now) >= 0)
	        {   sendDeleteMessages(termin, true);
	        }
	    }

	}

    // Termin ändern
	public void update(String kuerzel, Termin terminNeu) throws RemoteException
	{
	    Datum now = new Datum(new java.util.Date());

	    Long id = new Long(terminNeu.getID());
	    Termin terminAlt = (Termin) terminIDs.get(id);

        Serie serieAlt = terminAlt.getSerie();
        Serie serieNeu = terminNeu.getSerie();

        // Messages
	    if(terminNeu.getBeginn().isGreater(now) >= 0 || (serieNeu != null &&
	                                                     serieNeu.getEnde().isGreater(now) >= 0))
	    {   sendUpdateMessages(terminAlt, terminNeu);
	    }
        // update
        updateTermin(kuerzel,terminAlt, terminNeu);

        // gelöschte Teilnehmer
        Vector deletedPersons = new Vector();
        Hashtable psnAlt = terminAlt.getAllPersons();
        Hashtable psnNeu = terminNeu.getAllPersons();

        Enumeration e = psnAlt.keys();
        while(e.hasMoreElements())
        {
            id = (Long) e.nextElement();
            if(!psnNeu.containsKey(id))
            {
                deletedPersons.addElement((Person) psnAlt.get(id));
            }
        }


        // Serienupdate
        if(serieNeu != null)
        {
            if(serieAlt != null && serieNeu != null)
            {
                boolean update = !terminAlt.getBeginn().getDate().equals(terminNeu.getBeginn().getDate()) ||
                                 !terminAlt.getEnde().getDate().equals(terminNeu.getEnde().getDate());

                e = terminIDs.elements();
                while(e.hasMoreElements())
                {
                    Termin t = (Termin) e.nextElement();

                    if(t.getSerie() != null && t.getID() != terminAlt.getID() &&
                       t.getSerie().getID() == serieAlt.getID() && t.getBeginn().isGreater(terminAlt.getBeginn()) > 0)
                    {
                        // remove
                        removeTermin(kuerzel,t,false);

                        // NfktQueue
                        if(t.getBeginn().isGreater(now) >= 0)
                        {   server.getNfktQueue().removeNotifier(t);
                        }

                        if(update)
                        {
                            // Update-Messages
    	                    TerminEvent evt = new TerminEvent(t.getOwner(), null, t);
                            server.getMessageServer().addEvent(evt);
                        }

                        // Update-Message fuer gelöschte Teilnehmer
                        if(deletedPersons.size() > 0)
                        {
                            Enumeration enumeration = deletedPersons.elements();
                            while(enumeration.hasMoreElements())
                            {
                                TerminEvent evt = new TerminEvent(t.getOwner(), (Person) enumeration.nextElement(), t);
                                server.getMessageServer().addEvent(evt);
                            }
                        }
                    }
                }
            } else
            {
                // neue Serien
                //lastSerienID++;
		        //serieNeu.setID(lastSerienID);
            }

            // neue Serie eintragen
            Vector serieExpanded = getSerienTermine(terminNeu);

		    e = serieExpanded.elements();
		    while(e.hasMoreElements())
		    {
		        Termin tSerie = (Termin) e.nextElement();

		        // insert
	            insertTermin(kuerzel,tSerie);

	            // Notifikationen
	            if(tSerie.getNotifikationen() != null)
	            {   server.getNfktQueue().addNotifier(tSerie);
                }

	            // Update-Messages
	            TerminEvent evt = new TerminEvent(tSerie.getOwner(), null, tSerie);
                server.getMessageServer().addEvent(evt);
            }
        }

        // NfktQueue
        NfktQueue nfktQueue = server.getNfktQueue();

        if(terminAlt.getBeginn().isGreater(now) >= 0)
        {   nfktQueue.removeNotifier(terminAlt);
        }

        if(terminNeu.getBeginn().isGreater(now) >= 0)
	    {   nfktQueue.addNotifier(terminNeu);
        }


        // Update-Messages
		TerminEvent evt = new TerminEvent(terminNeu.getOwner(), null, terminNeu);
        server.getMessageServer().addEvent(evt);


        if(!terminAlt.getBeginn().getDate().equals(terminNeu.getBeginn().getDate()) ||
           !terminAlt.getEnde().getDate().equals(terminNeu.getEnde().getDate()))
        {
            evt = new TerminEvent(terminAlt.getOwner(), null, terminAlt);
            server.getMessageServer().addEvent(evt);
        }

        // Update-Message fuer gelöschte Teilnehmer
        if(deletedPersons.size() > 0)
        {
            e = deletedPersons.elements();
            while(e.hasMoreElements())
            {
                evt = new TerminEvent(terminAlt.getOwner(), (Person) e.nextElement(), terminAlt);
                server.getMessageServer().addEvent(evt);
            }
        }


	}

	// Fehlendes Eintragsrecht
	public void sendMissingRight(Termin termin, Vector persons) throws RemoteException
	{
	    String msg = "Versuch von " + c + termin.getOwner().getVorname() + " " + termin.getOwner().getNachname() + c +
	                 " einen Termin vom Typ " + c + termin.getTyp().getBezeichnung() + c +
	                 "\neinzutragen wegen fehlendem Eintragsrecht gescheitert.\n";

	    Enumeration e = persons.elements();
	    while(e.hasMoreElements())
	    {

	        //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(termin.getOwner(), (Person) e.nextElement(),
                                                msg, "Fehlendes Eintragsrecht");
            // send
            server.getMessageServer().addEvent(evt);
        }
	}
}