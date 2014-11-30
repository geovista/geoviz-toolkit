/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;

public interface ColorSymbolizer {

	public Color[] getColors(int numClasses);

	// public Color[] modifyColorSymbols(Color[] currSymbols, boolean[]
	// anchors);
	public int getNumClasses();

	// public Classifier getClassifier();

}