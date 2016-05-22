package logic.classes;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Transaction {

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
	
	public Transaction(float amount, Date transactionDate , int accuntId) throws Exception
	{
		this(0 , amount , transactionDate , accuntId);
		
		DatabaseInterface databaseInterface = DataBaseService.getDataBaseService();
		this.transId = databaseInterface.insertTransaction(this);
	}
	
	public String showAllDetails() throws Exception {
		return toString();
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
		return String.format("%s[%6d , Date : %s , %.2f] ", (amount < 0 ? "Deposite" : "Widrow") , transId , transactionDate.toString() , (amount < 0 ? -amount : amount));
	}
}
