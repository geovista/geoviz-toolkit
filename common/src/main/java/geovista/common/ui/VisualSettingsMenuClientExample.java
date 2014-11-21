/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VisualSettingsMenuClientExample extends JPanel implements
		VisualSettingsPopupListener {

	VisualSettingsPopupMenu popMenu;
	String textCheckBoxText = "Test this checkbox?";
	final static Logger logger = Logger
			.getLogger(VisualSettingsMenuClientExample.class.getName());

	public VisualSettingsMenuClientExample() {

		popMenu = new VisualSettingsPopupMenu(this);
		popMenu.addCheckBoxItem(textCheckBoxText, true);

		MouseAdapter listener = new VisualSettingsPopupAdapter(popMenu);
		popMenu.addMouseListener(listener);
		addMouseListener(listener);// this component lets the adapter know when
		// to show the popup

		setMinimumSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(200, 200));
	}

	public void setIndicationColor(Color indColor) {
		// TODO Auto-generated method stub

	}

	public void setSelectionColor(Color selColor) {
		// TODO Auto-generated method stub

	}

	public void useSelectionBlur(boolean selBlur) {
		logger.info("got :" + selBlur);

	}

	public void useSelectionFade(boolean selFade) {
		// TODO Auto-generated method stub

	}

	public Color getIndicationColor() {
		// TODO Auto-generated method stub
		return Color.red;
	}

	public Color getSelectionColor() {
		// TODO Auto-generated method stub
		return Color.YELLOW;
	}

	public boolean isSelectionBlur() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSelectionFade() {
		// TODO Auto-generated method stub
		return false;
	}

	public void useMultiIndication(boolean useMultiIndic) {
		// TODO Auto-generated method stub

	}

	public void processCustomCheckBox(boolean value, String text) {
		if (text.equals(textCheckBoxText)) {
			logger.info("got custom box, value = " + value);
		}

	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.add(new VisualSettingsMenuClientExample());
		app.pack();
		app.setVisible(true);
	}

	public boolean isSelectionOutline() {
		// TODO Auto-generated method stub
		return false;
	}

	public void useSelectionOutline(boolean selOutline) {
		// TODO Auto-generated method stub

	}

	public void useSimpleHighlighting(boolean simpleHighlighting) {
		// TODO Auto-generated method stub

	}

	public boolean isSimpleHighlighing() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getSelectionLineWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSelectionLineWidth(int width) {
		// TODO Auto-generated method stub

	}

}
