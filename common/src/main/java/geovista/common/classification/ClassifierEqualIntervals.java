/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import geovista.common.data.DescriptiveStatistics;

public class ClassifierEqualIntervals implements DescribedClassifier,
		BoundaryClassifier {

	private static final String shortName = "Eq Int";
	private static final String fullName = "Equal Intervals";
	transient private int[] classification;

	public ClassifierEqualIntervals() {

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
		return ClassifierEqualIntervals.shortName;
	}

	public String getFullName() {
		return ClassifierEqualIntervals.fullName;
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

		classification = new int[data.length];

		double max = DescriptiveStatistics.max(data);
		double min = DescriptiveStatistics.min(data);
		double dataRange = max - min;
		double classRange = dataRange / numClasses;

		double d = 0;
		double distFromMin = 0;
		double classesAlong = 0;
		int whichClass = 0;

		for (int i = 0; i < data.length; i++) {
			d = data[i];
			if (Double.isNaN(d)) {
				classification[i] = Classifier.NULL_CLASS;
			} else {
				if (d == max) {
					d = d - (classRange / 4);// make the max fit into the
					// last class
				}
				distFromMin = d - min;
				classesAlong = distFromMin / classRange;
				whichClass = (int) Math.floor(classesAlong);
				classification[i] = whichClass;
			}// end if
		}// next i

		return classification;
	}
}
