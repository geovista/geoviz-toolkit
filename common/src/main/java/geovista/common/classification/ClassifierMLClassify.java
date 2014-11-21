/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import geovista.common.data.DataSetForApps;

public class ClassifierMLClassify {

	private int[] classes; // each element indicates the class number of the
	// corresponding state.

	private Object[] dataObject; // data with unknown class information

	private String[] attributesDisplay;

	private double[][] dataArray; // array for classification, each row is a
	// data vector of an observation, each
	// collum is an attribute vector.

	private int classnumber = 5; // number of classes

	private MultiGaussian[] multiGaussian;

	private int possibleClass;

	public ClassifierMLClassify() {
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
		maximumClassifier();
	}

	public void setClassNumber(int classNumber) {
		classnumber = classNumber;
	}

	public void setClassificationModel(MultiGaussian[] multiGaussian) {
		this.multiGaussian = multiGaussian;
	}

	public void setSingleTuple(double[] tuple) {
		classifyTuple(tuple);
	}

	public int getClassTuple() {
		return possibleClass;
	}

	public int[] getClassificaiton() {
		return classes;
	}

	private void maximumClassifier() {
		// find estimated mean and standard deviation for the underlying
		// distribution of
		// each class
		classes = new int[dataArray.length];

		int tmpClass = 0;
		double[] pdfs = new double[classnumber];
		for (int i = 0; i < dataArray.length; i++) {
			for (int j = 0; j < classnumber; j++) {
				pdfs[j] = multiGaussian[j].getPDF(dataArray[i]);
			}
			// find the biggest pdf using density function of each class
			tmpClass = 0;
			for (int j = 1; j < classnumber; j++) {

				if (pdfs[j] > pdfs[tmpClass]) {
					tmpClass = j;
				}
			}
			// assign the class information to each observation.
			// this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa
			// data.
			classes[i] = tmpClass;
		}
	}

	private void classifyTuple(double[] tuple) {

		int tmpClass = 0;
		double[] pdfs = new double[classnumber];

		for (int j = 0; j < classnumber; j++) {
			pdfs[j] = multiGaussian[j].getPDF(tuple);
		}
		// find the biggest pdf using density function of each class
		tmpClass = 0;
		for (int j = 1; j < classnumber; j++) {

			if (pdfs[j] > pdfs[tmpClass]) {
				tmpClass = j;
			}
		}
		// assign the class information to each observation.
		// this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa data.
		possibleClass = tmpClass;

	}

}
