package swtkal.swing.elements;
//Achtung: Klasse im Wesentlichen unveraendert aus Calendarium uebernommen

//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//
//////////////////////////////////////////////////////////////////////////////////////////////
//// SpinZahlenfeld // SpinZahlenfeld // SpinZahlenfeld // SpinZahlenfeld // SpinZahlenfeld //
//////////////////////////////////////////////////////////////////////////////////////////////
//
//public class SpinZahlenfeld extends JPanel implements ActionListener {
//	private static final long serialVersionUID = -4699357883031953221L;
//
////	// Control
////	private Zahlenfeld zahlenfeld;
////
////	private JButton[] btn = new JButton[2];
////
////	public SpinZahlenfeld(int z) {
////		// Zahlenfeld
////		zahlenfeld = new Zahlenfeld(z);
////
////		setLayout(new BorderLayout());
////		create();
////	}
////
////	void create() {
////		ImageIcon gif;
////
////		JPanel buttons = new JPanel();
////		buttons.setLayout(new GridLayout(2, 1));
////
////		gif = client.Client.loadImageIcon("up.gif");
////		btn[0] = new JButton(gif);
////		btn[0].setPreferredSize(new Dimension(13, 10));
////		btn[0].setMargin(new Insets(0, 0, 0, 0));
////		btn[0].setActionCommand("UP");
////		btn[0].addActionListener(this);
////
////		gif = client.Client.loadImageIcon("down.gif");
////		btn[1] = new JButton(gif);
////		btn[1].setPreferredSize(new Dimension(13, 10));
////		btn[1].setMargin(new Insets(0, 0, 0, 0));
////		btn[1].setActionCommand("DOWN");
////		btn[1].addActionListener(this);
////
////		buttons.add(btn[0]);
////		buttons.add(btn[1]);
////
////		add("Center", zahlenfeld);
////		add("East", buttons);
////	}
////
////	public void setEnabled(boolean state) {
////		zahlenfeld.setEnabled(state);
////		btn[0].setEnabled(state);
////		btn[1].setEnabled(state);
////	}
////
////	public void setValue(int v) {
////		zahlenfeld.setValue(v);
////	}
////
////	public double getValue() {
////		return zahlenfeld.getValue();
////	}
////
////	// ActionListener // ActionListener // ActionListener // ActionListener //
////	// ActionListener //
////	public void actionPerformed(ActionEvent e) {
////		if (e.getActionCommand().equals("UP")) {
////			zahlenfeld.setValue(zahlenfeld.getValue() + 1);
////		} else {
////			if (zahlenfeld.getValue() > 1) {
////				zahlenfeld.setValue(zahlenfeld.getValue() - 1);
////			}
////		}
////	}
//
//}
