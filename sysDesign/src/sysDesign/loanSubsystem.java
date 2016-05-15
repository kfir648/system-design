package sysDesign;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class loanSubsystem {

	private DatabaseInterface SQLinteface;
	
	public void insertLoan (float amount,sysDesign.Date startDate, sysDesign.Date finalDate, int accountId) throws Exception{
		SQLinteface.insertAccountLoans(accountId, SQLinteface.insertLoan(amount, startDate, finalDate)); 
		
	}
	
	
	public loanSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}


	public Set<sysDesign.Loan> getLoansByAccountID (int id) throws SQLException{
		return SQLinteface.getLoansByAccountID(id);
	}
	
}
