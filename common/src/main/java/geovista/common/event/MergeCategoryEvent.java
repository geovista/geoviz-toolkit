/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventObject;

/**
 * An ClassColorEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 * 
 * 
 */
public class MergeCategoryEvent extends EventObject {

	private transient final String mergedClassLabel;

	// private transient Color classColor;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * indication value is indicated.
	 */

	// public ClassColorEvent(Object source, Color classColor){
	// super(source);
	// this.classColor = classColor;
	// }
	public MergeCategoryEvent(Object source, String mergedClassLabel) {
		super(source);
		// this.classColor = classColor;
		this.mergedClassLabel = mergedClassLabel;
	}

	// begin accessors
	// public Color getClassColor () {
	// return this.classColor;
	// }

	public String getClassLabel() {
		return mergedClassLabel;
	}

	// end accessors

}