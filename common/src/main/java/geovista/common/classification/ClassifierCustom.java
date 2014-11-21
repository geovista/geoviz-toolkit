/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import java.util.logging.Logger;

import geovista.common.data.DescriptiveStatistics;

public class ClassifierCustom implements DescribedClassifier,
		BoundaryClassifier {

	private static final String shortName = "Custom";
	private static final String fullName = "Custom Classifier";
	transient private int[] classification;
	double[] breaks;
	boolean inverse;
	final static Logger logger = Logger.getLogger(ClassifierCustom.class
			.getName());

	public ClassifierCustom() {

	}

	public double[] getEqualBoundaries(double[] data, int numClasses) {
		double[] boundaries = new double[numClasses + 1];
		double range = DescriptiveStatistics.range(data);
		double min = DescriptiveStatistics.min(data);
		double step = range / numClasses;

		for (int i = 0; i < boundaries.length; i++) {
			boundaries[i] = min + (step * i);
		}

		return boundaries;
	}

	public String getShortName() {
		return ClassifierCustom.shortName;
	}

	public String getFullName() {
		return ClassifierCustom.fullName;
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
		if (breaks == null) {
			// do 3 equal as a default
			ClassifierEqualIntervals classer = new ClassifierEqualIntervals();
			breaks = classer.getEqualBoundaries(data, numClasses);
		}

		classification = new int[data.length];

		for (int i = 0; i < data.length; i++) {
			if (inverse) {
				classification[i] = findBinInverse(data[i]);
			} else {
				classification[i] = findBin(data[i]);
			}
		}// next i

		return classification;
	}

	private int findBin(double val) {
		int bin = 0;
		if (Double.isNaN(val)) {
			return Classifier.NULL_CLASS;
		}
		for (int i = 0; i < breaks.length - 1; i++) {
			if (val > breaks[i] && val <= breaks[i + 1]) {
				return i;
			}
		}

		return bin;
	}

	private int findBinInverse(double val) {
		int bin = 0;
		if (Double.isNaN(val)) {
			return Classifier.NULL_CLASS;
		}
		for (int i = 0; i < breaks.length - 1; i++) {
			if (val <= breaks[i] && val > breaks[i + 1]) {
				return i;
			}
		}

		return bin;
	}

	public static void main(String[] args) {
		ClassifierCustom cust = new ClassifierCustom();
		double[] data = { 1, 6, 3, 4, 3, 2, 1, 2, 3, 4, 6 };
		double[] boundaries = cust.getEqualBoundaries(data, 3);
		for (double d : boundaries) {
			logger.info("" + d);
		}
		logger.info("" + "***************");
		cust.breaks = boundaries;

		int[] classes = cust.classify(data, 3);
		for (int i : classes) {
			logger.info("" + i);
		}
		cust.inverse = true;
		double[] newBoundaries = new double[boundaries.length];

		for (int i = 0; i < boundaries.length; i++) {
			newBoundaries[i] = boundaries[boundaries.length - i - 1];
		}
		cust.breaks = newBoundaries;

		logger.info("" + "***************");
		for (double d : boundaries) {
			logger.info("" + d);
		}
		logger.info("" + "***************");
		int[] invclasses = cust.classify(data, 3);
		for (int i : invclasses) {
			logger.info("" + i);
		}

	}

}
