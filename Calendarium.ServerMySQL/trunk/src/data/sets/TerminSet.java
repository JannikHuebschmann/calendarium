package data.sets; //
/////////////////////

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.TerminSetInterface;

/////////////////////////////////////////////////////////////////////////////////////////////
// TerminSet // TerminSet // TerminSet // TerminSet // TerminSet // TerminSet // TerminSet //
/////////////////////////////////////////////////////////////////////////////////////////////

public class TerminSet
{
    private TerminSetInterface ti;
    private Data daten;

    public TerminSet(Data d, String h)
    {
        daten = d;
        try
        {   ti = (TerminSetInterface) Naming.lookup(h + "TerminSetRemote");

        } catch(Exception e)
                {   e.printStackTrace();
                    System.out.println("<<<Error "+e.getMessage());
                        daten.showDialog();
        }
    }

    private int question(Termin termin)
    {
        Object[] options = { "Termin löschen", "Serie löschen", "Abbrechen" };
        char c = 34;

        String line1 = "Der Termin " + c + termin.getKurzText() + c + " ist Bestandteil einer Serie.";
        String line2 = "Wollen Sie nur den Termin oder die gesamte Serie löschen?";

        JTextArea area = new JTextArea(line1 + '\n' + line2);

        area.setBackground(SystemColor.control);
        area.setEditable(false);
        area.setColumns(Math.max(line1.length(), line2.length()));
        area.setRows(2);

                JOptionPane optionPane = new JOptionPane(area, JOptionPane.QUESTION_MESSAGE,
                                                         JOptionPane.YES_NO_OPTION, null,
                                                         options, options[0]);

        JDialog dialogPane = optionPane.createDialog(Data.parentFrame, "Termin löschen");

        dialogPane.show();
        dialogPane.dispose();

                Object selectedValue = optionPane.getValue();

                if(selectedValue != null)
                {
                    if(options[0].equals(selectedValue))
                    {   return 0;
                    } else
                    {   if(options[1].equals(selectedValue))
                        {   return 1;
                        }
                    }
                }

                return 2;
        }

    // Termine an einem Tag ausgeben
    public Vector getTermineVom(Datum vomDat, Vector personenListe)
    {   try
        {   return ti.getTermineVom(vomDat, personenListe);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

        // Termine in einem Zeitraum ausgeben
        public Vector[] getTermineVonBis(Datum vonDat, Datum bisDat, Vector personenListe)
        {   try
        {   return ti.getTermineVonBis(vonDat, bisDat, personenListe);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

        // Personen mit Terminen
    public Vector[] getPersonsWithTermin(Datum vonDat, Datum bisDat, Vector personenListe)
    {   try
        {   return ti.getPersonsWithTermin(vonDat, bisDat, personenListe);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

    // einen bestimmten Termin ausgeben
        public Termin getTerminByID(long id)
        {   try
        {   return ti.getTerminByID(id);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

    // Konflikte ausgeben
        public Vector getKonflikte(Termin termin)
        {   try
        {   return ti.getKonflikte(termin);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

    // konfliktfreie Intervalle ausgeben
    public Vector getFreeOfKonflikte(Hashtable persons, Datum bgn, Datum end, boolean wk)
    {   try
        {   return ti.getFreeOfKonflikte(persons, bgn, end, wk);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

    // Daten säubern
    public void deleteUntilDate(Datum bis)
    {   try
        {   ti.deleteUntilDate(Data.user, bis);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // Termin anlegen
    public void create(Termin termin)
    {   try
        {   ti.create(Data.user.getKuerzel(),termin);

        } catch(RemoteException e)
        {   e.printStackTrace();
            System.out.println("<<<Error :"+e.getMessage());
                        daten.showDialog();
        }
    }

    // Termin löschen
    public void delete(Termin termin)
    {
        try
        {   if(termin.getSerie() == null)
            {
                char c = 34;
                if(daten.areYouSure("Wollen sie den Termin " + c + termin.getKurzText() + c +
                                    " wirklich löschen?", "Termin löschen"))
                {   ti.delete(Data.user.getKuerzel(),termin, false);
                }
                        } else
            {
                int q;

                if((q = question(termin)) < 2)
                {
                    if(q == 0)
                    {   ti.delete(Data.user.getKuerzel(),termin, false);
                    } else
                    {   ti.delete(Data.user.getKuerzel(),termin, true);
                    }
                }
            }
        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // Termin ändern
    public void update(Termin termin)
    {   try
        {   ti.update(Data.user.getKuerzel(),termin);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // Fehlendes Eintragsrecht
        public void sendMissingRight(Termin termin, Vector persons)
        {   try
        {   ti.sendMissingRight(termin, persons);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }
}
