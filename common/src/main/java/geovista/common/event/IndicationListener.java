/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of IndicationEvents.
 * 
 * This interface also enables "fireIndicationChanged" methods in classes that
 * generate and broadcast IndicationEvents.
 * 
 */
public interface IndicationListener extends EventListener {

	public void indicationChanged(IndicationEvent e);

}