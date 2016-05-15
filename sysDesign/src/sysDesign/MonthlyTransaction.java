package sysDesign;

public abstract class MonthlyTransaction extends Transaction {
	private int paymentNumber;
	private Date finalDate;
	
	public MonthlyTransaction(int transId , float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate) {
		super(transId , amount, transactionDate , accountId);
		this.paymentNumber = paymentNumber;
		this.finalDate = finalDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	@Override
	public String toString() {
		return "MonthlyTransaction [" + super.toString() + " paymentNumber=" + paymentNumber + ", finalDate=" + finalDate + "]";
	}

	public int getPaymentNumber() {
		return paymentNumber;
	}
}
