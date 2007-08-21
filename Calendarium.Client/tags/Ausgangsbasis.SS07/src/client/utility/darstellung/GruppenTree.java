package client.utility.darstellung; //
//////////////////////////////////////

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.util.*;

import data.Data;
import basisklassen.Gruppe;
import basisklassen.Person;

////////////////////////////////////////////////////////////////////////////////////////////
// GruppenTree // GruppenTree // GruppenTree // GruppenTree // GruppenTree // GruppenTree //
////////////////////////////////////////////////////////////////////////////////////////////

public class GruppenTree {
	@SuppressWarnings("unused")
	private CellRenderer treeRenderer;

	public JTree gruppenTree = new JTree();

	public GruppenTree() {
		createModel();
		gruppenTree.setCellRenderer(treeRenderer = new CellRenderer());

		if (!(Data.user.getKuerzel().equals("Admin"))) {
			gruppenTree.setRootVisible(false);
		}
	}

	public void createModel() {
		// Root node
		Gruppe rootGroup = Data.gruppen.getAdminRoot(); // new
		// Gruppe(true,"AG","Admin-Gruppen");

		if (!(Data.user.getKuerzel().equals("Admin")))
			rootGroup.addGruppe(Data.gruppen.getUserRoot());

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootGroup);
		expandTree(root, rootGroup);

		gruppenTree.setModel(new DefaultTreeModel(root));

	}

	private void expandTree(DefaultMutableTreeNode parent, Gruppe gruppe) {
		Hashtable childs = gruppe.getGruppen();

		if (childs != null) {
			Enumeration e = childs.elements();
			while (e.hasMoreElements()) {
				Gruppe child = (Gruppe) e.nextElement();

				// Node
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
				parent.add(node);

				expandTree(node, child);

				// Personen
				Hashtable personen = child.getPersonen();

				if (personen != null) {
					Enumeration enumer = personen.elements();
					while (enumer.hasMoreElements()) {
						Person person = (Person) enumer.nextElement();

						DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(
								person);
						node.add(leaf);
					}
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// CellRenderer // CellRenderer // CellRenderer // CellRenderer //
	// CellRenderer // CellRenderer //
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	private class CellRenderer extends JLabel implements TreeCellRenderer {
		private static final long serialVersionUID = -9129859688697279991L;

		private Icon mLeafIcon;

		private Icon mOpenIcon;

		private Icon mClosedIcon;

		private Color textFarbe;

		public CellRenderer() {
			mLeafIcon = client.Client.loadImageIcon("mann.gif");
			mOpenIcon = (Icon) UIManager.get("Tree.openIcon");
			mClosedIcon = (Icon) UIManager.get("Tree.closedIcon");
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			textFarbe = SystemColor.textText;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

			if (node.getUserObject().getClass() == Gruppe.class) {
				Gruppe gruppe = (Gruppe) node.getUserObject();

				// vom Administrator angelegt?
				if (gruppe.getAdminFlag()) {
					textFarbe = Color.blue;
				}

				if (!leaf) {
					setText(gruppe.getName() + " (" + gruppe.getKuerzel() + ")");
				} else {
					setText(gruppe.getName() + " (" + gruppe.getKuerzel()
							+ "): leer");
				}

				if (expanded)
					setIcon(mOpenIcon);
				else
					setIcon(mClosedIcon);

				if (selected) {
					setForeground(SystemColor.textHighlightText);
					setBackground(SystemColor.textHighlight);
				} else {
					setForeground(textFarbe);
					setBackground(SystemColor.window);
				}

			} else {
				Person person = (Person) node.getUserObject();

				setText(person.getNameLang());
				setIcon(mLeafIcon);
				setForeground(textFarbe);
				setBackground(SystemColor.window);
			}

			return this;
		}

		public void paint(Graphics g) {
			int textstart, textwidth;

			textstart = getIcon().getIconWidth() + getIconTextGap();
			textwidth = getSize().width - textstart;

			getIcon().paintIcon(this, g, 0, 0);

			g.setColor(getBackground());
			g.fillRect(textstart, 0, textwidth, getSize().height);
			g.setColor(getForeground());
			g.drawString(getText(), textstart, g.getFontMetrics().getAscent());
		}
	}
}
