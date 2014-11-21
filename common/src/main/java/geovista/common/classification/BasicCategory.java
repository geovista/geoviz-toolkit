/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Yunping Liu */
package geovista.common.classification;

import java.util.HashSet;
import java.util.Set;

public class BasicCategory implements CategoryItf {
	private Object ID; // id of the category
	private String name; // human readable name
	private Set memberIds; // id of members that belong to the cateogry
	private Range range;
	private int index = -1;// index of the categoy in the categoryList
	private VisualInfo visualInfo;
	private String variableName; // the name of the variable based on which

	// the classification is made and the
	// category is generated

	public BasicCategory(Object ID, Set memberIds, String name, Range range) {
		this.ID = ID;
		this.memberIds = memberIds;
		this.name = name;
		this.range = range;
	}

	public BasicCategory(Object ID, Set memberIds, Range range) {
		this(ID, memberIds, null, range);
	}

	public BasicCategory() {
		this(null, new HashSet(), null, new Range());
	}

	public Object getID() {
		return ID;
	}

	public void setID(Object ID) {
		this.ID = ID;
	}

	public HashSet getMemberIds() {
		return (HashSet) memberIds;
	}

	public void setMemberIds(Set memberIds) {
		this.memberIds = memberIds;
	}

	public int getMemberIdSize() {
		if (getMemberIds() != null) {
			return getMemberIds().size();
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public VisualInfo getVisualInfo() {
		return visualInfo;
	}

	public void setVisualInfo(VisualInfo visualInfo) {
		this.visualInfo = visualInfo;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
