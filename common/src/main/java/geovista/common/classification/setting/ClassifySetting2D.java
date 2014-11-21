/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification.setting;

import geovista.common.classification.Range;

public class ClassifySetting2D extends AbstractClassifySetting {
	private String xVariableName, yVariableName;
	private String xClassifier, yClassifier;
	private int xNumOfCategory, yNumOfCategory;
	private Range[] xBound, yBound;

	public ClassifySetting2D() {
	}

	public ClassifySetting2D(String xVariableName, int xNumOfCategory,
			String xClassifier, Range[] xBound, String yVariableName,
			int yNumOfCategory, String yClassifier, Range[] yBound) {
		this.xVariableName = xVariableName;
		this.xClassifier = xClassifier;
		this.xNumOfCategory = xNumOfCategory;
		this.xBound = xBound;
		this.yVariableName = yVariableName;
		this.yClassifier = yClassifier;
		this.yNumOfCategory = yNumOfCategory;
		this.yBound = yBound;
	}

	public String getxVariableName() {
		return xVariableName;
	}

	public void setxVariableName(String xVariableName) {
		this.xVariableName = xVariableName;
	}

	public String getxClassifier() {
		return xClassifier;
	}

	public void setxClassifier(String xClassifier) {
		this.xClassifier = xClassifier;
	}

	public int getxNumOfCategory() {
		return xNumOfCategory;
	}

	public void setxNumOfCategory(int xNumOfCategory) {
		this.xNumOfCategory = xNumOfCategory;
	}

	public Range[] getxBound() {
		return xBound;
	}

	public void setxBound(Range[] xBound) {
		this.xBound = xBound;
	}

	public String getyVariableName() {
		return yVariableName;
	}

	public void setyVariableName(String yVariableName) {
		this.yVariableName = yVariableName;
	}

	public String getyClassifier() {
		return yClassifier;
	}

	public void setyClassifier(String yClassifier) {
		this.yClassifier = yClassifier;
	}

	public int getyNumOfCategory() {
		return yNumOfCategory;
	}

	public void setyNumOfCategory(int yNumOfCategory) {
		this.yNumOfCategory = yNumOfCategory;
	}

	public Range[] getyBound() {
		return yBound;
	}

	public void setyBound(Range[] yBound) {
		this.yBound = yBound;
	}
}
