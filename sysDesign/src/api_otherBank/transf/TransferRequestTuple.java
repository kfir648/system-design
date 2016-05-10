package api_otherBank.transf;

import java.util.Date;

public class TransferRequestTuple {

    public static final int NEW = 1;
    public static final int DONE = 2;
    public static final int UNDETERMINED = 30;
    public static final int ACCEPT = 31;
    public static final int REJECT = 32;
    
    public TransferRequestTuple(int id, int senderBankId, int senderAccountId, int receiverBankId, int receiverAccountId, int amount, int status, int outcome, Date arrivalDate) {
        
        this.id = id;
        this.senderBankId = senderBankId;
        this.senderAccountId = senderAccountId;
        this.receiverBankId = receiverBankId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.status = status;
        this.outcome = outcome;
        this.arrivalDate = arrivalDate;
        
    }
    
    public void dump() {
        System.out.print(arrivalDate + ": ");
        if (status == NEW)
			System.out.print("NEW");
		else 
			System.out.print("DONE");
		System.out.print(": ");
		if (outcome == UNDETERMINED)
			System.out.print("UNDET");
		else if (outcome == ACCEPT)
			System.out.print("ACCEPTED");
		else 
			System.out.print("REJECTED");
		System.out.print(": ");
		System.out.print(amount + " ILS, from account no. " + senderAccountId + " at bank no. " + senderBankId + " to account no. " + receiverAccountId + " at bank no. " + receiverBankId);
    }
    
    public int id;
    public int senderBankId;
	public int senderAccountId;
    public int receiverBankId;
	public int receiverAccountId;
	public int amount;
	public int status;
	public int outcome;
    public Date arrivalDate;
    
}