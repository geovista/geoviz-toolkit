/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.data;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsVectors {
	protected final static Logger logger = Logger
			.getLogger(StatisticsVectors.class.getName());
	private static int iDF = 0;

	public StatisticsVectors() {
	}

	public static double getDistance(double[] a, double[] b) {
		return distance(a, b);
	}

	public static double[] plus(double[] a, double[] b) {
		double[] c = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i] + b[i];
		}
		return c;
	}

	public static double[][] plus(double[][] a, double[][] b) {
		double[][] c = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[i][j] = a[i][j] + b[i][j];
			}
		}
		return c;
	}

	public static double[] minus(double[] a, double[] b) {
		int len;
		len = (a.length <= b.length) ? a.length : b.length;
		double[] c = new double[len];
		for (int i = 0; i < len; i++) {
			c[i] = a[i] - b[i];
		}
		return c;
	}

	public static double[] divide(double[] a, double b) {
		double[] c = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i] / b;
		}
		return c;
	}

	public static double[][] divide(double[][] a, double b) {
		double[][] c = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[i][j] = a[i][j] / b;
			}
		}
		return c;
	}

	public static void setvalue(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) {
			a[i] = b[i];
		}
	}

	public static double multiply(double[] a, double[] b) {
		double c = 0.0;
		for (int i = 0; i < a.length; i++) {
			c += a[i] * b[i];
		}
		return c;
	}

	public static double[] multiply(double a, double[] b) {
		double[] c = new double[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i] = a * b[i];
		}
		return c;
	}

	public static double[][] multiply(double a, double[][] b) {
		double[][] c = new double[b.length][b[0].length];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				c[i][j] = a * b[i][j];
			}
		}
		return c;
	}

	public static double[][] MultiplyMatrix(double[] a, double[] b)
			throws Exception {
		int tmsA = a.length;
		int tmsB = b.length;
		if (tmsA != tmsB) {
			System.err.print("Matrix Size Mismatch");
		}

		double matrix[][] = new double[tmsA][tmsA];

		for (int i = 0; i < tmsA; i++) {
			for (int j = 0; j < tmsA; j++) {
				matrix[i][j] = a[i] * b[j];
			}
		}
		return matrix;
	}

	public static double[][] MultiplyMatrix(double[][] a, double[][] b)
			throws Exception {

		int tms = a.length;
		int tmsB = b.length;
		if (tms != tmsB) {
			System.err.print("Matrix Size Mismatch");
		}

		double matrix[][] = new double[tms][tms];

		for (int i = 0; i < tms; i++) {
			for (int j = 0; j < tms; j++) {
				matrix[i][j] = 0;
			}
		}

		for (int i = 0; i < tms; i++) {
			for (int j = 0; j < tms; j++) {
				for (int p = 0; p < tms; p++) {
					matrix[i][j] += a[i][p] * b[p][j];
				}
			}
		}
		return matrix;
	}

	public static double[] MultiplyMatrix(double[] a, double[][] b)
			throws Exception {
		int tms = a.length;
		int tmsB = b.length;
		if (tms != tmsB) {
			System.err.print("Matrix Size Mismatch");
		}

		double matrix[] = new double[tms];

		for (int i = 0; i < tms; i++) {
			matrix[i] = 0;
		}

		for (int j = 0; j < tms; j++) {
			for (int p = 0; p < tms; p++) {
				matrix[j] += a[p] * b[p][j];
			}
		}

		return matrix;
	}

	public static double[] MultiplyMatrix(double[][] a, double[] b)
			throws Exception {
		int tms = a.length;
		int tmsB = b.length;
		if (tms != tmsB) {
			System.err.print("Matrix Size Mismatch");
		}

		double matrix[] = new double[tms];

		for (int i = 0; i < tms; i++) {
			matrix[i] = 0;
		}

		for (int j = 0; j < tms; j++) {
			for (int p = 0; p < tms; p++) {
				matrix[j] += a[j][p] * b[p];
			}
		}

		return matrix;
	}

	public static double distance(double[] a, double[] b) {
		return Math.sqrt(distanceSquare(a, b));
	}

	public static double distanceSquare(double[] a, double[] b) // square of
	// distance
	{
		double c = 0.0;
		for (int i = 0; i < a.length; i++) {
			c += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return c;
	}

	public static double covariance(double[] a, double[] aMean, double[] b,
			double[] bMean) {
		double c;
		c = multiply(minus(a, aMean), minus(b, bMean));
		return c;
	}

	public static double sum(double[] a) {
		double c = 0;
		for (double element : a) {
			c = c + element;
		}
		return c;
	}

	public static double mean(double[] a) {
		double c;
		c = sum(a) / a.length;
		return c;
	}

	public static double variance(double[] a) {
		double m;
		double v = 0;
		m = mean(a);
		for (double element : a) {
			v = v + Math.pow(element - m, 2);
		}
		v = v / a.length;
		return v;
	}

	public static double[] meanVector(double[][] data) {
		if (data == null) {
			return null;
		}
		int n = data[0].length;
		int len = data.length;
		double[] meanVector = new double[n]; // n is the number of variables

		for (int j = 0; j < len; j++) {
			meanVector = StatisticsVectors.plus(meanVector, data[j]);
		}
		meanVector = StatisticsVectors.divide(meanVector, len);
		return meanVector;
	}

	public static double[][] covarianceMatrx(double[][] data) {
		if (data == null) {
			return null;
		}
		int n = data[0].length;// n is the number of variables
		int len = data.length;
		double[] meanVector = StatisticsVectors.meanVector(data);
		double[][] covarianceMatrix = new double[n][n];

		int covDivident;

		covDivident = len - 1;

		// covariance matrix
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				covarianceMatrix[i][j] = 0;
				for (int l = 0; l < len; l++) {
					covarianceMatrix[i][j] += (data[l][i] - meanVector[i])
							* (data[l][j] - meanVector[j]);
				}
				covarianceMatrix[i][j] /= covDivident;
			}
		}
		return covarianceMatrix;
	}

	public static double[][] Transpose(double[][] a) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Performing Transpose...");
		}
		int tms = a.length;
		double m[][] = new double[tms][tms];
		for (int i = 0; i < tms; i++) {
			for (int j = 0; j < tms; j++) {
				m[i][j] = a[j][i];
			}
		}
		return m;
	}

	public static double[][] Inverse(double[][] a) throws Exception {
		// Formula used to Calculate Inverse:
		// inv(A) = 1/det(A) * adj(A)
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Performing Inverse...");
		}
		int tms = a.length;
		double m[][] = new double[tms][tms];
		double mm[][] = Adjoint(a);
		double det = Determinant(a);
		double dd = 0;

		dd = 1 / det;

		for (int i = 0; i < tms; i++) {
			for (int j = 0; j < tms; j++) {
				m[i][j] = dd * mm[i][j];
			}
		}
		return m;
	}

	public static double[][] Adjoint(double[][] a) throws Exception {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Performing Adjoint...");
		}
		int tms = a.length;

		double m[][] = new double[tms][tms];

		int ii, jj, ia, ja;
		double det;

		for (int i = 0; i < tms; i++) {
			for (int j = 0; j < tms; j++) {
				ia = ja = 0;

				double ap[][] = new double[tms - 1][tms - 1];

				for (ii = 0; ii < tms; ii++) {
					for (jj = 0; jj < tms; jj++) {
						if ((ii != i) && (jj != j)) {
							ap[ia][ja] = a[ii][jj];
							ja++;
						}
					}
					if ((ii != i) && (jj != j)) {
						ia++;
					}
					ja = 0;
				}

				det = Determinant(ap);
				m[i][j] = Math.pow(-1, i + j) * det;
			}
		}

		m = Transpose(m);

		return m;
	}

	public static double[][] UpperTriangle(double[][] m) {
		logger.finest("Converting to Upper Triangle...");
		double f1 = 0;
		double temp = 0;
		int tms = m.length; // get This Matrix Size (could be smaller than
		// global)
		int v = 1;
		iDF = 1;

		for (int col = 0; col < tms - 1; col++) {
			for (int row = col + 1; row < tms; row++) {
				v = 1;

				outahere: while (m[col][col] == 0) // check if 0 in diagonal
				{ // if so switch until not
					if (col + v >= tms) // check if switched all rows
					{
						iDF = 0;
						break outahere;
					}
					for (int c = 0; c < tms; c++) {
						temp = m[col][c];
						m[col][c] = m[col + v][c]; // switch rows
						m[col + v][c] = temp;
					}
					v++; // count row switchs
					iDF = iDF * -1; // each switch changes determinant
					// factor
				}

				if (m[col][col] != 0) {
					logger.finest("tms = " + tms + "   col = " + col
							+ "   row = " + row);
				}

				try {
					f1 = (-1) * m[row][col] / m[col][col];
					for (int i = col; i < tms; i++) {
						m[row][i] = f1 * m[col][i] + m[row][i];
					}
				} catch (Exception e) {
					logger.finest("Still Here!!!");
					e.printStackTrace();
				}
			}
		}
		return m;
	}

	public static double Determinant(double[][] matrix) {
		int tms = matrix.length;

		double det = 1;

		matrix = UpperTriangle(matrix);

		for (int i = 0; i < tms; i++) {
			det = det * matrix[i][i];
		} // multiply down diagonal

		det = det * iDF; // adjust w/ determinant factor
		return det;
	}
}
