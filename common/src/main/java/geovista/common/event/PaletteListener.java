/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

public interface PaletteListener extends EventListener {
	public void paletteChanged(PaletteEvent e);
}
