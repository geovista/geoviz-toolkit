/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import geovista.colorbrewer.ColorBrewer;
import geovista.colorbrewer.UnivariatePalette;

public class ColorRampPicker extends JPanel implements ComponentListener {

	public ColorRampSwatch[] panSet;
	protected boolean[] anchored;
	protected Color[] colors;
	protected ColorRamp ramp;
	private int nSwatches;

	public static final String COMMAND_SWATCH_COLOR_CHANGED = "color_changed";
	public static final String COMMAND_SWATCH_TEXTURE_CHANGED = "texture_changed";

	public static final int DEFAULT_NUM_SWATCHES = 3;

	public static final Color DEFAULT_LOW_COLOR = new Color(200, 200, 200);
	// public static final Color DEFAULT_LOW_COLOR = new Color(0, 0, 0);
	// //light grey
	// public static final Color DEFAULT_LOW_COLOR = new Color(0, 150, 150);
	public static final Color DEFAULT_HIGH_COLOR_PURPLE = new Color(255, 0, 0);
	public static final Color DEFAULT_HIGH_COLOR_GREEN = new Color(0, 0, 255);

	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	private transient int currOrientation = 0;

	private UnivariatePalette pal;

	public ColorRampPicker() {
		init();
	}

	private void init() {
		pal = ColorBrewer
				.getPalette(ColorBrewer.BrewerNames.Blues);
		// this.setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		ramp = new ColorRamp();

		nSwatches = ColorRampPicker.DEFAULT_NUM_SWATCHES; // default
		colors = new Color[nSwatches];
		colors[0] = ColorRampPicker.DEFAULT_LOW_COLOR;
		colors[nSwatches - 1] = ColorRampPicker.DEFAULT_HIGH_COLOR_PURPLE;
		anchored = new boolean[nSwatches];
		for (int i = 0; i < anchored.length; i++) {
			anchored[i] = false;
		}
		makeRamp(nSwatches);
		rampSwatches();
		colors = pal.getColors(colors.length);
		// ramp.rampColors(colors, anchored);

		setPreferredSize(new Dimension(365, 20)); // these match 0.5 of the
		// ClassifierPicker
		// this.setMinimumSize(new Dimension(200,20));
		// this.setMaximumSize(new Dimension(1000,60));
		addComponentListener(this);
	}

	public void setPalette(ColorBrewer.BrewerNames name) {
		pal = ColorBrewer.getPalette(name);
		int len = 0;
		if (colors != null) {
			len = colors.length;
		} else {
			len = ColorRampPicker.DEFAULT_NUM_SWATCHES;
		}
		colors = pal.getColors(len);
	}

	public void makeRamp(int nSwatches) {
		// first find out if we already have some colors etc.
		// if so, track the num.
		int len = 0;
		if (colors != null) {
			len = colors.length;
			removeAll();
		}

		panSet = new ColorRampSwatch[nSwatches];
		for (int i = 0; i < panSet.length; i++) {
			if (i == 0) { // first one
				panSet[i] = new ColorRampSwatch(this, true, true);
				panSet[i].setSwatchColor(getLowColor());
			} else if (i == nSwatches - 1) { // last one
				panSet[i] = new ColorRampSwatch(this, true, true);
				panSet[i].setSwatchColor(getHighColor());
			} else {
				if (i < len - 1) {
					boolean anch = anchored[i];

					Color c = colors[i];
					panSet[i] = new ColorRampSwatch(this, anch, false);
					panSet[i].setSwatchColor(c);
				} else {
					panSet[i] = new ColorRampSwatch(this, false, false);
					panSet[i].setSwatchColor(Color.white);
				}
			}
			// panSet[i].setPreferredSize(new Dimension(40,40));
			this.add(panSet[i]);
			// XXX this next box spacer business needs to be broken out properly
			// XXX using min, max, pref sizes??? maybe borders are enough...
			/*
			 * int propWidth = this.getWidth()/100; logger.finest(propWidth); if
			 * (propWidth < 1) { propWidth = 1; } else if (propWidth > 5) {
			 * propWidth = 5; } Dimension smallBox = new
			 * Dimension(propWidth,propWidth); this.add(new
			 * Box.Filler(smallBox,smallBox,smallBox));
			 */
		}
		anchored = new boolean[nSwatches];
		colors = new Color[nSwatches];

		// swatSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
	}

	public void rampSwatches() {
		if (panSet == null) {
			return;
		}
		if (panSet.length <= 0) {
			return;
		}
		for (int j = 0; j < panSet.length; j++) {
			colors[j] = panSet[j].getSwatchColor();
			if (panSet[j].getAnchored()) {
				anchored[j] = true;
			} else {
				anchored[j] = false;
			}
		}
		colors = pal.getColors(colors.length);
		// ramp.rampColors(colors, anchored);
		for (int j = 0; j < panSet.length; j++) {
			panSet[j].setSwatchColor(colors[j]);
		}

	}

	public void swatchChanged() {
		rampSwatches();
		fireActionPerformed(ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
	}

	private void changeOrientation(int orientation) {
		if (orientation == currOrientation) {
			return;
		} else if (orientation == ColorRampPicker.X_AXIS) {
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

			currOrientation = ColorRampPicker.X_AXIS;
			revalidate();
		} else if (orientation == ColorRampPicker.Y_AXIS) {
			Component[] comps = new Component[getComponentCount()];
			for (int i = 0; i < getComponentCount(); i++) {
				comps[i] = getComponent(i);
			}
			// this.removeAll();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			for (int i = getComponentCount() - 1; i > -1; i--) {
				this.add(comps[i]);
			}
			currOrientation = ColorRampPicker.Y_AXIS;
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

		if (ratio >= 1 && currOrientation == ColorRampPicker.Y_AXIS) {
			changeOrientation(ColorRampPicker.X_AXIS);
		}
		if (ratio < 1 && currOrientation == ColorRampPicker.X_AXIS) {
			changeOrientation(ColorRampPicker.Y_AXIS);
		}
	}

	// end component event handling
	// start accessors

	public void setPanSet(ColorRampSwatch[] panSet) {
		this.panSet = panSet;
	}

	public ColorRampSwatch[] getPanSet() {
		return panSet;
	}

	public void setAnchored(boolean[] anchored) {
		this.anchored = anchored;
	}

	public boolean[] getAnchored() {
		return anchored;
	}

	public void setColors(Color[] colors) {
		nSwatches = colors.length;
		this.colors = colors;
		anchored = new boolean[colors.length];
		for (int i = 0; i < colors.length; i++) {
			anchored[i] = true;
		}
		setLowColor(colors[0]);
		setHighColor(colors[colors.length - 1]);
		this.colors = colors;
		rampSwatches();
		this.repaint();
	}

	public Color[] getColors() {
		return colors;
	}

	// added by Jamison Conley Dec. 11 2003
	public SimplePaletteImpl getPalette() {
		SimplePaletteImpl palette = new SimplePaletteImpl();
		palette.setLowColor(getLowColor());
		palette.setHighColor(getHighColor());
		palette.addAnchors(colors, anchored);
		palette.setName("ramp palette");
		return palette;
	}

	public void setRamp(ColorRamp ramp) {
		this.ramp = ramp;
	}

	public ColorRamp getRamp() {
		return ramp;
	}

	public void setLowColor(Color lowColor) {
		colors[0] = lowColor;
		panSet[0].setSwatchColor(lowColor);
		makeRamp(nSwatches);
		rampSwatches();
		this.repaint();
	}

	public Color getLowColor() {
		return colors[0];
	}

	public void setHighColor(Color highColor) {
		colors[colors.length - 1] = highColor;
		makeRamp(nSwatches);
		rampSwatches();
		this.repaint();
		fireActionPerformed(ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
	}

	public Color getHighColor() {
		return colors[colors.length - 1];
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

	/***************************************************************************
	 * Following methods are added by Diansheng, July 16th, 2003.
	 **************************************************************************/
	public Color[] getInterpolatedColorsOfAnyNumber(Color lowColor,
			Color highColor, int numberOfColors) {
		for (int i = 1; i < colors.length - 1; i++) {
			anchored[i] = false;
		}

		setHighColor(highColor);
		setLowColor(lowColor);
		setNSwatches(numberOfColors);

		return getColors();
	}

	public void setAnchoredColor(Color acolor, int pos) {
		if (pos < 0 || pos >= nSwatches) {
			return;
		}

		colors[pos] = acolor;
		anchored[pos] = true;
		panSet[pos].setAnchored(true);
		panSet[pos].setSwatchColor(acolor);

		swatchChanged();

		this.repaint();
	}

	public Color[] getRampColorsOfAnyNumber(int numberOfColors) {
		for (int i = 1; i < colors.length - 1; i++) {
			anchored[i] = false;
		}
		setHighColor(Color.green);
		setLowColor(Color.blue);
		setNSwatches(numberOfColors);
		setAnchoredColor(Color.red, numberOfColors / 2);

		return getColors();
	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ColorRampPicker picker = new ColorRampPicker();
		app.add(picker);
		app.setVisible(true);

		boolean[] bools = new boolean[10];
		System.out.println(Arrays.toString(bools));

	}
}
