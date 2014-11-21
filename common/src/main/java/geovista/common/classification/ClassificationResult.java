/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details. */

package geovista.common.classification;

public interface ClassificationResult {

	public String[] getAttNames();

	public double[] getBoundaryX();

	public double[] getBoundaryY();

	public int[] getClassificationX();

	public int[] getClassificationY();

	public String getClassifierX();

	public String getClassifierY();

	public int getNumberOfClassX();

	public int getNumberOfClassY();

	// perhaps should not be here...
	public int getNumberOfAxis();

	// member count
	public int getRowCount();

	public String getAttX();

	public String getAttY();

	public double[] getDataX();

	public double[] getDataY();
}
