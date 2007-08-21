package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;

import basisklassen.Gruppe;
import basisklassen.Person;
import client.eintrag.TeilnehmerPanel;

//////////////////////////////////////////////////////////////////////////////////////////////
// GruppenCellRenderer // GruppenCellRenderer // GruppenCellRenderer // GruppenCellRenderer //
//////////////////////////////////////////////////////////////////////////////////////////////
public class GruppenCellRenderer extends JTextField implements
		TableCellRenderer {
	private static final long serialVersionUID = 6464281409083263649L;

	private Class klasse;

	private FontMetrics fm;

	private TeilnehmerPanel tPane = null;

	public GruppenCellRenderer(Class k) {
		super();

		klasse = k;
		setBorder(null);

		fm = getFontMetrics(getFont());
	}

	public GruppenCellRenderer(Class k, TeilnehmerPanel t) {
		super();

		tPane = t;
		klasse = k;
		setBorder(null);

		fm = getFontMetrics(getFont());
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setBackground(SystemColor.textHighlight);
			setForeground(SystemColor.textHighlightText);
		} else {
			setBackground(SystemColor.window);
			setForeground(SystemColor.textText);
		}

		if (value != null) {
			if (klasse == Gruppe.class) {
				Gruppe gruppe = (Gruppe) value;

				if (column == 0) {
					setText(gruppe.getKuerzel());
				} else {
					int width = table.getColumn("Name").getWidth() - 2;
					setText(getVisibleString(gruppe.getName(), width));
				}

				if (tPane != null) {
					if (tPane.hasFailed(gruppe))
						setForeground(Color.gray);
				}

			} else {
				Person person = (Person) value;

				if (column == 0) {
					setText(person.getKuerzel());
				} else {
					int width = table.getColumn("Name").getWidth() - 2;
					setText(getVisibleString(person.getNameLang(), width));
				}

				if (tPane != null) {
					if (tPane.hasFailed(person))
						setForeground(Color.gray);
				}
			}
		}

		return this;
	}

	String getVisibleString(String txt, int width) {
		if (fm.stringWidth(txt) > width) {
			return getVisibleString(txt.substring(0, txt.length() - 1), width);

		} else
			return txt;
	}
}
