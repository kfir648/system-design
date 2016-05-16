package sysDesign;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class TransactionSubsystem {

	private DatabaseInterface SQLinteface;
	
	public TransactionSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}

	public void insertTransactionHistory(sysDesign.Transaction transaction) throws Exception{
		int TrasactionId= SQLinteface.insertTransaction(transaction);
	}
	
	public Set<sysDesign.Transaction> getHistoryTransactionByAccountID (int id) throws Exception{
		return SQLinteface.getHistoryTransactionByAccountID(id);
	}
	
	
	
	
}
