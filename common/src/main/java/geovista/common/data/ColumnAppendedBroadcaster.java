/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.event.ColumnAppendedEvent;
import geovista.common.event.ColumnAppendedListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;

/**
 * This class is able to accept modified data for rebroadcast.
 */
public class ColumnAppendedBroadcaster implements DataSetListener {
	protected final static Logger logger = Logger
			.getLogger(ColumnAppendedBroadcaster.class.getName());
	private transient DataSetForApps dataSet;
	private transient EventListenerList listenerList;

	double[] newData;

	public ColumnAppendedBroadcaster() {
		super();
		listenerList = new EventListenerList();
	}

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	public EventListenerList getListenerList() {
		return listenerList;
	}

	public void dataSetChanged(DataSetEvent e) {
		// XXX this would prevent listeners from having a reference to the
		// original
		// data structure
		dataSet = new DataSetForApps(e.getDataSet());
	}

	/**
	 * implements ColumnAppendedListener
	 */
	public void addDataSetModifiedListener(ColumnAppendedListener l) {
		listenerList.add(ColumnAppendedListener.class, l);
	}

	/**
	 * removes an ColumnAppendedListener from the button
	 */
	public void removeDataSetModifiedListener(ColumnAppendedListener l) {
		listenerList.remove(ColumnAppendedListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireDataSetModifiedChanged() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("ShpToShp.fireDataSetModifiedChanged, Hi!!");
		}
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ColumnAppendedEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ColumnAppendedListener.class) {

				((ColumnAppendedListener) listeners[i + 1]).dataSetModified(e);
			}
		}
	}

	public void setNewData(double[] newData) {
		this.newData = newData;
		fireDataSetModifiedChanged();
	}

	public void setDataSet(DataSetForApps dataSet) {
		this.dataSet = dataSet;
	}

	public DataSetForApps getDataSet() {
		return dataSet;
	}

}
