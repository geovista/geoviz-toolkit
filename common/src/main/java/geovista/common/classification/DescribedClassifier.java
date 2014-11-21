/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

public interface DescribedClassifier extends Classifier {
	/**
	 * Returns descriptive names.
	 */

	public String getFullName();

	public String getShortName();
}
