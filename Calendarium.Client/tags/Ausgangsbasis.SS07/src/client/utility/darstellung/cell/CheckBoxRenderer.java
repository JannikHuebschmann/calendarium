package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import basisklassen.Gruppe;
import basisklassen.Person;
import client.eintrag.TeilnehmerPanel;

//////////////////////////////////////////////////////////////////////////////////////////////////////
// CheckBoxRenderer // CheckBoxRenderer // CheckBoxRenderer // CheckBoxRenderer // CheckBoxRenderer //
//////////////////////////////////////////////////////////////////////////////////////////////////////

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 5641291265753413243L;

	private Color mDefaultForeground;

	private Color mDefaultBackground;

	private TeilnehmerPanel tPane = null;

	private JLabel label = null;

	public CheckBoxRenderer() {
		super();
		setHorizontalAlignment(JCheckBox.CENTER);

		mDefaultForeground = SystemColor.textHighlight;
		;
		mDefaultBackground = SystemColor.window;
		setOpaque(true);
	}

	public CheckBoxRenderer(TeilnehmerPanel t) {
		super();
		tPane = t;
		setHorizontalAlignment(JCheckBox.CENTER);

		mDefaultForeground = SystemColor.textHighlight;
		;
		mDefaultBackground = SystemColor.window;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (tPane != null) {
			Object obj = table.getValueAt(row, 0);

			if (obj.getClass() == Gruppe.class) {
				if (tPane.hasFailed((Gruppe) obj))
					value = null;
			} else {
				if (tPane.hasFailed((Person) obj))
					value = null;
			}
		}

		if (value != null) {
			Boolean b = (Boolean) value;
			setSelected(b.booleanValue());

			if (isSelected) {
				setBackground(mDefaultForeground);
				setForeground(mDefaultBackground);

			} else {
				setBackground(mDefaultBackground);
				setForeground(mDefaultForeground);
			}

			return this;

		} else {
			if (label == null) {
				label = new JLabel();
				label.setVisible(false);
			}

			return label;
		}
	}
}
