package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Zahlenfeld // Zahlenfeld // Zahlenfeld // Zahlenfeld // Zahlenfeld // Zahlenfeld // Zahlenfeld //
////////////////////////////////////////////////////////////////////////////////////////////////////

public class Zahlenfeld extends JTextField implements ActionListener {
	private static final long serialVersionUID = -7993738184182610826L;

	private int vorKomma, nachKomma;

	private char komma = ',';

	private EventListenerList mActionListenerList;

	public Zahlenfeld() {
		this.vorKomma = 0;
		this.nachKomma = 0;
	}

	public Zahlenfeld(int vorKomma) {
		this.vorKomma = Math.max(0, vorKomma);
		this.nachKomma = 0;

		setColumns(vorKomma);
		mActionListenerList = new EventListenerList();
	}

	public Zahlenfeld(int vorKomma, String text) {
		this.vorKomma = Math.max(0, vorKomma);
		this.nachKomma = 0;

		setColumns(vorKomma);
		mActionListenerList = new EventListenerList();
		setText(text);
	}

	public Zahlenfeld(int vorKomma, int nachKomma) {
		this.vorKomma = Math.max(0, vorKomma);
		this.nachKomma = Math.max(0, nachKomma);

		setColumns(vorKomma + nachKomma + 1);
		mActionListenerList = new EventListenerList();
	}

	public void setValue(double wert) {
		int vor = 0, nach = 0;

		nach = Math.round((float) ((wert - (int) wert) * Math
				.pow(10, nachKomma)));

		if (nach == Math.pow(10, nachKomma)) {
			nach = 0;
			vor++;
		}

		vor += (int) wert;

		if (nach > 0) {
			setText(String.valueOf(vor) + komma + String.valueOf(nach));
		} else {
			setText(String.valueOf(vor));
		}
	}

	public double getValue() {
		if (getText().length() > 0) {
			return Double.valueOf(getText().replace(komma, '.')).doubleValue();
		} else {
			return 0;
		}
	}

	protected void processKeyEvent(KeyEvent e) {
		int size;
		int kommaPos = -1;

		if (nachKomma > 0) {
			kommaPos = getText().indexOf(komma);

			if (kommaPos >= 0) {
				size = kommaPos + nachKomma + 1;
			} else {
				size = vorKomma;
			}

		} else
			size = vorKomma;

		if (e.getModifiers() == 0) {
			int keyChar = e.getKeyChar();
			int keyCode = e.getKeyCode();

			if (keyChar >= '0' && keyChar <= '9'
					&& (size == 0 || getText().length() < size)
					|| nachKomma > 0 && kommaPos < 0 && keyChar == komma
					|| keyChar == '\n' || keyChar == '\t'
					|| keyCode == KeyEvent.VK_BACK_SPACE
					|| keyCode == KeyEvent.VK_DELETE
					|| keyCode == KeyEvent.VK_RIGHT
					|| keyCode == KeyEvent.VK_LEFT
					|| keyCode == KeyEvent.VK_END) {
				if ((keyCode == KeyEvent.VK_BACK_SPACE && getText().length() == 0)
						|| (keyCode == KeyEvent.VK_LEFT && getCaretPosition() == 0))

				{
					fireActionEvent(new ActionEvent(this, e.getID(),
							"PREVIOUS_CONTROL"));
				} else {
					super.processKeyEvent(e);
				}

			} else {
				if ((keyChar >= '0' && keyChar <= '9')
						|| (keyCode == KeyEvent.VK_RIGHT && getCaretPosition() >= size)) {
					try {
						if (getSelectedText() == null) {
							fireActionEvent(new ActionEvent(this, e.getID(),
									"NEXT_CONTROL"));
						} else {
							super.processKeyEvent(e);
						}
					} catch (java.lang.IllegalArgumentException ex) {
					}
				}
			}
		}
	}

	public void addActionListener(ActionListener listener) {
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener) {
		mActionListenerList.remove(ActionListener.class, listener);
	}

	protected void fireActionEvent(ActionEvent e) {
		Object[] listeners = mActionListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		fireActionEvent(e);
	}
}
