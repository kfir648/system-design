package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;

public class WidthrawDepositFrame extends JDialog {

	private JPanel contentPane;
	private JTextField txtfAmount;
	private JTextField txtfTargetAccount;
	private JTextField textField;

	public WidthrawDepositFrame() {
		super((JFrame)null,true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 200, 196);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JRadioButton rdbtnWithdraw = new JRadioButton("withdraw");
		panel.add(rdbtnWithdraw);
		
		JRadioButton rdbtnDeposit = new JRadioButton("deposit");
		panel.add(rdbtnDeposit);
		
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(rdbtnDeposit);
		btnGroup.add(rdbtnWithdraw);
		
		JLabel lblNewLabel = new JLabel("amount");
		panel.add(lblNewLabel);
		
		txtfAmount = new JTextField();
		panel.add(txtfAmount);
		txtfAmount.setColumns(10);
		
		JLabel lblAccount = new JLabel("account");
		panel.add(lblAccount);
		
		txtfTargetAccount = new JTextField();
		panel.add(txtfTargetAccount);
		txtfTargetAccount.setColumns(10);
		
		JLabel lblBank = new JLabel("Bank     ");
		panel.add(lblBank);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		panel.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

}
