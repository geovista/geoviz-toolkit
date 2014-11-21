/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClassifierMLParameters extends JPanel implements
		ListSelectionListener {

	private static int UNBIASCOV = 0; // divided by N-1
	private static int GAUSSIAN_DISTRIBUTION = 0;
	private int distribution;
	private int isUnBiasCov;
	private int classNum = 3;
	private String[] attributeNames;
	private int[] selectedAttIdx;
	private JList attList;
	private JPanel attSelPanel;
	private JPanel classifierParaPane;
	private JCheckBox visualDisplayButton;
	private final EventListenerList listenerListAction = new EventListenerList();
	protected final static Logger logger = Logger
			.getLogger(ClassifierMLParameters.class.getName());

	public ClassifierMLParameters(String[] attributeNames, int classNum) {
		super();
		this.attributeNames = attributeNames;
		this.classNum = classNum;
		AttributeSelectionPanel();
		classifierParaPanel();
		setLayout(new GridLayout(1, 2));
		this.add(attSelPanel);
		this.add(classifierParaPane);
		setPreferredSize(new Dimension(300, 250));
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

	public void setIsUnBiasCov(int isUnBiasCov) {
		this.isUnBiasCov = isUnBiasCov;
	}

	public int getIsUnBiasCov() {
		return isUnBiasCov;
	}

	public void setVisualDisplay(boolean visualDisplay) {
		visualDisplayButton.setSelected(visualDisplay);
	}

	public boolean getVisualDisplay() {
		return visualDisplayButton.isSelected();
	}

	public void setDistributionType(int distributionType) {
		distribution = distributionType;
	}

	public int getDistributionType() {
		return distribution;
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

		JPanel paraPane = new JPanel(new GridLayout(7, 1));
		// Class Number Display
		JLabel classNumStringLabel = new JLabel("Class #:");
		JLabel classNumLabel = new JLabel(new Integer(classNum).toString());
		paraPane.add(classNumStringLabel);
		paraPane.add(classNumLabel);
		// Select proper distribution for data
		JLabel distributionLabel = new JLabel("Select Distribution");
		JComboBox distributionCombo = new JComboBox();
		distributionCombo.addItem("Gaussian");
		distributionCombo.addItem("Student T");
		distribution = ClassifierMLParameters.GAUSSIAN_DISTRIBUTION;
		distributionCombo.setMinimumSize(new Dimension(80, 20));
		distributionCombo.setMaximumSize(new Dimension(120, 20));
		distributionCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				if (cb.getItemCount() > 0) {
					distribution = cb.getSelectedIndex();
				}// end if count > 0
			}// end inner class
		});// end add listener
		paraPane.add(distributionLabel);
		paraPane.add(distributionCombo);
		// Select Bias or unbias calculation for covariance matrix
		JLabel covLabel = new JLabel("Covariance matrix is");
		JComboBox covCombo = new JComboBox();
		covCombo.addItem("Unbias (divided by N-1)");
		covCombo.addItem("Bias (divided by N)");
		isUnBiasCov = ClassifierMLParameters.UNBIASCOV;
		covCombo.setMinimumSize(new Dimension(80, 20));
		covCombo.setMaximumSize(new Dimension(120, 20));
		covCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				if (cb.getItemCount() > 0) {
					isUnBiasCov = cb.getSelectedIndex();
				}// end if count > 0
			}// end inner class
		});// end add listener
		paraPane.add(covLabel);
		paraPane.add(covCombo);

		visualDisplayButton = new JCheckBox("Display Distribution");
		visualDisplayButton.setSelected(false);
		paraPane.add(visualDisplayButton);

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

		classifierParaPane.add(paraPane, BorderLayout.NORTH);
		classifierParaPane.add(concludePane, BorderLayout.SOUTH);

	}

	private void applyButton_actionPerformed() {

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