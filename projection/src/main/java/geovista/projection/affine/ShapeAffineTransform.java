/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.projection.affine;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.event.EventListenerList;

/**
 * put your documentation comment here
 */
public class ShapeAffineTransform implements ShapeTransformer {

	private Object[] inputDataSet;
	private Object[] outputDataSet;
	private Shape[] oldShapes;
	private Shape[] newShapes;
	private AffineTransform xForm;
	private EventListenerList listenerList;

	public ShapeAffineTransform() {
		super();
		xForm = new AffineTransform();
		listenerList = new EventListenerList();
	}

	public Shape[] makeTransformedShapes(Shape[] shapes, AffineTransform xForm) {
		Shape[] newShapes = new Shape[shapes.length];
		for (int i = 0; i < shapes.length; i++) {
			// GeneralPath gp = (GeneralPath)shapes[i];
			newShapes[i] = xForm.createTransformedShape(shapes[i]);
			// xForm.c
		}
		return newShapes;
	}

	public Point2D[] makeTransformedPoints(Point2D[] points,
			AffineTransform xForm) {
		Point2D[] newPoints = new Point2D[points.length];
		for (int i = 0; i < points.length; i++) {

			newPoints[i] = xForm.transform(points[i], null);

		}
		return newPoints;
	}

	// this.oldShapes = shapes;
	// this.newShapes = this.transform(oldShapes);
	// return this.transform(shapes);
	// }
	public Shape[] transform(Shape[] oldShapes) {
		Shape[] newShapes = new Shape[oldShapes.length];
		// we have a problem here... the shapes created with this method
		// are not independent of each other.
		for (int i = 0; i < oldShapes.length; i++) {

			newShapes[i] = xForm.createTransformedShape(oldShapes[i]);

		}
		return newShapes;
	}

	public void setInputDataSet(Object[] inputDataSet) {
		this.inputDataSet = inputDataSet;
	}

	public Object[] getInputDataSet() {
		return inputDataSet;
	}

	public void setOutputDataSet(Object[] outputDataSet) {
		this.outputDataSet = outputDataSet;
	}

	public Object[] getOutputDataSet() {
		return outputDataSet;
	}

	public void setOldShapes(Shape[] oldShapes) {
		this.oldShapes = oldShapes;

	}

	public Shape[] getOldShapes() {
		return oldShapes;
	}

	public void setNewShapes(Shape[] newShapes) {
		this.newShapes = newShapes;
	}

	public Shape[] getNewShapes() {
		return newShapes;
	}

	public void setXForm(AffineTransform xForm) {
		this.xForm = xForm;
	}

	public AffineTransform getXForm() {
		return xForm;
	}

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	public EventListenerList getListenerList() {
		return listenerList;
	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
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
	protected void fireActionPerformed(String command) {
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

}
