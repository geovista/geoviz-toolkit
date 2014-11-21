/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */
package geovista.common.classification;

public interface MultiVariateClassifier {

	public static final int NULL_CLASS = -1;

	public int[] multiVariateClassify(Object[] data, int numClasses);

}