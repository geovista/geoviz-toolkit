/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization.glyph;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Paints a glyph on itself and allows resizing.
 */
public class GlyphSwatch extends JPanel implements ComponentListener {
	Glyph glyph;
	int size;
	static Glyph DEFAULT_glyph = new NGon(3);
	static int DEFAULT_SIZE = 200;
	final static Logger logger = Logger.getLogger(GlyphSwatch.class.getName());

	public GlyphSwatch() {
		glyph = DEFAULT_glyph;
		glyph = new NGon(3);
		size = DEFAULT_SIZE;
		init();

	}

	public GlyphSwatch(Glyph glyph, int size) {
		this.glyph = glyph;
		this.size = size;
		init();
	}

	private void init() {
		glyph.setTargetArea(new Rectangle(0, 0, DEFAULT_SIZE, DEFAULT_SIZE));
		addComponentListener(this);
	}

	// void setGlyph(int nSides) {
	// glyph = new NGon(nSides);
	// glyph.setLocation(new Point(getWidth(), getHeight()));
	// Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
	// glyph.setTargetArea(rect);
	// this.repaint();
	//
	// }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		glyph.draw(g2);

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Glyph button test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		GlyphSwatch gButt = new GlyphSwatch();

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
		// aGlyph.setLocation(new Point(getWidth() / 2, getHeight() / 2));
		// glyph.setLocation(new Point(0, 0));
		// Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
		// glyph.setTargetArea(rect);
		// this.repaint();

	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setGlyphSize(int size) {
		this.size = size;
		glyph.setTargetArea(new Rectangle(0, 0, size, size));
		logger.info("glyph size = " + size);
		NGon gon = (NGon) glyph;
		Shape drawShape = gon.getDrawPath();
		logger.info("bounds = " + drawShape.getBounds());
		this.repaint();

	}

	public int getGlyphSize() {
		return size;
	}

	public Glyph getGlyph() {
		return glyph;
	}

	public void setGlyph(Glyph glyph) {
		this.glyph = glyph;
	}
}
