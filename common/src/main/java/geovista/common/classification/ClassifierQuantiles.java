/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

/**
 * Title: ClassifierQuantiles Description: Quantile classification method
 * Copyright: Copyright (c) 2001 Company: GeoVISTA
 *
 * @author Xiping Dai
 *
 */

import java.util.Arrays;
import java.util.logging.Logger;

import geovista.common.data.ArraySort2D;

public class ClassifierQuantiles implements DescribedClassifier,
		BoundaryClassifier {
	private static final String shortName = "Q Tiles";
	private static final String fullName = "Quantiles";
	private int[] classification;
	private int[] rank;
	private double[][] dataWithIndex;

	protected final static Logger logger = Logger
			.getLogger(ClassifierQuantiles.class.getName());

	public String getShortName() {
		return ClassifierQuantiles.shortName;
	}

	public String getFullName() {
		return ClassifierQuantiles.fullName;
	}

	public ClassifierQuantiles() {
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

		double nPerClass = (double) nObs / (double) numClasses;

		// now sort
		ArraySort2D sorter = new ArraySort2D();
		sorter.sortDouble(dataWithIndex, 0);

		for (int i = 0; i < numClasses; i++) {
			boundaries[i] = dataWithIndex[(int) (i * nPerClass)][0];
		}
		boundaries[numClasses] = dataWithIndex[nObs - 1][0];
		return boundaries;
	}

	public int[] classify(double[] data, int numClasses) {

		if (data == null) {
			logger.severe("Null passed into classify method, returning zero length array");
			return new int[0];
		}
		if (numClasses < 1) {
			throw new IllegalArgumentException(
					"Need at least one class to classify");
		}

		if (classification == null || classification.length != data.length) {
			classification = new int[data.length];
		}

		// find number of non-nulls
		int nObs = 0;
		for (int i = 0; i < data.length; i++) {
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
		rank = sorter.getSortedIndex(data, Classifier.NULL_CLASS);

		double numInEachClass = (double) nObs / (double) numClasses;

		for (int obs = 0; obs < data.length; obs++) {
			if (rank[obs] == Classifier.NULL_CLASS) {
				classification[obs] = Classifier.NULL_CLASS;
			} else {

				int whichClass = (int) (rank[obs] / numInEachClass);
				classification[obs] = whichClass;
			}
		}

		return classification;
	}

	public static void main(String[] args) {
		double[] someData = { Double.NaN, Double.NaN, 1, 1, 2, 45, 3, 45, 45,
				600, 1, Double.NaN, Double.NaN };
		ClassifierQuantiles classer = new ClassifierQuantiles();
		for (int i = 1; i < someData.length; i++) {
			int[] results = classer.classify(someData, i);

			logger.info(Arrays.toString(results));
		}
	}
}
