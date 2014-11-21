/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Display excentric labels around items in a visualization.
 * 
 * @author Jean-Daniel Fekete
 * 
 */
public interface ExcentricLabelClient {

	public int[] pickAll(Rectangle2D hitBox);

	public Shape getShapeAt(int id);

	public String getObservationLabel(int id);

	public void repaint();

}
