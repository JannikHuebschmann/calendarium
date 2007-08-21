package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import java.awt.*;

import basisklassen.Feiertag;

//////////////////////////////////////////////////////////////////////////////////////
// FeiertagListCellRenderer // FeiertagListCellRenderer // FeiertagListCellRenderer //
//////////////////////////////////////////////////////////////////////////////////////

public class FeiertagListCellRenderer extends JPanel implements
		ListCellRenderer {
	private static final long serialVersionUID = -9044029847413179479L;
	private JLabel dateLabel, textLabel;

	public FeiertagListCellRenderer() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(SystemColor.window);
		setOpaque(true);

		create();
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			Feiertag tag = (Feiertag) value;

			dateLabel.setText(tag.getDateString());
			textLabel.setText(tag.getBezeichnung());

			if (isSelected) {
				setBackground(SystemColor.textHighlight);
				dateLabel.setForeground(SystemColor.textHighlightText);
				textLabel.setForeground(SystemColor.textHighlightText);
			} else {
				setBackground(SystemColor.window);
				dateLabel.setForeground(SystemColor.textText);
				textLabel.setForeground(SystemColor.textText);
			}
		}
		return this;
	}

	public boolean isOpaque() {
		return true;
	}

	void create() {
		dateLabel = new JLabel();
		dateLabel.setPreferredSize(new Dimension(75, 20));

		textLabel = new JLabel();
		textLabel.setPreferredSize(new Dimension(120, 20));

		add(dateLabel);
		add(textLabel);
		add(Box.createHorizontalGlue());
	}
}
