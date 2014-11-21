/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification;

import java.awt.Color;

public class Category extends BasicCategory implements Comparable {
	// Integer ID;
	// Color color;
	// HashSet memberIds=new HashSet();//member's id
	int showLevel;

	boolean visible = true;
	protected Color color;
	float min = Float.NaN;
	float max = Float.NaN;

	// add a data id
	public void add(Object id) {
		getMemberIds().add(id);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/*
	 * public Object getID() { return super.getID() ; }
	 */
	/**
	 * Internally, ID is 0 based. But human like 1-based index.
	 * 
	 * @return 1-based index
	 */
	public Integer getViewID() {
		Integer id = (Integer) getID();// assume it use integer as ID
		return new Integer(id.intValue() + 1);
	}

	/*
	 * public HashSet getMemberIds() { return (HashSet) super.getMemberIds() ; }
	 */
	/*
	 * public void setID(Integer ID) { this.ID = ID; } public int
	 * getMemberIdSize() { return this.getMemberIds().size() ; } public HashSet
	 * getMemberIds() { return memberIds; }
	 * 
	 * public void setMemberIds(HashSet memberIds) { this.memberIds = memberIds; }
	 */

	public int getShowLevel() {
		return showLevel;
	}

	public void setShowLevel(int showLevel) {
		this.showLevel = showLevel;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public void tryToSetMaxMin(float value) {
		if (Float.isNaN(min)) {// must initialize. Otherwise if all value>0,
								// the min will be 0, which is WRONG
			max = value;
			min = value;
			return;
		}
		if (value < min) {
			min = value;
		} else if (value > max) {
			max = value;

		}
	}

	/**
	 * This is for paint category in a color bar. Comparison is based on min
	 * value
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		Category anotherCtg = ((Category) o);
		float aMin = anotherCtg.getMin();

		if (getMin() < aMin) {
			return -1;
		} else if (getMin() > aMin) {
			return 1;
		} else {
			return 0;
		}
	}

}
