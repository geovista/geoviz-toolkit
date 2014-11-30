/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import geovista.symbolization.glyph.Glyph;
import geovista.symbolization.glyph.GlyphPicker;

public class GlyphSwatch extends JPanel implements MouseListener {

	protected boolean anchored;
	protected Glyph swatchGlyph;
	protected transient ColorRampPicker parent;
	protected transient boolean isEnd;
	protected transient ImageIcon iconBlack;
	protected transient ImageIcon iconWhite;
	protected transient TexturePaint texPaint;
	protected final static Logger logger = Logger.getLogger(GlyphSwatch.class
			.getName());

	public GlyphSwatch(ColorRampPicker parent, boolean anchored, boolean end) {
		makeImage();
		this.parent = parent;

		addMouseListener(this);
		setAnchored(anchored);
		isEnd = end;
	}

	public void makeImage() {
		Class cl = this.getClass();
		URL urlGif = cl.getResource("resources/anchorBlack.gif");
		ImageIcon icon = new ImageIcon(urlGif, "Anchors the color in a ramp");
		iconBlack = icon;

		URL urlGif2 = cl.getResource("resources/anchorWhite.gif");
		ImageIcon icon2 = new ImageIcon(urlGif2, "Anchors the color in a ramp");
		iconWhite = icon2;
	}

	public void setTexPaint(TexturePaint texPaint) {
		this.texPaint = texPaint;
	}

	public void setswatchGlyph(Glyph newGlyph) {
		swatchGlyph = newGlyph;

	}

	public Glyph getswatchGlyph() {
		return swatchGlyph;
	}

	public void setAnchored(boolean anchor) {
		anchored = anchor;
		if (anchor || isEnd) {
			setBorder(BorderFactory.createLoweredBevelBorder());
		} else {
			setBorder(BorderFactory.createRaisedBevelBorder());
		}
	}

	public boolean getAnchored() {
		return anchored;
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
			//this.swatch.setBorder(BorderFactory.createLineBorder(Color.black))
			// ;
			logger.finest("Mouse clicked (# of clicks: " + e.getClickCount()
					+ ")");
			GlyphPicker chooser = new GlyphPicker();

			Glyph newColor = chooser.showGvDialog(GlyphSwatch.this,
					"Pick a Glyph", getBackground());
			if (newColor != null) {
				this.requestFocus();
				setswatchGlyph(newColor);
				setAnchored(true);
				parent.swatchChanged();

			}// end if newColor
		} else if (e.getClickCount() == 1) {// toggle anchor state on single
			// click
			if (isEnd) {
				return;// if we are an end, we should always remain anchored!
			}
			if (anchored) {
				setAnchored(false);
				parent.swatchChanged(); // if we are now "unanchored", need to
				// update
			} else {
				setAnchored(true); // This won't affect other swatches
			}
		}// end if doubleclick
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());

		// adding support for textures
		if (texPaint != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(texPaint);
			g2.fillRect(0, 0, getWidth(), getHeight());
		} else {
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		if (getAnchored()) {
			int midX = getWidth() / 2;
			int midY = getHeight() / 2;

			Color c = getBackground();
			int colorValue = c.getRed() + c.getBlue() + c.getGreen();
			Image ico = null;
			if (colorValue > 200) { // pulled this out of my hat
				ico = iconBlack.getImage();
			} else {
				ico = iconWhite.getImage();
			}
			midX = midX - (ico.getWidth(this) / 2);
			midY = midY - (ico.getHeight(this) / 2);
			g.drawImage(ico, midX, midY, this);
		}
		swatchGlyph.draw((Graphics2D) g);
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
		GlyphSwatch swat = new GlyphSwatch(pick, true, false);
		app.getContentPane().add(swat);

		// app.getContentPane().add(swatchesPanel,BorderLayout.SOUTH);

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}

}
