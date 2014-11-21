/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import geovista.common.data.DescriptiveStatistics;
import geovista.common.ui.MultiSlider;

public class ClassifierCustomGUI extends JPanel implements ActionListener,
		ChangeListener {
	Stroke connectorStroke;

	MultiSlider mSlider;
	JPanel mainPanel;
	JPanel textFieldPanel;
	JPanel classCountPanel;

	static int DEFAULT_N_CLASSES = 3;
	static int DEFUALT_LABEL_WIDTH = 4;
	JComboBox classBox;
	JCheckBox inverseBox;
	ArrayList<NumberTextField> classBoundaryFields;
	ArrayList<NumberTextField> classCountFields;

	boolean debugGUI = false;
	protected final static Logger logger = Logger
			.getLogger(ClassifierCustomGUI.class.getName());

	ClassifierCustom customClasser;
	ClassifierEqualIntervals equalClasser;
	JFrame frame;
	double[] savedData;

	ClassifierCustomGUI() {
		frame = new JFrame("Custom Classifier");
		frame.add(this);
		makeConnectorStroke();
		customClasser = new ClassifierCustom();
		equalClasser = new ClassifierEqualIntervals();
		int defaultNClasses = 3;
		classCountPanel = new JPanel();
		classCountPanel.setOpaque(false);
		if (debugGUI) {
			classCountPanel.setBorder(BorderFactory
					.createLineBorder(Color.cyan));
		}
		classCountFields = new ArrayList<NumberTextField>();

		createClassLabels(defaultNClasses);

		classBoundaryFields = new ArrayList<NumberTextField>();
		for (int i = 0; i < defaultNClasses + 1; i++) {
			double d = 0.01 * i;
			NumberTextField textField = new NumberTextField(String.valueOf(d));
			textField.index = i;
			classBoundaryFields.add(textField);
		}
		textFieldPanel = new JPanel();
		textFieldPanel.setOpaque(false);
		if (debugGUI) {
			textFieldPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		textFieldPanel.setPreferredSize(new Dimension(200, 20));

		mSlider = new MultiSlider();
		mSlider.setOpaque(false);
		mSlider.addChangeListener(this);
		mSlider.setPreferredSize(new Dimension(200, 50));
		setNClasses(defaultNClasses);
		setLayout(new BorderLayout());
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		if (debugGUI) {
			mainPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		}
		BoxLayout bLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(bLayout);
		mainPanel.add(classCountPanel);
		mainPanel.add(mSlider);
		mainPanel.add(textFieldPanel);

		this.add(mainPanel);

		String[] classArray = { "2", "3", "4", "5", "6", "7", "8" };

		classBox = new JComboBox(classArray);

		classBox.addActionListener(this);
		classBox.setSelectedIndex(1);
		JPanel nClassPanel = new JPanel();
		nClassPanel.setLayout(new BorderLayout());
		nClassPanel.setOpaque(false);
		nClassPanel.add(new JLabel("N Classes:"), BorderLayout.NORTH);
		nClassPanel.add(classBox);
		inverseBox = new JCheckBox("Invert?");
		inverseBox.setSelected(false);
		inverseBox.addActionListener(this);
		nClassPanel.add(inverseBox, BorderLayout.SOUTH);

		this.add(nClassPanel, BorderLayout.EAST);
		setPreferredSize(new Dimension(500, 100));
		mSlider.addChangeListener(this);
	}

	private void createClassLabels(int nClasses) {
		classCountPanel.removeAll();
		classCountFields.clear();
		for (int i = 0; i < nClasses; i++) {
			NumberTextField classLabel = new NumberTextField(String.valueOf(0d));
			classLabel.setCount(0);
			classLabel.setEditable(false);
			classLabel.index = i;
			classCountFields.add(classLabel);
			classCountPanel.add(classLabel);
		}
	}

	// normally this comes from the combobox
	void setNClasses(int nClasses) {
		mSlider.removeChangeListener(this);
		mSlider.setNumberOfThumbs(nClasses - 1);
		textFieldPanel.removeAll();
		classBoundaryFields = new ArrayList<NumberTextField>();
		for (int i = 0; i < nClasses + 1; i++) {
			double d = 0.01 * i;
			NumberTextField textField = new NumberTextField(String.valueOf(d));

			textField.setColumns(DEFUALT_LABEL_WIDTH);
			textField.addActionListener(this);
			textField.index = i;
			classBoundaryFields.add(textField);
		}
		classBoundaryFields.get(0).setEditable(false);
		classBoundaryFields.get(classBoundaryFields.size() - 1).setEditable(
				false);
		for (NumberTextField lab : classBoundaryFields) {
			textFieldPanel.add(lab);
		}
		revalidate();
		if (savedData != null) {
			doCustomClassification(savedData);
		}

		mSlider.addChangeListener(this);
	}

	void setData(double[] data) {
		savedData = data;

		customClasser.breaks = equalClasser.getEqualBoundaries(data,
				classCountFields.size());
		doCustomClassification(data);

	}

	private void doCustomClassification(double[] data) {
		double min = DescriptiveStatistics.min(data);
		double max = DescriptiveStatistics.max(data);
		logger.finest("min = " + min);
		logger.finest("max = " + max);
		int intMin = (int) (min * 100d);
		int intMax = (int) (max * 100d);
		mSlider.setMinimum(intMin);
		mSlider.setMaximum(intMax);

		mSlider.setInverted(customClasser.inverse);

		// da breaks

		int nClasses = classify(data, min, max);

		int[] classes = customClasser.classify(data,
				customClasser.breaks.length - 1);
		if (classCountFields.size() != nClasses) {
			createClassLabels(nClasses);
		}

		fillClassCounts(classes);
		mSlider.setNumberOfThumbs(nClasses - 1);

		setClassBoundaryFields();

		for (int i = 1; i < classBoundaryFields.size() - 1; i++) {
			double val = customClasser.breaks[i];
			int intVal = (int) (val * 100d);
			mSlider.setValueAt(i - 1, intVal);
		}

	}

	private void setClassBoundaryFields() {
		for (int i = 0; i < classBoundaryFields.size(); i++) {
			NumberTextField field = classBoundaryFields.get(i);
			logger.finest("break " + i + ": " + customClasser.breaks[i]);
			field.setValue(customClasser.breaks[i]);

		}
	}

	private int classify(double[] data, double min, double max) {
		String nString = (String) classBox.getSelectedItem();
		int nClasses = Integer.valueOf(nString);
		int nBreaks = nClasses + 1;// fence post
		double[] breaks = customClasser.breaks;

		if (breaks == null) {
			customClasser.classify(data, nClasses);
			breaks = customClasser.breaks;
		}
		if (breaks.length < nBreaks) {
			double[] newBreaks = new double[nBreaks];
			breaks[0] = min;
			for (int i = 1; i < breaks.length - 1; i++) {
				newBreaks[i] = breaks[i];
			}
			for (int i = breaks.length - 1; i < newBreaks.length; i++) {
				// XXX we would rather have nicely spaced breaks here
				newBreaks[i] = breaks[breaks.length - 2];
			}
			newBreaks[nBreaks - 1] = max;
			customClasser.breaks = newBreaks;
			this.repaint();

		}

		if (breaks.length > nBreaks) {
			double[] newBreaks = new double[nBreaks];
			newBreaks[0] = min;
			for (int i = 1; i < newBreaks.length - 1; i++) {
				newBreaks[i] = breaks[i];
			}
			newBreaks[nBreaks - 1] = max;
			customClasser.breaks = newBreaks;
		}
		return nClasses;
	}

	/* returns true if anything changed */
	private boolean fillClassCounts(int[] classes) {
		boolean somethingChanged = false;
		int[] counts = new int[classCountFields.size()];
		for (int i : classes) {

			if (i >= 0) {
				counts[i]++;
			}
		}

		for (NumberTextField field : classCountFields) {
			int currCount = field.count;
			int index = field.index;
			if (currCount != counts[index]) {
				field.setCount(counts[index]);
				somethingChanged = true;
			}
		}
		return somethingChanged;
	}

	void setClassValues(double[] classVals) {
		int[] intClassVals = new int[classVals.length];
		for (int i = 0; i < classVals.length; i++) {
			intClassVals[i] = (int) (classVals[i] * 100);
		}
		mSlider.setNumberOfThumbs(intClassVals);
	}

	public void actionPerformed(ActionEvent e) {
		mSlider.removeChangeListener(this);
		if (e.getSource() == classBox) {
			String nString = (String) classBox.getSelectedItem();
			int nClasses = Integer.valueOf(nString);
			setNClasses(nClasses);

		}
		if (e.getSource() instanceof NumberTextField) {
			NumberTextField tField = (NumberTextField) e.getSource();
			int index = tField.index;
			double val = tField.getDoubleValue() * 100;
			mSlider.setValueAt(index + 1, (int) val);

		}
		if (e.getSource().equals(inverseBox)) {
			logger.finest("inverting classer");
			customClasser.inverse = inverseBox.isSelected();
			double[] boundaries = customClasser.breaks;
			double[] newBoundaries = new double[boundaries.length];

			for (int i = 0; i < boundaries.length; i++) {
				newBoundaries[i] = boundaries[boundaries.length - i - 1];
			}
			customClasser.breaks = newBoundaries;

			doCustomClassification(savedData);

		}
		mSlider.addChangeListener(this);
		this.repaint();
		fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED);
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() != mSlider) {
			logger.severe("unexpected statechanged in "
					+ this.getClass().getName());
		}

		int whichThumb = mSlider.getCurrentThumbIndex();
		if (savedData.length > 1000 && mSlider.getValueIsAdjusting()) {
			return;
		}

		double val = mSlider.getValue();
		val = val / 100d;
		NumberTextField textField = classBoundaryFields.get(whichThumb + 1);
		textField.removeActionListener(this);
		textField.setValue(val);
		textField.addActionListener(this);

		// classBoundaryFields.get(whichThumb + 1).resi
		if (savedData != null && customClasser.breaks != null) {
			customClasser.breaks[whichThumb + 1] = val;

			int[] classes = customClasser.classify(savedData,
					customClasser.breaks.length - 1);

			boolean somethingChanged = fillClassCounts(classes);
			if (somethingChanged) {
				fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED);
			}

		}

		this.repaint();
	}

	@Override
	public void show() {

		frame.pack();
		frame.setVisible(true);
	}

	private void makeConnectorStroke() {
		float width = 2f;
		int cap = BasicStroke.CAP_ROUND;
		int join = BasicStroke.JOIN_BEVEL;

		float miterLimit = 0f;
		float[] dashPattern = { 2f, 3f };
		float dashPhase = 5f;
		connectorStroke = new BasicStroke(width, cap, join, miterLimit,
				dashPattern, dashPhase);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// if (savedData == null) {
		// return;
		// } // draw leader lines
		// Graphics2D g2 = (Graphics2D) g;
		// Stroke oldStroke = g2.getStroke();
		// g2.setStroke(connectorStroke);
		// int nBreaks = customClasser.breaks.length;
		// int nClasses = nBreaks - 1;
		// int range = mSlider.getMaximum() - mSlider.getMinimum();
		//
		// for (int i = 0; i < classBoundaryFields.size(); i++) {
		// NumberTextField field = classBoundaryFields.get(i);
		// Rectangle rect = field.getBounds();
		// int x1 = rect.x + (rect.width / 2);
		// int y1 = rect.y + rect.height;
		// if (mSlider.getModelAt(i) == null) {
		// g2.setStroke(oldStroke);
		// return;
		// }
		// int value = mSlider.getValueAt(i);
		// int distFromMin = value - mSlider.getMinimum();
		// float percentAlong = (float) distFromMin / (float) range;
		// int y2 = mSlider.getY();
		// int pixelsAlong = (int) (percentAlong * mSlider.getWidth());
		// int x2 = mSlider.getX() + pixelsAlong;
		// g2.drawLine(x1, y1, x2, y2);
		// }
		//
		//
		// g2.setStroke(oldStroke);
	}

	class NumberTextField extends JFormattedTextField {

		int index;
		int count;
		NumberFormat format;

		NumberTextField(String text) {

			// super(text);
			format = NumberFormat.getNumberInstance();

			format.setMaximumFractionDigits(3);
			JFormattedTextField tempField = new JFormattedTextField(format);
			super.setFormatterFactory(tempField.getFormatterFactory());
			// don't accept anything but numbers
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
						getToolkit().beep();
						e.consume();
					}
				}
			});
		}

		double getDoubleValue() {
			return Double.valueOf(this.getText());
		}

		void setValue(double val) {

			setText(String.valueOf(format.format(val)));
			this.repaint();
		}

		void setCount(int count) {
			this.count = count;
			setText("Count = " + count);
			// setColumns(this.getText().length());
		}
	}

	// XXX need to flesh this out.
	@SuppressWarnings("unused")
	public ClassifierCustom showClassifierDialog(double[] data,
			Classifier currClassifier) {
		return customClasser;
	}

	public ClassifierCustom getCustomClasser() {
		return customClasser;
	}

	public void setCustomClasser(ClassifierCustom customClasser) {
		this.customClasser = customClasser;
	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);

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

	}

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("geovista");
		logger.setLevel(Level.FINEST);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST);
		logger.addHandler(handler);
		logger.finest("java.version = " + System.getProperty("java.version")
				+ ", Runtime.avaialableProcessors = "
				+ Runtime.getRuntime().availableProcessors());

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ClassifierCustomGUI picker = new ClassifierCustomGUI();
		frame.add(picker);

		ClassifierEqualIntervals classer = new ClassifierEqualIntervals();
		double[] data = { 1, 2, Double.NaN, 4, 3, 2, 1, 2, 3, 4, 6 };
		int[] classes = classer.classify(data, 4);
		logger.info(Arrays.toString(classes));
		picker.setData(data);
		// picker.setClassValues(breaks);

		ClassifierCustom cust = new ClassifierCustom();
		cust.classify(data, 3);
		frame.pack();
		frame.setVisible(true);
	}
}
