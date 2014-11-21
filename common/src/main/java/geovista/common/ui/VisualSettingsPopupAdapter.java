/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualSettingsPopupAdapter extends MouseAdapter {
	VisualSettingsPopupMenu popMenu;

	public VisualSettingsPopupAdapter(VisualSettingsPopupMenu popMenu) {
		super();
		this.popMenu = popMenu;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	// XXX add in methods to handle
}
