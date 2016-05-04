package sysDesign;

public class OtherBankTransfer extends Transaction {

	private int otherBankNumber;

	public OtherBankTransfer(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber,
			int otherBankNumber) {
		super(amount, transactionDate, sourceAccountNumber, destenationAccountNumber);
		this.otherBankNumber = otherBankNumber;
	}

	public OtherBankTransfer(float amount, int day, int month, int year, int sourceAccountNumber,
			int destenationAccountNumber,int otherBankNumber) {
		super(amount, day, month, year, sourceAccountNumber, destenationAccountNumber);
		this.otherBankNumber = otherBankNumber;
	}

	public int getOtherBankNumber() {
		return otherBankNumber;
	}
	
	
	
	
}
