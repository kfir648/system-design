package sysDesign;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class AccountManagmentSubsystem {
	private static  AccountManagmentSubsystem accountSubsystem=null;
	private DatabaseInterface SQLinteface;
	
	
	public static AccountManagmentSubsystem getaccountSubsystem() throws SQLException{ ///////
		if (accountSubsystem==null){
			accountSubsystem=new AccountManagmentSubsystem();
		}
		return accountSubsystem;
	}
	
	
	
	private AccountManagmentSubsystem() throws SQLException {
		SQLinteface= DataBaseService.getDataBaseService();
	}

	public void insertAccount(float balance) throws SQLException{
		 SQLinteface.insertAccount(balance);
	}
	
	public sysDesign.Account getAccountByID (int id) throws Exception{
		return SQLinteface.getAccountByID(id);
	}
	
	
	public Set<sysDesign.Account> getAccountsByCustomerID (int id) throws SQLException{
	
		return SQLinteface.getAccountsByCustomerID(id);
	}
	
	
	public sysDesign.Customer getCustomerByID (int id) throws Exception{
		return SQLinteface.getCustomerByID(id);
	}

	public Set<sysDesign.Customer> getCustomersByAccountID (int id) throws Exception{
		return SQLinteface.getCustomersByAccountID(id);
	}
	
	public void updateAccountBalanceByID(float amount , int id) throws Exception{
		 SQLinteface.updateAccountBalanceByID(amount, id);
	}
	
	
}