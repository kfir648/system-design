package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Account {

	private int accountId;
	private float accountBalance;
	
	public Account(int accountId, float accountBalance) {
		this.accountId = accountId;
		this.accountBalance = accountBalance;
	}
	
	public Account(float accountBalance) {
		this.accountBalance = accountBalance;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		this.accountId = db.insertAccount(accountBalance);
	}
	
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateAccountBalanceByID(accountBalance, accountId);
	}
	public int getAccountId() {
		return accountId;
	}
}
