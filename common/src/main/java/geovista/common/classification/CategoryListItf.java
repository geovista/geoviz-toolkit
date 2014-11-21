/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

public interface CategoryListItf {
	public void add(CategoryItf ctg);

	public CategoryItf getCategory(int index);

	public String getVariableName();

	public void setVariableName(String variableName);

	public String getClassifyMethodName();

	public void setClassifyMethodName(String classifyMethodName);

	public int getClassifyMethodType();

	public void setClassifyMethodType(int classifyMethodType);

	public int getNumOfDimension();

	public void setNumOfDimension(int numOfDimension);

	public int getNumOfCategories();
	// public List getCtgList()
}
