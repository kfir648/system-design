package sysDesign;

public abstract class Transaction {

	private int transId;
	private float amount;
	private Date transactionDate;
	private int accuntId;
	
	public Transaction(float amount, Date transactionDate , int accuntId) {
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	public int getAccuntId() {
		return accuntId;
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
