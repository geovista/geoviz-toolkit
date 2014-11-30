/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Shape;

public interface ShapeSymbolizer {

	public static final Color DEFAULT_NULL_COLOR = Color.darkGray;

	public Shape[] symbolize(int numClasses);

	public int getNumClasses();

}