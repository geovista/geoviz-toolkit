/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import geovista.colorbrewer.ColorBrewer;
import geovista.colorbrewer.UnivariatePalette;
import geovista.geoviz.visclass.VisualClassifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.EventListenerList;

public class ColorBrewerPicker extends JPanel implements ComponentListener,
	ActionListener {

    protected Color[] colors;
    protected ColorRamp ramp;
    private int nSwatches;

    public static final String COMMAND_SWATCH_COLOR_CHANGED = "color_changed";
    public static final String COMMAND_SWATCH_TEXTURE_CHANGED = "texture_changed";

    public static final int DEFAULT_NUM_SWATCHES = 3;

    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    private transient int currOrientation = 0;

    private UnivariatePalette pal;
    JComboBox colorList;
    ComboBoxRenderer renderer;
    final static Logger logger = Logger.getLogger(ColorBrewerPicker.class
	    .getName());

    public ColorBrewerPicker() {
	init();
    }

    private void init() {
	if (VisualClassifier.debugStatus
		.equals(VisualClassifier.LayoutDebugStatus.Debug)) {
	    setBorder(BorderFactory.createTitledBorder(this.getClass()
		    .getSimpleName()));

	}
	nSwatches = DEFAULT_NUM_SWATCHES;
	pal = ColorBrewer.getPalette(ColorBrewer.BrewerNames.Blues);
	Object[] names = ColorBrewer.readContents().keySet().toArray();
	colorList = new JComboBox(names);
	renderer = new ComboBoxRenderer(
		ColorBrewer.getPalette(ColorBrewer.BrewerNames.Blues));
	renderer.nColors = ColorBrewerPicker.DEFAULT_NUM_SWATCHES;
	renderer.setPreferredSize(new Dimension(100, 10));
	colorList.setRenderer(renderer);

	// this.setBorder(BorderFactory.createLineBorder(Color.black));
	// setLayout(new BorderLayout());
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	this.add(colorList);
	colorList.addActionListener(this);
	// setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

	// setPreferredSize(new Dimension(365, 20)); // these match 0.5 of the
	// ClassifierPicker

	setPreferredSize(new Dimension(200, 20));
	setMinimumSize(new Dimension(200, 20));
	setMaximumSize(new Dimension(1000, 40));
	addComponentListener(this);
    }

    public void setPalette(ColorBrewer.BrewerNames name) {
	pal = ColorBrewer.getPalette(name);
	int len = 0;
	if (colors != null) {
	    len = colors.length;
	} else {
	    len = ColorBrewerPicker.DEFAULT_NUM_SWATCHES;
	}
	colors = pal.getColors(len);
    }

    public void makeRamp(int nSwatches) {
	// first find out if we already have some colors etc.
	// if so, track the num.
	int len = 0;
	if (colors != null) {
	    len = colors.length;
	    this.renderer.nColors = len;
	}

	// XXX this next box spacer business needs to be broken out properly
	// XXX using min, max, pref sizes??? maybe borders are enough...
	/*
	 * int propWidth = this.getWidth()/100; logger.finest(propWidth); if
	 * (propWidth < 1) { propWidth = 1; } else if (propWidth > 5) {
	 * propWidth = 5; } Dimension smallBox = new
	 * Dimension(propWidth,propWidth); this.add(new
	 * Box.Filler(smallBox,smallBox,smallBox));
	 */

	rampSwatches();
	// swatSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
    }

    public void rampSwatches() {
	colors = pal.getColors(nSwatches);

    }

    public void swatchChanged() {
	rampSwatches();
	fireActionPerformed(ColorBrewerPicker.COMMAND_SWATCH_COLOR_CHANGED);
    }

    private void changeOrientation(int orientation) {
	if (orientation == currOrientation) {
	    return;
	} else if (orientation == ColorBrewerPicker.X_AXIS) {
	    Component[] comps = new Component[getComponentCount()];
	    int counter = 0;
	    for (int i = getComponentCount() - 1; i > -1; i--) {
		comps[counter] = getComponent(i);
		counter++;
	    }
	    // this.removeAll();
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	    for (Component element : comps) {
		this.add(element);
	    }

	    currOrientation = ColorBrewerPicker.X_AXIS;
	    revalidate();
	} else if (orientation == ColorBrewerPicker.Y_AXIS) {
	    Component[] comps = new Component[getComponentCount()];
	    for (int i = 0; i < getComponentCount(); i++) {
		comps[i] = getComponent(i);
	    }
	    // this.removeAll();
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    for (int i = getComponentCount() - 1; i > -1; i--) {
		this.add(comps[i]);
	    }
	    currOrientation = ColorBrewerPicker.Y_AXIS;
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

	if (ratio >= 1 && currOrientation == ColorBrewerPicker.Y_AXIS) {
	    changeOrientation(ColorBrewerPicker.X_AXIS);
	}
	if (ratio < 1 && currOrientation == ColorBrewerPicker.X_AXIS) {
	    changeOrientation(ColorBrewerPicker.Y_AXIS);
	}
    }

    // end component event handling
    // start accessors

    public void setColors(Color[] colors) {
	nSwatches = colors.length;
	this.colors = colors;

	this.colors = colors;
	rampSwatches();
	this.repaint();
    }

    public Color[] getColors() {
	if (colors == null) {
	    colors = pal.getColors(nSwatches);
	}
	return colors;
    }

    public UnivariatePalette getPalette() {
	return pal;
    }

    public void setRamp(ColorRamp ramp) {
	this.ramp = ramp;
    }

    public ColorRamp getRamp() {
	return ramp;
    }

    public void setNSwatches(int nSwatches) {
	this.nSwatches = nSwatches;
	makeRamp(nSwatches);
	rampSwatches();
	validate();
	this.repaint();
    }

    public int getNSwatches() {
	return nSwatches;
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
	}
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
	UnivariatePalette pal;

	int nColors;

	public ComboBoxRenderer(UnivariatePalette pal) {
	    setOpaque(true);
	    setHorizontalAlignment(CENTER);
	    setVerticalAlignment(CENTER);
	    this.pal = pal;
	}

	/*
	 * This method finds the image and text corresponding to the selected
	 * value and returns the label, set up to display the text and image.
	 */
	public Component getListCellRendererComponent(JList list, Object value,
		int index, boolean isSelected, boolean cellHasFocus) {

	    int selectedIndex = index;
	    pal = ColorBrewer.getPalette((String) value);
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	    return this;
	}

	@Override
	public void paintComponent(Graphics g) {

	    // int nColors = 5;
	    Color[] colors = pal.getColors(nColors);
	    // only horizontal painting enabled for now...
	    int patchWidth = getWidth() / nColors;
	    for (int i = 0; i < nColors; i++) {
		g.setColor(colors[i]);
		g.fillRect(i * patchWidth, 0, patchWidth + 1, getHeight());
	    }
	    g.setColor(Color.black);
	    g.drawRect(0, 0, getWidth(), getHeight());

	}

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == colorList) {

	    Object paletteName = colorList.getSelectedItem();
	    pal = ColorBrewer.getPalette((String) paletteName);

	    logger.info("***");
	    logger.info("pal " + pal.getName());

	    rampSwatches();
	    fireActionPerformed(ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED);

	}

    }

    public static void main(String[] args) {
	JFrame app = new JFrame();
	app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ColorBrewerPicker picker = new ColorBrewerPicker();
	app.add(picker);
	app.setVisible(true);

	boolean[] bools = new boolean[10];
	System.out.println(Arrays.toString(bools));

    }

}
