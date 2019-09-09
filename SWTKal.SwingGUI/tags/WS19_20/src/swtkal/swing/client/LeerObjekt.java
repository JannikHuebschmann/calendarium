package swtkal.swing.client;
// Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import swtkal.domain.Datum;
import swtkal.swing.elements.PanelMitPopup;

//////////////////////////////////////////////////////////////////////////////////////
// LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt //
//////////////////////////////////////////////////////////////////////////////////////
public class LeerObjekt extends PanelMitPopup
{
	private static final long serialVersionUID = -867792269359339471L;
	private Datum date = null;

	void setDate(Datum d) {
		date = d;
	}

	Datum getDate() {
		return date;
	}
}
