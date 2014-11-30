/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;

import geovista.common.classification.Classifier;

public interface ColorClassifier {

	public Color[] symbolize(double[] data);

	public ColorSymbolizer getColorer();

	public Classifier getClasser();

}