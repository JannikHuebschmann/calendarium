package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.*;
import basisklassen.*;
import event.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// TerminSetRemote // TerminSetRemote // TerminSetRemote // TerminSetRemote // TerminSetRemote //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class TerminSetRemote extends UnicastRemoteObject implements interfaces.TerminSetInterface
{
	private static final long serialVersionUID = 72890124374397761L;

	// Server
    private Server server;

    // Daten
    private Hashtable terminIDs;
    private Hashtable termine;
    private long lastTerminID;       // Counter fuer TerminID
    private long lastSerienID;       // Counter fuer SerienID

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

    ///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
    private void load()
    {
        try {
			FileInputStream istream = new FileInputStream(
					"data/files/termine.dat");
			ObjectInputStream s = new ObjectInputStream(istream);

			lastTerminID = ((Long) s.readObject()).longValue();
			lastSerienID = ((Long) s.readObject()).longValue();
			terminIDs = (Hashtable) s.readObject();
			termine = (Hashtable) s.readObject();

			istream.close();

		} catch (Exception e) {
			lastTerminID = 0;
			lastSerienID = 0;
			termine = new Hashtable();
			terminIDs = new Hashtable();
		}
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern // Speichern // Speichern // Speichern // Speichern  //
    /////////////////////////////////////////////////////////////////////////////////
    public synchronized void save() {
		try {
			FileOutputStream ostream = new FileOutputStream(
					"data/files/termine.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastTerminID));
			p.writeObject(new Long(lastSerienID));
			p.writeObject(terminIDs);
			p.writeObject(termine);
			p.flush();

			ostream.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

    // /////////////////////////////////////////////////////////////////////////////////////
    // MessageEvents // MessageEvents // MessageEvents // MessageEvents //
	// MessageEvents //
    // /////////////////////////////////////////////////////////////////////////////////////

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
                                                msg, "Neuer Termin\n");
            
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
            {   message = "Der Serientermin " + c + termin.getKurzText() + c + " wurde gelöscht.";
            } else
            {   message = "Der Termin " + c + termin.getKurzText() + c + " am " +
                          termin.getBeginn().toString() + " wurde aus der zugehörigen Serie gelöscht.";
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
        String text, head;

        if(terminAlt.getSerie() == null || terminNeu.getSerie() == null)
        {   head = "Änderungen des Termins " + c + terminAlt.getKurzText() + c + " am " +
                   terminAlt.getBeginn().toString() + "\n\n";
        } else
        {   head = "Änderungen der Serientermins " + c + terminAlt.getKurzText() + c + " beginnend mit " +
                   terminAlt.getBeginn().toString() + "\n\n";
        }

        head += leerstring.substring(0, len) + "Liste der Änderungen\n" +
                leerstring.substring(0, len) + "--------------------\n";

        Enumeration e = persHashNeu.keys();
        while(e.hasMoreElements())
        {
            Long id = (Long) e.nextElement();
            Teilnehmer tn = (Teilnehmer) persHashNeu.get(id);

            if(!persHashAlt.containsKey(id))
            {
                                if(terminNeu.getSerie() == null)
                                {       // neuer Teilnehmer
                                        message = "Sie wurden dem Termin " + c + terminNeu.getKurzText() + c + " am " +
                              terminNeu.getBeginn().toString() + "\nals Teilnehmer neu hinzugefuegt.\n\n" +
                              getTerminText(persHashNeu, terminNeu) + getNfktText(terminNeu, tn);
                                } else
                                {       message = "Sie wurden dem Serientermin " + c + terminNeu.getKurzText() + c + " beginnend mit " +
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
                for(int i = 0; i < Math.min(nfNeu.length, nfAlt.length); i++)
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
                                    nfktAlt[i].toString() + " (gelöscht)\n";

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
                        {   text = "Notifikation " + (i + 1) + ": ";
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
                                        nfktAlt[i].toString() + " (gelöscht)\n";
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
                                                        message, "Termin-Änderung");
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
        String text, nl = "", msg = "";
        Hashtable kPersHashAlt = (Hashtable) persHashAlt.clone();

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
                   person.getNameLang() + " (gelöscht)\n";

            text = "";
            nl = "";

            String message;

            if(terminAlt.getSerie() == null)
            {   message = "Sie wurden vom Termin " + c + terminAlt.getKurzText() + c + " am " +
                          terminAlt.getBeginn().toString() + "\nals Teilnehmer gelöscht.\n";
            } else
            {   message = "Sie wurden vom Serientermin " + c + terminAlt.getKurzText() + c + " beginnend mit " +
                          terminAlt.getBeginn().toString() + "\nals Teilnehmer gelöscht.\n";
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
    @SuppressWarnings("unchecked")
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

    /**
		 * insert
		 * 
		 * @param termin
		 */
	@SuppressWarnings("unchecked")
	private void insertTermin(Termin termin)
	{
		// LastID setzen
		lastTerminID++;
		termin.setID(lastTerminID);

		// Hashtable
		terminIDs.put(new Long(lastTerminID), termin);

		// TerminListe
		addToTerminListe(termin);
	}

    // remove
    private void removeTermin(Termin termin)
    {
        // Hashtable
        terminIDs.remove(new Long(termin.getID()));

                // TerminListe
                removeFromTerminListe(termin);

    }

    // update
    @SuppressWarnings("unchecked")
	private void updateTermin(Termin terminAlt, Termin terminNeu)
    {
        Long id = new Long(terminAlt.getID());

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

                Enumeration e = terminListe.elements();
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
    }

    @SuppressWarnings("unchecked")
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

                if(t.getOwner().getID() == person.getID())
                {
                    termineAm.removeElement(t);
                    terminIDs.remove(new Long(t.getID()));

                } else count++;
            }

            if(termineAm.size() == 0) termine.remove(bgn);
        }

        save();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Termine an einem Tag ausgeben
    @SuppressWarnings("unchecked")
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
        @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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

                    Enumeration enumer = personenListe.elements();
                    while(enumer.hasMoreElements())
                    {
                        Person person = (Person) enumer.nextElement();
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
    @SuppressWarnings("unchecked")
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

                        Enumeration enumer = psns.keys();
                        while(enumer.hasMoreElements())
                        {
                            Long id = (Long) enumer.nextElement();

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
    @SuppressWarnings("unchecked")
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

                            Enumeration enumer = persons.keys();
                            while(enumer.hasMoreElements())
                            {
                                if(psns.containsKey((Long) enumer.nextElement()))
                                {
                                    if(t.getBeginn().isGreater(uGrenze) > 0)
                                    {   freeOfKonflikte.addElement(new String[]{uGrenze.toString(),
                                                                                t.getBeginn().toString()});
                                    }

                                    uGrenze.setDatum(t.getEnde());
                                    break;
                                }
                            }
                        }
                    }

                    if(oGrenze.isGreater(uGrenze) > 0)
                    {   freeOfKonflikte.addElement(new String[]{uGrenze.toString(), oGrenze.toString()});
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
        Datum now = new Datum(new Date());

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
                        termineAm.removeElement(t);

                        Long id = new Long(t.getID());
                        if(terminIDs.containsKey(id))
                        {
                            terminIDs.remove(id);

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

        save();
    }

   /** Termin anlegen
    * @param kuerzel Parameternutzung ist unklar BC
    */
	public void create(String kuerzel, Termin termin) throws RemoteException
	{
		Datum now = new Datum(new Date());

		// Messages
		if (termin.getBeginn().isGreater(now) >= 0)
		{
			sendNewMessages(termin);
		}

		// insert
		insertTermin(termin);

		// Notifikationen
		if (termin.getNotifikationen() != null)
		{
			server.getNfktQueue().addNotifier(termin);
		}

		// Update-Messages
		TerminEvent evt = new TerminEvent(termin.getOwner(), null, termin);
		server.getMessageServer().addEvent(evt);

		// Serie
		if (termin.getSerie() != null)
		{
			lastSerienID++;
			termin.getSerie().setID(lastSerienID);

			Vector serieExpanded = getSerienTermine(termin);

			Enumeration e = serieExpanded.elements();
			while (e.hasMoreElements())
			{
				Termin tSerie = (Termin) e.nextElement();
				insertTermin(tSerie);

				// Notifikationen
				if (tSerie.getNotifikationen() != null)
				{
					server.getNfktQueue().addNotifier(tSerie);
				}

				// Update-Messages
				evt = new TerminEvent(tSerie.getOwner(), null, tSerie);
				server.getMessageServer().addEvent(evt);
			}
		}

		save();
	}

        // Termin löschen
        public void delete(String kuerzel, Termin termin, boolean serie) throws RemoteException
        {
            Datum now = new Datum(new Date());

            if(!serie)
            {
                        // remove
                removeTermin(termin);

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

                Enumeration e = terminIDs.elements();
                while(e.hasMoreElements())
                {
                    Termin t = (Termin) e.nextElement();
                    if(t.getSerie() != null && t.getSerie().getID() == serienID)
                    {
                        // remove
                        removeTermin(t);

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

            save();
        }

    // Termin ändern
        @SuppressWarnings("unchecked")
		public void update(String kuerzel, Termin terminNeu) throws RemoteException
        {
            Datum now = new Datum(new Date());

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
        updateTermin(terminAlt, terminNeu);

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
                        removeTermin(t);

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
                            Enumeration enumer = deletedPersons.elements();
                            while(enumer.hasMoreElements())
                            {
                                TerminEvent evt = new TerminEvent(t.getOwner(), (Person) enumer.nextElement(), t);
                                server.getMessageServer().addEvent(evt);
                            }
                        }
                    }
                }
            } else
            {
                // neue Serien
                lastSerienID++;
                        serieNeu.setID(lastSerienID);
            }

            // neue Serie eintragen
            Vector serieExpanded = getSerienTermine(terminNeu);

                    e = serieExpanded.elements();
                    while(e.hasMoreElements())
                    {
                        Termin tSerie = (Termin) e.nextElement();

                        // insert
                    insertTermin(tSerie);

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

        save();
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
