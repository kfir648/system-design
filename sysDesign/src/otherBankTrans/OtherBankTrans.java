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
import sysDesign.Account;

public class OtherBankTrans {
	private IBTS server;
	private Thread thread;
	private boolean threadWorking;

	private static OtherBankTrans otherBankTrans = null;
	private ArrayList<TransactionListener> listeners = null;

	private final static long TIME_TO_CHECK_INCOM_TRANS = 10000;
	private final static String NAME = "IBTS";
	private final static int ID = 255;
	private final static String SECRET = "7cufHngk";
	private final static String LAST_REQUEST_ID_FILE = "lastRequestFile.dat";
	private final static int START_REQUEST_ID = 1;
	private final static int MAX_TRANSACTIONS = 10;

	public static OtherBankTrans getBankTrans() {
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

	public synchronized void addListener(TransactionListener listener) {
		if (listener == null)
			return;
		if (listeners == null)
			listeners = new ArrayList<>();
		if (listeners.contains(listener))
			return;
		listeners.add(listener);
	}

	public void removeListener(TransactionListener listener) {
		if (listener == null)
			return;
		if (listeners == null)
			return;
		listeners.remove(listener);
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
							fireListeners(transferRequestTuples);
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

	private void fireListeners(Set<TransferRequestTuple> transferRequestTuple) {
		if (listeners == null || listeners.isEmpty())
			return;

		TransactionEvent event = new TransactionEvent(this, transferRequestTuple);
		for (TransactionListener listener : listeners) {
			listener.transactionIncomes(event);
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
			accessFile.close();
		} else {
			accessFile = new RandomAccessFile(file, "wr");
			reqId = accessFile.readInt() + 1;
			accessFile.seek(0);
			accessFile.writeInt(reqId);
		}
		return reqId;
	}

	public static void main(String[] args) {
		try {
			String name = "IBTS";
			Registry registry = LocateRegistry.getRegistry(args[0]);
			IBTS server = (IBTS) registry.lookup(name);

			int myId = 666;
			String mySecret = "secret";

			Set<TransferRequestTuple> incomingReqs = server.getNewRequestsFor(mySecret, myId, 3);
			for (TransferRequestTuple reqTuple : incomingReqs) {
				server.accept(mySecret, myId, reqTuple.receiverAccountId, reqTuple.id);
			}

		} catch (Exception e) {
			System.err.println("DemoClient exception:");
			e.printStackTrace();
		}
	}
}

interface TransactionListener {
	void transactionIncomes(TransactionEvent event);
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
