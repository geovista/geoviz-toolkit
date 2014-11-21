/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.data;

import java.util.Vector;

public class TrainingData {

	Object[] dataObject;
	Object[] trainingData;
	String[] attributesDisplay;
	Vector[] trainingDataVector;
	Vector[] mergedTrainingDataVector;
	Vector[] classLabels;
	int classnumber;

	String[] trainingClassLabels;
	int[] trainingClassNumber;
	String[] trainingLabelsObs;
	String[] mergeClassLabels;
	String[] classLabelsAfterMerge;
	String mergedClassLabels; // i,j
	Vector mergedClassLabelsVector;

	public TrainingData() {
	}

	public void setDataObject(Object[] dataObject) {
		this.dataObject = dataObject;
		setTrainingData(this.dataObject);
	}

	public void setNumberOfClasses(int numClass) {
		classnumber = numClass;
	}

	public String[] getAttributeNames() {
		return attributesDisplay;
	}

	public String[] getTrainingClassInfo() {
		return trainingClassLabels;
	}

	public void setTrainingClassLabels(String[] classLabels) {
		trainingClassLabels = classLabels;
	}

	public String[] getTrainingClassLabels() {
		return trainingClassLabels;
	}

	public int[] getTrainingClassNumber() {
		return trainingClassNumber;
	}

	public void setMergeClassLabels(String[] mergeClassLabels) {
		this.mergeClassLabels = mergeClassLabels;
	}

	public String[] getClassLabelsAfterMerge() {
		return classLabelsAfterMerge;
	}

	public Vector[] getMergedTrainingDataVector() {
		return mergedTrainingDataVector;
	}

	public void setMegedClassLabels(String mergedClassLabels) {
		this.mergedClassLabels = mergedClassLabels;
		mergedClassLabelsVector = new Vector();
		int end = 0;
		int begin = 0;
		String tmpLabel = new String();
		for (int i = 0; i < this.mergedClassLabels.length(); i++) {
			if ((this.mergedClassLabels.charAt(i)) != ',') {
				continue;
			}
			end = i;
			tmpLabel = this.mergedClassLabels.substring(begin, end);
			mergedClassLabelsVector.add(tmpLabel);
			begin = end + 1;
		}
		tmpLabel = this.mergedClassLabels.substring(begin,
				this.mergedClassLabels.length());
		mergedClassLabelsVector.add(tmpLabel);
		mergedClassLabelsVector.trimToSize();

		if (mergedClassLabelsVector.size() <= 1) {
			mergedTrainingDataVector = trainingDataVector;
			classLabelsAfterMerge = trainingClassLabels;
		} else {
			mergeClasses();
		}
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
	public void setTrainingData(Object[] data) {
		setTrainingDataSet(new DataSetForApps(data));

	}

	private void setTrainingDataSet(DataSetForApps data) {
		// remove string data
		DataSetForApps dataObjTransfer = data;
		trainingData = dataObjTransfer.getDataObjectOriginal();
		attributesDisplay = dataObjTransfer.getAttributeNamesOriginal();
		double[][] trainingDataArray = new double[dataObjTransfer
				.getNumObservations()][attributesDisplay.length - 1];// last
																		// column
																		// for
																		// classificaiton
																		// info.
		// transfer data array to double array
		for (int j = 0; j < dataObjTransfer.getNumberNumericAttributes() - 1; j++) {
			int t = 0;
			if (trainingData[j + 1] instanceof double[]) {
				t = 0;
			} else if (trainingData[j + 1] instanceof int[]) {
				t = 1;
			} else if (trainingData[j + 1] instanceof boolean[]) {
				t = 2;
			}
			for (int i = 0; i < trainingDataArray.length; i++) {
				switch (t) {
				case 0:
					trainingDataArray[i][j] = ((double[]) trainingData[j + 1])[i];
					break;
				case 1:
					trainingDataArray[i][j] = ((int[]) trainingData[j + 1])[i];
					break;
				case 2:
					trainingDataArray[i][j] = ((boolean[]) trainingData[j + 1])[i]
							? 1.0 : 0.0;
					break;
				}
			}
		}
		// get class label from training data
		trainingDataVector = new Vector[classnumber];
		for (int i = 0; i < classnumber; i++) {
			trainingDataVector[i] = new Vector();
		}

		// If there isn't input for trainingClassLabels, the default will be 0,
		// 1, 2, 3, 4...
		if (trainingClassLabels == null) {
			trainingClassLabels = new String[classnumber];
			for (int i = 0; i < classnumber; i++) {
				// this.trainingClassLabels[i] = new String();
				trainingClassLabels[i] = Integer.toString(i);
			}
		}

		if (trainingClassNumber == null) {
			trainingClassNumber = new int[classnumber];
			for (int i = 0; i < classnumber; i++) {
				trainingClassNumber[i] = i + 1;// for Kioloa dataset
			}
		}

		int[] trainingLabelInt = new int[dataObjTransfer.getNumObservations()];
		trainingLabelsObs = new String[dataObjTransfer.getNumObservations()];
		// trainingLabelsObs = (String[])
		// trainingData[dataObjTransfer.getNumberNumericAttributes()];//last
		// column reserved for classificaiton info.
		trainingLabelInt = (int[]) trainingData[dataObjTransfer
				.getNumberNumericAttributes()];
		for (int i = 0; i < dataObjTransfer.getNumObservations(); i++) {
			trainingLabelsObs[i] = (new Integer(trainingLabelInt[i]))
					.toString();
		}
		for (int i = 0; i < trainingDataArray.length; i++) {
			for (int j = 0; j < classnumber; j++) {
				if (trainingLabelsObs[i].equals(trainingClassLabels[j])) {
					trainingDataVector[j].add(trainingDataArray[i]);
					continue;
				}
			}
		}
	}

	private void mergeClasses() {
		int newNum = classnumber - mergedClassLabelsVector.size() + 1;

		mergedTrainingDataVector = new Vector[newNum];
		for (int i = 0; i < newNum; i++) {
			mergedTrainingDataVector[i] = new Vector();
		}
		Vector mergedTrainingClasses = new Vector();
		classLabelsAfterMerge = new String[newNum];
		int position = -1;
		int m = 0;
		boolean foundClassMerged = false;
		for (int i = 0; i < trainingDataVector.length; i++) {
			for (int j = 0; j < mergedClassLabelsVector.size(); j++) {
				if (trainingClassLabels[i].equals(mergedClassLabelsVector
						.elementAt(j))) {
					for (int n = 0; n < trainingDataVector[i].size(); n++) {
						mergedTrainingClasses.add(trainingDataVector[i]
								.elementAt(n));
					}
					// mergedTrainingClasses.add(this.trainingDataVector[i]);
					if (position == -1) {
						position = i;
						mergedTrainingDataVector[m] = mergedTrainingClasses;
						classLabelsAfterMerge[m] = mergedClassLabels;
						m++;
					}
					// this.trainingDataVector
					foundClassMerged = true;
					continue;
				}

			}
			if (foundClassMerged == false) {
				mergedTrainingDataVector[m] = trainingDataVector[i];
				classLabelsAfterMerge[m] = trainingClassLabels[i];
				m++;
			}
			foundClassMerged = false;
		}
	}

	public Vector[] getTrainingDataVector() {
		return trainingDataVector;
	}

	public void setTrainingDataVector(Vector[] trainingDataVector) {
		this.trainingDataVector = trainingDataVector;
	}

}
