package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import basisklassen.EintragsTyp;

//////////////////////////////////////////////////////////////////////////////////////////////
// TypListCellRenderer // TypListCellRenderer // TypListCellRenderer // TypListCellRenderer //
//////////////////////////////////////////////////////////////////////////////////////////////

public class TypListCellRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = 7331183486869022822L;

	private JLabel entryLabel;

	private JPanel entryColorPanel;

	public TypListCellRenderer() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(SystemColor.window);
		setOpaque(true);

		create();
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
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
