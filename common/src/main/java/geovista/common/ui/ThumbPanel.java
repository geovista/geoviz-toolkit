/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;

/**
 * ThumbPanel is used in FoldupPanel to toggle its state.
 * 
 * Currently, the component only "folds up" vertically.
 */
public class ThumbPanel extends JPanel implements MouseListener {

	public static final int SIZE_WIDTH_UNFOLDED = 10;
	public static final int SIZE_HEIGHT_FOLDED = 8;
	public static final int SIZE_WIDTH_FOLDED = 60;
	private static final int heightUnfolded = 60;
	transient boolean folded = false;
	private static final Dimension unfoldedSize = new Dimension(
			ThumbPanel.SIZE_WIDTH_UNFOLDED, heightUnfolded);
	private static final Dimension foldedSize = new Dimension(
			ThumbPanel.SIZE_WIDTH_FOLDED, ThumbPanel.SIZE_HEIGHT_FOLDED);

	/**
	 * null ctr
	 */
	public ThumbPanel() {
		super();
		setMinimumSize(unfoldedSize);
		setPreferredSize(unfoldedSize);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		addMouseListener(this);
	}

	// start mouse events
	/**
	 * noop
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * noop
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * noop
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * noop
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * toggle our state and let any listening classes know.
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {

		if (folded) {
			folded = false;
			setMinimumSize(unfoldedSize);
			setPreferredSize(unfoldedSize);
			setMaximumSize(unfoldedSize);
			fireActionPerformed("unfold");
		} else {
			folded = true;
			setMinimumSize(foldedSize);
			setPreferredSize(foldedSize);
			setMaximumSize(foldedSize);
			fireActionPerformed("fold");
		}
	}

	// end mouse events

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireActionPerformed(String command) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							command);
				}
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (folded) {
			Polygon tri = new Polygon();
			tri.addPoint(2, 2);
			tri.addPoint(2, 8);
			tri.addPoint(6, 5);
			g.setColor(Color.white);
			g.fillPolygon(tri);
			g.setColor(Color.black);
			g.drawPolygon(tri);
		} else { // not folded
			Polygon tri = new Polygon();
			tri.addPoint(2, 2);
			tri.addPoint(8, 6);
			tri.addPoint(2, 10);
			Graphics2D g2 = (Graphics2D) g;
			double down = 10;
			double accross = 0;
			for (int i = 0; i < 3; i++) {
				g2.setColor(Color.white);
				g2.fillPolygon(tri);
				g2.setColor(Color.black);
				g2.drawPolygon(tri);
				g2.translate(accross, down);
			}
			g2.translate(accross * -3, down * -3);
		} // end if
	}// end method
}