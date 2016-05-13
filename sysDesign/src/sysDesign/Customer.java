package sysDesign;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;

public class Customer {

	private int customeriD;
	private String customerName;
	
	public Customer(int customeriD, String customerName) {
		this.customeriD = customeriD;
		this.customerName = customerName;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getCustomeriD() {
		return customeriD;
	}
	
	public Set<Account> getAllAccounts() throws SQLException {
		return DataBaseService.getDataBaseService().getAccountsByCustomerID(customeriD);
	}

}
