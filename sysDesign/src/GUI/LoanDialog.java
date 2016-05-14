package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoanDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfAmount;
	private JTextField tfPayments;
	private JTextField tfMonthly;
	private double amount = 0;
	private double payments = 0;
	private double monthly = 0;

	public LoanDialog() {
		setBounds(100, 100, 240, 342);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblAmount = new JLabel("amount");
		contentPanel.add(lblAmount);

		tfAmount = new DoubleJTextField();
		contentPanel.add(tfAmount);
		tfAmount.setColumns(10);
		tfAmount.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (!tfAmount.getText().equals("")) {
					amount = Double.parseDouble(tfAmount.getText());
					if (payments != 0) {
						monthly = Math.floor((amount / payments) * 100) / 100;
						tfMonthly.setText(String.valueOf(monthly));
					}
				} else {
					amount = 0;
				}
			}
		});
		JLabel lblStartingDate = new JLabel("   starting date   ");
		contentPanel.add(lblStartingDate);
		JCalendar jc = new JCalendar();
		contentPanel.add(jc);

		JLabel lblPayments = new JLabel("payments           ");
		contentPanel.add(lblPayments);

		tfPayments = new JTextField();
		contentPanel.add(tfPayments);
		tfPayments.setColumns(10);
		tfPayments.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (!tfPayments.getText().equals("")) {
					payments = Double.parseDouble(tfPayments.getText());
					if (amount != 0) {
						monthly = Math.floor((amount / payments) * 100) / 100;
						tfMonthly.setText(String.valueOf(monthly));
					}
				} else {
					payments = 0;
				}
			}
		});

		JLabel lblMonthlyAmount = new JLabel("monthly amount");
		contentPanel.add(lblMonthlyAmount);

		tfMonthly = new JTextField();
		tfMonthly.setEditable(false);
		contentPanel.add(tfMonthly);
		tfMonthly.setColumns(10);

		JButton btnSubmit = new JButton("Submit");
		contentPanel.add(btnSubmit);

		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

}
