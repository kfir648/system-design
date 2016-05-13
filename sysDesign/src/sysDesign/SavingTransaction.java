package sysDesign;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class SavingTransaction extends monthlyTransaction {

	private int saving;
	
	public SavingTransaction(float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate , int saving) throws Exception {
		super(0 ,amount, transactionDate, accountId , paymentNumber, finalDate);
		this.saving = saving;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertTransaction(this);
	}
	
	public SavingTransaction(int transId , float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate , int saving) {
		super(transId , amount, transactionDate, accountId , paymentNumber, finalDate);
		this.saving = saving;
	}
	
	public int getSaving(){
		return saving;
	}

}
