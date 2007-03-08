package server; //
//////////////////

import java.util.*;

import event.*;
import basisklassen.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// NfktQueue // NfktQueue // NfktQueue // NfktQueue // NfktQueue // NfktQueue // NfktQueue //
/////////////////////////////////////////////////////////////////////////////////////////////
public class NfktQueue implements Runnable
{   
    // Server
    private Server server;
    
    // NfktQueue
    private Vector nfktQueue;
    
    // Thread
    private Thread thread;
    
    // Datum
    private Datum now = new Datum();
    
    public NfktQueue(Server s)
    {   
        server = s;
        nfktQueue = new Vector();
        
        // Check for Notifications
        thread = new Thread(this);
        thread.start();
    }
    
    public void run()
    {   
        while(true)
        {   
            now.setDatum(new Date());
            
            try
            {   Thread.sleep(5000);
            } catch(InterruptedException e) {}
            
            if(nfktQueue.size() > 0)
            {   
                Notifier notifier = (Notifier) nfktQueue.firstElement();
                if(notifier.getZeitpunkt().isGreater(now) <= 0)
                {   
                    notifier.notifizieren();
                    notifier.setErledigt(true);
                    
                    nfktQueue.removeElement(notifier); 
                }
            }
        }
    }
    
    public void createNotifiers(Hashtable einträge)
    {   
        Enumeration e = einträge.elements();
        while(e.hasMoreElements())
        {   
            Eintrag eintrag = (Eintrag) e.nextElement();
            if(eintrag.getNotifikationen() != null)
            {   
                Notifikation[] nfkt = eintrag.getNotifikationen();
                for(int i = 0; i < nfkt.length; i++)
                {   
                    if(!nfkt[i].isErledigt())
                    {   
                        Datum nfktZeit = eintrag.getNfktRelevant().addDauer(- nfkt[i].getStunden());
                        
                        // Sortiert einordnen
                        insertSorted(new Notifier(nfktZeit, i, eintrag));
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public void insertSorted(Notifier notifier)
    {   
        int uGrenze = 0;
        int oGrenze = nfktQueue.size();
        
        if(oGrenze > 0 && !((Notifier) nfktQueue.firstElement()).after(notifier))
        {   
            int pos = oGrenze / 2;
            
            do
            {   if(((Notifier) nfktQueue.elementAt(pos)).after(notifier))
                {   
                    oGrenze = pos;
                    pos -= (oGrenze - uGrenze) / 2;
                    
                } else
                {   
                    uGrenze = pos;
                    pos += Math.max((oGrenze - uGrenze) / 2, 1);
                }
                
            } while(oGrenze - uGrenze > 1);
            
            if(pos > oGrenze)
            {   nfktQueue.addElement(notifier);
            } else
            {   nfktQueue.insertElementAt(notifier, pos);
            }
            
        } else nfktQueue.insertElementAt(notifier, 0);
    }
    
    
    public void addNotifier(Eintrag entry)
    {   
        Notifikation[] nfkt = entry.getNotifikationen();

        if(nfkt != null)
        {   
            for(int i = 0; i < nfkt.length; i++)
		    {   
				Datum nfktZeit = entry.getNfktRelevant().addDauer(- nfkt[i].getStunden());
                insertSorted(new Notifier(nfktZeit, i, entry));
            }
        }
    }
    
    public void removeNotifier(Termin t)
    {   
        if(t.getNotifikationen() != null && nfktQueue.size() > 0)
        {   
            int nfAnz = t.getNotifikationen().length, nfCount = 0;
            
            int count = 0;
            while(count < nfktQueue.size() && nfCount < nfAnz)
            {   
                Notifier notifier = (Notifier) nfktQueue.elementAt(count);
                
                if(notifier.getEintrag() instanceof Termin && 
                   notifier.getEintrag().getID() == t.getID())
                {   
                    nfktQueue.removeElementAt(count);
                    nfCount++;
                    
                } else count++;
            }
        }
    }
    
    public void removeNotifier(ToDo t)
    {   
        if(t.getNotifikationen() != null && nfktQueue.size() > 0)
        {
            int nfAnz = t.getNotifikationen().length, nfCount = 0;
            
            int count = 0;
            while(count < nfktQueue.size() && nfCount < nfAnz)
            {   
                Notifier notifier = (Notifier) nfktQueue.elementAt(count);
                
                if(notifier.getEintrag() instanceof ToDo && 
                   notifier.getEintrag().getID() == t.getID())
                {   
                    nfktQueue.removeElementAt(count);
                    nfCount++;
                    
                } else count++;
            }
        }
    }
    
    class Notifier
    {
        private Datum zeitpunkt;
        private int nfktNr;
        private Eintrag eintrag;
        
        Notifier(Datum z, int n, Eintrag e)
        {   
            zeitpunkt = z;
            nfktNr = n;
            eintrag = e;
        }
        
        boolean after(Notifier n)
        {   return (zeitpunkt.isGreater(n.getZeitpunkt()) > 0);
        }
        
        Datum getZeitpunkt()
        {   return zeitpunkt;
        }
        
        Eintrag getEintrag()
        {   return eintrag;
        }
        
        void notifizieren()
        {   
            char c = 34;
            String msg;
            
            double hr = eintrag.getNfktRelevant().getHoursBetween(now);
            Notifikation nfkt = eintrag.getNotifikationen()[nfktNr];
            
            if(hr > 0)
            {   
                int h = (int) hr;
                int min = (int) ((hr - h) * 60);
                
                String txt, hTxt, mTxt;
                
                switch(h)
                {   case 0:
                        hTxt = "";
                        break;
                        
                    case 1:
                        hTxt = " 1 Stunde ";
                        break;
                        
                    default:
                        hTxt = " " + h + " Stunden ";
                }
                
                switch(min)
                {   case 0:
                        mTxt = "";
                        break;
                        
                    case 1:
                        mTxt = " 1 Minute " ;
                        break;
                        
                    default:
                        mTxt = " " + min + " Minuten ";
                }
                
                if(hTxt.length() > 0 && mTxt.length() > 0) 
                {   txt = hTxt + "und" + mTxt;
                } else
                {   txt = hTxt + mTxt;
                }
                          
                if(eintrag instanceof Termin)
                {
                    msg = "Der Termin " + c + eintrag.getKurzText() + c + " am " +
                           eintrag.getNfktRelevant().toString() + "\nfindet in" + txt + 
                           "statt.\n";
                } else
                {   
                    msg = "Die Aufgabe " + c + eintrag.getKurzText() + c + " mit Fälligkeit " +
                           eintrag.getNfktRelevant().toString() + "\nsollte in" + txt + 
                           "erledigt sein.\n";
                }
                
                Hashtable persHash = eintrag.getAllPersonsWithNfkt();    
                
                Enumeration e = persHash.elements();
                while(e.hasMoreElements())
                {   
                    Teilnehmer tn = (Teilnehmer) e.nextElement();
                    if(tn.getNotifikationen()[nfktNr])
                    {
                        //////////////////////////////////////////////////////////////////////////
                        // Message senden // Message senden // Message senden // Message senden //
                        //////////////////////////////////////////////////////////////////////////
                        
                        NfktEvent evt = new NfktEvent(eintrag.getOwner(), (Person) tn.getTeilnehmer(),
                                                      msg, "Notifikation", nfkt.getTyp());
                        // send
                        server.getMessageServer().addEvent(evt);
                    }
                }
            }
        }
        
        void setErledigt(boolean b)
        {   
            Notifikation nfkt = eintrag.getNotifikationen()[nfktNr];
            
            if(eintrag instanceof Termin)
            {   
                nfkt.setErledigt(b);
                server.getTerminSetRemote().save();
                
            } else
            {   
                nfkt.setErledigt(b);
                server.getToDoSetRemote().save();
            }
        }
        
        public String toString()
        {   return zeitpunkt.toString();
        }
    }
}