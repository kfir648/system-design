package api_otherBank;
import java.util.Set;

import java.sql.SQLException;

import java.io.IOException;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api_otherBank.transf.TransferException;
import api_otherBank.transf.TransferResult;
import api_otherBank.transf.TransferRequestTuple;

import api_otherBank.AuthException;

public interface IBTS extends Remote {

    int allocateBlock(int numIds) throws RemoteException, IOException;

    // Sender interface
    
	void send(String secret, int reqId, int senderBankId, int senderAccountId, int receiverBankId, int receiverAccountId, int amount) throws RemoteException, SQLException, AuthException;
    
	public Set<TransferResult> getCompletedRequestsOf(String secret, int senderBankId, int numReqs) throws RemoteException, SQLException, AuthException;

	public void deleteRequest(String secret, int senderBankId, int senderAccountId, int reqId) throws RemoteException, TransferException, SQLException, AuthException;

    // Receiver interface
    
	public Set<TransferRequestTuple> getNewRequestsFor(String secret, int receiverBankId, int numReqs) throws RemoteException, SQLException, AuthException;

 	public void accept(String secret, int receiverBankId, int accountId, int reqId) throws RemoteException, TransferException, SQLException, AuthException;

	public void reject(String secret, int receiverBankId, int accountId, int reqId) throws RemoteException, TransferException, SQLException, AuthException;

}
