package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;


////////////////////////////////////////////////////////////////////////////////////////////////////////
// PanelMitPopup // PanelMitPopup // PanelMitPopup // PanelMitPopup // PanelMitPopup // PanelMitPopup //
////////////////////////////////////////////////////////////////////////////////////////////////////////

class PanelMitPopup extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1542575920659506128L;

	private JPopupMenu mMenu;

	private String[] items;

	// registrierte Listener
	EventListenerList mActionListenerList;

	PanelMitPopup() {
		mActionListenerList = new EventListenerList();
	}

	void setItems(String[] i) {
		items = i;
	}

	// MouseEvent // MouseEvent // MouseEvent // MouseEvent // MouseEvent //
	// MouseEvent //
	protected void processMouseEvent(MouseEvent e) {
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			if (mMenu == null) {
				// create
				mMenu = new JPopupMenu();
				{
					JMenuItem mOption[] = new JMenuItem[items.length];

					for (int i = 0; i < items.length; i++) {
						mOption[i] = mMenu.add(new JMenuItem(items[i]));
						mOption[i].setActionCommand(items[i]);
						mOption[i].addActionListener(this);
					}
				}
			}

			JComponent evtObj = (JComponent) e.getSource();
			mMenu.show(evtObj, e.getX(), e.getY());

		} else {
			super.processMouseEvent(e);
		}
	}

	// ActionEvent // ActionEvent // ActionEvent // ActionEvent // ActionEvent
	// //
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
