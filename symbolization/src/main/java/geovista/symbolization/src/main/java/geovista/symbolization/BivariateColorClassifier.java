/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;

import geovista.common.classification.Classifier;

public interface BivariateColorClassifier {

	public Color[] symbolize(double[] dataX, double[] dataY);

	public ColorSymbolizer getXColorSymbolizer();

	public ColorSymbolizer getYColorSymbolizer();

	public Classifier getClasserX();

	public Classifier getClasserY();

	/**
	 * Returns an array of colors, Color[X][Y], where X = the Xth class, and Y =
	 * the Yth class;
	 * 
	 * @return the colors
	 */
	public Color[][] getClassColors();

}