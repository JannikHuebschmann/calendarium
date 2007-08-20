package data; //
////////////////

import javax.swing.*;

import java.awt.*;

import data.sets.*;
import basisklassen.Person;

//////////////////////////////////////////////////////////////////////////////////////////
// Data // Data // Data // Data // Data // Data // Data // Data // Data // Data // Data //
//////////////////////////////////////////////////////////////////////////////////////////

public class Data
{
    // parentFrame
    public static JFrame parentFrame;

        // Daten
    public static PersonSet personen;       // Personen
    public static GroupSet gruppen;         // Gruppen
    public static TypSet typen;             // Typen
    public static FeiertagSet feiertage;    // Feiertage
    public static RightSet rechte;          // Rechte
    public static TerminSet termine;        // Termine
    public static ToDoSet toDo;             // ToDo-Eintäge
    public static Message msgServer;        // Connect
    public static AntwortSet antwort;

        public static Person user = null;           // User

        public Data(JFrame f, String hostname)
        {
            parentFrame = f;

                // Daten laden
                loadDaten(hostname);
        }

        // Initialisierung
        private void loadDaten(String hostname)
        {
                // Connect
                msgServer = new Message(this, hostname);

                // PersonenMenge
                personen = new PersonSet(this, hostname);

                // GruppenMenge
                gruppen = new GroupSet(this, hostname);

                // TypenMenge
                typen = new TypSet(this, hostname);

                // FeiertagsMenge
                feiertage = new FeiertagSet(this, hostname);

                // RechtsMenge
                rechte = new RightSet(this, hostname);

                // Termine
                termine = new TerminSet(this, hostname);

                // ToDo-Einträge
                toDo = new ToDoSet(this, hostname);
                
                //Antwort by johnny
                antwort = new AntwortSet(this, hostname);
        }

        public void setUser(Person p)
        {       user = p;
        }

        // Fehlermeldung
        public void showDialog()
        {
                String text = "Derzeit besteht keine Verbindung zum Server,\n" +
                      "bitte starten Sie das Programm zu einem späteren\n" +
                      "Zeitpunkt neu.\n";

        JTextArea area = new JTextArea(text);

        area.setBackground(SystemColor.control);
        area.setEditable(false);
        area.setColumns(48);
        area.setRows(3);

        JOptionPane optionPane = new JOptionPane(area, JOptionPane.WARNING_MESSAGE);
        JDialog dialogPane = optionPane.createDialog(parentFrame, "Fehlermeldung");
        dialogPane.show();

                System.exit(0);
        }

        public boolean areYouSure(String text, String title)
    {
        Object[] options = { "Ja", "Nein" };

        JTextArea area = new JTextArea(text);

        area.setBackground(SystemColor.control);
        area.setEditable(false);
        area.setColumns(text.length());
        area.setRows(1);

                JOptionPane optionPane = new JOptionPane(area, JOptionPane.QUESTION_MESSAGE,
                                                         JOptionPane.YES_NO_OPTION, null,
                                                         options, options[0]);

        JDialog dialogPane = optionPane.createDialog(parentFrame, title);

        dialogPane.show();
        dialogPane.dispose();

                Object selectedValue = optionPane.getValue();

                if(selectedValue != null)
                {   if(options[0].equals(selectedValue)) return true;
                }
                return false;
        }
}
