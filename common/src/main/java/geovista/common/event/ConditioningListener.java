/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of ConditioningEvents.
 * 
 * This interface also enables "fireConditioningChanged" methods in classes that
 * generate and broadcast ConditioningEvents.
 * 
 */
public interface ConditioningListener extends EventListener {

	public void conditioningChanged(ConditioningEvent e);

}