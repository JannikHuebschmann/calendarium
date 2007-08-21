package client.admin.feiertag; //
/////////////////////////////////

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import basisklassen.Feiertag;
import data.Data;

//////////////////////////////////////////////////////////////////////////////////////////////////
// EditFeiertageControl // EditFeiertageControl // EditFeiertageControl // EditFeiertageControl //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class EditFeiertageControl implements ActionListener {
	// ParentWindow
	private JFrame parentFrame;

	// EditFeiertage
	private EditFeiertage editFeiertage;

	// EditFeiertag
	private EditFeiertag editFeiertag;

	// Feiertag
	private Feiertag tag;

	// EditFlag
	private boolean edit;

	public EditFeiertageControl(JFrame f) {
		parentFrame = f;

		editFeiertage = new EditFeiertage();
		editFeiertage.addActionListener(this);
	}

	// GUI
	public JInternalFrame getGUI() {
		return editFeiertage.getGUI();
	}

	// create // create // create // create // create // create // create //
	// create // create //
	private void createFeiertag(Feiertag tag) {
		Data.feiertage.create(tag);
	}

	// update // update // update // update // update // update // update //
	// update // update //
	private void updateFeiertag(Feiertag tag) {
		Data.feiertage.update(tag);
	}

	// delete // delete // delete // delete // delete // delete // delete //
	// delete // delete //
	private void deleteFeiertag(Feiertag tag) {
		Data.feiertage.delete(tag);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// ActionListener // ActionListener // ActionListener // ActionListener //
	// ActionListener //
	// //////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();

		// /////////////////////////////////////////////////////////////////////////////////////
		// EditFeiertage // EditFeiertage // EditFeiertage // EditFeiertage //
		// EditFeiertage //
		// /////////////////////////////////////////////////////////////////////////////////////

		if (c.equals("new")) {
			edit = false;

			editFeiertag = new EditFeiertag(parentFrame);
			editFeiertag.addActionListener(this);

			editFeiertag.start();
		}

		else if (c.equals("edit")) {
			edit = true;

			if ((tag = editFeiertage.getSelectedFeiertag()) != null) {
				editFeiertag = new EditFeiertag(parentFrame);
				editFeiertag.addActionListener(this);

				editFeiertag.fill(tag);
				editFeiertag.start();
			}
		}

		else if (c.equals("delete")) {
			if ((tag = editFeiertage.getSelectedFeiertag()) != null) {
				deleteFeiertag(tag);

				// update
				editFeiertage.update();
			}
		}

		// ////////////////////////////////////////////////////////////////////////////////
		// EditFeiertag // EditFeiertag // EditFeiertag // EditFeiertag //
		// EditFeiertag //
		// ////////////////////////////////////////////////////////////////////////////////

		else if (c.equals("Abbrechen")) {
			// Dialog schlieﬂen
			editFeiertag.close();
		} else if (c.equals("OK")) {
			if (editFeiertag.checkInput()) {
				if (edit) {
					updateFeiertag(editFeiertag.getFeiertag());
				} else {
					createFeiertag(editFeiertag.getFeiertag());
				}

				// update
				editFeiertage.update();

				// Dialog schlieﬂen
				editFeiertag.close();
			}
		}
	}
}
