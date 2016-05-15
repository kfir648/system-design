package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sysDesign.Worker.PermissionType;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainMenu(PermissionType permission) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 220, 237);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnBalance = new JButton("Balance");
		btnBalance.setBounds(20, 25, 159, 31);
		contentPane.add(btnBalance);
		btnBalance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ////////////////////////
			}
		});

		JButton btnLoans = new JButton("Loans");
		btnLoans.setBounds(20, 67, 159, 31);
		contentPane.add(btnLoans);
		JButton btnSavings = new JButton("Savings");
		btnSavings.setBounds(20, 109, 159, 31);
		contentPane.add(btnSavings);
		
		if (permission != PermissionType.TELLER) {
			
			btnLoans.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new LoanDialog();
				}
			});

		} else {
			btnBalance.setEnabled(false);
			btnSavings.setEnabled(false);
		}

		JButton btnWidthrawdeposit = new JButton("widthraw/deposit");
		btnWidthrawdeposit.setBounds(20, 151, 159, 31);
		contentPane.add(btnWidthrawdeposit);
		btnWidthrawdeposit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new WidthrawDepositFrame();
			}
		});

		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
