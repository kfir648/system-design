package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class SavingTransaction extends monthlyTransaction {

	private int saving;
	
	public SavingTransaction(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber, int paymentNumber, Date finalDate , int saving) {
		super(amount, transactionDate, sourceAccountNumber, destenationAccountNumber, paymentNumber, finalDate);
		this.saving = saving;
	}
	
	public Saving getSaving() {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getSavingById(saving);
	}

}
