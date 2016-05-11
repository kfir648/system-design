package sysDesign;

public abstract class Transaction {

	private float amount;
	private Date transactionDate;
	private int sourceAccountNumber;
	private int destenationAccountNumber;
	
	public Transaction(float amount, Date transactionDate,int sourceAccountNumber,int destenationAccountNumber) {
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.destenationAccountNumber=destenationAccountNumber;
		this.sourceAccountNumber=sourceAccountNumber;
	}
	
	public Transaction(float amount,int day, int month, int year,int sourceAccountNumber,int destenationAccountNumber){
		this(amount,new Date(day,month,year),sourceAccountNumber,destenationAccountNumber);
	}
	
}
