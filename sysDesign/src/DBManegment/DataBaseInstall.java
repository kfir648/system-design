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

	String table[] = {"OtherBankTransfer", "MonthlyTransaction", "Transactions","AccountSavings", "AccountLoans",
			"Savings", "Loans", "CustomerAccount",  "Customer", "Account",};

	String query[] = {
			"Account(accountID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 100, INCREMENT BY 1), balance float NOT NULL, PRIMARY KEY (accountID))",

			"Customer(customerID int NOT NULL, name varchar(20), surname varchar(20),  PRIMARY KEY (customerID))",

			"CustomerAccount (accountID int NOT NULL, customerID int NOT NULL, FOREIGN KEY (customerID) REFERENCES Customer(customerID)  , FOREIGN KEY (accountID) REFERENCES Account(accountID))",
			
			"Loans(programID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 200, INCREMENT BY 1), amount float, startDate varchar(12), finalDate varchar(12), PRIMARY KEY (programID))",

			"Savings(programID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 300, INCREMENT BY 1), monthlyDeposit float NOT NULL, startDate varchar(12), finalDate varchar(12), PRIMARY KEY (programID))",
			
			"AccountLoans(accountID int, programID int, FOREIGN KEY (accountID) REFERENCES Account(accountID), FOREIGN KEY (programID) REFERENCES Loans (programID))",
			
			"AccountSavings(accountID int, programID int, FOREIGN KEY (accountID) REFERENCES Account(accountID), FOREIGN KEY (programID) REFERENCES Savings (programID))",
			
			"Transactions(transactionID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 400, INCREMENT BY 1), amount float, Date varchar(12), sourceID int, destinationID int, PRIMARY KEY (transactionID))", // account exists rely on logic

			"MonthlyTransaction(transactionID int, typeID int, type varchar(10), FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID))",   // type (loan or saving) rely on logic
			
			"OtherBankTransfer(transactionID int, transferID int, FOREIGN KEY (transactionID) REFERENCES Transactions (transactionID))",
	
			
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
				try {
					dropTable(table[i]);
				} catch (SQLException e) {
					System.out.println(table[i] + " Does not exist");
				}
			}

			for (int i = 0; i < query.length; i++) {
				System.out.println("Creates Table "+table[(table.length-1)-i]);
				createTable(query[i]);
				System.out.println("Table " + table[(table.length-1)-i] + " was created successfuly");
			}
			System.out.println("Committed the transaction");

		} catch (SQLException e) {

			printSQLException(e);

		}
	}

	public static void createTable(String query) throws SQLException {

		s = conn.createStatement();
		s.execute("create table " + query);
		conn.commit();
	}

	public static void dropTable(String query) throws SQLException {

		s = conn.createStatement();
		;
		s.execute("drop table " + query);
		conn.commit();

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
