package geovista.common.cluster;

import java.util.Vector;

/**
 * Title: PriorityQue Description: A very simple priority que implementation,
 * for use with the minimum spanning tree assignment. Copyright: Copyright (c)
 * 2002 Company: MAH TS
 * 
 * @author Markus Svensson
 * 
 */

public class PriorityQue {
	private int capacity;

	private final Vector que;

	/**
	 * Construct the que
	 * 
	 * @param The
	 *            number of elements in the initial que
	 */
	public PriorityQue(int numOfElements) {
		capacity = numOfElements;
		que = new Vector(capacity);
	}

	/**
	 * Inserts an edge into the que.
	 * 
	 * @param The
	 *            edge to insert
	 */
	public void insertItem(MSTEdge item) {
		que.add(item);
	}

	/**
	 * Get the num of elements in the que
	 * 
	 * @return The num of elements.
	 */
	public int getNumOfElements() {
		return capacity;
	}

	/**
	 * Removes the element with the lowest priority from the que.
	 * 
	 * @return The edge with the lowest weight
	 */
	public MSTEdge removeMin() {

		MSTEdge temp = new MSTEdge();
		int index = 0;
		for (int i = 0; i < que.size(); i++) {
			MSTEdge tmp1 = (MSTEdge) que.elementAt(i);
			if (temp.getWeight() > tmp1.getWeight()) {
				temp = tmp1;
				index = i;
			}
		}

		que.removeElementAt(index);

		capacity = que.size();
		return temp;
	}

}