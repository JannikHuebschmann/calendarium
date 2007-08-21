package client.ansicht; //
//////////////////////////

import basisklassen.Datum;

//////////////////////////////////////////////////////////////////////////////////////
// LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt // LeerObjekt //
//////////////////////////////////////////////////////////////////////////////////////
public class LeerObjekt extends PanelMitPopup {
	private static final long serialVersionUID = -867792269359339471L;
	private Datum date = null;

	void setDate(Datum d) {
		date = d;
	}

	Datum getDate() {
		return date;
	}
}
