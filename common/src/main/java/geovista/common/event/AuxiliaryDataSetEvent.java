/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import geovista.common.data.DataSetForApps;

/**
 * An AuxiliaryDataSetEvent signals that a new data set is available.
 * 
 */
public class AuxiliaryDataSetEvent extends DataSetEvent {

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * dataSet values are indicated.
	 * 
	 * This class is identical to DataSetEvent, except in name. So why have it?
	 * The idea is that classes such as GeoMap may want to handle some data sets
	 * in a special way. Simply adding a flag to the DataSetEvent would cause
	 * other clients which did not check that flag to behave incorrectly.
	 * 
	 */
	@Deprecated
	public AuxiliaryDataSetEvent(Object source, Object[] dataSet) {
		super(source, dataSet);

	}

	public AuxiliaryDataSetEvent(DataSetForApps dataSetForApps, Object source) {
		super(dataSetForApps, source);

	}

}
