package DBManegment;

import java.util.Set;

public interface DatabaseInterface {

	public int insertAccount(float balance);
	public boolean insertCustomer (int customerId, String customerName);
	public int insertLoan (float amount, sysDesign.Date startDate, sysDesign.Date finalDate); // a program of loan
	public int insertSavings(float monthlyDeposit, sysDesign.Date startDate, sysDesign.Date finalDate);  // a program of saving
	public int insertTransaction(float amount, sysDesign.Date date, int sourceID , int DestinationID);    // LOGIC responosibility to check account exists
	public boolean insertMonthlyTransaction(int transactionID, int typeID, String type);   // type == loanid / savingid
	public boolean insertOtherBankTransfer(int transactionID, int transferID);   // transfer id from other bank, transaction on this bank.
	public boolean insertBindCustomerAccount(int accountId, int customerId);  // binds in logic customer with account.
	public boolean insertAccountLoans(int accountID, int loanID);   // bind account to loans
	public boolean insertAccountSavings(int accountID, int savingID); // bind savings to loans
	public boolean updateAccountBalanceByID(float amount, int id); // update (not the profit) new amount
	public boolean updateBankNumber(int bankNumber);
	public boolean updateBankName (String bankName);
	public boolean updateBankLoanInterest (float interest);
	public boolean updateBankSavingInterest (float interest);
	public sysDesign.Bank getBank();
	public sysDesign.Account getAccountByID (int id);
	public sysDesign.Customer getCustomerByID (int id);
	public Set<sysDesign.Account> getAccountsByCustomerID (int id);
	public Set<sysDesign.Customer> getCustomersByAccountID (int id);
	public Set<sysDesign.Loan> getLoansByAccountID (int id);
	public Set<sysDesign.Savings> getSavingByAccountID (int id);
	public Set<sysDesign.Transaction> getHistoryTransactionByAccountID (int id);
	
	
	
}
