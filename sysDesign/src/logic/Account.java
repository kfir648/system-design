package logic;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Account {

	private int accountId;
	private float accountBalance;
	
	public Account(int accountId, float accountBalance) {
		this.accountId = accountId;
		this.accountBalance = accountBalance;
	}
	
	public Account(float accountBalance) throws SQLException {
		this.accountBalance = accountBalance;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		this.accountId = db.insertAccount(accountBalance);
	}
	
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) throws Exception {
		this.accountBalance = accountBalance;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateAccountBalanceByID(accountBalance, accountId);
	}
	public int getAccountId() {
		return accountId;
	}

	public void addCustomer(Customer customer) throws Exception {
		if(customer == null)
			throw new Exception("The customer is null");
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertBindCustomerAccount(accountId, customer.getCustomerId());
	}

	public Set<Customer> getAllCustomers() throws Exception {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getCustomersByAccountID(accountId);
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountBalance=" + accountBalance + "]";
	}

	public Set<Loan> getAllLoans() throws SQLException {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getLoansByAccountID(accountId);
	}
	
	public Set<Saving> getAllSaivingsByAccountID() throws SQLException {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getSavingByAccountID(accountId);
	}

	public Set<Transaction> getAllTransaction() throws Exception {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getHistoryTransactionByAccountID(accountId);
	}
	
	
	
}
