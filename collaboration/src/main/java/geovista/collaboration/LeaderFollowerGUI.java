/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class LeaderFollowerGUI extends JPanel implements ActionListener {

	Preferences prefs;
	ButtonGroup gr = new ButtonGroup();
	JRadioButton radioLeader;
	JRadioButton radioFollower;
	JRadioButton radioNeither;
	int state;
	public static final String STATE_PROPTERTY_NAME = "State";
	protected final static Logger logger = Logger
			.getLogger(LeaderFollowerGUI.class.getName());

	public LeaderFollowerGUI() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setBorder(BorderFactory.createTitledBorder("Choose Role"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// this.setPreferredSize(new Dimension(200,300));
		gr = new ButtonGroup();
		radioLeader = new JRadioButton("Leader");
		radioFollower = new JRadioButton("Follower");
		radioNeither = new JRadioButton("Neither");
		gr.add(radioLeader);
		gr.add(radioFollower);
		gr.add(radioNeither);
		this.add(radioLeader);
		this.add(radioFollower);
		this.add(radioNeither);
		radioNeither.setSelected(true);
		radioLeader.addActionListener(this);
		radioFollower.addActionListener(this);
		radioNeither.addActionListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == radioNeither) {
			if (state != JabberUtils.STATE_NEITHER) {
				int oldState = state;
				state = JabberUtils.STATE_NEITHER;
				this.firePropertyChange("State", oldState, state);
			}
		} else if (e.getSource() == radioFollower) {
			if (state != JabberUtils.STATE_FOLLOWER) {
				int oldState = state;
				state = JabberUtils.STATE_FOLLOWER;
				this.firePropertyChange("State", oldState, state);
			}
		} else if (e.getSource() == radioLeader) {
			if (state != JabberUtils.STATE_LEADER) {
				int oldState = state;
				state = JabberUtils.STATE_LEADER;
				this.firePropertyChange("State", oldState, state);
			}
		} else {
			System.out
					.println("LeaderFollowerGUI, recieved event from unknown action source");
		}
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		LeaderFollowerGUI rc = new LeaderFollowerGUI();
		app.getContentPane().add(rc);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
		StateListener listener = rc.new StateListener();
		rc.addPropertyChangeListener(listener);

	}

	private class StateListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(
					LeaderFollowerGUI.STATE_PROPTERTY_NAME)) {
				logger.finest("got event, new val = " + evt.getNewValue());
			}

		}

	}

}
