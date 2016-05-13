package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class SameBankTransfer extends Transaction {

	int sourceAccountNumber;
	int destenationAccountNumber;
	
	public SameBankTransfer(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber) {
		super(amount, transactionDate);
		this.sourceAccountNumber = sourceAccountNumber;
		this.destenationAccountNumber = destenationAccountNumber;
	}
	
	public Account getSourceAccount() {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getAccountByID(sourceAccountNumber);
	}
	
	public Account getDestinationAccount() {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getAccountByID(destenationAccountNumber);
	}
	
}
