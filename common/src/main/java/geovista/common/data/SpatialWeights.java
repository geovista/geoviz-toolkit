/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

public class SpatialWeights {

    ArrayList<ArrayList<WeightedNeighbor>> neighbors;
    protected final static Logger logger = Logger
	    .getLogger(SpatialWeights.class.getName());

    public SpatialWeights(int listLength) {
	super();
	neighbors = new ArrayList<ArrayList<WeightedNeighbor>>();
	for (int i = 0; i < listLength; i++) {
	    neighbors.add(new ArrayList<WeightedNeighbor>());
	}

    }

    public List<Integer> getNeighborIDs(int obs) {
	ArrayList<WeightedNeighbor> bors = neighbors.get(obs);
	ArrayList<Integer> borIds = new ArrayList(bors.size());
	for (WeightedNeighbor bor : bors) {
	    borIds.add(bor.getTo());
	}
	return borIds;
    }

    public List<Double> getWeights(int obs) {
	ArrayList<WeightedNeighbor> bors = neighbors.get(obs);
	ArrayList<Double> weights = new ArrayList(bors.size());
	for (WeightedNeighbor bor : bors) {
	    weights.add(bor.getWeight());
	}
	return weights;
    }

    public List<WeightedNeighbor> getWeightedNeighbors(int obs) {
	return neighbors.get(obs);
    }

    private void addNeighbor(int id1, int id2) {
	if (id1 == id2) {
	    return;
	}
	List<WeightedNeighbor> id1Neighbors = getWeightedNeighbors(id1);
	List<WeightedNeighbor> id2Neighbors = getWeightedNeighbors(id2);
	if (id1Neighbors == null) {
	    id1Neighbors = new ArrayList<WeightedNeighbor>();
	}
	if (id2Neighbors == null) {
	    id2Neighbors = new ArrayList<WeightedNeighbor>();
	}
	WeightedNeighbor one = new WeightedNeighbor(id1, id2, 1f);
	WeightedNeighbor two = new WeightedNeighbor(id2, id1, 1f);
	id1Neighbors.add(one);
	id2Neighbors.add(two);

    }

    public Collection<ArrayList<WeightedNeighbor>> findUniqueTopologies() {

	// current (simplest) case: single neighbors
	HashMap<Integer, ArrayList<WeightedNeighbor>> results = new HashMap<Integer, ArrayList<WeightedNeighbor>>();
	for (ArrayList<WeightedNeighbor> weights : neighbors) {
	    results.put(weights.size(), weights);

	}
	return results.values();
    }

    public void findNeighbors(List<Geometry> geoms) {
	long nTouches = 0;
	for (int i = 0; i < geoms.size() - 1; i++) {
	    Geometry geom = geoms.get(i);

	    for (int j = i + 1; j < geoms.size(); j++) {
		Geometry geom2 = geoms.get(j);
		boolean touches = false;
		try {
		    touches = geom.touches(geom2);
		    if (logger.isLoggable(Level.FINEST)) {
			logger.finest("" + touches);
		    }
		} catch (TopologyException te) {
		    logger.fine("buffing " + i + " and " + j);
		    geom = geom.buffer(0);
		    geom2 = geom2.buffer(0);
		    touches = geom.touches(geom2);
		}
		if (geom.touches(geom2)) {
		    addNeighbor(i, j);
		    nTouches++;
		}
	    }// next j
	}// next i
	logger.fine("Number of touches = " + nTouches);
    }
}
