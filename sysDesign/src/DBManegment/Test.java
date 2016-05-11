package DBManegment;

import sysDesign.Date;
import sysDesign.Account;

public class Test {

	public static void main(String[] args) {
		
		DataBaseService test = DataBaseService.getDataBaseService();
		
		test.ConnectDataBase();
		
		if (test.insertAccount(3156) == -1)
			System.out.println("fail1");
		
		if (!test.updateAccountBalanceByID(300, 100))
			System.out.println("fail14");
		
		if (!test.insertCustomer(30151, "Eviatar admon"))
			System.out.println("fail2");
		
		if (test.insertSavings(50, new Date(13, 9, 2016), new Date(13,12,2016)) == -1)
			System.out.println("fail3");
		
		if (test.insertLoan(315613, new Date(13, 8, 2016), new Date(14, 8, 2017)) == -1)
			System.out.println("fail4");
		
		if (!test.insertBindCustomerAccount(100, 30151))
			System.out.println("fail5");
		
		if (test.insertTransaction(307, new Date(12, 5, 2016), 100, 1) == -1)
			System.out.println("fail8");	
		
		if (test.insertTransaction(50, new Date(12, 5, 2016), 100, 1) == -1)
			System.out.println("fail13");
		
		if (test.insertTransaction(50, new Date(12, 5, 2016), 1, 100) == -1)
			System.out.println("fail12");
		
		if (!test.insertAccountLoans(100, 200))
			System.out.println("fail6");
		
		if (!test.insertAccountSavings(100, 300))
			System.out.println("fail7");	
	
		if (!test.insertMonthlyTransaction(400, 200, "Loans"))
			System.out.println("fail9");	
	
		if (!test.insertMonthlyTransaction(401, 300, "Savings"))
			System.out.println("fail10");
		
		if (!test.insertOtherBankTransfer(402, 105))
			System.out.println("fail11");
		
		if (test.getAccountByID(100)== null)
			System.out.println("fail16");
		
		else
			System.out.println(test.getAccountByID(100).toString());	
		
		if (test.getCustomerByID(30151)== null)
			System.out.println("fail15");
		
		else
		System.out.println(test.getCustomerByID(30151).getCustomeriD() +" "+ test.getCustomerByID(30151).getCustomerName());
		
		
		
		
		test.DisconnectDataBase();
		

	}

}
