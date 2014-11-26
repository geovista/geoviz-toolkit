/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.projection.affine;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Defines what ShapeTransformers must do.
 */
public interface ShapeTransformer {

	public Shape[] makeTransformedShapes(Shape[] shapes, AffineTransform xForm);

	public Point2D[] makeTransformedPoints(Point2D[] points,
			AffineTransform xForm);

	public void setXForm(AffineTransform xForm);

	public AffineTransform getXForm();

}
