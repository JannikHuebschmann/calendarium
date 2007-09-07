package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Datumsfeld // Datumsfeld // Datumsfeld // Datumsfeld // Datumsfeld // Datumsfeld // Datumsfeld //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class Datumsfeld extends JPanel implements ActionListener, FocusListener
{
	private static final long serialVersionUID = -2327672863703805466L;

// Datetoken an irgendeine vernünftige Stelle/Klasse schieben
	final String DATETOKEN = ".";

	private Zahlenfeld tag, monat, jahr;

	// EventList
	private EventListenerList mFocusListenerList;

	public Datumsfeld()
	{
		mFocusListenerList = new EventListenerList();

		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(this.getMaximumSize().width, 23));
		setBackground(Color.white);
		setToolTipText("TT:MM:JJJJ");
		create();
	}

	void create()
	{
		tag = new Zahlenfeld(2);
		tag.setPreferredSize(new Dimension(15, 15));
		tag.setAlignmentY((float) 0.5);
		tag.setBorder(new CustomBorder(Color.black, false, false, true, false));
		tag.setFocusAccelerator('d');
		tag.addActionListener(this);
		tag.addFocusListener(this);

		monat = new Zahlenfeld(2);
		monat.setPreferredSize(new Dimension(15, 15));
		monat.setAlignmentY((float) 0.5);
		monat.setBorder(new CustomBorder(Color.black, false, false, true, false));
		monat.addActionListener(this);
		monat.addFocusListener(this);

		jahr = new Zahlenfeld(4);
		jahr.setPreferredSize(new Dimension(30, 15));
		jahr.setAlignmentY((float) 0.5);
		jahr.setBorder(new CustomBorder(Color.black, false, false, true, false));
		jahr.addActionListener(this);
		jahr.addFocusListener(this);

		JLabel pkt1 = new JLabel(".");
		JLabel pkt2 = new JLabel(".");

		add(tag);
		add(pkt1);
		add(monat);
		add(pkt2);
		add(jahr);
	}

	public void setDate(GregorianCalendar cal)
	{
		int dy = cal.get(Calendar.DATE);
		int mt = cal.get(Calendar.MONTH) + 1;
		int yr = cal.get(Calendar.YEAR);

		if (dy < 10)
		{
			tag.setText("0" + String.valueOf(dy));
		}
		else
		{
			tag.setText(String.valueOf(dy));
		}

		if (mt < 10)
		{
			monat.setText("0" + String.valueOf(mt));
		}
		else
		{
			monat.setText(String.valueOf(mt));
		}

		jahr.setText(String.valueOf(yr));
	}

	public void setDate(String date)
	{
		// Format: TT.MM.JJJJ
		tag.setText(date.substring(0, 2));
		monat.setText(date.substring(3, 5));
		jahr.setText(date.substring(6, 10));
	}

	public String getDate()
	{
		String[] fill = new String[3];

		fill[0] = tag.getText().length() == 0 ? "99" : "";
		fill[1] = monat.getText().length() == 0 ? "99" : "";
		fill[2] = jahr.getText().length() == 0 ? "9999" : "";

		return fill[0] + tag.getText() + DATETOKEN + fill[1] + monat.getText()
				+ DATETOKEN + fill[2] + jahr.getText();
	}

	public GregorianCalendar getDateGreg()
	{
		return new GregorianCalendar(Integer.valueOf(jahr.getText()).intValue(),
				Integer.valueOf(monat.getText()).intValue() - 1, Integer.valueOf(
						tag.getText()).intValue());
	}

	public void setEnabled(boolean state)
	{
		tag.setEnabled(state);
		monat.setEnabled(state);
		jahr.setEnabled(state);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "NEXT_CONTROL")
		{
			if (e.getSource() == tag)
			{
				monat.requestFocus();
			}
			else if (e.getSource() == monat)
			{
				jahr.requestFocus();
			}
		}
		else if (e.getActionCommand() == "PREVIOUS_CONTROL")
		{
			if (e.getSource() == monat)
			{
				tag.requestFocus();
			}
			else if (e.getSource() == jahr)
			{
				monat.requestFocus();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// FocusListener // FocusListener // FocusListener // FocusListener //
	// FocusListener //
	// /////////////////////////////////////////////////////////////////////////////////////
	public void addFocusListener(FocusListener listener)

	{
	// hier gab es eine nullpointer, wenn das weg ist funkt es trotzdem?!?!?
	// mFocusListenerList.add(FocusListener.class, listener); //nullpointer

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
			if (evt.getSource() != tag && evt.getSource() != monat
					&& evt.getSource() != jahr)
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
