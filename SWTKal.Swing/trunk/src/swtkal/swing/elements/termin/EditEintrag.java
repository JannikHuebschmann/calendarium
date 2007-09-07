package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//import java.util.*;

//import swtkal.domain.EintragsTyp;
//import client.gui.*;
import swtkal.swing.ListenerForActions;

////////////////////////////////////////////////////////////////////////////////////////////
// EditEintrag // EditEintrag // EditEintrag // EditEintrag // EditEintrag // EditEintrag //
////////////////////////////////////////////////////////////////////////////////////////////
public class EditEintrag extends ListenerForActions implements ChangeListener, ItemListener
{
	JFrame parentFrame;
	JInternalFrame gui = new JInternalFrame("", true, true, false, true);

	// Tabs
	JTabbedPane tabs;
	AllgemeinPanel allgemeinPane;
	TeilnehmerPanel teilnehmerPane;
	SerienPanel serienPane;

//	// TabIndex
//	int oldTabIndex = 0;
//
//	// Eintagstyp
//	EintragsTyp oldEintragsTyp;

	// UserInterface // UserInterface // UserInterface // UserInterface //
	// UserInterface //
	public void createContent()
	{
		tabs = new JTabbedPane();
		tabs.addTab("Allgemein", allgemeinPane.getGUI());
		tabs.addTab("Teilnehmer", teilnehmerPane.getGUI());
		tabs.addTab("Serie", serienPane.getGUI());
		tabs.addChangeListener(this);

		// gui.setSize(460, 485);
		gui.setSize(512, 520);
		gui.setPreferredSize(gui.getSize());

		gui.getContentPane().setLayout(new BorderLayout());
		gui.getContentPane().add("Center", tabs);

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameEventHandler());
	}

	public void setTitle(String t)
	{
		gui.setTitle(t);
	}
//
//	void disableGUI() {
//		setEnabled(allgemeinPane.getGUI(), false);
//		setEnabled(teilnehmerPane.getGUI(), false);
//		setEnabled(serienPane.getGUI(), false);
//
//		TransPane transPane = new TransPane();
//
//		gui.setGlassPane(transPane);
//		gui.getGlassPane().setVisible(true);
//	}
//
//	void setEnabled(Component c, boolean e) {
//		if (c.getClass() == JPanel.class || c.getClass() == Datumsfeld.class
//				|| c.getClass() == Zeitfeld.class) {
//			for (int i = 0; i < ((Container) c).getComponentCount(); i++) {
//				setEnabled(((Container) c).getComponent(i), e);
//			}
//		} else if (c.getClass() == JTextField.class) {
//			((JTextField) c).setEditable(e);
//		} else if (c.getClass() == JTextArea.class) {
//			((JTextArea) c).setEditable(e);
//		} else if (c.getClass() == Zahlenfeld.class) {
//			((Zahlenfeld) c).setEditable(e);
//		} else if (c.getClass() == CharacterTextField.class) {
//			((CharacterTextField) c).setEditable(e);
//		}
//	}

	public JInternalFrame getGUI()
	{
		return gui;
	}

//	Rectangle getRectOK() {
//		if (tabs.getSelectedIndex() == 0) {
//			return allgemeinPane.getRectOK();
//		} else if (tabs.getSelectedIndex() == 1) {
//			return teilnehmerPane.getRectOK();
//		} else {
//			return serienPane.getRectOK();
//		}
//	}
//
//	Rectangle getRectAbbr() {
//		if (tabs.getSelectedIndex() == 0) {
//			return allgemeinPane.getRectAbbr();
//		} else if (tabs.getSelectedIndex() == 1) {
//			return teilnehmerPane.getRectAbbr();
//		} else {
//			return serienPane.getRectAbbr();
//		}
//	}
//
//	Vector getFailedPersonen() {
//		return teilnehmerPane.getFailedPersonen();
//	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// InternalFrameEventHandler // InternalFrameEventHandler //
	// InternalFrameEventHandler //
	// ///////////////////////////////////////////////////////////////////////////////////////
	class InternalFrameEventHandler extends InternalFrameAdapter
	{
		public void internalFrameClosed(InternalFrameEvent e)
		{
//			allgemeinPane.datePanel[0].closeCalendar();
//			allgemeinPane.datePanel[1].closeCalendar();
		}

		public void internalFrameActivated(InternalFrameEvent e)
		{
//			if (gui.getGlassPane() instanceof TransPane)
//			{
//				gui.getGlassPane().setVisible(true);
//			}
		}
	}

//	// ///////////////////////////////////////////////////////////////////////////////////////////
//	// TransPane // TransPane // TransPane // TransPane // TransPane //
//	// TransPane // TransPane //
//	// ///////////////////////////////////////////////////////////////////////////////////////////
//	class TransPane extends JPanel {
//		private static final long serialVersionUID = 3122868989076562594L;
//
//		TransPane() {
//			enableEvents(AWTEvent.MOUSE_EVENT_MASK);
//		}
//
//		public boolean isOpaque() {
//			return false;
//		}
//
//		public void paint(Graphics g) {
//		}
//
//		public void processMouseEvent(MouseEvent e) {
//			if (e.getID() == MouseEvent.MOUSE_CLICKED) {
//				Point pScr = new Point(((TransPane) e.getSource())
//						.getLocationOnScreen().x
//						+ e.getX(), ((TransPane) e.getSource())
//						.getLocationOnScreen().y
//						+ e.getY());
//
//				if (getRectOK().contains(pScr) || getRectAbbr().contains(pScr)) { // OK,
//					// Abbrechen
//					try {
//						gui.setClosed(true);
//					} catch (java.beans.PropertyVetoException ex) {
//					}
//				} else if (tabs.getComponentAt(e.getPoint()).getClass() == JTabbedPane.class) {
//					// Tabs
//					if (tabs.getBoundsAt(0).contains(e.getPoint())) {
//						tabs.setSelectedIndex(0);
//					} else if (tabs.getBoundsAt(1).contains(e.getPoint())) {
//						tabs.setSelectedIndex(1);
//					} else if (tabs.getBoundsAt(2).contains(e.getPoint())) {
//						tabs.setSelectedIndex(2);
//					}
//				}
//			}
//		}
//	}
//
	public void stateChanged(ChangeEvent e)
	{
	}

	// ItemListener // ItemListener // ItemListener // ItemListener //
	// ItemListener //
	public void itemStateChanged(ItemEvent e)
	{
//		if (e.getStateChange() == ItemEvent.SELECTED) {
//			// EintragsTyp typ = (EintragsTyp) e.getItem();//classcastexception
//			// bei typwechsel
//
//			EintragsTyp typ = new EintragsTyp((long) e.getID());
//			if (oldEintragsTyp != typ) {
//				if (!teilnehmerPane.isBerechtigtForAll(typ)) {
//					((JComboBox) e.getSource()).setSelectedItem(oldEintragsTyp);
//
//					char c = 34;
//					String zeile1 = "Sie besitzen nicht fuer alle Teilnehmer ein Eintragsrecht";
//					String zeile2 = "fuer Einträge vom Typ " + c
//							+ typ.getBezeichnung() + c + '!';
//
//					JTextArea area = new JTextArea(zeile1 + '\n' + zeile2);
//					area.setBackground(SystemColor.control);
//					area.setEditable(false);
//					area
//							.setColumns(Math.max(zeile1.length(), zeile2
//									.length()) + 2);
//					area.setRows(2);
//
//					JOptionPane optionPane = new JOptionPane(area,
//							JOptionPane.WARNING_MESSAGE);
//					JDialog dialogPane = optionPane.createDialog(parentFrame,
//							"Fehlende Rechte");
//
//					dialogPane.show();
//					dialogPane.dispose();
//
//				} else
//					oldEintragsTyp = typ;
//			}
//		}
	}
}
