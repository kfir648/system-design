package logic.subsystem;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import logic.classes.*;

public class LoanSubsystem {
	private static LoanSubsystem loanSubsystem=null;
	private DatabaseInterface SQLinteface;
	
	public static LoanSubsystem getLoanSubsystem() throws SQLException{
		if (loanSubsystem==null){
			loanSubsystem=new LoanSubsystem();
		}
		return loanSubsystem;
	}
	
	
	public void insertLoan (float amount,Date startDate, Date finalDate, int accountId) throws Exception{
		SQLinteface.insertLoan(amount, startDate, finalDate , accountId); 
	}
	
	
	private LoanSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}


	public Set<Loan> getLoansByAccountID (int id) throws SQLException{
		return SQLinteface.getLoansByAccountID(id);
	}
	
	public Loan getLoansByLoanID (int id) throws SQLException{
		return SQLinteface.getLoanById(id);
	}
	
	public Set<Loan> getLoansBycustomerID (int id) throws SQLException{
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
		
		for(Entry<Integer, Loan> entry : loans.entrySet()){
			int numOfPayments=0;
			Date startDate=entry.getValue().getFirstPaymentDate();
			numOfPayments=startDate.howManyMonths(startDate.getNow());
			for(int i=0;i<numOfPayments;i++){
				MonthlyTransaction transaction = new MonthlyTransaction(entry.getValue().getLoanId(),entry.getValue().getAmount(),startDate,entry.getValue().getAccountId(),i,entry.getValue().getFinalDate());
				
				startDate=startDate.next();
				loanSubsystem.SQLinteface.insertTransaction(transaction);
				
			}
		}
		
	}
	
}
