package DBManegment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import sysDesign.Account;
import sysDesign.Bank;
import sysDesign.Customer;
import sysDesign.Date;
import sysDesign.Loan;
import sysDesign.Savings;
import sysDesign.Transaction;

	public class DataBaseService implements DatabaseInterface {

	private static String dbName;
	private static String password;
	private static String userName;
	private static Properties props;
	private Connection conn = null;
	private static final String protocol = "jdbc:derby:";
	private ResultSet rs = null;
	private PreparedStatement psInsert = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psGet = null;
    private static DataBaseService dbs = null;
    
	private DataBaseService(String dbName) {
		
		new DataBaseInstall("myBank").Execute();
		DataBaseService.dbName = dbName;
		
	}

//	public DataBaseService(String dbName, String password, String userName) {
//
////		new DataBaseInstall("myBank").Execute();
//		DataBaseService.dbName = dbName;
//		DataBaseService.password = password;
//		DataBaseService.userName = userName;
//		props = new Properties();
//		props.setProperty(userName, password);
//
//	}
	
	public static DataBaseService getDataBaseService(){
			
			if (dbs == null)
			dbs = new DataBaseService("myBank");
			
		return dbs;
	}
	
	public void ConnectDataBase() {

		try {

			System.out.println("Data-Base connection begins");

			if (userName != null && password != null)
				conn = DriverManager.getConnection(protocol + dbName
						+ ";create=true");
			else
				conn = DriverManager.getConnection(protocol + dbName
						+ ";create=true", props);

			System.out.println("Connected to database " + dbName);

			conn.setAutoCommit(false);

		} catch (SQLException e) {
			printSQLException(e);
		}

	}

	public void DisconnectDataBase() {

		System.out.println("Shuting down, please wait");

		try {

			DriverManager.getConnection("jdbc:derby:;shutdown=true");

		} catch (SQLException se) {

			if (((se.getErrorCode() == 50000) && ("XJ015".equals(se
					.getSQLState())))) {

				System.out.println("Derby shut down normally");

			} else {

				System.err.println("Derby did not shut down normally");
				printSQLException(se);

			}
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
	
    private void reportFailure(String message) {
    	
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }

	@Override
	public int insertAccount(float balance) {
		
		int newAccId;
		
		if (balance < 0)
			return -1;
		
		try {
			
			psInsert = conn.prepareStatement("insert into account (balance) VALUES(?)", new String[]{"ACCOUNTID"});
			
			psInsert.setFloat(1, balance);
			
			psInsert.executeUpdate();
			
			rs = psInsert.getGeneratedKeys();
			
			rs.next();
			
			newAccId = rs.getInt(1);
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return -1;
		}
	
		return newAccId;
	
	}

	@Override
	public boolean insertCustomer(int customerId, String customerName) {

		String splitFullName[];
		int name=0, surname=1;
		
		customerName = customerName.trim();
		splitFullName = customerName.split(" ");
		
		if (splitFullName.length != 2 || customerId <= 0)
			return false;
		
	try {
			
		psInsert = conn.prepareStatement("insert into customer(customerID, name, surname) VALUES(?,?,?)");
		
		psInsert.setInt(1, customerId);
		psInsert.setString(2, splitFullName[name]);
		psInsert.setString(3, splitFullName[surname]);
		
		psInsert.executeUpdate();
		
		conn.commit();
			
		} catch (SQLException e) {
		
			printSQLException(e);
			return false;
		}
	
		return true;
	}

	@Override
	public int insertLoan(float amount, Date startDate, Date finalDate) {
		
		int loanID;
		
		if (startDate == null || finalDate == null || amount <= 0)
			return -1;
		
	try {
	
			psInsert = conn.prepareStatement("insert into loans(amount, startDate, finalDate)"
					+ " VALUES (?,?,?)", new String[]{"PROGRAMID"});
			
			psInsert.setFloat(1, amount);
			psInsert.setString(2, startDate.toString());
			psInsert.setString(3, finalDate.toString());
			
			psInsert.executeUpdate();
			
			rs = psInsert.getGeneratedKeys();
			
			rs.next();
			
			loanID = rs.getInt(1);
			
			conn.commit();
			
		} catch (SQLException e) {
		
			printSQLException(e);
			return -1;
		}
	
		return loanID;
		
	}

	@Override
	public int insertSavings(float monthlyDeposit, Date startDate, Date finalDate
			 ) {

		int savingID;
		
		if (startDate == null || finalDate == null || monthlyDeposit <= 0)
			return -1;
		
	try {
			
			psInsert = conn.prepareStatement("insert into savings (monthlyDeposit, startDate, finalDate)"
					+ " values (?,?,?)", new String[]{"PROGRAMID"});
			
			psInsert.setFloat(1, monthlyDeposit);
			psInsert.setString(2, startDate.toString());
			psInsert.setString(3, finalDate.toString());
			
			psInsert.executeUpdate();
			
			rs = psInsert.getGeneratedKeys();
			
			rs.next();
			
			savingID = rs.getInt(1);
			
			conn.commit();
			
		} catch (SQLException e) {
		
			printSQLException(e);
			return -1;
		}
	
		return savingID;
		
	}
	
	public boolean insertBindCustomerAccount(int accountId, int customerId){
		
		if (accountId  < 0 || customerId < 0)
			return false;
		
		try {
			
			psInsert = conn.prepareStatement("insert into CustomerAccount(accountId, customerId) VALUES(?,?)");
			
			psInsert.setInt(1, accountId);
			psInsert.setInt(2, customerId);
			
			psInsert.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
	
		return true;
	
	}
	
	public boolean insertAccountLoans(int accountID, int loanID){
		
		if (accountID  < 0 || loanID < 0)
			return false;
		
		try {
			
			psInsert = conn.prepareStatement("insert into AccountLoans(accountID, programID) VALUES(?,?)");
			
			psInsert.setInt(1, accountID);
			psInsert.setInt(2, loanID);
			
			psInsert.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
	
		return true;
	}
	public boolean insertAccountSavings(int accountID, int savingID){
		
		if (accountID  < 0 || savingID < 0)
			return false;
		
		try {
			
			psInsert = conn.prepareStatement("insert into AccountSavings(accountID, programID) VALUES(?,?)");
			
			psInsert.setInt(1, accountID);
			psInsert.setInt(2, savingID);
			
			psInsert.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
	
		return true;
	}

	@Override
	public int insertTransaction(float amount, sysDesign.Date date, int sourceID , int destinationID) {

		int transactionID;
		
		if (date == null || sourceID < 0 || destinationID < 0)
			return -1;
		
		try {
			
			psInsert = conn.prepareStatement("insert into Transactions(amount, date, sourceID, destinationID)"
					+ " VALUES(?,?,?,?)", new String[]{"TRANSACTIONID"});
			
			psInsert.setFloat(1, amount);
			psInsert.setString(2, date.toString());
			psInsert.setInt(3, sourceID);
			psInsert.setInt(4, destinationID);
	
			psInsert.executeUpdate();
			
			rs = psInsert.getGeneratedKeys();
			
			rs.next();
			
			transactionID = rs.getInt(1);
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return -1;
		}
	
		return transactionID;
		
	}

	public boolean insertMonthlyTransaction(int transactionID, int typeID, String type){
		
		if (transactionID < 0 || typeID < 0 || type == null)
			return false;
		
		try {
			
			psInsert = conn.prepareStatement("insert into MonthlyTransaction(transactionID, typeID, type)"
					+ " VALUES(?,?,?)");
			
			psInsert.setInt(1, transactionID);
			psInsert.setInt(2, typeID);
			psInsert.setString(3, type);

			psInsert.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
	
		return true;
	}
	
	public boolean insertOtherBankTransfer(int transactionID, int transferID){
		
		if (transferID  < 0 || transactionID < 0)
			return false;
		
		try {
			
			psInsert = conn.prepareStatement("insert into OtherBankTransfer(transactionID, transferID) VALUES(?,?)");
		
			psInsert.setInt(1, transactionID);
			psInsert.setInt(2, transferID);
			
			psInsert.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			printSQLException(e);
			return false;
		}
	
		return true;
			
	}
	
	public boolean updateAccountBalanceByID(float amount, int id) {

		if (id < 0)
			return false;
		
		 try {
			 			 
			psUpdate = conn.prepareStatement(
			         "update account set balance=? where accountID=?");
			
			psUpdate.setFloat(1, amount);
			psUpdate.setInt(2, id);
			
			psUpdate.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
		
			printSQLException(e);
			return false;
		}
		 return true;
	}
	
	@Override
	public boolean updateBankNumber(int bankNumber) {

		return false;
		
	}

	@Override
	public boolean updateBankName(String bankName) {

		return false;
	}

	@Override
	public boolean updateBankLoanInterest(float interest) {

		return false;
	}

	@Override
	public boolean updateBankSavingInterest(float interest) {

		return false;
	}

	@Override
	public Bank getBank() {

		return null;
	}

	@Override
	public Account getAccountByID(int id) {

		Account acc;		
		float balance;
		
		try {
			
			psGet = conn.prepareStatement("select balance from account where accountid=(?)");
			psGet.setInt(1, id);
			
			rs = psGet.executeQuery();
			
			if (!rs.next()){
				
				  reportFailure("No rows in ResultSet");
				  return null;
			}
			
			balance = rs.getFloat(1);
			
		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}
		
		acc = new Account(id, balance);
	
		return acc;
		
	}

	@Override
	public Customer getCustomerByID(int id) {

		Customer cust;
		String name;
			try {
			
			psGet = conn.prepareStatement("select name, surname from customer where customerid=(?)");
			psGet.setInt(1, id);
			
			rs = psGet.executeQuery();
			
			if (!rs.next()){
				
				  reportFailure("No rows in ResultSet");
				  return null;
			}
			
			name = rs.getString(1) +" "+ rs.getString(2);
			
		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}
		
		cust = new Customer(id, name);
	
		return cust;
	}

	@Override
	public Set<Account> getAccountsByCustomerID(int id) {

		Set<Account> acc = new LinkedHashSet<>();
		
		try {
			
			psGet = conn.prepareStatement("select account.accountid, balance from Account, AccountCustomer "
					+ "where customerid=(?) AND Accountcustomer.accountid = Account.accountid");
			psGet.setInt(1, id);
			
			rs = psGet.executeQuery();
			
			while(!rs.next())
				acc.add(new Account(rs.getInt(1), rs.getFloat(2)));			
						
		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}
	
		return null;
	}

	@Override
	public Set<Customer> getCustomersByAccountID(int id) {

		return null;
	}

	@Override
	public Set<Loan> getLoansByAccountID(int id) {

		return null;
	}

	@Override
	public Set<Savings> getSavingByAccountID(int id) {

		return null;
	}

	@Override
	public Set<Transaction> getHistoryTransactionByAccountID(int id) {

		return null;
	}
	
}
