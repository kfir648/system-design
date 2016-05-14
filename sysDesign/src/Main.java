import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import sysDesign.*;

public class Main
{
    public static void main(String[] args)
    {
    	 try {
			Account acc = new Account(0);
			acc.setAccountBalance(100);
			
			DatabaseInterface db = DataBaseService.getDataBaseService();
			Account acc2 = db.getAccountByID(1000); 
			
			Customer customer = new Customer(12352, "lior");
			acc.addCustomer(customer);
			
			Set<Account> accounts = customer.getAllAccounts();
			for(Account account : accounts)
			{
				System.out.println("" + account + " , " + customer);
			}
			
			Set<Customer> customers = acc2.getAllCustomers();
			for(Customer cus : customers)
				System.out.println("" + cus + " , " + acc2);
			
			db.DisconnectDataBase();
			//Set<Customer> customers = acc.getAllCustomers();
		} catch (SQLException e) {
			e.printStackTrace();
			
			System.err.println(e.getMessage());
			System.err.println(e.getSQLState());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
