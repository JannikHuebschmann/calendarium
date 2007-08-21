package client.utility.darstellung; //
//////////////////////////////////////

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

////////////////////////////////////////////////////////////////////////////////////////////
// CustomTable // CustomTable // CustomTable // CustomTable // CustomTable // CustomTable //
////////////////////////////////////////////////////////////////////////////////////////////

public class CustomTable extends JTable {
	private static final long serialVersionUID = -814101869487262356L;

	public CustomTable(DefaultTableModel model) {
		super(model);
	}

	protected void processKeyEvent(KeyEvent e) {
		if (e.getModifiers() == 0) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
				super.processKeyEvent(e);
			}
		}
	}

	protected void processMouseEvent(MouseEvent e) {
		if (e.getClickCount() == 1) {
			super.processMouseEvent(e);
		}
	}
}
