package sysDesign;

public class monthlyTransaction extends Transaction {

	private int paymentNumber;
	private Date finalDate;
	
	
	public monthlyTransaction(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber,
			int paymentNumber, Date finalDate) {
		super(amount, transactionDate, sourceAccountNumber, destenationAccountNumber);
		this.paymentNumber = paymentNumber;
		this.finalDate = finalDate;
	}


	public monthlyTransaction(float amount, int day, int month, int year, int sourceAccountNumber,
			int destenationAccountNumber,int paymentNumber, Date finalDate) {
		super(amount, day, month, year, sourceAccountNumber, destenationAccountNumber);
		this.paymentNumber = paymentNumber;
		this.finalDate = finalDate;
	}


	public Date getFinalDate() {
		return finalDate;
	}


	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}


	public int getPaymentNumber() {
		return paymentNumber;
	}
	
	
	
}
