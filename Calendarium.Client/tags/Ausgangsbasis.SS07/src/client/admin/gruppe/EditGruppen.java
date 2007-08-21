package client.admin.gruppe; //
///////////////////////////////

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import basisklassen.Gruppe;
import client.utility.ListenerForActions;
import client.utility.darstellung.SelectTreeEntry;

////////////////////////////////////////////////////////////////////////////////////////////
// EditGruppen // EditGruppen // EditGruppen // EditGruppen // EditGruppen // EditGruppen //
////////////////////////////////////////////////////////////////////////////////////////////

public class EditGruppen extends ListenerForActions implements
		TreeSelectionListener {
	// graphical Representation
	private JInternalFrame gui = new JInternalFrame("Gruppen verwalten", true,
			true, false, true);

	// Liste zur Gruppenauswahl
	private SelectTreeEntry selectGruppe;

	// GruppenTree
	private DefaultTreeModel model;

	private DefaultMutableTreeNode root;

	// Buttons
	private JButton btn[] = new JButton[3];

	// Gruppendaten
	private Gruppe parentSel;

	private Gruppe sel;

	private TreePath parentSelPath;

	private TreePath selPath;

	// Admin
	private boolean checkAdmin = (!(data.Data.user.getKuerzel().equals("Admin")));

	public EditGruppen() {
		gui.setSize(400, 300);
		gui.setPreferredSize(gui.getSize());
		gui.getContentPane().setLayout(new BorderLayout());

		if (checkAdmin) {
			// InternalFrameListener
			gui.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosing(InternalFrameEvent e) {
					gui.setVisible(false);
				}
			});
		}

		selectGruppe = new SelectTreeEntry();
		selectGruppe.addTreeSelectionListener(this);

		model = (DefaultTreeModel) selectGruppe.getTree().getModel();
		root = (DefaultMutableTreeNode) model.getRoot();

		create();
	}

	void create() {
		Dimension size;

		JPanel editPane = new JPanel();
		editPane.setLayout(new BorderLayout(10, 0));
		editPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		btn[0] = new JButton("Gruppe hinzufuegen");
		size = btn[0].getMaximumSize();
		size.width = 10000;
		btn[0].setMaximumSize(size);
		btn[0].setMnemonic('h');
		btn[0].setActionCommand("add");
		btn[0].addActionListener(this);

		btn[1] = new JButton("Grupppe löschen");
		btn[1].setMaximumSize(size);
		btn[1].setMnemonic('l');
		btn[1].setEnabled(false);
		btn[1].setActionCommand("delete");
		btn[1].addActionListener(this);

		btn[2] = new JButton("Gruppe bearbeiten");
		btn[2].setMaximumSize(size);
		btn[2].setMnemonic('b');
		btn[2].setEnabled(false);
		btn[2].setActionCommand("edit");
		btn[2].addActionListener(this);

		buttons.add(Box.createVerticalStrut(60));
		buttons.add(btn[0]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[1]);
		buttons.add(Box.createVerticalStrut(5));
		buttons.add(btn[2]);
		buttons.add(Box.createVerticalGlue());

		editPane.add("Center", selectGruppe.getGUI());
		editPane.add("East", buttons);

		gui.getContentPane().add("Center", editPane);
	}

	JInternalFrame getGUI() {
		return gui;
	}

	void update() {
		root.removeAllChildren();
		selectGruppe.createModel();
		model.reload();
	}

	Gruppe getSelected() {
		return sel;
	}

	Gruppe getParentOfSelected() {
		return parentSel;
	}

	TreePath getSelectedPath() {
		return selPath;
	}

	TreePath getParentPathOfSelected() {
		return parentSelPath;
	}

	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	public void actionPerformed(ActionEvent e) {
		// Selected
		selPath = selectGruppe.gruppenTree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
				.getLastPathComponent();

		sel = (Gruppe) node.getUserObject();

		// Parent of Selected
		if (!sel.getKuerzel().equals("ROOT")) // != 0)
		{
			parentSelPath = new TreePath(((DefaultMutableTreeNode) node
					.getParent()).getPath());
			node = (DefaultMutableTreeNode) parentSelPath
					.getLastPathComponent();

			parentSel = (Gruppe) node.getUserObject();

		} else
			parentSel = null;

		// ListenerForActions
		super.actionPerformed(e);
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

				selectGruppe.name.setText(gruppe.getName());
				selectGruppe.kuerzel.setText(gruppe.getKuerzel());

				if ((!checkAdmin || !gruppe.getAdminFlag())
						&& gruppe.getID() != 0) {
					btn[1].setEnabled(true);
					btn[2].setEnabled(true);
				} else {
					btn[1].setEnabled(false);
					btn[2].setEnabled(false);
				}
			} else {
				btn[1].setEnabled(false);
				btn[2].setEnabled(false);
			}
		}
	}
}
