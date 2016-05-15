package sysDesign;

import java.util.Set;

public class loanSubsystem {

	private SQL.DatabaseInterface SQLinteface= SQL.DatabaseInterface.getDatabaseService();
	
	
	public boolean insertLoan (float amount,sysDesign.Date startDate, sysDesign.Date finalDate, int accountId){
		return SQLinteface.insertLoan(amount, startDate, finalDate, accountId);
	}
	
	
	public Set<sysDesign.Loan> getLoansByAccountID (int id){
		return SQLinteface.getLoansByAccountID(id);
	}
	
}
