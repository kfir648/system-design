package otherBankTrans;

import ibts.api.transf.TransferResult;

import java.util.EventObject;
import java.util.Set;


public class ConformEvent extends EventObject {
	private static final long serialVersionUID = 2L;
	private Set<TransferResult> transferResults;
	
	public ConformEvent(Object source , Set<TransferResult> transferResults) {
		super(source);
		this.transferResults = transferResults;
	}
	
	public Set<TransferResult> getTransferResult() {
		return transferResults;
	}
	
}