/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.Arrays;
import java.util.logging.Logger;

public class ArraySort2D {
	final static Logger logger = Logger.getLogger(ArraySort2D.class.getName());

	private class IndexedDouble implements Comparable<IndexedDouble> {
		double data;
		int index;

		IndexedDouble(double data, int index) {
			this.data = data;
			this.index = index;
		}

		public int compareTo(IndexedDouble otherVal) {
			if (data < otherVal.data) {
				return -1;
			}
			if (data > otherVal.data) {
				return 1;
			}
			// not greater, not lesser, must be equal.
			return 0;
		}

	}

	// XXX this is freezing the jvm (infinate loop?) on nulls
	// ... needs to be fixed + have some unit tests...
	// fix implemented, but not tested...
	private class SortableDoubleArray implements
			Comparable<SortableDoubleArray> {

		double[] data;
		int index;

		SortableDoubleArray(double[] data, int index) {
			this.data = data;
			this.index = index;
		}

		public int compareTo(SortableDoubleArray o) {
			if (data[index] < o.data[index]) {
				return -1;
			}
			if (data[index] > o.data[index]) {
				return 1;
			}
			// not greater, not lesser, must be equal.
			return 0;
		}

	}

	public void sortDouble(double[][] dataIn, int whichColumn) {
		SortableDoubleArray[] doubleArrays = new SortableDoubleArray[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			doubleArrays[i] = new SortableDoubleArray(dataIn[i], whichColumn);
		}
		Arrays.sort(doubleArrays);
		for (int i = 0; i < dataIn.length; i++) {
			dataIn[i] = doubleArrays[i].data;
		}

	}

	public int[] getSortedIndex(double[] dataIn) {
		return this.getSortedIndex(dataIn, DataSetForApps.NULL_INT_VALUE);
	}

	public int[] getSortedIndex(double[] dataIn, int NaNValue) {

		IndexedDouble[] doubleArray = new IndexedDouble[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			doubleArray[i] = new IndexedDouble(dataIn[i], i);
		}

		Arrays.sort(doubleArray);
		int[] indexArray = new int[dataIn.length];
		double previousValue = Double.NaN;

		int previousI = 0;
		int nonNulls = 0;
		for (int i = 0; i < dataIn.length; i++) {
			if (Double.isNaN(doubleArray[i].data)) {
				indexArray[doubleArray[i].index] = NaNValue;
			} else if (doubleArray[i].data == previousValue) {
				indexArray[doubleArray[i].index] = previousI;
				nonNulls++;
			} else {
				indexArray[doubleArray[i].index] = nonNulls;
				previousValue = doubleArray[i].data;
				previousI = nonNulls;
				nonNulls++;
			}

		}

		return indexArray;

	}

	public static void main(String[] args) {
		double[] someData = { Double.NaN, Double.NaN, 1, 1, 2, 45, 3, 45, 45,
				1, Double.NaN, Double.NaN };
		ArraySort2D sorter = new ArraySort2D();
		int[] results = sorter.getSortedIndex(someData);

		logger.info(Arrays.toString(results));

	}
}
