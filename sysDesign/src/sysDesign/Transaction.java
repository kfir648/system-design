package sysDesign;

public abstract class Transaction {

	private int transId;
	private float amount;
	private Date transactionDate;
	private int accuntId;
	
	public Transaction(int transId , float amount, Date transactionDate , int accuntId) {
		this.transId = transId;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.accuntId = accuntId;
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

	public void setId(int id) throws Exception {
		if(id <= 0)
			throw new Exception("id is negative");
		transId = id;
	}

	@Override
	public String toString() {
		return "Transaction [transId=" + transId + ", amount=" + amount + ", transactionDate=" + transactionDate
				+ ", accuntId=" + accuntId + "]";
	}
}
