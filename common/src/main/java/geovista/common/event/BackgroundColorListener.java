/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of BackgroundColorEvents.
 * 
 * This interface also enables "fireBackgroundColorChanged" methods in classes
 * that generate and broadcast BackgroundColorEvents.
 * 
 */
public interface BackgroundColorListener extends EventListener {

	public void backgroundChanged(BackgroundColorEvent e);

}