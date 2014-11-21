/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

import java.util.HashSet;
import java.util.Set;

public interface CategoryItf {
	/**
	 * 
	 * @return a ID. getID().toString() must also unique
	 */
	public Object getID();

	public void setID(Object ID);

	public HashSet getMemberIds();

	public void setMemberIds(Set memberIds);

	public int getMemberIdSize();

	public Range getRange();

	public void setRange(Range range);

	public String getName();

	public void setName(String name);

	public String getVariableName();

	public void setVariableName(String variableName);

	public VisualInfo getVisualInfo();

	public void setVisualInfo(VisualInfo vif);

}
