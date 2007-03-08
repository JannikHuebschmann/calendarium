/*
 * Created on 07.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package message;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import basisklassen.Eintrag;
import basisklassen.Person;
import basisklassen.Teilnehmer;
import client.ansicht.Ansicht;
import data.Data;

/**
 * @author MartiniDestroyer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MessageFrame extends Ansicht {
	@SuppressWarnings("unused")
	private JFrame parentFrame;

	/**
	 * @param parentFrame
	 * @param termin
	 */
	public MessageFrame(JFrame f, Eintrag eintrag, JLabel status) {

		super(f, status);
		gui.setTitle("Nachricht schicken");

		gui.setSize(500, 400);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		JPanel panelnorth = new JPanel();
		panelnorth.setLayout(new BoxLayout(panelnorth, BoxLayout.PAGE_AXIS));
		panelnorth.setBorder(new TitledBorder(
				"Wem wollen Sie die Nachricht schicken?"));
		JPanel panelcenter = new JPanel();
		panelcenter
				.setBorder(new TitledBorder("Hier die Nachricht eintragen:"));
		panelcenter.setLayout(new BoxLayout(panelcenter, BoxLayout.LINE_AXIS));

		JPanel panelsouth = new JPanel();
		panelsouth.setSize(50, 20);

		JTextArea area = new JTextArea();

		JRadioButton buttonChef = new JRadioButton(
				"nur dem, der den Termin anlegte");
		JRadioButton buttonAll = new JRadioButton("allen Teilnehmern");
		buttonChef.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(buttonChef);
		group.add(buttonAll);

		JButton senden = new JButton();
		senden.setText("Senden");

		panelnorth.add(buttonChef);
		panelnorth.add(buttonAll);
		panelsouth.add(senden);
		panelcenter.add(area);

		gui.getContentPane().add(panelnorth, BorderLayout.NORTH);
		gui.getContentPane().add(panelcenter, BorderLayout.CENTER);
		gui.getContentPane().add(panelsouth, BorderLayout.SOUTH);

		senden.addActionListener(new AntwortActionListener(area, buttonChef,
				eintrag));

	}

	class AntwortActionListener implements ActionListener {

		private Vector teilnehmer;

		private JTextArea antwort;

		private JRadioButton chef;

		@SuppressWarnings("unused")
		private Person owner;// der, der den termin angelegt hat (chef)

		private Vector ownervct = new Vector();

		private Eintrag ein;

		@SuppressWarnings("unchecked")
		public AntwortActionListener(JTextArea a, JRadioButton b, Eintrag e) {

			antwort = a;
			chef = b;
			owner = e.getOwner();
			teilnehmer = e.getTeilnehmer();
			// e.getOwner().
			ownervct.addElement(new Teilnehmer(e.getOwner()));
			ein = e;
		}

		public void actionPerformed(ActionEvent e) {

			String nachricht = "Eine Antwort fuer den Eintrag \""
					+ ein.getKurzText() + "\" wurde verschickt:\n";
			nachricht += "\n----------------------Beginn der Nachricht----------------------\n"
					+ antwort.getText();
			nachricht += "\n-----------------------ENDE DER NACHRICHT-----------------------";

			if (chef.isSelected()) {
				Data.antwort.sendMessage(ein, ownervct, nachricht);
			} else {
				Data.antwort.sendMessage(ein, teilnehmer, nachricht);
			}

			// nach sendenklick wird fenster geschlossen
			try {

				gui.setClosed(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
	}
}
