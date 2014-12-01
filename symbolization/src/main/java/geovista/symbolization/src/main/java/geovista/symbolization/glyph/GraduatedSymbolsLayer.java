/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.symbolization.glyph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;

/**
 * 
 * Forms GlyphRenderer[] out of data given to it. Paints GlyphRenderer[] onto a
 * Graphics2D object passed in.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class GraduatedSymbolsLayer implements DataSetListener,
		IndicationListener {

	private transient GlyphRenderer[] plots;
	private transient DataSetForApps dataSet;
	private transient int indication = -1;
	private transient float penWidth;
	private transient int[] varList;
	private transient int[] obsList; // in case we want to subset for some
	// reason, i.e. selection or
	// conditioning
	private transient Color[] starColors;
	private transient final HashMap obsHashMap;
	// private static boolean DEBUG = false;

	public static final int MAX_N_VARS = 6;

	public GraduatedSymbolsLayer() {

		obsHashMap = new HashMap();

	}

	public void dataSetChanged(DataSetEvent e) {
		indication = -1;
		dataSet = e.getDataSetForApps();
		initStarPlots(dataSet.getNumObservations());

		int nVars = dataSet.getNumberNumericAttributes();
		int maxVars = GraduatedSymbolsLayer.MAX_N_VARS;

		if (maxVars < nVars) {
			nVars = maxVars;
		}

		varList = new int[nVars];
		for (int i = 0; i < nVars; i++) {
			varList[i] = i;
		}

		makeStarPlots();
	}

	public void subspaceChanged(SubspaceEvent e) {
		varList = e.getSubspace();
		findSpikeLengths(varList);

	}

	private void initStarPlots(int numPlots) {
		obsList = new int[dataSet.getNumObservations()];
		plots = new GlyphRenderer[obsList.length];

		plots = new GlyphRenderer[obsList.length];
		for (int i = 0; i < plots.length; i++) {
			obsList[i] = i;
			plots[i] = new GlyphRenderer();

		}
		obsHashMap.clear();
		for (int i = 0; i < plots.length; i++) {
			obsHashMap.put(new Integer(i), plots[i]);

		}

	}

	private void makeStarPlots() {

		applyStarFillColors(starColors);
		findSpikeLengths(varList);
		indication = -1; // clear indication

	}

	private void findSpikeLengths(int[] vars) {
		int nVars = vars.length;
		double[] minVals = new double[nVars];
		double[] maxVals = new double[nVars];
		double[] ranges = new double[nVars];

		int[][] spikeLengths = new int[obsList.length][nVars];
		for (int i = 0; i < nVars; i++) {
			double[] varData = dataSet.getNumericDataAsDouble(vars[i]);
			minVals[i] = DescriptiveStatistics.min(varData);
			maxVals[i] = DescriptiveStatistics.max(varData);
			ranges[i] = DescriptiveStatistics.range(varData);
			double range = 0d;
			double prop = 0d;
			for (int row = 0; row < obsList.length; row++) {
				int index = obsList[row];
				double val = varData[index];
				range = ranges[i];
				// make range zero-based
				range = range - minVals[i];
				val = val - minVals[i];
				prop = val / ranges[i];
				spikeLengths[row][i] = (int) (prop * 100d);
			}
		}
		int[] spikes = new int[nVars];
		for (int row = 0; row < obsList.length; row++) {

			for (int col = 0; col < spikes.length; col++) {

				spikes[col] = spikeLengths[row][col];
			}
			plots[row].setLengths(spikes);
		}

	}

	public void renderStars(Graphics2D g2) {

		if (plots == null || plots[0] == null) {
			return;
		}

		g2.setStroke(new BasicStroke(penWidth));

		for (GlyphRenderer element : plots) {
			element.paintStar(g2);
		}

		GlyphRenderer sp = (GlyphRenderer) obsHashMap.get(new Integer(
				indication));
		if (sp != null) {
			g2.setColor(GlyphRenderer.defaultIndicationColor);
			sp.paintStar(g2);
		}

	}

	public void renderStar(Graphics2D g2, int plotNumber) {
		// xxx need to handle negative width
		if (plotNumber < 0 || plotNumber >= plots.length) {
			return;
		}
		g2.setStroke(new BasicStroke(penWidth));
		plots[plotNumber].paintStar(g2);
	}

	public String[] getVarNames() {
		if (varList == null) {
			return null;
		}

		String[] varNames = new String[varList.length];
		for (int i = 0; i < varList.length; i++) {
			varNames[i] = dataSet.getNumericArrayName(varList[i]);
		}

		return varNames;
	}

	public double[] getValues(int obs) {
		if (varList == null) {
			return null;
		}
		double[] vals = new double[varList.length];

		for (int i = 0; i < varList.length; i++) {
			vals[i] = dataSet.getNumericValueAsDouble(varList[i], obs);
		}
		return vals;
	}

	public void indicationChanged(IndicationEvent e) {

		setIndication(e.getIndication());

	}

	public Glyph[] findGlyphs() {
		GlyphRenderer[] newPlots = new GlyphRenderer[plots.length];
		for (int i = 0; i < plots.length; i++) {
			newPlots[i] = plots[i].copy();
		}
		return newPlots;
	}

	public int getIndication() {
		return indication;
	}

	public void setIndication(int newIndication) {
		// the incoming indication is an index into the overall data set
		if (indication == newIndication) {
			return; // no need to do anything
		}

		GlyphRenderer spOld = (GlyphRenderer) obsHashMap.get(new Integer(
				indication));
		if (indication >= 0 && spOld != null) { // clear old indication, if we
			// have one
			spOld.setFillColor(starColors[indication]);
		}
		GlyphRenderer spNew = (GlyphRenderer) obsHashMap.get(new Integer(
				newIndication));

		if (spNew != null) { // paint new indication

			spNew.setFillColor(GlyphRenderer.defaultIndicationColor);

			indication = newIndication;
		}

	}

	public String getObservationName(int index) {
		// int orderIndex = this.plotsOrder[index];
		if (dataSet == null) {
			return "";
		}
		if (dataSet.getObservationNames() == null) {
			return String.valueOf(index);
		}
		return dataSet.getObservationNames()[index];
	}

	public void setStarFillColors(Color[] starColors) {
		applyStarFillColors(starColors);
	}

	void applyStarFillColors(Color[] starColors) {
		this.starColors = starColors;
		if (plots == null || starColors != null
				&& plots.length != starColors.length) {
			return;
		}
		if (this.starColors == null) {
			this.starColors = new Color[plots.length];
			for (int i = 0; i < plots.length; i++) {
				this.starColors[i] = GlyphRenderer.defaultFillColor;
				plots[i].setFillColor(this.starColors[i]);
			}
		} else {
			for (int i = 0; i < plots.length; i++) {
				plots[i].setFillColor(this.starColors[i]);
			}
		}

	}

	public Color getStarFillColor(int ind) {
		return starColors[ind];

	}

	void setPlotLocations(Rectangle[] plotLocs) {
		for (int i = 0; i < plots.length; i++) {
			plots[i].setTargetArea(plotLocs[i]);
		}
	}

	/*
	 * Sets the visible set of starplots. the array of ints is an array of which
	 * starplots are to be visible. Null arrays or arrays of length zero are
	 * ignored.
	 */
	public void setObsList(int[] obsList) { // for selections etc.
		if (obsList == null || obsList.length < 1) {
			return;
		}
		this.obsList = obsList;
		// no no no, we should set the target area of the relevant plots
		// this.plotsOrder = obsList;
		// this.plotLocations = new Rectangle[plotsOrder.length];
		// this.makeStarPlots();
		// and update our obsHashMap
		obsHashMap.clear();
		for (int element : obsList) {
			obsHashMap.put(new Integer(element), plots[element]);

		}
		indication = -1; // clear indication

	}

	public int[] getObsList() {
		return obsList;
	}

	public DataSetForApps getDataSet() {
		return dataSet;
	}
}
