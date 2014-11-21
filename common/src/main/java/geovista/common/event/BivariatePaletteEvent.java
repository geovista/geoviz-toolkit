/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

import geovista.common.color.BivariatePalette;

public class BivariatePaletteEvent extends EventObject {

	private transient BivariatePalette bivariatePalette;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * pallet is indicated.
	 */
	public BivariatePaletteEvent(Object source,
			BivariatePalette bivariatePalette) {
		super(source);
		this.bivariatePalette = bivariatePalette;
	}

	public BivariatePalette getPalette() {
		return bivariatePalette;
	}

	public void setPallet(BivariatePalette bivariatePalette) {
		this.bivariatePalette = bivariatePalette;
	}
}
