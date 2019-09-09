package swtkal.swing.elements.termin;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.tree.*;
//import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;

//import java.util.*;

//import swtkal.domain.*;
//import swtkal.swing.elements.*;

/////////////////////////////////////////////////////////////////////////////////////////////////
// TeilnehmerPanel // TeilnehmerPanel // TeilnehmerPanel // TeilnehmerPanel // TeilnehmerPanel //
/////////////////////////////////////////////////////////////////////////////////////////////////

public class TeilnehmerPanel // extends BasicSelection
// BasicSelection noch integrieren
{
	// Teilnehmer
//	private Vector teilnehmerListe = null;

	// parentFrame
//	private JFrame parentFrame;

	// graphical Representation
	private JPanel gui = new JPanel();

	// Checkbox
//	private boolean chkState[][] = { { true, true }, { true, true },
//			{ true, true } };
//
//	// Columns
//	private boolean column[] = { true, true, true };
//
//	// ColumnModel
//	private DefaultTableColumnModel gruppenColumnModel;
//
//	private DefaultTableColumnModel personenColumnModel;
//
//	private TableColumn gruppenCol[] = new TableColumn[3];
//
//	private TableColumn personenCol[] = new TableColumn[3];
//
//	// Buttons
//	private ButtonPanel buttons;
//
//	// Recht
//	private Recht recht = new Recht(EINTRAGSRECHT);
//
//	// No Rights
//	private Vector failedGruppen = null;
//
//	private Vector failedPersonen = null;
//
	// Events
	private EventListenerList mActionListenerList;

	public TeilnehmerPanel(JFrame f)
	{
//		super(204, new String[] { "Gruppen/Benachrichtigungen",
//				"Personen/Benachrichtigungen" });
//
//		parentFrame = f;
		mActionListenerList = new EventListenerList();

//		adjustTable();
		create();
	}

	void create()
	{
		gui.setLayout(new BorderLayout());
		
		JLabel label = new JLabel("Teilnehmerauswahl derzeit ungenutzt");
		label.setEnabled(false);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.setBorder(new EmptyBorder(0, 12, 0, 12));

//		buttons = new ButtonPanel(false);
//		buttons.addActionListener(this);
//
//		bottom.add(Box.createHorizontalGlue());
//		bottom.add(buttons);
//
//		gui.add("Center", super.getGUI());
		gui.add("Center", label);
		gui.add("South", bottom);
	}

	public JPanel getGUI()
	{
		return gui;
	}

//	void adjustTable()
//	{
//		TableColumn col;
//
//		// Header
//		EmptyRenderer emptyRend = new EmptyRenderer();
//		ButtonIconRenderer buttonRend = new ButtonIconRenderer();
//
//		// Columns
//		GruppenCellRenderer groupRend = new GruppenCellRenderer(Gruppe.class,
//				this);
//		GruppenCellRenderer persRend = new GruppenCellRenderer(Person.class, this);
//
//		gruppenTableModel.setColumnIdentifiers(new String[] { "Kuerzel", "Name",
//				"1", "2", "3" });
//		col = gruppenTable.getColumn("Kuerzel");
//		col.setHeaderRenderer(emptyRend);
//		col.setCellRenderer(groupRend);
//		col.setMaxWidth(26);
//
//		col = gruppenTable.getColumn("Name");
//		col.setHeaderRenderer(emptyRend);
//		col.setCellRenderer(groupRend);
//		col.setMaxWidth(105);
//
//		personenTableModel.setColumnIdentifiers(new String[] { "Kuerzel", "Name",
//				"1", "2", "3" });
//		col = personenTable.getColumn("Kuerzel");
//		col.setHeaderRenderer(emptyRend);
//		col.setCellRenderer(persRend);
//		col.setMaxWidth(26);
//
//		col = personenTable.getColumn("Name");
//		col.setHeaderRenderer(emptyRend);
//		col.setCellRenderer(persRend);
//		col.setMaxWidth(105);
//
//		// CellRenderer
//		CheckBoxRenderer cellRenderer = new CheckBoxRenderer(this);
//
//		// CellEditor
//		JCheckBox check = new JCheckBox();
//		check.setHorizontalAlignment(JCheckBox.CENTER);
//		DefaultCellEditor cellEditor = new DefaultCellEditor(check);
//
//		// Benachrichtigung
//		for (int i = 0; i < 3; i++)
//		{
//			// Gruppen
//			gruppenCol[i] = gruppenTable.getColumn(String.valueOf(i + 1));
//			gruppenCol[i].setMaxWidth(21);
//			gruppenCol[i].setCellRenderer(cellRenderer);
//			gruppenCol[i].setCellEditor(cellEditor);
//			gruppenCol[i].setHeaderRenderer(buttonRend);
//
//			// Personen
//			personenCol[i] = personenTable.getColumn(String.valueOf(i + 1));
//			personenCol[i].setMaxWidth(21);
//			personenCol[i].setCellRenderer(cellRenderer);
//			personenCol[i].setCellEditor(cellEditor);
//			personenCol[i].setHeaderRenderer(buttonRend);
//		}
//
//		// kein Reordering
//		JTableHeader header;
//
//		// Gruppen
//		header = gruppenTable.getTableHeader();
//		header.setReorderingAllowed(false);
//		header.setBorder(new CustomBorder(SystemColor.controlShadow, false,
//				false, true, false));
//		header.addMouseListener(new MouseEventHandler(0));
//
//		// Personen
//		header = personenTable.getTableHeader();
//		header.setReorderingAllowed(false);
//		header.setBorder(new CustomBorder(SystemColor.controlShadow, false,
//				false, true, false));
//		header.addMouseListener(new MouseEventHandler(1));
//
//		gruppenColumnModel = (DefaultTableColumnModel) gruppenTable
//				.getColumnModel();
//		personenColumnModel = (DefaultTableColumnModel) personenTable
//				.getColumnModel();
//	}

//	void fill(Vector tn)
//	{
//		teilnehmerListe = tn;
//
//		if (teilnehmerListe != null)
//		{
//			Enumeration e = teilnehmerListe.elements();
//			while (e.hasMoreElements())
//			{
//				Teilnehmer teilnehmer = (Teilnehmer) e.nextElement();
//				if (teilnehmer.getTeilnehmer().getClass() == Gruppe.class)
//				{
//					Gruppe gruppe = (Gruppe) teilnehmer.getTeilnehmer();
//					addGruppe(gruppe, teilnehmer.getNotifikationen(), true);
//
//				}
//				else
//				{
//					Person person = (Person) teilnehmer.getTeilnehmer();
//					addPerson(person, teilnehmer.getNotifikationen(), true);
//				}
//			}
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	int isBerechtigt(Gruppe gruppe)
//	{
//		Vector personen = new Vector();
//		getMissingBy(gruppe, personen);
//
//		if (personen.size() > 0)
//		{
//			Object[] options = { "Ja", "Nein" };
//
//			char c = 34;
//			String text, msg = "Sie haben keine Eintragsberechtigung fuer Einträge vom Typ "
//					+ c + recht.getEintragsTyp().getBezeichnung() + c + " bei:\n\n";
//			int len = msg.length() - 2;
//
//			Enumeration e = personen.elements();
//			Person p;
//			while (e.hasMoreElements())
//			{
//				p = (Person) e.nextElement();
//				msg += "    " + p.getNameLang() + '\n';
//
//				if (failedPersonen == null)
//					failedPersonen = new Vector();
//				if (!failedPersonen.contains(p))
//					failedPersonen.addElement(p);
//			}
//
//			text = "Wollen Sie die Gruppe " + c + gruppe.getName() + c
//					+ " trotzdem hinzufuegen?";
//			msg += '\n' + text;
//
//			JTextArea area = new JTextArea(msg);
//			area.setBackground(SystemColor.control);
//			area.setEditable(false);
//			area.setColumns(Math.max(len, text.length()));
//			area.setRows(personen.size() + 3);
//
//			JOptionPane optionPane = new JOptionPane(area,
//					JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, null,
//					options, options[0]);
//
//			JDialog dialogPane = optionPane.createDialog(parentFrame,
//					"Fehlende Rechte");
//
//			dialogPane.show();
//			dialogPane.dispose();
//
//			Object selectedValue = optionPane.getValue();
//
//			if (selectedValue != null)
//			{ // Trotzdem hinzufuegen
//				if (options[0].equals(selectedValue))
//				{
//					if (failedGruppen == null)
//						failedGruppen = new Vector();
//					failedGruppen.addElement(gruppe);
//
//					return -1;
//				}
//			}
//			// nicht hinzufuegen
//			return 0;
//
//		}
//		else
//			return 1;
//	}
//
//	@SuppressWarnings("unchecked")
//	void getMissingBy(Gruppe gruppe, Vector personen)
//	{
//		Person p;
//
//		Enumeration e = gruppe.getPersonen().elements();
//		while (e.hasMoreElements())
//		{
//			p = (Person) e.nextElement();
//			if (!Data.rechte.isBerechtigt(p, recht))
//			{
//				personen.addElement(p);
//			}
//		}
//
//		e = gruppe.getGruppen().elements();
//		while (e.hasMoreElements())
//		{
//			getMissingBy((Gruppe) e.nextElement(), personen);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	int isBerechtigt(Person person)
//	{
//		if (!Data.rechte.isBerechtigt(person, recht))
//		{
//			Object[] options = { "Ja", "Nein" };
//
//			char c = 34;
//			String text, msg = "Sie haben keine Eintragsberechtigung fuer Einträge vom Typ "
//					+ c + recht.getEintragsTyp().getBezeichnung() + c + " bei:";
//			int len = msg.length();
//
//			msg += "\n\n    " + person.getNameLang() + '\n';
//
//			text = "Wollen Sie diesen Teilnehmer trotzdem hinzufuegen?";
//			msg += '\n' + text;
//
//			JTextArea area = new JTextArea(msg);
//			area.setBackground(SystemColor.control);
//			area.setEditable(false);
//			area.setColumns(Math.max(len, text.length()));
//			area.setRows(4);
//
//			JOptionPane optionPane = new JOptionPane(area,
//					JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, null,
//					options, options[0]);
//
//			JDialog dialogPane = optionPane.createDialog(parentFrame,
//					"Fehlende Rechte");
//
//			dialogPane.show();
//			dialogPane.dispose();
//
//			Object selectedValue = optionPane.getValue();
//
//			if (selectedValue != null)
//			{ // Trotzdem hinzufuegen
//				if (options[0].equals(selectedValue))
//				{
//					if (failedPersonen == null)
//						failedPersonen = new Vector();
//					failedPersonen.addElement(person);
//
//					return -1;
//				}
//			}
//			// nicht hinzufuegen
//			return 0;
//
//		}
//		else
//			return 1;
//	}
//
//	void addGruppe(TreePath path)
//	{
//		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
//				.getLastPathComponent();
//		Gruppe gruppe = (Gruppe) node.getUserObject();
//
//		int erg = isBerechtigt(gruppe);
//
//		if (erg == 1)
//		{
//			addGruppe(gruppe, null, true);
//		}
//		else if (erg == -1)
//		{
//			addGruppe(gruppe, null, false);
//		}
//	}
//
//	void addGruppe(Gruppe gruppe, boolean[] nfkt, boolean isBerechtigt)
//	{
//		Gruppe g;
//
//		String kuerzel = gruppe.getKuerzel();
//		String name = gruppe.getName();
//
//		int count = gruppenTableModel.getRowCount();
//		int i;
//
//		// Sortiert einfuegen
//		for (i = 0; i < count; i++)
//		{
//			g = (Gruppe) gruppenTableModel.getValueAt(i, 0);
//
//			if (kuerzel.compareTo(g.getKuerzel()) != 0)
//			{
//				if (name.compareTo(g.getName()) < 0)
//					break;
//
//			}
//			else
//			{
//				i = -1;
//				break;
//			}
//		}
//
//		if (i >= 0)
//		{
//			count = gruppenColumnModel.getColumnCount();
//			Object[] data = new Object[count];
//
//			data[0] = gruppe;
//			data[1] = gruppe;
//
//			if (nfkt == null)
//			{
//				for (int j = 2; j < count; j++)
//				{
//					if (isBerechtigt)
//						data[j] = new Boolean(true);
//					else
//						data[j] = null;
//				}
//
//			}
//			else
//			{
//				for (int j = 0; j < nfkt.length; j++)
//				{
//					data[j + 2] = new Boolean(nfkt[j]);
//				}
//			}
//
//			gruppenTableModel.insertRow(i, data);
//		}
//	}
//
//	void removeGruppe(int rowIndex)
//	{
//		if (failedGruppen != null)
//		{
//			failedGruppen.removeElement((Gruppe) gruppenTable.getValueAt(rowIndex,
//					0));
//		}
//
//		gruppenTableModel.removeRow(rowIndex);
//	}
//
//	void addPerson(int index)
//	{
//		Person person = (Person) selectPerson.mModel.get(index);
//
//		int erg = isBerechtigt(person);
//
//		if (erg == 1)
//		{
//			addPerson(person, null, true);
//		}
//		else if (erg == -1)
//		{
//			addPerson(person, null, false);
//		}
//	}
//
//	void addPerson(Person person, boolean[] nfkt, boolean isBerechtigt)
//	{
//		Person p;
//
//		String kuerzel = person.getKuerzel();
//		String name = person.getNameLang();
//
//		int count = personenTableModel.getRowCount();
//		int i;
//
//		// Sortiert einfuegen
//		for (i = 0; i < count; i++)
//		{
//			p = (Person) personenTableModel.getValueAt(i, 0);
//
//			if (kuerzel.compareTo(p.getKuerzel()) != 0)
//			{
//				if (name.compareTo(p.getNameLang()) < 0)
//					break;
//
//			}
//			else
//			{
//				i = -1;
//				break;
//			}
//		}
//
//		if (i >= 0)
//		{
//			count = personenColumnModel.getColumnCount();
//			Object[] data = new Object[count];
//
//			data[0] = person;
//			data[1] = person;
//
//			if (nfkt == null)
//			{
//				for (int j = 2; j < count; j++)
//				{
//					if (isBerechtigt)
//						data[j] = new Boolean(true);
//					else
//						data[j] = null;
//				}
//
//			}
//			else
//			{
//				for (int j = 0; j < nfkt.length; j++)
//				{
//					data[j + 2] = new Boolean(nfkt[j]);
//				}
//			}
//
//			personenTableModel.insertRow(i, data);
//		}
//	}
//
//	void removePerson(int rowIndex)
//	{
//		if (failedPersonen != null)
//		{
//			failedPersonen.removeElement((Person) personenTable.getValueAt(
//					rowIndex, 0));
//		}
//
//		personenTableModel.removeRow(rowIndex);
//	}
//
//	void setCheckboxes(int column, int typ)
//	{
//		chkState[column - 2][typ] = !chkState[column - 2][typ];
//
//		if (typ == 0)
//		{
//			int count = gruppenTableModel.getRowCount();
//			for (int i = 0; i < count; i++)
//			{
//				gruppenTableModel.setValueAt(
//						new Boolean(chkState[column - 2][typ]), i, column);
//			}
//
//		}
//		else
//		{
//			int count = personenTableModel.getRowCount();
//			for (int i = 0; i < count; i++)
//			{
//				personenTableModel.setValueAt(
//						new Boolean(chkState[column - 2][typ]), i, column);
//			}
//		}
//	}
//
//	void setEintragsTyp(EintragsTyp t)
//	{
//		recht.setEintragsTyp(t);
//
//		failedGruppen = null;
//		failedPersonen = null;
//	}
//
//	public boolean hasFailed(Gruppe g)
//	{
//		if (failedGruppen != null)
//		{
//			return failedGruppen.contains(g);
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	public boolean hasFailed(Person p)
//	{
//		if (failedPersonen != null)
//		{
//			return failedPersonen.contains(p);
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	void updateColumns(boolean[] nfkt)
//	{
//		for (int i = 0; i < nfkt.length; i++)
//		{
//			if (nfkt[i])
//			{
//				if (!column[i])
//				{
//					TableColumn col;
//
//					col = gruppenTable.getColumn("Name");
//					col.setMaxWidth(col.getMaxWidth() - 22);
//
//					col = personenTable.getColumn("Name");
//					col.setMaxWidth(col.getMaxWidth() - 22);
//
//					gruppenColumnModel.addColumn(gruppenCol[i]);
//					personenColumnModel.addColumn(personenCol[i]);
//					column[i] = true;
//
//					int count = gruppenTableModel.getRowCount();
//					for (int j = 0; j < count; j++)
//					{
//						gruppenTableModel.setValueAt(new Boolean(true), j, i + 2);
//					}
//
//					count = personenTableModel.getRowCount();
//					for (int j = 0; j < count; j++)
//					{
//						personenTableModel.setValueAt(new Boolean(true), j, i + 2);
//					}
//				}
//
//			}
//			else
//			{
//				if (column[i])
//				{
//					TableColumn col;
//
//					col = gruppenTable.getColumn("Name");
//					col.setMaxWidth(col.getMaxWidth() + 22);
//
//					col = personenTable.getColumn("Name");
//					col.setMaxWidth(col.getMaxWidth() + 22);
//
//					gruppenColumnModel.removeColumn(gruppenCol[i]);
//					personenColumnModel.removeColumn(personenCol[i]);
//					column[i] = false;
//				}
//			}
//		}
//	}
//
//	/*
//	 * void getAttribute() { int i, j, rows, cols;
//	 * 
//	 * Gruppe g; Person p;
//	 * 
//	 * if(teilnehmerListe != null) teilnehmerListe.removeAllElements();
//	 * 
//	 * rows = gruppenTableModel.getRowCount(); cols =
//	 * gruppenColumnModel.getColumnCount();
//	 * 
//	 * for(i = 0; i < rows; i++) { boolean nfkt[] = new boolean[cols - 2];
//	 * 
//	 * for(j = 2; j < cols; j++) { nfkt[j - 2] = ((Boolean)
//	 * gruppenTableModel.getValueAt(i, j)).booleanValue(); }
//	 * 
//	 * g = (Gruppe) gruppenTableModel.getValueAt(i, 0);
//	 * 
//	 * if(!hasFailed(g)) { teilnehmerListe.addElement(new Teilnehmer(g, nfkt)); } }
//	 * 
//	 * rows = personenTableModel.getRowCount();
//	 * 
//	 * for(i = 0; i < rows; i++) { boolean nfkt[] = new boolean[cols - 2];
//	 * 
//	 * for(j = 2; j < cols; j++) { nfkt[j - 2] = ((Boolean)
//	 * personenTableModel.getValueAt(i, j)).booleanValue(); }
//	 * 
//	 * p = (Person) personenTableModel.getValueAt(i, 0);
//	 * 
//	 * if(!hasFailed(p)) { teilnehmerListe.addElement(new Teilnehmer(p, nfkt)); } } }
//	 */
//
//	@SuppressWarnings("unchecked")
//	void getAttribute()
//	{
//		int i, j, rows, cols;
//
//		Gruppe g;
//		Person p;
//
//		if (teilnehmerListe != null)
//			teilnehmerListe.removeAllElements();
//
//		rows = gruppenTableModel.getRowCount();
//		cols = gruppenColumnModel.getColumnCount();
//
//		for (i = 0; i < rows; i++)
//		{
//			boolean nfkt[] = new boolean[cols - 2];
//			int count = 0;
//
//			for (j = 2; j < 5; j++)
//			{
//				if (column[j - 2])
//				{
//					nfkt[count] = ((Boolean) gruppenTableModel.getValueAt(i, j))
//							.booleanValue();
//					count++;
//				}
//			}
//
//			g = (Gruppe) gruppenTableModel.getValueAt(i, 0);
//
//			if (!hasFailed(g))
//			{
//				teilnehmerListe.addElement(new Teilnehmer(g, nfkt));
//			}
//		}
//
//		rows = personenTableModel.getRowCount();
//
//		for (i = 0; i < rows; i++)
//		{
//			boolean nfkt[] = new boolean[cols - 2];
//			int count = 0;
//
//			for (j = 2; j < 5; j++)
//			{
//				if (column[j - 2])
//				{
//					nfkt[count] = ((Boolean) personenTableModel.getValueAt(i, j))
//							.booleanValue();
//					count++;
//				}
//			}
//
//			p = (Person) personenTableModel.getValueAt(i, 0);
//
//			if (!hasFailed(p))
//			{
//				teilnehmerListe.addElement(new Teilnehmer(p, nfkt));
//			}
//		}
//	}
//
//	Rectangle getRectOK()
//	{
//		return buttons.getScreenLocation(0);
//	}
//
//	Rectangle getRectAbbr()
//	{
//		return buttons.getScreenLocation(1);
//	}
//
//	boolean isBerechtigtForAll(EintragsTyp typ)
//	{
//		Recht r = new Recht(typ, EINTRAGSRECHT);
//
//		Enumeration e = getAllPersonen().elements();
//		while (e.hasMoreElements())
//		{
//			Person p = (Person) e.nextElement();
//			if (p.getID() != Data.user.getID())
//			{
//				if (!Data.rechte.isBerechtigt(p, r))
//				{
//					return false;
//				}
//			}
//		}
//
//		return true;
//	}
//
//	@SuppressWarnings("unchecked")
//	Vector getAllPersonen()
//	{
//		Vector personen = new Vector();
//
//		// PersonenTable
//		int rows = personenTable.getRowCount();
//
//		for (int i = 0; i < rows; i++)
//			personen.addElement((Person) personenTable.getValueAt(i, 0));
//
//		// GruppenTable
//		rows = gruppenTable.getRowCount();
//
//		for (int i = 0; i < rows; i++)
//			getPersonen((Gruppe) gruppenTable.getValueAt(i, 0), personen);
//
//		return personen;
//	}
//
//	@SuppressWarnings("unchecked")
//	void getPersonen(Gruppe gruppe, Vector personen)
//	{
//		Hashtable ps = (Hashtable) gruppe.getPersonen();
//		Person p;
//
//		Enumeration e = ps.elements();
//		while (e.hasMoreElements())
//		{
//			p = (Person) e.nextElement();
//			if (!personen.contains(p))
//				personen.addElement(p);
//		}
//
//		Hashtable gr = (Hashtable) gruppe.getGruppen();
//
//		e = gr.elements();
//		while (e.hasMoreElements())
//			getPersonen((Gruppe) e.nextElement(), personen);
//	}
//
//	Vector getFailedPersonen()
//	{
//		return failedPersonen;
//	}

	// ActionListener ///////////////////////////////////////////////////
	public void addActionListener(ActionListener listener)
	{
		mActionListenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener)
	{
		mActionListenerList.remove(ActionListener.class, listener);
	}

//	protected void fireActionEvent(ActionEvent e)
//	{
//		Object[] listeners = mActionListenerList.getListenerList();
//
//		for (int i = listeners.length - 2; i >= 0; i -= 2)
//		{
//			if (listeners[i] == ActionListener.class)
//			{
//				((ActionListener) listeners[i + 1]).actionPerformed(e);
//			}
//		}
//	}
//
//	public void actionPerformed(ActionEvent e)
//	{
//		String c = e.getActionCommand();
//
//		if (c.equals("rightArrow1"))
//		{ // add Gruppe
//			TreePath path = selectGruppe.getTree().getSelectionPath();
//			if (path != null)
//				addGruppe(path);
//		}
//		else if (c.equals("leftArrow1"))
//		{ // remove Gruppe
//			int index = gruppenTable.getSelectedRow();
//			if (index >= 0)
//				removeGruppe(index);
//		}
//		else if (c.equals("rightArrow2"))
//		{ // add Person
//			int index = selectPerson.mListBox.getSelectedIndex();
//			if (index >= 0)
//				addPerson(index);
//		}
//		else if (c.equals("leftArrow2"))
//		{ // remove Person
//			int index = personenTable.getSelectedRow();
//			if (index >= 0)
//				removePerson(index);
//		}
//
//		else
//			fireActionEvent(e);
//	}
//
//	// ////////////////////////////////////////////////////////////////////////////////////////
//	// ButtonIconRenderer // ButtonIconRenderer // ButtonIconRenderer //
//	// ButtonIconRenderer //
//	// ////////////////////////////////////////////////////////////////////////////////////////
//	class ButtonIconRenderer implements TableCellRenderer
//	{
//		private JPanel mPanel;
//
//		private JButton mButton;
//
//		public ButtonIconRenderer()
//		{
//			mPanel = new JPanel();
//			mPanel.setLayout(new BorderLayout());
//			mPanel.setPreferredSize(new Dimension(30, 20));
//			mPanel.setBackground(SystemColor.control);
//
//			mButton = new JButton();
//			mButton.setBorder(null);
//			mButton.setPreferredSize(new Dimension(20, 20));
//			mButton.setMargin(new Insets(0, 0, 0, 0));
//			mButton.setAlignmentY((float) 0.5);
//			mPanel.add("Center", mButton);
//		}
//
//		public Component getTableCellRendererComponent(JTable table,
//				Object value, boolean isSelected, boolean hasFocus, int row,
//				int column)
//		{
//			String id = table.getColumnName(column);
//			int nr = Integer.valueOf(id).intValue();
//
//			ImageIcon bGif = client.Client.loadImageIcon("b" + nr + "0.gif");
//			mButton.setIcon(bGif);
//
//			return mPanel;
//		}
//	}
//
//	// /////////////////////////////////////////////////////////////////////////////////////
//	// EmptyRenderer // EmptyRenderer // EmptyRenderer // EmptyRenderer //
//	// EmptyRenderer //
//	// /////////////////////////////////////////////////////////////////////////////////////
//	class EmptyRenderer implements TableCellRenderer
//	{
//		private JPanel mPanel;
//
//		public EmptyRenderer()
//		{
//			mPanel = new JPanel();
//			mPanel.setPreferredSize(new Dimension(100, 20));
//			mPanel.setBackground(SystemColor.control);
//		}
//
//		public Component getTableCellRendererComponent(JTable table,
//				Object value, boolean isSelected, boolean hasFocus, int row,
//				int column)
//		{
//			return mPanel;
//		}
//	}
//
//	// ////////////////////////////////////////////////////////////////////////////////////
//	// MouseEventHandler // MouseEventHandler // MouseEventHandler //
//	// MouseEventHandler //
//	// ////////////////////////////////////////////////////////////////////////////////////
//	class MouseEventHandler extends MouseAdapter
//	{
//		int typ;
//
//		MouseEventHandler(int t)
//		{
//			typ = t;
//		}
//
//		public void mouseClicked(MouseEvent e)
//		{
//			Rectangle rect;
//			int i;
//
//			for (i = 2; i < 5; i++)// /Achtung Spalten
//			{
//				rect = ((JTableHeader) e.getSource()).getHeaderRect(i);
//				if (rect.contains(e.getPoint()))
//					break;
//			}
//
//			if (i < 5)
//			{
//				setCheckboxes(i, typ);
//			}
//		}
//	}
}
