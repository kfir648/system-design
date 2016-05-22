package logic.classes;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Customer {

	private int customerId;
	private String customerName;
	
	public Customer(int customerId, String customerName) throws Exception {
		this.customerId = customerId;
		this.customerName = customerName;
		
		try{
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertCustomer(customerId, customerName);
		}catch(SQLException e)
		{
			if(e.getSQLState() == "23505")
				return;
			throw e;
		}
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) throws SQLException {
		this.customerName = customerName;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateCustomer(customerId , customerName);
	}
	public int getCustomerId() {
		return customerId;
	}
	
	public Set<Account> getAllAccounts() throws SQLException {
		return DataBaseService.getDataBaseService().getAccountsByCustomerID(customerId);
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + "]";
	}

	public void addAccount(Account acc) throws Exception {
		if(acc == null)
			throw new Exception("The account you enter is null");
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertBindCustomerAccount(acc.getAccountId(), customerId);
	}

}
