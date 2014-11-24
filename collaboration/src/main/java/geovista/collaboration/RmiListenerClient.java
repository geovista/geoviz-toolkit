/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.awt.geom.Rectangle2D;

public interface RmiListenerClient {

	public void remoteSelectionChanged(String source, int[] selection);

	public void remoteSpatialExtentChanged(String source, Rectangle2D extent);

	public void remoteSubspaceChanged(String source, int[] subspace);

	public void remoteMessageReceived(String name, String msg);

}
