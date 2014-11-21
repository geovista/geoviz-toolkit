/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.common.jts;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An empty shape with no geometry.
 */
public class EmptyShape implements Shape {
	public final static EmptyShape INSTANCE = new EmptyShape();
	private static EmptyIterator ei = new EmptyIterator();
	private static Rectangle er = new Rectangle(0, 0, 0, 0);

	/**
	 * Hides default constructor.
	 */
	private EmptyShape() {

	}

	/**
	 * Always returns false.
	 */
	public boolean contains(Point2D arg0) {

		return false;
	}

	/**
	 * Always returns false.
	 */
	public boolean contains(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Always returns false.
	 */
	public boolean contains(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Always returns false.
	 */
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns an empty rectangle.
	 */
	public Rectangle getBounds() {

		return er;
	}

	/**
	 * Returns null;
	 */
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns an empty AffineTransform.
	 */
	public PathIterator getPathIterator(AffineTransform arg0) {
		// TODO Auto-generated method stub
		return ei;
	}

	/**
	 * Returns an empty PathIterator.
	 */
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		// TODO Auto-generated method stub
		return ei;
	}

	/**
	 * Always returns false.
	 */
	public boolean intersects(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Always returns false.
	 */
	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}