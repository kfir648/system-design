package sysDesign;

public class Account {

	private int accountId;
	private float accountBalance;
	
	
	public Account(int accountId, float accountBalance) {
		this.accountId = accountId;
		this.accountBalance = accountBalance;
	}
	
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}
	public int getAccountId() {
		return accountId;
	}
	
	
	
	
}
