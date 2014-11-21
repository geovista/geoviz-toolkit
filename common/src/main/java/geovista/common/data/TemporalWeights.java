/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TemporalWeights {

	ArrayList<ArrayList<WeightedNeighbor>> neighbors;
	protected final static Logger logger = Logger
			.getLogger(TemporalWeights.class.getName());

	public TemporalWeights(int listLength) {
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
		addNeighbor(id1, id2, 1d);

	}

	private void addNeighbor(int id1, int id2, double weight) {
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

	// weights are given from nearest to farthest
	public void findNeighbors(List<Integer> vars, List<Double> weights) {
		int nTouches = 0;
		for (int var = 0; var < vars.size(); var++) {
			for (int weightID = 0; weightID < weights.size(); weightID++) {
				if (var > weightID) {
					addNeighbor(var, var + weightID, weights.get(weightID));
					nTouches++;
				}
			}
		}

		logger.info("Number of temporal touches = " + nTouches);
	}
}
