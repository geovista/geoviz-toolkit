/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.util.Vector;

public class MultiGaussianModel {

	private MultiGaussian[] multiGaussian;
	private int classNumber;
	private Vector[] trainingDataVector;

	public MultiGaussianModel() {
	}

	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}

	// before setting training data, set class number.
	public void setTrainingData(Vector[] data) {

		trainingDataVector = data;
		classNumber = trainingDataVector.length;

		multiGaussian = new MultiGaussian[classNumber];

		for (int i = 0; i < classNumber; i++) {
			multiGaussian[i] = new MultiGaussian();
			multiGaussian[i].setTrainingData(trainingDataVector[i]);
		}
	}

	public MultiGaussian[] getMultiGaussianModel() {
		return multiGaussian;
	}
}
