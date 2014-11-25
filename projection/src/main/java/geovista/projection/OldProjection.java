/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.projection;

import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Defines what projections must do.
 */
public interface OldProjection {

	/*
	 * this is in radians unless the projection specifies otherwise.
	 */
	public Point2D.Double project(double lat, double longVal, Point2D.Double pt);

	public Shape project(Shape input);

}