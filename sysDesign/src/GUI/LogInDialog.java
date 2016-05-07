package GUI;

import javax.swing.JDialog;
import javax.swing.JFrame;

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
}
