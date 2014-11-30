/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class is a utility class which allows other classes to find ramped and
 * interpolated color values.
 */
public class ColorRamp {

	private transient Color lowColor; // associated with low values
	private transient Color highColor; // associated with high values
	static final Logger logger = Logger.getLogger(ColorRamp.class.getName());

	public ColorRamp() {
		// lowColor = Color.white;
		// highColor = Color.black;
	}

	public void rampInts(int[] intList, boolean[] anchored) {
		if (intList == null || intList.length < 1) {
			return; // nothing do do
		}
		int lowInt = 0;
		int highInt = Integer.MAX_VALUE;
		anchored[0] = true;
		anchored[intList.length - 1] = true;
		// here we walk through and ramp based on the anchored colors
		int currLowSwatch = 0;
		int currHighSwatch = 0;
		for (int j = 0; j < intList.length - 1; j++) {
			// find a locked color
			if (anchored[j]) {
				lowInt = (intList[j]);
				currLowSwatch = j;
				// look for the next anchored color
				for (int k = j + 1; k < intList.length; k++) {
					if (anchored[k]) {
						highInt = (intList[k]);
						currHighSwatch = k;
						// if there are any colors in between
						int numSwatches = currHighSwatch - currLowSwatch - 1;
						if (numSwatches > 0) {
							int currSwat = 1;
							for (int l = currLowSwatch + 1; l < currHighSwatch; l++) {
								float prop;
								prop = currSwat / ((float) numSwatches + 1);
								currSwat++;
								float diff = highInt - lowInt;
								float newVal = diff * prop + lowInt;

								intList[l] = Math.round(newVal);
							}// next l
						} // if swatches > 0
						// currLowSwatch = k;
						break;// found that anchor, don't find more.
					}// end if k locked

				}// next k
			}// end if j locked
		}// next j

		// this.lowColor = (colorList[0]);//????

	}

	public void rampColors(Color[] colorList, boolean[] anchored) {
		if (colorList == null || colorList.length < 1) {
			return; // nothing do do
		}

		anchored[0] = true;
		anchored[colorList.length - 1] = true;
		// here we walk through and ramp based on the anchored colors
		int currLowSwatch = 0;
		int currHighSwatch = 0;
		for (int j = 0; j < colorList.length - 1; j++) {
			// find a locked color
			if (anchored[j]) {
				lowColor = (colorList[j]);
				currLowSwatch = j;
				// look for the next anchored color
				for (int k = j + 1; k < colorList.length; k++) {
					if (anchored[k]) {
						highColor = (colorList[k]);
						currHighSwatch = k;
						// if there are any colors in between
						int numSwatches = currHighSwatch - currLowSwatch - 1;
						if (numSwatches > 0) {
							int currSwat = 1;
							for (int l = currLowSwatch + 1; l < currHighSwatch; l++) {
								double prop;
								prop = currSwat / ((double) numSwatches + 1);
								currSwat++;
								Color back = new Color(getRampedValueRGB(prop));
								colorList[l] = back;
							}// next l
						} // if swatches > 0
						// currLowSwatch = k;
						break;// found that anchor, don't find more.
					}// end if k locked

				}// next k
			}// end if j locked
		}// next j

		lowColor = (colorList[0]);// ????

	}

	public void rampColors(Color[] colorList) {
		boolean[] anchors = new boolean[colorList.length];
		anchors[0] = true;
		anchors[colorList.length - 1] = true;
		this.rampColors(colorList, anchors);
	}

	public int getRampedValueRGB(double prop) {
		// hack for hsv
		// int i = 0;
		// if (i == 0){
		// return this.getRampedValueHSB(prop);
		// }
		if (highColor == null || lowColor == null) {
			logger.info("hit null color");
			return Color.black.getRGB();
		}
		int redRange = highColor.getRed() - lowColor.getRed();
		int newRed = (int) Math.round(prop * redRange);
		newRed = newRed + lowColor.getRed();

		int GreenRange = highColor.getGreen() - lowColor.getGreen();
		int newGreen = (int) Math.round(prop * GreenRange);
		newGreen = newGreen + lowColor.getGreen();

		int BlueRange = highColor.getBlue() - lowColor.getBlue();
		int newBlue = (int) Math.round(prop * BlueRange);
		newBlue = newBlue + lowColor.getBlue();

		int intARGB = (255 << 24) | (newRed << 16) | (newGreen << 8)
				| (newBlue << 0);
		return intARGB;
	}

	public int getRampedValueHSB(double aProp) {
		float prop = (float) aProp;
		float[] lowVals;
		float[] highVals;

		lowVals = Color.RGBtoHSB(lowColor.getRed(), lowColor.getGreen(),
				lowColor.getBlue(), null);
		highVals = Color.RGBtoHSB(highColor.getRed(), highColor.getGreen(),
				highColor.getBlue(), null);

		float hueRange = highVals[0] - lowVals[0];
		float newHue = prop * hueRange;
		newHue = newHue + lowVals[0];

		float saturationRange = highVals[1] - lowVals[1];
		float newSaturation = prop * saturationRange;
		newSaturation = newSaturation + lowVals[1];

		float brightnessRange = highVals[2] - lowVals[2];
		float newBrightness = prop * brightnessRange;
		newBrightness = newBrightness + lowVals[2];

		int intARGB = Color.HSBtoRGB(newHue, newSaturation, newBrightness);

		return intARGB;
	}

	public int getRampedValueHB(double aProp) {
		float prop = (float) aProp;
		float[] lowVals;
		float[] highVals;
		lowVals = Color.RGBtoHSB(lowColor.getRed(), lowColor.getGreen(),
				lowColor.getBlue(), null);
		highVals = Color.RGBtoHSB(highColor.getRed(), highColor.getGreen(),
				highColor.getBlue(), null);

		float hueRange = highVals[0] - lowVals[0];
		float newHue = prop * hueRange;
		newHue = newHue + lowVals[0];

		float brightnessRange = highVals[2] - lowVals[2];
		float newBrightness = prop * brightnessRange;
		newBrightness = newBrightness + lowVals[2];

		int intARGB = Color.HSBtoRGB(newHue, 1.0f, newBrightness);

		return intARGB;
	}

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new BorderLayout());

		// make color ramp
		JPanel rampPan = new JPanel();
		rampPan.setLayout(new FlowLayout());
		JPanel[] panSet = new JPanel[10];
		ColorRamp ramp = new ColorRamp();

		int[] someInts = { 1, 2, 9, 10 };
		boolean[] someAnchors = { false, true, false, false };
		ramp.rampInts(someInts, someAnchors);

		for (int i = 0; i < panSet.length; i++) {
			panSet[i] = new JPanel();
			panSet[i].setPreferredSize(new Dimension(100, 100));
			double prop;
			prop = (double) i / (double) panSet.length;
			Color back = new Color(ramp.getRampedValueRGB(prop));
			panSet[i].setBackground(back);
			rampPan.add(panSet[i]);
		}
		app.getContentPane().add(rampPan, BorderLayout.SOUTH);

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}

}
