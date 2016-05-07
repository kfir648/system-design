package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LogInPanel extends JPanel {
	public LogInPanel(JDialog superFrame) {
		JButton submitButton;
		JTextField	idTextField;
		JPasswordField passwordField;
		JLabel idLabel;
		JLabel passLabel;
		
		submitButton = new JButton("log in");
		idTextField = new JTextField(10);
		passwordField = new JPasswordField(10);
		idLabel = new JLabel("id:");
		passLabel = new JLabel("passwaord:");
		
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// check server answer to result
				boolean result = true;
				if (result) {
					superFrame.dispose();
				}
			}
		});
		add(idLabel);
		add(idTextField);
		add(passLabel);
		add(passwordField);
		add(submitButton);
	}
}
