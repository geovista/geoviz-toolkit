/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.awt.Color;
import java.util.EventObject;
import java.util.HashSet;

/**
 * An SelectionEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 * 
 * The integers represents the indexes of that observation in the overall data
 * set.
 * 
 */
public class SelectionEvent extends EventObject {

	private int[] selection;
	private transient int[] higherLevelSelection;
	private transient Color[] multipleSlectionColors;
	private transient double[] selectedPortions;

	public enum SelectionCoverage {
		NONE, ALL, PARTIAL
	}

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * selection values are indicated.
	 */
	public SelectionEvent() {
		super(new Double(34d));

	}

	public SelectionEvent(Object source, int[] selection, int[] higherLevelSel,
			double[] portions) {
		super(source);
		this.selection = selection;
		higherLevelSelection = higherLevelSel;
		selectedPortions = portions;
	}

	public static int[] makeAndSelection(int[] leftSel, int[] rightSel) {

		HashSet<Integer> intSet = new HashSet<Integer>();
		for (int i : leftSel) {
			intSet.add(i);
		}
		for (int i : rightSel) {
			intSet.add(i);
		}
		Object[] objArray = intSet.toArray();
		int[] newSel = new int[objArray.length];
		for (int i = 0; i < objArray.length; i++) {

			Integer intNum = (Integer) objArray[i];
			newSel[i] = intNum;
		}

		return newSel;
	}

	public static int[] makeXORSelection(int[] leftSel, int[] rightSel) {

		HashSet<Integer> intSet = new HashSet<Integer>();
		for (int i : leftSel) {
			intSet.add(i);
		}
		for (int i : rightSel) {
			if (intSet.contains(i)) {
				intSet.remove(i);
			} else {
				intSet.add(i);

			}
		}
		Object[] objArray = intSet.toArray();
		int[] newSel = new int[objArray.length];
		for (int i = 0; i < objArray.length; i++) {

			Integer intNum = (Integer) objArray[i];
			newSel[i] = intNum;
		}

		return newSel;
	}

	public SelectionEvent(Object source, int[] selection, int[] higherLevelSel) {
		super(source);
		this.selection = selection;
		higherLevelSelection = higherLevelSel;
	}

	public SelectionEvent(Object source, int[] selection) {
		super(source);
		this.selection = selection;
	}

	public SelectionEvent(Object source, Color[] multipleSlectionColors) {
		super(source);
		this.multipleSlectionColors = multipleSlectionColors;
	}

	public int[] getHigherLevelSelection() {
		if (higherLevelSelection == null) {
			return new int[0];
		}
		return higherLevelSelection;
	}

	public double[] getSelectedPortions() {
		return selectedPortions;
	}

	public void setSelection(int[] selection) {
		this.selection = selection;
	}

	// begin accessors
	public int[] getSelection() {
		if (selection == null && multipleSlectionColors == null) {
			return new int[0]; // jin: fix nullpointexception
		}
		if (selection == null) {
			int selCount = 0;
			for (Color element : multipleSlectionColors) {
				if (element != null) {
					selCount++;
				}
			}
			selection = new int[selCount];
			selCount = 0;
			for (int i = 0; i < multipleSlectionColors.length; i++) {
				if (multipleSlectionColors[i] != null) {
					selection[selCount] = i;
					selCount++;
				}
			}
		}
		return selection;
	}

	public Color[] getMultipleSlectionColors() {
		return multipleSlectionColors;
	}
	// end accessors

}