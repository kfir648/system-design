package DBManegment;

import java.util.Set;

import sysDesign.Account;
import sysDesign.Bank;
import sysDesign.Customer;
import sysDesign.Loan;
import sysDesign.Saving;
import sysDesign.Transaction;

public interface DatabaseInterface {

	public int insertAccount(float balance);

	public boolean insertCustomer(int customerId, String customerName);

	public int insertLoan(float amount, sysDesign.Date startDate,
			sysDesign.Date finalDate); // a program of loan

	public int insertSavings(float monthlyDeposit, sysDesign.Date startDate,
			sysDesign.Date finalDate); // a program of saving

	public int insertTransaction(float amount, sysDesign.Date date,
			int sourceID, int DestinationID); // LOGIC responosibility to check
												// account exists

	public boolean insertMonthlyTransaction(int transactionID, int typeID,
			String type); // type == loanid / savingid

	public boolean insertOtherBankTransfer(int transactionID, int transferID); // transfer
																				// id
																				// from
																				// other
																				// bank,
																				// transaction
																				// on
																				// this
																				// bank.

	public boolean insertBindCustomerAccount(int accountId, int customerId); // binds
																				// in
																				// logic
																				// customer
																				// with
																				// account.

	public boolean insertAccountLoans(int accountID, int loanID); // bind
																	// account
																	// to loans

	public boolean insertAccountSavings(int accountID, int savingID); // bind
																		// savings
																		// to
																		// loans

	public boolean updateAccountBalanceByID(float amount, int id); // update
																	// (not the
																	// profit)
																	// new
																	// amount

	public boolean updateBankNumber(int bankNumber);

	public boolean updateBankName(String bankName);

	public boolean updateBankLoanInterest(float interest);

	public boolean updateBankSavingInterest(float interest);

	public Bank getBank();

	public Account getAccountByID(int id);

	public Customer getCustomerByID(int id);

	public Set<Account> getAccountsByCustomerID(int id);

	public Set<Customer> getCustomersByAccountID(int id);

	public Set<Loan> getLoansByAccountID(int id);

	public Set<Saving> getSavingByAccountID(int id);

	public Set<Transaction> getHistoryTransactionByAccountID(int id);

	int insertTransaction(Transaction transaction) throws Exception;
	
	public Loan getLoanById(int id);
	public Saving getSavingById(int id);
}
