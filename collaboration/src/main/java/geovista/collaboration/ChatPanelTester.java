/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */

package geovista.collaboration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class ChatPanelTester extends JPanel implements ActionListener {

	JButton sendMessageButton;
	JEditorPane conversationPane;
	JTextArea inputArea;
	// XXX hack
	String name = "HyangJa";

	public ChatPanelTester() {
		setBorder(new LineBorder(Color.black));
		JPanel convPanel = new JPanel(new BorderLayout());
		JLabel conv = new JLabel("Conversation:");
		convPanel.add(conv, BorderLayout.NORTH);
		conversationPane = new JEditorPane();
		conversationPane.setEditable(false);
		conversationPane.setPreferredSize(new Dimension(400, 200));
		convPanel.add(conversationPane, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		this.add(convPanel, BorderLayout.CENTER);

		JPanel sendPanel = new JPanel();
		inputArea = new JTextArea();
		inputArea.setPreferredSize(new Dimension(200, 30));
		sendMessageButton = new JButton("Send \n Message");
		sendPanel.add(inputArea);
		sendPanel.add(sendMessageButton);
		this.add(sendPanel, BorderLayout.SOUTH);
		sendMessageButton.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendMessageButton) {
			String message = name + ": " + inputArea.getText();
			appendMessage(message);
		}
	}

	public void addMessage(String message) {
		appendMessage(message);
	}

	private void appendMessage(String message) {
		// XXX hack
		String currConv = conversationPane.getText();
		String newConv = currConv + "\n" + message;
		conversationPane.setText(newConv);
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		ChatPanelTester rc = new ChatPanelTester();
		rc.setPreferredSize(new Dimension(450, 400));
		rc.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(rc);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
	}
}
