package server.remote; //
/////////////////////////

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import server.*;
import basisklassen.*;
import event.*;

///////////////////////////////////////////////////////////////////////////////////////
// ToDoSetRemote // ToDoSetRemote // ToDoSetRemote // ToDoSetRemote // ToDoSetRemote //
///////////////////////////////////////////////////////////////////////////////////////

public class ToDoSetRemote extends UnicastRemoteObject implements interfaces.ToDoSetInterface
{
	private static final long serialVersionUID = -4161311733795410436L;

	// Server
    private Server server;

	// Daten
	private Hashtable toDoIDs;  // IDs
	private Vector toDos;       // geordnet nach erinnernAb
    private long lastToDoID;     // Counter fuer ToDoID
    private long lastSerienID;   // Counter fuer SerienID

    // Konstanten
    private int len = 18;
    private String leerstring = "                                                              ";
    private char c = 34;

    public ToDoSetRemote(Server s, String name) throws RemoteException
    {
        server = s;

        // registrieren
        try
        {   Naming.bind(name + "ToDoSetRemote", this);

        } catch(Exception e)
        {   e.printStackTrace();
        }

        load();

        // NfktQueue
        server.getNfktQueue().createNotifiers(toDoIDs);
    }

	///////////////////////////////////////////////////////////////////////////////////
    // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden // Laden //
    ///////////////////////////////////////////////////////////////////////////////////
	private void load()
    {
        try
		{   FileInputStream istream = new FileInputStream("data/files/todo.dat");
            ObjectInputStream s = new ObjectInputStream(istream);

			lastToDoID = ((Long) s.readObject()).longValue();
			lastSerienID = ((Long) s.readObject()).longValue();
			toDoIDs = (Hashtable) s.readObject();
            toDos = (Vector) s.readObject();

            istream.close();

		} catch(Exception e)
		{
			lastToDoID = 0;
			lastSerienID = 0;
			toDos = new Vector();
			toDoIDs = new Hashtable();
		}
    }

	/////////////////////////////////////////////////////////////////////////////////
    // Speichern // Speichern //  Speichern // Speichern // Speichern // Speichern //
    /////////////////////////////////////////////////////////////////////////////////
	public synchronized void save()
    {
        try
		{	FileOutputStream ostream = new FileOutputStream("data/files/todo.dat");
			ObjectOutputStream p = new ObjectOutputStream(ostream);

			p.writeObject(new Long(lastToDoID));
			p.writeObject(new Long(lastSerienID));
			p.writeObject(toDoIDs);
			p.writeObject(toDos);
			p.flush();

			ostream.close();

		} catch(IOException e)
		{
		    e.printStackTrace();
		    System.exit(0);
		}
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // MessageEvents // MessageEvents // MessageEvents // MessageEvents // MessageEvents //
    ///////////////////////////////////////////////////////////////////////////////////////

    // Message: Neuer ToDo-Eintrag
    private void sendNewMessages(ToDo toDo)
    {
        Hashtable persHash = toDo.getAllPersonsWithNfkt();
        String message = getToDoText(persHash, toDo);

        Enumeration e = persHash.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();
            String msg = message + getNfktText(toDo, tn);

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(toDo.getOwner(), (Person) tn.getTeilnehmer(),
                                                msg, "Neuer ToDo-Eintrag\n");
            // send
            server.getMessageServer().addEvent(evt);
        }
    }

    // Message: ToDo-Eintrag gelöscht
    private void sendDeleteMessages(ToDo toDo, boolean allOfSerie)
    {
        Hashtable persHash = toDo.getAllPersonsWithNfkt();
        String message;

        if(toDo.getSerie() == null)
        {   message = "Der ToDo-Eintrag " + c + toDo.getKurzText() + c + " mit Fälligkeit " +
                      toDo.getFälligPer().getDate() + " wurde gelöscht.";
        } else
        {   if(allOfSerie)
            {   message = "Der SerienToDo-Eintrag " + c + toDo.getKurzText() + c + " wurde gelöscht.";
            } else
            {   message = "Der ToDo-Eintrag " + c + toDo.getKurzText() + c + " mit Fälligkeit " +
                          toDo.getFälligPer().getDate() + " wurde aus der zugehörigen Serie gelöscht.";
            }
        }

        Enumeration e = persHash.elements();
        while(e.hasMoreElements())
        {
            Teilnehmer tn = (Teilnehmer) e.nextElement();

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(toDo.getOwner(), (Person) tn.getTeilnehmer(),
                                                message, "ToDo-Eintrag gelöscht");
            // send
            server.getMessageServer().addEvent(evt);
        }
    }

    // Messages: ToDo-Eintrag geändert
    private void sendUpdateMessages(ToDo toDoAlt, ToDo toDoNeu)
    {
        // Teilnehmer
        Hashtable persHashAlt = toDoAlt.getAllPersonsWithNfkt();
        Hashtable persHashNeu = toDoNeu.getAllPersonsWithNfkt();

        // Notifikationen
        Notifikation[] nfktAlt = toDoAlt.getNotifikationen();
        Notifikation[] nfktNeu = toDoNeu.getNotifikationen();

        String changeText = getChangeText(persHashAlt, toDoAlt, persHashNeu, toDoNeu);
        String nfktText = "", message = "";
        String text, head;

        if(toDoAlt.getSerie() == null || toDoNeu.getSerie() == null)
        {   head = "Änderungen des ToDo-Eintrags " + c + toDoAlt.getKurzText() + c + " mit Fälligkeit " +
                   toDoAlt.getFälligPer().getDate() + "\n\n";
        } else
        {   head = "Änderungen des SerienToDo-Eintrags " + c + toDoAlt.getKurzText() + c + " beginnend mit " +
                   toDoAlt.getErinnernAb().getDate() + "\n\n";
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
                // neuer Teilnehmer
				if(toDoNeu.getSerie() == null)
                {	message = "Sie wurden dem ToDo-Eintrag " + c + toDoNeu.getKurzText() + c + " mit Fälligkeit " +
                              toDoNeu.getFälligPer().getDate() + "\nals Teilnehmer neu hinzugefuegt.\n\n" +
                              getToDoText(persHashNeu, toDoNeu) + getNfktText(toDoNeu, tn);
				} else
				{	message = "Sie wurden vom SerienToDoEintrag " + c + toDoNeu.getKurzText() + c + " beginnend mit " +
				              toDoNeu.getErinnernAb().getDate() + "\nalsTeilnehmer neu hinzugefuegt.\n\n" +
							  getToDoText(persHashNeu, toDoNeu) + getNfktText(toDoNeu, tn);

				}

                //////////////////////////////////////////////////////////////////////////
                // Message senden // Message senden // Message senden // Message senden //
                //////////////////////////////////////////////////////////////////////////

                MessageEvent evt = new MessageEvent(toDoNeu.getOwner(),
                                                   (Person) tn.getTeilnehmer(),
                                                    message, "Neuer ToDo-Eintrag");
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
                    MessageEvent evt = new MessageEvent(toDoNeu.getOwner(),
                                                       (Person) tn.getTeilnehmer(),
                                                        message, "ToDo-Eintrag geändert");
                    // send
                    server.getMessageServer().addEvent(evt);
                }
            }
        }
    }

    // ToDoText
    private String getToDoText(Hashtable persHash, ToDo toDo)
    {
        String text, msg = "";

        msg += leerstring.substring(0, len) + "ToDo-Daten:\n" +
               leerstring.substring(0, len) + "-----------\n";

        text = "Erinnern ab: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getErinnernAb().getDate() + '\n';

        text = "Fällig per: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getFälligPer().getDate() + '\n';

        text = "Typ: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getTyp().getBezeichnung() + '\n';

        text = "Kurztext: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getKurzText() + '\n';

        text = "Langtext: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getLangText() + '\n';

        text = "Hyperlink: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getHyperlink() + '\n';

        text = "Ort: ";
        msg += leerstring.substring(0, len - text.length()) + text +
               toDo.getOrt() + '\n';

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
        if(toDo.getSerie() != null)
        {
            msg += "\n";
            text = "Serie: ";

            msg += leerstring.substring(0, len - text.length()) + text +
                   toDo.getSerie().toString() + '\n';

            if(toDo.getSerie().getWerktags())
                msg += leerstring.substring(0, len) + "nur Werktags\n";
            else
                msg += leerstring.substring(0, len) + "auch Sonn- und Feiertags\n";

            msg += leerstring.substring(0, len) + "vom " +
                   toDo.getSerie().getBeginn().getDate() + " bis " +
                   toDo.getSerie().getEnde().getDate() + '\n';
        }

        return msg + '\n';
    }

    // NfktText
    private String getNfktText(ToDo toDo, Teilnehmer tn)
    {
        String text, msg = "";
        boolean[] nf = tn.getNotifikationen();

        if(toDo.getNotifikationen() != null)
        {
            String h = leerstring.substring(0, len - 16) + "Sie werden erinnert durch:" + '\n' +
                       leerstring.substring(0, len - 16) + "--------------------------\n";

            for(int i = 0; i < toDo.getNotifikationen().length; i++)
            {
                if(nf[i])
                {
                    msg += h;
                    h = "";

                    text = "Notifikation " + (i + 1) + ": ";
                    msg += leerstring.substring(0, len - text.length()) + text +
                           toDo.getNotifikationen()[i].toString() + '\n';
                }
            }
        }
        return msg;
    }

    // ChangeText
    String getChangeText(Hashtable persHashAlt, ToDo toDoAlt,
                         Hashtable persHashNeu, ToDo toDoNeu)
    {
        String text, nl = "", msg = "";
        Hashtable kPersHashAlt = (Hashtable) persHashAlt.clone();

        //////////////////////////////////////////////////////////////////////////////////////
        // Vergleiche // Vergleiche // Vergleiche // Vergleiche // Vergleiche // Vergleiche //
        //////////////////////////////////////////////////////////////////////////////////////

        if(!toDoNeu.getErinnernAb().equals(toDoAlt.getErinnernAb()))
        {
            // Erinnern ab
            text = "Erinnern ab: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getErinnernAb().getDate() + '\n';
        }

        if(!toDoNeu.getFälligPer().equals(toDoAlt.getFälligPer()))
        {
            // Fällig per
            text = "Fällig per: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getFälligPer().getDate() + '\n';
        }

        if(toDoNeu.getTyp().getID() != toDoAlt.getTyp().getID())
        {
            // Typänderung
            text = "Typ: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getTyp().getBezeichnung() + '\n';
        }

        if(!toDoNeu.getKurzText().equals(toDoAlt.getKurzText()))
        {
            // Kurztext
            text = "Kurztext: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getKurzText() + '\n';
        }
        if(!toDoNeu.getLangText().equals(toDoAlt.getLangText()))
        {
            // Langtext
            text = "Langtext: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getLangText() + '\n';
        }
        if(!toDoNeu.getHyperlink().equals(toDoAlt.getHyperlink()))
        {
            // Hyperlink
            text = "Hyperlink: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getHyperlink() + '\n';
        }

        if(!toDoNeu.getOrt().equals(toDoAlt.getOrt()))
        {
            // Ortsänderung
            text = "Ort: ";
            msg += leerstring.substring(0, len - text.length()) + text +
                   toDoNeu.getOrt() + '\n';
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

            if(toDoAlt.getSerie() == null)
            {   message = "Sie wurden vom ToDo-Eintrag " + c + toDoAlt.getKurzText() + c + " mit Fälligkeit " +
                          toDoAlt.getFälligPer().getDate() + "\nals Teilnehmer gelöscht.\n";
            } else
            {   message = "Sie wurden vom SerienToDo-Eintrag " + c + toDoAlt.getKurzText() + c + " beginnend mit " +
                          toDoAlt.getErinnernAb().getDate() + "\nals Teilnehmer gelöscht.\n";
            }

            //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(toDoAlt.getOwner(), person,
                                                message, "Teilnahme abgesagt");
            // send
            server.getMessageServer().addEvent(evt);
        }

        text = "Serie: ";
        if(msg.length() > 0) nl = "\n";

        Serie serieNeu = toDoNeu.getSerie();
        Serie serieAlt = toDoAlt.getSerie();

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
	private Vector getSerienToDos(ToDo toDo)
    {
        Vector expanded = new Vector();
        Serie serie = toDo.getSerie();

        // ErinnenAb
        Datum erinnernAb = new Datum();
        erinnernAb.setDatum(toDo.getErinnernAb());

        // Ende
        Datum end = serie.getEnde();

        // Frequenz
        int freq = serie.getFrequenz();

        // Werktags
        boolean wk = serie.getWerktags();

        switch(serie.getTyp())
        {
            case 0:     // täglich

                erinnernAb.add(freq);
                while(end.isGreater(erinnernAb) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null &&
                                   erinnernAb.getWeekDay() < 6))
                        {
                            expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                        }
                    } catch(Exception e) {}

                    erinnernAb.add(freq);
                }

                break;

            case 1:     // monatlich absolut

                erinnernAb.addMonth(freq);
                while(end.isGreater(erinnernAb) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null &&
                                   erinnernAb.getWeekDay() < 6))
                        {
                            expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                        }
                    } catch(Exception e) {}

                    erinnernAb.addMonth(freq);
                }

                break;


            case 2:     // monatlich relativ

                erinnernAb.addMonth(serie.getBeginn(), freq);
                while(end.isGreater(erinnernAb) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null &&
                                   erinnernAb.getWeekDay() < 6))
                        {
                            expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                        }
                    } catch(Exception e) {}

                    erinnernAb.addMonth(serie.getBeginn(), freq);
                }

                break;

            case 3:     // jährlich

                erinnernAb.addYear(freq);
                while(end.isGreater(erinnernAb) >= 0)
                {
                    try
                    {   if(!wk || (server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null &&
                                   erinnernAb.getWeekDay() < 6))
                        {
                            expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                        }
                    } catch(Exception e) {}

                    erinnernAb.addYear(freq);
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
                for(int i = erinnernAb.getWeekDay() + 1; i < 7; i++)
                {
                    erinnernAb.add(1);

                    if(end.isGreater(erinnernAb) >= 0 && sel[i])
                    {
                        try
                        {   if(!wk || (i < 6 && server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null))
                            {   expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                            }
                        } catch(Exception e) {}
                    }
                }

                do
                {   erinnernAb.add((freq - 1) * 7 );

                    for(int i = 0; i < 7; i++)
                    {
                        erinnernAb.add(1);
                        if(end.isGreater(erinnernAb) >= 0 && sel[i])
                        {
                            try
                            {   if(!wk || (i < 6 && server.getFeiertagSetRemote().getFeiertagByDate(erinnernAb) == null))
                                {   expanded.addElement(getToDoOfSerie(toDo, erinnernAb));
                                }
                            } catch(Exception e) {}
                        }
                    }

                } while(end.isGreater(erinnernAb) >= 0);

        }

        return expanded;
    }

    private ToDo getToDoOfSerie(ToDo toDo, Datum erinnernAb)
    {
        ToDo toDoNeu = new ToDo(toDo.getOwner());

        // ErinnernAb
        toDoNeu.getErinnernAb().setDatum(erinnernAb);

        // FälligPer
        int tage = toDo.getErinnernAb().getDaysBetween(toDo.getFälligPer());

        toDoNeu.getFälligPer().setDatum(erinnernAb);
        toDoNeu.getFälligPer().add(tage);

        // uebrigen Attibute uebernehmen
        toDoNeu.setKurzText(toDo.getKurzText());
        toDoNeu.setLangText(toDo.getLangText());
        toDoNeu.setTyp(toDo.getTyp());
        toDoNeu.setOrt(toDo.getOrt());
        toDoNeu.setHyperlink(toDo.getHyperlink());

        // Referenzen
        toDoNeu.setNotifikationen(toDo.getNotifikationen());
        toDoNeu.setTeilnehmer(toDo.getTeilnehmer());
        toDoNeu.setSerie(toDo.getSerie());

		return toDoNeu;
    }

    // insert
    @SuppressWarnings("unchecked")
	private void insertToDo(ToDo toDo)
    {
        // LastID setzen
		lastToDoID++;
		toDo.setID(lastToDoID);

		// Hashtable
        toDoIDs.put(new Long(lastToDoID), toDo);

		// ToDoListe
		addToListe(toDo);
    }

    // remove
    private void removeToDo(ToDo toDo)
    {
        // Hashtable
        toDoIDs.remove(new Long(toDo.getID()));

		// ToDoListe
		removeFromListe(toDo);
    }

    // update
    @SuppressWarnings("unchecked")
	private void updateToDo(ToDo toDoAlt, ToDo toDoNeu)
    {
        Long id = new Long(toDoAlt.getID());

        // Hashtable
        toDoIDs.put(id, toDoNeu);

        if(!toDoAlt.getErinnernAb().equals(toDoNeu.getErinnernAb()))
		{
		    removeFromListe(toDoAlt);
            addToListe(toDoNeu);

        } else
        {
            Enumeration e = toDos.elements();
			while(e.hasMoreElements())
			{
				Vector toDoAb = (Vector) e.nextElement();
				Datum erinnernAb = ((ToDo) toDoAb.firstElement()).getErinnernAb();

				if(erinnernAb.equals(toDoNeu.getErinnernAb()))
				{
					Enumeration enumer = toDoAb.elements();
					int count = 0;

					while(enumer.hasMoreElements())
					{
						ToDo t = (ToDo) enumer.nextElement();
						if(t.getID() == toDoNeu.getID())
						{
							toDoAb.setElementAt(toDoNeu, count);
							return;

						} else count++;
					}
				}
			}
        }
    }

    @SuppressWarnings("unchecked")
	private void addToListe(ToDo toDo)
    {
		Enumeration e = toDos.elements();
        int count1 = 0;

		while(e.hasMoreElements())
        {
            Vector toDoAb = (Vector) e.nextElement();
            Datum erinnernAb = ((ToDo) toDoAb.firstElement()).getErinnernAb();

			switch(erinnernAb.isGreater(toDo.getErinnernAb()))
			{
			case 0: // add

				Enumeration enumer = toDoAb.elements();
				int count2 = 0;

				while(enumer.hasMoreElements())
				{
					ToDo t = (ToDo) enumer.nextElement();
					if(t.getFälligPer().isGreater(toDo.getFälligPer()) >= 0)
					{
						toDoAb.insertElementAt(toDo, count2);
						return;

					} else count2++;
				}

				if(count2 >= toDoAb.size())
				{   toDoAb.addElement(toDo);
				}

				return;

			case 1: // new

                Vector fälligPer = new Vector();
				fälligPer.addElement(toDo);

				toDos.insertElementAt(fälligPer, count1);

                return;

			default:
				count1++;
			}
		}

		if(count1 >= toDos.size())
        {
			Vector fälligPer = new Vector();
			fälligPer.addElement(toDo);

			toDos.addElement(fälligPer);
        }
    }

    private void removeFromListe(ToDo toDo)
    {
        Enumeration e = toDos.elements();
		while(e.hasMoreElements())
		{
		    Vector toDoAb = (Vector) e.nextElement();
            Datum erinnernAb = ((ToDo) toDoAb.firstElement()).getErinnernAb();

            if(erinnernAb.equals(toDo.getErinnernAb()))
            {
				Enumeration enumer = toDoAb.elements();
			    while(enumer.hasMoreElements())
				{
					ToDo t = (ToDo) enumer.nextElement();
					if(t.getID() == toDo.getID())
					{
						toDoAb.removeElement(t);

						if(toDoAb.size() == 0)
						{	toDos.removeElement(toDoAb);
						}

						return;
					}
				}
            }
        }
    }

    // Person löschen
    public void deletePerson(Person person)
    {
        int count1 = 0;
        while(count1 < toDos.size())
        {
            Vector toDoAb = (Vector) toDos.elementAt(count1);

            int count2 = 0;
            while(count2 < toDoAb.size())
            {
                ToDo t = (ToDo) toDoAb.elementAt(count2);
                if(t.getOwner().getID() == person.getID())
                {
                    toDoAb.removeElement(t);
                    toDoIDs.remove(new Long(t.getID()));

                } else count2++;
            }

            if(toDoAb.size() == 0)
            {   toDos.removeElement(toDoAb);

            } else count1++;
        }

        save();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods // RemoteMethods //
    ///////////////////////////////////////////////////////////////////////////////////////

    // ToDo-Einträge eines Tages ausgeben
    @SuppressWarnings("unchecked")
	public Vector getToDoVom(Datum vomDat, Vector personenListe) throws RemoteException
    {
        Vector filtered = new Vector();

        Enumeration e = toDos.elements();
        while(e.hasMoreElements())
        {
            Vector toDoAb = (Vector) e.nextElement();
            Datum d = ((ToDo) toDoAb.firstElement()).getErinnernAb();

            if(vomDat.isGreater(d) >= 0)
            {
                Enumeration enumer = toDoAb.elements();
                while(enumer.hasMoreElements())
                {
                    ToDo toDo = (ToDo) enumer.nextElement();
                    if(vomDat.isGreater(toDo.getFälligPer()) <= 0)
                    {
                        if(toDo.isRelevantFor(personenListe))
                        {   filtered.addElement(toDo);
                        }
                    }
                }
            }
        }

        return filtered;
    }

	// ToDo-Einträge in einem Zeitraum ausgeben
	@SuppressWarnings("unchecked")
	public Vector[] getToDoVonBis(Datum vonDat, Datum bisDat, Vector personenListe) throws RemoteException
	{
	    int count = vonDat.getDaysBetween(bisDat);

		Vector filtered[] = new Vector[count + 1];
		for(int i = 0; i <= count; i++) filtered[i] = new Vector();

		Datum bisDatum = new Datum();

		// Erinnern ab
        Enumeration e = toDos.elements();
		while(e.hasMoreElements())
		{
			Vector toDoAb = (Vector) e.nextElement();
			Datum erinnAb = ((ToDo) toDoAb.firstElement()).getErinnernAb();

			if(bisDat.isGreater(erinnAb) >= 0)
			{
				// Fällig per
				Enumeration enumer = toDoAb.elements();
				while(enumer.hasMoreElements())
				{
					bisDatum.setDatum(bisDat);
					ToDo toDo = (ToDo) enumer.nextElement();

					if(vonDat.isGreater(toDo.getFälligPer()) <= 0 && toDo.isRelevantFor(personenListe))
					{
					    boolean added = false;

						for(int i = count; i >= 0; i--)
						{
						    if(bisDatum.isGreater(toDo.getErinnernAb()) >= 0 &&
							   bisDatum.isGreater(toDo.getFälligPer()) <= 0)
							{
								filtered[i].addElement(toDo);
								added = true;

							} else if(added) break;

							bisDatum.substract(1);
						}
                    }
				}
            }
        }

        return filtered;
	}

	// ein bestimmtes ToDo ausgeben
	public ToDo getToDoByID(long id) throws RemoteException
	{   return (ToDo) toDoIDs.get(new Long(id));
	}

    // Daten säubern
    public void deleteUntilDate(Person person, Datum bis) throws RemoteException
    {
        Datum now = new Datum(new Date());

        int count1 = 0;
        while(count1 < toDos.size())
        {
            Vector toDoAb = (Vector) toDos.elementAt(count1);
            if(bis.isGreater(((ToDo) toDoAb.firstElement()).getErinnernAb()) >= 0)
            {
                int count2 = 0;
                while(count2 < toDoAb.size())
                {
                    ToDo t = (ToDo) toDoAb.elementAt(count2);
                    if(t.getOwner().getID() == person.getID() && bis.isGreater(t.getFälligPer()) >= 0)
                    {
                        toDoAb.removeElement(t);
                        toDoIDs.remove(new Long(t.getID()));

                        if(t.getFälligPer().isGreater(now) >= 0)
                        {
                            // NfktQueue
                            server.getNfktQueue().removeNotifier(t);

                            // Messages
                            sendDeleteMessages(t, false);
                        }

                        // Update-Messages
		                ToDoEvent evt = new ToDoEvent(t.getOwner(), null, t);
                        server.getMessageServer().addEvent(evt);

                    } else count2++;
                }

                if(toDoAb.size() == 0)
                {   toDos.removeElement(toDoAb);

                } else count1++;

            } else break;
        }

        save();
    }

	// ToDo anlegen
	public void create(String kuerzel, ToDo toDo) throws RemoteException
	{
	    Datum now = new Datum(new Date());

	    // Messages
	    if(toDo.getFälligPer().isGreater(now) >= 0)
	    {   sendNewMessages(toDo);
	    }

	    // insert
	    insertToDo(toDo);

		// Notifikationen
		if(toDo.getNotifikationen() != null)
		{   server.getNfktQueue().addNotifier(toDo);
        }

		// Update-Messages
		ToDoEvent evt = new ToDoEvent(toDo.getOwner(), null, toDo);
        server.getMessageServer().addEvent(evt);

		// Serie
		if(toDo.getSerie() != null)
		{
		    lastSerienID++;
		    toDo.getSerie().setID(lastSerienID);

		    Vector serieExpanded = getSerienToDos(toDo);

		    Enumeration e = serieExpanded.elements();
		    while(e.hasMoreElements())
		    {
		        ToDo tSerie = (ToDo) e.nextElement();
		        insertToDo(tSerie);

		        // Notifikationen
		        if(tSerie.getNotifikationen() != null)
		        {   server.getNfktQueue().addNotifier(tSerie);
                }

		        // Update-Messages
		        evt = new ToDoEvent(tSerie.getOwner(), null, tSerie);
                server.getMessageServer().addEvent(evt);
		    }
		}

        save();
	}

	// ToDo löschen
	public void delete(String kuerzel, ToDo toDo, boolean serie) throws RemoteException
	{
	    Datum now = new Datum(new Date());

	    if(!serie)
	    {
	        // remove
	        removeToDo(toDo);

	        if(toDo.getFälligPer().isGreater(now) >= 0)
	        {
	            // NfktQueue
	            server.getNfktQueue().removeNotifier(toDo);

	            // Messages
	            sendDeleteMessages(toDo, serie);
	        }

	        // Update-Messages
		    ToDoEvent evt = new ToDoEvent(toDo.getOwner(), null, toDo);
            server.getMessageServer().addEvent(evt);

	    } else
	    {
	        long serienID = toDo.getSerie().getID();

            Enumeration e = toDoIDs.elements();
	        while(e.hasMoreElements())
	        {
	            ToDo t = (ToDo) e.nextElement();
	            if(t.getSerie() != null && t.getSerie().getID() == serienID)
	            {
	                // remove
	                removeToDo(t);

	                if(toDo.getFälligPer().isGreater(now) >= 0)
	                {
	                    // NfktQueue
	                    server.getNfktQueue().removeNotifier(t);
	                }

	                // Update-Messages
		            ToDoEvent evt = new ToDoEvent(t.getOwner(), null, t);
                    server.getMessageServer().addEvent(evt);
	            }
	        }

			// Messages
	        if(toDo.getSerie().getEnde().isGreater(now) >= 0)
	        {   sendDeleteMessages(toDo, serie);
	        }
	    }

	    save();
	}

    // ToDo ändern
	@SuppressWarnings("unchecked")
	public void update(String kuerzel, ToDo toDoNeu) throws RemoteException
	{
	    Datum now = new Datum(new Date());

	    Long id = new Long(toDoNeu.getID());
	    ToDo toDoAlt = (ToDo) toDoIDs.get(id);

        Serie serieAlt = toDoAlt.getSerie();
        Serie serieNeu = toDoNeu.getSerie();

        // Messages
	    if(toDoNeu.getFälligPer().isGreater(now) >= 0 || (serieNeu != null &&
	                                                      serieNeu.getEnde().isGreater(now) >= 0))
	    {   sendUpdateMessages(toDoAlt, toDoNeu);
	    }

        // update
        updateToDo(toDoAlt, toDoNeu);

		// gelöschte Teilnehmer
        Vector deletedPersons = new Vector();
        Hashtable psnAlt = toDoAlt.getAllPersons();
        Hashtable psnNeu = toDoNeu.getAllPersons();

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
                boolean update = toDoAlt.getErinnernAb().getDaysBetween(toDoNeu.getErinnernAb()) > 0 ||
                                 toDoAlt.getFälligPer().getDaysBetween(toDoNeu.getFälligPer()) < 0;

                e = toDoIDs.elements();
                while(e.hasMoreElements())
                {
                    ToDo t = (ToDo) e.nextElement();

                    if(t.getSerie() != null && t.getID() != toDoAlt.getID() &&
                       t.getSerie().getID() == serieAlt.getID() && t.getErinnernAb().isGreater(toDoAlt.getErinnernAb()) > 0)
                    {
                        // remove
                        removeToDo(t);

                        // NfktQueue
                        if(t.getFälligPer().isGreater(now) >= 0)
	                    {   server.getNfktQueue().removeNotifier(t);
	                    }

                        if(update)
                        {
                            // Update-Messages
    	                    ToDoEvent evt = new ToDoEvent(t.getOwner(), null, t);
                            server.getMessageServer().addEvent(evt);
                        }

                        // Update-Message fuer gelöschte Teilnehmer
                        if(deletedPersons.size() > 0)
                        {
                            Enumeration enumer = deletedPersons.elements();
                            while(enumer.hasMoreElements())
                            {
                                ToDoEvent evt = new ToDoEvent(t.getOwner(), (Person) enumer.nextElement(), t);
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
            Vector serieExpanded = getSerienToDos(toDoNeu);

		    e = serieExpanded.elements();
		    while(e.hasMoreElements())
		    {
		        ToDo tSerie = (ToDo) e.nextElement();

		        // insert
	            insertToDo(tSerie);

	            // Notifikationen
	            if(tSerie.getNotifikationen() != null)
	            {   server.getNfktQueue().addNotifier(tSerie);
                }

	            // Update-Messages
	            ToDoEvent evt = new ToDoEvent(tSerie.getOwner(), null, tSerie);
                server.getMessageServer().addEvent(evt);
            }
        }

        // NfktQueue
        NfktQueue nfktQueue = server.getNfktQueue();

        if(toDoAlt.getFälligPer().isGreater(now) >= 0)
        {   nfktQueue.removeNotifier(toDoAlt);
        }

        if(toDoNeu.getFälligPer().isGreater(now) >= 0)
	    {   nfktQueue.addNotifier(toDoNeu);
        }

        // Update-Messages
		ToDoEvent evt = new ToDoEvent(toDoNeu.getOwner(), null, toDoNeu);
        server.getMessageServer().addEvent(evt);

        if(toDoAlt.getErinnernAb().getDaysBetween(toDoNeu.getErinnernAb()) > 0 ||
           toDoAlt.getFälligPer().getDaysBetween(toDoNeu.getFälligPer()) < 0)
        {
            evt = new ToDoEvent(toDoAlt.getOwner(), null, toDoAlt);
            server.getMessageServer().addEvent(evt);
        }

        // Update-Message fuer gelöschte Teilnehmer
        if(deletedPersons.size() > 0)
        {
            e = deletedPersons.elements();
            while(e.hasMoreElements())
            {
                evt = new ToDoEvent(toDoAlt.getOwner(), (Person) e.nextElement(), toDoAlt);
                server.getMessageServer().addEvent(evt);
            }
        }

        save();
	}

	// Fehlendes Eintragsrecht
	public void sendMissingRight(ToDo toDo, Vector persons) throws RemoteException
	{
	    String msg = "Versuch von " + c + toDo.getOwner().getVorname() + " " + toDo.getOwner().getNachname() + c +
	                 " ein ToDo vom Typ " + c + toDo.getTyp().getBezeichnung() + c +
	                 "\neinzutragen wegen fehlendem Eintragsrecht gescheitert.\n";

	    Enumeration e = persons.elements();
	    while(e.hasMoreElements())
	    {

	        //////////////////////////////////////////////////////////////////////////
            // Message senden // Message senden // Message senden // Message senden //
            //////////////////////////////////////////////////////////////////////////

            MessageEvent evt = new MessageEvent(toDo.getOwner(), (Person) e.nextElement(),
                                                msg, "Fehlendes Eintragsrecht");
            // send
            server.getMessageServer().addEvent(evt);
        }
	}
}