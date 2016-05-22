package logic.classes;

import java.sql.SQLException;

public  class MonthlyTransaction extends Transaction {
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
	
	public int getPaymentNumber() {
		return paymentNumber;
	}
	
	public String showAllDetails() throws SQLException {
		return "Mounthly Transaction:Payment Number:" + paymentNumber + " ,  Final Date:" + finalDate.toString();  
	}
}
