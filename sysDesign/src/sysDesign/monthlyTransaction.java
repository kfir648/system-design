package sysDesign;

public abstract class monthlyTransaction extends Transaction {
	private int paymentNumber;
	private Date finalDate;
	
	public monthlyTransaction(float amount, Date transactionDate, int sourceAccountNumber, 
			int destenationAccountNumber, int paymentNumber, Date finalDate) {
		super(amount, transactionDate);
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
