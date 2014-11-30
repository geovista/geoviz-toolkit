/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import javax.swing.colorchooser.*;

public class GvSwatchChooserPanel extends JPanel {
	transient Dimension numSwatches;
	transient Dimension swatSize;
	transient Dimension gap;
	transient Color[] colors;
	transient JPanel[] swatches;

	public GvSwatchChooserPanel() {
		initValues();
		initColors();
		setLayout(new GridLayout(numSwatches.height, numSwatches.width,
				gap.width, gap.height));
		for (JPanel swatche : swatches) {
			this.add(swatche);
		}
	}

	protected void initValues() {
		numSwatches = new Dimension(31, 9);
		gap = new Dimension(1, 1);
		swatSize = new Dimension(30, 30);

	}

	protected void initColors() {
		int[] rawValues = initRawValues();
		int numColors = rawValues.length / 3;

		colors = new Color[numColors];
		swatches = new JPanel[numColors];
		for (int i = 0; i < numColors; i++) {
			colors[i] = new Color(rawValues[(i * 3)], rawValues[(i * 3) + 1],
					rawValues[(i * 3) + 2]);
			swatches[i] = new JPanel();
			swatches[i].setBackground(colors[i]);
			swatches[i].setPreferredSize(swatSize);
			swatches[i].setSize(swatSize);
			swatches[i].setVisible(true);
		}

	}

	private int[] initRawValues() {

		int[] rawValues = { 255, 207, 226, 255, 206, 210, 255, 208, 192, 255,
				208, 152, 255, 211, 45, 255, 224, 0, 249, 233, 0, 206, 245, 0,
				99, 255, 85, 0, 255, 185, 0, 255, 219, 41, 255, 245, 96, 249,
				255, 185, 236, 255, 197, 232, 255, 216, 227, 255, 231, 223,
				255, 248, 216, 255, 255, 202, 255, 255, 208, 241, 255, 207,
				226, 255, 206, 210, 255, 208, 192, 255, 208, 152, 255, 211, 45,
				255, 224, 0, 249, 233, 0, 206, 245, 0, 99, 255, 85, 0, 255,
				185, 0, 255, 219, 255, 153, 186, 255, 164, 167, 255, 154, 102,
				255, 166, 0, 255, 182, 0, 244, 196, 0, 219, 205, 0, 178, 216,
				0, 72, 230, 66, 0, 235, 157, 0, 236, 189, 0, 235, 224, 0, 230,
				255, 0, 218, 255, 124, 209, 255, 161, 201, 255, 202, 191, 255,
				238, 175, 255, 255, 155, 255, 255, 156, 218, 255, 153, 186,
				255, 164, 167, 255, 154, 102, 255, 166, 0, 255, 182, 0, 244,
				196, 0, 219, 205, 0, 178, 216, 0, 72, 230, 66, 0, 235, 157, 0,
				236, 189, 255, 107, 156, 255, 108, 119, 255, 120, 58, 255, 141,
				0, 241, 156, 0, 214, 169, 0, 190, 177, 0, 151, 188, 0, 27, 201,
				39, 0, 206, 134, 0, 206, 164, 0, 206, 199, 0, 205, 239, 0, 196,
				255, 0, 185, 255, 101, 175, 255, 176, 155, 255, 223, 138, 255,
				255, 123, 239, 255, 111, 195, 255, 107, 156, 255, 108, 119,
				255, 120, 58, 255, 141, 0, 241, 156, 0, 214, 169, 0, 190, 177,
				0, 151, 188, 0, 27, 201, 39, 0, 206, 134, 0, 206, 164, 255, 80,
				133, 255, 82, 96, 255, 95, 33, 232, 116, 0, 206, 132, 0, 183,
				143, 0, 161, 150, 0, 126, 160, 0, 0, 173, 0, 0, 177, 112, 0,
				178, 140, 0, 177, 174, 0, 176, 212, 0, 171, 252, 0, 162, 255,
				0, 149, 255, 150, 126, 255, 194, 112, 241, 227, 98, 210, 250,
				86, 168, 255, 80, 133, 255, 82, 96, 255, 95, 33, 232, 116, 0,
				206, 132, 0, 183, 143, 0, 161, 150, 0, 126, 160, 0, 0, 173, 0,
				0, 177, 112, 0, 178, 140, 229, 46, 110, 233, 48, 73, 223, 68,
				0, 194, 94, 0, 171, 108, 0, 152, 117, 0, 133, 124, 0, 102, 132,
				0, 0, 146, 0, 0, 149, 90, 0, 150, 116, 0, 150, 151, 0, 149,
				186, 0, 144, 224, 0, 135, 239, 0, 123, 244, 127, 100, 231, 168,
				85, 209, 197, 70, 182, 220, 53, 144, 229, 46, 110, 233, 48, 73,
				223, 68, 0, 194, 94, 0, 171, 108, 0, 152, 117, 0, 133, 124, 0,
				102, 132, 0, 0, 146, 0, 0, 149, 90, 0, 150, 116, 197, 0, 90,
				199, 0, 52, 189, 36, 0, 157, 73, 0, 138, 86, 0, 122, 93, 0,
				106, 99, 0, 80, 106, 0, 0, 119, 0, 0, 121, 70, 0, 121, 94, 0,
				122, 126, 0, 121, 159, 0, 117, 195, 0, 109, 209, 0, 97, 213,
				107, 73, 199, 143, 56, 180, 167, 38, 156, 187, 0, 122, 197, 0,
				90, 199, 0, 52, 189, 36, 0, 157, 73, 0, 138, 86, 0, 122, 93, 0,
				106, 99, 0, 80, 106, 0, 0, 119, 0, 0, 121, 70, 0, 121, 94, 168,
				0, 71, 169, 0, 39, 155, 0, 0, 117, 57, 5, 106, 64, 0, 91, 71,
				0, 80, 75, 0, 59, 81, 0, 0, 90, 0, 0, 94, 53, 0, 94, 73, 0, 94,
				102, 0, 92, 126, 0, 89, 156, 0, 84, 180, 0, 71, 187, 88, 43,
				170, 120, 15, 152, 139, 0, 134, 158, 0, 104, 168, 0, 71, 169,
				0, 39, 155, 0, 0, 117, 57, 5, 106, 64, 0, 91, 71, 0, 80, 75, 0,
				59, 81, 0, 0, 90, 0, 0, 94, 53, 0, 94, 73, 123, 0, 65, 128, 0,
				41, 127, 0, 0, 85, 39, 13, 71, 47, 22, 65, 50, 14, 57, 53, 15,
				41, 58, 0, 0, 66, 0, 0, 69, 37, 0, 70, 55, 0, 68, 77, 0, 65,
				90, 0, 62, 108, 0, 59, 128, 0, 47, 159, 70, 6, 136, 95, 0, 121,
				109, 0, 109, 123, 0, 87, 123, 0, 65, 128, 0, 41, 127, 0, 0, 85,
				39, 13, 71, 47, 22, 65, 50, 14, 57, 53, 15, 41, 58, 0, 0, 66,
				0, 0, 69, 37, 0, 70, 55, 84, 0, 53, 80, 0, 35, 82, 0, 19, 58,
				23, 7, 45, 31, 19, 41, 33, 17, 36, 34, 21, 24, 38, 0, 0, 41, 8,
				0, 44, 24, 0, 44, 36, 0, 42, 47, 0, 41, 58, 0, 41, 75, 0, 38,
				80, 0, 25, 126, 55, 0, 106, 74, 0, 94, 81, 0, 86, 83, 0, 69,
				84, 0, 53, 80, 0, 35, 82, 0, 19, 58, 23, 7, 45, 31, 19, 41, 33,
				17, 36, 34, 21, 24, 38, 0, 0, 41, 8, 0, 44, 24, 0, 44, 36 };
		return rawValues;
	}

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		GvSwatchChooserPanel swatPanel = new GvSwatchChooserPanel();

		app.getContentPane().add(swatPanel);
		app.pack();
		app.setVisible(true);

	}
}
