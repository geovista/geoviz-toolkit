/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: James Macgill */
package geovista.common.event;

import java.util.EventObject;

/**
 * TODO: The class is it stands is non-functional as it provides no get methods
 * for the events param values. As I do not know the true names for these I have
 * ommited them for now. This class exists to allow the src to compile it should
 * be replaced with a funtional class soon.
 * 
 * @author jamesm
 */
public class DimensionEvent extends EventObject {

	/** Creates a new instance of DimensionEvent */
	public DimensionEvent(Object src, int var, String name) {
		super(src);
	}

}
