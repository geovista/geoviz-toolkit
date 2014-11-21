/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification;

public class CategorizerQuantiles extends BasicCategorizer {

	public CategorizerQuantiles() {
		classifer = new ClassifierQuantiles();

	}

	/**
	 * set max and min value for each category in ctgList. Note: can also
	 * exploit local variable "end" in ClassifierQuantiles.classfy() end = max
	 * value of each class.
	 * 
	 * @param ctgList
	 * @param rawData
	 * @param classedData
	 */
	@Override
	protected void setCategorygetBoundary(CategoryList ctgList,
			double[] rawData, int[] classedData) {
		for (int i = 0; i < rawData.length; i++) {

			double value = rawData[i];
			int categoryID = classedData[i];
			Category ctg = ctgList.getCategoryByID(categoryID);
			if (ctg != null) {
				ctg.tryToSetMaxMin((float) value);
			}
		}
	}
}
