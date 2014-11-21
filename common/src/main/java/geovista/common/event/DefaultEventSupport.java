/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.event;

import javax.swing.event.EventListenerList;

public class DefaultEventSupport {
	protected EventListenerList listenerList = new EventListenerList();

	/***************************************************************************
	 * selection
	 **************************************************************************/
	/**
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {

		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component
	 */
	public void removeSelectionListener(SelectionListener l) {

		listenerList.remove(SelectionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see javax.swing.event.EventListenerList
	 */
	public void fireSelectionChanged(int[] newSelection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}

				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		} // next i
	}
}
