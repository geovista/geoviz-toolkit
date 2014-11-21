/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DescriptiveStatistics {

	private static final Logger logger = Logger
			.getLogger(DescriptiveStatistics.class.getName());

	/**
	 * Finds whether the array has any elements which are not Double.NaN
	 */
	public static boolean hasNonNullValues(double[] doubleArray) {
		for (int i = 0; i < doubleArray.length; i++) {
			if (!(Double.isNaN(doubleArray[i]))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Finds whether the array has any elements which are not the specified NaN
	 * value.
	 */
	public static boolean hasNonNullValues(int[] intArray, int NaNValue) {
		for (int element : intArray) {
			if (element != NaNValue) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Finds whether the array has any elements which are not Integer.MIN_VALUE
	 */
	public static boolean hasNonNullValues(int[] intArray) {
		for (int element : intArray) {
			if (element != Integer.MIN_VALUE) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Finds whether the array has any elements which are Double.NaN
	 */
	public static boolean hasNullValues(double[] doubleArray) {
		for (double element : doubleArray) {
			if (Double.isNaN(element)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Finds whether the array has any elements which are Integer.MIN_VALUE
	 */
	public static boolean hasNullValues(int[] intArray) {
		for (int element : intArray) {
			if (element == Integer.MIN_VALUE) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Finds whether the array has any elements which are null
	 */
	public static boolean hasNullValues(Object[] ObjectArray) {
		for (Object element : ObjectArray) {
			if (element == null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the smallest value of a given double array.
	 */
	public static double min(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.min";
			logger.fine(s);
			return Double.NaN;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Double.NaN;
		}

		double min = Double.MAX_VALUE;
		double temp = 0;

		for (double element : doubleArray) {
			temp = element;

			if (temp < min) {
				min = temp;
			} // end if
		} // end for

		return min;
	} // end Min

	/**
	 * Returns the smallest value of a given double array.
	 */
	public static double minIgnoreNaN(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {

			String s = "Empty array passed to DescriptiveStatistics.min";
			logger.fine(s);

			return Double.NaN;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Double.NaN;
		}

		double min = Double.MAX_VALUE;
		double temp = 0;

		for (double element : doubleArray) {
			temp = element;

			if (!Double.isNaN(temp)) {
				if (temp < min) {
					min = temp;
				} // end if lower
			} // end if not NaN
		} // end for

		return min;
	} // end Min

	/**
	 * Returns the smallest value of a given int array, exluding the given NaN
	 * encoded values.
	 */
	public static int minIgnoreNaN(int[] intArray, int NaNValue) {
		// first make sure that array is not empty
		if (intArray.length < 1) {

			String s = "Empty array passed to DescriptiveStatistics.min";
			logger.fine(s);

			return NaNValue;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(intArray) == false) {
			return NaNValue;
		}

		int min = Integer.MAX_VALUE;
		int temp = 0;

		// we need to ignore the specified NaN
		for (int element : intArray) {

			temp = element;

			if ((temp < min) && (temp != NaNValue)) {
				min = temp;
			} // end if
		} // end for

		return min;
	} // end Min

	/**
	 * Returns the smallest value of a given int array.
	 */
	public static int min(int[] intArray) {
		// first make sure that array is not empty
		if (intArray.length < 1) {

			String s = "Empty array passed to DescriptiveStatistics.min";
			logger.fine(s);

			return Integer.MIN_VALUE;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(intArray) == false) {
			return Integer.MIN_VALUE;
		}

		int min = Integer.MAX_VALUE;
		int temp = 0;

		// we need to ignore Integer.MIN_VALUE because that is our null
		for (int element : intArray) {
			temp = element;

			if ((temp < min) && (temp != Integer.MIN_VALUE)) {
				min = temp;
			} // end if
		} // end for

		return min;
	} // end Min

	/**
	 * Returns the largest value of a given double array.
	 */
	public static double max(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.max";
			logger.fine(s);
			return Double.NaN;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Double.NaN;
		}

		double max = Double.MIN_VALUE;
		double temp = 0;

		for (double element : doubleArray) {
			temp = element;

			if (temp > max) {
				max = temp;
			} // end if
		} // end for

		return max;
	} // end max

	/**
	 * Returns the largest value of a given double array.
	 */
	public static double maxIgnoreNaN(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.max";
			logger.fine(s);
			return Double.NaN;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Double.NaN;
		}

		double max = Double.MIN_VALUE;
		double temp = 0;

		for (double element : doubleArray) {
			temp = element;

			if (!Double.isNaN(temp)) {
				if (temp > max) {
					max = temp;
				} // end if
			} // end if not NaN
		} // end for

		return max;
	} // end max

	/**
	 * Returns the largest value of a given int array.
	 */
	public static int max(int[] intArray) {

		// first make sure that array is not empty
		if (intArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.max";
			logger.fine(s);
			return Integer.MIN_VALUE;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(intArray) == false) {
			return Integer.MIN_VALUE;
		}

		int max = Integer.MIN_VALUE;
		int temp = 0;

		for (int element : intArray) {
			temp = element;

			if (temp > max) {
				max = temp;
			} // end if
		} // end for

		return max;
	} // end max

	/**
	 * Returns the largest value of a given int array.
	 */
	public static int maxIgnoreNaN(int[] intArray, int NaNValue) {

		// first make sure that array is not empty
		if (intArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.max";
			logger.fine(s);
			return Integer.MIN_VALUE;
		} // end if

		if (DescriptiveStatistics.hasNonNullValues(intArray, NaNValue) == false) {
			return NaNValue;
		}

		int max = Integer.MIN_VALUE;
		int temp = 0;

		for (int element : intArray) {
			if (element != NaNValue) {

				temp = element;

				if (temp > max && temp != NaNValue) {
					max = temp;
				} // end if new one larger
			}// end if not NaN
		} // end for

		return max;
	} // end max

	/**
	 * Returns the difference between the largest and smallest value in a given
	 * double array.
	 */
	public static double range(double[] doubleArray) {
		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Integer.MIN_VALUE;
		}

		double min;
		double max;
		double range;
		min = DescriptiveStatistics.min(doubleArray);
		max = DescriptiveStatistics.max(doubleArray);
		range = max - min;

		return range;
	} // end Range

	/**
	 * Returns the difference between the largest and smallest value in a given
	 * double array.
	 */
	public static double rangeIgnoreNaN(double[] doubleArray) {
		if (DescriptiveStatistics.hasNonNullValues(doubleArray) == false) {
			return Integer.MIN_VALUE;
		}

		double min;
		double max;
		double range;
		min = DescriptiveStatistics.minIgnoreNaN(doubleArray);
		max = DescriptiveStatistics.maxIgnoreNaN(doubleArray);
		range = max - min;

		return range;
	} // end Range

	/**
	 * Returns the difference between the largest and smallest value in a given
	 * int array.
	 */
	public static int rangeIgnoreNaN(int[] intArray, int NaNValue) {
		if (DescriptiveStatistics.hasNonNullValues(intArray) == false) {
			return NaNValue;
		}

		int min;
		int max;
		int range;
		min = DescriptiveStatistics.minIgnoreNaN(intArray, NaNValue);
		max = DescriptiveStatistics.maxIgnoreNaN(intArray, NaNValue);
		range = max - min;

		return range;
	} // end Range

	/**
	 * Returns the difference between the largest and smallest value in a given
	 * int array.
	 */
	public static int range(int[] intArray) {
		if (DescriptiveStatistics.hasNonNullValues(intArray) == false) {
			return Integer.MIN_VALUE;
		}

		int min;
		int max;
		int range;
		min = DescriptiveStatistics.min(intArray);
		max = DescriptiveStatistics.max(intArray);
		range = max - min;

		return range;
	} // end Range

	/**
	 * Returns the sum of a given array.
	 * 
	 */
	public static double sum(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.sum";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double sum = 0;
		double temp = 0;
		double n = doubleArray.length;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			sum += temp;
		} // end for

		return sum;
	} // end mean

	/**
	 * Returns the arithmetic mean for a given array.
	 * 
	 */
	public static double mean(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.mean";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double mean = 0;
		double temp = 0;
		double n = doubleArray.length;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			mean += temp;
		} // end for

		mean = mean / n;

		return mean;
	} // end mean

	/**
	 * Returns the arithmetic mean for a given array.
	 * 
	 */
	public static double meanIgnoreNaN(double[] doubleArray) {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.mean";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double mean = 0;
		double temp = 0;
		double n = doubleArray.length;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];

			if (Double.isNaN(temp)) {
				n = n - 1; // it's NaN, so decriment the count
			} else {
				mean += temp;
			}
		} // end for

		mean = mean / n;

		return mean;
	} // end mean

	/**
	 * Returns the arithmetic mean for a given array. This method minimizes
	 * rounding error, but is not as fast. Suggested by Masahiro
	 * Takatsuka/2000-Mar-15
	 */
	public static double fineMean(double[] doubleArray)
			throws IllegalArgumentException {
		// first make sure that array is not empty
		if (doubleArray.length < 1) {
			String s = "Empty array passed to DescriptiveStatistics.fineMean";
			IllegalArgumentException gve = new IllegalArgumentException(s);
			throw gve;
		} // end if

		double fineMean = 0;
		double temp = 0;
		double n = doubleArray.length;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i] - fineMean;
			fineMean += (temp / (i + 1));
		} // end for

		return fineMean;
	} // end fineMean

	/**
	 * Returns the variance for a given array.
	 * 
	 */
	public static double variance(double[] doubleArray, boolean sample) {
		// first make sure that array is not empty
		if (doubleArray.length < 2) {
			String s = "Array with less than two elements passed to DescriptiveStatistics.variance";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double temp = 0;
		double variance = 0;
		double sum = 0;
		double n = doubleArray.length;
		double mean = DescriptiveStatistics.mean(doubleArray);

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp -= mean;
			temp = temp * temp;
			sum += temp;
		} // end for

		if (sample == true) {
			variance = sum / (n - 1);
		} else {
			variance = sum / n;
		} // end if

		return variance;
	} // end variance

	/**
	 * Returns the variance for a given array. This method minimizes rounding
	 * error, but is not as fast. Suggested by Masahiro Takatsuka/2000-Mar-15
	 */
	public static double fineVariance(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 2) {
			String s = "Array with less than two elements passed to DescriptiveStatistics.fineVariance";
			IllegalArgumentException gve = new IllegalArgumentException(s);
			throw gve;
		} // end if

		double temp = 0;
		double fineVariance = 0;
		double sum = 0;
		double n = doubleArray.length;
		double mean = DescriptiveStatistics.mean(doubleArray);

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i] - mean;
			mean += (temp / (i + 1));
			sum += ((i * temp * temp) / (i + 1));
		} // end for

		if (sample == true) {
			fineVariance = sum / (n - 1);
		} else {
			fineVariance = sum / n;
		} // end if

		return fineVariance;
	} // end fineVariance

	/**
	 * Returns the standard deviation for a given array.
	 * 
	 */
	public static double stdDev(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 2) {
			String s = "Array with less than two elements passed to DescriptiveStatistics.stdDev";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double temp = 0;
		double stdDev = 0;

		temp = DescriptiveStatistics.variance(doubleArray, sample);
		stdDev = Math.sqrt(temp);

		return stdDev;
	} // end stdDev

	/**
	 * Returns the standard deviation for a given array. This method minimizes
	 * rounding error, but is not as fast.
	 */
	public static double fineStdDev(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 1) {
			String s = "Array with less than two elements passed to DescriptiveStatistics.fineStdDev";
			IllegalArgumentException gve = new IllegalArgumentException(s);
			throw gve;
		} // end if

		double temp = 0;
		double fineStdDev = 0;

		temp = DescriptiveStatistics.fineVariance(doubleArray, sample);
		fineStdDev = Math.sqrt(temp);

		return fineStdDev;
	} // end fineStdDev

	/**
	 * Returns the skewness for a given array. Skewness indicates if data are
	 * slanted one way or the other around the mean. A negative skewness
	 * indicates that there are more values greater than the mean, and the
	 * distribution is skewed to the right. A positive skewness indicates that
	 * there are more values less than the mean and the distribution is skewed
	 * to the left.
	 */
	public static double skewness(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 3) {
			String s = "Array with less than three elements passed to DescriptiveStatistics.skewness";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double temp = 0;
		double sum = 0;
		double n = doubleArray.length;
		double stdDev = DescriptiveStatistics.stdDev(doubleArray, sample);
		double mean = DescriptiveStatistics.mean(doubleArray);

		// now make sure that standard deviation is not zero
		if (stdDev == 0) {
			String s = "Array with standard devation of zero passed to DescriptiveStatistics.skewness";
			logger.fine(s);
			return Double.NaN;
		} // end if

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp -= mean;
			sum += (temp * temp * temp);
		} // end for

		double skewness = sum / (n * stdDev * stdDev * stdDev);

		return skewness;
	} // end skewness

	/**
	 * Returns the kurtosis for a given array. Kurtosis is the "peakyness" of
	 * the data. A kurtosis of 3 indicates Gaussian distribution. A kurtosis of
	 * less than 3 is a flatter distribution; a kurtosis of greater than 3 is a
	 * peaky disribution.
	 * 
	 */
	public static double populationKurtosis(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 4) {
			String s = "Array with less than four elements passed to DescriptiveStatistics.kurtosis";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double temp = 0;
		double sum = 0;
		double n = doubleArray.length;
		double stdDev = DescriptiveStatistics.stdDev(doubleArray, sample);
		double mean = DescriptiveStatistics.mean(doubleArray);

		// now make sure that standard deviation is not zero
		if (stdDev == 0) {
			String s = "Array with standard devation of zero passed to DescriptiveStatistics.kurtosis";
			logger.fine(s);
			return Double.NaN;
		} // end if

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp -= mean;
			sum += (temp * temp * temp * temp);
		} // end for

		double kurtosis = sum / ((n - 1) * stdDev * stdDev * stdDev * stdDev);

		return kurtosis - 3;
	} // end kurtosis

	/**
	 * Returns the kurtosis for a given array. Kurtosis is the "peakyness" of
	 * the data. A kurtosis of 3 indicates Gaussian distribution. A kurtosis of
	 * less than 3 is a flatter distribution; a kurtosis of greater than 3 is a
	 * peaky disribution.
	 * 
	 * kurtosis = { [n(n+1) / (n -1)(n - 2)(n-3)] sum[(x_i - mean)^4] / std^4 }
	 * - [3(n-1)^2 / (n-2)(n-3)]
	 * 
	 */
	public static double kurtosis(double[] doubleArray, boolean sample) {
		// first make sure that array has enough elements
		if (doubleArray.length < 4) {
			String s = "Array with less than four elements passed to DescriptiveStatistics.kurtosis";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double sum = 0;
		double n = doubleArray.length;
		double stdDev = DescriptiveStatistics.stdDev(doubleArray, sample);
		double mean = DescriptiveStatistics.mean(doubleArray);

		// now make sure that standard deviation is not zero
		if (stdDev == 0) {
			String s = "Array with standard devation of zero passed to DescriptiveStatistics.kurtosis";
			logger.fine(s);
			return Double.NaN;
		} // end if
		double kurtosis = 0;
		double divisor = 0;
		double dividend = 0;
		dividend = n * (n + 1);
		divisor = (n - 1) * (n - 2) * (n - 3);
		kurtosis = dividend / divisor;

		for (double element : doubleArray) {
			sum = sum + Math.pow((element - mean), 4);
		}
		kurtosis = (kurtosis * sum) / (Math.pow(stdDev, 4));

		double tempDividend = 3 * Math.pow((n - 1), 2);
		double tempDivisor = (n - 2) * (n - 3);

		kurtosis = kurtosis - (tempDividend / tempDivisor);

		return kurtosis;
	} // end kurtosis

	public static void main(String[] args) {
		double[] vals = { 3, 2, 3, 3, 3, 3, 3, 3, 3, 2 };
		double kurtosis = DescriptiveStatistics.kurtosis(vals, true);
		logger.info("kurtosis = " + kurtosis);
	}

	/**
	 * Returns the covariance for a given array.
	 * 
	 */
	public static double covariance(double[] doubleArray,
			double[] doubleArray2, boolean sample) {
		// first make sure the arrays have the same number of elements
		if (doubleArray.length != doubleArray2.length) {
			String s = "Two arrays with unequal numbers of elements passed to DescriptiveStatistics.covariance";
			logger.fine(s);
			return Double.NaN;
		} // end if

		// now make sure that arrays have enough elements
		if (doubleArray.length < 1) {
			String s = "Array with less than one element passed to DescriptiveStatistics.covariance";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double mean = DescriptiveStatistics.mean(doubleArray);
		double mean2 = DescriptiveStatistics.mean(doubleArray2);
		double sum = 0;
		double temp = 0;
		double temp2 = 0;
		double n = doubleArray.length;
		double covariance;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp2 = doubleArray2[i];
			temp -= mean;
			temp2 -= mean2;
			sum += (temp * temp2);
		} // end for

		if (sample == true) {
			covariance = sum / (n - 1);
		} else {
			covariance = sum / n;
		} // end if

		return covariance;
	} // end covariance

	/**
	 * Returns the covariance for a given arrays, ignoring NaN values.
	 * 
	 */
	public static double covariancefIgnoreNaN(double[] doubleArray,
			double[] doubleArray2, boolean sample) {
		// first make sure the arrays have the same number of elements
		if (doubleArray.length != doubleArray2.length) {
			String s = "Two arrays with unequal numbers of elements passed to DescriptiveStatistics.covariance";
			logger.fine(s);
			return Double.NaN;
		} // end if

		// now make sure that arrays have enough elements
		if (doubleArray.length < 1) {
			String s = "Array with less than one element passed to DescriptiveStatistics.covariance";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double mean = DescriptiveStatistics.meanIgnoreNaN(doubleArray);
		double mean2 = DescriptiveStatistics.meanIgnoreNaN(doubleArray2);
		double sum = 0;
		double temp = 0;
		double temp2 = 0;
		double n = doubleArray.length;
		double covariance;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp2 = doubleArray2[i];

			if (Double.isNaN(temp) || Double.isNaN(temp2)) {
				n--;
			} else {
				temp -= mean;
				temp2 -= mean2;
				sum += (temp * temp2);
			}
		} // end for

		if (sample == true) {
			covariance = sum / (n - 1);
		} else {
			covariance = sum / n;
		} // end if

		return covariance;
	} // end covariance

	/**
	 * Returns two new arrays, without NaN values, eliminating entries in both
	 * arrays for those places that have a NaN in either. If one array is
	 * longer, the longer one is truncated to be the same length as the the
	 * shorter one, minus NaN entries.
	 * 
	 */
	private static Object[] stripNaNs(double[] arrayOne, double[] arrayTwo) {
		// first count the number of non-NaN places
		int numNotNaN = 0;
		int shorterArrayLen = arrayOne.length;
		if (arrayTwo.length < shorterArrayLen) {
			shorterArrayLen = arrayTwo.length;
		}
		for (int i = 0; i < shorterArrayLen; i++) {
			if (!Double.isNaN(arrayOne[i]) && !Double.isNaN(arrayTwo[i])) {
				numNotNaN++;
			}
		}

		Object[] returnThing = new Object[2];

		// if we find either 0 or all non-nulls, just return originals
		if (numNotNaN == 0 || numNotNaN == arrayOne.length) {
			returnThing[0] = arrayOne;
			returnThing[1] = arrayTwo;

			return returnThing;
		}

		// these next two lines might be better off cached somewhere
		double[] newOne = new double[numNotNaN];
		double[] newTwo = new double[numNotNaN];
		int j = 0;

		for (int i = 0; i < newOne.length; i++) {
			if (!Double.isNaN(arrayOne[j]) && !Double.isNaN(arrayTwo[j])) {
				newOne[i] = arrayOne[j];
				newTwo[i] = arrayTwo[j];
				j++;
			} else { // skip it
				j++;
			}
		}

		returnThing[0] = newOne;
		returnThing[1] = newTwo;

		return returnThing;
	}

	/**
	 * Returns Pearson's correlation coefficient for a given array.
	 * 
	 */
	public static double correlationCoefficientIgnoreNaN(double[] doubleArray,
			double[] doubleArray2, boolean sample) {
		Object[] obj = DescriptiveStatistics.stripNaNs(doubleArray,
				doubleArray2);
		double[] newOne = (double[]) obj[0];
		double[] newTwo = (double[]) obj[1];

		return DescriptiveStatistics.correlationCoefficient(newOne, newTwo,
				sample);
	}

	public static double[] getRegressionResiduals(double[] dataX, double[] dataY) {
		double[] regressionElement = new double[2];
		double sumX, sumY, sumX2, sumXY, slope, intercept;
		int sampleSize = 0;
		sumX = sumY = sumX2 = sumXY = slope = intercept = 0;

		regressionElement[0] = 0;
		regressionElement[1] = 0;

		for (int i = 0; i < dataX.length; i++) {
			sumX = sumX + dataX[i];
			sumY = sumY + dataY[i];
			sumX2 = sumX2 + (dataX[i] * dataX[i]);
			sumXY = sumXY + (dataX[i] * dataY[i]);
			sampleSize++;
		}

		double denominator = 0;
		if (sampleSize > 0) {
			denominator = sumX2 - (sumX * sumX) / sampleSize;
		}

		if (Math.abs(denominator) > 1.0e-12) {
			slope = (sumXY - sumX * sumY / sampleSize) / denominator;
			intercept = (sumY - slope * sumX) / sampleSize;
		}

		regressionElement[0] = slope;
		regressionElement[1] = intercept;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Slope is " + slope + "  Intercept is " + intercept);
		}
		return regressionElement;
	}

	/**
	 * Returns Pearson's correlation coefficient for a given array.
	 * 
	 */
	public static double correlationCoefficient(double[] doubleArray,
			double[] doubleArray2, boolean sample) {
		// first make sure the arrays have the same number of elements
		if (doubleArray.length != doubleArray2.length) {
			String s = "Two arrays with unequal numbers of elements passed to DescriptiveStatistics.correlationCoefficient";
			logger.fine(s);
			return Double.NaN;
		} // end if

		// now make sure that arrays have enough elements
		if (doubleArray.length < 1) {
			String s = "Array with less than one element passed to DescriptiveStatistics.correlationCoefficient";
			logger.fine(s);
			return Double.NaN;
		} // end if

		double covariance = DescriptiveStatistics.covariance(doubleArray,
				doubleArray2, sample);
		double mean = DescriptiveStatistics.mean(doubleArray);
		double mean2 = DescriptiveStatistics.mean(doubleArray2);
		double sum = 0;
		double sum2 = 0;
		double temp = 0;
		double temp2 = 0;
		double n = doubleArray.length;
		double correlationCoefficient;

		for (int i = 0; i < n; i++) {
			temp = doubleArray[i];
			temp -= mean;
			sum += (temp * temp);
		} // end for

		if (sample == true) {
			sum = Math.sqrt(sum / (n - 1));
		} else {
			sum = Math.sqrt(sum / n);
		} // end if

		for (int i = 0; i < n; i++) {
			temp2 = doubleArray2[i];
			temp2 -= mean2;
			sum2 += (temp2 * temp2);
		} // end for

		if (sample == true) {
			sum2 = Math.sqrt(sum2 / (n - 1));
		} else {
			sum2 = Math.sqrt(sum2 / n);
		} // end if

		correlationCoefficient = covariance / (sum * sum2);

		return correlationCoefficient;
	} // end correlationCoefficient

	/**
	 * @return
	 */
	public static double[] calculateZScores(double[] data) {
		double[] xData;

		xData = new double[data.length];
		System.arraycopy(data, 0, xData, 0, data.length);

		double meanX = DescriptiveStatistics.fineMean(xData);
		double stdDevX = DescriptiveStatistics.fineStdDev(xData, false);
		double[] fullXData = new double[xData.length];
		for (int i = 0; i < xData.length; i++) {
			fullXData[i] = (xData[i] - meanX) / stdDevX;
		}
		return fullXData;
	}

	public static double percentAbove(double[] values, double value) {

		Arrays.sort(values);

		return percentAbovePresort(values, value);
	}

	public static double percentAbovePresort(double[] values, double value) {

		int place = Arrays.binarySearch(values, value);
		double along = (double) (place + 1) / (double) values.length;
		return 1 - (Math.abs(along));
	}

	public static void sort(double[][] dataIn, int whichColumn) {
		double[] dataSorted = new double[dataIn.length];

		for (int i = 0; i < dataSorted.length; i++) {
			dataSorted[i] = dataIn[i][whichColumn];
		} // next i

		Arrays.sort(dataSorted);

		// make a run length list
		// double[] uniqueVals = new double[dataSorted.length];
		int[] numOccur = new int[dataSorted.length];
		int counter = 0;

		// uniqueVals[0] = dataSorted[0];
		numOccur[0] = 1;

		int totalUnique = 1;

		for (int i = 1; i < dataSorted.length; i++) {
			if (dataSorted[i] != dataSorted[i - 1]) { // hit a new one.
				counter++;
				dataSorted[counter] = dataSorted[i];
				numOccur[counter] = 1;
				totalUnique++;
			} else { // it's a repeat
				numOccur[counter]++;
			} // end if
		} // next i

		double[] uniqueValsSorted = new double[totalUnique];
		System.arraycopy(dataSorted, 0, uniqueValsSorted, 0, totalUnique);

		// now walk through original data to find the order
		// we need to keep track of how many "hits" we've had
		double[] tempTuple = new double[dataIn[0].length]; // holding copies
		// while(madeSwitch) {
		int[] hitCount = new int[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			// now find each data value
			double val = dataIn[i][whichColumn];
			int uniquePos = Arrays.binarySearch(uniqueValsSorted, val);
			int pos = 0;

			for (int j = 0; j < uniquePos; j++) {
				pos = pos + numOccur[j];
			}

			pos = pos + hitCount[uniquePos]; // if we are not the first of
			// that val

			// pos is where this data tuple is "supposed" to be. Is it there
			// already?
			// or is the "right" value there already?
			double targetVal = dataIn[pos][whichColumn];

			if (pos == i) {
				// direct hit!
				hitCount[uniquePos]++; // keep track of how many of that val we
				// passed in the correct position as i
			} else if (val == targetVal) {
				// put this in the right place
				while (val == targetVal) {
					pos++;
					targetVal = dataIn[pos][whichColumn];
				}

				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--; // try again???

				// now put this in the "pos"

				// if not... switch 'em XXX search on this string and refactor
			} else {
				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--; // try again???
			} // end if
		} // next i

	}
}
