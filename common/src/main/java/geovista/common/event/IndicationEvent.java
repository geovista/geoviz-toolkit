/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An IndicationEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 * 
 * The "indication" represents the index of that observation in the overall data
 * set.
 * 
 * This event can optionally contain information about the X and Y class of the
 * observation's classification.
 * 
 */
public class IndicationEvent extends EventObject {

	private final int indication;
	private int xClass = -1;
	private int yClass = -1;
	private int highLevelIndication = -1;
	private int[] neighbors;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * indication value is indicated.
	 */

	public IndicationEvent(Object source, int indication) {
		super(source);
		this.indication = indication;
	}

	public IndicationEvent(Object source, int indication, int[] neighbors) {
		super(source);
		this.indication = indication;
		this.neighbors = neighbors;
	}

	public IndicationEvent(Object source, int indication, int highLevelInd) {
		super(source);
		this.indication = indication;
		highLevelIndication = highLevelInd;
	}

	public int getHighLevelIndication() {
		return highLevelIndication;
	}

	public IndicationEvent(Object source, int indication, int xClass,
			int yClass, int[] neighbors) {
		super(source);
		this.indication = indication;
		this.xClass = xClass;
		this.yClass = yClass;
		this.neighbors = neighbors;
	}

	// begin accessors
	public int getIndication() {
		return indication;
	}

	public int getXClass() {
		return xClass;
	}

	public int getYClass() {
		return yClass;
	}

	/*
	 * garunteed not to be null. May return empty array.
	 */
	public int[] getNeighbors() {
		if (neighbors == null) {
			int[] emptyArray = {};
			return emptyArray;
		}
		return neighbors;
	}
	// end accessors

}