/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization.glyph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.logging.Level;
import java.util.logging.Logger;

import geovista.symbolization.AffineTransformModifier;

/**
 * Paint a glyph;
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class GlyphRenderer implements Glyph {

	boolean figureReady;

	GeneralPath originalFigure;

	Shape paintFigure;

	Rectangle targetArea;
	AffineTransform zoomForm;

	Color fillColor;
	Color outlineColor;

	public static final Color defaultFillColor = Color.white;
	public static final Color defaultIndicationColor = Color.green;

	private static Stroke outlineStroke = new BasicStroke(0.8f);

	public static final Color defaultOutlineColor = Color.black;

	private final boolean fill = true;

	final static Logger logger = Logger
			.getLogger(GlyphRenderer.class.getName());

	public GlyphRenderer() {
		fillColor = GlyphRenderer.defaultFillColor;
		outlineColor = GlyphRenderer.defaultOutlineColor;

		figureReady = false;

	}

	public GlyphRenderer copy() {
		GlyphRenderer newCopy = new GlyphRenderer();
		newCopy.setFillColor(getFillColor());

		return newCopy;
	}

	public void setLengths(int[] spikeLengths) {
		if (spikeLengths == null) {
			return;
		}

		fillPaths();
	}

	private void fillPaths() {
		originalFigure = new GeneralPath();

		figureReady = true;
	}

	@SuppressWarnings("unused")
	private void findPoints(int[] lengths) {
		// need some trigonometry here
		double nVals = lengths.length;
		double max = Math.PI * 2;
		double angle = 0;
		double fraction = 0;
		float x = 0;
		float y = 0;
		double iDouble = 0;

		for (int i = 0; i < lengths.length; i++) {
			iDouble = i;
			fraction = iDouble / nVals;
			angle = fraction * max;
			x = (float) Math.sin(angle) * lengths[i];
			y = (float) Math.cos(angle) * lengths[i];
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("Setting location, x = " + x + ", y = " + y);
			}

		}

	}

	private void projectFigure(Rectangle targetArea) {
		if (paintFigure == null) {
			figureReady = false;
			return;
		}
		Rectangle paintArea = new Rectangle();
		paintArea.setBounds(-100, -100, 200, 200);
		// why double the x and y on the positive?
		// because the width and height need to be the negative value + the same
		// positive to be centered on zero
		zoomForm = AffineTransformModifier.makeGeogAffineTransform(paintArea,
				targetArea, true, true);

		paintFigure = originalFigure.createTransformedShape(zoomForm);

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("in StarPlotRenderer.projectFigure");
		}
		figureReady = true;
	}

	/**
	 * 
	 */

	public void paintStar(Graphics2D target) {
		if (!figureReady || paintFigure == null) {
			return;
		}
		Stroke st = target.getStroke();

		target.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (fill) {
			target.setColor(fillColor);
			target.fill(paintFigure);

			target.setColor(outlineColor);
			target.setStroke(GlyphRenderer.outlineStroke);
			target.draw(paintFigure);
		}
		target.setStroke(st);

	}

	public void draw(Graphics2D g2) { // we use this when we are acting as a
		// glyph

		paintStar(g2);

	}

	public static void main(String[] args) {
		GlyphRenderer sd = new GlyphRenderer();
		int[] lengths = { 1, 34, 100, 0, 87, 22 };

		sd.setLengths(lengths);
		/*
		 * //some trig double sineAns = 0; double cosAns= 0; double radians=0;
		 * radians = 0; sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/4; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/2; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI + Math.PI /
		 * 2; sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ ");
		 */

	}

	/**
	 * Getter for property fillColor.
	 * 
	 * @return Value of property fillColor.
	 * 
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Setter for property fillColor.
	 * 
	 * @param fillColor
	 *            New value of property fillColor.
	 * 
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Rectangle getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(Rectangle targetArea) {
		this.targetArea = targetArea;
		projectFigure(this.targetArea);
	}

	public void setLocation(Point location) { // when we are a Glyph

		int width = 50; // xxx totally abitrary
		int height = 50;
		int x = location.x - (width / 2);
		int y = location.y - (height / 2);
		Rectangle targetArea = new Rectangle(x, y, width, height);
		setTargetArea(targetArea);
	}

	public Shape getOutline() {
		return paintFigure;
	}

}
