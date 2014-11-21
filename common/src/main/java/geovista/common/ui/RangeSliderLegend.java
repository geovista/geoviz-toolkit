/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Isaac Brewer */

package geovista.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* *******************************************************************
 Implementation of class RangeSliderLegend
 ******************************************************************* */
/**
 * Generally describe RangeSliderLegend in here.
 * 
 * 
 * @author IsaacBrewer (isaacbrewer@hotmail.com)
 */
public class RangeSliderLegend extends JPanel implements ChangeListener {
	private transient JTabbedPane tabbedPane;

	protected final static Logger logger = Logger
			.getLogger(RangeSliderLegend.class.getName());
	private transient Vector sliderTabs;
	private transient int currentIndex = 0;
	protected RangeSlider slider;
	protected Color sliderColor;
	protected Color labelPanelColor;
	protected Color fontColor;
	protected int numberOfSliders = 0;
	protected EventListenerList listenerList = new EventListenerList();

	public Object data;

	private static final String DEFAULT_TITLE = "No Title";
	private String tabTitle = DEFAULT_TITLE;

	// =========================== CONSTRUCTORS ============================= //
	public RangeSliderLegend() {
		super();
		slider = createDefaultSlider();
		RangeSlider[] defaultSliderPanes = new RangeSlider[1];
		defaultSliderPanes[0] = slider;
		initialize();
		addTabs(defaultSliderPanes);

	}

	public RangeSliderLegend(String title) {
		super();
		initialize();
	}

	public RangeSliderLegend(RangeSlider[] sliderPanes, String title) {
		super();
		initialize();
		addTabs(sliderPanes);
	}

	// ============================= METHODS ===================================
	// //
	protected void initializeDefaults() {

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.addChangeListener(this);
		this.add(tabbedPane);
		setBackground(Color.black);
	}

	/**
	 * Creates a rangeslider using all of the defaults
	 * 
	 * @return
	 */
	protected RangeSlider createDefaultSlider() {
		RangeSlider slider = new RangeSlider();
		numberOfSliders = 1;
		return slider;

	}

	/**
	 * Initializes the rangeSliders
	 */
	protected void initialize() {
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.addChangeListener(this);
		this.add(tabbedPane);
		setBackground(Color.black);
		sliderTabs = new Vector();
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
	 * @param o
	 */
	public void setDataObject(Object o) {
		data = o;
	}

	/**
	 * creates tabs on the RangeSliderLegend and adds the sliders to them.
	 * 
	 * @param sliderPanes
	 */
	protected void addTabs(RangeSlider[] sliderPanes) {
		int numberOfSliderPanes = sliderPanes.length;
		logger.finest("numberOfSliderPanes = " + numberOfSliderPanes);
		for (int i = 0; i < numberOfSliderPanes; i++) {
			logger.finest("RangeSliderLegend.ctor(), sliderPanes[" + i + "] = "
					+ sliderPanes[i]);
			logger.finest("tabbedPane = " + tabbedPane);
			// tabbedPane.addTab(sliderPanes[i].getTabTitle(), sliderPanes[i]);
			tabbedPane.addTab(getTabTitle(), sliderPanes[i]);
			sliderTabs.add(sliderPanes[i]);
		}
	}

	/**
	 * Set how many sliders are created.
	 * 
	 * @param numberOfSliders
	 */
	public void setCreateSlider(int numberOfSliders) {

		int count = numberOfSliders;
		for (int i = 0; i < count; i++) {

			RangeSlider slider = new RangeSlider();
			// String attributeName = getTabTitle();
			String attributeName = tabTitle;
			tabbedPane.addTab(attributeName, slider);
			sliderTabs.add(slider);

		}

	}

	public int getCreateSlider() {

		return numberOfSliders;

	}

	public int getMaximum() {

		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getMaximum();
		// return rangeSlider.getMaximum();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param max
	 */
	public void setMaximum(int max) {
		logger.finest("MAX = " + max);
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setMaximum(max);

	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMinimum() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getMinimum();

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param min
	 */
	public void setMinimum(int min) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setMinimum(min);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param min
	 */
	public int getMajorTickSpacing() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getMajorTickSpacing();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param majorTick
	 */
	public void setMajorTickSpacing(int majorTick) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setMajorTickSpacing(majorTick);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int getMinorTickSpacing() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getMinorTickSpacing();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param minorTick
	 */
	public void setMinorTickSpacing(int minorTick) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setMinorTickSpacing(minorTick);

	}

	public String getUnits() {

		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getUnits();
	}

	/**
	 * Method to set title of the rangeslider
	 * 
	 * @param s
	 */
	public void setUnits(String u) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setUnits(u);

	}

	public String getTabTitle() {

		return tabTitle;

	}

	public void setTabTitle(String tt) {

		tabTitle = tt;
		tabbedPane.setTitleAt(currentIndex, tabTitle);
		repaint();
	}

	public String getTitle() {

		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getTitle();
	}

	/**
	 * Method to set title of the rangeslider
	 * 
	 * @param s
	 */
	public void setTitle(String s) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setTitle(s);

	}

	/**
	 * Returns the color of the panel containing the labels of the thumb
	 * locations
	 * 
	 * @return
	 */
	public Color getLabelPanelColor() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getLabelPanelColor();
		// return sliderPanes[currentIndex].getLabelPanelColor();
	}

	/**
	 * sets the color of the panel containing the labels of the thumb locations
	 * 
	 * @param lpc
	 */
	public void setLabelPanelColor(Color lpc) {

		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setLabelPanelColor(lpc);
		// sliderPanes[currentIndex].setLabelPanelColor(lpc);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Color getSliderColor() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getSliderColor();
		// return sliderPanes[currentIndex].getSliderColor();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sc
	 */
	public void setSliderColor(Color sc) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setSliderColor(sc);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Color getFontColor() {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		return rslider.getFontColor();
		// return sliderPanes[currentIndex].getFontColor();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param fc
	 */
	public void setFontColor(Color fc) {
		RangeSlider rslider = (RangeSlider) sliderTabs.get(currentIndex);
		rslider.setFontColor(fc);
		// sliderPanes[currentIndex].setSliderColor(fc);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param attributeName
	 * @param min
	 * @param max
	 * @param precision
	 * @return
	 */
	public RangeSlider addRangeSlider(String attributeName, double min,
			double max, double precision) {
		RangeSlider slider = new RangeSlider(attributeName, min, max, precision);
		tabbedPane.addTab(attributeName, slider);
		sliderTabs.add(slider);
		return slider;
	}

	/*
	 * public void addAttSlider (String attname, double minvalue, double
	 * maxvalue, double precision) Once I have the metadata, I'll call this
	 * method several times (depends on how many attributes I have). Inside this
	 * method, you just create a new slider, add it to the tab pane, show the
	 * attname as the tab title, customize the range of the slider with minvalue
	 * and maxvalue. At this point you can just ignore the precision (I'll tell
	 * you how to use it when we meet)
	 */
	public double[] getAttributeRange() {
		double[] attributeRange = slider.getValueDoubles();
		return attributeRange;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param ce
	 */
	public void stateChanged(ChangeEvent ce) {
		try {
			// (MultiSlider)ce.getSource()
			Object src = ce.getSource();
			logger.finest("Src = " + src);
			if (src instanceof JTabbedPane) {
				currentIndex = tabbedPane.getSelectedIndex();
				logger.finest("Current index = " + currentIndex);
			}
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
	public static void main(String[] args) {
		JFrame test = new JFrame("ATTRIBUTE SLIDER");
		test.setSize(800, 800);
		RangeSliderLegend testRangeSliderLegend = new RangeSliderLegend();
		/*
		 * RangeSlider slider1 = testRangeSliderLegend.addRangeSlider("TMAX", //
		 * attributeName 0.0, // min 100.0, // max 1.0); // precision
		 * slider1.setTitle("Maximum Temperature Range");
		 * slider1.setUnits("° F");
		 * slider1.setToolTipText("Select temperature maximum");
		 * slider1.setTabTitle("Temp MAX"); slider1.setMajorTickSpacing(5);
		 * slider1.setMinorTickSpacing(1);
		 */
		/*
		 * RangeSlider slider2 = testRangeSliderLegend.addRangeSlider("TMIN", //
		 * name -23.0, // min 77.0, // max 1.0); // precision
		 * slider2.setTitle("Minimum Temperature Range");
		 * slider2.setUnits("° F");
		 * slider2.setToolTipText("Set temperature minimum");
		 * slider2.setTabTitle("Temp MIN"); slider2.setMajorTickSpacing(5);
		 * slider2.setMinorTickSpacing(1); RangeSlider slider3 =
		 * testRangeSliderLegend.addRangeSlider("PRECIP", // attributeName 0.0,
		 * // min 5.98, // max 0.01); // precision
		 * slider3.setTitle("Precipitation Range"); slider3.setUnits("inches");
		 * slider3.setToolTipText("Set Precipitation Min and Max");
		 * slider3.setTabTitle("PRECIP"); slider3.setMajorTickSpacing(50);
		 * slider3.setMinorTickSpacing(25);
		 */
		test.getContentPane().add(testRangeSliderLegend, BorderLayout.CENTER);
		test.addWindowListener(new WindowAdapter() {

			/**
			 * Method for exiting the program correctly
			 * 
			 * @param we
			 */
			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		test.pack();
		test.setVisible(true);
	}
}
