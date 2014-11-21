/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import geovista.common.data.DataSetForApps;

public class ClassifierKMeansClassify {

	private Object[] dataObject;
	private int[] cluster; // each element indicates the class number of the
							// corresponding state.
	private double[][] clustermean; // each row is a mean vector of a class.
	private int[] selectedAttIdx; // the indices of attributes considered in
									// this classification.
	private String[] attributesDisplay;
	private double[][] dataArray; // array for classification, each row is a
									// data vector of an observation, each
									// collum is an attribute vector.

	public ClassifierKMeansClassify() {
	}

	public int[] getKMeansClusters() {
		return cluster;
	}

	public void setClusterMean(double[][] clustermean) {
		this.clustermean = clustermean;
	}

	/**
	 * @param data
	 * 
	 * This method is deprecated becuase it wants to create its very own pet
	 * DataSetForApps. This is no longer allowed, to allow for a mutable, common
	 * data set. Use of this method may lead to unexpected program behavoir.
	 * Please use setDataSet instead.
	 */
	@Deprecated
	public void setDataObject(Object[] data) {
		setDataSet(new DataSetForApps(data));

	}

	public void setDataSet(DataSetForApps dataSet) {
		// remove string data
		DataSetForApps dataObjTransfer = dataSet;

		dataObject = dataObjTransfer.getDataSetNumericAndSpatial();
		attributesDisplay = dataObjTransfer.getAttributeNamesNumeric();
		dataArray = new double[dataObjTransfer.getNumObservations()][attributesDisplay.length];
		// transfer data array to double array
		for (int j = 0; j < attributesDisplay.length; j++) {
			int t = 0;
			if (dataObject[j + 1] instanceof double[]) {
				t = 0;
			} else if (dataObject[j + 1] instanceof int[]) {
				t = 1;
			} else if (dataObject[j + 1] instanceof boolean[]) {
				t = 2;
			}
			for (int i = 0; i < dataArray.length; i++) {
				switch (t) {
				case 0:
					dataArray[i][j] = ((double[]) dataObject[j + 1])[i];
					break;
				case 1:
					dataArray[i][j] = ((int[]) dataObject[j + 1])[i];
					break;
				case 2:
					dataArray[i][j] = ((boolean[]) dataObject[j + 1])[i] ? 1.0
							: 0.0;
					break;
				}
			}
		}
		if (selectedAttIdx == null) {
			selectedAttIdx = new int[attributesDisplay.length];
			for (int i = 0; i < attributesDisplay.length; i++) {
				selectedAttIdx[i] = i;
			}
		}
		KMeansCluster();
	}

	public void setDataArray(double[][] dataArray) {
		this.dataArray = dataArray;
		if (selectedAttIdx == null) {
			selectedAttIdx = new int[attributesDisplay.length];
			for (int i = 0; i < attributesDisplay.length; i++) {
				selectedAttIdx[i] = i;
			}
		}
		KMeansCluster();
	}

	private void KMeansCluster() {
		// loop to classify
		double[] obsWithConsideredAtt = new double[selectedAttIdx.length];// pick
																			// data
																			// within
																			// considered
																			// attributes
																			// for
																			// each
																			// observation.
		cluster = new int[dataArray.length];

		// allocate each vector to the closest cluster mean
		for (int j = 0; j < dataArray.length; j++) // every vector
		{
			double distancecloser = Double.MAX_VALUE;
			double distance = 0.0;
			cluster[j] = 0;
			// copy those considered attributes value to array for distance
			// calculation
			for (int k = 0; k < selectedAttIdx.length; k++) {
				obsWithConsideredAtt[k] = dataArray[j][selectedAttIdx[k]];
			}

			for (int k = 0; k < clustermean.length; k++) // every cluster
															// mean
			{
				// distance = getDistance(dataArray[j], clustermean[k]);
				distance = getDistance(obsWithConsideredAtt, clustermean[k]);
				if (distance < distancecloser) {
					cluster[j] = k;
					distancecloser = distance;
				}
			}
		}
	}

	public static double getDistance(double[] a, double[] b) {
		return distance(a, b);
	}

	public static double distance(double[] a, double[] b) {
		return Math.sqrt(distance2(a, b));
	}

	public static double distance2(double[] a, double[] b) // square of
															// distance
	{
		double c = 0.0;
		for (int i = 0; i < a.length; i++) {
			c += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return c;
	}

}
