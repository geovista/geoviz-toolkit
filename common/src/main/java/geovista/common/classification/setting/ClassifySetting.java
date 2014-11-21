/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification.setting;

public interface ClassifySetting {
	public static final int TYPE_VARIABLE = 1; // change variable based on
												// which classification is make
	public static final int TYPE_BOUNDARY = 2; // change boundary of some
												// categories
	public static final int TYPE_NUM_OF_CATEGORY = 3;// change number of
														// categories
	public static final int TYPE_METHOD = 4;// change number of categories
	public static final int TYPE_COMBINED = 9;// any combination of
												// VARIABLE,BOUNDARY,NUM_OF_CATEGORY

	// Classification method names
	public final static String EQUAL_INTERVAL = "Equal Intervals";
	public final static String KMEANS = "KMeans";
	public final static String MODIFIED_QUANTILES = "ModifiedQuantiles";
	public final static String QUANTILES = "Quantiles";
	public final static String RAW_QUANTILES = "Raw Quantiles";
	public final static String STANDARD_DEVIATION = "Standard Deviation";
	public final static String SELF_DEFINED = "Self Defined";

	/*
	 * public static final int DIMENSION_1D=10; public static final int
	 * DIMENSION_2D=20; public static final int DIMENSION_ND=30;
	 */
	/**
	 * the type determine what change it is. It should be a vaule defined above
	 * 
	 * @return
	 */
	public int getType();

	public void setType(int type);
}
