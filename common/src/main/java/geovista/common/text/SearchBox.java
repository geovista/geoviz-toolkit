package geovista.common.text;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import geovista.common.event.TextEvent;
import geovista.common.event.TextListener;

public class SearchBox extends JPanel implements ActionListener {
	JTextField textField;
	public JButton goButt;

	public SearchBox() {
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(80, 30));
		this.add(textField);

		goButt = new JButton("Go!");
		this.add(goButt);
		goButt.addActionListener(this);

	}

	public static void main(String[] args) {

		JFrame app = new JFrame();
		SearchBox box = new SearchBox();
		app.add(box);
		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goButt) {
			String text = textField.getText();
			fireTextChanged(text);
		}
	}

	public void addWordListener(ActionListener l) {
		goButt.addActionListener(l);
	}

	/**
	 * adds an TextListener
	 */
	public void addTextListener(TextListener l) {
		listenerList.add(TextListener.class, l);
	}

	/**
	 * removes an TextListener from the component
	 */
	public void removeTextListener(TextListener l) {
		listenerList.remove(TextListener.class, l);
	}

	private void fireTextChanged(String newText) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TextEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TextListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TextEvent(this, newText);
				}

				((TextListener) listeners[i + 1]).textChanged(e);
			}
		} // next i
	}
}
