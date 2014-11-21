/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.util.logging.Level;
import java.util.logging.Logger;

import geovista.common.data.ArraySort2D;

public class ClassifierModifiedQuantiles implements DescribedClassifier,
		BoundaryClassifier {
	protected final static Logger logger = Logger
			.getLogger(ClassifierModifiedQuantiles.class.getName());
	private static final String shortName = "MQ Tiles";
	private static final String fullName = "ModifiedQuantiles";
	transient private int[] classification;
	transient private double[][] dataWithIndex;

	public ClassifierModifiedQuantiles() {
	}

	public String getShortName() {
		return ClassifierModifiedQuantiles.shortName;
	}

	public String getFullName() {
		return ClassifierModifiedQuantiles.fullName;
	}

	public double[] getEqualBoundaries(double[] data, int numClasses) {
		double[] boundaries = new double[numClasses + 1];
		int nObs;
		if (dataWithIndex == null || dataWithIndex.length != data.length
				|| dataWithIndex[0].length != 2) {
			dataWithIndex = new double[data.length][2];
		}

		// add index
		// and find number of non-nulls
		nObs = 0;
		for (int i = 0; i < data.length; i++) {
			dataWithIndex[i][0] = data[i];
			dataWithIndex[i][1] = i;
			if (!Double.isNaN(data[i])) {
				nObs++;
			}
		}
		// now sort
		ArraySort2D sorter = new ArraySort2D();
		sorter.sortDouble(dataWithIndex, 0);
		if (numClasses < 5) {
			double nPerClass = (double) nObs / (double) numClasses;
			for (int i = 0; i < numClasses; i++) {
				boundaries[i] = dataWithIndex[(int) (i * nPerClass)][0];
			}
			boundaries[numClasses] = dataWithIndex[nObs - 1][0];
		} else {
			double nPerClass = (double) nObs / (double) (numClasses - 2);
			for (int i = 0; i < numClasses; i++) {
				if (i == 0) {
					boundaries[i] = dataWithIndex[0][0];
				} else if (i == 1) {
					boundaries[i] = dataWithIndex[(int) (nPerClass * 0.5)][0];
				} else if (i == (numClasses - 1)) {
					boundaries[i] = dataWithIndex[(int) (nPerClass * (i - 1.5))][0];
				} else {
					boundaries[i] = dataWithIndex[(int) (nPerClass * (i - 1))][0];
				}
			}
			boundaries[numClasses] = dataWithIndex[nObs - 1][0];
		}
		return boundaries;
	}

	public int[] classify(double[] data, int numClasses) {

		if (data == null) {
			throw new IllegalArgumentException(
					"Can't pass null into classify method");
		}
		if (numClasses < 1) {
			throw new IllegalArgumentException(
					"Need at least one class to classify");
		}

		if (classification == null || classification.length != data.length) {
			classification = new int[data.length];
		}
		if (dataWithIndex == null || dataWithIndex.length != data.length
				|| dataWithIndex[0].length != 2) {
			dataWithIndex = new double[data.length][2];
		}

		// add index
		// and find number of non-nulls
		int nObs = 0;
		for (int i = 0; i < data.length; i++) {
			dataWithIndex[i][0] = data[i];
			dataWithIndex[i][1] = i;
			if (!Double.isNaN(data[i])) {
				nObs++;
			}
		}

		// If classes number more than available obeservations numbers for
		// classfy
		// Change the the number of classes to the available number
		if (numClasses > nObs) {
			numClasses = nObs;
		}

		// now sort
		ArraySort2D sorter = new ArraySort2D();
		sorter.sortDouble(dataWithIndex, 0);
		int index;

		if (numClasses < 5) {
			ClassifierQuantiles useQuantiles = new ClassifierQuantiles();
			classification = useQuantiles.classify(data, numClasses);
			return classification;
		}

		double numInEachClass = (double) nObs / (double) (numClasses - 2);
		int endElm = 0;
		int lastClassEndElm = 0;
		double end;
		// Calculate the begin and end values for this classification range. For
		// quantiles, we might
		// have to do some tricky round-off.
		for (int i = 0; i < numClasses; i++) {
			// lastClassEndElm = endElm;
			if (i < (numClasses - 1)) {
				if (i == 0) {
					endElm = (int) (numInEachClass * 0.5);
				} else if (i == (numClasses - 2)) {
					endElm = (int) (numInEachClass * (i - 0.5)) - 1;
				} else {
					endElm = (int) (numInEachClass * i) - 1;
				}
				// Check to see if the class end (boundary) has passed the ideal
				// one.
				if ((lastClassEndElm >= endElm && (lastClassEndElm != 0))) {
					continue;
				}

				// Determine how many elements in the array are equal to the
				// last element in this (i) class.
				end = dataWithIndex[endElm][0];
				int sameBefore = 0;
				int sameAfter = 0;
				while (((endElm - sameBefore - 1) >= lastClassEndElm)
						&& (dataWithIndex[endElm - sameBefore - 1][0] == end)) {
					sameBefore++;
				}
				// Determine how many elements in the i+1 class are equal to the
				// last element in i class .
				while (((endElm + sameAfter + 1) <= nObs)
						&& (dataWithIndex[endElm + sameAfter + 1][0] == end)) {
					sameAfter++;
				}
				// If the same numbers of same data on both sides of
				// classification line,
				// assign all of the same data to the next class.
				if ((sameBefore <= sameAfter)) {
					endElm = endElm - sameBefore;
					if (lastClassEndElm == endElm) {
						classification[endElm] = i;
					}
					for (int j = lastClassEndElm + 1; j <= endElm; j++) {
						index = (int) dataWithIndex[j][1];
						classification[index] = i;
					}
					lastClassEndElm = endElm;

					if ((int) (sameAfter / numInEachClass) > 0) {
						endElm = endElm + sameBefore + sameAfter;
						for (int j = lastClassEndElm + 1; j <= endElm; j++) {
							index = (int) dataWithIndex[j][1];
							classification[index] = i + 1;
						}
						lastClassEndElm = endElm;
					}
				} else {
					endElm = endElm + sameAfter;
					for (int j = lastClassEndElm + 1; j <= endElm; j++) {
						index = (int) dataWithIndex[j][1];
						classification[index] = i;
					}
					lastClassEndElm = endElm;
				}
			} else {
				// Everything goes into last bucket
				// System.out.print("last Elm: " + (nObs - 1) + " ");
				index = 0;
				for (int j = lastClassEndElm + 1; j < nObs; j++) {
					index = (int) dataWithIndex[j][1];
					classification[index] = i;
				}
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("last class drawn.");
				}
			}
		}
		index = 0;
		for (int j = nObs; j < data.length; j++) {
			index = (int) dataWithIndex[j][1];
			classification[index] = Classifier.NULL_CLASS;
		}
		return classification;
	}
}
