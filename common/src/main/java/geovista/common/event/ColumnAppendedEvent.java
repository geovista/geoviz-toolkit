/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

import geovista.common.data.DataSetForApps;

/**
 * An ColumnAppendedEvent signals that some new data is available for the
 * current data set. For example, a derived field has been calculated.
 * 
 */

public class ColumnAppendedEvent extends EventObject {

	private transient final DataSetForApps newDataSet;
	// private transient final DataSetForApps oldDataSet;

	private final ChangeType eventType;

	public static enum ChangeType {
		TYPE_MODIFIED, TYPE_REDUCED, TYPE_EXTENDED
	}

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * newDataSet and newData values are indicated. The newDataSet is the
	 * unmodified data set. Clients will compare this array against their
	 * current arrays to find how this applies. This constructor internally sets
	 * the event as TYPE_EXTENDED.
	 */

	public ColumnAppendedEvent(DataSetForApps oldDataSet,
			DataSetForApps newDataSet, Object source) {
		super(source);
		eventType = ColumnAppendedEvent.ChangeType.TYPE_EXTENDED;
		this.newDataSet = newDataSet;
		// this.oldDataSet = oldDataSet;

		// XXX question: should we check for the appropriate array length
		// here???

	}

	// begin accessors
	/**
	 * This method may be used to compare the broadcast newDataSet reference to
	 * a previously broadcast one.
	 */

	public DataSetForApps getDataSet() {
		return newDataSet;
	}

	/**
	 * Returns the type of event (TYPE_EXTENDED, TYPE_REDUCED, or TYPE_MODIFIED)
	 */

	public ColumnAppendedEvent.ChangeType getEventType() {
		return eventType;
	}

	// end accessors

}
