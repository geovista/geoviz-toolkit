/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import java.util.logging.Level;
import java.util.logging.Logger;

import geovista.common.data.DescriptiveStatistics;

public class ClassifierStdDev implements DescribedClassifier,
		BoundaryClassifier {

	private static final String shortName = "Std Dev";
	private static final String fullName = "Standard Deviation";
	transient private int[] classification;
	protected final static Logger logger = Logger
			.getLogger(ClassifierStdDev.class.getName());

	public ClassifierStdDev() {
	}

	public String getShortName() {
		return ClassifierStdDev.shortName;
	}

	public String getFullName() {
		return ClassifierStdDev.fullName;
	}

	public double[] getEqualBoundaries(double[] data, int numClasses) {
		double[] boundaries = new double[numClasses + 1];
		double max = DescriptiveStatistics.max(data);
		double min = DescriptiveStatistics.min(data);
		// double dataRange = DescriptiveStatistics.range(data);
		double mean = DescriptiveStatistics.mean(data);
		double stdDev = DescriptiveStatistics.fineStdDev(data, true);
		// When 'numClasses' is even, do the classification by loop
		if ((numClasses % 2) == 0) {
			for (int i = 0; i < boundaries.length; i++) {
				if (i <= numClasses / 2) {
					if (i == 0) {
						boundaries[i] = min;
					} else {
						boundaries[i] = mean + (stdDev * (i - numClasses / 2));
						if (min >= boundaries[i]) {
							boundaries[i] = min;
						}
					}
				} else {
					if (i == numClasses) {
						boundaries[i] = max;
					} else {
						boundaries[i] = mean + (stdDev * (i - numClasses / 2));
						if (max <= boundaries[i]) {
							boundaries[i] = max;
						}
					}
				}
				// if (i == 0){
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2));
				// if (min < boundaries[i])
				// boundaries[i] = min;
				// }else if (i == numClasses){
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2));
				// if (max > boundaries[i])
				// boundaries[i] = max;
				// }else{
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2));
				// }
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("" + boundaries[i]);
				}
			}
		} else {
			for (int i = 0; i < boundaries.length; i++) {
				if (i <= numClasses / 2) {
					if (i == 0) {
						boundaries[i] = min;
					} else {
						boundaries[i] = mean
								+ (stdDev * (i - numClasses / 2 - 0.5));
						if (min >= boundaries[i]) {
							boundaries[i] = min;
						}
					}
				} else {
					if (i == numClasses) {
						boundaries[i] = max;
					} else {
						boundaries[i] = mean
								+ (stdDev * (i - numClasses / 2 - 0.5));
						if (max <= boundaries[i]) {
							boundaries[i] = max;
						}
					}
				}

				// if (i == 0){
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2 - 0.5));
				// if (min < boundaries[i])
				// boundaries[i] = min;
				// }else if (i == numClasses){
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2 - 0.5));
				// if (max > boundaries[i])
				// boundaries[i] = max;
				// }else{
				// boundaries[i] = mean + (stdDev * (i - numClasses / 2 - 0.5));
				// }
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("" + boundaries[i]);
				}
			}
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

		// double max = DescriptiveStatistics.max(data);
		// double min = DescriptiveStatistics.min(data);
		// double dataRange = DescriptiveStatistics.range(data);
		double mean = DescriptiveStatistics.mean(data);
		double stdDev = DescriptiveStatistics.fineStdDev(data, true);

		classification = new int[data.length];

		// if the number of classes is only 1, just classify all the data into
		// one class
		if (numClasses == 1) {
			for (int i = 0; i < data.length; i++) {
				classification[i] = 0;
			}
		}

		// When 'numClasses' is even, do the classification by loop
		if (Math.floor(numClasses / 2) == (numClasses / 2)) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < (numClasses / 2); j++) {
					// data less than the lower limit is classified into the
					// first class
					if (data[i] <= (mean - (((double) numClasses - 2) / 2)
							* stdDev)) {
						classification[i] = 0;
					}

					// data greater than the upper limit is classified into the
					// last class
					if (data[i] > (mean + (((double) numClasses - 2) / 2)
							* stdDev)) {
						classification[i] = numClasses - 1;
					}
					// other data are classified as follows
					else {
						if ((data[i] > (mean - ((numClasses / 2) - j - 1)
								* stdDev))
								&& (data[i] <= (mean - ((numClasses / 2) - j - 2)
										* stdDev))) {
							classification[i] = j + 1;
						}
					}
				}
				if (Double.isNaN(data[i])) {
					classification[i] = Classifier.NULL_CLASS;
				}
			}
		}

		// When 'numClasses' is odd, do the classification similarly with a
		// little difference
		if (Math.floor(numClasses / 2) < (numClasses / 2)) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j <= ((numClasses + 1) / 2); j++) {
					// data less than the lower limit is classified into the
					// first class
					if (data[i] <= (mean - ((numClasses - 1) / 2) * stdDev)) {
						classification[i] = 0;
					}

					// data greater than the upper limit is classified into the
					// last class
					if (data[i] > (mean + ((numClasses - 1) / 2) * stdDev)) {
						classification[i] = numClasses - 1;
					}

					// data between mean plus stdDev and mean minus stdDev is
					// classified into the middle class
					if ((data[i] <= (mean + stdDev))
							&& (data[i] > (mean - stdDev))) {
						classification[i] = (numClasses + 1) / 2 - 1;
					}
					// other data are classified as follows
					else {
						if (data[i] <= mean) {
							if (((data[i] > (mean - (((numClasses + 1) / 2) - j - 1)
									* stdDev)) && (data[i] <= (mean - (((numClasses + 1) / 2)
									- j - 2))
									* stdDev))) {
								classification[i] = j + 1;
							}

							if (data[i] > mean) {
								if (((data[i] > (mean - (((numClasses + 1) / 2)
										- j - 1)
										* stdDev)) && (data[i] <= (mean - (((numClasses + 1) / 2)
										- j - 2))
										* stdDev))) {
									classification[i] = j;
								} // end if
							} // end if
						} // end if
					} // end else
				} // end for
			} // end for
		} // end if

		return classification;
	} // end classify
}
