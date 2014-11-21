/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Yunping Liu 
 *
 * Contain some categories.  There are 2 cases
    1. conotain cateogries for only one dimension (variable). That is, the classification is made independently only on the variable
    2. contain categories for multiple dimensions (variables). That is, the classification is made based on these variables.
 * @author: yunping liu
 * @dates: Dec 7, 2004$
 * 
 */
package geovista.common.classification;

// import java.awt.*;
import java.util.List;
import java.util.Vector;

public class BasicCategoryList implements CategoryListItf {
	protected Vector categories;
	protected String classifyMethodName;
	protected int classifyMethodType;
	protected int numOfDimension;
	protected String variableName; // the name of the variable based on which

	// the classification is made

	/** Creates a new instance of JDMCategoryList */
	public BasicCategoryList(Vector categories, String classifyMethodName,
			int classifyMethodType, int numOfDimension) {
		if (categories != null) {
			this.categories = categories;
		} else {
			this.categories = new Vector();
		}
		this.classifyMethodName = classifyMethodName;
		this.classifyMethodType = classifyMethodType;
		this.numOfDimension = numOfDimension;
	}

	public BasicCategoryList(String classifyMethodName, int classifyMethodType,
			int numOfDimension) {
		this(null, classifyMethodName, classifyMethodType, numOfDimension);
	}

	public void add(CategoryItf category) {
		categories.add(category);
	}

	public void clearAll() {
		if (getNumOfCategories() > 0) {
			categories.clear();
		}
	}

	public CategoryItf getCategory(int index) {
		return (CategoryItf) categories.get(index);
	}

	public String getClassifyMethodName() {
		return classifyMethodName;
	}

	public int getClassifyMethodType() {
		return classifyMethodType;
	}

	public int getNumOfCategories() {
		return categories.size();
	}

	public int getNumOfDimension() {
		return numOfDimension;
	}

	public void setClassifyMethodName(String classifyMethodName) {
		this.classifyMethodName = classifyMethodName;
	}

	public void setClassifyMethodType(int classifyMethodType) {
		this.classifyMethodType = classifyMethodType;
	}

	public void setNumOfDimension(int numOfDimension) {
		this.numOfDimension = numOfDimension;
	}

	public List getCategoryList() {
		return categories;
	}

	public void setCategoryList(List categoryList) {
		categories = (Vector) categoryList;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
