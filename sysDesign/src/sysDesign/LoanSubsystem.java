package sysDesign;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class LoanSubsystem {
	private static LoanSubsystem loanSubsystem=null;
	private DatabaseInterface SQLinteface;
	
	public static LoanSubsystem getLoanSubsystem() throws SQLException{ ///////
		if (loanSubsystem==null){
			loanSubsystem=new LoanSubsystem();
		}
		return loanSubsystem;
	}
	
	
	public void insertLoan (float amount,sysDesign.Date startDate, sysDesign.Date finalDate, int accountId) throws Exception{
		SQLinteface.insertAccountLoans(accountId, SQLinteface.insertLoan(amount, startDate, finalDate)); 
		
	}
	
	
	private LoanSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}


	public Set<sysDesign.Loan> getLoansByAccountID (int id) throws SQLException{
		return SQLinteface.getLoansByAccountID(id);
	}
	
	public sysDesign.Loan getLoansByLoanID (int id) throws SQLException{
		return SQLinteface.getLoanById(id);
	}
	
	public Set<sysDesign.Loan> getLoansBycustomerID (int id) throws SQLException{
		Set<Account> Accounts =SQLinteface.getAccountsByCustomerID(id);
		Set<Loan> loans= new LinkedHashSet<>();
		for (Account acc : Accounts){
			loans.addAll(acc.getAllLoans());
		}
		return loans;
	}
	
}
