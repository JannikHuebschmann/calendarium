package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import swtkal.swing.elements.Datumsfeld;
import swtkal.swing.elements.MonatsObjekt;

/////////////////////////////////////////////////////////////////////////////////////////////
// DatePanel // DatePanel // DatePanel // DatePanel // DatePanel // DatePanel // DatePanel //
/////////////////////////////////////////////////////////////////////////////////////////////

class DatePanel implements ActionListener
{
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	private JWindow mtWindow;

	private MonatsObjekt mtPanel;

	private Datumsfeld dateField;

	private JButton calendar;

	DatePanel(JFrame f)
	{
		parentFrame = f;

		gui.setLayout(new BoxLayout(gui, BoxLayout.X_AXIS));
		gui.setBorder(new EmptyBorder(0, 5, 0, 5));
		create();
	}

	void create()
	{
		JLabel l1 = new JLabel("Datum:");
		l1.setPreferredSize(new Dimension(50, 20));
		l1.setAlignmentY((float) 0.5);
		l1.setDisplayedMnemonic('d');
		dateField = new Datumsfeld();
		dateField.setPreferredSize(new Dimension(90, 23));
		dateField.setMaximumSize(new Dimension(500, 23));

		ImageIcon calGif = new ImageIcon("images/calendar.gif");
		calendar = new JButton(calGif);
		calendar.setPreferredSize(new Dimension(20, 20));
		calendar.setMargin(new Insets(0, 0, 0, 0));
		calendar.setAlignmentY((float) 0.5);
		calendar.addActionListener(this);

		gui.add(l1);
		gui.add(dateField);
		gui.add(Box.createRigidArea(new Dimension(8, 23)));
		gui.add(calendar);

	}

	JPanel getGUI()
	{
		return gui;
	}

	void closeCalendar()
	{
		if (mtWindow != null)
			mtWindow.dispose();
	}

	Datumsfeld getDatumsfeld()
	{
		return dateField;
	}

	JButton getCalendarButton()
	{
		return calendar;
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() != mtPanel)
		{
			if (mtPanel == null)
			{
// TODO MtPaneYears ordentlich aus Client-Properties einlesen!				
//				String intervall = ((Client) parentFrame)
//						.getProperty("MtPaneYears");
String intervall ="-1 .. +5";
				mtPanel = new MonatsObjekt(new Date(), intervall);
				mtPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
				mtPanel.addActionListener(this);

				mtWindow = new JWindow(parentFrame);
				mtWindow.setSize(190, 208);
				mtWindow.getContentPane().setLayout(new BorderLayout());
				mtWindow.getContentPane().add("Center", mtPanel);

				// Location
				mtWindow.setLocation(dateField.getLocationOnScreen().x, dateField
						.getLocationOnScreen().y
						+ dateField.getSize().height);
				mtWindow.show();

			}
			else
			{
				if (!mtWindow.isVisible())
				{ // Location
					mtWindow.setLocation(dateField.getLocationOnScreen().x,
							dateField.getLocationOnScreen().y
									+ dateField.getSize().height);
					mtWindow.show();

				}
				else
					mtWindow.setVisible(false);
			}
		}
		else
		{
			dateField.setDate(mtPanel.getDate());
			mtWindow.setVisible(false);
		}
	}
}
