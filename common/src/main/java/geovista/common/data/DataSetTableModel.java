/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Xiping Dai and Frank Hardisty
 */

package geovista.common.data;

import java.util.logging.Logger;

import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;

public class DataSetTableModel extends AbstractTableModel {

	DataSetForApps dataSet;

	final static Logger logger = Logger.getLogger(DataSetTableModel.class
			.getName());

	/**

	 */
	public DataSetTableModel(DataSetForApps dataSet) {
		this.dataSet = dataSet;
		listenerList = new EventListenerList();

	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {

		return false;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// noop, we don't allow editing
		logger.info("trying to set value, not permitted");

	}

	public int getColumnCount() {
		if (dataSet == null) {
			return 0;
		}
		return dataSet.getAttributeNamesOriginal().length;
	}

	@Override
	public String getColumnName(int columnIndex) {

		return dataSet.getAttributeNamesOriginal()[columnIndex];

	}

	public int getRowCount() {
		if (dataSet == null) {
			return 0;
		}
		return dataSet.getNumObservations();
	}

	// end TableModel events

	/**
	 * @return the listenerList
	 */
	public EventListenerList getListenerList() {
		return listenerList;
	}

	/**
	 * @param listenerList
	 *            the listenerList to set
	 */
	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		Object dataArray = dataSet.getDataObjectOriginal()[columnIndex + 1];
		Object datum = null;
		if (dataArray instanceof double[]) {
			double[] doubleArray = (double[]) dataArray;
			datum = new Double(doubleArray[rowIndex]);
		} else if (dataArray instanceof int[]) {
			int[] intArray = (int[]) dataArray;
			datum = new Integer(intArray[rowIndex]);
		} else if (dataArray instanceof String[]) {
			String[] stringArray = (String[]) dataArray;
			datum = new String(stringArray[rowIndex]);
		} else if (dataArray instanceof boolean[]) {
			boolean[] booleanArray = (boolean[]) dataArray;
			datum = new Boolean(booleanArray[rowIndex]);
		} else {
			logger.severe("datasetforaps, getcolumnclass, hit unknown array type, type = "
					+ dataArray.getClass());
		}

		return datum;

	}

}
