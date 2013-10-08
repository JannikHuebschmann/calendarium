package swtkal.swing;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import javax.swing.event.*;
import java.awt.event.*;

//////////////////////////////////////////////////////////////////////////////////////////
// ListenerForActions // ListenerForActions // ListenerForActions // ListenerForActions //
//////////////////////////////////////////////////////////////////////////////////////////

public class ListenerForActions implements ActionListener {
	// EventList
	private EventListenerList mActionListenerList;

	public ListenerForActions() {
		mActionListenerList = new EventListenerList();

	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void addActionListener(ActionListener listener) {
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener) {
		mActionListenerList.remove(ActionListener.class, listener);
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

	public void actionPerformed(ActionEvent e) {
		fireActionEvent(e);
	}
}
