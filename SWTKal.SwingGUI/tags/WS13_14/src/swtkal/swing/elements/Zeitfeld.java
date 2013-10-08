package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import swtkal.domain.Datum;
import swtkal.swing.elements.CustomBorder;

//////////////////////////////////////////////////////////////////////////////////////////////////
// Zeitfeld // Zeitfeld // Zeitfeld // Zeitfeld // Zeitfeld // Zeitfeld // Zeitfeld // Zeitfeld //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Zeitfeld extends JPanel implements ActionListener, FocusListener
{
	private static final long serialVersionUID = -6213502841923618430L;

	private Zahlenfeld minuten, stunde;

	// EventList
	private EventListenerList mFocusListenerList;

	public Zeitfeld()
	{
		mFocusListenerList = new EventListenerList();

		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(this.getMaximumSize().width, 23));
		setBackground(Color.white);
		setToolTipText("hh:mm");
		create();
	}

	void create()
	{
		minuten = new Zahlenfeld(2);
		minuten.setPreferredSize(new Dimension(15, 15));
		minuten.setAlignmentY((float) 0.5);
		minuten
				.setBorder(new CustomBorder(Color.black, false, false, true, false));
		minuten.setFocusAccelerator('u');
		minuten.addActionListener(this);
		minuten.addFocusListener(this);

		stunde = new Zahlenfeld(2);
		stunde.setPreferredSize(new Dimension(15, 15));
		stunde.setAlignmentY((float) 0.5);
		stunde
				.setBorder(new CustomBorder(Color.black, false, false, true, false));
		stunde.addActionListener(this);
		stunde.addFocusListener(this);

		JLabel pkt = new JLabel(":");

		add(stunde);
		add(pkt);
		add(minuten);
	}

	public void setTime(String time)
	{
		// Format: hh:mm
		stunde.setText(time.substring(0, 2));
		minuten.setText(time.substring(3, 5));
	}

	public String getTime()
	{
		String[] fill = new String[2];

		fill[0] = stunde.getText().length() == 0 ? "99" : "";
		fill[1] = minuten.getText().length() == 0 ? "99" : "";

		return fill[0] + stunde.getText() + Datum.TIMETOKEN + fill[1]
				+ minuten.getText();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "NEXT_CONTROL")
		{
			if (e.getSource() == stunde)
			{
				minuten.requestFocus();
			}
		}
		else if (e.getActionCommand() == "PREVIOUS_CONTROL")
		{
			if (e.getSource() == minuten)
			{
				stunde.requestFocus();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// FocusListener // FocusListener // FocusListener // FocusListener //
	// FocusListener //
	// /////////////////////////////////////////////////////////////////////////////////////
	public void addFocusListener(FocusListener listener)
	{ // mFocusListenerList.add(FocusListener.class,
	// listener);by
	// johnny
	}

	public void removeFocusListener(FocusListener listener)
	{
		mFocusListenerList.remove(FocusListener.class, listener);
	}

	protected void fireFocusEvent(FocusEvent e)
	{
		Object[] listeners = mFocusListenerList.getListenerList();

		if (listeners.length > 0)
		{
			FocusEvent newEvent = new FocusEvent(this, e.getID());

			for (int i = listeners.length - 2; i >= 0; i -= 2)
			{
				if (listeners[i] == FocusListener.class)
				{
					if (e.getID() == FocusEvent.FOCUS_GAINED)
					{
						((FocusListener) listeners[i + 1]).focusGained(newEvent);
					}
					else
					{
						((FocusListener) listeners[i + 1]).focusLost(newEvent);
					}
				}
			}
		}
	}

	public void focusGained(FocusEvent e)
	{
		fireFocusEvent(e);
	}

	public void focusLost(FocusEvent e)
	{
		EventQueue queue = getToolkit().getSystemEventQueue();
		FocusEvent evt = (FocusEvent) queue.peekEvent(FocusEvent.FOCUS_GAINED);

		if (evt != null)
		{
			if (evt.getSource() != minuten && evt.getSource() != stunde)
			{
				fireFocusEvent(e);
			}
		}
		else
		{
			fireFocusEvent(e);
		}
	}
}
