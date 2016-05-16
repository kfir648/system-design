package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sysDesign.Worker;
import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

@SuppressWarnings("serial")
public class LogInDialog extends JDialog {
	public LogInDialog() {
		super((JFrame)null,true);
		LogInPanel lp = new LogInPanel(this);
		
		
		setSize(400,100);
		add(lp);
		setAlwaysOnTop(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	private class LogInPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		JTextField idTextField;
		JPasswordField passwordField;

		public LogInPanel(JDialog superFrame) {

			JButton submitButton = new JButton("log in");
			idTextField = new JTextField(10);
			passwordField = new JPasswordField(10);

			submitButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						DatabaseInterface db = DataBaseService.getDataBaseService();
						Worker user = new Worker(idTextField.getText(), new String(passwordField.getPassword()));
						superFrame.dispose();
						new MainMenu(user.getPermission());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			});
			add(new JLabel("ID:"));
			add(idTextField);
			add(new JLabel("Passward:"));
			add(passwordField);
			add(submitButton);
		}
	}
}
