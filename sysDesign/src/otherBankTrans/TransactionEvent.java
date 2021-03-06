package otherBankTrans;

import ibts.api.transf.TransferRequestTuple;

import java.util.EventObject;
import java.util.Set;

public class TransactionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Set<TransferRequestTuple> transferRequestTuple;

	public TransactionEvent(Object source, Set<TransferRequestTuple> transferRequestTuple) {
		super(source);
		this.transferRequestTuple = transferRequestTuple;
	}

	public Set<TransferRequestTuple> getTransferRequestTuple() {
		return transferRequestTuple;
	}
}