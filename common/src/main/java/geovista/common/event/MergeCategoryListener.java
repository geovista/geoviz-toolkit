/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of IndicationEvents.
 * 
 * This interface also enables "fireClassColorChanged" methods in classes that
 * generate and broadcast ClassColorEvents.
 * 
 */
public interface MergeCategoryListener extends EventListener {

	public void mergeCategoryChanged(MergeCategoryEvent e);

}