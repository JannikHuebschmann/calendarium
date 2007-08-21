package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import data.*;
import basisklassen.*;
import client.Client;

///////////////////////////////////////////////////////////////////////////////////////
// Jahresansicht // Jahresansicht // Jahresansicht // Jahresansicht // Jahresansicht //
///////////////////////////////////////////////////////////////////////////////////////

public class Jahresansicht extends Ansicht implements data.Shared {
	// Feiertage
	private Hashtable feiertage = Data.feiertage.getAllFeiertage();

	// MonthPane
	private MonthPane[] monthPane = new MonthPane[12];

	// Personen mit Terminen
	private Vector[] psnListe;

	// Jahr
	private int jahr;

	private int weekDayBgn;

	public Jahresansicht(JFrame frame, JLabel status) {
		super(frame, status);
		gui.setTitle("Jahresansicht");

		gui.setSize(650, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		Calendar now = Calendar.getInstance();
		jahr = now.get(Calendar.YEAR);

		// IntervallGrenzen
		getGrenzen();

		// Daten
		psnListe = Data.termine.getPersonsWithTermin(bgnAnsicht, endAnsicht,
				openCal.getPersonenListe());

		create();
	}

	private void create() {
		gui.getContentPane().add("North", createSelectPane());
		gui.getContentPane().add("Center", createYearPane());
	}

	private void getGrenzen() {
		int weekDayEnd;

		bgnAnsicht = new Datum("01" + DATETOKEN + "01" + DATETOKEN + jahr);
		endAnsicht = new Datum("01" + DATETOKEN + "12" + DATETOKEN + jahr);

		weekDayBgn = bgnAnsicht.getWeekDay();
		weekDayEnd = endAnsicht.getWeekDay();

		if (weekDayBgn > 0) {
			bgnAnsicht.setDatum((32 - weekDayBgn) + DATETOKEN + "12"
					+ DATETOKEN + (jahr - 1));
		}

		endAnsicht.setDatum((11 - weekDayEnd) + DATETOKEN + "01" + DATETOKEN
				+ (jahr + 1));
	}

	JPanel createKalender() {
		selKalender = new KalenderAuswahl(this, false);
		selKalender.addListDataListener(this);

		selKalender.getGUI().setMaximumSize(new Dimension(300, 100));
		selKalender.getGUI().setPreferredSize(new Dimension(300, 100));

		return selKalender.getGUI();
	}

	private JPanel createSelectPane() {
		// selectPane
		JPanel selectPane = new JPanel();
		selectPane.setLayout(new BoxLayout(selectPane, BoxLayout.X_AXIS));

		selectPane.add(Box.createHorizontalStrut(20));
		selectPane.add(createSelectDate(String.valueOf(jahr), false));
		selectPane.add(Box.createHorizontalStrut(20));
		selectPane.add(createKalender());

		setDateLabelSize(new Dimension(50, 50));
		return selectPane;
	}

	private JPanel createYearPane() {
		JPanel yearPane = new JPanel();
		yearPane.setLayout(new GridLayout(3, 4, 5, 5));
		yearPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		for (int i = 0; i < 12; i++) {
			JPanel pane = new JPanel();
			pane.setLayout(new BorderLayout());

			// Überschrift
			JLabel label = new JLabel(MONTHNAMESLONG[i]);
			label.setHorizontalAlignment(JLabel.CENTER);

			pane.add("North", label);
			pane.add("Center", monthPane[i] = new MonthPane(i + 1, jahr));

			yearPane.add(pane);
		}

		return yearPane;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("leftArrow")) {
			jahr--;
			getGrenzen();
		} else if (e.getActionCommand().equals("rightArrow")) {
			jahr++;
			getGrenzen();
		}

		updateEinträge();
		setDateLabel(String.valueOf(jahr));
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void intervalAdded(ListDataEvent e) {
		updateEinträge();
	}

	public void intervalRemoved(ListDataEvent e) {
		updateEinträge();
	}

	// update
	private void updateEinträge() {
		// Daten
		psnListe = Data.termine.getPersonsWithTermin(bgnAnsicht, endAnsicht,
				openCal.getPersonenListe());

		for (int i = 0; i < 12; i++) {
			JPanel pane = (JPanel) monthPane[i].getParent();
			pane.remove(monthPane[i]);

			pane.add("Center", monthPane[i] = new MonthPane(i + 1, jahr));
			pane.validate();
		}
	}

	int getPsnListeIndex(int[] day) {
		int dy = day[0];
		int mt = day[1];
		int yr = day[2];

		if (yr >= jahr) {
			int index = weekDayBgn + dy;

			if (yr > jahr) {
				mt = 13;
				yr = jahr;
			}

			for (int i = 1; i < mt; i++) {
				index += Datum.calcMonthDays(i, yr);
			}

			return index - 1;

		} else
			return dy + weekDayBgn - 32;
	}

	// Overloaded
	public void updateTermine(Termin termin) {
		Vector[] pListe = Data.termine.getPersonsWithTermin(termin.getBeginn(),
				termin.getEnde(), openCal.getPersonenListe());
		Datum bgn = new Datum();
		bgn.setDatum(termin.getBeginn());

		for (int i = 0, index = 0; i < pListe.length; i++) {
			int dy = bgn.getDay();
			int mt = bgn.getMonth();
			int yr = bgn.getYear();

			if (index == 0)
				index = getPsnListeIndex(new int[] { dy, mt, yr });
			else
				index++;

			// Daten
			psnListe[index] = pListe[i];

			if (yr > jahr) {
				monthPane[11].updateDay(bgn);
			} else if (yr < jahr) {
				monthPane[0].updateDay(bgn);
			} else {
				monthPane[mt - 1].updateDay(bgn);

				// davor
				if (mt > 1)
					monthPane[mt - 2].updateDay(bgn);

				// danach
				if (mt < 12)
					monthPane[mt].updateDay(bgn);
			}

			bgn.add(1);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// MonthPane // MonthPane // MonthPane // MonthPane // MonthPane //
	// MonthPane //
	// //////////////////////////////////////////////////////////////////////////////
	class MonthPane extends JPanel {
		private static final long serialVersionUID = 2706015702227029658L;

		private int yr, mt;

		private Hashtable dayPanes = new Hashtable();

		MonthPane(int mt, int yr) {
			this.mt = mt;
			this.yr = yr;

			setLayout(new GridLayout(6, 7));

			create();
		}

		@SuppressWarnings("unchecked")
		private void create() {
			int maxDay = Datum.calcMonthDays(mt, yr);
			int weekDay = Datum.getWeekDay(1, mt, yr);

			if (mt == 1) {
				mt = 12;
				yr--;

			} else
				mt--;

			int maxDayPrev = Datum.calcMonthDays(mt, yr);

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					int index = i * 7 + j;
					int dy;

					Color fontColor = Color.gray;

					if (index < weekDay) {
						dy = index + maxDayPrev - weekDay + 1;

					} else {
						if (index == weekDay + maxDay || index == weekDay) {
							if (mt == 12) {
								mt = 1;
								yr++;

							} else
								mt++;
						}

						if (index >= weekDay + maxDay) {
							dy = index - maxDay - weekDay + 1;

						} else {
							fontColor = Color.black;
							dy = index - weekDay + 1;
						}
					}

					Feiertag feiertag;
					String dateString = (dy < 10 ? "0" + dy : "" + dy)
							+ DATETOKEN + (mt < 10 ? "0" + mt : "" + mt)
							+ DATETOKEN + yr;

					if ((feiertag = (Feiertag) feiertage.get(dateString)) != null
							|| j == 6) {
						// Feiertag
						if (fontColor == Color.gray)
							fontColor = mixColors(fontColor, FEIERTAG_FOREGRD);
						else
							fontColor = FEIERTAG_FOREGRD;
					}

					DayPane dayPane = new DayPane(new int[] { dy, mt, yr },
							fontColor);
					dayPanes.put(dateString, dayPane);

					if (feiertag != null)
						dayPane.setToolTipText(feiertag.getBezeichnung());

					add(dayPane);
				}
			}
		}

		private Color mixColors(Color c1, Color c2) {
			int rot = (c1.getRed() + c2.getRed()) / 2;
			int gruen = (c1.getGreen() + c2.getGreen()) / 2;
			int blau = (c1.getBlue() + c2.getBlue()) / 2;

			return new Color(rot, gruen, blau);
		}

		void updateDay(Datum day) {
			if (dayPanes.containsKey(day.getDate())) {
				DayPane dayPane = (DayPane) dayPanes.get(day.getDate());
				dayPane.updateDay();
			}
		}

		class DayPane extends JPanel {
			private static final long serialVersionUID = -6632775278580728714L;

			private int[] day;

			private Color labelColor;

			DayPane(int[] day, Color labelColor) {
				this.day = day;
				this.labelColor = labelColor;

				// MouseListener
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if (evt.getClickCount() >= 1) {
							// Tagesansicht
							Tagesansicht tagesansicht = new Tagesansicht(
									parentFrame, statusLabel, getDatum(),
									openCal.getPersonenListe());
							// Listener
							((Client) parentFrame).getListener().addObserver(
									tagesansicht);

							JInternalFrame gui = tagesansicht.getGUI();

							JLayeredPane layer = ((Client) parentFrame)
									.getLayeredPane();
							try {
								gui.setSelected(true);
							} catch (java.beans.PropertyVetoException ex) {
							}

							layer.add(gui, 0);
						}
					}
				});
			}

			Datum getDatum() {
				return new Datum((day[0] < 10 ? "0" + day[0] : "" + day[0])
						+ DATETOKEN
						+ (day[1] < 10 ? "0" + day[1] : "" + day[1])
						+ DATETOKEN + day[2]);
			}

			private void paintBoxes(Graphics g) {
				int index = getPsnListeIndex(day);
				int count = psnListe[index].size();

				if (count > 0) {
					int h = getSize().height;
					int w = getSize().width / count;

					Color colorOld = g.getColor();
					Enumeration e = psnListe[index].elements();

					for (int i = 0; i < count; i++) {
						Person person = (Person) e.nextElement();
						Color farbCode = PERSFARBEN[openCal
								.getColorIndex(person)];

						g.setColor(farbCode);
						g.fillRect(i * w, 0, (i + 1) * w, h);
					}

					g.setColor(colorOld);
				}
			}

			private void paintText(Graphics g) {
				Dimension d = getSize();
				FontMetrics fm = g.getFontMetrics();
				String dateString = String.valueOf(day[0]);

				int x = (d.width - fm.stringWidth(dateString)) / 2;
				int y = (d.height + fm.getMaxAscent() - fm.getMaxDescent()) / 2;

				g.setColor(labelColor);
				g.drawString(dateString, x, y);
			}

			private void paintRaisedBevel(Graphics g) {
				int h = getSize().height;
				int w = getSize().width;

				g.setColor(getBackground().brighter().brighter());
				g.drawLine(0, 0, 0, h - 1);
				g.drawLine(1, 0, w - 1, 0);

				g.setColor(getBackground().brighter());
				g.drawLine(1, 1, 1, h - 2);
				g.drawLine(2, 1, w - 2, 1);

				g.setColor(getBackground().darker().darker());
				g.drawLine(1, h - 1, w - 1, h - 1);
				g.drawLine(w - 1, 1, w - 1, h - 2);

				g.setColor(getBackground().darker());
				g.drawLine(2, h - 2, w - 2, h - 2);
				g.drawLine(w - 2, 2, w - 2, h - 3);
			}

			public void paint(Graphics g) {
				paintBoxes(g);
				paintText(g);
				paintRaisedBevel(g);
				;
			}

			void updateDay() {
				paint(getGraphics());
			}
		}
	}
}
