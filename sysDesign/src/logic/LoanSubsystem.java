package logic;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
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
	
	
	public void insertLoan (float amount,logic.Date startDate, logic.Date finalDate, int accountId) throws Exception{
		SQLinteface.insertAccountLoans(accountId, SQLinteface.insertLoan(amount, startDate, finalDate)); 
		
	}
	
	
	private LoanSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}


	public Set<logic.Loan> getLoansByAccountID (int id) throws SQLException{
		return SQLinteface.getLoansByAccountID(id);
	}
	
	public logic.Loan getLoansByLoanID (int id) throws SQLException{
		return SQLinteface.getLoanById(id);
	}
	
	public Set<logic.Loan> getLoansBycustomerID (int id) throws SQLException{
		Set<Account> Accounts =SQLinteface.getAccountsByCustomerID(id);
		Set<Loan> loans= new LinkedHashSet<>();
		for (Account acc : Accounts){
			loans.addAll(acc.getAllLoans());
		}
		return loans;
	}
	
	public void updateLoanTransaction() throws Exception {
		Map<Integer , Loan> loans = SQLinteface.getRelevantLoans();
		Map<Integer,Set<LoanTransaction>> loanTransactions = SQLinteface.getAllRelevantLoanTransaction();  
		
		for(Entry<Integer, Loan> entry : loans.entrySet())
		
	}
	
}
