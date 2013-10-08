package swtkal.swing.client;
//Achtung: im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import swtkal.domain.*;
import swtkal.swing.elements.CustomBorder;

///////////////////////////////////////////////////////////////////////////////////////////////////////////
// TerminListeObjekt // TerminListeObjekt // TerminListeObjekt // TerminListeObjekt // TerminListeObjekt //
///////////////////////////////////////////////////////////////////////////////////////////////////////////

class TerminListeObjekt extends ListeObjekt
{
	private Color /* LABEL_BACKGROUND, */ ZEILE_BACKGROUND, LABEL_FONTCOLOR;

	private int HOUR_HEIGHT;

	// Beschriftung
	private boolean small;

	@SuppressWarnings("rawtypes")
	TerminListeObjekt(Ansicht s, Datum d, Vector termine)
	{
		super(s, d);
		small = (s.getClass() != Tagesansicht.class);

		init();
		create(termine);
	}

	// Konstanten fuer die Objekte
	void init()
	{
//		LABEL_BACKGROUND = new Color(204, 204, 204);
		ZEILE_BACKGROUND = new Color(204, 204, 30);
		LABEL_FONTCOLOR = Color.black;
		HOUR_HEIGHT = 36;
	}

	@SuppressWarnings("rawtypes")
	void create(Vector termine)
	{
		TerminScrollListe terminListe;

		if (termine != null && termine.size() > 0)
		{	// gefüllte TerminScrollListe
			Termin [][] grid = new Termin[48][];
			int index = 0;

			// Grid mit den Terminen aufbauen
			while (index < termine.size())
			{
				addToGrid(termine, index, grid);
				index++;
			}
			terminListe = new TerminScrollListe(termine, grid);
		}
		else
		{  // leere TerminScrollListe
			terminListe = new TerminScrollListe();
		}

		@SuppressWarnings("unused")
		WaitForPaint wait = new WaitForPaint(terminListe);

		gui.setMinimumSize(new Dimension(0, 0));
		gui.add("Center", terminListe);
	}

	@SuppressWarnings("rawtypes")
	void addToGrid(Vector termine, int index, Termin[][] grid)
	{
		int bgnMin, bgnHour;
		int endMin, endHour;
		int gridWidth;
		int bgn, end;
		int i, j;

		if (grid[0] == null)
		{
			gridWidth = 0;
		}
		else
		{
			gridWidth = grid[0].length;
		}

		Termin termin = (Termin) termine.elementAt(index);

		if (termin.getBeginn().isGreater(date) >= 0)
		{
			bgnHour = termin.getBeginn().getHour();
			bgnMin = termin.getBeginn().getMin();
		}
		else
		{
			bgnHour = 0;
			bgnMin = 0;
		}

		if (termin.getEnde().isGreater(nextDate) <= 0)
		{
			endHour = termin.getEnde().getHour();
			endMin = termin.getEnde().getMin();
		}
		else
		{
			endHour = 24;
			endMin = 0;
		}

		// Stunden und Minuten runden.
		if (bgnMin < 15)
			bgnMin = 0;
		else if (bgnMin < 45)
			bgnMin = 30;
		else
		{
			bgnMin = 0;
			bgnHour++;
		}

		if (endMin < 15)
			endMin = 0;
		else if (endMin < 45)
			endMin = 30;
		else
		{
			endMin = 0;
			endHour++;
		}

		if (bgnMin > 0)
			bgnMin = 1;
		if (endMin > 0)
			endMin = 1;

		bgn = bgnHour * 2 + bgnMin;
		end = endHour * 2 + endMin - 1;

		if (gridWidth == 0)
		{	// erste Spalte erzeugen und Termin an der entsprechenden Stelle einfügen
			for (i = 0; i < 48; i++)
			{
				grid[i] = new Termin[1];

				if (i >= bgn && i <= end)
				{
					grid[i][0] = termin;					
				}
				else
				{
					grid[i][0] = null;
				}
			}
			gridWidth = 1;
		}
		else
		{	// Termin an den richtigen Stellen im vorhandenen Grid einfügen
			// Schleife sucht einen passenden freien Bereich im Grid
			for (i = 0; i < gridWidth; i++)
			{
				for (j = bgn; j <= end; j++)
				{
					if (grid[j][i] != null)
						break;
				}
				if (j > end)
					break;
			}

			if (i == gridWidth)
			{	// eine neue Spalte wird ins Grid eingefügt und Termin dort eingetragen
				gridWidth++;
				resizeGrid(grid, gridWidth);

				for (j = 0; j < 48; j++)
					if (j >= bgn && j <= end)
					{
						grid[j][gridWidth - 1] = termin;
					}
					else
					{
						grid[j][gridWidth - 1] = null;
					}

			}
			else
			{	// Termin in der aktuelle Spalte einfügen
				for (j = bgn; j <= end; j++)
				{
					grid[j][i] = termin;
				}
			}
		}
	}

	// Redimensionieren des Arrays
	void resizeGrid(Termin[][] grid, int size)
	{
		Termin[] help;

		int m = Math.min(size, grid[0].length);
		int i, j;

		for (i = 0; i < 48; i++)
		{
			help = new Termin[size];
			for (j = 0; j < m; j++)
			{
				help[j] = grid[i][j];
			}

			grid[i] = help;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// TerminScrollListe // TerminScrollListe // TerminScrollListe //
	// TerminScrollListe //
	// ////////////////////////////////////////////////////////////////////////////////////
	class TerminScrollListe extends JScrollPane
	{
		private static final long serialVersionUID = 6696899111075620797L;

		TerminScrollListe()
		{
			create();
		}

		@SuppressWarnings("rawtypes")
		TerminScrollListe(Vector termine, Termin[][] grid)
		{
			create(grid, termine);
		}

		void create()
		{
			int x;

			// Create the panel that will be scrolled.
			JPanel mControlPane = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();

			mControlPane.setLayout(gridbag);

			// Constraints
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1.0;

			for (int i = 0; i < 24; i++)
			{
				// Constraints
				c.gridx = 0;
				c.gridy = i * 2;
				c.weightx = 0.0;
				c.gridwidth = 1;
				c.gridheight = 2;

				// Create headers of hours
				JPanel headers = new JPanel();
				headers.setBorder(new CustomBorder(Color.black, true, false, true,
						true));
				headers.setLayout(new BoxLayout(headers, BoxLayout.X_AXIS));

				if (i < 10)
				{
					x = small ? 7 : 16;
					headers.add(Box.createRigidArea(new Dimension(x, HOUR_HEIGHT)));
				}
				else
				{
					x = small ? 0 : 8;
					headers.add(Box.createRigidArea(new Dimension(x, HOUR_HEIGHT)));
				}
				JLabel hLabel = new JLabel(String.valueOf(i));
				hLabel.setForeground(LABEL_FONTCOLOR);

				x = small ? 12 : 16;
				hLabel.setFont(new Font("Dialog", Font.BOLD, x));
				headers.add(hLabel);

				if (!small)
					headers.add(Box.createRigidArea(new Dimension(5, HOUR_HEIGHT)));

				// add to
				gridbag.setConstraints(headers, c);
				mControlPane.add(headers);

				for (int j = 0; j < 2; j++)
				{
					JLabel mLabel;

					// Constraints
					c.gridx = 1;
					c.gridy = i * 2 + j;
					c.weightx = 0.0;
					c.gridwidth = 1;
					c.gridheight = 1;

					// Create the labels.
					if (j == 0)
					{
						mLabel = new JLabel(":00");
						mLabel.setBorder(new CustomBorder(Color.black, true, true,
								false, false));
					}
					else
					{
						mLabel = new JLabel(":30");
						mLabel.setBorder(new CustomBorder(Color.black, false, true,
								true, false));
					}

					mLabel.setForeground(LABEL_FONTCOLOR);

					x = small ? 8 : 10;
					mLabel.setFont(new Font("Dialog", Font.PLAIN, x));

					x = small ? 12 : 20;
					mLabel.setPreferredSize(new Dimension(x, HOUR_HEIGHT / 2 + 1));

					// Add to
					gridbag.setConstraints(mLabel, c);
					mControlPane.add(mLabel);

					// Constraints
					c.gridx = 2;
					c.gridy = i * 2 + j;
					c.weightx = 1.0;
					c.gridwidth = 1;
					c.gridheight = 1;

					// Create the panels.
					LeerObjekt zeile = new LeerObjekt();
					zeile.setItems(new String[] { "Termin eintragen" });
					zeile.addMouseListener(sicht);
					zeile.addActionListener(sicht);

					zeile.setBackground(ZEILE_BACKGROUND);
					zeile.setBorder(new LineBorder(Color.black));
					zeile.setPreferredSize(new Dimension(30, HOUR_HEIGHT / 2 + 1));

					setDateForPane(zeile, i * 2 + j);

					// Add to
					gridbag.setConstraints(zeile, c);
					mControlPane.add(zeile);
				}	// end for j
			}	// end for i

			// Tell the scroll pane which component to scroll.
			setViewportView(mControlPane);
		}

		@SuppressWarnings("rawtypes")
		void create(Termin[][] grid, Vector termine)
		{
			int bWidth, bHeight;
			int i, j, k, x;

			// Create the panel that will be scrolled.
			JPanel mControlPane = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();

			mControlPane.setLayout(gridbag);

			// Constraints
			c.fill = GridBagConstraints.BOTH;

			for (i = 0; i < 24; i++)
			{
				// Constraints
				c.gridx = 0;
				c.gridy = i * 2;
				c.weightx = 0.0;
				c.gridwidth = 1;
				c.gridheight = 2;

				// Create headers of hours
				JPanel headers = new JPanel();
				headers.setBorder(new CustomBorder(Color.black, true, false, true,
						true));
				headers.setLayout(new BoxLayout(headers, BoxLayout.X_AXIS));

				if (i < 10)
				{
					x = small ? 7 : 16;
					headers.add(Box.createRigidArea(new Dimension(x, HOUR_HEIGHT)));
				}
				else
				{
					x = small ? 0 : 8;
					headers.add(Box.createRigidArea(new Dimension(x, HOUR_HEIGHT)));
				}
				JLabel hLabel = new JLabel(String.valueOf(i));
				hLabel.setForeground(LABEL_FONTCOLOR);

				x = small ? 12 : 16;
				hLabel.setFont(new Font("Dialog", Font.BOLD, x));
				headers.add(hLabel);

				if (!small)
					headers.add(Box.createRigidArea(new Dimension(5, HOUR_HEIGHT)));

				// add to
				gridbag.setConstraints(headers, c);
				mControlPane.add(headers);

				for (j = 0; j < 2; j++)
				{
					JLabel mLabel;

					// Constraints
					c.gridx = 1;
					c.gridy = i * 2 + j;
					c.weightx = 0.0;
					c.gridwidth = 1;
					c.gridheight = 1;

					// Create the labels.
					if (j == 0)
					{
						mLabel = new JLabel(":00");
						mLabel.setBorder(new CustomBorder(Color.black, true, true,
								false, false));
					}
					else
					{
						mLabel = new JLabel(":30");
						mLabel.setBorder(new CustomBorder(Color.black, false, true,
								true, false));
					}

					mLabel.setForeground(LABEL_FONTCOLOR);

					x = small ? 8 : 10;
					mLabel.setFont(new Font("Dialog", Font.PLAIN, x));

					x = small ? 12 : 20;
					mLabel.setPreferredSize(new Dimension(x, HOUR_HEIGHT / 2 + 1));

					// Add to
					gridbag.setConstraints(mLabel, c);
					mControlPane.add(mLabel);
				}
			}

			// Constraints
			c.weighty = 1.0;
			c.weightx = 1.0;

			for (i = 0; i < grid[0].length; i++)
			{
				bHeight = 0;
				bWidth = 0;

				for (j = 0; j < 48; j++)
				{
					if (grid[j][i] != null)
					{
						if (bHeight > 0 && (grid[j - 1][i] != grid[j][i] || j == 47))
						{
							c.gridx = i + 2;
							c.gridy = j - bHeight;
							c.gridwidth = bWidth;
							c.gridheight = bHeight + (j == 47 ? 1 : 0);

							Termin t = grid[j-1][i];
//							Termin t = (Termin) termine.elementAt(terminNr);

//							TerminObjekt tObj = new TerminObjekt(t, sicht.openCal,
//									true, 10);
							TerminObjekt tObj = new TerminObjekt(t, null, true, 10);
							tObj.addMouseListener(sicht);
							tObj.addActionListener(sicht);

							gridbag.setConstraints(tObj, c);
							mControlPane.add(tObj);

							bHeight = 0;
							bWidth = 0;
						}

						bHeight++;

						if (i == 0 || grid[j][i] != grid[j][i - 1]
								&& (j == 0 || grid[j - 1][i] != grid[j][i]))
						{
							k = i + 1;
							bWidth = 1;
							while (k < grid[0].length && grid[j][i] == grid[j][k])
							{
								k++;
								bWidth++;
							}
						}

					}
					else
					{	// grid[j][i] == 0
						if (bHeight > 0 && bWidth > 0)
						{
							c.gridx = i + 2;
							c.gridy = j - bHeight;
							c.gridwidth = bWidth;
							c.gridheight = bHeight;

							Termin t = grid[j-1][i];
//							Termin t = (Termin) termine.elementAt(terminNr);

//							TerminObjekt tObj = new TerminObjekt(t, sicht.openCal,
//									true, 10);
							TerminObjekt tObj = new TerminObjekt(t, null, true, 10);
							tObj.addMouseListener(sicht);
							tObj.addActionListener(sicht);

							gridbag.setConstraints(tObj, c);
							mControlPane.add(tObj);

							bHeight = 0;
							bWidth = 0;
						}

						c.gridx = i + 2;
						c.gridy = j;
						c.gridwidth = 1;
						c.gridheight = 1;

						LeerObjekt nothing = new LeerObjekt();
						nothing.setItems(new String[] { "Termin eintragen" });
						nothing.addMouseListener(sicht);
						nothing.addActionListener(sicht);

						nothing.setBackground(ZEILE_BACKGROUND);
						nothing.setBorder(new CustomBorder(Color.black, true, false,
								true, false));
						nothing
								.setMaximumSize(new Dimension(500, HOUR_HEIGHT / 2 + 1));

						setDateForPane(nothing, j);

						gridbag.setConstraints(nothing, c);
						mControlPane.add(nothing);
					}
				}
			}

			// Create the JScrollPane.
			setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			// Tell the scroll pane which component to scroll.
			setViewportView(mControlPane);
		}
	}

	private void setDateForPane(LeerObjekt pane, int j)
	{
		String dateText = date.getDateStr();

		String timeText = String.valueOf(j / 2);
		if (j % 2 == 0)
		{
			timeText += ":00";
		}
		else
		{
			timeText += ":30";
		}

		pane.setDate(new Datum(dateText, timeText));
	}

	class WaitForPaint implements Runnable
	{
		Thread t;

		JScrollBar scrollBar;

		WaitForPaint(JScrollPane pane)
		{
			scrollBar = pane.getVerticalScrollBar();

			t = new Thread(this);
			t.start();
		}

		public void run()
		{
			try
			{
				while (!scrollBar.isShowing())
				{
					Thread.sleep(500);
				}

				scrollBar.setValue(302);		// sets scrollbar to 8:00 AM

				while (scrollBar.getValue() == 0)
				{
					Thread.sleep(500);
					scrollBar.setValue(302);	// sets scrollbar to 8:00 AM
				}

			}
			catch (Exception e)
			{}
		}
	}
}
