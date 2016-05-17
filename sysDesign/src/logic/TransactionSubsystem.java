package logic;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class TransactionSubsystem {

	private DatabaseInterface SQLinteface;
	
	private static TransactionSubsystem subsystem;
	
	public static TransactionSubsystem getTransctionSubsystem() throws SQLException {
		if(subsystem == null)
			subsystem = new TransactionSubsystem();
		return subsystem;
	}
	
	private TransactionSubsystem() throws SQLException {
		SQLinteface = DataBaseService.getDataBaseService();
	}

	public void insertTransactionHistory(logic.Transaction transaction) throws Exception{
		SQLinteface.insertTransaction(transaction);
	}
	
	public Set<logic.Transaction> getHistoryTransactionByAccountID (int id) throws Exception{
		return SQLinteface.getHistoryTransactionByAccountID(id);
	}
	
	public OtherBankTransfer getOtherBankTransByReqId(int reqId) throws Exception {
		return SQLinteface.getOtherBankTransByReqId(reqId);
	}
	
	
}
