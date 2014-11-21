/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.classification;

import java.util.Vector;

import geovista.common.data.DataSetForApps;
import geovista.common.data.StatisticsVectors;

public class ClusterMixtureModels {

	private static double THRESHOLD = 0.001;
	private Object[] dataObject;
	private String[] attributesDisplay;
	private double[][] dataArray;
	private int[] selectedAttIdx;
	private ClassifierKMeans kMeans;
	private int[] kMeansClusters;
	private Vector[] dataInClasses;
	private int numClusters;
	private double[] priors;
	private MultiGaussian[] multiGaussian;
	private Vector clusterMeanVector;
	private Vector clusterCovVector;
	private double[][] posterior;
	private int[] clusterResults;

	// private ColorSymbolizerForClassification clusterToColors;

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

	public void setDataSet(DataSetForApps data) {
		// remove string data
		DataSetForApps dataObjTransfer = data;
		dataObject = dataObjTransfer.getDataSetNumericAndSpatial();// XXX
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
		init();
	}

	public void setNumberOfCluster(int num) {
		numClusters = num;
	}

	public int[] getClusterResults() {
		return clusterResults;
	}

	/*
	 * public Color[] getClusterColors() { clusterToColors = new
	 * ColorSymbolizerForClassification();
	 * clusterToColors.setClassification(this.clusterResults);
	 * clusterToColors.setNumClasses(this.numClusters); this.clusterColors =
	 * clusterToColors.getSymbolize(); return this.clusterColors; }
	 */

	private void init() {
		clusterResults = new int[dataArray.length];
		kMeans = new ClassifierKMeans();
		kMeans.setClusterNumber(numClusters);
		kMeans.setAttributesDisplay(attributesDisplay);
		kMeans.setDataArray(dataArray);
		kMeansClusters = kMeans.getKMeansClusters();
		dataInClasses = new Vector[numClusters];
		priors = new double[numClusters];
		multiGaussian = new MultiGaussian[numClusters];
		clusterMeanVector = new Vector();
		clusterCovVector = new Vector();
		clusterCovVector.setSize(numClusters);
		posterior = new double[dataArray.length][numClusters];

		for (int i = 0; i < numClusters; i++) {
			dataInClasses[i] = new Vector();
		}
		// divide data vectors into individule classes.
		for (int i = 0; i < dataArray.length; i++) {
			for (int j = 0; j < numClusters; j++) {
				if (kMeansClusters[i] == j) {
					dataInClasses[j].add(dataArray[i]);
				}
			}
		}
		// set up prior probability ak
		for (int i = 0; i < numClusters; i++) {
			dataInClasses[i].trimToSize();
			priors[i] = (double) dataInClasses[i].size()
					/ (double) dataArray.length;
		}
		for (int i = 0; i < numClusters; i++) {
			multiGaussian[i] = new MultiGaussian();
			multiGaussian[i].setTrainingData(dataInClasses[i]);
		}

		double sumPosterior;
		double likelihoodOld = 0;
		double likelihood = 0;
		double stop = 1000;
		double[] pdfForOneObvs = new double[numClusters];

		// E-step: compute the posterior probabilities for all i = 1, ..., n, k
		// = 1, ...,K
		for (int i = 0; i < dataArray.length; i++) {
			sumPosterior = 0.0;
			for (int k = 0; k < numClusters; k++) {
				pdfForOneObvs[k] = multiGaussian[k].getPDF(dataArray[i]);
				sumPosterior += priors[k] * pdfForOneObvs[k];
			}
			for (int k = 0; k < numClusters; k++) {
				posterior[i][k] = priors[k] * pdfForOneObvs[k] / sumPosterior;
			}
			likelihoodOld += Math.log(sumPosterior);
		}

		while (stop > ClusterMixtureModels.THRESHOLD) {
			// M-step
			double sumPik;
			for (int k = 0; k < numClusters; k++) {
				double[] mean = new double[dataArray[0].length];
				double[][] covMatrix = new double[dataArray[0].length][dataArray[0].length];
				sumPik = 0;
				for (int i = 0; i < dataArray.length; i++) {
					sumPik += posterior[i][k];
					mean = StatisticsVectors.plus(mean, StatisticsVectors
							.multiply(posterior[i][k], dataArray[i]));
				}
				// ak(P=1)
				priors[k] = sumPik / dataArray.length;
				mean = StatisticsVectors.divide(mean, sumPik);
				// uk(p+1)
				clusterMeanVector.add(k, mean.clone());
				// Ek(p+1)
				double[] xminusmean = new double[mean.length];
				for (int i = 0; i < dataArray.length; i++) {
					xminusmean = StatisticsVectors.minus(dataArray[i], mean);
					try {
						covMatrix = StatisticsVectors.plus(covMatrix,
								StatisticsVectors.multiply(posterior[i][k],
										StatisticsVectors.MultiplyMatrix(
												xminusmean, xminusmean)));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				covMatrix = StatisticsVectors.divide(covMatrix, sumPik);
				clusterCovVector.add(k, covMatrix.clone());
			}
			clusterCovVector.trimToSize();
			// objective function
			for (int i = 0; i < numClusters; i++) {
				multiGaussian[i] = new MultiGaussian();
				multiGaussian[i].setMeanVector((double[]) clusterMeanVector
						.get(i));
				multiGaussian[i]
						.setCovarianceMatrix((double[][]) clusterCovVector
								.get(i));
			}

			for (int n = 0; n < dataArray.length; n++) {
				sumPosterior = 0.0;
				for (int k = 0; k < numClusters; k++) {
					pdfForOneObvs[k] = multiGaussian[k].getPDF(dataArray[n]);
					sumPosterior += priors[k] * pdfForOneObvs[k];
				}
				likelihood += Math.log(sumPosterior);
				for (int k = 0; k < numClusters; k++) {
					posterior[n][k] = priors[k] * pdfForOneObvs[k]
							/ sumPosterior;
				}
			}
			stop = Math.abs((likelihood - likelihoodOld) / likelihood);
			likelihoodOld = likelihood;
		}

		// classify based on posterior
		int tmpClass = 0;
		double[] pdfs = new double[numClusters];
		for (int i = 0; i < dataArray.length; i++) {
			for (int j = 0; j < numClusters; j++) {
				pdfs[j] = posterior[i][j];
			}
			// find the biggest pdf using density function of each class
			tmpClass = 0;
			for (int j = 1; j < numClusters; j++) {
				if (pdfs[j] > pdfs[tmpClass]) {
					tmpClass = j;
				}
			}
			// assign the class information to each observation.
			// this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa
			// data.
			clusterResults[i] = tmpClass;
		}
	}

}