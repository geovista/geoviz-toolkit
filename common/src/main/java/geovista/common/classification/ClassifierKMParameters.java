/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClassifierKMParameters extends JPanel implements
		ListSelectionListener {
	protected final static Logger logger = Logger
			.getLogger(ClassifierKMParameters.class.getName());
	private int classNum = 3;
	private String[] attributeNames;
	private int[] selectedAttIdx;
	private JList attList;
	private JPanel attSelPanel;
	private JPanel classifierParaPane;
	private JTextField[] iniField;
	private int[] iniObsIdx;
	private final EventListenerList listenerListAction = new EventListenerList();

	public ClassifierKMParameters(String[] attributeNames, int classNum) {
		super();
		this.attributeNames = attributeNames;
		this.classNum = classNum;
		AttributeSelectionPanel();
		classifierParaPanel();
		setLayout(new GridLayout(1, 2));
		this.add(attSelPanel);
		this.add(classifierParaPane);
		setPreferredSize(new Dimension(300, 50));
		revalidate();
	}

	public void setAttributeNames(String[] attributesNames) {
		attributeNames = attributesNames;
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public void setSelectedAttIdx(int[] selectedAttIdx) {
		this.selectedAttIdx = selectedAttIdx;
	}

	public int[] getSelectedAttIdx() {
		return selectedAttIdx;
	}

	public int[] getIniObsIdx() {
		return iniObsIdx;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public int getClassNum() {
		return classNum;
	}

	private void AttributeSelectionPanel() {
		JScrollPane attPane;
		JButton seNoneButton;
		JButton seAllButton;
		attSelPanel = new JPanel(new BorderLayout());
		attList = new JList(attributeNames);
		selectedAttIdx = new int[attributeNames.length];
		for (int i = 0; i < attributeNames.length; i++) {
			selectedAttIdx[i] = i;
		}
		attList.setSelectedIndices(selectedAttIdx);

		attList.setName("Attributes for Classifiers");
		JLabel attListName = new JLabel("Attribute List");
		attList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// attList.addListSelectionListener(this);
		// JScrollPane scrollPane = new JScrollPane(attList);
		attPane = new JScrollPane(attList);
		seNoneButton = new JButton("None");
		seNoneButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				logger.finest("about to press button Select");
				seNoneButton_actionPerformed();
				logger.finest("after pressed button Select");
			}
		});
		seAllButton = new JButton("All");
		seAllButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				seAllButton_actionPerformed();
			}
		});
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel.add(seNoneButton);
		buttonPanel.add(seAllButton);

		attSelPanel.add(attListName, BorderLayout.NORTH);
		attSelPanel.add(attPane, BorderLayout.CENTER);
		attSelPanel.add(buttonPanel, BorderLayout.SOUTH);

		attList.addListSelectionListener(this);
		selectedAttIdx = attList.getSelectedIndices();
	}

	private void seNoneButton_actionPerformed() {
		attList.clearSelection();
	}

	private void seAllButton_actionPerformed() {
		selectedAttIdx = new int[attributeNames.length];
		for (int i = 0; i < attributeNames.length; i++) {
			selectedAttIdx[i] = i;
		}
		attList.setSelectedIndices(selectedAttIdx);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		JList theList = (JList) e.getSource();
		if (theList.isSelectionEmpty()) {
			return;
		}
		selectedAttIdx = attList.getSelectedIndices();
	}

	private void classifierParaPanel() {
		classifierParaPane = new JPanel(new BorderLayout());

		// Class Number Display
		JPanel classNumPane = new JPanel(new GridLayout(1, 2));
		JLabel classNumStringLabel = new JLabel("Class #:");
		JLabel classNumLabel = new JLabel(new Integer(classNum).toString());
		classNumPane.add(classNumStringLabel);
		classNumPane.add(classNumLabel);

		// Initial points setup
		JPanel iniPane = new JPanel(new BorderLayout());
		JLabel iniStringLabel = new JLabel("Set up Initial Points:");

		JPanel iniSetPane = new JPanel(new GridLayout(classNum, 2));
		JLabel[] iniLabel = new JLabel[classNum];
		iniField = new JTextField[classNum];
		for (int i = 0; i < classNum; i++) {
			iniLabel[i] = new JLabel(new Integer(i).toString());
			iniField[i] = new JTextField();
			iniField[i].setText(new Integer(i).toString());
			iniField[i].setAlignmentY(Component.TOP_ALIGNMENT);
			iniSetPane.add(iniLabel[i]);
			iniSetPane.add(iniField[i]);
		}
		iniPane.add(iniStringLabel, BorderLayout.NORTH);
		iniPane.add(iniSetPane, BorderLayout.CENTER);

		// Conclusion button panel
		JPanel concludePane = new JPanel(new GridLayout(1, 2));
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				applyButton_actionPerformed();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed();
			}
		});
		concludePane.add(applyButton);
		concludePane.add(cancelButton);

		classifierParaPane.add(classNumPane, BorderLayout.NORTH);
		classifierParaPane.add(iniPane, BorderLayout.CENTER);
		classifierParaPane.add(concludePane, BorderLayout.SOUTH);

	}

	private void applyButton_actionPerformed() {
		iniObsIdx = new int[classNum];
		for (int i = 0; i < classNum; i++) {
			iniObsIdx[i] = Integer.parseInt(iniField[i].getText());
		}
		fireActionPerformed();
	}

	// Cancel, means using default parameter sets, all of attributes are
	// considered and non initial points are specified.
	private void cancelButton_actionPerformed() {
		selectedAttIdx = new int[attributeNames.length];
		for (int i = 0; i < attributeNames.length; i++) {
			selectedAttIdx[i] = i;
		}
		attList.setSelectedIndices(selectedAttIdx);
		iniObsIdx = null;
		fireActionPerformed();
	}

	/**
	 * adds an ActionListener to the button
	 */
	public void addActionListener(ActionListener l) {
		listenerListAction.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerListAction.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireActionPerformed() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerListAction.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		ActionEvent e2 = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				"OK");
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				((ActionListener) listeners[i + 1]).actionPerformed(e2);
			}
		}
	}

}