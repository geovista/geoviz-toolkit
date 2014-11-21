/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.util.logging.Logger;

import geovista.common.data.ArraySort2D;

public class ClassifierRawQuantiles implements DescribedClassifier,
		BoundaryClassifier {

	private static final String shortName = "RQTiles";
	private static final String fullName = "Raw Quantiles";
	transient private int[] classification;
	transient private double[][] dataWithIndex;
	transient private int nObs;
	final static Logger logger = Logger.getLogger(ClassifierRawQuantiles.class
			.getName());

	public ClassifierRawQuantiles() {

	}

	public String getShortName() {
		return ClassifierRawQuantiles.shortName;
	}

	public String getFullName() {
		return ClassifierRawQuantiles.fullName;
	}

	public double[] getEqualBoundaries(double[] data, int numClasses) {
		double[] boundaries = new double[numClasses + 1];

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
		Throwable t = new Throwable();
		StackTraceElement[] es = t.getStackTrace();
		for (StackTraceElement e : es) {
			System.out.println(" in class:" + e.getClassName()
					+ " in source file:" + e.getFileName() + " in method:"
					+ e.getMethodName() + " at line:" + e.getLineNumber() + " "
					+ (e.isNativeMethod() ? "native" : ""));
		}
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

		double nPerClass = (double) nObs / (double) numClasses;
		int whichClass = 0;
		int index = 0;
		int indexPosition = 1;
		for (int i = 0; i < data.length; i++) {
			index = (int) dataWithIndex[i][indexPosition];
			if (Double.isNaN(dataWithIndex[i][0])) {
				classification[index] = Classifier.NULL_CLASS;
			} else {
				whichClass = (int) Math.floor(i / nPerClass);
				if (whichClass == numClasses) {
					System.err
							.println("needed hack... in Classifier Raw Quantiles");
					whichClass--;
				}
				classification[index] = whichClass;
			}// end if null
		}// next obs

		return classification;
	}
}
