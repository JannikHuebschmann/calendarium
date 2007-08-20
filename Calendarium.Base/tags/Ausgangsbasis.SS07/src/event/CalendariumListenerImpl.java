package event; //
/////////////////

import javax.swing.*;

import java.awt.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import data.*;

///////////////////////////////////////////////////////////////////////////////////
// CalendariumListenerImpl // CalendariumListenerImpl // CalendariumListenerImpl //
///////////////////////////////////////////////////////////////////////////////////

// Client
public class CalendariumListenerImpl extends UnicastRemoteObject implements CalendariumListener,
                                                                            EventConstants
{   // ParentFrame
    private static final long serialVersionUID = 8817416637570209772L;

	private JFrame parentFrame;

    // Observed
    private BeeingWatched observed;

    public CalendariumListenerImpl(JFrame f) throws RemoteException
    {
        parentFrame = f;
        observed = new BeeingWatched();
    }

    public synchronized void processCalendariumEvent(CalendariumEvent e) throws RemoteException
    {
        switch(e.getEventID())
        {
            case MESSAGE_EVT:   // MessageEvent

                MessageEvent mEvt = (MessageEvent) e;

                if(Data.user.getVorzugsNfkt() == Shared.NFKT_BEEP)
                {
                    parentFrame.getToolkit().beep();
                    @SuppressWarnings("unused")
					DialogThread dialog = new DialogThread(mEvt.getTitle(), mEvt.getMessage());
                }
                break;

            case NFKT_EVT:      // NfktEvent

                NfktEvent nEvt = (NfktEvent) e;

                if(nEvt.getDelivery() == Shared.NFKT_BEEP && nEvt.isShowing())
                {
                    parentFrame.getToolkit().beep();
                    @SuppressWarnings("unused")
					DialogThread dialog = new DialogThread(nEvt.getTitle(), nEvt.getMessage());
                }

                break;

            case ADMIN_EVT:     // AdminEvent

                AdminEvent aEvt = (AdminEvent) e;
                @SuppressWarnings("unused")
				DialogThread dialog = new DialogThread("Mitteilung des Administrators",
                                                       aEvt.getMessage());
        }

        observed.notifyObservers(e);
    }

    public boolean isActive() throws RemoteException
    {   return true;
    }

    public void addObserver(Observer o)
    {   observed.addObserver(o);
    }

    public void deleteObserver(Observer o)
    {   observed.deleteObserver(o);
    }

    class BeeingWatched extends Observable
    {
        public void notifyObservers(Object b)
        {
            setChanged();
            super.notifyObservers(b);
        }
    }

    class DialogThread implements Runnable
    {
                // Thread
                private Thread thread;

                // Dialog
                private String title;
            private String text;

                DialogThread(String title, String text)
                {
                    this.title = title;
                    this.text = text;

                        thread = new Thread(this);
                        thread.start();
                }

        private void showDialog()
        {
            JTextArea area = new JTextArea(text);

            StringTokenizer st = new StringTokenizer(text, "\n");
            int len = 0, count = 0;

            while(st.hasMoreTokens())
            {   len = Math.max(len, st.nextToken().length());
                count++;
            }

            area.setBackground(SystemColor.control);
            area.setEditable(false);
            area.setColumns(len);
            area.setRows(count);

            JOptionPane optionPane = new JOptionPane(area, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialogPane = optionPane.createDialog(parentFrame, title);

            dialogPane.show();
            dialogPane.dispose();
        }

                public void run()
                {
                        try
                        {       showDialog();

                        } catch(Exception ex) {}
                }
        }
}
