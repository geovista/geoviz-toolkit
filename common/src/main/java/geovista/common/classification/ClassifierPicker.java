/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;

import geovista.common.data.DataSetForApps;
import geovista.common.event.ClassificationEvent;
import geovista.common.event.ClassificationListener;
import geovista.common.event.ColumnAppendedEvent;
import geovista.geoviz.visclass.VisualClassifier;

public class ClassifierPicker extends JPanel implements ActionListener,
	ComponentListener {
    public static final String COMMAND_CLASSES_CHANGED = "colors";
    public static final String COMMAND_BEAN_REGISTERED = "hi!";
    public static final String COMMAND_SELECTED_VARIABLE_CHANGED = "var_change";
    public static final String COMMAND_SELECTED_CLASSIFIER_CHANGED = "classer_change";
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int VARIABLE_CHOOSER_MODE_ACTIVE = 0;
    public static final int VARIABLE_CHOOSER_MODE_FIXED = 1;
    public static final int VARIABLE_CHOOSER_MODE_HIDDEN = 2;
    private int nClasses;

    private int[] previousClassification;
    private int[] classification;
    private final boolean update;
    transient private JSlider classSlider;
    transient protected JComboBox classifCombo;
    protected transient JComboBox variableCombo;
    transient private WholeNumberField classesField;
    protected transient String currVariableName;
    protected transient int currVariableIndex;
    private transient DataSetForApps dataSet;
    protected transient DescribedClassifier[] classers;
    private transient int currClasser;
    private transient int currOrientation = ClassifierPicker.X_AXIS;
    private int variableChooserMode = ClassifierPicker.VARIABLE_CHOOSER_MODE_HIDDEN; // default
    private ClassifierCustomGUI custGUI;
    static int CLASSIFIER_CUSTOM = 6;
    final static Logger logger = Logger.getLogger(ClassifierPicker.class
	    .getName());

    public ClassifierPicker() {
	super();
	if (VisualClassifier.debugStatus
		.equals(VisualClassifier.LayoutDebugStatus.Debug)) {
	    setBorder(BorderFactory.createTitledBorder(this.getClass()
		    .getSimpleName()));

	}

	classification = new int[0];
	nClasses = 3;
	addComponentListener(this);
	classers = new DescribedClassifier[7];

	classers[0] = new ClassifierQuantiles();
	classers[1] = new ClassifierModifiedQuantiles();
	classers[2] = new ClassifierEqualIntervals();
	classers[3] = new ClassifierStdDev();
	classers[4] = new ClassifierRawQuantiles();
	classers[5] = new ClassifierJenks();
	classers[6] = new ClassifierCustom();

	update = true;
	init();
	setPreferredSize(new Dimension(365, 35)); // these match the
	// colorRampPicker

	// this.setMinimumSize(new Dimension(200,20));
	// this.setMaximumSize(new Dimension(1000,60));
	setVariableChooserMode(variableChooserMode);
    }

    private void init() {
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	// JLabel classesLabel = new JLabel("N Classes:");
	classSlider = new JSlider(SwingConstants.HORIZONTAL, 2, 20, nClasses);

	// classSlider.setPreferredSize(new Dimension(100,30));
	classSlider.addChangeListener(new SliderListener());
	classSlider.setMajorTickSpacing(5);
	classSlider.setMinorTickSpacing(1);
	classSlider.setPaintTicks(false);
	classSlider.setPaintLabels(false);
	classSlider.setSnapToTicks(true);
	classSlider.setMinimumSize(new Dimension(100, 20));
	classSlider.setMaximumSize(new Dimension(100, 20));

	// classSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
	// this.add(classesLabel);
	this.add(classSlider);

	classesField = new WholeNumberField(nClasses, 1);
	classesField.addActionListener(this);
	this.add(classesField);

	classifCombo = new JComboBox();

	fillCombo();
	classifCombo.setMinimumSize(new Dimension(80, 20));
	classifCombo.setMaximumSize(new Dimension(120, 20));
	classifCombo.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();

		if (cb.getItemCount() > 0) {
		    int classer = cb.getSelectedIndex();

		    if (currClasser != classer) {
			ClassifierPicker.this.setClasser(classer);
			ClassifierPicker.this
				.fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED);
			ClassifierPicker.this.fireClassificationChanged();
		    } // end if

		    if (classer == ClassifierPicker.CLASSIFIER_CUSTOM) {
			if (custGUI == null) {
			    custGUI = new ClassifierCustomGUI();
			    custGUI.addActionListener(ClassifierPicker.this);
			    classers[ClassifierPicker.CLASSIFIER_CUSTOM] = custGUI.customClasser;

			}
			double[] data = dataSet
				.getNumericDataAsDouble(currVariableIndex);
			custGUI.setData(data);
			custGUI.show();
			custGUI.setVisible(true);
		    }

		} // end if count > 0
	    } // end inner class
	}); // end add listener

	JLabel twoSpacesclassifCombo = new JLabel("  ");
	this.add(twoSpacesclassifCombo);

	// this.add(classifLabel);
	this.add(classifCombo);

	variableCombo = new JComboBox();
	variableCombo.setMinimumSize(new Dimension(80, 20));
	variableCombo.setMaximumSize(new Dimension(120, 20));
	variableCombo.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();

		if (cb.getItemCount() > 0) {
		    if (currVariableIndex != cb.getSelectedIndex()) {
			String arrayName = (String) cb.getSelectedItem();
			currVariableName = arrayName;
			currVariableIndex = cb.getSelectedIndex();
			ClassifierPicker.this
				.fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED);

			// ClassifierPicker.this.setVariableColumn(index + 1);
			// //skip header
			// + 1
			ClassifierPicker.this.fireClassificationChanged();
		    } // end if
		} // end if count > 0
	    } // end inner class
	}); // end add listener

	JLabel twoSpacesVariablePicker = new JLabel("  ");
	this.add(twoSpacesVariablePicker);

	// this.add(updateLabel);
	this.add(variableCombo);

	// this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    private void fillCombo() {
	for (DescribedClassifier element : classers) {
	    classifCombo.addItem(element.getFullName());
	}
    }

    public void changeOrientation(int orientation) {
	if (orientation == currOrientation) {
	    return;
	} else if (orientation == ClassifierPicker.X_AXIS) {
	    Component[] comps = new Component[getComponentCount()];

	    for (int i = 0; i < getComponentCount(); i++) {
		comps[i] = getComponent(i);
	    }

	    classSlider.setOrientation(SwingConstants.HORIZONTAL);
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	    for (int i = 0; i < getComponentCount(); i++) {
		this.add(comps[i]);
	    }

	    currOrientation = ClassifierPicker.X_AXIS;
	    revalidate();
	} else if (orientation == ClassifierPicker.Y_AXIS) {
	    Component[] comps = new Component[getComponentCount()];

	    for (int i = 0; i < getComponentCount(); i++) {
		comps[i] = getComponent(i);
	    }

	    // this.classSlider.setOrientation(JSlider.VERTICAL);
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	    for (int i = 0; i < getComponentCount(); i++) {
		this.add(comps[i]);
	    }

	    currOrientation = ClassifierPicker.Y_AXIS;
	    revalidate();
	}
    }

    // start component event handling
    // note: this class only listens to itself
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
	float ratio = ((float) getWidth() / (float) getHeight());

	if ((ratio >= 1) && (currOrientation == ClassifierPicker.Y_AXIS)) {
	    changeOrientation(ClassifierPicker.X_AXIS);
	}

	if ((ratio < 1) && (currOrientation == ClassifierPicker.X_AXIS)) {
	    changeOrientation(ClassifierPicker.Y_AXIS);
	}
    }

    // end component event handling

    public void setDataSet(DataSetForApps data) {
	dataSet = data;
	setVariableNames(dataSet.getAttributeNamesNumeric());
	if (dataSet.getNumberNumericAttributes() > 1) {
	    variableCombo.setSelectedIndex(0);
	}
    }

    public void setAdditionalData(DataSetForApps dataIn) {
	currVariableIndex = variableCombo.getSelectedIndex();
	dataSet = dataSet.appendDataSet(dataIn);

	setVariableNames(dataSet.getAttributeNamesNumeric());
	variableCombo.setSelectedIndex(currVariableIndex);
    }

    public void setVariableNames(String[] variableNames) {
	variableCombo.removeAllItems();

	for (String element : variableNames) {
	    variableCombo.addItem(element);
	}
    }

    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();

	if (command.equals(WholeNumberField.COMMAND_NEW_VAL)) {
	    resetClassifications();
	    WholeNumberField holeField = (WholeNumberField) e.getSource();
	    nClasses = holeField.getValue();

	    if (classSlider.getMinimum() > nClasses) {
		classSlider.setValueIsAdjusting(true);
		classSlider.setValue(classSlider.getMinimum());
	    } else if (classSlider.getMaximum() < nClasses) {
		classSlider.setValueIsAdjusting(true);
		classSlider.setValue(classSlider.getMaximum());
	    } else {
		/*
		 * //removed by jin chen to fix the bug that seting number in
		 * the text field won't change the number of category although
		 * it move slider's knob
		 * this.classSlider.setValueIsAdjusting(true);
		 */
		classSlider.setValue(nClasses);
	    }
	}
	// it came from a custom classer!!!
	if (command.equals(COMMAND_SELECTED_CLASSIFIER_CHANGED)) {

	    setClasser(CLASSIFIER_CUSTOM);
	    classers[CLASSIFIER_CUSTOM] = custGUI.getCustomClasser();
	    setNClasses(custGUI.getCustomClasser().breaks.length - 1);
	    fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED);
	    ClassifierPicker.this.fireClassificationChanged();
	}

    }

    /**
     * implements ActionListener
     */
    public void addActionListener(ActionListener l) {
	listenerList.add(ActionListener.class, l);
	fireActionPerformed(ClassifierPicker.COMMAND_BEAN_REGISTERED);
    }

    /**
     * removes an ActionListener from the component
     */
    public void removeActionListener(ActionListener l) {
	listenerList.remove(ActionListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @see EventListenerList
     */
    private void fireActionPerformed(String command) {
	if (update) {
	    // Guaranteed to return a non-null array
	    Object[] listeners = listenerList.getListenerList();
	    ActionEvent e = null;

	    // Process the listeners last to first, notifying
	    // those that are interested in this event
	    for (int i = listeners.length - 2; i >= 0; i -= 2) {
		if (listeners[i] == ActionListener.class) {
		    // Lazily create the event:
		    if (e == null) {
			e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				command);
		    }

		    ((ActionListener) listeners[i + 1]).actionPerformed(e);
		}
	    } // next i
	} // end if
    }

    public void setNClasses(int nClasses) {
	classSlider.setValue(nClasses);
	classesField.setValue(nClasses);
	this.nClasses = nClasses;
	resetClassifications();

    }

    public int getNClasses() {
	return nClasses;
    }

    public int getVariableChooserMode() {
	return variableChooserMode;
    }

    public void setVariableChooserMode(int variableChooserMode) {
	this.variableChooserMode = variableChooserMode;

	if (variableChooserMode == ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE) {
	    variableCombo.setEnabled(true);
	    variableCombo.setVisible(true);
	} else if (variableChooserMode == ClassifierPicker.VARIABLE_CHOOSER_MODE_FIXED) {
	    variableCombo.setEnabled(false);
	    variableCombo.setVisible(true);
	} else if (variableChooserMode == ClassifierPicker.VARIABLE_CHOOSER_MODE_HIDDEN) {
	    variableCombo.setVisible(false);
	} else {
	    throw new IllegalArgumentException(
		    "ClassifierPicker.setVariableChooserMode,"
			    + " unknown mode encountered, mode = "
			    + variableChooserMode);
	}
    }

    public Classifier getClasser() {
	return classers[currClasser];
    }

    public DataSetForApps getDataSet() {
	return dataSet;
    }

    private void setClasser(int classer) {
	currClasser = classer;
    }

    public int getCurrVariableIndex() {
	return currVariableIndex;
    }

    public void setCurrVariableIndex(int currVariableIndex) {
	this.currVariableIndex = currVariableIndex;
	variableCombo.setSelectedIndex(currVariableIndex);
	fireClassificationChanged(); // inserted fah 7 oct 03
    }

    public void dataSetModified(ColumnAppendedEvent e) {
	if (e.getEventType() == ColumnAppendedEvent.ChangeType.TYPE_EXTENDED) {
	    // XXX unsafe, modifying for build only...
	    // Object[] newData = (Object[])e.getNewData();

	    Object[] newData = null;
	    DataSetForApps newDataSet = new DataSetForApps(newData);
	    dataSet = newDataSet;
	    String[] newVarNames = newDataSet.getAttributeNamesOriginal();
	    for (String element : newVarNames) {
		variableCombo.addItem(element);
	    }

	}
    }

    /**
     * 
     * @return the results of the current classification on the current data set
     */
    private int[] findClassification() {

	int nClasses = getNClasses();
	Classifier classer = getClasser();
	double[] data = dataSet.getNumericDataAsDouble(currVariableIndex);
	if (data.length != classification.length) {
	    if (classer instanceof ClassifierCustom) {
		ClassifierCustom cust = (ClassifierCustom) classer;
		cust.breaks = findBreaks(data, previousClassification, nClasses);
	    }
	    classification = classer.classify(data, nClasses);
	    previousClassification = classification;
	}
	return classification;

    }

    static double[] findBreaks(double[] data, int[] classification, int nClasses) {
	double[] breaks = new double[nClasses + 1];
	// all breaks start at zero
	// the break for class N is the maximum value
	// we (optimistically) assume the classes don't overlap
	for (int i = 0; i < data.length; i++) {
	    double val = data[i];
	    if (val < breaks[0]) {
		breaks[0] = val;
	    }
	    if (val > breaks[nClasses]) {
		breaks[nClasses] = val;
	    }
	    int classed = classification[i];
	    if (val > breaks[classed + 1]) {
		breaks[classed + 1] = val;
	    }
	}

	// we need to account for empty classes
	double prevBreak = breaks[0];
	for (int i = 0; i < breaks.length; i++) {
	    if (prevBreak > breaks[i]) {
		breaks[i] = prevBreak;
	    }
	    prevBreak = breaks[i];
	}
	return breaks;
    }

    void resetClassifications() {
	if (classification.length > 0) {
	    previousClassification = classification;
	}
	classification = new int[0];
    }

    public void tableChanged(TableModelEvent e) {
	resetClassifications();
	int col = e.getColumn();
	setCurrVariableIndex(col);

    }

    /**
     * adds an ClassificationListener
     */
    public void addClassificationListener(ClassificationListener l) {
	listenerList.add(ClassificationListener.class, l);
    }

    /**
     * removes an ClassificationListener from the component
     */
    public void removeClassificationListener(ClassificationListener l) {
	listenerList.remove(ClassificationListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @see EventListenerList
     */
    public void fireClassificationChanged() {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	ClassificationEvent e = null;

	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == ClassificationListener.class) {
		// Lazily find the classification
		int[] newClassification = findClassification();
		// Lazily create the event:
		if (e == null) {
		    e = new ClassificationEvent(this, newClassification);
		}

		((ClassificationListener) listeners[i + 1])
			.classificationChanged(e);
	    }
	} // next i
    }

    /** Listens to the check boxen. */
    class CheckBoxListener implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    /*
	     * if (e.getSource().equals(ClassifierPicker.this.updateBox)){ if
	     * (e.getStateChange() == ItemEvent.SELECTED &&
	     * ClassifierPicker.this.setupFinished){
	     * ClassifierPicker.this.update = true;
	     * ClassifierPicker.this.fireActionPerformed
	     * (ClassifierPicker.COMMAND_CLASSES_CHANGED); } else if
	     * (e.getStateChange() == ItemEvent.DESELECTED){
	     * 
	     * ClassifierPicker.this.update = false; } }
	     */
	}
    }

    class SliderListener implements ChangeListener {
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider) e.getSource();
	    int classes = source.getValue();

	    if (!source.getValueIsAdjusting()) {
		nClasses = classes;
		classesField.setValue(classes);
		resetClassifications();
		fireClassificationChanged();
		fireActionPerformed(ClassifierPicker.COMMAND_CLASSES_CHANGED);
	    }
	}
    }

    public static void main(String[] args) {

	ClassifierEqualIntervals classer = new ClassifierEqualIntervals();
	double[] data = { 1, 2, Double.NaN, 4, 3, 2, 1, 2, 3, 4, 66 };
	int[] classes = classer.classify(data, 4);
	double[] breaks = ClassifierPicker.findBreaks(data, classes, 4);
	for (double d : breaks) {
	    logger.info("" + d);
	}

    }

    public JComboBox getVariableCombo() {
	return variableCombo;
    }
}
