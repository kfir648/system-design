package sysDesign;

import java.util.Set;

public class transactionSubsystem {

	private SQL.DatabaseInterface SQLinteface= SQL.DatabaseInterface.getDatabaseService();
	
	public boolean insertTransactionHistory(sysDesign.Transaction transaction){
		return SQLinteface.insertTransactionHistory(transaction);
	}
	
	public Set<sysDesign.Transaction> getHistoryTransactionByAccountID (int id){
		return SQLinteface.getHistoryTransactionByAccountID(id);
	}
	
	
	
	
}
