/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An SubspaceEvent signals that a set of variables, or columns, is selected.
 * 
 */
public class SubspaceEvent extends EventObject {

	private transient int[] subspace;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * subspace values are indicated.
	 */

	public SubspaceEvent(Object source, int[] subspace) {
		super(source);
		this.subspace = subspace;
	}

	// begin accessors
	public void setSubspace(int[] subspace) {
		this.subspace = subspace;
	}

	public int[] getSubspace() {
		return subspace;
	}
	// end accessors

}