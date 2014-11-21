/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An AnnotationEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class AnnotationEvent extends EventObject {

	private String annotation;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * annotation values are indicated.
	 */

	public AnnotationEvent(Object source, String annotation) {
		super(source);
		this.annotation = annotation;
	}

	// begin accessors
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getAnnotation() {
		return annotation;
	}
	// end accessors

}