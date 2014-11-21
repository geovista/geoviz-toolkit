/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Color;
import java.util.BitSet;

import geovista.common.classification.Classifier;
import geovista.common.data.DataSetForApps;

public class VisualSettings {

	public enum SelectionSettings {
		FADE, BLUR, OUTLINE
	}

	public static boolean FADE_ENABLED_DEFAULT = true;
	public static boolean BLUR_ENABLED_DEFAULT = true;

	// Data
	DataSetForApps currentDataSet;

	// Display
	Color indicationColor;
	Color selectionColor;
	Color indicationNeighborsColor;
	Color backgroundColor;

	// Category
	Classifier preferredClassifier;
	BitSet selection;

}
