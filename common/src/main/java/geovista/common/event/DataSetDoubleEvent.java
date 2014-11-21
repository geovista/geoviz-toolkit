/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An DataSetEvent signals that a new data set is available.
 * 
 */
public class DataSetDoubleEvent extends EventObject {

	private transient final double[][] dataSet;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * dataSet values are indicated.
	 */

	public DataSetDoubleEvent(Object source, double[][] dataSet) {
		super(source);
		this.dataSet = dataSet;
	}

	// begin accessors

	public double[][] getDataSet() {
		return dataSet;
	}
	// end accessors

}
