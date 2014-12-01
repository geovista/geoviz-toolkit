/*------------------------------------------------------------------------------
 * GeoVISTA Center, Penn State Geography Deptartment*
 * Copyright (c), 2002-5, GeoVISTA Center and Frank Hardisty
 *
 * Original Authors: Bonan Li, Frank Hardisty
 * $Author: hardisty $
 *
 * $Date: 2005/11/04 19:19:26 $
 *
 * $Id: Glyph.java,v 1.2 2005/11/04 19:19:26 hardisty Exp $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 A symbol is a shape with additional painting information. It allows a calling
 class to call symbol.draw(), and expect that the background color etc. will
 be set appropriately.
 ------------------------------------------------------------------------------*/
package geovista.symbolization.glyph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import geovista.symbolization.AffineTransformModifier;

public class NGon implements Glyph {
    public static final Color DEFAULT_INDICATION_COLOR = Color.green;
    Color fillColor;
    Color strokeColor;
    Shape path;
    Shape drawPath;
    AffineTransform zoomForm;
    int nSides;
    static int defaultNSides = 3;
    int size;
    Point location;
    static int defaultSize = 200;
    final static Logger logger = Logger.getLogger(NGon.class.getName());

    public NGon(int nSides) {
	this.nSides = nSides;
	path = NGon.findNGon(nSides);
	fillColor = Color.blue;
	strokeColor = Color.black;
	zoomForm = new AffineTransform();
	size = defaultSize;
    }

    public void draw(Graphics2D g2) {

	g2.setColor(strokeColor);
	g2.draw(drawPath);
	g2.setColor(fillColor);
	g2.fill(drawPath);
    }

    public Color getFillColor() {
	return fillColor;
    }

    public void setFillColor(Color fillColor) {
	this.fillColor = fillColor;

    }

    /* center on the location */
    public void setLocation(Point location) {
	if (location == null) {
	    return;
	}
	this.location = location;
	// Rectangle bounds = path.getBounds();
	// int width = bounds.width * 30;
	// int height = bounds.height * 30;
	int x = location.x - (size / 2);
	int y = location.y - (size / 2);

	Rectangle targetArea = new Rectangle(x, y, size, size);
	setTargetArea(targetArea);

    }

    public void setSize(int pixels) {
	size = pixels;
	setLocation(location);
    }

    public void setNSides(int nSides) {
	this.nSides = nSides;
	path = NGon.findNGon(nSides);
	setLocation(location);// zooms as well

    }

    public void setTargetArea(Rectangle targetArea) {
	int x = targetArea.x - targetArea.width / 2;
	int y = targetArea.y - targetArea.height / 2;
	location = new Point(x, y);
	Rectangle paintArea = path.getBounds();

	zoomForm = AffineTransformModifier.makeGeogAffineTransform(paintArea,
		targetArea, true, true);
	drawPath = zoomForm.createTransformedShape(path);

    }

    public static GeneralPath findNGon(int nSides) {
	double x = 0;
	double y = 0.5;
	double heading = 90;// an angle, in degrees

	// we start from the mid-point of x, because that is sure to
	// be one of the closest edge points to the center

	double angle = 360.0 / nSides;

	double step = Math.sin(Math.toRadians(angle / 2.0)); // sin(pi/N)
	heading = angle / 2.0;
	GeneralPath p = new GeneralPath();
	p.moveTo((float) x, (float) y);
	// logger.info("x = " + x + ",y = " + y + " heading = " +
	// heading);
	// logger.info("nSides = " + nSides);
	for (int i = 0; i < nSides; i++) {

	    x += step * Math.cos(Math.toRadians(heading));

	    y += step * Math.sin(Math.toRadians(heading));
	    p.lineTo((float) x, (float) y);

	    heading += angle;
	    // logger.info("x = " + x + ",y = " + y + " heading = "+
	    // heading);
	}

	return p;
    }

    public static void main(String[] args) {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	NGon gon = new NGon(3);
	NGonPanel nGonPanel = gon.new NGonPanel();
	frame.add(nGonPanel);

	frame.pack();
	frame.setVisible(true);
    }

    public class NGonPanel extends JPanel implements ChangeListener {
	int n = 40;
	float size = 200;
	GeneralPath path;
	JSlider nGonSlider;
	JSlider sizeSlider;

	public NGonPanel() {
	    setLayout(new BorderLayout());
	    nGonSlider = new JSlider();
	    nGonSlider.addChangeListener(this);
	    this.add(nGonSlider, BorderLayout.SOUTH);

	    sizeSlider = new JSlider();
	    sizeSlider.addChangeListener(this);
	    sizeSlider.setOrientation(SwingConstants.VERTICAL);
	    this.add(sizeSlider, BorderLayout.WEST);
	    setMinimumSize(new Dimension(200, 200));
	    setPreferredSize(new Dimension(200, 200));
	    path = NGon.findNGon(n);

	}

	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    AffineTransform xForm = AffineTransform.getScaleInstance(200, 200);
	    xForm = new AffineTransform(size, 0, 0, size, size, 0);
	    g2.setTransform(xForm);
	    g2.setColor(Color.black);
	    g2.fill(path);

	}

	public void stateChanged(ChangeEvent e) {
	    if (e.getSource().equals(nGonSlider)) {
		JSlider slider = (JSlider) e.getSource();
		path = NGon.findNGon(slider.getValue());
		this.repaint();
	    }
	    if (e.getSource().equals(sizeSlider)) {
		JSlider slider = (JSlider) e.getSource();
		size = 5 * slider.getValue();
		logger.info("size = " + size);
		this.repaint();
	    }
	}

    }

    public NGon copy() {
	NGon copy = new NGon(nSides);
	copy.path = path;
	copy.drawPath = drawPath;
	copy.nSides = nSides;
	copy.fillColor = fillColor;
	copy.zoomForm = zoomForm;
	return copy;
    }

    public Shape getDrawPath() {
	return drawPath;
    }

    public void setDrawPath(Shape drawPath) {
	this.drawPath = drawPath;
    }

}
