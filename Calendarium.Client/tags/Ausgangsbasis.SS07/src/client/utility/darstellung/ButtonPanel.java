package client.utility.darstellung; //
//////////////////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

////////////////////////////////////////////////////////////////////////////////////////////
// ButtonPanel // ButtonPanel // ButtonPanel // ButtonPanel // ButtonPanel // ButtonPanel //
////////////////////////////////////////////////////////////////////////////////////////////

public class ButtonPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5585934576683549811L;

	private EventListenerList mActionListenerList;

	private JButton okButton, abbrButton;

	public ButtonPanel(boolean space) {
		mActionListenerList = new EventListenerList();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new EmptyBorder(0, 0, 10, 0));
		create(space);
	}

	void create(boolean space) {
		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(95, 20));
		okButton.setMaximumSize(okButton.getPreferredSize());
		okButton.setMinimumSize(okButton.getPreferredSize());
		okButton.setMnemonic('o');
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);

		abbrButton = new JButton("Abbrechen");
		abbrButton.setPreferredSize(new Dimension(95, 20));
		abbrButton.setMaximumSize(abbrButton.getPreferredSize());
		abbrButton.setMinimumSize(abbrButton.getPreferredSize());
		abbrButton.setMnemonic('a');
		abbrButton.setActionCommand("Abbrechen");
		abbrButton.addActionListener(this);

		if (space)
			add(Box.createHorizontalGlue());
		add(okButton);
		add(Box.createRigidArea(new Dimension(20, 20)));
		add(abbrButton);
		if (space)
			add(Box.createHorizontalGlue());
	}

	public Rectangle getScreenLocation(int t) {
		if (t == 0) {
			return new Rectangle(okButton.getLocationOnScreen().x, okButton
					.getLocationOnScreen().y, okButton.getSize().width,
					okButton.getSize().height);
		} else {
			return new Rectangle(abbrButton.getLocationOnScreen().x, abbrButton
					.getLocationOnScreen().y, abbrButton.getSize().width,
					abbrButton.getSize().height);
		}
	}

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
