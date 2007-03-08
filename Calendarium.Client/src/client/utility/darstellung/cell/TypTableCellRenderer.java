package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

import basisklassen.EintragsTyp;

//////////////////////////////////////////////////////////////////////////////////////////////////
// TypTableCellRenderer // TypTableCellRenderer // TypTableCellRenderer // TypTableCellRenderer //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class TypTableCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 4689153573661823286L;

	private JLabel entryLabel;

	private JPanel entryColorPanel;

	public TypTableCellRenderer() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(SystemColor.window);
		setOpaque(true);

		create();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null) {
			EintragsTyp typ = (EintragsTyp) value;

			entryLabel.setText(typ.getBezeichnung());
			entryColorPanel.setBackground(typ.getFarbe());

			if (isSelected) {
				setBackground(SystemColor.textHighlight);
				entryLabel.setForeground(SystemColor.textHighlightText);
			} else {
				setBackground(SystemColor.window);
				entryLabel.setForeground(SystemColor.textText);
			}
		}
		return this;
	}

	public boolean isOpaque() {
		return true;
	}

	void create() {
		entryColorPanel = new JPanel();
		entryColorPanel.setBorder(new LineBorder(Color.black));
		entryColorPanel.setMaximumSize(new Dimension(15, 15));
		entryColorPanel.setPreferredSize(new Dimension(15, 15));

		entryLabel = new JLabel();

		add(Box.createRigidArea(new Dimension(5, 20)));
		add(entryColorPanel);
		add(Box.createRigidArea(new Dimension(5, 20)));
		add(entryLabel);
	}
}
