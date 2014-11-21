/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification;

public class Range {
	// public static final float PRCESION=1f;//it is not practical to get
	// absolute equality. This is for approximately equal
	double max;
	double min;

	// private boolean valid;

	// private float range;
	public Range() {
		this(0.0, 0.0);
	}

	public Range(double max, double min) {
		this.max = max;
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public boolean isValid() {
		return (getRange() > 0);
	}

	public void setExtreme(Range extreme) {
		max = extreme.max;
		min = extreme.min;
	}

	public double getRange() {
		return (max - min);
	}

	public boolean equal(Range range) {
		if (getMax() == range.getMax() && getMin() == range.getMin()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + min + "," + max + "]";
	}
}
