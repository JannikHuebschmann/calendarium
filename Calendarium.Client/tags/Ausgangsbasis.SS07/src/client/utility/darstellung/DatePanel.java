package client.utility.darstellung; //
//////////////////////////////////////

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import client.Client;
import client.gui.Datumsfeld;

/////////////////////////////////////////////////////////////////////////////////////////////
// DatePanel // DatePanel // DatePanel // DatePanel // DatePanel // DatePanel // DatePanel //
/////////////////////////////////////////////////////////////////////////////////////////////

public class DatePanel implements ActionListener {
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	private JWindow mtWindow;

	private MonatsObjekt mtPanel;

	private Datumsfeld dateField;

	private JButton calendar;

	public DatePanel(JFrame f) {
		parentFrame = f;

		gui.setLayout(new BoxLayout(gui, BoxLayout.X_AXIS));
		gui.setBorder(new EmptyBorder(0, 5, 0, 5));

		create(50);
	}

	public DatePanel(JFrame f, int lWidth) {
		parentFrame = f;

		gui.setLayout(new BoxLayout(gui, BoxLayout.X_AXIS));
		gui.setBorder(new EmptyBorder(0, 5, 0, 5));

		create(lWidth);
	}

	void create(int lWidth) {
		JLabel l1 = new JLabel("Datum:");
		l1.setPreferredSize(new Dimension(lWidth, 20));
		l1.setAlignmentY((float) 0.5);
		l1.setDisplayedMnemonic('d');

		dateField = new Datumsfeld();
		dateField.setPreferredSize(new Dimension(90, 23));
		dateField.setMaximumSize(new Dimension(500, 23));

		ImageIcon calGif = client.Client.loadImageIcon("calendar.gif");
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

	public JPanel getGUI() {
		return gui;
	}

	public void closeCalendar() {
		if (mtWindow != null)
			mtWindow.dispose();
	}

	public Datumsfeld getDatumsfeld() {
		return dateField;
	}

	JButton getCalendarButton() {
		return calendar;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != mtPanel) {
			if (mtPanel == null) {
				String intervall = ((Client) parentFrame)
						.getProperty("MtPaneYears");
				// String intervall = ((Monitor)
				// parentFrame).getProperty("MtPaneYears");//original
				mtPanel = new MonatsObjekt(new Date(), intervall);
				mtPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
				mtPanel.addActionListener(this);

				mtWindow = new JWindow(parentFrame);
				mtWindow.setSize(190, 208);
				mtWindow.getContentPane().setLayout(new BorderLayout());
				mtWindow.getContentPane().add("Center", mtPanel);

				// Location
				mtWindow.setLocation(dateField.getLocationOnScreen().x,
						dateField.getLocationOnScreen().y
								+ dateField.getSize().height);
				mtWindow.show();

			} else {
				if (!mtWindow.isVisible()) { // Location
					mtWindow.setLocation(dateField.getLocationOnScreen().x,
							dateField.getLocationOnScreen().y
									+ dateField.getSize().height);
					mtWindow.show();

				} else
					mtWindow.setVisible(false);
			}
		} else {
			dateField.setDate(mtPanel.getDate());
			mtWindow.setVisible(false);
		}
	}
}
