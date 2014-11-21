/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

public abstract class BasicCategorizer implements Categorizer {
	// protected String shortName = "BasicCategorizer"; //Must be overide by
	// subclass
	// protected String fullName = "BasicCategorizer";
	protected DescribedClassifier classifer;

	public CategoryList categorize(double[] rawData, int numClasses) {
		int[] classedData = classify(rawData, numClasses);
		return this.categorize(rawData, classedData, numClasses);
		/*
		 * CategoryList ctgList=new CategoryList(classedData,numClasses);
		 * this.findMaxMin(ctgList,rawData,classedData); return ctgList;
		 */
	}

	public CategoryList categorize(double[] rawData, int[] classedData,
			int numClasses) {

		CategoryList ctgList = new CategoryList(classedData, numClasses);
		setCategorygetBoundary(ctgList, rawData, classedData);
		return ctgList;
	}

	/**
	 * set max and min value for each category in ctgList.
	 * 
	 * @param ctgList
	 * @param rawData
	 * @param classedData
	 */
	protected abstract void setCategorygetBoundary(CategoryList ctgList,
			double[] rawData, int[] classedData);

	/**
	 * This mehtod is out-of-date. It is just for keeping compatible with old
	 * version code Use categorize() instead
	 * 
	 * @param data
	 * @param numClasses
	 * @return
	 */
	public int[] classify(double[] data, int numClasses) {
		return classifer.classify(data, numClasses);
	}

	public String getShortName() {
		return classifer.getShortName();
	}

	public String getFullName() {
		return classifer.getFullName();
	}
}
