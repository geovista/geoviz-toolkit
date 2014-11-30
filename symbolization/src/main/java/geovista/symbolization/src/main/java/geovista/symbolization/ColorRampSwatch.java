/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorRampSwatch extends JPanel implements MouseListener {

	protected boolean anchored;
	protected Color swatchColor;
	protected transient ColorRampPicker parent;
	protected transient boolean isEnd;
	protected static ImageIcon iconBlack;
	protected static ImageIcon iconWhite;
	protected transient TexturePaint texPaint;
	protected final static Logger logger = Logger
			.getLogger(ColorRampSwatch.class.getName());

	public ColorRampSwatch(ColorRampPicker parent, boolean anchored, boolean end) {

		this.parent = parent;
		swatchColor = Color.white;
		setBackground(swatchColor);
		addMouseListener(this);
		setAnchored(anchored);
		isEnd = end;
	}

	public void setTexPaint(TexturePaint texPaint) {
		this.texPaint = texPaint;
	}

	public void setSwatchColor(Color newColor) {
		swatchColor = newColor;
		setBackground(newColor);
	}

	public Color getSwatchColor() {
		return swatchColor;
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
			// this.swatch.setBorder(BorderFactory.createLineBorder(Color.black))
			// ;
			logger.finest("Mouse clicked (# of clicks: " + e.getClickCount()
					+ ")");
			GvColorChooser chooser = new GvColorChooser();
			Color newColor = chooser.showGvDialog(ColorRampSwatch.this,
					"Pick a Color", getBackground());
			if (newColor != null) {
				this.requestFocus();
				setSwatchColor(newColor);
				setToolTipText("<html> " + "Red = " + newColor.getRed()
						+ "<br>" + "Green = " + newColor.getGreen() + "<br>"
						+ "Blue = " + newColor.getBlue());

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
		ColorRampSwatch swat = new ColorRampSwatch(pick, true, false);
		app.getContentPane().add(swat);

		// app.getContentPane().add(swatchesPanel,BorderLayout.SOUTH);

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}

}
