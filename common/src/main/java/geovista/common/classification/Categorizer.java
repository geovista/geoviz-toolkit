/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification;

public interface Categorizer extends DescribedClassifier {
	public CategoryList categorize(double[] rawData, int numClasses);

	public CategoryList categorize(double[] rawData, int[] classedData,
			int numClasses);

}
