/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

public interface Category2D extends CategoryItf {
	public Range getXRange();

	public Range getYRange();

	public void setXRange(Range r);

	public void setYRange(Range r);

	// will call getVariableName() to get value
	public String getXVariableName();

	public String getYVariableName();

	// will call setVariableName() to set the value
	public void setXVariableName(String vn);

	public void setYVariableName(String vn);

}
