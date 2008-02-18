package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;

//////////////////////////////////////////////////////////////////////////////////
// CustomBorder // CustomBorder // CustomBorder // CustomBorder // CustomBorder //
//////////////////////////////////////////////////////////////////////////////////

//extends changed
public class CustomBorder extends javax.swing.border.LineBorder {
	private static final long serialVersionUID = -231994249150036334L;

	private boolean oben;

	private boolean rechts;

	private boolean unten;

	private boolean links;

	public CustomBorder(Color color, boolean l1, boolean l2, boolean l3,
			boolean l4) {
		super(color);

		oben = l1;
		rechts = l2;
		unten = l3;
		links = l4;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Color oldColor = g.getColor();
		g.setColor(lineColor);

		if (oben == true)
			g.drawLine(x, y, x + width - 1, y);
		if (links == true)
			g.drawLine(x, y, x, y + height - 1);
		if (rechts == true)
			g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
		if (unten == true)
			g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);

		g.setColor(oldColor);
	}
}
