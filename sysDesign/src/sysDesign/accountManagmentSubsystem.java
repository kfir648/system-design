package sysDesign;

import java.util.Set;

public class accountManagmentSubsystem {

	private SQL.DatabaseInterface SQLinteface= SQL.DatabaseInterface.getDatabaseService();
	
	
	
	public boolean insertAccount(float balance){
		
		return SQLinteface.insertAccount(balance);
	}
	
	public sysDesign.Account getAccountByID (int id){
		return SQLinteface.getAccountByID(id);
	}
	
	
	public Set<sysDesign.Account> getAccountsByCustomerID (int id){
		
		return SQLinteface.getAccountsByCustomerID(id);
	}
	
	
	public sysDesign.Customer getCustomerByID (int id){
		return SQLinteface.getCustomerByID(id);
	}

	public Set<sysDesign.Customer> getCustomersByAccountID (int id){
		return SQLinteface.getCustomersByAccountID(id);
	}
	
	public boolean updateAccountBalanceByID(float amount , int id){
		return SQLinteface.updateAccountBalanceByID(amount, id);
	}
	
	
}
