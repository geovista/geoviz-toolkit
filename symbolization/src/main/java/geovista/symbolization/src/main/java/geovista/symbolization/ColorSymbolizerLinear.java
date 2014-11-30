/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;

import geovista.colorbrewer.Palette1D;

//import javax.swing.colorchooser.*;

public class ColorSymbolizerLinear implements ColorSymbolizer {

	private Color lowColor; // associated with low values
	private Color highColor; // associated with high values
	// private ColorRamp ramper;
	private boolean[] anchors;
	private Color[] rampingColors;
	private final Palette1D pal;

	public ColorSymbolizerLinear(Palette1D pal) {
		this.pal = pal;
		// defaults
		lowColor = ColorRampPicker.DEFAULT_LOW_COLOR;
		highColor = ColorRampPicker.DEFAULT_HIGH_COLOR_PURPLE;
		initColors(3);

	}

	private void initColors(int numColors) {
		rampingColors = new Color[numColors];
		rampingColors[0] = lowColor; // anchor first and last colors
		rampingColors[numColors - 1] = highColor;
		if (rampingColors.length > 2) {
			for (int i = 1; i < numColors - 2; i++) {
				rampingColors[i] = Color.black;
			}
		}
	}

	public Color getLowColor() {
		return lowColor;
	}

	public void setLowColor(Color aColor) {
		lowColor = aColor;
		rampingColors[0] = aColor;
	}

	public Color getHighColor() {
		return highColor;
	}

	public void setHighColor(Color aColor) {
		highColor = aColor;
		if (rampingColors.length > 1) {
			rampingColors[rampingColors.length - 1] = aColor;
		}
	}

	public boolean[] getAnchors() {
		return anchors;
	}

	public void setAnchors(boolean[] anchors) {
		this.anchors = anchors;
	}

	public int getNumClasses() {
		return rampingColors.length;
	}

	public Color[] getRampingColors() {
		return rampingColors;

	}

	public void setRampingColors(Color[] rampingColors) {
		this.rampingColors = rampingColors;
		lowColor = rampingColors[0];
		highColor = rampingColors[rampingColors.length - 1];

	}

	public Color[] getColors(int numColors) {
		if (anchors == null || anchors.length != numColors) {
			anchors = new boolean[numColors];
			anchors[0] = true; // anchor first and last colors
			anchors[numColors - 1] = true;
		}

		rampingColors = pal.getColors(numColors);

		return rampingColors;
	}

}
