/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Component;
import java.awt.Shape;

public interface ShapeReporter {

	public Shape reportShape();

	public Component renderingComponent();

}
