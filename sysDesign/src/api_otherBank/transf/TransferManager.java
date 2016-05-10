package api_otherBank.transf;

import java.util.Set;

import java.sql.SQLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TransferManager extends Remote {

    // Sender interface
    
	void send(int reqId, int senderBankId, int senderAccountId, int receiverBankId, int receiverAccountId, int amount) throws RemoteException, SQLException;
    
	public Set<TransferResult> getCompletedRequestsOf(int senderBankId, int numReqs) throws RemoteException, SQLException;

	public void deleteRequest(int senderBankId, int senderAccountId, int reqId) throws RemoteException, TransferException, SQLException;

    // Receiver interface
    
	public Set<TransferRequestTuple> getNewRequestsFor(int receiverBankId, int numReqs) throws RemoteException, SQLException;

 	public void accept(int receiverBankId, int accountId, int reqId) throws RemoteException, TransferException, SQLException;

	public void reject(int receiverBankId, int accountId, int reqId) throws RemoteException, TransferException, SQLException;

}