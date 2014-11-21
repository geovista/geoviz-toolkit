/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of SpatialExtentEvents.
 * 
 * This interface also enables "fireSpatialExtentChanged" methods in classes
 * that generate and broadcast SpatialExtentEvents.
 * 
 */
public interface SpatialExtentListener extends EventListener {

	public void spatialExtentChanged(SpatialExtentEvent e);

	public SpatialExtentEvent getSpatialExtentEvent();

}