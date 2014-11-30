/*
 * SimplePalletImpl.java
 *
 * Created on December 12, 2003, 9:13 AM
 */

package geovista.symbolization;

import java.awt.Color;
import java.util.Vector;
import java.util.logging.Logger;

import geovista.colorbrewer.Palette;

/**
 * 
 * @author jfc173
 */
public class SimplePaletteImpl implements Palette {
	protected final static Logger logger = Logger
			.getLogger(SimplePaletteImpl.class.getName());
	private Color low = Color.white;
	private Color high = Color.black;
	private final Vector anchors = new Vector();
	private final ColorRamp ramp = new ColorRamp();
	private String name;
	private int noOfInputColors;

	/** Creates a new instance of SimplePalletImpl */
	public SimplePaletteImpl() {
	}

	public Color[] getColors(int length) {
		logger.finest("Simple palette impl is getting the colors.");
		Color[] returnColors = new Color[length];
		returnColors[0] = low;
		returnColors[length - 1] = high;
		boolean[] anchored = new boolean[length];
		for (int j = 0; j < length; j++) {
			anchored[j] = false;
		}
		int i = 0;
		while (i < anchors.size()) {
			int position = recalculateAnchorPosition(
					((Integer) anchors.get(i)).intValue(), length);
			if ((position < length - 1) && (position > 0)) {
				returnColors[position] = (Color) anchors.get(i + 1);
				anchored[position] = true;
			}
			i = i + 2;
		}
		ramp.rampColors(returnColors, anchored);
		return returnColors;
	}

	public void setLowColor(Color c) {
		low = c;
	}

	public void setHighColor(Color c) {
		high = c;
	}

	public void addAnchors(Color[] colors, boolean[] anchored) {
		noOfInputColors = colors.length;
		for (int i = 0; i < colors.length; i++) {
			if (anchored[i]) {
				anchors.add(new Integer(i));
				anchors.add(colors[i]);
			}
		}
	}

	private int recalculateAnchorPosition(int oldPosition, int newSize) {
		return Math.round(((float) oldPosition / (float) noOfInputColors)
				* newSize);
	}

	public void setName(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

	public String getUnivariateName() {
		return getName();
	}

	public int getRecommendedMaxLength() {
		// 30 sounds good, doesn't it? There's no theory behind the choice of
		// this number.
		return 30;
	}

	public boolean isDivergent() {
		// Returns false even though it has no idea whether this is true or
		// false.
		return false;
	}

	public boolean isQualatative() {
		// Returns false even though it has no idea whether this is true or
		// false.
		return false;
	}

	public boolean isSequential() {
		// Returns false even though it has no idea whether this is true or
		// false.
		return false;
	}

	private String colorToHex(Color c) {
		String ret = "#";
		if (c.getRed() < 16) {
			ret = ret + "0" + c.getRed();
		} else {
			ret = ret + c.getRed();
		}
		if (c.getGreen() < 16) {
			ret = ret + "0" + c.getGreen();
		} else {
			ret = ret + c.getGreen();
		}
		if (c.getBlue() < 16) {
			ret = ret + "0" + c.getBlue();
		} else {
			ret = ret + c.getBlue();
		}
		return ret;
	}

	@Override
	public String toString() {
		return "Color ramp from " + colorToHex(low) + " to " + colorToHex(high);
	}

	public boolean isQualitative() {
		return false;
	}

}
