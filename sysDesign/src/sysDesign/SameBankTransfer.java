package sysDesign;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class SameBankTransfer extends Transaction {

	int sourceAccountNumber;
	int destenationAccountNumber;
	
	public SameBankTransfer(float amount, Date transactionDate , int accountId , int sourceAccountNumber, int destenationAccountNumber) throws Exception {
		super(0 , amount, transactionDate , accountId);
		this.sourceAccountNumber = sourceAccountNumber;
		this.destenationAccountNumber = destenationAccountNumber;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertTransaction(this);
	}
	
	public SameBankTransfer(int transId , float amount, Date transactionDate , int accountId , int sourceAccountNumber, int destenationAccountNumber) throws Exception {
		super(transId , amount, transactionDate , accountId);
		this.sourceAccountNumber = sourceAccountNumber;
		this.destenationAccountNumber = destenationAccountNumber;
	}
	
	public int getSourceAccount() throws SQLException {
		return sourceAccountNumber;
	}
	
	public int getDestinationAccount() {
		return destenationAccountNumber;
	}

	@Override
	public String toString() {
		return "Same Bank Transfer " + super.toString();
	}

	@Override
	public String showAllDetails() throws SQLException {
		if(getAccuntId() == sourceAccountNumber)
		{
			return sourceAccountNumber + " --> " + getAmount() + " --> " + destenationAccountNumber;
		}else{
			return destenationAccountNumber + " <-- " + getAmount() + " <-- " + sourceAccountNumber;
		}
	}
	
}
