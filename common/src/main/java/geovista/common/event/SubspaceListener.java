/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of SubspaceEvents.
 * 
 * This interface also enables "fireSubspaceChanged" methods in classes that
 * generate and broadcast SubspaceEvents.
 * 
 */
public interface SubspaceListener extends EventListener {

	public void subspaceChanged(SubspaceEvent e);

}