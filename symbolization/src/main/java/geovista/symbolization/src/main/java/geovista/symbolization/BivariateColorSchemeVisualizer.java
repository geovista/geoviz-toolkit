/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;

import geovista.colorbrewer.ColorBrewer;
import geovista.colorbrewer.UnivariatePalette;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.symbolization.event.ColorClassifierEvent;
import geovista.symbolization.event.ColorClassifierListener;

// import javax.swing.colorchooser.*;

public class BivariateColorSchemeVisualizer extends JPanel implements
		IndicationListener, ColorClassifierListener, ActionListener {

	protected final static Logger logger = Logger
			.getLogger(BivariateColorSchemeVisualizer.class.getName());
	private ColorSymbolizer xSymbolizer;
	private ColorSymbolizer ySymbolizer;
	private TexturePanel[] swatches;
	private Dimension gap;
	private boolean usingDefaultX = true;
	private boolean usingDefaultY = true;

	private transient TexturePaint texPaint;

	private transient int oldIndication = 0;

	private static final String COMMAND_CLASSIFICATION_READY = "classer_ready";
	UnivariatePalette blues = ColorBrewer
			.getPalette(ColorBrewer.BrewerNames.Blues);
	UnivariatePalette greens = ColorBrewer
			.getPalette(ColorBrewer.BrewerNames.Greens);

	public BivariateColorSchemeVisualizer() {
		// defaults
		gap = new Dimension(0, 0);
		xSymbolizer = new ColorSymbolizerLinear(blues);
		ySymbolizer = new ColorSymbolizerLinear(greens);
		initSwatches();
		colorSwatches();
		makeTexPaint();
		setPreferredSize(new Dimension(40, 40));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	private void makeTexPaint() {

		int texSize = 4;
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, texSize, texSize);
		BufferedImage buff = new BufferedImage(texSize, texSize,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buff.createGraphics();
		Color trans = new Color(0, 0, 0, 0); // transarent black
		g2.setColor(trans);
		g2.fillRect(0, 0, texSize, texSize);
		g2.setColor(Color.blue);
		g2.drawLine(0, 0, 32, 32);

		texPaint = new TexturePaint(buff, rect);

	}

	private void initSwatches() {
		int numX = xSymbolizer.getNumClasses();
		int numY = ySymbolizer.getNumClasses();
		int numSwatches = numX * numY;
		if (swatches == null || swatches.length != numSwatches) {
			swatches = new TexturePanel[numSwatches];
		}
		removeAll();
		setLayout(new GridLayout(numY, numX, gap.width, gap.height));
		// this.setLayout(gridBag);
		// GridBagConstraints conts = new GridBagConstraints();
		// this.setLayout(new GridLayout(numY,numX));
		for (int i = 0; i < swatches.length; i++) {
			swatches[i] = new TexturePanel();
			// conts.
			// gridBag.setConstraints(swatches[i],conts);
			this.add(swatches[i]);
		}
		logger.finest("bicolorSchemeViz, w = " + getWidth());

	}

	private void colorSwatches() {
		int numX = xSymbolizer.getNumClasses();
		int numY = ySymbolizer.getNumClasses();
		GridLayout gl = (GridLayout) getLayout();

		if (gl.getColumns() != numX || gl.getRows() != numY) {
			initSwatches();
		}
		Color[] xColors = xSymbolizer.getColors(xSymbolizer.getNumClasses());
		Color[] yColors = ySymbolizer.getColors(ySymbolizer.getNumClasses());
		int counter = 0;
		for (int j = yColors.length - 1; j >= 0; j--) {
			for (int i = 0; i < xColors.length; i++) {

				Color mixedColor = ColorInterpolator
						.mixColorsRGBHighSaturation(xColors[i], yColors[j]);
				swatches[counter].setBackground(mixedColor);
				swatches[counter].setToolTipText("<html> " + "X class = "
						+ (i + 1) + "<br>" + "Y class = " + (j + 1) + "<br>"
						+ "<br>" + "counter = " + counter + "<br>" + "Red = "
						+ mixedColor.getRed() + "," + " Green = "
						+ mixedColor.getGreen() + "," + " Blue = "
						+ mixedColor.getBlue());

				counter++;

			}
		}
		fireActionPerformed(BivariateColorSchemeVisualizer.COMMAND_CLASSIFICATION_READY);
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		ColorClassifier colorSym = e.getColorSymbolClassification();

		logger.finest("spank me, the color classifier changed!");
		if (e.getOrientation() == ColorClassifierEvent.SOURCE_ORIENTATION_X) {
			setXSymbolizer(colorSym.getColorer());
		} else if (e.getOrientation() == ColorClassifierEvent.SOURCE_ORIENTATION_Y) {
			setYSymbolizer(colorSym.getColorer());
		}
	}

	public void setXSymbolizer(ColorSymbolizer xSymbolizer) {
		this.xSymbolizer = xSymbolizer;
		// XXX hacked in
		this.xSymbolizer = new ColorSymbolizerLinear(blues);

		usingDefaultX = false;
		if (!usingDefaultY) {
			initSwatches();
			colorSwatches();
			revalidate();
			this.repaint();
		}
	}

	public ColorSymbolizer getXSymbolizer() {
		return xSymbolizer;

	}

	public void setYSymbolizer(ColorSymbolizer ySymbolizer) {
		this.ySymbolizer = ySymbolizer;
		// XXX hacked in
		this.ySymbolizer = new ColorSymbolizerLinear(greens);
		usingDefaultY = false;
		if (!usingDefaultX) {
			initSwatches();
			colorSwatches();
			revalidate();
		}
	}

	public ColorSymbolizer getYSymbolizer() {
		return ySymbolizer;
	}

	public void setSwatches(TexturePanel[] swatches) {
		this.swatches = swatches;
	}

	public TexturePanel[] getSwatches() {
		return swatches;
	}

	public void setGap(Dimension gap) {
		this.gap = gap;
	}

	public Dimension getGap() {
		return gap;
	}

	public BivariateColorClassifier getBivariateColorClassification() {
		BivariateColorClassifierSimple biColorer = new BivariateColorClassifierSimple();
		biColorer.setColorerX(xSymbolizer);
		biColorer.setColorerY(ySymbolizer);
		// biColorer.setClasserX(this.cl);
		return biColorer;
	}

	public void indicationChanged(IndicationEvent e) {

		// clear old indication
		// if it exists
		if (oldIndication < swatches.length) {
			swatches[oldIndication].setPaint(null);
		}

		int xIndic = e.getXClass();
		int yIndic = e.getYClass();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("x = " + xIndic);
			logger.finest("y = " + yIndic);
		}
		if (xIndic < 0 || yIndic < 0) {
			this.repaint(); // clear old indication
			return;
		}
		if (xIndic > xSymbolizer.getNumClasses() - 1
				|| yIndic > ySymbolizer.getNumClasses() - 1) {
			this.repaint(); // clear old indication
			return;
		}

		int otherY = ySymbolizer.getNumClasses() - 1 - yIndic;

		int indicSwatch = otherY * xSymbolizer.getNumClasses();
		indicSwatch += xIndic;
		swatches[indicSwatch].setPaint(texPaint);
		logger.finest("bivarviz, indicated swatch = " + indicSwatch);
		oldIndication = indicSwatch;
		this.repaint();
	}

	/**
	 * adds an ActionListener
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
		}// next i

	}

	public void setBivariateColorSymbolClassification(
			BivariateColorClassifier biColorer) {
		xSymbolizer = biColorer.getXColorSymbolizer();
		ySymbolizer = biColorer.getYColorSymbolizer();
		colorSwatches();

	}

	private class TexturePanel extends JPanel {

		private transient TexturePaint p;

		public void setPaint(TexturePaint p) {
			this.p = p;
			// this.repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			Shape fillShape = new Rectangle2D.Float(0, 0, getWidth(),
					getHeight());
			g2.setColor(getBackground());
			g2.fill(fillShape);
			if (p != null) {
				g2.setPaint(p);
				g2.fill(fillShape);
			}

		}

	}
}
