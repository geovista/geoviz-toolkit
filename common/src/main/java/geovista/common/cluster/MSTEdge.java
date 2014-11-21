package geovista.common.cluster;

import java.util.ResourceBundle;

/**
 * Title: Minimum Spanning Tree Edge class Description: Used for the minimum
 * spanning tree assignment
 * 
 * @author Markus Svensson
 * @author Frank Hardisty
 */

public class MSTEdge implements Comparable {
	private int start;

	private int end;

	private final double weight;

	/**
	 * Default constructor, init weight to +infinity
	 */
	public MSTEdge() {
		weight = Integer.MAX_VALUE;
	}

	/**
	 * Constructor
	 * 
	 * @param The
	 *            start vertex
	 * @param The
	 *            end vertex
	 * @param The
	 *            weight
	 */
	public MSTEdge(int start, int end, double weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	/**
	 * Get the weight of the edge
	 * 
	 * @return The weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Get the start vertex
	 * 
	 * @return The start vertex
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Get the end vertex
	 * 
	 * @return The end vretex
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Print the edge
	 * 
	 * @param The
	 *            language to use for output
	 * @return String representation of the edge
	 */
	public String toString(ResourceBundle res) {
		String returnString = (res.getString("Start") + start
				+ res.getString("End") + end + res.getString("Weight") + weight);
		return returnString;

	}

	public int compareTo(Object obj) {
		MSTEdge e = (MSTEdge) obj;
		int val = 0;
		if (Double.isNaN(e.weight)) {
			if (Double.isNaN(weight)) {
				return 0;
			}
			return 1;
		}// end if the other value is NaN

		if (Double.isNaN(weight)) {
			val = -1;// everything is bigger than NaN
		} else if (weight < e.weight) {
			val = -1;
		} else if (weight > e.weight) {
			val = 1;
		}

		return val;
	}
}