/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */

package geovista.collaboration;

public interface MessageReceiver {
	void receiveMessage(String name, String msg);
}
