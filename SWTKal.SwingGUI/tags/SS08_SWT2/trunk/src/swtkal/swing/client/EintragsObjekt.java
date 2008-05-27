package swtkal.swing.client;
// Achtung: Klasse im Wesentlichen unververaendert aus Calendarium uebernommen

import java.awt.event.*;
import javax.swing.event.*;

import swtkal.domain.*;
import swtkal.swing.elements.PanelMitPopup;

////////////////////////////////////////////////////////////////////////////////////////////
// EintragsObjekt // EintragsObjekt // EintragsObjekt // EintragsObjekt // EintragsObjekt //
////////////////////////////////////////////////////////////////////////////////////////////

public class EintragsObjekt extends PanelMitPopup implements MouseListener {
	private static final long serialVersionUID = -8818548102392064429L;

//	OffeneKalender openCal;

	Eintrag eintrag;

	String kuerzel = "";

	// LeseBerechtigung?
	boolean isBerechtigt = false;

	// registrierte Listener
	private EventListenerList mMouseListenerList;

	public EintragsObjekt() {
		mMouseListenerList = new EventListenerList();
	}

	public void setContextMenu() {
		String typ;

		if (eintrag.getClass() == Termin.class) {
			typ = "Termin";
		} else {
			typ = "ToDo";
		}

//		if (eintrag.getHyperlink().length() == 0) {
			setItems(new String[] { typ + " eintragen", typ + " bearbeiten",
					typ + " löschen" /* , "Nachricht schicken" */ });
//		} else {
//			setItems(new String[] { typ + " eintragen", typ + " bearbeiten",
//					typ + " löschen", "Nachricht schicken",
//					"Hyperlink verfolgen" });
//		}
	}

	public Eintrag getEintrag() {
		return eintrag;
	}

	public boolean isLeseBerechtigt() {
		return isBerechtigt;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	// EventSteuerung // EventSteuerung // EventSteuerung // EventSteuerung //
	// EventSteuerung //
	public void addMouseListener(MouseListener listener) {
		mMouseListenerList.add(MouseListener.class, listener);
	}

	public void removeMouseListener(MouseListener listener) {
		mMouseListenerList.remove(MouseListener.class, listener);
	}

	protected void fireMouseEvent(MouseEvent e) {
		Object[] listeners = mMouseListenerList.getListenerList();

		MouseEvent newEvent = new MouseEvent(this, e.getID(), e.getWhen(), e
				.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e
				.isPopupTrigger());

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MouseListener.class) {
				switch (newEvent.getID()) {
				case MouseEvent.MOUSE_ENTERED:
					((MouseListener) listeners[i + 1]).mouseEntered(newEvent);
					break;

				case MouseEvent.MOUSE_EXITED:
					((MouseListener) listeners[i + 1]).mouseExited(newEvent);
					break;

				case MouseEvent.MOUSE_CLICKED:
					super.processMouseEvent(newEvent);
					((MouseListener) listeners[i + 1]).mouseClicked(newEvent);
				}
			}
		}
	}

	protected void fireActionEvent(ActionEvent e) {
		Object[] listeners = mActionListenerList.getListenerList();

		ActionEvent newEvent = new ActionEvent(this, e.getID(), e
				.getActionCommand());

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(newEvent);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		fireMouseEvent(e);
	}

	public void mouseEntered(MouseEvent e) {
		fireMouseEvent(e);
	}

	public void mouseExited(MouseEvent e) {
		fireMouseEvent(e);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
