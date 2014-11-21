/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.awt.geom.Rectangle2D;
import java.util.EventObject;

/**
 * An SpatialExtentEvent signals that a set of observations has been singled
 * out. This is often because the user has somehow indicated that these
 * observations are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class SpatialExtentEvent extends EventObject {

	private Rectangle2D spatialExtent;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * spatialExtent values are indicated.
	 */

	public SpatialExtentEvent(Object source, Rectangle2D spatialExtent) {
		super(source);
		this.spatialExtent = spatialExtent;
	}

	// begin accessors
	public void setSpatialExtent(Rectangle2D spatialExtent) {
		this.spatialExtent = spatialExtent;
	}

	public Rectangle2D getSpatialExtent() {
		return spatialExtent;
	}
	// end accessors

}