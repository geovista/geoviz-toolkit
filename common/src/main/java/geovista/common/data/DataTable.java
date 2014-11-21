/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

/**
 * The contract data table sources must implement.
 */
public interface DataTable {

	public enum BaseDataType {
		Integer, Double, String, Boolean, Geometry
	}

	public Object getValue(int row, int colulmn);

	public ColumnData getColumn(int column);

}
