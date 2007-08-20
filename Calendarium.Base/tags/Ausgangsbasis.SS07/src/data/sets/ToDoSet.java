package data.sets; //
/////////////////////

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.rmi.*;

import data.Data;
import basisklassen.*;
import interfaces.ToDoSetInterface;

/////////////////////////////////////////////////////////////////////////////////////////////////////
// ToDoSet // ToDoSet // ToDoSet // ToDoSet // ToDoSet // ToDoSet // ToDoSet // ToDoSet // ToDoSet //
/////////////////////////////////////////////////////////////////////////////////////////////////////

public class ToDoSet
{
    private ToDoSetInterface ti;
    private Data daten;

    public ToDoSet(Data d, String h)
    {
        daten = d;
        try
        {   ti = (ToDoSetInterface) Naming.lookup(h + "ToDoSetRemote");

        } catch(Exception e)
                {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    private int question(ToDo toDo)
    {
        Object[] options = { "ToDo löschen", "Serie löschen", "Abbrechen" };
        char c = 34;

        String line1 = "Der ToDo-Eintrag " + c + toDo.getKurzText() + c + " ist Bestandteil einer Serie.";
        String line2 = "Wollen Sie nur diesen Eintrag oder die gesamte Serie löschen?";

        JTextArea area = new JTextArea(line1 + '\n' + line2);

        area.setBackground(SystemColor.control);
        area.setEditable(false);
        area.setColumns(Math.max(line1.length(), line2.length()));
        area.setRows(2);

                JOptionPane optionPane = new JOptionPane(area, JOptionPane.QUESTION_MESSAGE,
                                                         JOptionPane.YES_NO_OPTION, null,
                                                         options, options[0]);

        JDialog dialogPane = optionPane.createDialog(Data.parentFrame, "ToDo löschen");

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

    // ToDo-Einträge eines Tages ausgeben
    public Vector getToDoVom(Datum vomDat, Vector personenListe)
    {   try
        {   return ti.getToDoVom(vomDat, personenListe);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

        // ToDo-Einträge in einem Zeitraum ausgeben
        public Vector[] getToDoVonBis(Datum vonDat, Datum bisDat, Vector personenListe)
        {   try
        {   return ti.getToDoVonBis(vonDat, bisDat, personenListe);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }

        return null;
    }

        // ein bestimmtes ToDo ausgeben
        public ToDo getToDoByID(long id)
        {   try
        {   return ti.getToDoByID(id);

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

    // ToDo anlegen
    public void create(ToDo toDo)
    {   try
        {   ti.create(Data.user.getKuerzel(),toDo);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // ToDo löschen
    public void delete(ToDo toDo)
    {
        try
        {   if(toDo.getSerie() == null)
            {
                char c = 34;
                if(daten.areYouSure("Wollen sie den ToDo-Eintrag " + c + toDo.getKurzText() + c +
                                    " wirklich löschen?", "ToDo löschen"))
                {   ti.delete(Data.user.getKuerzel(),toDo, false);
                }
            }else
            {
                int q;

                if((q = question(toDo)) < 2)
                {
                    if(q == 0)
                    {   ti.delete(Data.user.getKuerzel(),toDo, false);
                    } else
                    {   ti.delete(Data.user.getKuerzel(),toDo, true);
                    }
                }
            }
        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // ToDo ändern
    public void update(ToDo toDo)
    {   try
        {   ti.update(Data.user.getKuerzel(),toDo);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }

    // Fehlendes Eintragsrecht
        public void sendMissingRight(ToDo toDo, Vector persons)
        {   try
        {   ti.sendMissingRight(toDo, persons);

        } catch(RemoteException e)
        {   e.printStackTrace();
                        daten.showDialog();
        }
    }
}
