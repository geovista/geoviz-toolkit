/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Color;

public interface VisualSettingsPopupListener {

	public void setBackground(Color bg);// implemented by JComponent

	public Color getBackground();

	public void setIndicationColor(Color indColor);

	public Color getIndicationColor();

	public void setSelectionColor(Color selColor);

	public Color getSelectionColor();

	public void useSelectionFade(boolean selFade);

	public boolean isSelectionFade();

	public void useSelectionBlur(boolean selBlur);

	public boolean isSelectionBlur();

	public void useSelectionOutline(boolean selOutline);

	public boolean isSelectionOutline();

	public void useMultiIndication(boolean useMultiIndic);

	public void processCustomCheckBox(boolean value, String text);

	public int getSelectionLineWidth();

	public void setSelectionLineWidth(int width);

	// public void setSelectionFadeAmount(float fadeAmount)
	// public void setSelectionBlurAmount(float blurAmount)

}
