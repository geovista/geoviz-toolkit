/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.common.color;

import java.awt.Color;

/**
 * 
 * @author jamesm
 */
public interface BivariatePalette {
	public static final int SEQUENTIAL_SEQUENTIAL = 1;
	public static final int SEQUENTIAL_DIVERGING = 2;
	public static final int SEQUENTIAL_QUALITATIVE = 3;
	public static final int QUALITATIVE_SEQUENTIAL = 4;
	public static final int DIVERGING_DIVERGING = 5;
	public static final int DIVERGING_SEQUENTIAL = 6;

	public String getName();

	public int getType();

	public Color[][] getColors(int cols, int rows);

}
