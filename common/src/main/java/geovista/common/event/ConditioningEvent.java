/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An ConditioningEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 * 
 * The integers signal if the given observation is in range or not.
 * 
 * 0 = in current conditioning -1 = not in current conditioning
 * 
 */
public class ConditioningEvent extends EventObject {

	private transient int[] conditioning;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * conditioning values are indicated.
	 */

	public ConditioningEvent(Object source, int[] conditioning) {
		super(source);
		this.conditioning = conditioning;
	}

	// begin accessors
	public void setConditioning(int[] conditioning) {
		this.conditioning = conditioning;
	}

	public int[] getConditioning() {
		return conditioning;
	}
	// end accessors

}