package client.utility.darstellung; //
//////////////////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

////////////////////////////////////////////////////////////////////////////////////////////
// Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile // Pfeile //
////////////////////////////////////////////////////////////////////////////////////////////

public class Pfeile extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8868672038490883289L;
	// Events
	private EventListenerList mActionListenerList;

	public Pfeile(int typ) {
		mActionListenerList = new EventListenerList();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(0, 5, 0, 5));
		create(typ);
	}

	void create(int typ) {
		ImageIcon pfeilRe = client.Client.loadImageIcon("pfeil_re.gif");
		ImageIcon pfeilLi = client.Client.loadImageIcon("pfeil_li.gif");

		JButton b1 = new JButton(pfeilRe);
		b1.setPreferredSize(new Dimension(23, 20));
		b1.setMargin(new Insets(0, 0, 0, 0));
		b1.setAlignmentX((float) 0.5);
		b1.setActionCommand("rightArrow" + typ);
		b1.addActionListener(this);

		JButton b2 = new JButton(pfeilLi);
		b2.setPreferredSize(new Dimension(23, 20));
		b2.setMargin(new Insets(0, 0, 0, 0));
		b2.setAlignmentX((float) 0.5);
		b2.setActionCommand("leftArrow" + typ);
		b2.addActionListener(this);

		add(Box.createVerticalGlue());
		add(Box.createRigidArea(new Dimension(30, 60)));
		add(b1);
		add(Box.createRigidArea(new Dimension(30, 10)));
		add(b2);
		add(Box.createVerticalGlue());
	}

	// ActionListener ///////////////////////////////////////////////////
	public void addActionListener(ActionListener listener) {
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener) {
		mActionListenerList.remove(ActionListener.class, listener);
	}

	protected void fireActionEvent(ActionEvent e) {
		Object[] listeners = mActionListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		fireActionEvent(e);
	}
}
