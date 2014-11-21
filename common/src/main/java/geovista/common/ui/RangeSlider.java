/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Isaac Brewer*/

package geovista.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* *******************************************************************
 Implementation of class RangeSlider
 ******************************************************************* */
/**
 * Generally describe RangeSlider in here.
 *
 * \n * @author Isaac Brewer (isaacbrewer@hotmail.com)
 */
/**
 * put your documentation comment here
 */
public class RangeSlider extends JPanel implements ChangeListener {
	protected final static Logger logger = Logger.getLogger(RangeSlider.class
			.getName());
	private static final String DEFAULT_TITLE = "No Title";
	private static final String DEFAULT_ERROR = "not specified";
	private static final Color DEFAULT_LABEL_PANEL_COLOR = Color.black;
	private static final Color DEFAULT_SLIDER_COLOR = Color.lightGray;
	private static final Color DEFAULT_FONT_COLOR = Color.white;
	private static final int DEFAULT_NUMBER_OF_THUMBS = 2;
	private static final int DEFAULT_SLIDER_MINIMUM = 0;
	private static final int DEFAULT_SLIDER_MAXIMUM = 100;
	private static final int DEFAULT_MAJOR_TICKS = 5;
	private static final int DEFAULT_MINOR_TICKS = 1;
	private static final int DEFAULT_ORIENTATION = SwingConstants.HORIZONTAL;
	private static final int NUM_MAJOR_TICKS = 5;
	private static final int NUM_MINOR_TICKS = 10;
	private static final String DEFAULT_NAME = "RangeSlider";
	JPanel labelPanel;
	JPanel sliderPanel;
	JLabel sliderNumberDisplayed[];
	MultiSlider rangeSlider;
	private boolean divideResults;
	// private int divisor;
	private transient double divisor;
	protected int numberOfThumbs;
	// private transient String tabTitle = DEFAULT_TITLE;
	private transient String title = DEFAULT_TITLE;
	private transient String titleValues;
	private transient String toolTip = DEFAULT_ERROR;
	private transient String units = DEFAULT_ERROR;
	protected String attributeName = DEFAULT_NAME;
	protected Color sliderColor = DEFAULT_SLIDER_COLOR;
	protected Color labelPanelColor = DEFAULT_LABEL_PANEL_COLOR;
	protected Color fontColor = DEFAULT_FONT_COLOR;
	protected int orientationConstant = DEFAULT_ORIENTATION;
	protected int sliderMinimum = DEFAULT_SLIDER_MINIMUM;
	protected int sliderMaximum = DEFAULT_SLIDER_MAXIMUM;
	protected int minorTicks = DEFAULT_MINOR_TICKS;
	protected int majorTicks = DEFAULT_MAJOR_TICKS;
	private double minorTicksDouble;
	private double majorTicksDouble;
	private int intmin;
	private int intmax;
	private transient Object data;
	private transient double[] ranges; // min (0) and max (1) values of slider.
	private transient boolean attChanged = true;
	private transient boolean rangesChanged = true;
	private transient boolean titleChanged = false;
	private transient boolean valueAdjusting = false;
	private transient boolean paintLabel = true;
	transient protected EventListenerList listenerList = new EventListenerList();
	private transient double totalLength;
	private transient int valueAt0;
	private transient int valueAt1;

	/**
	 * put your documentation comment here
	 */
	public RangeSlider() {
		super();
		divideResults = false;
		// initialize(DEFAULT_NAME, DEFAULT_ORIENTATION,
		// DEFAULT_NUMBER_OF_THUMBS,
		// DEFAULT_SLIDER_MINIMUM, DEFAULT_SLIDER_MAXIMUM, DEFAULT_MAJOR_TICKS,
		// DEFAULT_MINOR_TICKS, DEFAULT_DIVISOR);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param String
	 *            attributeName
	 * @param int orientationConstant
	 * @param int numberOfThumbs
	 * @param int sliderMinimum
	 * @param int sliderMaximum
	 * @param int majorTicks
	 * @param int minorTicks
	 * @param int divisor
	 */
	public RangeSlider(String attributeName, int orientationConstant,
			int numberOfThumbs, int sliderMinimum, int sliderMaximum,
			int majorTicks, int minorTicks,
			// int divisor) {
			double divisor) {
		super();
		divideResults = false;
		initialize(attributeName, orientationConstant, numberOfThumbs,
				sliderMinimum, sliderMaximum, majorTicks, minorTicks, divisor);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param String
	 *            attributeName
	 * @param double sliderMinimum
	 * @param // 0.0 double sliderMaximum
	 * @param // 5.98 double precision
	 */
	public RangeSlider(String attributeName, double sliderMinimum, // 0.0
			double sliderMaximum, // 5.98
			double precision) // 0.01
	{
		divisor = (int) (1 / precision);
		divideResults = true;
		totalLength = (sliderMaximum - sliderMinimum);
		majorTicksDouble = totalLength / NUM_MAJOR_TICKS;
		minorTicksDouble = totalLength / NUM_MINOR_TICKS;
		initialize(attributeName, DEFAULT_ORIENTATION,
				DEFAULT_NUMBER_OF_THUMBS, (int) (sliderMinimum * divisor), // 0
				(int) (sliderMaximum * divisor), // 598
				(int) (majorTicksDouble * divisor), // 120
				(int) (minorTicksDouble * divisor), // ~60
				divisor); // 100
	}

	// ============================ METHODS
	// ========================================== //
	private void initialize(String attributeName, int orientationConstant,
			int numberOfThumbs, int sliderMinimum, int sliderMaximum,
			int majorTicks, int minorTicks, double divisor) {
		logger.finest("Inside initialize.");
		removeAll();
		this.attributeName = attributeName;
		this.numberOfThumbs = numberOfThumbs;
		this.orientationConstant = orientationConstant;
		this.sliderMinimum = sliderMinimum;
		this.sliderMaximum = sliderMaximum;
		this.majorTicks = majorTicks;
		this.minorTicks = minorTicks;
		// MTW. We should do be careful about poorly-initialized data
		if (divisor == 0.0) {
			System.err
					.println("RangeSlider.initialize(), divisor is 0.0, defaulting to 1");
			divisor = 1;
		}
		this.divisor = divisor;
		setLayout(new BorderLayout());
		// this.sliderPanel = new JPanel();
		// this.labelPanel = new JPanel();
		rangeSlider = new MultiSlider();
		int height = 80;
		int width = 425;
		Dimension sliderSize = new Dimension(width, height);
		rangeSlider.setPreferredSize(sliderSize);
		rangeSlider.setPaintTicks(true);
		rangeSlider.setPaintLabels(paintLabel);
		rangeSlider.setBounded(true);
		rangeSlider.setOrientation(orientationConstant);
		rangeSlider.setMaximum(sliderMaximum);
		rangeSlider.setMinimum(sliderMinimum);
		rangeSlider.setMajorTickSpacing(majorTicks);
		rangeSlider.setMinorTickSpacing(minorTicks);
		rangeSlider.setPaintLabels(paintLabel);
		rangeSlider.setNumberOfThumbs(numberOfThumbs);
		rangeSlider.setToolTipText(toolTip);
		rangeSlider.setForeground(fontColor);
		rangeSlider.setBackground(sliderColor);
		add(rangeSlider);
		// sliderPanel.setBackground(sliderColor);
		// sliderPanel.add(rangeSlider);
		// Identify whether this slider needs special labels
		if (divisor != 1) {
			generateNewLabels(divisor, sliderMinimum, sliderMaximum);
		}
		// labelPanel.setBackground(labelPanelColor);
		// labelPanel.setVisible(true);
		/*
		 * sliderNumberDisplayed = new JLabel[numberOfThumbs]; for (int i = 0; i
		 * < numberOfThumbs; i++) { sliderNumberDisplayed[i] = new JLabel();
		 * sliderNumberDisplayed
		 * [i].setHorizontalAlignment(SwingConstants.CENTER);
		 * sliderNumberDisplayed[i].setForeground(fontColor);
		 * sliderNumberDisplayed
		 * [i].setText(Double.toString((double)rangeSlider.getValueAt
		 * (i)/divisor)); // + " " + getUnits());
		 * labelPanel.add(sliderNumberDisplayed[i]); }
		 */
		// setBackground(getLabelPanelColor());
		setVisible(true);
		// add(sliderPanel);
		// add(sliderPanel, BorderLayout.CENTER);
		// add(labelPanel, BorderLayout.SOUTH);
		rangeSlider.addChangeListener(new RangeSliderHandler());
	}

	/**
	 * Sets the legend
	 * 
	 * @param legend
	 */
	protected void setRangeSliderLegend(RangeSliderLegend legend) {
	}

	/**
	 * If labels are a value other than a double, this method converts and sets
	 * the new labels in the corect format
	 * 
	 * @param divisor
	 * @param sliderMinimum
	 * @param sliderMaximum
	 */
	private void generateNewLabels(double divisor, int sliderMinimum,
			int sliderMaximum) {
		Hashtable convertedLabels = new Hashtable();
		int j = 0;
		for (int i = sliderMinimum; i < sliderMaximum; i += divisor, j++) {
			createSliderLabel(convertedLabels, i, divisor);
		}
		// Now, convert and explicitly add our terminating value
		createSliderLabel(convertedLabels, sliderMaximum, divisor);
		rangeSlider.setLabelTable(convertedLabels);
	}

	/**
	 * Hashes the created lables
	 * 
	 * @param hashLabels
	 * @param value
	 * @param divisor
	 */
	private void createSliderLabel(Hashtable hashLabels, int value,
			double divisor) {
		double dividedValue = value / divisor;
		logger.finest("GvAS.createSliderLabel(), value = " + value
				+ ", dividedValue = " + dividedValue);
		String stringLabel = Double.toString(dividedValue);
		JLabel label = new JLabel(stringLabel);
		hashLabels.put(new Integer(value), label);
	}

	class RangeSliderHandler implements ChangeListener {

		/**
		 * put your documentation comment here
		 * 
		 * @param ce
		 */
		public void stateChanged(ChangeEvent ce) {
			MultiSlider slider = (MultiSlider) ce.getSource();
			toolTip = " ";
			for (int i = 0; i < numberOfThumbs; i++) {
				double valueAdjusted = slider.getValueAt(i) / divisor;
				try {
					// sliderNumberDisplayed[i].setText("	" +
					// Double.toString(valueAdjusted));
					// + " " + getUnits());
					if (i == rangeSlider.getCurrentThumbIndex()) {
						toolTip = Double.toString(rangeSlider.getValueAt(i)
								/ divisor);
					}
					// toolTip = toolTip +
					// Double.toString((double)rangeSlider.getValueAt
					// (i)/divisor) + " ";
					if (i == 0) {
						titleValues = Double.toString(valueAdjusted)
								+ "           "
								+ title
								+ "           "
								+ Double.toString(rangeSlider.getValueAt(1)
										/ divisor);
					} else {
						titleValues = Double.toString(rangeSlider.getValueAt(0)
								/ divisor)
								+ "           "
								+ title
								+ "           "
								+ Double.toString(valueAdjusted);
					}
					rangeSlider.setBorder(BorderFactory.createTitledBorder(
							BorderFactory.createRaisedBevelBorder(),
							titleValues, TitledBorder.CENTER,
							TitledBorder.ABOVE_TOP));
					if (!rangeSlider.getValueIsAdjusting() || valueAdjusting) {
						fireChangeEvent();
					}
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
			rangeSlider.setToolTipText(toolTip);
			// rangeSlider.setCurrentThumbColor(Color.blue);
		}
	}

	/**
	 * Returns and integer array of the selected values on the two thumbs
	 * 
	 * @return
	 */
	public int[] getValue() {
		int numThumbs = rangeSlider.getNumberOfThumbs();
		int[] arrayReturnValues = new int[numThumbs];
		for (int i = 0; i < numThumbs; i++) {
			arrayReturnValues[i] = rangeSlider.getValueAt(i);
		}
		return arrayReturnValues;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public double[] getValueDoubles() {
		int numThumbs = rangeSlider.getNumberOfThumbs();
		double[] arrayReturnValues = new double[numThumbs];
		for (int i = 0; i < numThumbs; i++) {
			if (divideResults) {
				arrayReturnValues[i] = (rangeSlider.getValueAt(i) / divisor);
			} else {
				arrayReturnValues[i] = rangeSlider.getValueAt(i);
			}
		}
		return arrayReturnValues;
	}

	/**
	 * METHODS FOR CONFIGURING THE SLIDERS
	 * 
	 * @param s
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param s
	 */
	public void setTitle(String s) {
		if (s == null) {
			return;
		}
		if (!title.equals(s) || title.equals(RangeSlider.DEFAULT_TITLE)) {
			title = s;
			titleChanged = true;
			if (divideResults == false && rangesChanged == false) {
				double precision = 1.0;
				divisor = 1.0 / precision;
				divideResults = true;
				totalLength = (ranges[1] - ranges[0]);
				majorTicksDouble = totalLength / NUM_MAJOR_TICKS;
				minorTicksDouble = totalLength / NUM_MINOR_TICKS;
				int minVal = (int) Math.floor(ranges[0] * divisor);
				int maxVal = (int) Math.ceil(ranges[1] * divisor);
				initialize(attributeName, DEFAULT_ORIENTATION,
						DEFAULT_NUMBER_OF_THUMBS, minVal, maxVal, // 598
						(int) (majorTicksDouble * divisor), // 120
						(int) (minorTicksDouble * divisor), // ~60
						divisor);
				rangeSlider.setValueAt(0, minVal);
				rangeSlider.setValueAt(1, maxVal);
				logger.finest("After initialize in setTitle.");
				attChanged = true;
				rangesChanged = true;
				titleChanged = false;
				// repaint();
				// set the border name to include variable name and maximum and
				// minimum numbers.
				// if ((rangeSlider != null)){
				titleValues = Double.toString(rangeSlider.getValueAt(0)
						/ divisor)
						+ "           "
						+ title
						+ "           "
						+ Double.toString(rangeSlider.getValueAt(1) / divisor);
				rangeSlider.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createRaisedBevelBorder(), titleValues,
						TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
				// }else {
				// titleValues = title;
				// }
				repaint();
			} else {
				attChanged = false;
			}
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	@Override
	public String getToolTipText() {
		return toolTip;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param tt
	 */
	@Override
	public void setToolTipText(String tt) {
		toolTip = tt;
		rangeSlider.setToolTipText(toolTip);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return attributeName;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param s
	 */
	public void setUnits(String s) {
		units = s;
		for (int i = 0; i < numberOfThumbs; i++) {
			double valueAdjusted = rangeSlider.getValueAt(i) / divisor;
			try {
				// sliderNumberDisplayed[i].setText("	" +
				// Double.toString(valueAdjusted));
				// + " " + getUnits());
				rangeSlider.setToolTipText(Double.toString(valueAdjusted));
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		repaint();
	}

	// Re-added this code. I'm not sure where IB would like it
	public void setDataObject(Object data) {
		this.data = data;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Object getDataObject() {
		return data;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	/*
	 * public String getTabTitle () { if (tabTitle == null) { return
	 * DEFAULT_TITLE; } else return tabTitle; } / put your documentation comment
	 * here
	 * 
	 * @param tt
	 */
	/*
	 * public void setTabTitle (String tt) { tabTitle = tt; if (this.legend !=
	 * null) { this.legend.updateTabTitle(tt); } repaint(); } / put your
	 * documentation comment here
	 * 
	 * @return
	 */
	public Color getLabelPanelColor() {
		return labelPanelColor;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lpc
	 */
	public void setLabelPanelColor(Color lpc) {
		labelPanelColor = lpc;
		labelPanel.setBackground(labelPanelColor);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Color getSliderColor() {
		return sliderColor;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sc
	 */
	public void setSliderColor(Color sc) {
		sliderColor = sc;
		rangeSlider.setBackground(sliderColor);
		sliderPanel.setBackground(sliderColor);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Color getFontColor() {
		return fontColor;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param fc
	 */
	public void setFontColor(Color fc) {
		fontColor = fc;
		rangeSlider.setForeground(fontColor);
		for (int i = 0; i < numberOfThumbs; i++) {
			sliderNumberDisplayed[i].setForeground(fontColor);
		}
		repaint();
	}

	public void setValueAdjusting(boolean valueAdjusting) {
		this.valueAdjusting = valueAdjusting;
	}

	public boolean getValueAdjusting() {
		return valueAdjusting;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMaximum() {
		return rangeSlider.getMaximum();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param max
	 */
	public void setMaximum(int max) {
		logger.finest("MAX = " + max);
		rangeSlider.setMaximum(max);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param max
	 */
	public void setMaxValue(double max) {
		int intmax = (int) (max * divisor);
		rangeSlider.setValueAt(1, intmax);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMinimum() {
		return rangeSlider.getMinimum();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param min
	 */
	public void setMinValue(double min) {
		int intmin = (int) (min * divisor);
		rangeSlider.setValueAt(0, intmin);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param min
	 */
	public void setMinimum(int min) {
		rangeSlider.setMinimum(min);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param minMax
	 */
	public void setMinMaxValue(double[] minMax) {
		logger.finest("Set up ranges in slider...");
		if (minMax == null) {
			return;
		}
		if ((divideResults == false && attChanged == false)) {
			double precision = 1.0;
			divisor = 1.0 / precision;
			divideResults = true;
			totalLength = (minMax[1] - minMax[0]);
			majorTicksDouble = totalLength / NUM_MAJOR_TICKS;
			minorTicksDouble = totalLength / NUM_MINOR_TICKS;
			int minVal = (int) Math.floor(minMax[0] * divisor);
			int maxVal = (int) Math.ceil(minMax[1] * divisor);
			initialize(attributeName, DEFAULT_ORIENTATION,
					DEFAULT_NUMBER_OF_THUMBS, minVal, // 0
					maxVal, // 598
					(int) (majorTicksDouble * divisor), // 120
					(int) (minorTicksDouble * divisor), // ~60
					divisor);
			logger.finest("After initialize.");
			attChanged = true;
			rangesChanged = true;
			titleChanged = false; // 100
			rangeSlider.setValueAt(0, minVal);
			rangeSlider.setValueAt(1, maxVal);
			if ((title != null)) {
				titleValues = Double.toString(rangeSlider.getValueAt(0)
						/ divisor)
						+ "       "
						+ title
						+ "       "
						+ Double.toString(rangeSlider.getValueAt(1) / divisor);
				rangeSlider.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createRaisedBevelBorder(), titleValues,
						TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
			}
		} else {
			if (titleChanged == false && title != null) {
				rangesChanged = true;
			} else {
				rangesChanged = false;
			}
			ranges = minMax;
			intmin = (int) Math.floor(minMax[0] * divisor);
			intmax = (int) Math.ceil(minMax[1] * divisor);
			rangeSlider.setValueAt(0, intmin);
			rangeSlider.setValueAt(1, intmax);
		}
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param rangeTitle
	 */
	public void setRangeTitle(Object[] rangeTitle) {
		logger.finest("set range and title...");
		if (rangeTitle == null) {
			return;
		}
		logger.finest((String) rangeTitle[0]);
		ranges = (double[]) rangeTitle[1];
		logger.finest("ranges " + ranges[0]);
		if (title != (String) rangeTitle[0]) {
			logger.finest("initialize..." + (String) rangeTitle[0]);
			title = (String) rangeTitle[0];
			double precision = 1.0;
			divisor = 1.0 / precision;
			totalLength = (ranges[1] - ranges[0]);
			majorTicksDouble = totalLength / NUM_MAJOR_TICKS;
			minorTicksDouble = totalLength / NUM_MINOR_TICKS;
			intmin = (int) Math.floor(ranges[0] * divisor);
			intmax = (int) Math.ceil(ranges[1] * divisor);
			initialize(attributeName, DEFAULT_ORIENTATION,
					DEFAULT_NUMBER_OF_THUMBS, intmin, // 0
					intmax, // 598
					(int) (majorTicksDouble * divisor), // 120
					(int) (minorTicksDouble * divisor), // ~60
					divisor);
			rangeSlider.setValueAt(0, intmin);
			rangeSlider.setValueAt(1, intmax);
			titleValues = Double.toString(intmin) + "           " + title
					+ "           " + Double.toString(intmax);
			rangeSlider.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createRaisedBevelBorder(), titleValues,
					TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		} else {
			intmin = (int) Math.floor(ranges[0] * divisor);
			intmax = (int) Math.ceil(ranges[1] * divisor);
			rangeSlider.setValueAt(0, intmin);
			rangeSlider.setValueAt(1, intmax);
		}
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMajorTickSpacing() {
		return rangeSlider.getMajorTickSpacing();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param majorTick
	 */
	public void setMajorTickSpacing(int majorTick) {
		rangeSlider.setMajorTickSpacing(majorTick);
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMinorTickSpacing() {
		return rangeSlider.getMinorTickSpacing();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param minorTick
	 */
	public void setMinorTickSpacing(int minorTick) {
		rangeSlider.setMinorTickSpacing(minorTick);
		repaint();
	}

	public boolean getPaintLabel() {
		return paintLabel;
	}

	public void setPaintLabel(boolean paintLabel) {
		logger.finest("set Paint Label " + paintLabel);
		this.paintLabel = paintLabel;
		valueAt0 = rangeSlider.getValueAt(0);
		valueAt1 = rangeSlider.getValueAt(1);

		double precision = 1.0;
		divisor = 1.0 / precision;
		totalLength = (ranges[1] - ranges[0]);
		majorTicksDouble = totalLength / NUM_MAJOR_TICKS;
		minorTicksDouble = totalLength / NUM_MINOR_TICKS;
		intmin = (int) Math.floor(ranges[0] * divisor);
		intmax = (int) Math.ceil(ranges[1] * divisor);
		initialize(attributeName, DEFAULT_ORIENTATION,
				DEFAULT_NUMBER_OF_THUMBS, intmin, // 0
				intmax, // 598
				(int) (majorTicksDouble * divisor), // 120
				(int) (minorTicksDouble * divisor), // ~60
				divisor);
		rangeSlider.setValueAt(0, valueAt0);
		rangeSlider.setValueAt(1, valueAt1);
		titleValues = Double.toString(valueAt0) + "       " + title + "       "
				+ Double.toString(valueAt1);
		rangeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createRaisedBevelBorder(), titleValues, TitledBorder.CENTER,
				TitledBorder.ABOVE_TOP));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getNumberOfThumbs() {
		return rangeSlider.getNumberOfThumbs();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param numThumbs
	 */
	public void setNumberOfThumbs(int numThumbs) {
		rangeSlider.setNumberOfThumbs(numThumbs);
		repaint();
	}

	// Event handling
	public void stateChanged(ChangeEvent ce) {
		try {
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * adds an ChangeListener to the button
	 */
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	/**
	 * removes an ChangeListener from the button
	 */
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireChangeEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(e);
			}
		}
	}

	/* ------------------------- test method -------------------------- */
	/*
	 * public static void main (String[] args) { JFrame test = new
	 * JFrame("TEST OF ATTRIBUTE SLIDER"); test.setSize(800, 800); //RangeSlider
	 * testRangeSliderDefaults = new RangeSlider();
	 * //test.getContentPane().add(testRangeSliderDefaults,
	 * BorderLayout.CENTER); / RangeSlider testRangeSlider1 = new
	 * RangeSlider("tmax", MultiSlider.HORIZONTAL, // alignment 2, //
	 * numberOfThumbs 0, // min 255, // max 50, //major ticks 25, // minor ticks
	 * 1.0 // Divisor (for decimal number) );
	 */
	/*
	 * double[] minMax = { 0.000001, 10 }; RangeSlider testRangeSlider1 = new
	 * RangeSlider(); Object[] rangesTitle = new Object[2]; rangesTitle[0] =
	 * "Test"; rangesTitle[1] = minMax; //testRangeSlider1.setTitle("TEST");
	 * //testRangeSlider1.setUnits("° F");
	 * //testRangeSlider1.setMinMaxValue(minMax);
	 * testRangeSlider1.setRangeTitle(rangesTitle);
	 * testRangeSlider1.setToolTipText("Select temperature maximum");
	 * //testRangeSlider1.setTabTitle("test"); RangeSlider testRangeSlider2 =
	 * new RangeSlider(); test.getContentPane().add(testRangeSlider1,
	 * BorderLayout.NORTH); // test.getContentPane().add(testRangeSlider2,
	 * BorderLayout.CENTER); test.addWindowListener(new WindowAdapter() {
	 * 
	 * / put your documentation comment here
	 * 
	 * @param we
	 */
	/*
	 * public void windowClosing (WindowEvent we) { System.exit(0); } });
	 * test.pack(); test.setVisible(true); }
	 */

	/**
	 * put your documentation comment here
	 * 
	 * @param oos
	 * @exception IOException
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param ois
	 * @exception ClassNotFoundException
	 *                , IOException
	 */
	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

}
