/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty, Blair Christian */

package geovista.common.data;

import javax.swing.JDialog;

/**
 * This interface defines the general contract for smoothers.
 * 
 */
public interface SpatialSmoother {
	/**
	 * 
	 * The values returned represent a grid of smoothed values in colomn-major
	 * format.
	 * 
	 * Smoothing is a potentially expensive operation, so clients may wish to
	 * call SpatialSmoothers in a seperate thread.
	 * 
	 * Some classifiers may not make use of weights, conditioning, or the int[]
	 * argument.
	 * 
	 */
	public double[] smooth(double[] xVals, double[] yVals, double[] rates,
			double[] weights, double[] conditioningVariable,
			int[] additionalVariable, int numRows, int numColumns);

	/**
	 * This method is called to prompt the user for additional arguments used by
	 * particular smoothing algorithms. The JDialog should return an object
	 * which contains the correct arguments, such that that object can be passed
	 * into the "setAdditionalParameters" method defined in this interface.
	 * 
	 */
	public JDialog promptUser();

	/**
	 * The object retrieved from the JDialog created by calling "promptUser"
	 * should be passed in here.
	 * 
	 */
	public void setAdditionalParameters(Object parameters);

}
