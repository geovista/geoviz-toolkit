/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of AuxiliaryDataSetEvents.
 * 
 * 
 */
public interface AuxiliaryDataSetListener extends EventListener {
	/**
	 * Addded instead of changed fits the semantics better.
	 * 
	 * 
	 */

	public void dataSetAdded(AuxiliaryDataSetEvent e);

}
