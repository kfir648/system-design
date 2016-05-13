package sysDesign;

public abstract class monthlyTransaction extends Transaction {
	private int paymentNumber;
	private Date finalDate;
	
	public monthlyTransaction(int transId , float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate) {
		super(transId , amount, transactionDate , accountId);
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
