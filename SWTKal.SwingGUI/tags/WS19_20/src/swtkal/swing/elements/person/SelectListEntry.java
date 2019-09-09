package swtkal.swing.elements.person;
// Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;

import swtkal.domain.Person;
import swtkal.server.Server;
import swtkal.swing.elements.CharacterTextField;

/////////////////////////////////////////////////////////////////////////////////////////////////
// Personenauswahl // Personenauswahl // Personenauswahl // Personenauswahl // Personenauswahl //
/////////////////////////////////////////////////////////////////////////////////////////////////
public class SelectListEntry implements ActionListener, ListSelectionListener,
		ItemSelectable
{
	// graphical Representation
	private JPanel gui = new JPanel();

	// JList
	@SuppressWarnings("rawtypes")
	public DefaultListModel mModel;

	@SuppressWarnings("rawtypes")
	public JList mListBox;

	private int mPos = 0;

	private int mSize = 0;

	// JTextField
	private CharacterTextField name, kuerzel;

	private String nameOld = "";

	// registrierte Listener
	private EventListenerList mListenerList;
	
	public SelectListEntry(boolean hasBtns, Server server)
	{
		// initialisieren
		mListenerList = new EventListenerList();

		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		if (hasBtns)
			gui.setBorder(new EmptyBorder(10, 10, 10, 10));

		create(hasBtns, server);
	}

	void create(boolean hasBtns, Server server)
	{
		JPanel enterName = new JPanel();
		enterName.setLayout(new BoxLayout(enterName, BoxLayout.X_AXIS));

		JLabel l1 = new JLabel("Name:");
		l1.setPreferredSize(new Dimension(50, 20));
		l1.setDisplayedMnemonic('n');

		name = new CharacterTextField();
		name.setFocusAccelerator('n');
		name.addActionListener(this);
		name.addKeyListener(new KeyEventHandler());

		enterName.add(l1);
		enterName.add(name);

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

		JPanel buttons = null;

		if (hasBtns)
		{
			buttons = new JPanel();
			buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

			JButton b1 = new JButton("OK");
			b1.setPreferredSize(new Dimension(95, 20));
			b1.setMnemonic('o');
			b1.setActionCommand("OK");
			b1.addActionListener(this);

			JButton b2 = new JButton("Abbrechen");
			b2.setPreferredSize(new Dimension(95, 20));
			b2.setMnemonic('a');
			b2.setActionCommand("Abbrechen");
			b2.addActionListener(this);

			buttons.add(Box.createHorizontalGlue());
			buttons.add(b1);
			buttons.add(Box.createRigidArea(new Dimension(20, 20)));
			buttons.add(b2);
			buttons.add(Box.createHorizontalGlue());
		}

		gui.add(enterName);
		gui.add(Box.createVerticalStrut(10));
		gui.add(enterKuerzel);
		gui.add(Box.createVerticalStrut(10));
		gui.add(createList(server));
		if (hasBtns)
		{
			gui.add(Box.createVerticalStrut(10));
			gui.add(buttons);
		}
	}

	public JPanel getGUI()
	{
		return gui;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	JScrollPane createList(Server server)
	{
		mListBox = new JList();
		mListBox.setCellRenderer(new SelectionCellRenderer());
		mListBox.setBackground(Color.white);
		mListBox.setVisibleRowCount(5);
		mListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Listener
		mListBox.getSelectionModel().addListSelectionListener(this);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane(mListBox);
		scrollPane.setPreferredSize(new Dimension(100, 70));

		// Model zuweisen
		createModel(server);

		return scrollPane;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createModel(Server server)
	{
		mModel = new DefaultListModel();
		mSize = 0;

		if (server.getPersonVector() != null)
		{
			Enumeration e = server.getPersonVector().elements();
			while (e.hasMoreElements())
			{
				mModel.addElement((Person) e.nextElement());
				mSize++;
			}
			mSize--;
		}

		name.setText("");
		kuerzel.setText("");
		nameOld = "";
		mPos = 0;

		try
		{
			mListBox.setModel(mModel);
			mListBox.validate();
			mListBox.repaint();
		}
		catch (RuntimeException e)
		{

		}
	}

	@SuppressWarnings("rawtypes")
	public void removePerson(String kuerzel)
	{
		Enumeration enumer = mModel.elements();
		while (enumer.hasMoreElements())
		{
			Person p = (Person) enumer.nextElement();
			if (p.getKuerzel().equals(kuerzel))
				mModel.removeElement(p);
		}

	}

	@SuppressWarnings("rawtypes")
	private Person contains(String kuerzel)
	{
		Enumeration enumer = mModel.elements();
		while (enumer.hasMoreElements())
		{
			Person p = (Person) enumer.nextElement();
			if (p.getKuerzel().equals(kuerzel))
				return p;
		}

		return null;
	}

	private void updateListPointer(boolean checkName)
	{
		if (checkName)
		{
			String nameNew = name.getText().toUpperCase();
			int len = nameNew.length();
			String entry = "";

			if (len >= nameOld.length())
			{
				if (nameNew.compareTo(nameOld) > 0)
				{
					int isGreater;
					do
					{
						entry = getCharsAt(mPos, len);
						isGreater = nameNew.compareTo(entry);
						mPos++;

					}
					while (isGreater > 0 && !(mPos > mSize));

					mPos--;
					mListBox.ensureIndexIsVisible(mPos);

					if (isGreater == 0
							&& (mPos == mSize || nameNew.compareTo(getCharsAt(
									mPos + 1, len)) != 0))
					{
						Person person = (Person) mModel.get(mPos);
						String persName = person.getName();
						nameOld = persName.toUpperCase();

						name.setText(persName);
						mListBox.setSelectedIndex(mPos);

						return;
					}
				}
			}
			else
				mPos = 0;

			nameOld = nameNew;

		}
		else
		{
			String k = kuerzel.getText().toUpperCase();
			Person person;

			if ((person = contains(k)) != null)
			{
				kuerzel.setText(k);
				mListBox.setSelectedValue(person, true);
			}
		}
	}

	String getCharsAt(int mPos, int len)
	{
		Person person = (Person) mModel.get(mPos);
		String txt = person.getName();

		if (txt.length() <= len)
		{
			return txt.toUpperCase();
		}
		else
		{
			return txt.substring(0, len).toUpperCase();
		}
	}

	@SuppressWarnings("deprecation")
	public Object[] getSelectedObjects()
	{
		return mListBox.getSelectedValues();
	}

	// Register ItemListener
	public void addItemListener(ItemListener listener)
	{
		mListenerList.add(ItemListener.class, listener);
	}

	// Un-register ItemListener
	public void removeItemListener(ItemListener listener)
	{
		mListenerList.remove(ItemListener.class, listener);
	}

	// Register ListSelectionListener
	public void addListSelectionListener(ListSelectionListener listener)
	{
		mListenerList.add(ListSelectionListener.class, listener);
	}

	// Un-register ListSelectionListener
	public void removeListSelectionListener(ListSelectionListener listener)
	{
		mListenerList.remove(ListSelectionListener.class, listener);
	}

	protected void fireEvent(EventObject newEvent)
	{
		Object[] listeners = mListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (newEvent instanceof ItemEvent)
			{
				if (listeners[i] == ItemListener.class)
				{
					((ItemListener) listeners[i + 1])
							.itemStateChanged((ItemEvent) newEvent);
				}
			}
			else if (newEvent instanceof ListSelectionEvent)
			{
				if (listeners[i] == ListSelectionListener.class)
				{
					((ListSelectionListener) listeners[i + 1])
							.valueChanged((ListSelectionEvent) newEvent);
				}
			}
		}
	}

	// ActionListener
	public void actionPerformed(ActionEvent e)
	{
		if (!e.getActionCommand().equals("Abbrechen"))
		{
			int index = mListBox.getSelectedIndex();
			if (index >= 0)
			{
				fireEvent(new ItemEvent(this, 0, mModel.get(index),
						ItemEvent.ITEM_STATE_CHANGED));
			}
		}
		else
		{ // Abbrechen
			fireEvent(new ItemEvent(this, 0, null, ItemEvent.ITEM_STATE_CHANGED));
		}
	}

	// ListSelectionListener
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getFirstIndex() >= 0)
		{
			mPos = mListBox.getSelectedIndex();
			Person person = (Person) mModel.get(mPos);

			name.setText(person.getName());
			kuerzel.setText(person.getKuerzel());
		}

		fireEvent(e);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// SelectionCellRenderer // SelectionCellRenderer // SelectionCellRenderer
	// // SelectionCell //
	// ////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	class SelectionCellRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = -8100911759388290485L;

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			Person person = (Person) value;
			setText(person.getName() + " (" + person.getKuerzel() + ")");

			if (isSelected)
			{
				setBackground(SystemColor.textHighlight);
				setForeground(SystemColor.textHighlightText);
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(SystemColor.textText);
			}

			return this;
		}

		public boolean isOpaque()
		{
			return true;
		}
	}

	class KeyEventHandler extends KeyAdapter
	{
		public void keyReleased(KeyEvent e)
		{
			if (e.getSource() == name)
			{
				updateListPointer(true);
			}
			else
			{
				updateListPointer(false);
			}
		}
	}
}
