/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification;

import java.awt.Color;

public class BasicVisualInfo implements VisualInfo {
	Color color;
	CategoryItf category;

	public BasicVisualInfo(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/*
	 * public CategoryItf getCategory() { return category; }
	 * 
	 * public void setCategory(CategoryItf category) { this.category = category; }
	 */
}
