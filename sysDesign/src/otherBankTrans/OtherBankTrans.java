package otherBankTrans;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Set;

import api_otherBank.AuthException;
import api_otherBank.IBTS;
import api_otherBank.transf.TransferException;
import api_otherBank.transf.TransferRequestTuple;
import api_otherBank.transf.TransferResult;
import sysDesign.Account;

public class OtherBankTrans {
	private IBTS server;
	private Thread thread;
	private boolean threadWorking;

	private static OtherBankTrans otherBankTrans = null;
	private ArrayList<TransactionListener> newTranslisteners = null;
	private ArrayList<ConformTransactionListener> conformTransactionListeners = null;

	private final static long TIME_TO_CHECK_INCOM_TRANS = 10000; //10 seconds
	private final static String NAME = "IBTS";
	private final static int ID = 255;
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
		if(otherBankTrans == null)
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
	
	public synchronized void addConformTransferListener(ConformTransactionListener listener) {
		if (listener == null)
			return;
		if (conformTransactionListeners == null)
			conformTransactionListeners = new ArrayList<>();
		if (conformTransactionListeners.contains(listener))
			return;
		conformTransactionListeners.add(listener);
	}

	public synchronized void removeConformTransferListener(ConformTransactionListener listener) {
		if (listener == null)
			return;
		if (conformTransactionListeners == null)
			return;
		conformTransactionListeners.remove(listener);
	}

	private OtherBankTrans() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry();
		server = (IBTS) registry.lookup(NAME);
		
		threadWorking = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (threadWorking) {
					try {
						Set<TransferRequestTuple> transferRequestTuples = server.getNewRequestsFor(SECRET, ID, MAX_TRANSACTIONS);
						if (!transferRequestTuples.isEmpty())
							fireNewRequestListeners(transferRequestTuples);
						
						Set<TransferResult> transferResults = server.getCompletedRequestsOf(SECRET, ID, MAX_TRANSACTIONS);
						if(!transferResults.isEmpty())
							fireConformRequestListeners(transferResults);
						Thread.sleep(TIME_TO_CHECK_INCOM_TRANS);
					} catch (InterruptedException | RemoteException | SQLException | AuthException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public boolean sendTransaction(Account sender, Account reciver, int otherBankId, float amount) {
		try {
			server.send(SECRET, getLastRequestId(), ID, sender.getAccountId(), otherBankId, reciver.getAccountId(),
					(int) amount);
			return true;
		} catch (SQLException | AuthException | IOException e) {
			e.printStackTrace();
			return false;
		}

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
		if (conformTransactionListeners == null || conformTransactionListeners.isEmpty())
			return;

		ConformEvent event = new ConformEvent(this, transferResults);
		for (ConformTransactionListener listener : conformTransactionListeners) {
			listener.conformTransaction(event);
		}
	}

	public boolean accept(TransferRequestTuple requestTuple) {
		try {
			server.accept(SECRET, ID, requestTuple.receiverAccountId, requestTuple.id);
			return true;
		} catch (RemoteException | TransferException | SQLException | AuthException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean reject(TransferRequestTuple requestTuple) {
		try {
			server.reject(SECRET, ID, requestTuple.receiverAccountId, requestTuple.id);
			return true;
		} catch (RemoteException | TransferException | SQLException | AuthException e) {
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
			accessFile = new RandomAccessFile(file, "w");
			accessFile.writeInt(START_REQUEST_ID);
		} else {
			accessFile = new RandomAccessFile(file, "wr");
			reqId = accessFile.readInt() + 1;
			accessFile.seek(0);
			accessFile.writeInt(reqId);
		}
		accessFile.close();
		return reqId;
	}

//	public static void main(String[] args) {
//		OtherBankTrans bankTrans = getOtherBankTrans();
//		bankTrans.addNewTransferListener(new TransactionListener() {
//			@Override
//			public void transactionIncomes(TransactionEvent event) {
//				for(TransferRequestTuple requestTuple : event.getTransferRequestTuple())
//				{
//					bankTrans.accept(requestTuple);
//					System.out.println("the transactions " + requestTuple.id + " accepted");
//				}
//			}
//		});
//		bankTrans.addConformTransferListener(new ConformTransactionListener() {
//			@Override
//			public void conformTransaction(ConformEvent event) {
//				for(TransferResult result : event.getTransferResult())
//					System.out.println(result.getRequestId());				
//			}
//		});
//		bankTrans.sendTransaction(new Account(1234, 4321), new Account(43212, 2324), ID, 123);
//	}
}

interface TransactionListener {
	void transactionIncomes(TransactionEvent event);
}

interface ConformTransactionListener {
	void conformTransaction(ConformEvent event);
}

class TransactionEvent extends EventObject {
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

class ConformEvent extends EventObject {
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
