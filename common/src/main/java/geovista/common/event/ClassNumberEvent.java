/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventObject;

/**
 * An ClassNumberEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation. Jin: The
 * event means the number of categories in a classfication result has changed.
 * It also indicate dimension of classificaiton changed ( 1D or 2D, see comment
 * for bivariateClassNumber) todo: it would be better if implement the concept
 * as with 2 subclasses: 1D ClassNumber event, 2D ClassNumberEvent.
 * 
 */
public class ClassNumberEvent extends EventObject {

	private transient int classNumber; // jin:
	private transient int[] bivariateClassNumber; // jin: if the array is not
													// empty, it must be a
													// multivariate
													// classificaiton. In this
													// case, classNumber must be
													// 0

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * indication value is indicated.
	 */

	public ClassNumberEvent(Object source, int classNumber) {
		super(source);
		this.classNumber = classNumber;
	}

	// begin accessors
	public int getClassNumber() {
		return classNumber;
	}

	// end accessors

	public ClassNumberEvent(Object source, int[] classNumber) {
		super(source);
		bivariateClassNumber = classNumber;
	}

	public int[] getBivariateClassNumber() {
		return bivariateClassNumber;
	}

}
