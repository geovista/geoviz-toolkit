/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An TextEvent signals that some kind of text has been selected.
 * 
 */
public class TextEvent extends EventObject {

	private String text;

	/**
	 * The constructor is the same as that for EventObject, except that the text
	 * values are indicated.
	 */

	public TextEvent(Object source, String text) {
		super(source);
		this.text = text;
	}

	// begin accessors
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	// end accessors

}