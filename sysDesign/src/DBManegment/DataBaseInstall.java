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

	String table[] = { "Other_BankTransfer", "Monthly_Transaction",
			"Transactions", "Account_Savings", "Account_Loans", "Savings",
			"Loans", "Customer_Account", "Customer", "Account", "Same_Bank_transfer" , "Saving_transfer" , "loan_transfer"};

	String query[] = {
			"Account(accountID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 100, INCREMENT BY 1), balance float NOT NULL, PRIMARY KEY (accountID))",

			"Customer(customerID int NOT NULL, name varchar(40),  PRIMARY KEY (customerID))",

			"Customer_Account (accountID int NOT NULL, customerID int NOT NULL, FOREIGN KEY (customerID) REFERENCES Customer(customerID)  , FOREIGN KEY (accountID) REFERENCES Account(accountID))",

			"Loan(loan_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 200, INCREMENT BY 1), amount float, startDate varchar(12), finalDate varchar(12), PRIMARY KEY (programID))",

			"Saving(saving_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 300, INCREMENT BY 1), monthlyDeposit float NOT NULL, startDate varchar(12), finalDate varchar(12), PRIMARY KEY (programID))",

			"Account_Loans(accountID int, programID int, FOREIGN KEY (accountID) REFERENCES Account(accountID), FOREIGN KEY (programID) REFERENCES Loans (programID))",

			"Account_Savings(accountID int, programID int, FOREIGN KEY (accountID) REFERENCES Account(accountID), FOREIGN KEY (programID) REFERENCES Savings (programID))",

			"Transactions(transactionID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 400, INCREMENT BY 1), amount float, Date varchar(12), transType int , PRIMARY KEY (transactionID))",

			"Same_Bank_Transfer(transactionID int, typeID int, type varchar(10), FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID))",

			"Other_Bank_Transfer(transactionID int, transferID int , transType, FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID))", 
			
			"Saving_transfer(transactionID int , saving_id int , FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID) , FOREIGN KEY (saving_id) REFERENCES Saving (saving_ID)",
			
			"Loan_transfer(transactionID int , loan_id int , FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID) , FOREIGN KEY (loan_id) REFERENCES loan (loan_ID)",
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

			for (int i = 0; i < table.length; i++) {
				dropTable(table[i]);
			}

			for (int i = 0; i < query.length; i++) {
				System.out.println("Creates Table "
						+ table[(table.length - 1) - i]);
				createTable(query[i]);
				System.out.println("Table " + table[(table.length - 1) - i]
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

		}
	}

	public static void dropTable(String query) {

		try {
			s = conn.createStatement();
			s.execute("drop table " + query);
			conn.commit();
		} catch (SQLException e) {

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
