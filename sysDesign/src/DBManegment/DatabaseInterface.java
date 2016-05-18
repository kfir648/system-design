package DBManegment;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import logic.Account;
import logic.Bank;
import logic.Customer;
import logic.Date;
import logic.Loan;
import logic.LoanTransaction;
import logic.OtherBankTransfer;
import logic.Saving;
import logic.SavingTransaction;
import logic.Transaction;
import logic.Worker;
import logic.Worker.PermissionType;

public interface DatabaseInterface {

	public int insertAccount(float balance) throws SQLException;

	public void insertCustomer(int customerId, String customerName) throws Exception;

	public int insertLoan(float amount, logic.Date startDate,
			logic.Date finalDate) throws Exception;

	public int insertSaving(float monthlyDeposit, logic.Date startDate, logic.Date finalDate) throws Exception;
	
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

	public Account getAccountByID(int id) throws Exception;

	public Customer getCustomerByID(int id) throws Exception;

	public Set<Account> getAccountsByCustomerID(int id) throws SQLException;

	public Set<Customer> getCustomersByAccountID(int id) throws Exception;

	public Set<Loan> getLoansByAccountID(int id) throws SQLException;

	public Set<Saving> getSavingByAccountID(int id) throws SQLException;

	public Set<Transaction> getHistoryTransactionByAccountID(int id) throws Exception;
	
	public Transaction getTransactionById(int id) throws Exception;

	public Loan getLoanById(int id) throws SQLException;
	
	public Saving getSavingById(int id) throws SQLException;

	public void updateCustomer(int customeriD, String customerName) throws SQLException;

	public void updateLoan(int loanId, float amount, Date firstPaymentDate, Date finalDate) throws SQLException;

	public void updateSaving(int savingId, float monthlyPaymentNumber, Date startSavingDate, Date finalSavingsDate) throws SQLException;
	
	public void DisconnectDataBase();
	
	public ResultSet queryTest(String sql) throws SQLException;
	
	public boolean conformUserName(String userName , String passward) throws SQLException;
	
	public PermissionType getPermissions(Worker user) throws SQLException, Exception;
	
	public void insertWorker(Worker manager, Worker worker , PermissionType permissionType) throws Exception;

	public void acceptOtherBankTransfer(int transId) throws SQLException;

	public void rejectOtherBankTransfer(int transId) throws SQLException;

	public OtherBankTransfer getOtherBankTransByReqId(int reqId)throws SQLException, Exception;

	public Map<Integer , Loan> getRelevantLoans()throws SQLException;
	
	public void setLoanIrrelevant(int loanId) throws SQLException;

	public Map<Integer, Set<LoanTransaction>> getAllRelevantLoanTransaction() throws Exception;

	public Map<Integer, Saving> getRelevantSavings() throws Exception;

	public Map<Integer, Set<SavingTransaction>> getAllRelevantSavingTransaction() throws Exception ;
	
	public void setSavingIrrelevant(int loanId) throws SQLException;
}
