package client.utility.darstellung.cell; //
///////////////////////////////////////////

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import basisklassen.EintragsTyp;

///////////////////////////////////////////////////////////////////////////////////////
// TypCellEditor // TypCellEditor // TypCellEditor // TypCellEditor // TypCellEditor //
///////////////////////////////////////////////////////////////////////////////////////

public class TypCellEditor extends JPanel implements ComboBoxEditor {
	private static final long serialVersionUID = 6769076926177415254L;

	private JTextField entryLabel;

	private JPanel entryColorPanel;

	public TypCellEditor() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(SystemColor.window);

		create();
	}

	public Component getEditorComponent() {
		return this;
	}

	public Object getItem() {
		return entryLabel.getText();
	}

	public void setItem(Object obj) {

		EintragsTyp typ = null;
		try {

			try {
				typ = (EintragsTyp) obj;// classcast exception
			} catch (RuntimeException e1) {

			}

			// typ=new EintragsTyp(obj);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (typ != null) {

			entryLabel.setText(typ.getBezeichnung());
			entryColorPanel.setBackground(typ.getFarbe());
		}

	}

	public void addActionListener(ActionListener listener) {
	}

	public void removeActionListener(ActionListener listener) {
	}

	public void selectAll() {
	}

	void create() {
		entryColorPanel = new JPanel();
		entryColorPanel.setBorder(new LineBorder(Color.black));
		entryColorPanel.setMaximumSize(new Dimension(15, 15));
		entryColorPanel.setPreferredSize(new Dimension(15, 15));

		entryLabel = new JTextField();
		entryLabel.setBorder(null);
		entryLabel.setEditable(false);

		add(Box.createRigidArea(new Dimension(5, 20)));
		add(entryColorPanel);
		add(Box.createRigidArea(new Dimension(5, 20)));
		add(entryLabel);

		validate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
	 */
	// public void setItem(Object obj) {
	//		
	// EintragsTyp typ = new EintragsTyp(obj);
	//		
	// }
}
