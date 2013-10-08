package swtkal.swing.client;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

//import event.*;
//import data.Data;
//import data.Shared;
import swtkal.domain.*;
//import message.MessageFrame;
//import client.eintrag.*;
import swtkal.exceptions.TerminException;

///////////////////////////////////////////////////////////////////////////////
// Ansicht // Ansicht // Ansicht // Ansicht // Ansicht // Ansicht // Ansicht //
///////////////////////////////////////////////////////////////////////////////

public class Ansicht implements ActionListener, MouseListener,
		ListDataListener, Observer //, EventConstants, Shared
{
	JLabel statusLabel;
	JFrame parentFrame;
	SwingClient client;

	// graphical Representation
	public JInternalFrame gui = new JInternalFrame("", true, true, false, true);

	// Datum
	JLabel dateLabel[] = new JLabel[2];

	Datum bgnAnsicht;

	Datum endAnsicht;

//	// Offene Kaleder
//	OffeneKalender openCal = new OffeneKalender();
//
//	// EinträgeVon
//	KalenderAuswahl selKalender;

	public Ansicht(JFrame f, JLabel s, SwingClient c)
	{
		parentFrame = f;
		statusLabel = s;
		client = c;

//		openCal.add(Data.user, 0);
	}

//	JPanel createKalender()
//	{
//		selKalender = new KalenderAuswahl(this, true);
//		selKalender.addListDataListener(this);
//		return selKalender.getGUI();
//	}

	JPanel createSelectDate(String dateText, boolean isfeiertag)
	{
		return createSelectDate(dateText, "", isfeiertag, null);
	}

	JPanel createSelectDate(String dateText1, String dateText2,
			boolean isfeiertag, Feiertag feiertag)
	{
		JPanel selectDate = new JPanel();
		selectDate.setLayout(new BoxLayout(selectDate, BoxLayout.X_AXIS));

		JButton b1 = new JButton(new ImageIcon("images/left.gif"));
		b1.setMargin(new Insets(0, 0, 0, 0));
		b1.setAlignmentY((float) 0.5);
		b1.setRolloverEnabled(true);
		b1.setRolloverIcon(new ImageIcon("images/leftRollover.gif"));
		b1.setActionCommand("leftArrow");
		b1.addActionListener(this);

		JButton b2 = new JButton(new ImageIcon("images/right.gif"));
		b2.setMargin(new Insets(0, 0, 0, 0));
		b2.setAlignmentY((float) 0.5);
		b2.setRolloverEnabled(true);
		b2.setRolloverIcon(new ImageIcon("images/rightRollover.gif"));
		b2.setActionCommand("rightArrow");
		b2.addActionListener(this);

		dateLabel[0] = new JLabel(dateText1);

		dateLabel[0].setFont(new Font("Dialog", Font.BOLD, 16));
		dateLabel[0].setHorizontalAlignment(JLabel.CENTER);

		selectDate.add(Box.createHorizontalGlue());
		selectDate.add(b1);
		selectDate.add(Box.createHorizontalStrut(10));

//		if (dateText2.length() > 0)
//		{
//			dateLabel[0].setVerticalAlignment(JLabel.BOTTOM);
//
//			JPanel pane = new JPanel();
//			pane.setLayout(new GridLayout(2, 1));
//
//			dateLabel[1] = new JLabel(dateText2);
//			dateLabel[1].setFont(new Font("Dialog", Font.BOLD, 16));
//			dateLabel[1].setHorizontalAlignment(JLabel.CENTER);
//			dateLabel[1].setVerticalAlignment(JLabel.TOP);
//
//			if (isfeiertag)
//			{
//				dateLabel[0].setForeground(FEIERTAG_FOREGRD);
//				dateLabel[1].setForeground(FEIERTAG_FOREGRD);
//				if (feiertag != null)
//				{
//					dateLabel[0].setToolTipText(feiertag.getBezeichnung());
//					dateLabel[1].setToolTipText(feiertag.getBezeichnung());
//				}
//			}
//			else
//			{
//				dateLabel[0].setForeground(Color.black);
//				dateLabel[1].setForeground(Color.black);
//			}
//
//			pane.add(dateLabel[0]);
//			pane.add(dateLabel[1]);
//
//			selectDate.add(pane);
//
//		}
//		else
//		{
			dateLabel[0].setVerticalAlignment(JLabel.CENTER);
			selectDate.add(dateLabel[0]);
//		}

		selectDate.add(Box.createHorizontalStrut(10));
		selectDate.add(b2);
		selectDate.add(Box.createHorizontalGlue());

		return selectDate;
	}

	void setDateLabel(String dateText)
	{
		dateLabel[0].setText(dateText);
		dateLabel[0].repaint();
	}

//	void setDateLabel(String dateText1, String dateText2, boolean isfeiertag,
//			Feiertag feiertag)
//	{
//		dateLabel[0].setText(dateText1);
//		dateLabel[0].repaint();
//
//		dateLabel[1].setText(dateText2);
//		dateLabel[1].repaint();
//		if (isfeiertag)
//		{
//			dateLabel[0].setForeground(FEIERTAG_FOREGRD);
//			dateLabel[1].setForeground(FEIERTAG_FOREGRD);
//			if (feiertag != null)
//			{
//				dateLabel[0].setToolTipText(feiertag.getBezeichnung());
//				dateLabel[1].setToolTipText(feiertag.getBezeichnung());
//			}
//		}
//		else
//		{
//			dateLabel[0].setForeground(Color.black);
//			dateLabel[1].setForeground(Color.black);
//		}
//	}

	void setDateLabelSize(Dimension x)
	{
//		dateLabel[0].setPreferredSize(x);
//		if (dateLabel[1] != null)
//			dateLabel[1].setPreferredSize(x);
	}

	public JInternalFrame getGUI()
	{
		return gui;
	}

	@SuppressWarnings("deprecation")
	private void showDialog(String title, String text)
	{
		JTextArea area = new JTextArea(text);

		area.setBackground(SystemColor.control);
		area.setEditable(false);
		area.setColumns(text.length());
		area.setRows(1);

		JOptionPane optionPane = new JOptionPane(area,
				JOptionPane.INFORMATION_MESSAGE);
		JDialog dialogPane = optionPane.createDialog(parentFrame, title);

		dialogPane.show();
		dialogPane.dispose();
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	public void actionPerformed(ActionEvent e)
	{
		String c = e.getActionCommand();
		JLayeredPane layer = ((JFrame) parentFrame).getLayeredPane();

		if (e.getSource() instanceof TerminObjekt)
		{
			TerminObjekt obj = (TerminObjekt) e.getSource();
			Termin termin = (Termin) obj.getEintrag();

			if (c.indexOf("eintragen") >= 0)
			{
				// neuen Termin eintragen
				EditTerminControl editTermin = new EditTerminControl(
						parentFrame, client, termin.getBeginn());
				JInternalFrame gui = editTermin.getGUI();

				try
				{
					gui.setSelected(true);
				}
				catch (java.beans.PropertyVetoException ex)
				{}

				layer.add(gui, 0);
				gui.setVisible(true);
			}
			else if (c.indexOf("bearbeiten") >= 0)
			{
				// geklickten Termin bearbeiten
//				if (obj.isLeseBerechtigt())
//				{
					EditTerminControl editTermin = new EditTerminControl(
							parentFrame, client, termin);
					JInternalFrame gui = editTermin.getGUI();

					try
					{
						gui.setSelected(true);
					}
					catch (java.beans.PropertyVetoException ex)
					{}

					layer.add(gui, 0);
					gui.setVisible(true);
//				}
//				else
//				{
//					showDialog("Termin anzeigen",
//							"Sie besitzen keine Leseberechtigung fuer diesen Termin!");
//				}
			}
			else if (c.indexOf("löschen") >= 0)
			{
				// geklickten Termin löschen
//				if (termin.getOwner().getID() == Data.user.getID())
//				{
					try
					{
						client.getServer().delete(termin);
						// Aktualisieren der Tagesansicht anstoßen
						client.actionPerformed(new ActionEvent(this, 0, "Tagesansicht"));
					}
					catch (TerminException te)
					{
						showDialog("Termin löschen",te.getMessage());
					}
//				}
//				else
//				{
//					showDialog("Termin löschen",
//							"Sie sind nicht berechtigt, diesen Termin zu löschen!");
//				}
			}
//			else if (c.indexOf("Nachricht schicken") >= 0)
//			{
//				MessageFrame frame = new MessageFrame(parentFrame,
//						(Eintrag) termin, statusLabel);
//
//				JInternalFrame gui = frame.getGUI();
//
//				try
//				{
//					gui.setSelected(true);
//				}
//				catch (java.beans.PropertyVetoException ex)
//				{}
//				layer.add(gui, 0);
//				gui.setVisible(true);
//			}
//
//			else if (c.indexOf("Hyperlink verfolgen") >= 0)
//			{
//				// Hyperlink verfolgen - changed by daniela esberger (13. Juni
//				// 2005)
//				// Browser-Setting in userProperties bei Windows nicht mehr
//				// noetig
//
//				if (!System.getProperty("os.name").startsWith("Windows"))
//				{
//
//					// Hyperlink verfolgen
//					String browser = ((SwingClient) parentFrame)
//							.getProperty("BrowserPath");
//
//					Runtime r = Runtime.getRuntime();
//					@SuppressWarnings("unused")
//					Process p = null;
//
//					if (browser.length() > 0)
//					{
//						try
//						{
//							p = r.exec(browser + " " + termin.getHyperlink());
//
//						}
//						catch (Exception ex)
//						{
//							ex.printStackTrace();
//						}
//
//					}
//					else
//					{
//						showDialog(
//								"Fehlender Property-Eintrag",
//								"Bitte tragen Sie in den UserProperties den vollständigen Pfad fuer Ihren Browser ein!");
//					}
//
//				}
//				// if WINDOWS
//				else
//				{
//
//					Runtime r = Runtime.getRuntime();
//					@SuppressWarnings("unused")
//					Process p = null;
//
//					try
//					{
//						p = r.exec("rundll32 url.dll,FileProtocolHandler "
//								+ termin.getHyperlink());
//
//					}
//					catch (Exception ex)
//					{
//						ex.printStackTrace();
//					}
//
//				}
//
//				/*
//				 * Hyperlink hyperlink = new Hyperlink(termin.getHyperlink());
//				 * JInternalFrame gui = hyperlink.getGUI();
//				 * 
//				 * try { gui.setSelected(true); }
//				 * catch(java.beans.PropertyVetoException ex) {}
//				 * 
//				 * layer.add(gui, 0);
//				 */
//			}
//		}
//
//		else if (e.getSource() instanceof ToDoObjekt)
//		{
//			ToDoObjekt obj = (ToDoObjekt) e.getSource();
//			ToDo toDo = (ToDo) obj.getEintrag();
//
//			if (c.indexOf("eintragen") >= 0)
//			{
//				// ToDo eintragen
//				EditToDoControl editToDo = new EditToDoControl(parentFrame);
//				JInternalFrame gui = editToDo.getGUI();
//
//				try
//				{
//					gui.setSelected(true);
//				}
//				catch (java.beans.PropertyVetoException ex)
//				{}
//
//				layer.add(gui, 0);
//				gui.setVisible(true);// added 17.05.04
//			}
//
//			else if (c.indexOf("bearbeiten") >= 0)
//			{
//
//				if (obj.isLeseBerechtigt())
//				{
//					// ToDo bearbeiten
//					EditToDoControl editToDo = new EditToDoControl(parentFrame, toDo);
//					JInternalFrame gui = editToDo.getGUI();
//
//					try
//					{
//						gui.setSelected(true);
//					}
//					catch (java.beans.PropertyVetoException ex)
//					{}
//
//					layer.add(gui, 0);
//					gui.setVisible(true);// added 17.05.04
//
//				}
//				else
//				{
//					showDialog("ToDo anzeigen",
//							"Sie besitzen keine Leseberechtigung fuer diesen ToDo-Eintrag!");
//				}
//			}// else if
//
//			else if (c.indexOf("löschen") >= 0)
//			{
//				// ToDo löschen
//				if (toDo.getOwner().getID() == Data.user.getID())
//				{
//					Data.toDo.delete(toDo);
//
//				}
//				else
//				{
//					showDialog("ToDo löschen",
//							"Sie sind nicht berechtigt, diesen ToDo-Eintrag zu löschen!");
//				}
//			}
//			else if (c.indexOf("Nachricht schicken") >= 0)
//			{
//				MessageFrame frame = new MessageFrame(parentFrame, (Eintrag) toDo,
//						statusLabel);
//
//				JInternalFrame gui = frame.getGUI();
//
//				try
//				{
//					gui.setSelected(true);
//				}
//				catch (java.beans.PropertyVetoException ex)
//				{}
//				layer.add(gui, 0);
//				gui.setVisible(true);
//			}
//
//			else if (c.indexOf("Hyperlink verfolgen") >= 0)
//			{
//				// Hyperlink verfolgen - changed by daniela esberger (13. Juni
//				// 2005)
//				// Browser-Setting in userProperties bei Windows nicht mehr
//				// noetig
//
//				if (!System.getProperty("os.name").startsWith("Windows"))
//				{
//
//					// Hyperlink verfolgen
//					String browser = ((SwingClient) parentFrame)
//							.getProperty("BrowserPath");
//
//					Runtime r = Runtime.getRuntime();
//					@SuppressWarnings("unused")
//					Process p = null;
//
//					if (browser.length() > 0)
//					{
//						try
//						{
//							p = r.exec(browser + " " + toDo.getHyperlink());
//
//						}
//						catch (Exception ex)
//						{
//							ex.printStackTrace();
//						}
//
//					}
//					else
//					{
//						showDialog(
//								"Fehlender Property-Eintrag",
//								"Bitte tragen Sie in den UserProperties den vollständigen Pfad fuer Ihren Browser ein!");
//					}
//
//				}
//
//				// if WINDOWS
//
//				else
//				{
//
//					Runtime r = Runtime.getRuntime();
//					@SuppressWarnings("unused")
//					Process p = null;
//
//					try
//					{
//						p = r.exec("rundll32 url.dll,FileProtocolHandler "
//								+ toDo.getHyperlink());
//
//					}
//					catch (Exception ex)
//					{
//						ex.printStackTrace();
//					}
//
//				}
//
//				/*
//				 * Hyperlink hyperlink = new Hyperlink(toDo.getHyperlink());
//				 * JInternalFrame gui = hyperlink.getGUI();
//				 * 
//				 * try { gui.setSelected(true); }
//				 * catch(java.beans.PropertyVetoException ex) {}
//				 * 
//				 * layer.add(gui, 0);
//				 */
//			}
		}
		else if (e.getSource() instanceof LeerObjekt)
		{
			Datum date = ((LeerObjekt) e.getSource()).getDate();

			if (c.indexOf("Termin") >= 0)
			{
				// neuen Termin eintragen
				EditTerminControl editTermin = new EditTerminControl(
						parentFrame, client, date);
				JInternalFrame gui = editTermin.getGUI();

				try
				{
					gui.setSelected(true);
				}
				catch (java.beans.PropertyVetoException ex)
				{}

				layer.add(gui, 0);
				gui.setVisible(true);
			}
		}
	}

	// ListDataListener // ListDataListener // ListDataListener //
	// ListDataListener //
	public void contentsChanged(ListDataEvent e)
	{}

	public void intervalAdded(ListDataEvent e)
	{}

	public void intervalRemoved(ListDataEvent e)
	{}

	// MouseListener // MouseListener // MouseListener // MouseListener //
	// MouseListener //
	public void mouseEntered(MouseEvent evt)
	{
//		if (evt.getSource() instanceof EintragsObjekt)
//		{
//			EintragsObjekt obj = (EintragsObjekt) evt.getSource();
//
//			if (obj.isLeseBerechtigt())
//			{
//				statusLabel.setText(obj.getEintrag().getTyp().getBezeichnung());
//			}
//		}
	}

	public void mouseExited(MouseEvent evt)
	{
//		statusLabel.setText("");
	}

	public void mouseClicked(MouseEvent evt)
	{
		if (evt.getClickCount() > 1)
		{
//			if (evt.getSource() instanceof TerminObjekt)
//			{
//				TerminObjekt obj = (TerminObjekt) evt.getSource();
//				Termin termin = (Termin) obj.getEintrag();
//
//				if (obj.isLeseBerechtigt())
//				{
//					// Termin bearbeiten
//					EditTerminControl editTermin = new EditTerminControl(
//							parentFrame, termin);
//					JInternalFrame gui = editTermin.getGUI();
//
//					JLayeredPane layer = ((SwingClient) parentFrame).getLayeredPane();
//					try
//					{
//						gui.setSelected(true);
//					}
//					catch (java.beans.PropertyVetoException ex)
//					{}
//
//					layer.add(gui, 0);
//					gui.setVisible(true);// added 18.05.04
//
//				}
//				else
//				{
//					showDialog("Termin anzeigen",
//							"Sie besitzen keine Leseberechtigung fuer diesen Termin!");
//				}
//			}
//
//			else if (evt.getSource() instanceof ToDoObjekt)
//			{
//				ToDoObjekt obj = (ToDoObjekt) evt.getSource();
//				ToDo toDo = (ToDo) obj.getEintrag();
//
//				if (obj.isLeseBerechtigt())
//				{
//					// ToDo bearbeiten
//					EditToDoControl editToDo = new EditToDoControl(parentFrame, toDo);
//					JInternalFrame gui = editToDo.getGUI();
//
//					JLayeredPane layer = ((SwingClient) parentFrame).getLayeredPane();
//					try
//					{
//						gui.setSelected(true);
//					}
//					catch (java.beans.PropertyVetoException ex)
//					{}
//
//					layer.add(gui, 0);
//					gui.setVisible(true);// added 18.05.04
//				}
//				else
//				{
//					showDialog("ToDo anzeigen",
//							"Sie besitzen keine Leseberechtigung fuer diesen ToDo-Eintrag!");
//				}
//			}
//
//			else if (evt.getSource() instanceof LeerObjekt)
//			{
//				Datum date = ((LeerObjekt) evt.getSource()).getDate();
//				JInternalFrame gui;
//
//				if (date.isCorrectTime())
//				{
//					// Neuen Termin erstellen
//					EditTerminControl editTermin = new EditTerminControl(
//							parentFrame, date);
//					gui = editTermin.getGUI();
//
//				}
//				else
//				{
//					// Neuen ToDo-Eintrag erstellen
//					EditToDoControl editToDo = new EditToDoControl(parentFrame, date);
//					gui = editToDo.getGUI();
//				}
//
//				JLayeredPane layer = ((SwingClient) parentFrame).getLayeredPane();
//
//				try
//				{
//					gui.setSelected(true);
//				}
//				catch (java.beans.PropertyVetoException ex)
//				{}
//
//				layer.add(gui, 0);
//				gui.setVisible(true);// added 18.05.04
//			}// leerobjekt
		}
	}

	public void mousePressed(MouseEvent evt)
	{}

	public void mouseReleased(MouseEvent evt)
	{}

	// Observer
	public void update(Observable obj, Object arg)
	{
//		CalendariumEvent evt = (CalendariumEvent) arg;
//
//		if (evt.getEventID() == TERMIN_EVT) // Update Termin
//		{
//			Termin termin = ((TerminEvent) evt).getTermin();
//
//			if (termin.getBeginn().isGreater(endAnsicht) <= 0
//					&& termin.getEnde().isGreater(bgnAnsicht) >= 0
//					&& termin.isRelevantFor(openCal.getPersonenListe()))
//			{
//				// update
//				updateTermine(termin);
//			}
//		}
//		else if (evt.getEventID() == TODO_EVT) // Update ToDo
//		{
//			ToDo toDo = ((ToDoEvent) evt).getToDo();
//
//			if (toDo.getErinnernAb().isGreater(endAnsicht) <= 0
//					&& toDo.getFälligPer().isGreater(bgnAnsicht) >= 0
//					&& toDo.isRelevantFor(openCal.getPersonenListe()))
//			{
//				// update
//				updateToDo(toDo);
//			}
//		}
	}

	// overloaded by childs
	public void updateTermine(Termin t)
	{}

	public void updateToDo(ToDo t)
	{}
}
