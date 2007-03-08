package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

import basisklassen.Person;
import client.ansicht.OffeneKalender;

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// PersonListCellRenderer // PersonListCellRenderer // PersonListCellRenderer // PersonListCellRenderer //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

public class PersonListCellRenderer extends JPanel implements ListCellRenderer,
		data.Shared {
	private static final long serialVersionUID = -7561809740250890879L;

	private JLabel entryLabel;

	private JPanel entryColorPanel;

	private OffeneKalender openCal;

	public PersonListCellRenderer(OffeneKalender o) {
		openCal = o;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(SystemColor.window);
		setOpaque(true);

		create();
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			Person person = (Person) value;

			entryLabel.setText(person.getNameLang() + " ("
					+ person.getKuerzel() + ")");
			entryColorPanel.setBackground(PERSFARBEN[openCal
					.getColorIndex(person)]);

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
