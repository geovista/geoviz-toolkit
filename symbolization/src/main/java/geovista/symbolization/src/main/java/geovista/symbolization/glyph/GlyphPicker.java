/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization.glyph;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Paint a glyph;
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class GlyphPicker extends JDialog implements ActionListener {
	private JPanel myPanel = null;
	private JButton yesButton = null;
	private JButton noButton = null;
	private boolean answer = false;
	private Glyph glyph;

	public boolean getAnswer() {
		return answer;
	}

	public GlyphPicker(JFrame frame, boolean modal, String myMessage) {
		super(frame, modal);
		myPanel = new JPanel();
		getContentPane().add(myPanel);
		myPanel.add(new JLabel(myMessage));
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);
		myPanel.add(yesButton);
		noButton = new JButton("No");
		noButton.addActionListener(this);
		myPanel.add(noButton);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	public GlyphPicker() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public Glyph showGvDialog(Component component, String title,
			Color initialColor) {
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		if (yesButton == e.getSource()) {
			System.err.println("User chose yes.");
			answer = true;
			setVisible(false);
		} else if (noButton == e.getSource()) {
			System.err.println("User chose no.");
			answer = false;
			setVisible(false);
		}
	}

	public static void main(String[] args) {
		GlyphPicker picker = new GlyphPicker(null, false, "title");
		picker.setVisible(true);
	}

	public Glyph getGlyph() {
		return glyph;
	}

	public void setGlyph(Glyph glyph) {
		this.glyph = glyph;
	}

}
