/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.awt.Color;
import java.util.EventObject;

/**
 * An ColorArrayEvent signals that there are colors available for each
 * observation. This is often because the user has somehow indicated that these
 * observations are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class ColorArrayEvent extends EventObject {

	private transient Color[] colors;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * colors values are indicated.
	 */

	public ColorArrayEvent(Object source, Color[] colors) {
		super(source);
		this.colors = colors;
	}

	// begin accessors
	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public Color[] getColors() {
		return colors;
	}

	// end accessors

}