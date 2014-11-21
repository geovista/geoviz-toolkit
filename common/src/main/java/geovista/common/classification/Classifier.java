/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

public interface Classifier {

	public static final int NULL_CLASS = -1;

	/**
	 * Returns an integer array the same length as the input data array, with
	 * each integer representing the class of that datum.
	 * 
	 */

	public int[] classify(double[] data, int numClasses);
}
