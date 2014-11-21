/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.awt.Color;
import java.util.EventObject;

/**
 * An ClassColorEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 * 
 * 
 */
public class ClassColorEvent extends EventObject {

	private transient String classLabel;
	private transient final Color classColor;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * indication value is indicated.
	 */

	public ClassColorEvent(Object source, Color classColor) {
		super(source);
		this.classColor = classColor;
	}

	public ClassColorEvent(Object source, String classLabel, Color classColor) {
		super(source);
		this.classColor = classColor;
		this.classLabel = classLabel;
	}

	// begin accessors
	public Color getClassColor() {
		return classColor;
	}

	public String getClassLabel() {
		return classLabel;
	}

	// end accessors

}