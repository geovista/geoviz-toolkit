/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.awt.Color;
import java.util.EventObject;

/**
 * An BackgroundColorEvent signals that a single observation has been singled
 * out. This is often because the user has "moused over" that observation.
 * 
 * The integer represents the index of that observation in the overall data set.
 * 
 */
public class BackgroundColorEvent extends EventObject {

	private transient Color background;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * background value is indicated.
	 */

	public BackgroundColorEvent(Object source, Color background) {
		super(source);
		this.background = background;
	}

	// begin accessors
	public void setBackgroundColor(Color background) {
		this.background = background;
	}

	public Color getBackgroundColor() {
		return background;
	}
	// end accessors

}