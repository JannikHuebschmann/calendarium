package client.admin; //
////////////////////////

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import event.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
// Noticias // Noticias // Noticias // Noticias // Noticias // Noticias // Noticias // Noticias //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Noticias implements ActionListener, Observer, EventConstants {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Nachrichten", true, true,
			false, true);

	// Control
	private JTextArea textArea;

	private JButton button;

	public Noticias() {
		gui.setSize(400, 250);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		// InternalFrameListener
		gui.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				gui.setVisible(false);
			}
		});

		create();
	}

	private void create() {
		JPanel textPane = new JPanel();
		textPane.setLayout(new BorderLayout());
		textPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		textArea = new JTextArea();
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED));

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(SystemColor.window);

		textPane.add("Center", scrollPane);

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 10, 10, 10));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		button = new JButton("Löschen");
		button.setPreferredSize(new Dimension(100, 20));
		button.setEnabled(false);
		button.setMnemonic('L');
		button.setActionCommand("delete");
		button.addActionListener(this);

		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalGlue());

		gui.getContentPane().add("Center", textPane);
		gui.getContentPane().add("South", buttonPane);
	}

	public JInternalFrame getGUI() {
		return gui;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("delete")) {
			button.setEnabled(false);
			textArea.setText("");
		}
	}

	public void update(Observable obj, Object arg) {
		CalendariumEvent evt = (CalendariumEvent) arg;

		switch (evt.getEventID()) {
		case MESSAGE_EVT: // MessageEvent

			textArea.setText(textArea.getText() + '\n' + evt.getTimeStamp()
					+ '\n' + ((MessageEvent) evt).getMessage() + '\n');

			button.setEnabled(true);
			break;

		case NFKT_EVT: // NfktEvent

			textArea.setText(textArea.getText() + '\n' + evt.getTimeStamp()
					+ '\n' + ((NfktEvent) evt).getMessage() + '\n');

			button.setEnabled(true);
			break;
		}
	}
}
