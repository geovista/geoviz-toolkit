/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SpatialStatistics {

	private static final Logger logger = Logger
			.getLogger(SpatialStatistics.class.getName());

	/**
	 */
	//
	public static double[] calculateMoranScores(double[] zData,
			SpatialWeights sw) {

		double[] moranScores = new double[zData.length];
		for (int i = 0; i < zData.length; i++) {
			List<Integer> bors = sw.getNeighborIDs(i);
			double sumScore = 0;
			for (int j = 0; j < bors.size(); j++) {
				sumScore = sumScore + zData[bors.get(j)];
			}
			moranScores[i] = zData[i] * sumScore;
		}
		return moranScores;
	}

	public static double[] calculateSpaceTimeMoranScores(List<double[]> data,
			SpatialWeights sw, TemporalWeights tw) {

		double[] moranScores = new double[data.get(0).length];
		double sumScore = 0;
		for (int obs = 0; obs < data.get(0).length; obs++) {
			for (int timeSlice = 0; timeSlice < data.size(); timeSlice++) {
				double[] randomData = data.get(timeSlice);

				List<Integer> bors = sw.getNeighborIDs(obs);

				for (int j = 0; j < bors.size(); j++) {
					sumScore = sumScore + randomData[bors.get(j)];
					// sumScore = sumScore + randomData[bors.get(j)]
					// * (1 * (timeSlice + 1));
				}
				// sumScore = sumScore * (1 / (timeSlice + 1));

			}
			moranScores[obs] = data.get(0)[obs] * sumScore;
		}
		// logger.info(Arrays.toString(moranScores));
		return moranScores;
	}

	public static double[] calculateRandomMoranScores(double[] zData,
			SpatialWeights sw) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("calculating moran scores");
		}
		double[] moranData;
		moranData = new double[zData.length];
		System.arraycopy(zData, 0, moranData, 0, zData.length);

		shuffleCollection(moranData);

		double[] moranScores = new double[moranData.length];

		for (int i = 0; i < moranData.length; i++) {
			List<Integer> bors = sw.getNeighborIDs(i);
			double sumScore = 0;
			for (int j = 0; j < bors.size(); j++) {
				sumScore = sumScore + moranData[bors.get(j)];
			}
			moranScores[i] = moranData[i] * sumScore;
		}
		return moranScores;
	}

	public static double[] calculateRandomSpaceTimeMoranScores(
			List<double[]> data, SpatialWeights sw, TemporalWeights tw) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("calculating moran scores");
		}
		ArrayList<double[]> moranData;
		moranData = new ArrayList<double[]>(data.size());

		for (int i = 0; i < data.size(); i++) {
			double[] dat = data.get(i);
			double[] newDat = new double[dat.length];
			System.arraycopy(dat, 0, newDat, 0, dat.length);
			shuffleCollection(newDat);
			moranData.add(newDat);
		}

		return SpatialStatistics.calculateSpaceTimeMoranScores(moranData, sw,
				tw);

	}

	private static void shuffleCollection(double[] moranData) {
		ArrayList<Double> randomList = new ArrayList();
		for (double d : moranData) {
			randomList.add(d);
		}
		Collections.shuffle(randomList);
		for (int i = 0; i < randomList.size(); i++) {
			moranData[i] = randomList.get(i);
		}
	}

	public static List<double[]> findMonteValues(double[] data, int iterations,
			SpatialWeights sw, boolean useTopology) {

		ArrayList<double[]> monteVals = new ArrayList<double[]>();
		for (@SuppressWarnings("unused")
		double element : data) {
			double[] obsMonte = new double[iterations];
			monteVals.add(obsMonte);
		}

		for (int iteration = 0; iteration < iterations; iteration++) {
			double[] iterationVals = calculateRandomMoranScores(data, sw);
			for (int obs = 0; obs < data.length; obs++) {
				double[] monteArray = monteVals.get(obs);
				monteArray[iteration] = iterationVals[obs];
			}
		}

		return monteVals;
	}

	public static List<double[]> findSpaceTimeMonteValues(List<double[]> data,
			int iterations, SpatialWeights sw, TemporalWeights tw,
			boolean useTopology) {

		ArrayList<double[]> monteVals = new ArrayList<double[]>();
		for (int i = 0; i < data.get(0).length; i++) {
			double[] obsMonte = new double[iterations];
			monteVals.add(obsMonte);
		}

		for (int iteration = 0; iteration < iterations; iteration++) {
			double[] iterationVals = calculateRandomSpaceTimeMoranScores(data,
					sw, tw);
			for (int obs = 0; obs < data.get(0).length; obs++) {
				double[] monteArray = monteVals.get(obs);
				monteArray[iteration] = iterationVals[obs];
			}
		}

		return monteVals;
	}

	public static double[] findSpaceTimeMonteValues(List<double[]> data,
			int iterations, ArrayList<WeightedNeighbor> neighbors,
			TemporalWeights tw) {

		double[] monteVals = new double[iterations];
		Random rand = new Random();
		HashSet<Integer> numbersUsed = new HashSet<Integer>();
		for (int iteration = 0; iteration < iterations; iteration++) {

			double sumScore = 0;
			for (int timeSlice = 0; timeSlice < data.size(); timeSlice++) {
				double[] sliceData = data.get(timeSlice);
				numbersUsed.clear();
				for (int bor = 0; bor < neighbors.size(); bor++) {
					int randInt = getUniqueRandom(rand, numbersUsed,
							sliceData.length);
					sumScore = sumScore + sliceData[randInt];
				}

			}
			int obs = getUniqueRandom(rand, numbersUsed, data.get(0).length);
			monteVals[iteration] = data.get(0)[obs] * sumScore;
		}

		return monteVals;
	}

	private static int getUniqueRandom(Random rand,
			HashSet<Integer> numbersUsed, int length) {
		int randNum = rand.nextInt(length);
		if (numbersUsed.contains(randNum) == false) {
			numbersUsed.add(randNum);
			return randNum;
		}
		// logger.info("collision!!!" + randNum);
		return getUniqueRandom(rand, numbersUsed, length);
	}

	public static double[] findPValues(double[] zData, int numTries,
			SpatialWeights sw) {

		// if this approach uses too much memory, we could do it in iterations,
		// using the sqrt of
		// the number of iterations desired, and then averaging
		List<double[]> monteVals = findMonteValues(zData, numTries, sw, false);
		double[] pVals = new double[zData.length];

		double[] moranScores = SpatialStatistics
				.calculateMoranScores(zData, sw);

		for (int obs = 0; obs < zData.length; obs++) {
			pVals[obs] = DescriptiveStatistics.percentAbove(monteVals.get(obs),
					moranScores[obs]);
		}

		return pVals;
	}

	public static double[] findSpaceTimePValues(
			List<double[]> spatioTemporalData, int numTries, SpatialWeights sw,
			TemporalWeights tw, boolean useTopology) {
		double[] pVals = null;
		if (useTopology) {
			pVals = findSpaceTimePValuesTopology(spatioTemporalData, numTries,
					sw, tw);
		} else {
			pVals = findSpaceTimePValuesFullRandom(spatioTemporalData,
					numTries, sw, tw);
		}
		return pVals;
	}

	private static ProbabilityDensity findDensity(
			List<double[]> spatioTemporalData,
			ArrayList<WeightedNeighbor> neighbors, int numTries,
			TemporalWeights tw) {
		ProbabilityDensity pd = null;
		double[] values = findSpaceTimeMonteValues(spatioTemporalData,
				numTries, neighbors, tw);
		pd = new ProbabilityDensity(values);

		return pd;

	}

	private static double[] findSpaceTimePValuesTopology(
			List<double[]> spatioTemporalData, int numTries, SpatialWeights sw,
			TemporalWeights tw) {
		Collection<ArrayList<WeightedNeighbor>> results = sw
				.findUniqueTopologies();
		HashMap<Integer, ProbabilityDensity> densities = new HashMap<Integer, ProbabilityDensity>();
		for (ArrayList<WeightedNeighbor> bors : results) {
			ProbabilityDensity pd = findDensity(spatioTemporalData, bors,
					numTries, tw);
			logger.info("bors n = " + bors.size());
			logger.info(Arrays.toString(pd.getUnderlyingArray()));
			densities.put(bors.size(), pd);
		}
		double[] pVals = new double[spatioTemporalData.get(0).length];
		for (int i = 0; i < pVals.length; i++) {
			int nBors = sw.getNeighborIDs(i).size();
			ProbabilityDensity pd = densities.get(nBors);
			pVals[i] = pd.findPValue(spatioTemporalData.get(0)[i]);
		}
		return pVals;
	}

	private static double[] findSpaceTimePValuesFullRandom(
			List<double[]> spatioTemporalData, int numTries, SpatialWeights sw,
			TemporalWeights tw) {
		List<double[]> monteVals = findSpaceTimeMonteValues(spatioTemporalData,
				numTries, sw, tw, false);
		double[] pVals = new double[spatioTemporalData.get(0).length];

		double[] moranScores = SpatialStatistics.calculateSpaceTimeMoranScores(
				spatioTemporalData, sw, tw);

		for (int obs = 0; obs < spatioTemporalData.get(0).length; obs++) {
			pVals[obs] = DescriptiveStatistics.percentAbove(monteVals.get(obs),
					moranScores[obs]);
		}
		return pVals;
	}
}
