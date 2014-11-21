/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventObject;

import geovista.common.classification.ClassificationResult;

/**
 * An ClassificationEvent signals that the data has been classified into n
 * classes.
 * 
 * The integers represents the class of each observation in the overall data
 * set.
 * 
 */
public class ClassificationResultEvent extends EventObject {

	private transient ClassificationResult classification;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * classification values are indicated.
	 */

	public ClassificationResultEvent(Object source,
			ClassificationResult classification) {
		super(source);
		this.classification = classification;
	}

	// begin accessors
	public void setClassificationResult(ClassificationResult classification) {
		this.classification = classification;
	}

	public ClassificationResult getClassificationResult() {
		return classification;
	}

	// end accessors

}
