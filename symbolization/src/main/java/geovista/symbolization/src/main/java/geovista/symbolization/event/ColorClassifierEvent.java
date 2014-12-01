/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.symbolization.event;

import java.util.EventObject;

import geovista.symbolization.ColorClassifier;

/**
 * An ColorClassifierEvent signals that a set of observations has been singled
 * out. This is often because the user has somehow indicated that these
 * observations are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class ColorClassifierEvent extends EventObject {

	private transient ColorClassifier colorClasser;
	public static final int SOURCE_ORIENTATION_X = 0;
	public static final int SOURCE_ORIENTATION_Y = 1;
	private int orientation;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * colorClasser values are indicated.
	 */

	public ColorClassifierEvent(Object source,
			ColorClassifier colorClasser) {
		super(source);
		this.colorClasser = colorClasser;
		orientation = -1;// none
	}

	// begin accessors
	public void setColorSymbolClassification(
			ColorClassifier colorClasser) {
		this.colorClasser = colorClasser;
	}

	public ColorClassifier getColorSymbolClassification() {
		return colorClasser;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getOrientation() {
		return orientation;
	}

	// end accessors

}