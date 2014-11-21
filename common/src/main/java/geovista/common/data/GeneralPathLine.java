/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class wraps a java.awt.geom.GeneralPath. It is intended that the only
 * difference between this class and a GeneralPath is semantic.
 * 
 * This class is being introduced because there are many classes which pass
 * around a DataSetForApps in its Object[] form. Thus, the spatial type
 * information needs to be encoded in the type of the class.
 * 
 * 
 * 
 * @author Frank Hardisty
 * @see java.awt.geom.GeneralPath
 */
public class GeneralPathLine implements Shape {
	GeneralPath gp = null;

	public static final int WIND_EVEN_ODD = GeneralPath.WIND_EVEN_ODD;
	public static final int WIND_NON_ZERO = GeneralPath.WIND_NON_ZERO;

	public GeneralPathLine() {
		gp = new GeneralPath();
	}

	public GeneralPathLine(int rule) {
		gp = new GeneralPath(rule);
	}

	public GeneralPathLine(int rule, int initialCapacity) {
		gp = new GeneralPath(rule, initialCapacity);
	}

	public GeneralPathLine(Shape s) {
		gp = new GeneralPath(s);
	}

	public synchronized void moveTo(float x, float y) {
		gp.moveTo(x, y);
	}

	public synchronized void lineTo(float x, float y) {
		gp.lineTo(x, y);
	}

	public synchronized void quadTo(float x1, float y1, float x2, float y2) {
		gp.quadTo(x1, y1, x2, y2);
	}

	public synchronized void curveTo(float x1, float y1, float x2, float y2,
			float x3, float y3) {
		gp.curveTo(x1, y1, x2, y2, x3, y3);
	}

	public synchronized void closePath() {
		gp.closePath();
	}

	public void append(Shape s, boolean connect) {
		gp.append(s, connect);
	}

	public void append(PathIterator pi, boolean connect) {
		gp.append(pi, connect);
	}

	public synchronized int getWindingRule() {
		return gp.getWindingRule();
	}

	public void setWindingRule(int rule) {
		gp.setWindingRule(rule);
	}

	public synchronized Point2D getCurrentPoint() {
		return gp.getCurrentPoint();
	}

	public synchronized void reset() {
		gp.reset();
	}

	public void transform(AffineTransform at) {
		gp.transform(at);
	}

	public synchronized Shape createTransformedShape(AffineTransform at) {
		return gp.createTransformedShape(at);
	}

	public java.awt.Rectangle getBounds() {
		return gp.getBounds();
	}

	public synchronized Rectangle2D getBounds2D() {
		return gp.getBounds2D();
	}

	public boolean contains(double x, double y) {
		return gp.contains(x, y);
	}

	public boolean contains(Point2D p) {
		return gp.contains(p);
	}

	public boolean contains(double x, double y, double w, double h) {
		return gp.contains(x, y, w, h);
	}

	public boolean contains(Rectangle2D r) {
		return gp.contains(r);
	}

	public boolean intersects(double x, double y, double w, double h) {
		return gp.intersects(x, y, w, h);
	}

	public boolean intersects(Rectangle2D r) {

		return gp.intersects(r);
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return gp.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return gp.getPathIterator(at, flatness);
	}

	@Override
	public Object clone() {
		return new GeneralPathLine(gp);
	}

}
