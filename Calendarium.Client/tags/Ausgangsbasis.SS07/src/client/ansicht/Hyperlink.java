package client.ansicht; //
//////////////////////////

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/////////////////////////////////////////////////////////////////////////////////////////////
// Hyperlink // Hyperlink // Hyperlink // Hyperlink // Hyperlink // Hyperlink // Hyperlink //
/////////////////////////////////////////////////////////////////////////////////////////////

public class Hyperlink implements ActionListener {
	// graphical Representation
	JInternalFrame gui = new JInternalFrame("Hyperlink", true, true, false,
			true);

	// Link
	private String link;

	// Controls
	private TextField url;

	private BrowserPane browserPane;

	public Hyperlink(String link) {
		this.link = link;

		gui.setSize(600, 600);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		create();
	}

	private void create() {
		url = new TextField(link);
		url.addActionListener(this);

		browserPane = new BrowserPane();
		browserPane.addActionListener(this);

		try {
			browserPane.setPage(link);
		} catch (IOException ex) {
		}

		JScrollPane scrollPane = new JScrollPane(browserPane);

		gui.getContentPane().add("North", url);
		gui.getContentPane().add("Center", scrollPane);
	}

	JInternalFrame getGUI() {
		return gui;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browserPane) {
			url.setText(e.getActionCommand());

		} else {
			try {
				browserPane.setPage(url.getText());
			} catch (IOException ex) {
			}
		}
	}

	class BrowserPane extends JEditorPane implements HyperlinkListener {
		private static final long serialVersionUID = 2614502272641987942L;
		// Events
		private EventListenerList mActionListenerList;

		BrowserPane() {
			super();
			addHyperlinkListener(this);

			mActionListenerList = new EventListenerList();
		}

		public void setPage(URL page) throws IOException {
			super.setPage(page);
			fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
					page.toString()));
		}

		// ActionListener // ActionListener // ActionListener // ActionListener
		// //
		public void addActionListener(ActionListener listener) {
			mActionListenerList.add(ActionListener.class, listener);
		}

		public void removeActionListener(ActionListener listener) {
			mActionListenerList.remove(ActionListener.class, listener);
		}

		protected void fireActionEvent(ActionEvent evt) {
			if (mActionListenerList == null) {
				return;
			}

			Object[] listeners = mActionListenerList.getListenerList();

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == ActionListener.class) {
					((ActionListener) listeners[i + 1]).actionPerformed(evt);
				}
			}
		}

		// HyperlinkListener // HyperlinkListener // HyperlinkListener //
		// HyperlinkListener //
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				try {
					setPage(e.getURL());

				} catch (IOException ex) {
				}
			}
		}
	}
}
