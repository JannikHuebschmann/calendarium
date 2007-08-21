package monitor; //
///////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


import data.Data;
import basisklassen.Person;

////////////////////////////////////////////////////////////////////////////////////////////
// Logins // Logins // Logins // Logins // Logins // Logins // Logins // Logins // Logins //
////////////////////////////////////////////////////////////////////////////////////////////

public class Logins implements ActionListener
{
    // graphical Representation
        private JInternalFrame gui = new JInternalFrame("Logged Users", true, true, false, true);

    // TableModel
    private DefaultTableModel mTableModel;

    public Logins()
    {
        gui.setSize(350, 250);
        gui.setPreferredSize(gui.getSize());
        gui.getContentPane().setLayout(new BorderLayout());

        create();

        // InternalFrameListener
        gui.addInternalFrameListener(new InternalFrameAdapter()
        {
            public void internalFrameClosing(InternalFrameEvent e)
            {
                try
                {   gui.setSelected(false);
                } catch(java.beans.PropertyVetoException ex) {}

                gui.setVisible(false);
            }

            public void internalFrameActivated(InternalFrameEvent e)
            {   fill();
            }

        });
    }

    void create()
    {
        JPanel tablePane = new JPanel();
        tablePane.setLayout(new BorderLayout());
        tablePane.setBorder(new EmptyBorder(10, 10, 10, 10));

        mTableModel = new DefaultTableModel();
        mTableModel.setColumnIdentifiers(new String[]{"Kzl", "Name", "Adresse"});

        JTable mTable = new JTable(mTableModel);
        mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            mTable.getTableHeader().setReorderingAllowed(false);
        mTable.getTableHeader().setResizingAllowed(true);

        TableColumn col = mTable.getColumn("Kzl");
        col.setMinWidth(26);

        col = mTable.getColumn("Name");
        col.setMinWidth(105);

        col = mTable.getColumn("Adresse");
        col.setMinWidth(105);

        JScrollPane scrollPane = new JScrollPane(mTable);
        scrollPane.setBackground(SystemColor.window);

        tablePane.add("Center", scrollPane);

        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(new EmptyBorder(0, 10, 10, 10));
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

        JButton button = new JButton("Refresh");
        button.setPreferredSize(new Dimension(100, 20));
        button.setMnemonic('R');
        button.setActionCommand("Refresh");
        button.addActionListener(this);

        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(button);
        buttonPane.add(Box.createHorizontalGlue());

        gui.getContentPane().add("Center", tablePane);
        gui.getContentPane().add("South", buttonPane);
    }

    void fill()
    {
        int i, count, z;

        Hashtable logs = Data.msgServer.getConnections();

        for(i = 0, count = mTableModel.getRowCount(); i < count; i++)
        {   mTableModel.removeRow(0);
        }

        Enumeration e = logs.keys();
        while(e.hasMoreElements())
        {
            Person person = (Person) e.nextElement();

            // Sortiert einfuegen
            for(i = 0, count = mTableModel.getRowCount(); i < count; i++)
            {
                if(person.getNameLang().compareTo((String) mTableModel.getValueAt(i, 1)) < 0) break;
            }

            Enumeration enumer = ((Hashtable) logs.get(person)).keys();
            z = 0;

            while(enumer.hasMoreElements())
            {   mTableModel.insertRow(i + z, new String[]{person.getKuerzel(),
                                                          person.getNameLang(),
                                                          (String) enumer.nextElement()});
                z++;
            }
        }
    }

    public JInternalFrame getGUI()
    {   return gui;
    }

    public void actionPerformed(ActionEvent e)
    {   fill();
    }
}
