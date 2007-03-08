package client.ansicht; //
//////////////////////////

import javax.swing.*;

import java.util.*;
import java.awt.*;

import basisklassen.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// ToDoListeObjekt // ToDoListeObjekt // ToDoListeObjekt // ToDoListeObjekt // ToDoListeObjekt //
/////////////////////////////////////////////////////////////////////////////////////////////////

class ToDoListeObjekt extends ListeObjekt {
	ToDoListeObjekt(Ansicht s, Datum d, Vector toDo) {
		super(s, d);
		create(toDo);
	}

	void create(Vector toDo) {
		ToDoScrollListe liste = new ToDoScrollListe(toDo);
		gui.add("Center", liste);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ToDoScrollListe // ToDoScrollListe // ToDoScrollListe // ToDoScrollListe
	// //
	// ////////////////////////////////////////////////////////////////////////////
	class ToDoScrollListe extends JScrollPane {
		private static final long serialVersionUID = -6525673116256360861L;
		JPanel mainPane;

		ToDoScrollListe(Vector toDo) {
			mainPane = new JPanel();
			create(toDo);
		}

		void create(Vector toDo) {
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			mainPane.setLayout(gridbag);

			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.gridx = 0;
			c.gridy = 0;

			if (toDo != null && toDo.size() > 0) {
				Enumeration e = toDo.elements();
				while (e.hasMoreElements()) {
					ToDo tD = (ToDo) e.nextElement();
					ToDoObjekt tObj = new ToDoObjekt(tD, sicht.openCal, 10);

					if (tObj.isLeseBerechtigt()) {
						tObj.addMouseListener(sicht);
						tObj.addActionListener(sicht);

						gridbag.setConstraints(tObj, c);
						mainPane.add(tObj);
						c.gridy++;
					}
				}
			}

			LeerObjekt pane = new LeerObjekt();
			pane.setBackground(Color.white);
			pane.setItems(new String[] { "ToDo eintragen" });
			pane.setDate(date);

			// Listener
			pane.addMouseListener(sicht);
			pane.addActionListener(sicht);

			c.weighty = 1.0;
			gridbag.setConstraints(pane, c);
			mainPane.add(pane);

			// Create the JScrollPane.
			setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			// Tell the scroll pane which component to scroll.
			setViewportView(mainPane);
		}
	}
}
