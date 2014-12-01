/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization.glyph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * ComboBoxDemo.java uses these additional files: images/Bird.gif images/Cat.gif
 * images/Dog.gif images/Rabbit.gif images/Pig.gif
 */
public class GlyphComboBox extends JPanel implements ActionListener {
	GlyphLabel picture;
	public JComboBox glyphList;
	public static int PLUS_FACTOR = 3;

	public GlyphComboBox() {
		super(new BorderLayout());

		String[] glyphStrings = { "Triangle", "Square", "Pentagon", "Hexagon",
				"Heptagon" };
		int[] glyphSides = { 3, 4, 5, 6, 7 };

		// Create the combo box, select the item at index 4.
		// Indices start at 0, so 4 specifies the hexagon.
		glyphList = new JComboBox(glyphStrings);
		glyphList.setSelectedIndex(3);
		glyphList.addActionListener(this);

		// Set up the picture.
		picture = new GlyphLabel();
		picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		int nSides = glyphSides[glyphList.getSelectedIndex()];
		updateLabel(glyphStrings[glyphList.getSelectedIndex()], nSides);
		picture.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// The preferred size is hard-coded to be the width of the
		// widest image and the height of the tallest image + the border.
		// A real program would compute this.
		picture.setPreferredSize(new Dimension(30, 30));
		picture.setBorder(BorderFactory.createLineBorder(Color.black));

		// Lay out the demo.
		add(glyphList, BorderLayout.NORTH);
		add(picture);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/** Listens to the combo box. */
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String glyphName = (String) cb.getSelectedItem();

		int nSides = cb.getSelectedIndex() + PLUS_FACTOR;
		updateLabel(glyphName, nSides);
		this.repaint();
	}

	protected void updateLabel(String name, int sides) {

		picture.setGlyph(sides);
		picture.setToolTipText(name.toLowerCase());
		picture.repaint();
		this.repaint();

		this.setSize(getWidth(), getHeight());

	}

	class GlyphLabel extends JLabel implements ComponentListener {
		NGon aGlyph;

		public GlyphLabel() {
			super();
			addComponentListener(this);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			aGlyph.draw(g2);
		}

		void setGlyph(int nSides) {
			aGlyph = new NGon(nSides);
			aGlyph.setLocation(new Point(getWidth(), getHeight()));
			Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
			aGlyph.setTargetArea(rect);
			this.repaint();

		}

		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void componentResized(ComponentEvent arg0) {
			// aGlyph.setLocation(new Point(getWidth() / 2, getHeight() / 2));
			aGlyph.setLocation(new Point(0, 0));
			Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
			aGlyph.setTargetArea(rect);
			this.repaint();

		}

		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("ComboBoxDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new GlyphComboBox();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

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
}