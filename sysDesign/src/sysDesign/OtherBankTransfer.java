package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class OtherBankTransfer extends Transaction {
	
	int sourceAccuntId , sourceBank;
	int destinationAccuntId , destinationBank;
	
	public OtherBankTransfer(float amount, Date transactionDate , int accountId , int sourceAccuntId , int sourceBank ,	int destinationAccuntId , int destinationBank) throws Exception {
		super(0 , amount, transactionDate , accountId);
		
		this.sourceAccuntId = sourceAccuntId;
		this.sourceBank = sourceBank;
		this.destinationAccuntId = destinationAccuntId;
		this.destinationBank = destinationBank;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertTransaction(this);
	}

	public OtherBankTransfer(int transId , float amount, Date transactionDate , int accountId , int sourceAccuntId , int sourceBank ,	int destinationAccuntId , int destinationBank) throws Exception {
		super(transId , amount, transactionDate , accountId);
		
		this.sourceAccuntId = sourceAccuntId;
		this.sourceBank = sourceBank;
		this.destinationAccuntId = destinationAccuntId;
		this.destinationBank = destinationBank;
	}
	
	@Override
	public String toString() {
		return "OtherBankTransfer [" + super.toString() + ", sourceAccuntId=" + sourceAccuntId + ", sourceBank=" + sourceBank
				+ ", destinationAccuntId=" + destinationAccuntId + ", destinationBank=" + destinationBank + "]";
	}

	public int getSourceAccuntId() {
		return sourceAccuntId;
	}

	public int getSourceBank() {
		return sourceBank;
	}

	public int getDestinationAccuntId() {
		return destinationAccuntId;
	}

	public int getDestinationBank() {
		return destinationBank;
	}
	
}
