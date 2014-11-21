/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */

package geovista.common.classification.setting;

public class AbstractClassifySetting implements ClassifySetting {
	/*
	 * a value of VARIABLE,BOUNDARY,NUM_OF_CATEGORY,COMBINED .... With a type
	 * other than COMBINED(e.g.:VARIABLE) , u should only set one attribute
	 * (e.g. : VARIABLE)
	 */
	int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
