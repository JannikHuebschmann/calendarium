package swtkal.swing.client;
// Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

import swtkal.domain.*;
import swtkal.swing.elements.CustomBorder;

//////////////////////////////////////////////////////////////////////////////////////////////////
// TerminObjekt // TerminObjekt // TerminObjekt // TerminObjekt // TerminObjekt // TerminObjekt //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class TerminObjekt extends EintragsObjekt
{
	private static final long serialVersionUID = 6608615191785897124L;

	private Termin termin;

	public TerminObjekt(Termin t, OffeneKalender o, boolean b, int f)
	{
		eintrag = t;
//		openCal = o;
		termin = (Termin) eintrag;

		setContextMenu();
		create(b, f);
	}

	private void create(boolean border, int fontSize)
	{
//		boolean multiView = (openCal.getSize() > 1);
		String terminText = "";

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.weightx = 0.0;

		if (border)
			c.weighty = 1.0;

		// Owner?
//		if (termin.getBesitzer() != null
//				&& termin.getBesitzer().getID() == Data.user.getID())
			isBerechtigt = true;

//		Hashtable pn = termin.getAllPersons();
//		Recht recht = new Recht(termin.getTyp(), LESERECHT);
//
//		Enumeration e = openCal.getEnumeration();
//		while (e.hasMoreElements())
//		{
//			Person person = (Person) e.nextElement();
//			if (pn.containsKey(new Long(person.getID())))
//			{
//				kuerzel += person.getKuerzel() + ", ";
//				if (!isBerechtigt)
//				{
//					if (Data.user.getID() == person.getID()
//							|| Data.rechte.isBerechtigt(person, recht))
//						isBerechtigt = true;
//				}
//
//				// Balken links
//				if (multiView)
//				{
//					Color farbCode = PERSFARBEN[openCal.getColorIndex(person)];
//
//					JPanel farbPane = new JPanel();
//					if (border)
//					{
//						farbPane.setBorder(new LineBorder(Color.black));
//					}
//					else
//					{
//						farbPane.setBorder(new CustomBorder(Color.black, false, true,
//								true, false));
//					}
//
//					farbPane.setPreferredSize(new Dimension(5, 5));
//					farbPane.setBackground(farbCode);
//
//					gridbag.setConstraints(farbPane, c);
//					add(farbPane);
//
//					c.gridx++;
//				}
//			}
//		}
//
//		kuerzel = kuerzel.substring(0, kuerzel.length() - 2);

		// TextPane
		DefaultStyledDocument doc = new DefaultStyledDocument();

		SimpleAttributeSet text = new SimpleAttributeSet();
		StyleConstants.setFontFamily(text, "Dialog");
		StyleConstants.setForeground(text, Color.black);
		StyleConstants.setFontSize(text, fontSize);
		if (termin.getVerschiebbar())
			StyleConstants.setItalic(text, true);

		if (termin.getBeginn().getDaysBetween(termin.getEnde()) == 0)
		{
			terminText = " " + termin.getBeginn().getTimeStr() + " - "
					+ termin.getEnde().getTimeStr();
		}
		else
		{
			terminText = " " + termin.getBeginn().toString() + " - "
					+ termin.getEnde().toString();
		}

		if (isBerechtigt)
			terminText += ", " + termin.getKurzText();

		try
		{
			doc.insertString(0, terminText, text);
		}
		catch (BadLocationException ex)
		{}

//		if (multiView)
//		{
//			SimpleAttributeSet name = new SimpleAttributeSet();
//			StyleConstants.setFontFamily(name, "Dialog");
//			StyleConstants.setFontSize(name, fontSize + 1);
//			StyleConstants.setBold(name, true);
//
//			try
//			{
//				doc.insertString(0, kuerzel, name);
//			}
//			catch (BadLocationException ex)
//			{}
//		}

		JTextPane textPane = new JTextPane(doc);
		textPane.setEnabled(false);
		textPane.addMouseListener(this);

		if (border)
		{
			textPane.setBorder(new CustomBorder(Color.black, true, true, false,
					false));
		}

		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridheight = 1;

		gridbag.setConstraints(textPane, c);
		add(textPane);

		if (isBerechtigt)
		{
			GridBagLayout bag = new GridBagLayout();
			GridBagConstraints cs = new GridBagConstraints();

			// Balken unten
			JPanel typPane = new JPanel();
			typPane.setLayout(bag);
			typPane.setPreferredSize(new Dimension(20, 3));
			typPane.addMouseListener(this);

			if (border)
			{
				typPane.setBorder(new CustomBorder(Color.black, false, true, false,
						false));
			}

			cs.fill = GridBagConstraints.BOTH;
			cs.gridx = 0;
			cs.gridy = 0;
			cs.weightx = 1.0;

			JPanel leer = new JPanel();
			leer.setBackground(Color.white);

			bag.setConstraints(leer, cs);
			typPane.add(leer);

			JPanel farbe = new JPanel();
			farbe.setMinimumSize(new Dimension(20, 3));
			farbe.setPreferredSize(new Dimension(20, 3));
//			farbe.setBackground(Data.typen.getUserTyp(termin.getTyp()).getFarbe());

			cs.gridx = 1;
			cs.weightx = 0.0;

			bag.setConstraints(farbe, cs);
			typPane.add(farbe);

			JPanel line = new JPanel();
			line.setPreferredSize(new Dimension(20, 1));
			line.setBackground(Color.black);

			cs.gridx = 0;
			cs.gridy = 1;
			cs.gridwidth = 2;
			cs.weightx = 1.0;

			bag.setConstraints(line, cs);
			typPane.add(line);

			// add
			c.gridy = 1;
			c.weighty = 0.0;

			gridbag.setConstraints(typPane, c);
			add(typPane);
		}
	}
}
