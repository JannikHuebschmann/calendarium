package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import javax.swing.*;
import java.awt.event.*;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CharacterTextField // CharacterTextField // CharacterTextField // CharacterTextField // CharacterTextField //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class CharacterTextField extends JTextField {
	private static final long serialVersionUID = 3082654525527084296L;
	int size = 0;

	public CharacterTextField() {
		size = 0;
	}

	public CharacterTextField(int s) {
		size = s;
	}

	protected void processKeyEvent(KeyEvent e) {
		int mods = e.getModifiers();

		if (mods == 0) {
			int keyChar = e.getKeyChar();
			int keyCode = e.getKeyCode();

			if ((size == 0 || getText().length() < size) && keyChar >= 'A'
					&& keyChar <= 'Z' || keyChar >= 'a' && keyChar <= 'z'
					|| keyChar == 'Ä' || keyChar == 'ä' || keyChar == 'Ö'
					|| keyChar == 'ö' || keyChar == 'Ü' || keyChar == 'ü'
					|| keyChar == 'ß' || keyChar == '-' || keyChar == '\n'
					|| keyChar == ' ' || keyCode == KeyEvent.VK_BACK_SPACE
					|| keyCode == KeyEvent.VK_DELETE
					|| keyCode == KeyEvent.VK_RIGHT
					|| keyCode == KeyEvent.VK_LEFT
					|| keyCode == KeyEvent.VK_END) {
				super.processKeyEvent(e);
			}
		} else if ((mods & KeyEvent.SHIFT_MASK) != 0) {
			int keyChar = e.getKeyChar();
			if (keyChar >= 'A' && keyChar <= 'Z' || keyChar == 'Ä'
					|| keyChar == 'Ö' || keyChar == 'Ü') {
				super.processKeyEvent(e);
			}
		} else if ((mods & KeyEvent.ALT_MASK) != 0) {
			super.processKeyEvent(e);
		}
	}
}
