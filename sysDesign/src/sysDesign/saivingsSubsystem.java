package sysDesign;

import java.util.Set;

public class saivingsSubsystem {

	private SQL.DatabaseInterface SQLinteface= SQL.DatabaseInterface.getDatabaseService();
	
	public boolean createSavings(float amount , Date startDate, Date finalDate,int accountId){
			return SQLinteface.insertSavings(amount, startDate, finalDate, accountId);
		}
	
	public Set<sysDesign.Savings> getSavingByAccountID (int id){
		return SQLinteface.getSavingByAccountID(id);
	}
	
	
}
