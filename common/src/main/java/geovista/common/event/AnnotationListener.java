/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of AnnotationEvents.
 * 
 * This interface also enables "fireAnnotationChanged" methods in classes that
 * generate and broadcast AnnotationEvents.
 * 
 */
public interface AnnotationListener extends EventListener {

	public void annotationChanged(AnnotationEvent e);

}