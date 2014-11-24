/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */

package geovista.collaboration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

public class RemoteCollaboration extends JPanel implements SelectionListener,
		SpatialExtentListener, SubspaceListener, ActionListener,
		RmiListenerClient, MessageReceiver {

	JPanel ipPanel;
	ChatPanel chatPanel;
	JPanel sendPanel;
	JTextField ipField;
	JButton connectButton;

	JPanel buttonPanel;
	JButton sendSelection;
	JButton sendVariables;
	JButton sendSpatialExtent;
	EventListenerList listenerList;

	RmiListener localListener;
	Object remoteListener; // is an RmiListener remotely

	String remoteIP;

	int[] selection;
	Rectangle2D spatialExtent;
	int[] subspace;

	public static final int DEFAULT_PORT = 3232;

	public static final String USER_NAME = "Frank";
	final static Logger logger = Logger.getLogger(RemoteCollaboration.class
			.getName());
	static boolean DEBUG = true;

	public RemoteCollaboration() {
		RmiListener.startRegistry();
		int delay = 2000; // milliseconds
		ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				registerLocalRmiListener();
			}
		};
		new Timer(delay, taskPerformer).start();

		setLayout(new BorderLayout());
		ipPanel = makeIpPanel();
		this.add(ipPanel, BorderLayout.NORTH);
		buttonPanel = makeButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		chatPanel = new ChatPanel();
		chatPanel.setMsgReciever(this);
		this.add(chatPanel, BorderLayout.CENTER);
		listenerList = new EventListenerList();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectButton) {
			turnButtonsOn();

			remoteIP = ipField.getText();
			remoteListener = RmiListener.connectToRemoteJVM(remoteIP);
		} else if (e.getSource() == sendSelection) {
			sendRemoteSelection();
		} else if (e.getSource() == sendSpatialExtent) {
			sendRemoteSpatialExtent();
		} else if (e.getSource() == sendVariables) {
			sendRemoteSubspace();
		} else {
			logger.finest(this.getClass().getName()
					+ " recieved unknown action");
		}

	}

	private JPanel makeIpPanel() {
		ipPanel = new JPanel(new FlowLayout());
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		JLabel ipLabel = new JLabel("IP Address:");
		ipPanel.add(ipLabel);

		ipField = new JTextField();
		ipField.setPreferredSize(new Dimension(120, 25));
		ipPanel.add(ipField);
		ipPanel.add(connectButton);

		ipPanel.setBorder(new LineBorder(Color.black));

		Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
		String ipAddress = gvPrefs.get("LastGoodIP", "");
		ipField.setText(ipAddress);

		return ipPanel;

	}

	private JPanel makeButtonPanel() {
		buttonPanel = new JPanel();
		// this.buttonPanel.setLayout(new
		// BoxLayout(this.buttonPanel,BoxLayout.Y_AXIS));
		sendSelection = new JButton("Send Selection");
		sendVariables = new JButton("Send Variables");
		sendSpatialExtent = new JButton("Send Spatial Extent");
		buttonPanel.add(sendSelection);
		buttonPanel.add(sendVariables);
		buttonPanel.add(sendSpatialExtent);

		sendSelection.addActionListener(this);
		sendVariables.addActionListener(this);
		sendSpatialExtent.addActionListener(this);
		buttonPanel.setBorder(new LineBorder(Color.black));
		sendSelection.setEnabled(false);
		sendVariables.setEnabled(false);
		sendSpatialExtent.setEnabled(false);
		return buttonPanel;

	}

	private void sendRemoteSpatialExtent() {
		if (spatialExtent == null || remoteListener == null) {
			return;
		}

		RemoteSpatialExtentListener spatialExtentListener = (RemoteSpatialExtentListener) remoteListener;

		// (x,y,w,h)

		double x = spatialExtent.getX();
		double y = spatialExtent.getY();
		double width = spatialExtent.getWidth();
		double height = spatialExtent.getHeight();

		double[] extent = { x, y, width, height };
		try {
			spatialExtentListener.extentChanged(RemoteCollaboration.USER_NAME,
					extent);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

	}

	private void sendRemoteSelection() {
		if (selection == null || remoteListener == null) {
			return;
		}

		RemoteSelectionListener selectionListener = (RemoteSelectionListener) remoteListener;

		try {
			selectionListener.selectionChanged(RemoteCollaboration.USER_NAME,
					selection);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

	}

	private void sendRemoteSubspace() {
		if (subspace == null || remoteListener == null) {
			return;
		}

		RemoteSubspaceListener subspaceListener = (RemoteSubspaceListener) remoteListener;

		try {
			subspaceListener.subspaceChanged(RemoteCollaboration.USER_NAME,
					selection);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

	}

	public void receiveMessage(String Name, String msg) {
		// got this from our local chat window
		// pass it along to remote listeners
		if (RemoteCollaboration.logger.isLoggable(Level.FINEST)) {
			System.out
					.println("RemoteCollaboration, got message from chat panel: "
							+ msg);
		}
		if (msg == null || remoteListener == null) {
			return;
		}

		RemoteMessageReceiver msgListener = (RemoteMessageReceiver) remoteListener;

		try {
			if (RemoteCollaboration.logger.isLoggable(Level.FINEST)) {
				System.out
						.println("RemoteCollaboration, sending message to remote listener: "
								+ msg);
			}
			msgListener.receiveMessage(RemoteCollaboration.USER_NAME, msg);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

	}

	SpatialExtentEvent savedEvent;

	public SpatialExtentEvent getSpatialExtentEvent() {
		return savedEvent;
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {

		spatialExtent = e.getSpatialExtent();
		// we got this event from a remote jvm via our local rmiListener
		// so pass it along to other listeners on the local jvm
		fireSpatialExtentChanged(spatialExtent);
		savedEvent = e;
	}

	public void subspaceChanged(SubspaceEvent e) {

		subspace = e.getSubspace();
		// we got this event from a remote jvm via our local rmiListener
		// so pass it along to other listeners on the local jvm
		fireSubspaceChanged(subspace);

	}

	public void selectionChanged(SelectionEvent e) {

		selection = e.getSelection();
		// we got this event from a remote jvm via our local rmiListener
		// so pass it along to other listeners on the local jvm
		fireSelectionChanged(selection);

	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, selection);
	}

	public void remoteMessageReceived(String name, String message) {
		// we got this selection from somewhere (probably another JVM)
		// send it to our local chat panel
		chatPanel.receiveMessage(name, message);
	}

	public void remoteSelectionChanged(String source, int[] selection) {
		if (RemoteCollaboration.logger.isLoggable(Level.FINEST)) {
			logger.finest("got remote message from " + source);
		}
		// we got this selection from somewhere (probably another JVM)
		// send it along to registered listeners in this JVM
		fireSelectionChanged(selection);
	}

	public void remoteSpatialExtentChanged(String source, Rectangle2D extent) {

		fireSpatialExtentChanged(extent);

	}

	public void remoteSubspaceChanged(String source, int[] subspace) {

	}

	private void turnButtonsOn() {
		sendSelection.setEnabled(true);
		sendVariables.setEnabled(true);
		sendSpatialExtent.setEnabled(true);

	}

	private void registerLocalRmiListener() {

		if (localListener != null) {
			return;
		}
		try {
			localListener = new RmiListener(RemoteCollaboration.DEFAULT_PORT);
			localListener.setClient(this);

		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * adds an SpatialExtentListener
	 */
	public void addSpatialExtentListener(SpatialExtentListener l) {
		listenerList.add(SpatialExtentListener.class, l);
	}

	/**
	 * removes an SpatialExtentListener from the component
	 */
	public void removeSpatialExtentListener(SpatialExtentListener l) {
		listenerList.remove(SpatialExtentListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireSpatialExtentChanged(Rectangle2D newSpatialExtent) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SpatialExtentEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SpatialExtentListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SpatialExtentEvent(this, newSpatialExtent);
				}

				((SpatialExtentListener) listeners[i + 1])
						.spatialExtentChanged(e);
			}
		} // next i
	}

	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	public void removeSelectionListener(SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireSelectionChanged(int[] newSelection) {

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}
				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		} // next i

	}

	// Add Subspace Changed Listener
	public void addSubspaceListener(SubspaceListener l) {
		listenerList.add(SubspaceListener.class, l);
	}

	// Removes that Listener
	public void removeSubspaceListener(SubspaceListener l) {
		listenerList.remove(SubspaceListener.class, l);
	}

	// Fires Subspace Changed
	public void fireSubspaceChanged(int[] selection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SubspaceEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SubspaceListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SubspaceEvent(this, selection);
				}

				((SubspaceListener) listeners[i + 1]).subspaceChanged(e);
			}
		}
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		RemoteCollaboration rc = new RemoteCollaboration();
		rc.setPreferredSize(new Dimension(450, 400));
		rc.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(rc);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.pack();
		app.setVisible(true);
	}
}
