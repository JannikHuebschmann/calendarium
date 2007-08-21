package client.ansicht; //
//////////////////////////

import javax.swing.*;
import java.awt.*;

import basisklassen.Datum;

////////////////////////////////////////////////////////////////////////////////////////////
// ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt // ListeObjekt //
////////////////////////////////////////////////////////////////////////////////////////////

class ListeObjekt {
	// parentWindow
	Ansicht sicht;

	// graphical Representation
	JPanel gui = new JPanel();

	// Date
	Datum date;

	ListeObjekt(Ansicht s, Datum d) {
		sicht = s;
		date = new Datum(d.getDate());

		gui.setLayout(new BorderLayout());
	}

	JPanel getGUI() {
		return gui;
	}
}
