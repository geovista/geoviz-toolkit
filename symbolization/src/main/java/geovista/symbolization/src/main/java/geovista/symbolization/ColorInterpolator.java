/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;

// import javax.swing.colorchooser.*;

public class ColorInterpolator {

	public ColorInterpolator() {
	}

	public static Color mixColorsHSB(Color leftColor, Color rightColor) {
		// float[] lowVals;
		// lowVals =
		// Color.RGBtoHSB(this.getLowColor().getRed(),this.getLowColor().getGreen
		// (),this.getLowColor().getBlue(),null);
		float[] leftVals;
		leftVals = Color.RGBtoHSB(leftColor.getRed(), leftColor.getGreen(),
				leftColor.getBlue(), null);
		float[] rightVals;
		rightVals = Color.RGBtoHSB(rightColor.getRed(), rightColor.getGreen(),
				rightColor.getBlue(), null);
		float h = (leftVals[0] + rightVals[0]) / 2f;
		float s = (leftVals[1] + rightVals[1]) / 2f;
		float b = (leftVals[2] + rightVals[2]) / 2f;

		return Color.getHSBColor(h, s, b);

	}

	/*
	 * creates a new instance of a Color and returns that, does not modify input
	 * colors
	 */
	public static Color mixColorsRGB(Color leftColor, Color rightColor) {

		// hack for hsv

		// int i = 0;
		// if (i == 0){
		// return ColorInterpolator.mixColorsHSB(leftColor,rightColor);
		// }

		// float[] lowVals;
		// lowVals =
		// Color.RGBtoHSB(this.getLowColor().getRed(),this.getLowColor().getGreen
		// (),this.getLowColor().getBlue(),null);
		if (leftColor == null || rightColor == null) {
			return Color.black;
		}
		int r = (int) (((float) leftColor.getRed() + (float) rightColor
				.getRed()) / 2f);
		int g = (int) (((float) leftColor.getGreen() + (float) rightColor
				.getGreen()) / 2f);
		int b = (int) (((float) leftColor.getBlue() + (float) rightColor
				.getBlue()) / 2f);
		int a = (int) (((float) leftColor.getAlpha() + (float) rightColor
				.getAlpha()) / 2f);
		return new Color(r, g, b, a);

	}

	public static Color mixColorsRGBHighSaturation(Color leftColor,
			Color rightColor) {

		Color mixedColor = mixColorsRGB(leftColor, rightColor);
		float[] hsb = new float[3];
		Color.RGBtoHSB(mixedColor.getRed(), mixedColor.getGreen(), mixedColor
				.getBlue(), hsb);

		// logger.info("****");
		// logger.info("s " + sat);

		float[] leftVals;
		if (leftColor != null) {
			leftVals = Color.RGBtoHSB(leftColor.getRed(), leftColor.getGreen(),
					leftColor.getBlue(), null);
		} else {
			leftVals = new float[4];
		}
		float[] rightVals;
		if (rightColor != null) {
			rightVals = Color.RGBtoHSB(rightColor.getRed(), rightColor
					.getGreen(), rightColor.getBlue(), null);
		} else {
			rightVals = new float[4];
		}

		float s = (leftVals[1] + rightVals[1]) / 2f;

		if (leftVals[1] > rightVals[1]) {
			s = leftVals[1];
		} else {
			s = rightVals[1];
		}

		Color hsbColor = Color.getHSBColor(hsb[0], s, hsb[2]);
		// Color hsbColor = new Color(hsb[0], newSat, hsb[2]);

		return hsbColor;
	}
}
