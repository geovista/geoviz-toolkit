/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization.glyph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * manages a set of GlyphSwatchs and communications with other components
 */
public class GlyphSizePicker extends JPanel implements ComponentListener {

	ArrayList<GlyphSwatch> gyphButts;
	int minSize;
	int maxSize;
	int nSymbols;

	public static final int DEFAULT_NUM_SWATCHES = 5;

	public static final int DEFAULT_LOW_SIZE = 10;
	// grey
	public static final int DEFAULT_HIGH_SIZE = 50;
	final static Logger logger = Logger.getLogger(GlyphSizePicker.class
			.getName());

	public GlyphSizePicker() {
		minSize = DEFAULT_LOW_SIZE;
		maxSize = DEFAULT_HIGH_SIZE;
		nSymbols = DEFAULT_NUM_SWATCHES;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		gyphButts = new ArrayList<GlyphSwatch>();
		for (int i = 0; i < DEFAULT_NUM_SWATCHES; i++) {
			GlyphSwatch gBut = new GlyphSwatch();
			gBut.setPreferredSize(new Dimension(DEFAULT_HIGH_SIZE,
					DEFAULT_HIGH_SIZE));
			gBut.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			gyphButts.add(gBut);
			this.add(gBut);
		}
		rampSizes();
		// setPreferredSize(new Dimension(150, 100));

	}

	private void rampSizes() {
		int range = maxSize - minSize;
		int step = range / nSymbols;
		for (int i = 0; i < gyphButts.size(); i++) {
			int size = (step * i) + minSize;

			logger.info("size = " + size);
			gyphButts.get(i).setGlyphSize(size);

		}

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Glyph size picker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		GlyphSizePicker gButt = new GlyphSizePicker();

		frame.add(gButt);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent arg0) {

	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setNSides(int sides) {
		for (GlyphSwatch gButt : gyphButts) {
			int size = gButt.getGlyphSize();
			gButt.setGlyph(new NGon(sides));
			gButt.setGlyphSize(size);
		}

	}
}
