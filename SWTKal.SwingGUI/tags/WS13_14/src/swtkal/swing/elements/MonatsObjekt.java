package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen
// FIXME Klasse MonatsObjekt enthaelt noch kleinere Fehler und muss ueberarbeitet werden
// insbesondere type casting Probleme in der local class CenteredEditor

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;

import swtkal.domain.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
// MonatsObjekt // MonatsObjekt // MonatsObjekt // MonatsObjekt // MonatsObjekt // MonatsObjekt //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class MonatsObjekt extends JPanel implements ActionListener, ItemListener
{
	private static final long serialVersionUID = -474376454842120422L;

// XXX Farbkodierungen evtl. an eine zentrale Stelle verschieben?	
   static final Color HEADING_BACKGRD = Color.gray;                 // new Color(128, 255, 128);   // Überschrift
   static final Color TAGE_BACKGRD = new Color(230, 230, 230);      // Tage
   static final Color TAG_BACKGRD = new Color(230, 205, 160);       // Datumslabel
   static final Color FEIERTAG_FOREGRD = Color.red;                 // Feiertag

	// Datum
	private GregorianCalendar cal,		// aktuelles Datum
	                          day;		// selektiertes Datum

	// Combobox
	@SuppressWarnings("rawtypes")
	private JComboBox monthCombo;

	@SuppressWarnings("rawtypes")
	private JComboBox yearCombo;

	// Buttons
	private JButton days[] = new JButton[42];

	// JPanel
	private JPanel dyPanel;

	// Events
	private EventListenerList mActionListenerList = new EventListenerList();;

	// YearSelect
	int grenze[] = { 1, 5 };

	public MonatsObjekt(Datum date, String intervall)
	{
		setIntervallGrenzen(intervall);

		cal = (GregorianCalendar) Calendar.getInstance();
		day = (GregorianCalendar) Calendar.getInstance();

		cal.set(Calendar.DATE, date.getDay());
		cal.set(Calendar.MONTH, date.getMonth() - 1);
		cal.set(Calendar.YEAR, date.getYear());

		setLayout(new BorderLayout());
		create(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

		@SuppressWarnings("unused")
		WaitForPaint w = new WaitForPaint();
	}

	public MonatsObjekt(Date datum, String intervall)
	{
		setIntervallGrenzen(intervall);

		cal = (GregorianCalendar) Calendar.getInstance();
		day = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(datum);

		setLayout(new BorderLayout());
		create(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

		@SuppressWarnings("unused")
		WaitForPaint w = new WaitForPaint();
	}

	private void setIntervallGrenzen(String s)
	{
		StringTokenizer st = new StringTokenizer(s, "..");
		int count = 0;

		while (st.hasMoreTokens())
		{
			String g = st.nextToken().trim();

			if (g.startsWith("-") || g.startsWith("+"))
			{
				g = g.substring(1);
			}

			grenze[count] = Integer.valueOf(g).intValue();
			count++;
		}
	}

	private void create(int mt, int yr)
	{
		add("North", createMtSelect(mt, yr));
		add("Center", dyPanel = createDySelect(mt, yr));
	}

	public GregorianCalendar getDate()
	{
		return day;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel createMtSelect(int mt, int yr)
	{
		JPanel selectMonth = new JPanel();
		selectMonth.setLayout(new BoxLayout(selectMonth, BoxLayout.X_AXIS));

		JButton b1 = new JButton(new ImageIcon("images/left.gif"));
		b1.setMargin(new Insets(0, 0, 0, 0));
		b1.setAlignmentY((float) 0.5);
		b1.setRolloverEnabled(true);
		b1.setRolloverIcon(new ImageIcon("images/leftRollover.gif"));
		b1.setActionCommand("previousMonth");
		b1.addActionListener(this);

		JButton b2 = new JButton(new ImageIcon("images/right.gif"));
		b2.setMargin(new Insets(0, 0, 0, 0));
		b2.setAlignmentY((float) 0.5);
		b2.setRolloverEnabled(true);
		b2.setRolloverIcon(new ImageIcon("images/rightRollover.gif"));
		b2.setActionCommand("nextMonth");
		b2.addActionListener(this);

		JPanel monthPanel = new JPanel();
		monthPanel.setPreferredSize(new Dimension(100, 50));
		monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.Y_AXIS));

		monthCombo = new JComboBox();
		monthCombo.setMaximumSize(new Dimension(100, 20));

		for (int i = 0; i < 12; i++)
		{
			monthCombo.addItem(Datum.MONTHNAMESLONG[i]);
		}

		monthCombo.setEditable(true);
		monthCombo.setEditor(new CenteredEditor());
		monthCombo.setSelectedIndex(mt);
		monthCombo.addItemListener(this);

		yearCombo = new JComboBox();
		yearCombo.setMaximumSize(new Dimension(60, 20));

		for (int i = yr - grenze[0]; i <= yr + grenze[1]; i++)
		{
			yearCombo.addItem(new Integer(i));
		}

		yearCombo.setEditable(true);
		yearCombo.setEditor(new CenteredEditor());
		yearCombo.setSelectedIndex(grenze[0]);
		yearCombo.addItemListener(this);

		monthPanel.add(Box.createHorizontalGlue());
		monthPanel.add(monthCombo);
		monthPanel.add(Box.createVerticalStrut(2));
		monthPanel.add(yearCombo);
		monthPanel.add(Box.createHorizontalGlue());

		selectMonth.add(Box.createHorizontalGlue());
		selectMonth.add(b1);
		selectMonth.add(Box.createHorizontalStrut(10));
		selectMonth.add(monthPanel);
		selectMonth.add(Box.createHorizontalStrut(10));
		selectMonth.add(b2);
		selectMonth.add(Box.createHorizontalGlue());

		return selectMonth;
	}

	private JPanel createDySelect(int mo, int yr)
	{
		String dayName = null;
//		Feiertag feiertag;
		Color fontColor;
		int index, dy;

		// Kalender
		day.set(yr, mo, 1);

		int maxDay = calcMonthDays(mo);
		int maxDayPrev = calcMonthDays(Math.max(mo - 1, 0));
		int weekDay = (day.get(Calendar.DAY_OF_WEEK) + 5) % 7;

		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());

		JPanel selectDay = new JPanel();
		selectDay.setLayout(new GridLayout(7, 7));
		selectDay.setBackground(TAG_BACKGRD);

		// Überschrift
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				if (i == 0)
				{
					JPanel heading = new JPanel();
					heading.setBackground(HEADING_BACKGRD);

					JLabel text = new JLabel(Datum.DAYNAMESSHORT[j]);
					text.setForeground(Color.black);
					text.setFont(new Font("Dialog", Font.BOLD, 12));
					text.setHorizontalAlignment(JLabel.CENTER);
					heading.add(text);
					selectDay.add(heading);

				}
				else
				{
					index = (i - 1) * 7 + j;
					if (index < weekDay)
					{
						dy = index + maxDayPrev - weekDay + 1;
						dayName = String.valueOf(dy);
						fontColor = Color.gray;

						if (mo == 0)
						{
							day.set(yr - 1, 11, dy);
						}
						else
						{
							day.set(yr, mo - 1, dy);
						}
					}
					else if (index >= weekDay + maxDay)
					{
						dy = index - maxDay - weekDay + 1;
						dayName = String.valueOf(dy);
						fontColor = Color.gray;

						if (mo == 11)
						{
							day.set(yr + 1, 0, dy);
						}
						else
						{
							day.set(yr, mo + 1, dy);
						}
					}
					else
					{
						dy = index - weekDay + 1;
						dayName = String.valueOf(dy);
						fontColor = Color.black;

						day.set(yr, mo, dy);
					}

					days[index] = new JButton(dayName);

// Feiertagsfarbe fuer Feiertage reparieren					
					if ( /* (feiertag = Data.feiertage.getFeiertagByDate(new Datum(day))) != null
							|| */ j == 6)
					{
						// Feiertag
						if (fontColor == Color.gray)
						{
							days[index].setForeground(mixColors(fontColor,
									FEIERTAG_FOREGRD));
						}
						else
						{
							days[index].setForeground(FEIERTAG_FOREGRD);
						}

//						if (feiertag != null)
//						{
//							days[index].setToolTipText(feiertag.getBezeichnung());
//						}

					}
					else
						days[index].setForeground(fontColor);

					if (compareDate(day, cal))
					{ // aktuelles Datum
						days[index].setBorder(new CompoundBorder(new BevelBorder(
								BevelBorder.RAISED), new LineBorder(Color.blue, 1)));
					}

					days[index].setMargin(new Insets(0, 0, 0, 0));
					days[index].getAccessibleContext().setAccessibleDescription(
							String.valueOf(day.get(Calendar.MONTH)));
					days[index].setActionCommand(String.valueOf(index));
					days[index].addActionListener(this);
					selectDay.add(days[index]);

					day.set(yr, mo, dy);
				}
			}
		}

		pane.add("West", selectDay);
		pane.add("Center", new JPanel());

		return pane;
	}

	private Color mixColors(Color c1, Color c2)
	{
		int rot = (c1.getRed() + c2.getRed()) / 2;
		int gruen = (c1.getGreen() + c2.getGreen()) / 2;
		int blau = (c1.getBlue() + c2.getBlue()) / 2;

		return new Color(rot, gruen, blau);
	}

	private boolean compareDate(GregorianCalendar c1, GregorianCalendar c2)
	{
		return c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
	}

	private int calcMonthDays(int month)
	{
		if ((month == 3) || (month == 5) || (month == 8) || (month == 10))
			return (30);
		else if (month == 1)
		{
			if (day.isLeapYear(day.get(Calendar.YEAR)))
				return (29);
			else
				return (28);
		}
		else
			return (31);
	}

	// ActionListener ///////////////////////////////////////////////////
	public void addActionListener(ActionListener listener)
	{
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener)
	{
		mActionListenerList.remove(ActionListener.class, listener);
	}

	protected void fireActionEvent(ActionEvent e)
	{
		Object[] listeners = mActionListenerList.getListenerList();
		ActionEvent newEvent = new ActionEvent(this, e.getID(), "changeDate");

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ActionListener.class)
			{
				((ActionListener) listeners[i + 1]).actionPerformed(newEvent);
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		int index, dy, mt, yr;

		mt = day.get(Calendar.MONTH);
		yr = day.get(Calendar.YEAR);

		if (e.getActionCommand().equals("previousMonth"))
		{
			if (mt==0 && yr>(cal.get(Calendar.YEAR)-grenze[0]))
			{
				mt = 11;
				yr--;
			}
			else
				mt--;

			monthCombo.setSelectedIndex(mt);
			yearCombo.setSelectedIndex(yr-cal.get(Calendar.YEAR)+1);

			remove(dyPanel);
			dyPanel = createDySelect(mt, yr);
			add("Center", dyPanel);
			validate();
		}
		else if (e.getActionCommand().equals("nextMonth"))
		{
			if (mt==11 && yr<(cal.get(Calendar.YEAR)+grenze[1]))
			{
				mt = 0;
				yr++;
			}
			else
				mt++;

			monthCombo.setSelectedIndex(mt);
			yearCombo.setSelectedIndex(yr-cal.get(Calendar.YEAR)+1);
			
			remove(dyPanel);
			dyPanel = createDySelect(mt, yr);
			add("Center", dyPanel);
			validate();
		}
		else	// day selected
		{
			index = Integer.valueOf(e.getActionCommand()).intValue();
			dy = Integer.valueOf(days[index].getText()).intValue();

			int mo = Integer.valueOf(
					days[index].getAccessibleContext().getAccessibleDescription())
					.intValue();
			if (mt != mo)	// Tag in einem Nachbarmonat angeklickt
			{
				if (index < 7) 	// Vorgaengermonat
				{
					if (mt==0 && yr>(cal.get(Calendar.YEAR)-grenze[0]))
					{
						mt = 11;
						yr--;
						yearCombo.setSelectedIndex(yearCombo.getSelectedIndex() - 1);
					}
					else
						mt--;						
				}
				else					// Nachfolgemonat
				{
					if (mt==11 && yr<(cal.get(Calendar.YEAR)+grenze[1]))
					{
						mt = 0;
						yr++;
						yearCombo.setSelectedIndex(yearCombo.getSelectedIndex() + 1);
					}
					else
						mt++;
				}

				monthCombo.setSelectedIndex(mt);

				remove(dyPanel);
				dyPanel = createDySelect(mt, yr);
				add("Center", dyPanel);
				validate();
			}

			day.set(yr, mt, dy);
			fireActionEvent(e);
		}
	}

	// ItemListener // ItemListener // ItemListener // ItemListener //
	// ItemListener //
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			int mt, yr;

			mt = day.get(Calendar.MONTH);
			yr = day.get(Calendar.YEAR);

			if (e.getSource() == monthCombo && monthCombo.getSelectedIndex()!=-1)
			{
				mt = monthCombo.getSelectedIndex();
			}
			else if (e.getSource() == yearCombo && yearCombo.getSelectedIndex()!=-1)
			{
				yr = ((Integer) yearCombo.getSelectedItem()).intValue();				
			}

			remove(dyPanel);
			dyPanel = createDySelect(mt, yr);
			add("Center", dyPanel);
			validate();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// CenteredEditor // CenteredEditor // CenteredEditor // CenteredEditor //
	// CenteredEditor //
	// //////////////////////////////////////////////////////////////////////////////////////////
	class CenteredEditor extends JTextField implements ComboBoxEditor
	{
		private static final long serialVersionUID = -2433171445811956116L;

		CenteredEditor()
		{
			setBorder(null);
			setEditable(false);
			setHorizontalAlignment(JTextField.CENTER);
		}

		public Component getEditorComponent()
		{
			return this;
		}

		public Object getItem()
		{
			return getText();
		}

		public void setItem(Object obj)
		{
			setText(obj.toString());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////
	// WaitForPaint // WaitForPaint // WaitForPaint // WaitForPaint //
	// WaitForPaint //
	// ////////////////////////////////////////////////////////////////////////////////
	class WaitForPaint implements Runnable
	{
		Thread t;

		WaitForPaint()
		{
			t = new Thread(this);
			t.start();
		}

		public void run()
		{
			while (!isShowing())
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception e)
				{}
			}

			monthCombo.invalidate();
			monthCombo.doLayout();

			yearCombo.invalidate();
			yearCombo.doLayout();
		}
	}
}
