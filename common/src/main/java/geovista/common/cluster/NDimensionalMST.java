/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.common.cluster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;

/**
 * Creates a minimum spanning tree. Uses Diansheng's SpatialMST to do the work.
 * 
 */
public class NDimensionalMST {

	public static final String COMMAND_DATA_SET_MADE = "dataMade";
	public static final int GRAPH_TYPE_FULL = 0;
	public static final int GRAPH_TYPE_DELAUNAY = 1;
	public static final int GRAPH_TYPE_GABRIEL = 2;
	public static final int GRAPH_TYPE_MST = 3;
	private transient EventListenerList listenerList;
	private transient int[] points; // one point per observation
	// we start with every edge, then these get weaned
	private transient MSTEdge[] edges; // to make MST, but soon this will
	// be just the Gabriel Graph edges

	private transient MSTEdge[] mstEdges;

	// ex:
	// [2][3] = an edge from index 2 to index 3
	private transient int[][] returnEdges; // row, column. n rows = n edges.

	private transient double[] tempLow; // for hypersphere calculations
	private transient double[] tempHigh;
	transient double mstMedian;
	final static Logger logger = Logger.getLogger(NDimensionalMST.class
			.getName());

	// test
	public NDimensionalMST() {
	}

	// this method is for subspaces
	public void addSubspaceData(DataSetForApps data) {
		// selectedIndicies is the number of variables
		// so I suppose in this case it is always 1, index 0

		// doubleData[1] =
		// this.normalize(data.getNumericDataAsDouble(selectedIndicies[i] + 1));
		// //skip variable names

		points = new int[data.getNumberNumericAttributes()];

		// this.tempLow = new double[data.getNumberNumericAttributes()];
		// this.tempHigh = new double[data.getNumberNumericAttributes()];

		for (int i = 0; i < data.getNumberNumericAttributes(); i++) {
			points[i] = i; // set the id = implicit index
		}

		int nEdges = (data.getNumberNumericAttributes())
				* (data.getNumberNumericAttributes()); // matrix, so square it
		nEdges = nEdges - data.getNumberNumericAttributes(); // take diagonal
		// away (no
		// distances
		// from points
		// to
		// themselves)
		nEdges = nEdges / 2;// only need to process one way, it's symetric
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("nEdges " + nEdges);
		}
		// nEdges+=1;//what the ???

		edges = new MSTEdge[nEdges]; // this is the max, we can make it
		// smaller later

		double dist = 0;
		int edgeCount = 0;

		Object[] doubleData = new Object[data.getNumberNumericAttributes()];
		for (int i = 0; i < data.getNumberNumericAttributes(); i++) {
			// +1 for datasetforapps foulness
			doubleData[i] = data.getNumericDataAsDouble(i); // set
			// the
			// id =
			// implicit
			// index
		}
		double[] doubleOrigin = null;
		double[] doubleDest = null;
		logger.finest("before making edges");
		for (int origin = 0; origin < data.getNumberNumericAttributes(); origin++) {
			int originPoint = points[origin];

			for (int dest = 1; dest < data.getNumberNumericAttributes(); dest++) {
				if (origin < dest) {
					int destPoint = points[dest];
					doubleOrigin = (double[]) doubleData[origin];
					doubleDest = (double[]) doubleData[dest];
					dist = DescriptiveStatistics
							.correlationCoefficientIgnoreNaN(doubleOrigin,
									doubleDest, false);
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest("origin = " + origin + ", dest = " + dest
								+ ", dist = " + dist);
					}
					dist = Math.abs(dist);
					dist = 1 - dist;
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest("dist " + dist);
					}
					edges[edgeCount] = new MSTEdge(originPoint, destPoint, dist);
					edgeCount++;

				} // end if origin != dest
			} // next dest
		} // next origin

		logger.finest("before quicksort");
		// now sort those edges
		Arrays.sort(edges);
		// QuickSort.sort(edges, 0, edges.length - 1);

		logger.finest("num edges before mst = " + edges.length);
		// dgMST.setEdges(edges);
		// XXX fix me
		mstEdges = MinimumSpanningTree.kruskalArray(edges);
		// mstEdges = (MSTEdge[]) MinimumSpanningTree.kruskal(null, null, null,
		// 0)
		// .toArray();
		int medianPlace = mstEdges.length / 2;
		mstMedian = mstEdges[medianPlace].getWeight();

		// SpatialEdge[] newEdges = dgMST.getNewEdges();
	}

	/*
	 * public void addData(DataSetForApps data, int[] selectedIndicies) {
	 * Object[] doubleData = new Object[selectedIndicies.length];
	 * 
	 * for (int i = 0; i < selectedIndicies.length; i++) { doubleData[i] =
	 * this.normalize(data.getNumericDataAsDouble(selectedIndicies[i] + 1));
	 * //skip variable names }
	 * 
	 * points = new SpatialPoint[data.getNumObservations()];
	 * 
	 * this.tempLow = new double[data.getNumObservations()]; this.tempHigh = new
	 * double[data.getNumObservations()];
	 * 
	 * for (int i = 0; i < data.getNumObservations(); i++) { points[i] = new
	 * SpatialPoint(i); //set the id = implicit index }
	 * 
	 * int nEdges = data.getNumObservations() data.getNumObservations();
	 * //matrix, so square it nEdges = nEdges - data.getNumObservations();
	 * //take diagonal away (no distances from points to themselves) nEdges =
	 * nEdges / 2;
	 * 
	 * edges = new SpatialEdge[nEdges]; //this is the max, we can make it
	 * smaller later
	 * 
	 * double dist = 0; int edgeCount = 0; double[] originLocation = new
	 * double[selectedIndicies.length]; double[] destinationLocation = new
	 * double[selectedIndicies.length]; double[] testLocation= new
	 * double[selectedIndicies.length];
	 * 
	 * logger.finest("before making edges"); for (int origin = 0; origin <
	 * data.getNumObservations(); origin++) { SpatialPoint originPoint =
	 * points[origin];
	 * 
	 * for (int i = 0; i < selectedIndicies.length; i++) { double[] doubleDat =
	 * (double[]) doubleData[i]; originLocation[i] = doubleDat[origin]; }
	 * 
	 * boolean logger.isLoggable(Level.FINEST) = false;
	 * 
	 * if (origin == 451) { logger.isLoggable(Level.FINEST) = true; }
	 * 
	 * logger.finest("origin = " + origin); //long mem =
	 * Runtime.getRuntime().freeMemory(); logger.finest("free mem = " + mem);
	 * for (int dest = 0; dest < data.getNumObservations(); dest++) { if (origin
	 * < dest) { SpatialPoint destPoint = points[dest];
	 * 
	 * for (int i = 0; i < selectedIndicies.length; i++) { double[] doubleDat =
	 * (double[]) doubleData[i]; destinationLocation[i] = doubleDat[dest]; }
	 * 
	 * if (logger.isLoggable(Level.FINEST)) { logger.finest("dest = " + dest);
	 * //mem = Runtime.getRuntime().freeMemory(); logger.finest("detail: free
	 * mem = " + mem); } boolean foundAnyPoint = false; boolean foundThisPoint =
	 * false; for (int testPoint = 0; testPoint < data.getNumObservations();
	 * testPoint++) { if (testPoint != origin && testPoint != dest) { for (int i
	 * = 0; i < selectedIndicies.length; i++) { double[] doubleDat = (double[])
	 * doubleData[i]; testLocation[i] = doubleDat[testPoint]; } foundThisPoint =
	 * (
	 * this.withinHyperSphere(originLocation,destinationLocation,testLocation));
	 * } if (foundThisPoint){ foundAnyPoint = true; break; } foundThisPoint =
	 * false; } if (!foundAnyPoint){ dist =
	 * this.manhattanDistance(originLocation, destinationLocation);
	 * edges[edgeCount] = new SpatialEdge(originPoint, destPoint, dist);
	 * edgeCount++; } } //end if origin != dest } //next dest } //next origin
	 * 
	 * //now reduce the number in the edge array SpatialEdge[] newEdges = new
	 * SpatialEdge[edgeCount]; System.arraycopy(edges,0,newEdges,0,edgeCount);
	 * edges = newEdges; logger.finest("nEdges = " + edgeCount); //bail here if
	 * doing gabriel if (data.getNumObservations() > 3){ return; }
	 * 
	 * 
	 * //SpatialEdge[] newEdges = dgMST.getNewEdges(); }
	 */
	public void addData(DataSetForApps data, int[] selectedIndicies) {
		Object[] doubleData = new Object[selectedIndicies.length];

		for (int i = 0; i < selectedIndicies.length; i++) {
			doubleData[i] = NDimensionalMST.normalize(data
					.getNumericDataAsDouble(selectedIndicies[i])); // skip
			// variable
			// names
		}

		points = new int[data.getNumObservations()];

		tempLow = new double[data.getNumObservations()];
		tempHigh = new double[data.getNumObservations()];

		for (int i = 0; i < data.getNumObservations(); i++) {
			points[i] = i; // set the id = implicit index
		}

		int nEdges = data.getNumObservations() * data.getNumObservations(); // matrix
		// ,
		// so
		// square
		// it
		nEdges = nEdges - data.getNumObservations(); // take diagonal away
		// (no distances from
		// points to themselves)
		nEdges = nEdges / 2;

		edges = new MSTEdge[nEdges]; // this is the max, we can make it
		// smaller later

		double dist = 0;
		int edgeCount = 0;
		double[] originLocation = new double[selectedIndicies.length];
		double[] destinationLocation = new double[selectedIndicies.length];
		logger.finest("before making edges");
		for (int origin = 0; origin < data.getNumObservations(); origin++) {
			int originPoint = points[origin];

			for (int i = 0; i < selectedIndicies.length; i++) {
				double[] doubleDat = (double[]) doubleData[i];
				originLocation[i] = doubleDat[origin];
			}

			if (origin == 451) {
			}
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("origin = " + origin);
			}

			for (int dest = 0; dest < data.getNumObservations(); dest++) {
				if (origin < dest) {
					int destPoint = points[dest];

					for (int i = 0; i < selectedIndicies.length; i++) {
						double[] doubleDat = (double[]) doubleData[i];
						destinationLocation[i] = doubleDat[dest];
					}

					if (logger.isLoggable(Level.FINEST)) {
						long mem = Runtime.getRuntime().freeMemory();
						logger.finest("dest = " + dest);
						mem = Runtime.getRuntime().freeMemory();
						logger.finest("detail: free mem = " + mem);
					}

					dist = NDimensionalMST.manhattanDistance(originLocation,
							destinationLocation);
					edges[edgeCount] = new MSTEdge(originPoint, destPoint, dist);
					edgeCount++;

				} // end if origin != dest
			} // next dest
		} // next origin

		// now reduce the number in the edge array
		// MSTEdge[] newEdges = new MSTEdge[edgeCount];
		// System.arraycopy(edges, 0, newEdges, 0, edgeCount);
		// edges = newEdges;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("nEdges = " + edgeCount);
		}
		mstEdges = findMSTEdges(edges);
		// Edge[] newEdges = dgMST.getNewEdges();
	}

	private MSTEdge[] findMSTEdges(MSTEdge[] edges) {

		// now sort those edges
		Arrays.sort(edges);
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("num edges before mst = " + edges.length);
		}
		return MinimumSpanningTree.kruskalArray(edges);
	}

	// XXX is this method used anywhere?
	MSTEdge[] findAllEdges(Object[] doubleData, DataSetForApps data,
			int[] selectedIndicies) {
		points = new int[data.getNumObservations()];

		tempLow = new double[data.getNumObservations()];
		tempHigh = new double[data.getNumObservations()];

		for (int i = 0; i < data.getNumObservations(); i++) {
			points[i] = i; // set the id = implicit index
		}

		int nEdges = data.getNumObservations() * data.getNumObservations(); // matrix
		// ,
		// so
		// square
		// it
		nEdges = nEdges - data.getNumObservations(); // take diagonal away
		// (no distances from
		// points to themselves)
		nEdges = nEdges / 2;

		edges = new MSTEdge[nEdges]; // this is the max, we can make it
		// smaller later

		double dist = 0;
		int edgeCount = 0;
		double[] originLocation = new double[selectedIndicies.length];
		double[] destinationLocation = new double[selectedIndicies.length];
		double[] testLocation = new double[selectedIndicies.length];

		logger.finest("before making edges");
		for (int origin = 0; origin < data.getNumObservations(); origin++) {
			int originPoint = points[origin];

			for (int i = 0; i < selectedIndicies.length; i++) {
				double[] doubleDat = (double[]) doubleData[i];
				originLocation[i] = doubleDat[origin];
			}

			if (origin == 451) {
			}

			logger.finest("origin = " + origin);

			for (int dest = 0; dest < data.getNumObservations(); dest++) {
				if (origin < dest) {
					int destPoint = points[dest];

					for (int i = 0; i < selectedIndicies.length; i++) {
						double[] doubleDat = (double[]) doubleData[i];
						destinationLocation[i] = doubleDat[dest];
					}

					if (logger.isLoggable(Level.FINEST)) {
						long mem = Runtime.getRuntime().freeMemory();
						logger.finest("free mem = " + mem);
						logger.finest("dest = " + dest);

						logger.finest("detail: free mem = " + mem);
					}
					boolean foundAnyPoint = false;
					boolean foundThisPoint = false;
					for (int testPoint = 0; testPoint < data
							.getNumObservations(); testPoint++) {
						if (testPoint != origin && testPoint != dest) {
							for (int i = 0; i < selectedIndicies.length; i++) {
								double[] doubleDat = (double[]) doubleData[i];
								testLocation[i] = doubleDat[testPoint];
							}
							foundThisPoint = (withinHyperSphere(originLocation,
									destinationLocation, testLocation));
						}
						if (foundThisPoint) {
							foundAnyPoint = true;
							break;
						}
						foundThisPoint = false;
					}
					if (!foundAnyPoint) {
						dist = NDimensionalMST.manhattanDistance(
								originLocation, destinationLocation);
						edges[edgeCount] = new MSTEdge(originPoint, destPoint,
								dist);
						edgeCount++;
					}

				} // end if origin != dest
			} // next dest
		} // next origin
		return edges;
	}

	private boolean withinHyperSphere(double[] pointOne, double[] pointTwo,
			double[] candidate) {

		double factor = Math.pow(1.05, pointOne.length);
		// first we fill in the temp data and check for out of hypercube
		for (int var = 0; var < pointOne.length; var++) {
			if (pointOne[var] < pointTwo[var]) {
				tempLow[var] = pointOne[var] / factor;
				tempHigh[var] = pointTwo[var] * factor;
			} else {
				tempLow[var] = pointTwo[var] / factor;
				tempHigh[var] = pointOne[var] * factor;
			}
			if (candidate[var] < tempLow[var] || candidate[var] > tempHigh[var]) {
				return false;
			}
		}

		return true;
	}

	public MSTEdge[] getMST() {
		return mstEdges;
	}

	public MSTEdge[] getFullGraph() {
		return edges;
	}

	public MSTEdge[] getGabrielGraph() {
		return edges;
	}

	public int[][] getMSTIndexes() {
		returnEdges = new int[mstEdges.length][2];

		for (int i = 0; i < mstEdges.length; i++) {
			returnEdges[i][0] = mstEdges[i].getStart();
			returnEdges[i][1] = mstEdges[i].getEnd();
		}

		return returnEdges;
	}

	public static double manhattanDistance(double[] doubleArray,
			double[] d2Array) {
		double temp = 0;
		double sum = 0;

		for (int i = 0; i < doubleArray.length; i++) {
			temp = doubleArray[i] - d2Array[i];

			if (temp < 0) {
				sum -= temp;
			} else {
				sum += temp;
			}
		} // end for

		return sum;
	} // end distance

	public static double[] normalize(double[] data) {
		double range = geovista.common.data.DescriptiveStatistics
				.rangeIgnoreNaN(data);
		double min = geovista.common.data.DescriptiveStatistics
				.minIgnoreNaN(data);
		double mean = geovista.common.data.DescriptiveStatistics
				.meanIgnoreNaN(data);

		double ratio = 1d / range;
		double[] dataBack = new double[data.length];
		double meanBack = mean - min;
		meanBack = meanBack * ratio;
		logger.finest("meanBack = " + meanBack);

		for (int i = 0; i < data.length; i++) {
			double newDat = data[i];
			if (Double.isNaN(newDat)) {
				dataBack[i] = meanBack;
			} else {
				newDat = newDat - min;
				newDat = newDat * ratio;
				dataBack[i] = newDat;
			}
		}

		return dataBack;
	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireActionPerformed(String command) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							command);
				}

				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}
}