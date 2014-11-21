/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.util.Vector;
import java.util.logging.Logger;

import geovista.common.data.StatisticsVectors;

public class MultiGaussian {
	private double[][] classTrainingData;
	private double[] meanVector;
	private double[][] covarianceMatrix;
	private double[][] inverseCovMatrix;
	private double determinant;
	private double sqrtDeterminant;
	private int n;
	private int len;// observation numbers
	private boolean isUnbias = true;
	protected final static Logger logger = Logger.getLogger(MultiGaussian.class
			.getName());

	public MultiGaussian() {
	}

	public MultiGaussian(double[][] classTrainingData) {
		this.classTrainingData = classTrainingData;
		n = this.classTrainingData[0].length;
		getMultiGaussianDistribution();
	}

	public void setTrainingData(double[][] classTrainingData) {
		this.classTrainingData = classTrainingData;
		n = this.classTrainingData[0].length;
		getMultiGaussianDistribution();
	}

	public void setTrainingData(Vector classTrainingDataVector) {
		n = ((double[]) classTrainingDataVector.get(0)).length;
		classTrainingData = new double[classTrainingDataVector.size()][n];
		for (int i = 0; i < classTrainingDataVector.size(); i++) {
			classTrainingData[i] = (double[]) classTrainingDataVector.get(i);
		}
		getMultiGaussianDistribution();
	}

	public double[][] getTrainingData() {
		return classTrainingData;
	}

	public void setMeanVector(double[] meanVector) {
		this.meanVector = meanVector;
	}

	public double[] getMeanVector() {
		return meanVector;
	}

	public void setCovarianceMatrix(double[][] covarianceM) {
		covarianceMatrix = covarianceM;
	}

	public double[][] getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public void setIsUnbias(boolean isUnbias) {
		this.isUnbias = isUnbias;
	}

	public boolean getIsUnbias() {
		return isUnbias;
	}

	private void getMultiGaussianDistribution() {
		// p(X|wi)~N(Ui, Ei) for one class
		len = classTrainingData.length; // observation numbers
		int covDivident;
		if (isUnbias == true) {
			covDivident = len - 1;
		} else {
			covDivident = len;
		}

		meanVector = new double[n]; // n is the number of variables
		covarianceMatrix = new double[n][n];
		// mean
		for (int j = 0; j < len; j++) {
			meanVector = StatisticsVectors.plus(meanVector,
					classTrainingData[j]);
		}
		meanVector = StatisticsVectors.divide(meanVector, len);
		// covariance matrix
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				covarianceMatrix[i][j] = 0;
				for (int l = 0; l < len; l++) {
					covarianceMatrix[i][j] += (classTrainingData[l][i] - meanVector[i])
							* (classTrainingData[l][j] - meanVector[j]);
				}
				covarianceMatrix[i][j] /= covDivident;
				// System.out.print(covarianceMatrix[i][j]);
				// System.out.print(" ");
			}

		}
		try {
			inverseCovMatrix = StatisticsVectors.Inverse(covarianceMatrix);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		determinant = StatisticsVectors.Determinant(covarianceMatrix);
		sqrtDeterminant = Math.sqrt(determinant);
	}

	public double getPDF(double[] X) {
		// p(X|wi) = 1 / ((2PI)power(n/2) * determinant(Ei) power(1/2) * e
		// power(-1/2 * (X-Ui)power(T) * Ei power(-1) * (X - Ui))))
		double pdf = 0;
		int n = X.length;
		double[] XMinusMean = new double[n];
		XMinusMean = StatisticsVectors.minus(X, meanVector);
		double expValue = 0;
		double[] expVectorTmp = new double[n];

		try {
			expVectorTmp = StatisticsVectors.MultiplyMatrix(XMinusMean,
					inverseCovMatrix);
			expValue = -StatisticsVectors.multiply(expVectorTmp, XMinusMean) / 2;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		pdf = 1 / (Math.pow(2 * Math.PI, (double) n / 2) * sqrtDeterminant)
				* Math.exp(expValue);

		return pdf;
	}

}