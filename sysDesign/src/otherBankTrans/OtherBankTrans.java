package otherBankTrans;

import ibts.api.AuthException;
import ibts.api.IBTS;
import ibts.api.transf.TransferException;
import ibts.api.transf.TransferRequestTuple;
import ibts.api.transf.TransferResult;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import logic.classes.Account;

public class OtherBankTrans {
	private IBTS server;
	private Thread thread;
	private boolean threadWorking;

	private static OtherBankTrans otherBankTrans = null;
	private ArrayList<TransactionListener> newTranslisteners = null;
	private ArrayList<ConfirmTransactionListener> confirmTransactionListeners = null;

	private final static long TIME_TO_CHECK_INCOM_TRANS = 5000; // 5 seconds
	private final static String NAME = "IBTS";
	public final static int ID = 255;
	private final static String SECRET = "7cufHngk";
	private final static String LAST_REQUEST_ID_FILE = "lastRequestFile.dat";
	private final static int START_REQUEST_ID = 1;
	private final static int MAX_TRANSACTIONS = 10;

	public static OtherBankTrans getOtherBankTrans() {
		try {
			if (otherBankTrans == null)
				otherBankTrans = new OtherBankTrans();
			return otherBankTrans;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public static void closeConnection() {
		if (otherBankTrans == null)
			return;
		otherBankTrans.threadWorking = false;
		try {
			otherBankTrans.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void addNewTransferListener(TransactionListener listener) {
		if (listener == null)
			return;
		if (newTranslisteners == null)
			newTranslisteners = new ArrayList<>();
		if (newTranslisteners.contains(listener))
			return;
		newTranslisteners.add(listener);
	}

	public synchronized void removeNewTransferListener(TransactionListener listener) {
		if (listener == null)
			return;
		if (newTranslisteners == null)
			return;
		newTranslisteners.remove(listener);
	}

	public synchronized void addConformTransferListener(ConfirmTransactionListener listener) {
		if (listener == null)
			return;
		if (confirmTransactionListeners == null)
			confirmTransactionListeners = new ArrayList<>();
		if (confirmTransactionListeners.contains(listener))
			return;
		confirmTransactionListeners.add(listener);
	}

	public synchronized void removeConfirmTransferListener(ConfirmTransactionListener listener) {
		if (listener == null)
			return;
		if (confirmTransactionListeners == null)
			return;
		confirmTransactionListeners.remove(listener);
	}

	private OtherBankTrans() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("172.20.18.34");
		server =  (IBTS) registry.lookup(NAME);

		threadWorking = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (threadWorking) {
					try {
						Set<TransferRequestTuple> transferRequestTuples = server.getNewRequestsFor(SECRET, ID,
								MAX_TRANSACTIONS);
						if (transferRequestTuples != null && !transferRequestTuples.isEmpty())
							fireNewRequestListeners(transferRequestTuples);

						Set<TransferResult> transferResults = server.getCompletedRequestsOf(SECRET, ID,
								MAX_TRANSACTIONS);
						if (transferRequestTuples != null && !transferResults.isEmpty())
							fireConformRequestListeners(transferResults);
						Thread.sleep(TIME_TO_CHECK_INCOM_TRANS);
					} catch (AuthException | RemoteException | SQLException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public void deleteConfirmRequest(int senderBankId , int senderAccountId , int reqId) throws RemoteException, TransferException, SQLException, AuthException {
		server.deleteRequest(SECRET,senderBankId , senderAccountId , reqId);
	}
	
	public int sendTransaction(Account sender, Account reciver, int otherBankId, float amount)
			throws SQLException, AuthException, IOException {
		int reqId = getLastRequestId();
		server.send(SECRET, reqId, ID, sender.getAccountId(), otherBankId, reciver.getAccountId(), (int) amount);
		return reqId;
	}

	private void fireNewRequestListeners(Set<TransferRequestTuple> transferRequestTuple) {
		if (newTranslisteners == null || newTranslisteners.isEmpty())
			return;

		TransactionEvent event = new TransactionEvent(this, transferRequestTuple);
		for (TransactionListener listener : newTranslisteners) {
			listener.transactionIncomes(event);
		}
	}

	private void fireConformRequestListeners(Set<TransferResult> transferResults) {
		if (confirmTransactionListeners == null || confirmTransactionListeners.isEmpty())
			return;

		ConformEvent event = new ConformEvent(this, transferResults);
		for (ConfirmTransactionListener listener : confirmTransactionListeners) {
			listener.conformTransaction(event);
		}
	}

	public int accept(TransferRequestTuple requestTuple) throws RemoteException, TransferException, SQLException, AuthException {
		server.accept(SECRET, ID, requestTuple.receiverAccountId, requestTuple.id);
		return requestTuple.id;
	}

	public boolean reject(TransferRequestTuple requestTuple) {
		
			try {
				server.reject(SECRET, ID, requestTuple.receiverAccountId, requestTuple.id);
				return true;
			} catch (RemoteException | TransferException | SQLException | AuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}

	private int getLastRequestId() throws IOException {
		int reqId;
		File file = new File(LAST_REQUEST_ID_FILE);
		RandomAccessFile accessFile;
		if (!file.exists()) {
			reqId = START_REQUEST_ID;
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.writeInt(START_REQUEST_ID);
		} else {
			accessFile = new RandomAccessFile(file, "rw");
			reqId = accessFile.readInt() + 1;
			accessFile.seek(0);
			accessFile.writeInt(reqId);
		}
		accessFile.close();
		return reqId;
	}
}
