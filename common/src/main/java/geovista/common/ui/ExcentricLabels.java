/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jean-Daniel Fekete and Frank Hardisty */

package geovista.common.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Display excentric labels around items in a visualization.
 * 
 * @author Jean-Daniel Fekete
 * 
 */
public class ExcentricLabels extends MouseAdapter implements Comparator,
		MouseMotionListener {
	Timer insideTimer;
	int[] hits;// changed from IntColumn to int[] by fah
	Rectangle2D.Double cursorBounds;
	int centerX;
	int centerY;
	int focusSize = 50;
	Point2D.Double[] itemPosition;
	Point2D.Double[] linkPosition;
	Point2D.Double[] labelPosition;
	Point2D.Double[] left;
	int leftCount;
	Point2D.Double[] right;
	int rightCount;
	boolean xStable;
	boolean yStable;
	JComponent vpanel;
	ExcentricLabelClient client;
	boolean visible;
	int gap = 5;
	int maxLabels;
	int labelCount;
	int threshold = 20;
	boolean opaque;
	Color backgroundColor = Color.WHITE;

	/**
	 * Constructor for ExcentricLabels.
	 */
	public ExcentricLabels() {
		cursorBounds = new Rectangle2D.Double(0, 0, focusSize, focusSize);

		insideTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
			}
		});
		insideTimer.setRepeats(false);
		setMaxLabels(20);
	}

	/**
	 * @see javax.swing.JToolTip#setComponent(JComponent)
	 */
	public void setComponent(JComponent c) {
		if (c instanceof ExcentricLabelClient) {
			if (vpanel == c) {
				return;
			}
			if (vpanel != null) {
				vpanel.removeMouseListener(this);
				vpanel.removeMouseMotionListener(this);
				// visualization = null;
				client = null;

			}
			vpanel = c;
			if (vpanel != null) {
				// visualization = vpanel.getVisualization();
				client = (ExcentricLabelClient) c;
				vpanel.addMouseListener(this);
				vpanel.addMouseMotionListener(this);
			}
		} else {
			throw new RuntimeException(
					"Invalid component type for ExcentricLabels");
		}
	}

	public void paint(Graphics2D graphics) {
		// if (visualization == null || !visible)
		if (client == null || !visible) { // added fah 18 march 2003
			return;
		}
		FontMetrics fm = graphics.getFontMetrics();
		computeExcentricLabels(graphics);

		Line2D.Double line = new Line2D.Double();
		for (int i = 0; i < labelCount; i++) {
			int hit = hits[i];
			String lab = client.getObservationLabel(hit);// added fah 18
			// march 2003
			// visualization.getLabel(hits.get(i));
			if (lab == null) {
				lab = "item" + hits[i];
			}

			Point2D.Double pos = labelPosition[i];
			if (opaque) {
				graphics.setColor(backgroundColor);
				Rectangle2D sb = fm.getStringBounds(lab, graphics);
				graphics.fillRect((int) (pos.x + sb.getX() - 2), (int) (pos.y
						+ sb.getY() - 2), (int) sb.getWidth() + 4, (int) sb
						.getHeight() + 4);
			}
			graphics.setColor(Color.BLACK);
			graphics.drawString(lab, (int) (pos.x), (int) (pos.y));
			line.setLine(itemPosition[i], linkPosition[i]);
			graphics.setXORMode(Color.white);
			graphics.draw(line);
			graphics.setPaintMode();
		}
		graphics.setStroke(new BasicStroke(2f));
		graphics.setColor(new Color(255, 0, 0, 128));
		graphics.draw(cursorBounds);
	}

	protected void computeExcentricLabels(Graphics2D graphics) {
		if (client == null) {
			return;
		}

		cursorBounds.x = centerX - focusSize / 2;
		cursorBounds.y = centerY - focusSize / 2;

		// if (hits == null) hits = new IntColumn("pickAll");

		hits = client.pickAll(cursorBounds);
		// hits = visualization.pickAll(cursorBounds, bounds, hits);

		labelCount = Math.min(maxLabels, hits.length);
		if (labelCount != 0) {
			computeItemPositions();
			projectLeftRight(graphics);
		}
	}

	protected void computeItemPositions() {
		Rectangle2D.Double inter = new Rectangle2D.Double();

		for (int i = 0; i < labelCount; i++) {
			// Rectangle2D rect =
			// visualization.getShapeAt(hits.get(i)).getBounds2D();
			int hit = hits[i];
			Rectangle2D rect = client.getShapeAt(hit).getBounds2D();
			Rectangle2D.intersect(cursorBounds, rect, inter);
			itemPosition[i].setLocation(inter.getCenterX(), inter.getCenterY());
		}
	}

	protected double comparableValueLeft(Point2D.Double pos) {
		if (yStable) {
			return pos.y;
		}
		return Math.atan2(pos.y - centerY, centerX - pos.x);
	}

	protected double comparableValueRight(Point2D.Double pos) {
		if (yStable) {
			return pos.getY();
		}
		return Math.atan2(pos.y - centerY, pos.x - centerX);
	}

	protected void projectLeftRight(Graphics2D graphics) {
		int radius = focusSize / 2;
		int i;

		leftCount = 0;
		rightCount = 0;
		double maxHeight = 0;
		FontMetrics fm = graphics.getFontMetrics();

		for (i = 0; i < labelCount; i++) {
			Point2D.Double itemPos = itemPosition[i];
			int hit = hits[i];
			String lab = client.getObservationLabel(hit);
			if (lab == null) {
				lab = "item" + hits[i];
			}
			Rectangle2D sb = fm.getStringBounds(lab, graphics);
			Point2D.Double linkPos = linkPosition[i];
			Point2D.Double labelPos = labelPosition[i];

			maxHeight = Math.max(sb.getHeight(), maxHeight);
			if (itemPosition[i].getX() < centerX) {
				linkPos.y = comparableValueLeft(itemPos);
				if (xStable) {
					linkPos.x = itemPos.x - radius - gap;
				} else {
					linkPos.x = centerX - radius - gap;
				}
				labelPos.x = linkPos.x - sb.getWidth();
				left[leftCount++] = linkPos;
			} else {
				linkPos.y = comparableValueRight(itemPos);
				if (xStable) {
					linkPos.x = itemPos.x + radius + gap;
				} else {
					linkPos.x = centerX + radius + gap;
				}
				labelPos.x = linkPos.x;
				right[rightCount++] = linkPos;
			}
		}

		Arrays.sort(left, 0, leftCount, this);
		Arrays.sort(right, 0, rightCount, this);
		double yMidLeft = leftCount * maxHeight / 2;
		double yMidRight = rightCount * maxHeight / 2;
		int ascent = fm.getAscent();

		for (i = 0; i < leftCount; i++) {
			Point2D.Double pos = left[i];
			pos.y = i * maxHeight + centerY - yMidLeft + ascent;
		}
		for (i = 0; i < rightCount; i++) {
			Point2D.Double pos = right[i];
			pos.y = i * maxHeight + centerY - yMidRight + ascent;
		}
		for (i = 0; i < linkPosition.length; i++) {
			labelPosition[i].y = linkPosition[i].y;
		}
	}

	/**
	 * Returns the visible.
	 * 
	 * @return boolean
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            The visible to set
	 */
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			client.repaint();
			// visualization.repaint();
		}
	}

	/**
	 * For sorting points vertically.
	 * 
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		double d = ((Point2D.Double) o1).getY() - ((Point2D.Double) o2).getY();
		if (d < 0) {
			return -1;
		} else if (d == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @see java.awt.event.MouseAdapter#mouseEntered(MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		insideTimer.restart();
	}

	/**
	 * @see java.awt.event.MouseAdapter#mouseExited(MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		insideTimer.stop();
		setVisible(false);
	}

	/**
	 * @see java.awt.event.MouseAdapter#mousePressed(MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		setVisible(false);
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
	 */

	public void mouseDragged(MouseEvent e) {
	}

	int dist2(int dx, int dy) {
		return dx * dx + dy * dy;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
	 */

	public void mouseMoved(MouseEvent e) {
		if (isVisible()) {
			if (dist2(centerX - e.getX(), centerY - e.getY()) > threshold
					* threshold) {
				setVisible(false);
				insideTimer.restart();
			}
			client.repaint();
			// visualization.repaint();
		}
		centerX = e.getX();
		centerY = e.getY();
	}

	/**
	 * Returns the gap.
	 * 
	 * @return int
	 */
	public int getGap() {
		return gap;
	}

	/**
	 * Sets the gap.
	 * 
	 * @param gap
	 *            The gap to set
	 */
	public void setGap(int gap) {
		this.gap = gap;
	}

	/**
	 * Returns the maxLabels.
	 * 
	 * @return int
	 */
	public int getMaxLabels() {
		return maxLabels;
	}

	void allocatePoints(Point2D.Double[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = new Point2D.Double();
		}
	}

	/**
	 * Sets the maxLabels.
	 * 
	 * @param maxLabels
	 *            The maxLabels to set
	 */
	public void setMaxLabels(int maxLabels) {
		this.maxLabels = maxLabels;
		itemPosition = new Point2D.Double[maxLabels];
		allocatePoints(itemPosition);
		linkPosition = new Point2D.Double[maxLabels];
		allocatePoints(linkPosition);
		labelPosition = new Point2D.Double[maxLabels];
		allocatePoints(labelPosition);
		left = new Point2D.Double[maxLabels];
		right = new Point2D.Double[maxLabels];
	}

	/**
	 * Returns the threshold.
	 * 
	 * When the mouse moves a distance larger than this threshold since the last
	 * event, excentric labels are disabled.
	 * 
	 * @return int
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Sets the threshold.
	 * 
	 * When the mouse moves a distance larger than the specified threshold since
	 * the last event, excentric labels are disabled.
	 * 
	 * @param threshold
	 *            The threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * Returns the focusSize.
	 * 
	 * @return int
	 */
	public int getFocusSize() {
		return focusSize;
	}

	/**
	 * Sets the focusSize.
	 * 
	 * @param focusSize
	 *            The focusSize to set
	 */
	public void setFocusSize(int focusSize) {
		this.focusSize = focusSize;
		cursorBounds = new Rectangle2D.Double(0, 0, focusSize, focusSize);
	}

	/**
	 * Returns the backgroundColor.
	 * 
	 * @return Color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Returns the opaque.
	 * 
	 * @return boolean
	 */
	public boolean isOpaque() {
		return opaque;
	}

	/**
	 * Sets the backgroundColor.
	 * 
	 * @param backgroundColor
	 *            The backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets the opaque.
	 * 
	 * @param opaque
	 *            The opaque to set
	 */
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}

}
