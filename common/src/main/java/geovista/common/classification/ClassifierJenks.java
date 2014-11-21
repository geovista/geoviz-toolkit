/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Authors: Hisaji ONO, James Macgill, Diansheng Guo, Frank Hardisty */

/**
 * Hisaji ONO hi_ono2001 at ybb.ne.jp Fri Mar 3 18:04:05 CET 2006 As you
 * might know, most of Jenks' optimization method depends on Fischer's
 * "EXACT OPTIMIZATION" METHOD in (Fisher, W. D., 1958, On grouping for
 * maximum homogeneity. Journal of the American Statistical Association, 53,
 * 789, 98)
 * 
 * This source code is available from following CMU's statlib site in
 * fortran code.
 * 
 * http://lib.stat.cmu.edu/cmlib/src/cluster/fish.f
 * 
 * Jenks' one is available in following paper media. Probably its in Basic.
 * 
 * Jenks, G. F. (1977). Optimal data classification for choropleth maps,
 * Occasional paper No. 2. Lawrence, Kansas: University of Kansas,
 * Department of Geography.
 * 
 * I've ported above code into
 * Geotools-lite(http://sourceforge.net/project/showfiles.php?group_id=4091)
 * in Java by modified by J. Macgill.
 * 
 * Most of Jenks'
 * code(uk.ac.leeds.ccg.geotools.classification.NaturalBreaks.java) as
 * follows.
 * 
 */
package geovista.common.classification;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassifierJenks implements DescribedClassifier {
	public static final String shortName = "Jenk's";
	public static final String fullName = "Jenk's Optimal";
	transient private int[] classification;
	protected final static Logger logger = Logger
			.getLogger(ClassifierJenks.class.getName());

	/**
	 * This method performs an unconstrained grouping of a given set of data
	 * items into a given number of groups. The input data items will be ordered
	 * first.
	 * 
	 * A constrained version of this method may be developed to accept the given
	 * dataset in its given order, which is assumed to represent some preference
	 * (or constraints) imposed by the user.
	 * 
	 * @return double[] - breaks--from smallest to largest
	 * @param double[] data_
	 * @param numclass
	 *            int
	 */
	public static double[] getJenksBreaks(double[] data_, int numclass) {
		int numdata = data_.length;
		// copy and sort the data
		double[] sorteddata = new double[numdata];
		System.arraycopy(data_, 0, sorteddata, 0, numdata);
		Arrays.sort(sorteddata);

		double[][] mat1 = new double[numdata + 1][numclass + 1];
		double[][] mat2 = new double[numdata + 1][numclass + 1];

		for (int i = 1; i <= numclass; i++) {
			mat1[1][i] = 1;
			mat2[1][i] = 0;
			for (int j = 2; j <= numdata; j++) {
				mat2[j][i] = Double.MAX_VALUE;
			}
		}

		double ssd = 0;
		for (int rangeEnd = 2; rangeEnd <= numdata; rangeEnd++) {
			double sumX = 0;
			double sumX2 = 0;
			double w = 0;
			int dataId;
			for (int m = 1; m <= rangeEnd; m++) {
				dataId = rangeEnd - m + 1;

				double val = sorteddata[dataId - 1];
				sumX2 += val * val;
				sumX += val;
				w++;
				ssd = sumX2 - (sumX * sumX) / w;

				for (int j = 2; j <= numclass; j++) {
					if (!(mat2[rangeEnd][j] < (ssd + mat2[dataId - 1][j - 1]))) {
						mat1[rangeEnd][j] = dataId;
						mat2[rangeEnd][j] = ssd + mat2[dataId - 1][j - 1];
					}

				}

			}

			mat1[rangeEnd][1] = 1;
			mat2[rangeEnd][1] = ssd;
		}

		// logger.info("\nmat1:\n");
		// for (int i=0; i< mat1.length; i++)
		// {
		// logger.info();
		// for (int k=0; k< mat1[0].length; k++)
		// System.out.print((float)mat1[i][k]+ "\t\t");
		// }
		// logger.info("\nmat2:\n");
		// for (int i=0; i< mat2.length; i++)
		// {
		// logger.info();
		// for (int k=0; k< mat2[0].length; k++)
		// System.out.print((float)mat2[i][k]+ "\t\t");
		// }

		double[] kbreaks = new double[numclass];
		kbreaks[numclass - 1] = sorteddata[numdata - 1]; // the last
		// break is
		// the
		// maximum
		// value

		// break value is included in the lower class.
		int k = numdata;
		for (int j = numclass; j >= 2; j--) {
			int id = (int) (mat1[k][j]) - 2;
			kbreaks[j - 2] = sorteddata[id];
			k = (int) mat1[k][j] - 1;
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Jenk's Classification Breaks:");
			for (int i = 0; i < numclass; i++) {
				logger.finest(" i = " + kbreaks[i]);
			}
		}

		return kbreaks;
	}

	public static void main(String[] args) {
		double[] data = { 1, 1.5, 2, 4, 6, 7.1, 10 };
		data = new double[12000];
		Random rand = new Random();
		for (int i = 0; i < data.length; i++) {
			data[i] = (int) (rand.nextDouble() * 1000);
		}
		long start = System.nanoTime();

		ClassifierJenks jenk = new ClassifierJenks();
		int[] classes = jenk.classify(data, 4);
		long end = System.nanoTime();
		for (int i = 0; i < classes.length; i++) {
			logger.info(i + " " + classes[i]);
		}
		logger.info("classifying " + data.length + " observations took "
				+ ((end - start) / 1000000000f) + " seconds");
	}

	public String getFullName() {
		return fullName;
	}

	public String getShortName() {
		return shortName;
	}

	private int findClass(double obs, double[] breaks) {
		int classNum = 0;
		for (double breaker : breaks) {
			if (breaker >= obs) {
				return classNum;
			}
			classNum++;
		}
		return classNum;
	}

	public int[] classify(double[] data, int numClasses) {
		classification = new int[data.length];
		double[] breaks = ClassifierJenks.getJenksBreaks(data, numClasses);

		for (int i = 0; i < data.length; i++) {
			double num = data[i];
			classification[i] = findClass(num, breaks);
		}

		// TODO Auto-generated method stub
		return classification;
	}

}
