package client.admin.typ; //
////////////////////////////

import javax.swing.*;
import javax.swing.border.*;
//import javax.swing.preview.*; //komplett raus genommen weil oben swing importiert wird?!?!?

import java.awt.*;

import basisklassen.EintragsTyp;
import client.gui.CharacterTextField;
import client.utility.ListenerForActions;
import client.utility.darstellung.ButtonPanel;

/////////////////////////////////////////////////////////////////////////////////////////////////////
// EditTyp // EditTyp // EditTyp // EditTyp // EditTyp // EditTyp // EditTyp // EditTyp // EditTyp //
/////////////////////////////////////////////////////////////////////////////////////////////////////

public class EditTyp extends ListenerForActions {
	// ParentFrame
	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	// Dialog
	private JDialog dialog;

	// Controls
	private CharacterTextField bezeichnung;

	private JColorChooser farbSelect;

	private JPanel farbPane;

	// Eintragstyp
	private EintragsTyp typ = null;

	public EditTyp(JFrame f) {
		parentFrame = f;

		gui.setLayout(new BorderLayout(0, 10));
		gui.setBorder(new EmptyBorder(10, 10, 0, 10));
		// gui.setSize(500, 500);//by johnny
		// parentFrame.setSize(500,500);//by johnny

		create();
	}

	void create() {
		// Typ // Typ // Typ // Typ // Typ // Typ // Typ // Typ // Typ // Typ //
		// Typ // Typ //

		JPanel typPane = new JPanel();
		typPane.setLayout(new BoxLayout(typPane, BoxLayout.Y_AXIS));
		JPanel zeile = new JPanel();
		zeile.setLayout(new BoxLayout(zeile, BoxLayout.X_AXIS));

		JLabel label = new JLabel("Bezeichnung:");
		label.setPreferredSize(new Dimension(80, 20));
		label.setDisplayedMnemonic('B');

		bezeichnung = new CharacterTextField(20);
		bezeichnung.setPreferredSize(new Dimension(50, 20));
		bezeichnung.setFocusAccelerator('B');

		zeile.add(label);
		zeile.add(bezeichnung);

		farbPane = new JPanel();
		farbPane.setPreferredSize(new Dimension(500, 2));
		farbPane.setMaximumSize(new Dimension(500, 2));

		typPane.add(zeile);
		typPane.add(Box.createVerticalStrut(5));
		typPane.add(farbPane);
		typPane.add(Box.createVerticalStrut(10));

		// Farbe // Farbe // Farbe // Farbe // Farbe // Farbe // Farbe // Farbe
		// // Farbe //
		farbSelect = new JColorChooser();
		farbSelect.setBorder(new TitledBorder("Farbe auswählen"));
		farbSelect.setSize(40, 40);
		farbSelect.setColor(Color.white);

		// Buttons // Buttons // Buttons // Buttons // Buttons // Buttons //
		// Buttons //
		ButtonPanel buttons = new ButtonPanel(true);
		buttons.addActionListener(this);

		gui.add("North", typPane);
		gui.add("Center", farbSelect);
		gui.add("South", buttons);
	}

	void fill(EintragsTyp t) {
		typ = t;

		bezeichnung.setText(typ.getBezeichnung());
		farbPane.setBackground(typ.getFarbe());
		farbSelect.setColor(typ.getFarbe());
	}

	void start(boolean admin) {
		if (!admin)
			bezeichnung.setEditable(false);

		if (typ != null) {
			dialog = new JDialog(parentFrame, "Eintragstyp bearbeiten", true);
		} else {
			dialog = new JDialog(parentFrame, "Eintragstyp neu anlegen", true);
		}

		dialog.setSize(500, 500);// 350,400
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add("Center", gui);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - 350) / 2,
				(screenSize.height - 400) / 2);
		dialog.show();
	}

	void close() {
		dialog.dispose();
	}

	EintragsTyp getTyp() {
		if (typ != null) {
			typ.setBezeichnung(bezeichnung.getText());
			typ.setFarbe(farbSelect.getColor());

		} else {
			typ = new EintragsTyp(farbSelect.getColor(), bezeichnung.getText());
		}

		return typ;
	}
}
