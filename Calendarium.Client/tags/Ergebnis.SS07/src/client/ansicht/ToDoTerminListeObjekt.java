package client.ansicht; //
//////////////////////////

import javax.swing.*;

import java.util.*;
import java.awt.*;

import basisklassen.*;

//////////////////////////////////////////////////////////////////////////////////////////////////////
// ToDoTerminListeObjekt // ToDoTerminListeObjekt // ToDoTerminListeObjekt // ToDoTerminListeObjekt //
//////////////////////////////////////////////////////////////////////////////////////////////////////

class ToDoTerminListeObjekt extends ListeObjekt {
	private Vector toDo;

	private Vector termine;

	ToDoTerminListeObjekt(Ansicht s, Datum d, Vector toDo, Vector termine) {
		super(s, d);

		this.toDo = toDo;
		this.termine = termine;

		create(toDo, termine);
	}

	void create(Vector toDo, Vector termine) {
		if (toDo != null || termine != null) {
			ToDoTermineScrollListe liste = new ToDoTermineScrollListe(toDo,
					termine);
			gui.add("Center", liste);
		}
	}

	Vector getTermine() {
		return termine;
	}

	Vector getToDo() {
		return toDo;
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ToDoTermineScrollListe // ToDoTermineScrollListe //
	// ToDoTermineScrollListe //
	// //////////////////////////////////////////////////////////////////////////////
	class ToDoTermineScrollListe extends JScrollPane {
		private static final long serialVersionUID = -6973611487177235167L;
		JPanel mainPane;

		ToDoTermineScrollListe(Vector toDo, Vector termine) {
			mainPane = new JPanel();
			create(toDo, termine);
		}

		void create(Vector toDo, Vector termine) {
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			mainPane.setLayout(gridbag);

			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.gridx = 0;
			c.gridy = 0;

			// ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo // ToDo //
			// ToDo // ToDo //
			if (toDo != null && toDo.size() > 0) {
				Enumeration e = toDo.elements();
				while (e.hasMoreElements()) {
					ToDo tD = (ToDo) e.nextElement();

					ToDoObjekt tObj = new ToDoObjekt(tD, sicht.openCal, 10);
					tObj.addMouseListener(sicht);
					tObj.addActionListener(sicht);

					gridbag.setConstraints(tObj, c);
					mainPane.add(tObj);
					c.gridy++;
				}
			}

			// Termin // Termin // Termin // Termin // Termin // Termin //
			// Termin // Termin //
			if (termine != null && termine.size() > 0) {
				Enumeration e = termine.elements();
				while (e.hasMoreElements()) {
					Termin termin = (Termin) e.nextElement();

					TerminObjekt tObj = new TerminObjekt(termin, sicht.openCal,
							false, 10);
					tObj.addMouseListener(sicht);
					tObj.addActionListener(sicht);

					gridbag.setConstraints(tObj, c);
					mainPane.add(tObj);
					c.gridy++;
				}
			}

			LeerObjekt pane = new LeerObjekt();
			pane.setBackground(Color.white);
			pane
					.setItems(new String[] { "ToDo eintragen",
							"Termin eintragen" });
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
