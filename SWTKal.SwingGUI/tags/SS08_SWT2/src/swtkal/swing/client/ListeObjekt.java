package swtkal.swing.client;
// Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import javax.swing.*;

import swtkal.domain.Datum;

////////////////////////////////////////////////////////////////////////////////////////////
// ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt //
////////////////////////////////////////////////////////////////////////////////////////////

class ListeObjekt
{
	// parentWindow
	Ansicht sicht;

	// graphical Representation
	JPanel gui = new JPanel();

	// Date
	Datum date, nextDate;

	ListeObjekt(Ansicht s, Datum d)
	{
		sicht = s;
		date = new Datum(d.getDateStr());
		nextDate = new Datum(d.getDateStr());
		nextDate.add(1);

		gui.setLayout(new BorderLayout());
	}

	JPanel getGUI()
	{
		return gui;
	}
}
