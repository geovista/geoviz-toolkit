/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

public interface BoundaryClassifier extends Classifier {

	/**
	 * Return the boundaries for the requested number of classes. It is assumed
	 * that the boundaries cover the whole data data range. This should include
	 * both the start and end boundaries, so we pass back the number of classes
	 * plus one.
	 */

	public double[] getEqualBoundaries(double[] data, int numClasses);

}
