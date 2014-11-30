/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import geovista.colorbrewer.ColorBrewer;
import geovista.colorbrewer.Palette;
import geovista.colorbrewer.UnivariatePalette;

public class ColorBrewerSwatch extends JPanel implements MouseListener {

	protected UnivariatePalette swatchPalette;
	protected transient ColorRampPicker parent;

	protected final static Logger logger = Logger
			.getLogger(ColorBrewerSwatch.class.getName());

	public ColorBrewerSwatch(ColorRampPicker parent, UnivariatePalette pal) {

		this.parent = parent;
		swatchPalette = pal;

		addMouseListener(this);

	}

	public void setPalette(UnivariatePalette newPalette) {
		swatchPalette = newPalette;
	}

	public Palette getSwatchPalette() {
		return swatchPalette;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 1) { // double or more clicks
			// this.swatch.setBorder(BorderFactory.createLineBorder(Color.black))
			// ;
			logger.finest("Mouse clicked (# of clicks: " + e.getClickCount()
					+ ")");
			GvColorChooser chooser = new GvColorChooser();
			Color newColor = chooser.showGvDialog(ColorBrewerSwatch.this,
					"Pick a Color", getBackground());
			if (newColor != null) {
				this.requestFocus();
				// setSwatchColor(newColor);
				setToolTipText("<html> " + "Red = " + newColor.getRed()
						+ "<br>" + "Green = " + newColor.getGreen() + "<br>"
						+ "Blue = " + newColor.getBlue());

				// setAnchored(true);
				parent.swatchChanged();
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {

		int nColors = 5;
		Color[] colors = swatchPalette.getColors(nColors);
		// only horizontal painting enabled for now...
		int patchWidth = getWidth() / nColors;
		for (int i = 0; i < nColors; i++) {
			g.setColor(colors[i]);
			g.fillRect(i * patchWidth, 0, patchWidth + 1, getHeight());
		}

	}

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) {
		JFrame app = new JFrame();
		// app.getContentPane().setLayout(new BorderLayout());
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ColorRampPicker pick = new ColorRampPicker();
		ColorBrewerSwatch swat = new ColorBrewerSwatch(pick,
				ColorBrewer
						.getPalette(ColorBrewer.BrewerNames.Blues));
		app.getContentPane().add(swat);

		// app.getContentPane().add(swatchesPanel,BorderLayout.SOUTH);

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}
}
