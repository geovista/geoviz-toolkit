/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.classification;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.event.EventListenerList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class WholeNumberField extends JTextField implements FocusListener,
		KeyListener {
	private final NumberFormat integerFormatter;
	public static final String COMMAND_NEW_VAL = "newVal";

	public WholeNumberField(int value, int columns) {

		super(columns);
		addKeyListener(this);
		integerFormatter = NumberFormat.getNumberInstance(Locale.US);
		integerFormatter.setParseIntegerOnly(true);
		setValue(value);
		addFocusListener(this);
		setMinimumSize(new Dimension(40, 20));
		setPreferredSize(new Dimension(40, 20));
		setMaximumSize(new Dimension(40, 20));
	}

	public int getValue() {
		// if this string ends in a return, fire a change

		int retVal = 0;
		try {
			retVal = integerFormatter.parse(getText()).intValue();
		} catch (ParseException e) {
			// This should never happen because insertString allows
			// only properly formatted data to get in the field.
			// toolkit.beep();
			e.printStackTrace();
		}
		return retVal;
	}

	public void setValue(int value) {
		setText(integerFormatter.format(value));
		this.fireActionPerformed(WholeNumberField.COMMAND_NEW_VAL);
	}

	@Override
	protected Document createDefaultModel() {
		return new WholeNumberDocument();
	}

	protected class WholeNumberDocument extends PlainDocument {
		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;

			for (int i = 0; i < result.length; i++) {

				result[j++] = source[i];

			}
			super.insertString(offs, new String(result, 0, j), a);

		}
	}

	// implement listeners
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		this.fireActionPerformed(WholeNumberField.COMMAND_NEW_VAL);
	}

	public void keyPressed(KeyEvent e) {
		char ch = e.getKeyChar();
		if (Character.isWhitespace(ch)) {
			this.fireActionPerformed(WholeNumberField.COMMAND_NEW_VAL);
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	// end implement listeners
	/**
	 * implements ActionListener
	 */
	@Override
	public synchronized void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the component
	 */
	@Override
	public synchronized void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireActionPerformed(String command) {
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
		}// next i
	}
}
