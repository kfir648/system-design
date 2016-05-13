package DBManegment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import sysDesign.Account;
import sysDesign.Bank;
import sysDesign.Customer;
import sysDesign.Loan;
import sysDesign.Saving;
import sysDesign.Transaction;

public interface DatabaseInterface {

	public int insertAccount(float balance) throws SQLException;

	public void insertCustomer(int customerId, String customerName) throws Exception;

	public int insertLoan(float amount, sysDesign.Date startDate,
			sysDesign.Date finalDate) throws Exception; // a program of loan

	public int insertSaving(float monthlyDeposit, sysDesign.Date startDate, sysDesign.Date finalDate) throws Exception; // a program of saving
	
	public void insertBindCustomerAccount(int accountId, int customerId) throws Exception;

	public void insertAccountLoans(int accountID, int loanID) throws Exception; 

	public void insertAccountSavings(int accountID, int savingID) throws Exception; 

	public int insertTransaction(Transaction transaction) throws Exception;
	
	public void updateAccountBalanceByID(float amount, int id) throws Exception; 

	public void updateBankNumber(int bankNumber) throws IOException;

	public void updateBankName(String bankName) throws IOException;

	public void updateBankLoanInterest(float interest) throws IOException;

	public void updateBankSavingInterest(float interest) throws IOException;

	public Bank getBank() throws IOException;

	public Account getAccountByID(int id) throws SQLException;

	public Customer getCustomerByID(int id) throws SQLException;

	public Set<Account> getAccountsByCustomerID(int id) throws SQLException;

	public Set<Customer> getCustomersByAccountID(int id) throws SQLException;

	public Set<Loan> getLoansByAccountID(int id) throws SQLException;

	public Set<Saving> getSavingByAccountID(int id) throws SQLException;

	public Set<Transaction> getHistoryTransactionByAccountID(int id) throws Exception;
	
	public Transaction getTransactionById(int id) throws Exception;

	public Loan getLoanById(int id) throws SQLException;
	
	public Saving getSavingById(int id) throws SQLException;
}
