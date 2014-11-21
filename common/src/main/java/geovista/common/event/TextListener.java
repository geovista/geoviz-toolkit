/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of TextEvents.
 * 
 * This interface also enables "fireTextChanged" methods in classes that
 * generate and broadcast TextEvents.
 * 
 */
public interface TextListener extends EventListener {

	public void textChanged(TextEvent e);

}