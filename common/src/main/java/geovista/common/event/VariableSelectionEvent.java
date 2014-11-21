/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An VariableSelectionEvent signals that there is index of a variable available
 * for each observation. This is often because the user has somehow indicated
 * that these observations are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class VariableSelectionEvent extends EventObject {

	private transient int variableIndex;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * variableIndex values are indicated.
	 */

	public VariableSelectionEvent(Object source, int variableIndex) {
		super(source);
		this.variableIndex = variableIndex;
	}

	// begin accessors
	public void setVariableIndex(int variableIndex) {
		this.variableIndex = variableIndex;
	}

	public int getVariableIndex() {
		return variableIndex;
	}

	// end accessors

}
