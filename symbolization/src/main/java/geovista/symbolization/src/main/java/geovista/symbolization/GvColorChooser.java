/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;

//import javax.swing.colorchooser.*;

public class GvColorChooser extends JColorChooser {

	public GvColorChooser(Color c) {
		super(c);
		init();
	}

	public GvColorChooser() {
		// AbstractColorChooserPanel panels[] = { new MunsellChooserPanel() };
		super();
		init();
	}

	public void init() {
		AbstractColorChooserPanel[] panels = getChooserPanels();
		AbstractColorChooserPanel[] newPanels = new AbstractColorChooserPanel[panels.length + 1];
		// this.removeall
		newPanels[0] = new MunsellChooserPanel();
		for (int i = 1; i < newPanels.length; i++) {
			newPanels[i] = panels[i - 1];
		}
		setChooserPanels(newPanels);
		// this.remo
	}

	public Color showGvDialog(Component component, String title,
			Color initialColor) {

		final GvColorChooser pane = new GvColorChooser(initialColor != null
				? initialColor : Color.white);

		ColorTracker ok = new ColorTracker(pane);
		JDialog dialog = createDialog(component, title, true, pane, ok, null);
		// dialog.addWindowListener(new ColorChooserDialog.Closer());
		// dialog.addComponentListener(new ColorChooserDialog.DisposeOnClose());

		dialog.setVisible(true); // blocks until user brings dialog down...

		return ok.getColor();
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
		GvColorChooser swat = new GvColorChooser();
		app.getContentPane().add(swat);

		// app.getContentPane().add(swatchesPanel,BorderLayout.SOUTH);

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}

	/*
	 * Class which builds a color chooser dialog consisting of a GvColorChooser
	 * with "Ok", "Cancel", and "Reset" buttons.
	 * 
	 * Note: This needs to be fixed to deal with localization!
	 */
	class ColorChooserDialog extends JDialog {
		private transient Color initialColor;
		private transient final GvColorChooser chooserPane;

		public ColorChooserDialog(Component c, String title, boolean modal,
				GvColorChooser chooserPane, ActionListener okListener,
				ActionListener cancelListener) {
			super(JOptionPane.getFrameForComponent(c), title, modal);
			// setResizable(false);

			this.chooserPane = chooserPane;

			String okString = UIManager.getString("ColorChooser.okText");
			String cancelString = UIManager
					.getString("ColorChooser.cancelText");
			String resetString = UIManager.getString("ColorChooser.resetText");

			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(chooserPane, BorderLayout.CENTER);

			/*
			 * Create Lower button panel
			 */
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton okButton = new JButton(okString);
			getRootPane().setDefaultButton(okButton);
			okButton.setActionCommand("OK");
			if (okListener != null) {
				okButton.addActionListener(okListener);
			}
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			buttonPane.add(okButton);

			JButton cancelButton = new JButton(cancelString);

			cancelButton.setActionCommand("cancel");
			if (cancelListener != null) {
				cancelButton.addActionListener(cancelListener);
			}
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			buttonPane.add(cancelButton);

			JButton resetButton = new JButton(resetString);
			resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reset();
				}
			});

			buttonPane.add(resetButton);
			contentPane.add(buttonPane, BorderLayout.SOUTH);
			// below taken out for 1.3 compatiblity
			// if (JDialog.isDefaultLookAndFeelDecorated()) {
			// boolean supportsWindowDecorations =
			// UIManager.getLookAndFeel().getSupportsWindowDecorations();
			// if (supportsWindowDecorations) {
			// getRootPane().setWindowDecorationStyle(JRootPane.
			// COLOR_CHOOSER_DIALOG);
			// }
			// }
			// applyComponentOrientation(((c == null) ? getRootPane() :
			// c).getComponentOrientation());

			pack();
			setLocationRelativeTo(c);
		}

		@Override
		public void show() {
			initialColor = chooserPane.getColor();
			super.setVisible(true);
		}

		public void reset() {
			chooserPane.setColor(initialColor);
		}

		class Closer extends WindowAdapter implements Serializable {
			@Override
			public void windowClosing(WindowEvent e) {
				Window w = e.getWindow();
				w.setVisible(false);
			}
		}

		class DisposeOnClose extends ComponentAdapter implements Serializable {
			@Override
			public void componentHidden(ComponentEvent e) {
				Window w = (Window) e.getComponent();
				w.dispose();
			}
		}

	}

	class ColorTracker implements ActionListener, Serializable {
		transient GvColorChooser chooser;
		transient Color color;

		public ColorTracker(GvColorChooser c) {
			chooser = c;
		}

		public void actionPerformed(ActionEvent e) {
			color = chooser.getColor();
		}

		public Color getColor() {
			return color;
		}
	}
}
