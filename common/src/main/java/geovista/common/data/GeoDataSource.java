/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

/**
 * Interface implemented by sources that can return a DataSetForApps
 */
public interface GeoDataSource {

	public DataSetForApps getDataForApps();
}