package DBManegment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseInstall {

	private static String dbName;
	private static String password;
	private static String userName;
	private static Properties props;
	private static Connection conn = null;
	private static Statement s;
	private static String protocol = "jdbc:derby:";

	String table[] = {
			"Account" ,
			"Customer" ,
			"Customer_Account" ,
			"Loan" ,
			"Saving" ,
			"Account_Loans" ,
			"Account_Savings" ,
			"Transactions" ,
			"Same_Bank_Transfer" ,
			"Other_Bank_Transfer" ,
			"Saving_transfer" ,
			"Loan_transfer"};

	String query[] = {
			"Account("
			+ " account_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
			+ " balance float NOT NULL,"
			+ " PRIMARY KEY (account_ID))",

			"Customer("
			+ " customer_ID int NOT NULL,"
			+ " name varchar(40) NOT NULL,"
			+ " PRIMARY KEY (customer_ID))",

			"Customer_Account("
			+ " account_ID int NOT NULL,"
			+ " customer_ID int NOT NULL,"
			+ " FOREIGN KEY (customer_ID) REFERENCES Customer(customer_ID),"
			+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID))",

			"Loan("
			+ " loan_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
			+ " amount float,"
			+ " start_Date varchar(12),"
			+ " final_Date varchar(12),"
			+ " PRIMARY KEY (loan_ID))",

			"Saving("
			+ " saving_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000,INCREMENT BY 1),"
			+ " monthly_Deposit float NOT NULL,"
			+ " start_Date varchar(12),"
			+ " final_Date varchar(12),"
			+ " PRIMARY KEY (saving_ID))",

			"Account_Loans("
			+ " account_ID int,"
			+ " loan_ID int,"
			+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID),"
			+ " FOREIGN KEY (loan_ID) REFERENCES Loan(loan_ID))",

			"Account_Savings("
			+ " account_ID int,"
			+ " saving_ID int,"
			+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID),"
			+ " FOREIGN KEY (saving_ID) REFERENCES Saving(saving_ID))",

			"Transactions("
			+ " transaction_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
			+ " amount float,"
			+ " Date varchar(12),"
			+ " trans_Type int , "
			+ " account_id int ,"
			+ " PRIMARY KEY (transaction_ID),"
			+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID))",

			"Same_Bank_Transfer("
			+ " transaction_ID int,"
			+ " source_Id int ,"
			+ " dest_Id int ,"
			+ " FOREIGN KEY (transaction_ID) REFERENCES Transactions (transaction_ID),"
			+ " FOREIGN KEY (source_Id) REFERENCES Account(account_ID),"
			+ " FOREIGN KEY (dest_Id) REFERENCES Account(account_ID))",

			" Other_Bank_Transfer("
			+ " transaction_ID int,"
			+ " source_accunt_ID int,"
			+ " source_bank_id int,"
			+ " dest_accunt_id int,"
			+ " dest_bank_id int,"
			+ " FOREIGN KEY (transaction_ID) REFERENCES Transactions (transaction_ID))", 
			
			"Saving_transfer("
			+ " transaction_ID int,"
			+ " payment_Number int,"
			+ " final_Date int,"
			+ " saving_id int ,"
			+ " FOREIGN KEY (transaction_ID) REFERENCES Transactions (transaction_ID),"
			+ " FOREIGN KEY (saving_id) REFERENCES Saving (saving_ID))",
			
			"Loan_transfer("
			+ " transaction_ID int ,"
			+ " payment_Number int,"
			+ " final_Date int,"
			+ " loan_id int,"
			+ " FOREIGN KEY (transaction_ID) REFERENCES Transactions (transaction_ID),"
			+ " FOREIGN KEY (loan_id) REFERENCES loan (loan_ID))",
	};

	public DataBaseInstall(String dbName) {
		DataBaseInstall.dbName = dbName;
	}

	public DataBaseInstall(String dbName, String password, String userName) {

		DataBaseInstall.dbName = dbName;
		DataBaseInstall.password = password;
		DataBaseInstall.userName = userName;
		props = new Properties();
		props.setProperty(userName, password);

	}

	public void Execute() {

		try {

			System.out.println("Data-Base installation begins");

			if (userName != null && password != null)
				conn = DriverManager.getConnection(protocol + dbName
						+ ";create=true");
			else
				conn = DriverManager.getConnection(protocol + dbName
						+ ";create=true", props);

			System.out.println("Connected to and created database " + dbName);

			conn.setAutoCommit(false);

			System.out.println("Creates tables");
			
			System.out.println("\n------------------------------Create all the tables------------------------------------");
			for (int i = 0; i < query.length; i++) {
				System.out.println("Creates Table " + table[table.length - 1 - i]);
				createTable(query[i]);
				System.out.println("Table " + table[table.length - 1 - i]
						+ " was created successfuly");
			}

			System.out.println("Committed the transaction");

		} catch (SQLException e) {

			printSQLException(e);

		}
	}

	public static void createTable(String query) {

		try {
			s = conn.createStatement();
			s.execute("create table " + query);
			conn.commit();
		} catch (SQLException e) {
			if(e.getSQLState().equals("X0Y32"))
				return;
			printSQLException(e);
		}
	}

	public static void dropTable(String query) {

		try {
			s = conn.createStatement();
			s.execute("drop table " + query);
			conn.commit();
			System.out.println(query + " has droped");
		} catch (SQLException e) {
			if(e.getSQLState() == "42Y55")
			{	
				System.out.println(query + " is not exist");
				return;
			}
			printSQLException(e);
		}

	}

	public static void printSQLException(SQLException e) {
		// Unwraps the entire exception chain to unveil the real cause of the
		// Exception.
		while (e != null) {
			System.err.println("\n----- SQLException -----");
			System.err.println("  SQL State:  " + e.getSQLState());
			System.err.println("  Error Code: " + e.getErrorCode());
			System.err.println("  Message:    " + e.getMessage());
			// for stack traces, refer to derby.log or uncomment this:
			// e.printStackTrace(System.err);
			e = e.getNextException();
		}
	}

}
