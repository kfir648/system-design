package DBManegment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseInstall {

	private static String dbName;
	private static Properties props;
	private static Connection conn = null;
	private static Statement s;
	private static String protocol = "jdbc:derby:";

	String table[] = { "Worker" ,"Account", "Customer", "Customer_Account", "Loan", "Saving",
			"Transactions", "Same_Bank_Transfer", "Other_Bank_Transfer", "Saving_transfer", "Loan_transfer" };

	String query[] = {
			"Worker("
					+ " user_name varchar(20) not null,"
					+ " passward varchar(20) not null,"
					+ " permission_type int not null,"
					+ " PRIMARY KEY(user_name))",
					
			"Account(" 
					+ " account_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
					+ " balance float NOT NULL," 
					+ " PRIMARY KEY (account_ID))",

			"Customer(" + " customer_ID int NOT NULL," + " name varchar(40) NOT NULL," + " PRIMARY KEY (customer_ID))",

			"Customer_Account(" 
					+ " account_ID int NOT NULL," + " customer_ID int NOT NULL,"
					+ " FOREIGN KEY (customer_ID) REFERENCES Customer(customer_ID),"
					+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID),"
					+ " PRIMARY KEY(customer_ID , account_ID))",

			"Loan(" 
					+ " loan_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
					+ " amount float," 
					+ " start_Date varchar(12)," 
					+ " final_Date varchar(12),"
					+ " relevant int,"
					+ "account_ID int,"
					+ "FOREIGN KEY (account_ID) REFERENCES Account(account_ID), "
					+ " PRIMARY KEY (loan_ID))",

			"Saving(" 
					+ " saving_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000,INCREMENT BY 1),"
					+ " monthly_Deposit float NOT NULL," 
					+ " start_Date varchar(12)," 
					+ " final_Date varchar(12),"
					+ " account_ID int,"
					+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID), "
					+ " PRIMARY KEY (saving_ID))",

			" Transactions("
					+ " transaction_ID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1000, INCREMENT BY 1),"
					+ " amount float," 
					+ " Date varchar(12)," 
					+ " trans_Type int , " 
					+ " account_id int ,"
					+ " PRIMARY KEY (transaction_ID)," 
					+ " FOREIGN KEY (account_ID) REFERENCES Account(account_ID))",

			" Same_Bank_Transfer(" 
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
					+ " req_id int,"
					+ " accepted int,"
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
					+ " FOREIGN KEY (loan_id) REFERENCES loan (loan_ID))", };

	public DataBaseInstall(String dbName) {
		DataBaseInstall.dbName = dbName;
	}

	public void Execute() {

		try {
			props = new Properties();
			
			System.out.println("Data-Base installation begins");
			try{
				conn = DriverManager.getConnection(protocol + dbName + ";create=false", props);
			}catch(SQLException e)
			{
				if(e.getSQLState().equals("XJ004"))
					conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
				else
					throw e;
			}
			System.out.println("Connected to and created database " + dbName);

			conn.setAutoCommit(true);

			System.out.println("Creates tables");

			System.out.println(
					"\n------------------------------Create all the tables------------------------------------");
			for (int i = 0; i < query.length; i++) {
				System.out.print("Creates Table " + table[i]);
				createTable(query[i]);
			}

			System.out.println("Committed the transaction");

		} catch (SQLException e) {

			printSQLException(e);

		}
	}

	public static void createTable(String query) throws SQLException {

		try {
			s = conn.createStatement();
			s.execute("create table " + query);
			conn.commit();
			System.out.println("....successfully");
		} catch (SQLException e) {
			if (e.getSQLState().equals("X0Y32")) 
				System.out.println("....exists");
			else
				printSQLException(e);
		}finally {
			s.close();
		}
	}

	public static void dropTable(String query) throws SQLException {

		try {
			s = conn.createStatement();
			s.execute("drop table " + query);
			conn.commit();
			System.out.println(query + " has droped");
		} catch (SQLException e) {
			if (e.getSQLState() == "42Y55") 
				System.out.println(query + " is not exist");
			else
				printSQLException(e);
		}finally {
			s.close();
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
