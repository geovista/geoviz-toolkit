/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

public class CustomizedMultiSlider extends JPanel implements MouseListener,
		MouseMotionListener {

	protected static final int WIDTH = 240;
	protected static final int HEIGHT = 120;
	public static final String COMMAND_BOUNDARIES_MOVED = "cmdMov";
	public static final String COMMAND_BOUNDARIES_NUMBERCHANGED = "cmdChg";
	protected static final Color LINECOLOR = Color.red;
	protected int orientation;
	protected int numberOfThumb;
	protected Vector valueOfThumb;
	protected Vector pixelValues;
	protected Dimension sizeOfPanel;
	protected Color[] classColors;
	transient protected int mouseX1, mouseX2, mouseY1, mouseY2;
	protected double scale;
	protected double[] data;
	protected boolean labelVisible = true;
	protected JPopupMenu popup;
	protected JDialog dialog;
	protected JFrame dummyFrame;
	transient protected JTextField changeBoundaryField;
	protected double changedThumbValue;
	protected int idxOfChangedThumb;
	protected EventListenerList listenerListAction = new EventListenerList();
	protected final static Logger logger = Logger
			.getLogger(CustomizedMultiSlider.class.getName());

	public CustomizedMultiSlider() {
		sizeOfPanel = new Dimension(WIDTH, HEIGHT);
		super.setMaximumSize(sizeOfPanel);
		super.setPreferredSize(sizeOfPanel);
		super.setSize(sizeOfPanel);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public int getNumberOfThumb() {
		return numberOfThumb;
	}

	public void setNumberOfThumb(int numberOfThumb) {
		this.numberOfThumb = numberOfThumb;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public Color[] getColors() {
		return classColors;
	}

	public void setColors(Color[] colors) {
		classColors = colors;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public Vector getValueOfThumb() {
		return valueOfThumb;
	}

	public void setValueOfThumb(Vector valueOfThumb) {
		this.valueOfThumb = valueOfThumb;
	}

	public double[] getValuesOfThumb() {
		double[] values = new double[valueOfThumb.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = ((Double) valueOfThumb.get(i)).doubleValue();
		}
		return values;
	}

	public void setValuesOfThumb(double[] values) {
		if (valueOfThumb == null) {
			valueOfThumb = new Vector();
		} else {
			valueOfThumb.clear();
		}
		for (int i = 0; i < values.length; i++) {
			valueOfThumb.add(i, new Double(values[i]));
		}
		valueOfThumb.trimToSize();
		initialize();
	}

	public void setLabelVisible(boolean visible) {
		labelVisible = visible;
	}

	protected void initialize() {
		setBackground(Color.white);
		setForeground(CustomizedMultiSlider.LINECOLOR);
		convertValuesToPixels();

	}

	protected void convertValuesToPixels() {
		if (pixelValues == null) {
			pixelValues = new Vector();
		} else {
			pixelValues.clear();
		}
		double min = ((Double) (valueOfThumb.get(0))).doubleValue();
		double max = ((Double) (valueOfThumb.get(valueOfThumb.size() - 1)))
				.doubleValue();
		int minPixel = 0;
		int maxPixel = getWidth();
		scale = getScale(minPixel, maxPixel, min, max);
		int pixelValue;
		for (int i = 0; i < valueOfThumb.size(); i++) {
			pixelValue = getValueScreen(((Double) valueOfThumb.get(i))
					.doubleValue(), scale, minPixel, min);
			pixelValues.add(i, new Integer(pixelValue));
		}
		pixelValues.trimToSize();
	}

	@Override
	public void paintComponent(Graphics g) {
		int v;
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());

		// draw class colors
		if (classColors != null && pixelValues != null) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest(pixelValues.size() + "," + classColors.length);
			}
			int v1 = 0;
			v = 0;
			for (int i = 1; i < pixelValues.size(); i++) {
				v = ((Integer) (pixelValues.get(i))).intValue();
				drawClassColors(g, classColors[i - 1], v1, 0, v - v1,
						getHeight());
				v1 = v;
			}
			// this.drawClassColors(g, classColors[this.pixelValues.size()-1],
			// v, 0, this.getWidth()-v1, this.getHeight());
		}

		// draw class boundaries.
		if (pixelValues != null) {
			g.setColor(Color.white);
			for (int i = 0; i < pixelValues.size(); i++) {
				v = ((Integer) (pixelValues.get(i))).intValue();
				drawValues(g, v, 0, v, getHeight());
			}
			if (labelVisible == true) {
				drawBoundaryLabels(g);
			}
		}

		// draw dragged line
		Graphics2D g2d = (Graphics2D) g;
		drawABoundary(g2d, mouseX2, 0, mouseX2, getHeight());

	}

	protected void drawValues(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	protected void drawClassColors(Graphics g, Color color, int x1, int y1,
			int w, int h) {
		g.setColor(color);
		g.fillRect(x1, y1, w, h);
	}

	protected void drawABoundary(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		Stroke tempStroke = g2d.getStroke();
		float[] dash = new float[3];
		dash[0] = (float) 5.0;
		dash[1] = (float) 7.0;
		dash[2] = (float) 5.0;
		BasicStroke dashStroke = new BasicStroke((float) 1.0,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, (float) 10.0,
				dash, 0);
		g2d.setStroke(dashStroke);
		g2d.setPaintMode();
		g2d.setColor(Color.white);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setStroke(tempStroke);
	}

	protected void drawBoundaryLabels(Graphics g) {

		g.setColor(Color.black);
		String label;
		for (int i = 0; i < valueOfThumb.size() - 1; i++) {
			// g.drawLine(this.xBoundariesInt[i], 0, this.xBoundariesInt[i],
			// this.getHeight());
			label = ((Double) (valueOfThumb.get(i))).toString();
			if (label.length() >= 8) {
				label = label.substring(0, 7);
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.rotate(Math.PI / 2,
					((Integer) pixelValues.get(i)).intValue() + 2, 3);
			g.drawString(label, ((Integer) pixelValues.get(i)).intValue() + 2,
					3);
			g2d.rotate(-Math.PI / 2,
					((Integer) pixelValues.get(i)).intValue() + 2, 3);
		}
		label = ((Double) (valueOfThumb.get(valueOfThumb.size() - 1)))
				.toString();
		if (label.length() >= 8) {
			label = label.substring(0, 7);
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.PI / 2, ((Integer) pixelValues
				.get(valueOfThumb.size() - 1)).intValue() - 8, 3);
		g
				.drawString(label, ((Integer) pixelValues.get(valueOfThumb
						.size() - 1)).intValue() - 8, 3);
		g2d.rotate(-Math.PI / 2, ((Integer) pixelValues
				.get(valueOfThumb.size() - 1)).intValue() - 8, 3);
	}

	/**
	 * Calculate scale between real data and integer data for showing up on
	 * screen.
	 * 
	 * @param min
	 * @param max
	 * @param dataMin
	 * @param dataMax
	 * @return scale
	 */
	protected double getScale(int min, int max, double dataMin, double dataMax) {
		double scale;
		scale = (max - min) / (dataMax - dataMin);
		return scale;
	}

	/**
	 * Convert the single value to integer value worked on screen.
	 * 
	 * @param data
	 * @param scale
	 * @param min
	 * @param dataMin
	 * @return valueScreen
	 */
	protected int getValueScreen(double data, double scale, int min,
			double dataMin) {
		int valueScreen;
		if (Double.isNaN(data)) {
			valueScreen = Integer.MIN_VALUE;
		} else {
			valueScreen = (int) ((data - dataMin) * scale + min);
		}
		return valueScreen;
	}

	protected double getValueFromScreenValue(int data, double scale, int min,
			double dataMin) {
		double value;
		value = ((data - min)) / scale + dataMin;
		return value;
	}

	protected void makeToolTip(int x, int y) {
		if (pixelValues != null) {
			for (int i = 0; i < pixelValues.size(); i++) {
				if (Math.abs(x - ((Integer) pixelValues.get(i)).intValue()) < 5) {
					setToolTipText(((Double) valueOfThumb.get(i)).toString());
				}
			}
		}
	}

	/**
	 * Begin the drawing of selection region (box).
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
		mouseX1 = e.getX();
	}

	/**
	 * Work with mouseDragged to draw a selection region (box) for selection.
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		int v, v1;
		mouseX2 = e.getX();
		for (int i = 0; i < pixelValues.size(); i++) {
			v = ((Integer) (pixelValues.get(i))).intValue();
			if (Math.abs(mouseX1 - v) <= 5) {
				// merge boundaries
				if ((mouseX2 - mouseX1) > 0 && (i + 1) < pixelValues.size()) {
					v1 = ((Integer) (pixelValues.get(i + 1))).intValue();
					if (Math.abs(mouseX2 - v1) <= 5) {
						pixelValues.remove(i);
						valueOfThumb.remove(i);
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != 0) {
						pixelValues.set(i, new Integer(mouseX2));
						valueOfThumb.set(i, new Double(getValueFromScreenValue(
								mouseX2, scale, 0, ((Double) valueOfThumb
										.get(0)).doubleValue())));
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_MOVED);
					}
				} else if ((mouseX2 - mouseX1) < 0 && (i - 1) >= 0) {
					v1 = ((Integer) (pixelValues.get(i - 1))).intValue();
					if (Math.abs(mouseX2 - v1) <= 3) {
						pixelValues.remove(i);
						valueOfThumb.remove(i);
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != pixelValues.size() - 1) {
						pixelValues.set(i, new Integer(mouseX2));
						valueOfThumb.set(i, new Double(getValueFromScreenValue(
								mouseX2, scale, 0, ((Double) valueOfThumb
										.get(0)).doubleValue())));
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_MOVED);
					}
				}
				// add new boundaries
				if (i == 0) {
					v1 = ((Integer) (pixelValues.get(1))).intValue();
					if ((mouseX2 - mouseX1) > 0 && (mouseX2 < v1)) {
						pixelValues.add(1, new Integer(mouseX2));
						valueOfThumb.add(1, new Double(getValueFromScreenValue(
								mouseX2, scale, 0, ((Double) valueOfThumb
										.get(0)).doubleValue())));
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
				if (i == pixelValues.size() - 1) {
					v1 = ((Integer) (pixelValues.get(i - 1))).intValue();
					if ((mouseX2 - mouseX1) < 0 && (mouseX2 > v1)) {
						pixelValues.add(i, new Integer(mouseX2));
						valueOfThumb.add(i, new Double(getValueFromScreenValue(
								mouseX2, scale, 0, ((Double) valueOfThumb
										.get(0)).doubleValue())));
						fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
			}
		}
		mouseX2 = 0;
		this.repaint();

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
		logger.finest("mouse exited: ");
	}

	/**
	 * Work with mouseReleased to draw a selection region (box) for selection.
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
		int v;
		for (int i = 0; i < pixelValues.size(); i++) {
			v = ((Integer) (pixelValues.get(i))).intValue();
			if (Math.abs(mouseX1 - v) <= 5) {

				mouseX2 = e.getX();
				this.repaint();
			}
		}

	}

	/**
	 * Mouse over, it will show the values for current point by tool tip.
	 * 
	 * @param e
	 */
	public void mouseMoved(MouseEvent e) {
		if (e != null) {
			makeToolTip(e.getX(), e.getY());
			e.consume();
		}

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Mouse click for selecting or brushing points (observations).
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		logger.finest("mouseClicked" + e.getX());
		int position = e.getX();
		int v1, v2;
		if (e.getClickCount() == 2) {
			for (int i = 0; i < pixelValues.size() - 1; i++) {
				v1 = ((Integer) (pixelValues.get(i))).intValue();
				v2 = ((Integer) (pixelValues.get(i + 1))).intValue();
				if ((position - v1) > 5 && (v2 - position) > 5) {
					pixelValues.add(i + 1, new Integer(position));
					valueOfThumb.add(i + 1, new Double(getValueFromScreenValue(
							position, scale, 0, ((Double) valueOfThumb.get(0))
									.doubleValue())));
					fireActionPerformed(CustomizedMultiSlider.COMMAND_BOUNDARIES_NUMBERCHANGED);
					return;
				} else if (Math.abs(position - v1) <= 5) {
					idxOfChangedThumb = i;
					changedThumbValue = ((Double) valueOfThumb.get(i))
							.doubleValue();
					showChangeBoundaryDialog(300, 300);
				}
			}
		}
	}

	private void showChangeBoundaryDialog(int x, int y) {
		if (dummyFrame == null) {
			dummyFrame = new JFrame();
			dialog = new JDialog(dummyFrame, "Boundary Setup ", true);
			JButton actionButton;
			JButton resetButton;
			dialog.setLocation(x, y);
			dialog.setSize(150, 80);
			dialog.getContentPane().setLayout(new GridLayout(2, 2));
			changeBoundaryField = new JTextField(16);
			// create buttons for action
			actionButton = new JButton("OK");
			actionButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * Button to set up new data ranges shown up in scatter plot.
				 * 
				 * @param e
				 */
				public void actionPerformed(ActionEvent e) {
					try {
						actionButton_actionPerformed(e);
					} catch (Exception ex) {
						logger.throwing(this.getClass().getName(),
								"showChangeBoundarydlg", ex);
					}
				}
			});
			resetButton = new JButton("Cancel");
			resetButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void actionPerformed(ActionEvent e) {
					resetButton_actionPerformed(e);
				}
			});
			dialog.getContentPane().add(new JLabel(("Boundary:")));
			dialog.getContentPane().add(changeBoundaryField);
			dialog.getContentPane().add(resetButton);
			dialog.getContentPane().add(actionButton);
		}

		changeBoundaryField.setText(Double.toString(changedThumbValue));

		dialog.setVisible(true);
	}

	/**
	 * Set up new data ranges to show.
	 * 
	 * @param e
	 */
	private void actionButton_actionPerformed(ActionEvent e) {
		// get the input data from text field
		changedThumbValue = Double.parseDouble(changeBoundaryField.getText());
		valueOfThumb.set(idxOfChangedThumb, new Double(changedThumbValue));

		convertValuesToPixels();
		dialog.setVisible(false);
		repaint();

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	private void resetButton_actionPerformed(ActionEvent e) {

		// changeBoundaryField.setText(((Double)(this.valueOfThumb.get(this.idxOfChangedThumb))).toString());

		// this.convertValuesToPixels();
		dialog.setVisible(false);
		// repaint();
	}

	/**
	 * adds an ActionListener to the button
	 */
	public void addActionListener(ActionListener l) {
		listenerListAction.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerListAction.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireActionPerformed(String command) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerListAction.getListenerList();
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

	// test
	public static void main(String[] args) {
		JFrame app = new JFrame();
		CustomizedMultiSlider slider = new CustomizedMultiSlider();
		double[] testData = new double[4];
		Color[] colors = new Color[3];
		testData[0] = 12;
		testData[1] = 20;
		testData[2] = 40;
		testData[3] = 101;
		colors[0] = Color.yellow;
		colors[1] = Color.green;
		colors[2] = Color.blue;
		slider.setValuesOfThumb(testData);
		// slider.setColors(colors);
		app.setSize(180, 100);
		app.getContentPane().add(slider);
		app.pack();
		app.setVisible(true);

	}

}
