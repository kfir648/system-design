package logic.classes;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class OtherBankTransfer extends Transaction {
	
	private int sourceAccuntId , sourceBank;
	private int destinationAccuntId , destinationBank;
	
	private int reqId;
	private boolean accepted = false; 
	
	public OtherBankTransfer( float amount, Date transactionDate , int accountId , int sourceAccuntId , int sourceBank , int destinationAccuntId , int destinationBank , int reqId) throws Exception {
		this(0 , amount , transactionDate , accountId , sourceAccuntId , sourceBank , destinationAccuntId , destinationBank ,reqId);
		DatabaseInterface db = DataBaseService.getDataBaseService();
		this.setTransId(db.insertTransaction(this));
		if(destinationAccuntId == getAccuntId())
			acceptTransfer();
	}

	public OtherBankTransfer(int transId  , float amount, Date transactionDate , int accountId , int sourceAccuntId , int sourceBank ,	int destinationAccuntId , int destinationBank, int reqId) throws Exception {
		super(transId , amount, transactionDate , accountId);
		
		this.reqId = reqId;
		this.sourceAccuntId = sourceAccuntId;
		this.sourceBank = sourceBank;
		this.destinationAccuntId = destinationAccuntId;
		this.destinationBank = destinationBank;
	}
	
	public void acceptTransfer() throws SQLException {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.acceptOtherBankTransfer(getTransId());
		accepted = true;
	}
	
	public void rejectTransfer() throws SQLException {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.rejectOtherBankTransfer(getTransId());
		accepted = false;
	}
	
	@Override
	public String toString() {
		return "Other Bank Transfer " + super.toString();
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

	@Override
	public String showAllDetails() throws SQLException {
		if(sourceBank == 255)
		{
			return sourceAccuntId + " --> " + getAmount() + " --> " + destinationAccuntId + " Bank: " + destinationBank + ", " + (accepted?"Completed":"Incomplete");
		}else{
			return destinationAccuntId + " <-- " + getAccuntId() + " <-- " + sourceAccuntId + " Banke: " + sourceBank + ", " + (accepted?"Completed":"Incomplete"); 
		}
	}

	public int getReqId() {
		return reqId;
	}
	
}
