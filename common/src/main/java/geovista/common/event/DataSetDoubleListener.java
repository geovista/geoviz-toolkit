/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of DataSetDoubleEvents.
 * 
 * This interface also enables "fireDataSetDoubleChanged" methods in classes
 * that generate and broadcast DataSetDoubleEvents.
 * 
 */
public interface DataSetDoubleListener extends EventListener {

	public void dataSetDoubleChanged(DataSetDoubleEvent e);

}
