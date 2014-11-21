/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Bonan Li */

package geovista.common.event;

import java.util.EventListener;

public interface layerChangeListener extends EventListener {
	public void layerChanged(layerChangeEvent e);
}