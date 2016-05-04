package sysDesign;

public class SameBankTransfer extends Transaction {

	public SameBankTransfer(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber) {
		super(amount, transactionDate, sourceAccountNumber, destenationAccountNumber);
		// TODO Auto-generated constructor stub
	}

	public SameBankTransfer(float amount, int day, int month, int year, int sourceAccountNumber,
			int destenationAccountNumber) {
		super(amount, day, month, year, sourceAccountNumber, destenationAccountNumber);
		// TODO Auto-generated constructor stub
	}

	
	
	
}
