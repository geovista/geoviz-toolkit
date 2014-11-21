/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An ClassificationEvent signals that the data has been classified into n
 * classes.
 * 
 * The integers represents the class of each observation in the overall data
 * set.
 * 
 */
public class ClassificationEvent extends EventObject {

	private transient int[] classification;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * classification values are indicated.
	 */

	public ClassificationEvent(Object source, int[] classification) {
		super(source);
		this.classification = classification;
	}

	// begin accessors
	public void setClassification(int[] classification) {
		this.classification = classification;
	}

	public int[] getClassification() {
		return classification;
	}
	// end accessors

}