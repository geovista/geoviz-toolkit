/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

public class CategorizerRawQuantiles extends BasicCategorizer {

	public CategorizerRawQuantiles() {
		classifer = new ClassifierRawQuantiles();

	}

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
