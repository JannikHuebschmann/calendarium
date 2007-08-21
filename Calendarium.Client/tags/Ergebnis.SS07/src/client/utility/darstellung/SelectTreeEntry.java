package client.utility.darstellung; //
//////////////////////////////////////

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import basisklassen.Gruppe;
import client.gui.CharacterTextField;
import data.Data;

/////////////////////////////////////////////////////////////////////////////////////////////////
// SelectTreeEntry // SelectTreeEntry // SelectTreeEntry // SelectTreeEntry // SelectTreeEntry //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class SelectTreeEntry extends GruppenTree implements
		TreeSelectionListener, ActionListener {
	// graphical Representation
	private JPanel gui = new JPanel();

	// Controls
	public CharacterTextField name, kuerzel;

	// registrierte Listener
	private EventListenerList mTreeSelectionListenerList;

	public SelectTreeEntry() {
		// initialisieren
		mTreeSelectionListenerList = new EventListenerList();

		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		create();
	}

	void create() {
		JPanel enterGruppe = new JPanel();
		enterGruppe.setLayout(new BoxLayout(enterGruppe, BoxLayout.X_AXIS));

		JLabel l1 = new JLabel("Name:");
		l1.setPreferredSize(new Dimension(50, 20));
		l1.setDisplayedMnemonic('n');

		name = new CharacterTextField();
		name.setFocusAccelerator('n');
		name.addActionListener(this);
		name.addKeyListener(new KeyEventHandler());

		enterGruppe.add(l1);
		enterGruppe.add(name);

		JPanel enterKuerzel = new JPanel();
		enterKuerzel.setLayout(new BoxLayout(enterKuerzel, BoxLayout.X_AXIS));

		JLabel l2 = new JLabel("Kuerzel:");
		l2.setPreferredSize(new Dimension(50, 20));
		l2.setDisplayedMnemonic('k');

		kuerzel = new CharacterTextField();
		kuerzel.setFocusAccelerator('k');
		kuerzel.addActionListener(this);
		kuerzel.addKeyListener(new KeyEventHandler());

		enterKuerzel.add(l2);
		enterKuerzel.add(kuerzel);

		gruppenTree.addTreeSelectionListener(this);

		// inserted to avoid a java.lang.NullPointerException
		// changed by daniela esberger - 2005/03/31
		gruppenTree.setSelectionRow(0);

		JScrollPane scrollpane = new JScrollPane(gruppenTree);
		scrollpane.setPreferredSize(new Dimension(100, 70));

		gui.add(enterGruppe);
		gui.add(Box.createRigidArea(new Dimension(0, 10)));
		gui.add(enterKuerzel);
		gui.add(Box.createRigidArea(new Dimension(0, 10)));
		gui.add(scrollpane);
	}

	public JPanel getGUI() {
		return gui;
	}

	public JTree getTree() {
		return gruppenTree;
	}

	void updateTreeSelection(boolean checkName) {
		if (checkName) {
			String n = name.getText().toUpperCase();
			String kuerzelOld = "";
			int len = n.length();
			int count = 0;

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) gruppenTree
					.getModel().getRoot();
			DefaultMutableTreeNode ergNode = null;

			Enumeration e = node.breadthFirstEnumeration();
			while (e.hasMoreElements()) {
				node = (DefaultMutableTreeNode) e.nextElement();
				if (node.getUserObject().getClass() == Gruppe.class) {
					Gruppe gruppe = (Gruppe) node.getUserObject();

					String grname = gruppe.getName();
					String kuerzel = gruppe.getKuerzel();
					int lenGruppe = grname.length();

					if (lenGruppe <= len) {
						grname = grname.toUpperCase();
					} else {
						grname = grname.substring(0, len).toUpperCase();
					}

					if (n.compareTo(grname) == 0 && kuerzel != kuerzelOld) {
						ergNode = node;

						if (lenGruppe == len) {
							count = 1;
							break;

						} else {
							if (count++ > 1)
								break;
							kuerzelOld = kuerzel;
						}
					}
				}
			}

			if (count == 1) {
				TreePath path = new TreePath(ergNode.getPath());
				gruppenTree.setSelectionPath(path);
				gruppenTree.expandPath(path);
				gruppenTree.scrollPathToVisible(path);
			}

		} else {
			String k = kuerzel.getText().toUpperCase();
			if (Data.gruppen.contains(k)) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) gruppenTree
						.getModel().getRoot();

				Enumeration e = node.breadthFirstEnumeration();
				while (e.hasMoreElements()) {
					node = (DefaultMutableTreeNode) e.nextElement();
					if (node.getUserObject().getClass() == Gruppe.class) {
						Gruppe gruppe = (Gruppe) node.getUserObject();

						if (k.compareTo(gruppe.getKuerzel()) == 0) {
							TreePath path = new TreePath(node.getPath());
							gruppenTree.setSelectionPath(path);
							gruppenTree.expandPath(path);
							gruppenTree.scrollPathToVisible(path);

							break;
						}
					}
				}
			}
		}
	}

	// Register TreeSelectionListener
	public void addTreeSelectionListener(TreeSelectionListener listener) {
		mTreeSelectionListenerList.add(TreeSelectionListener.class, listener);
	}

	// Un-register TreeSelectionListener
	public void removeTreeSelectionListener(TreeSelectionListener listener) {
		mTreeSelectionListenerList
				.remove(TreeSelectionListener.class, listener);
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
	}

	protected void fireTreeSelectionEvent(TreeSelectionEvent newEvent) {
		Object[] listeners = mTreeSelectionListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeSelectionListener.class) {
				((TreeSelectionListener) listeners[i + 1])
						.valueChanged(newEvent);
			}
		}
	}

	// TreeSelectionListener // TreeSelectionListener // TreeSelectionListener
	// // TreeSelectionListener //
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();

		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			if (node.getUserObject().getClass() == Gruppe.class) {
				Gruppe gruppe = (Gruppe) node.getUserObject();

				name.setText(gruppe.getName());
				kuerzel.setText(gruppe.getKuerzel());
			}

			fireTreeSelectionEvent(e);
		}
	}

	// KeyEventListener // KeyEventListener // KeyEventListener //
	// KeyEventListener // KeyEventListener //
	class KeyEventHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == name) {
				updateTreeSelection(true);
			} else {
				updateTreeSelection(false);
			}
		}
	}
}
