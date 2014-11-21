/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification.setting;

import geovista.common.classification.Range;

public class ClassifySetting1D extends AbstractClassifySetting {
	public static final String DIMENSION_NAME_X = "X";
	public static final String DIMENSION_NAME_Y = "Y";

	private String variableName;
	private String dimensionName;
	private String classifier;
	private int numOfCategory;
	private Range[] bound;

	public ClassifySetting1D() {
	}

	public ClassifySetting1D(String variableName, int numOfCategory,
			String classifier, Range[] bound) {
		this.variableName = variableName;
		this.numOfCategory = numOfCategory;
		this.classifier = classifier;
		this.bound = bound;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public int getNumOfCategory() {
		return numOfCategory;
	}

	public void setNumOfCategory(int numOfCategory) {
		this.numOfCategory = numOfCategory;
	}

	public Range[] getBound() {
		return bound;
	}

	public void setBound(Range[] bound) {
		this.bound = bound;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public String getDimensionName() {
		return dimensionName;
	}

	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

}
