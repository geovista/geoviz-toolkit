/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jivesoftware.smack.XMPPConnection;

public class GeoJabberPreferences extends JFrame implements ActionListener,
		ChangeListener, ListSelectionListener {

	String[] servers = { "localhost", "Google Chat", "Jabber.org" };
	private String currentServer;
	JList serverList;
	JLabel serverLabel;

	JList collaboratorList;
	String[] collaborators = { "Hannah", "Frank", "HyangJa" };
	String collaborator;
	JLabel collaboratorLabel;
	String currentCollaborator;

	JTextField userName;
	JPasswordField passwordField;
	String user;
	JLabel userLabel;

	boolean autoConnect;
	private XMPPConnection connection;

	Color darkerGrey = new Color(200, 250, 200);

	Preferences prefs;

	public GeoJabberPreferences() {
		init();

	}

	public GeoJabberPreferences(XMPPConnection conn) {
		init();
		if (conn != null) {
			connection = conn;
			userName.setText(conn.getUser());
			prefs = Preferences.userNodeForPackage(conn.getClass());
		}
	}

	/**
	 * 
	 */
	private void init() {
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(makeServerPanel());
		getContentPane().add(makeUserPanel());
		getContentPane().add(makeCollaboratorPanel());
		pack();

	}

	public JPanel makeServerPanel() {

		serverList = new JList(servers);
		JPanel serverPanel = new JPanel();
		Border lineBorder = BorderFactory.createLineBorder(Color.black);
		Border titledBorder = BorderFactory.createTitledBorder(lineBorder,
				"Server");
		serverPanel.setBorder(titledBorder);
		serverPanel.add(serverList);
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.addListSelectionListener(this);

		serverLabel = new JLabel(servers[0]);

		serverLabel.setBackground(darkerGrey);
		serverLabel.setOpaque(true);
		serverPanel.add(serverLabel);
		return serverPanel;
	}

	public JPanel makeCollaboratorPanel() {

		collaboratorList = new JList(collaborators);

		JPanel collaboratorPanel = new JPanel();

		Border lineBorder = BorderFactory.createLineBorder(Color.black);
		Border titledBorder = BorderFactory.createTitledBorder(lineBorder,
				"Collaborator");
		collaboratorPanel.setBorder(titledBorder);

		JScrollPane scrollPane = new JScrollPane(collaboratorList);
		collaboratorPanel.add(scrollPane);
		collaboratorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		collaboratorList.addListSelectionListener(this);
		collaboratorLabel = new JLabel(collaborators[0]);
		collaboratorLabel.setBackground(darkerGrey);
		collaboratorLabel.setOpaque(true);
		collaboratorPanel.add(collaboratorLabel);
		return collaboratorPanel;
	}

	public JPanel makeUserPanel() {

		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

		Border lineBorder = BorderFactory.createLineBorder(Color.black);
		Border titledBorder = BorderFactory.createTitledBorder(lineBorder,
				"User");
		userPanel.setBorder(titledBorder);
		JLabel nameLabel = new JLabel("Name:");
		userPanel.add(nameLabel);

		userName = new JTextField();
		userName.setPreferredSize(new Dimension(80, 25));
		userPanel.add(userName);

		JLabel passwordLabel = new JLabel("Password:");
		userPanel.add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(40, 25));
		userPanel.add(passwordField);

		return userPanel;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(serverList)) {
			currentServer = (String) serverList.getSelectedValue();
			serverLabel.setText(currentServer);
		} else if (e.getSource().equals(collaboratorList)) {
			currentCollaborator = (String) collaboratorList.getSelectedValue();
			collaboratorLabel.setText(currentCollaborator);
		}
		pack();
	}

	/**
	 * @return Returns the name of the current server.
	 */
	public String getCurrentServer() {
		return currentServer;
	}

	/**
	 * @param connection
	 *            The connection to set.
	 */
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return Returns the connection.
	 */
	public XMPPConnection getConnection() {
		return connection;
	}

	static public void main(String args[]) {
		GeoJabberPreferences rc = new GeoJabberPreferences();

		rc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rc.pack();
		rc.setVisible(true);
	}

}
