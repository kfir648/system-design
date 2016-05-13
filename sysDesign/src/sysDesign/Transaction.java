package sysDesign;

public abstract class Transaction {

	private int transId;
	private float amount;
	private Date transactionDate;
	
	public Transaction(float amount, Date transactionDate) {
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	protected void setTransId(int id) {
		this.transId = id;
	}
	
	public float getAmount()
	{
		return amount;
	}

	public int getTransId() {
		return transId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}
}
