import java.sql.SQLException;
import java.util.Set;

import sysDesign.*;

public class Main
{
    public static void main(String[] args)
    {
    	 try {
			Account acc = new Account(0);
			acc.setAccountBalance(100);
			
			Customer customer = new Customer(12352, "lior");
			acc.addCustomer(customer);
			
			Customer customer2 = new Customer(12352, "lior");
			
			Set<Account> accounts = customer.getAllAccounts();
			for(Account account : accounts)
			{
				System.out.println("" + account + acc);
			}
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
