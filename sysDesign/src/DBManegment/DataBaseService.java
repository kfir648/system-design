package DBManegment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import otherBankTrans.OtherBankTrans;
import sysDesign.Account;
import sysDesign.Bank;
import sysDesign.Customer;
import sysDesign.Date;
import sysDesign.Loan;
import sysDesign.LoanTransaction;
import sysDesign.OtherBankTransfer;
import sysDesign.SameBankTransfer;
import sysDesign.SavingTransaction;
import sysDesign.Saving;
import sysDesign.Transaction;

public class DataBaseService implements DatabaseInterface {

	private static final int SAVING_TRANS_ID = 0;
	private static final int LOAN_TRANS_ID = 1;
	private static final int SAME_BANK_TRANS_ID = 2;
	private static final int OTHER_BANK_ID = 3;
	
	private static String password;
	private static String userName;
	private static Properties props;
	private Connection conn = null;
	private static final String protocol = "jdbc:derby:";
	private static DataBaseService dbs = null;

	private DataBaseService(String dbName) {

		new DataBaseInstall(dbName).Execute();
		ConnectDataBase(dbName);

	}

	public static DataBaseService getDataBaseService() {

		if (dbs == null)
			dbs = new DataBaseService("myBank");

		return dbs;
	}

	private void ConnectDataBase(String dbName) {

		try {

			System.out.println("Data-Base connection begins");

			if (userName != null && password != null)
				conn = DriverManager.getConnection(protocol + dbName + ";create=true");
			else
				conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

			System.out.println("Connected to database " + dbName);

			conn.setAutoCommit(false);

		} catch (SQLException e) {
			printSQLException(e);
		}

	}

	public void DisconnectDataBase() {

		ResultSet rs = null;

		System.out.println("Shuting down, please wait");

		try {

			DriverManager.getConnection("jdbc:derby:;shutdown=true");

		} catch (SQLException se) {

			if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {

				System.out.println("Derby shut down normally");

			} else {

				System.err.println("Derby did not shut down normally");
				printSQLException(se);

			}
		} finally {

			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException sqle) {
				printSQLException(sqle);
			}

			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException sqle) {
				printSQLException(sqle);
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
		ResultSet rs = null;
		PreparedStatement psInsert = null;

		if (balance < 0)
			return -1;

		try {

			psInsert = conn.prepareStatement("insert into account (balance) VALUES(?)", new String[] { "ACCOUNTID" });

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

		PreparedStatement psInsert = null;

		if (customerId <= 0)
			return false;

		try {

			psInsert = conn.prepareStatement("insert into customer(customerID, name) VALUES(?,?)");

			psInsert.setInt(1, customerId);
			psInsert.setString(2, customerName);

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
		ResultSet rs = null;
		PreparedStatement psInsert = null;

		if (startDate == null || finalDate == null || amount <= 0)
			return -1;

		try {

			psInsert = conn.prepareStatement("insert into loans(amount, startDate, finalDate)" + " VALUES (?,?,?)",
					new String[] { "PROGRAMID" });

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
	public int insertSavings(float monthlyDeposit, Date startDate, Date finalDate) {

		int savingID;
		ResultSet rs = null;
		PreparedStatement psInsert = null;

		if (startDate == null || finalDate == null || monthlyDeposit <= 0)
			return -1;

		try {

			psInsert = conn.prepareStatement(
					"insert into savings (monthlyDeposit, startDate, finalDate)" + " values (?,?,?)",
					new String[] { "PROGRAMID" });

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

	public boolean insertBindCustomerAccount(int accountId, int customerId) {

		PreparedStatement psInsert = null;

		if (accountId < 0 || customerId < 0)
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

	public boolean insertAccountLoans(int accountID, int loanID) {

		PreparedStatement psInsert = null;

		if (accountID < 0 || loanID < 0)
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

	public boolean insertAccountSavings(int accountID, int savingID) {

		PreparedStatement psInsert = null;

		if (accountID < 0 || savingID < 0)
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
	public int insertTransaction(Transaction transaction) throws Exception {
		if (transaction == null)
			throw new Exception("transaction is null");
		
		int transactionID;
		int transType = 0;
		PreparedStatement psInsert = null;
		psInsert = conn.prepareStatement(
				"insert into Transactions(amount, date , transType) VALUES(?,?,?)",
				new String[] { "TRANSACTIONID" });
		if(transaction instanceof SavingTransaction)
		{
			transType = 3;
		}
		else if(transaction instanceof LoanTransaction)
		{
			transType = LOAN_TRANS_ID;
		}
		else if(transaction instanceof SameBankTransfer)
		{
			transType = SAME_BANK_TRANS_ID;
		}
		else if(transaction instanceof OtherBankTransfer)
		{
			transType = OTHER_BANK_ID;
		}
		
		psInsert.setFloat(1, transaction.getAmount());
		psInsert.setString(2, transaction.getTransactionDate().formatDate());
		psInsert.setInt(3, transType);

		psInsert.executeUpdate();

		ResultSet rs = psInsert.getGeneratedKeys();

		rs.next();

		transactionID = rs.getInt(1);
		
		switch(transType)
		{
		case LOAN_TRANS_ID:
		default:
			OtherBankTransfer otherBankTransfer = (OtherBankTransfer)transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case OTHER_BANK_ID:
			SameBankTransfer sameBankTransfer = (SameBankTransfer)transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case SAME_BANK_TRANS_ID:
			LoanTransaction loanTransaction = (LoanTransaction)transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case SAVING_TRANS_ID:
			SavingTransaction savingTransaction = (SavingTransaction)transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		}

		conn.commit();

		return transactionID;

	}

	public boolean updateAccountBalanceByID(float amount, int id) {

		PreparedStatement psUpdate = null;

		if (id < 0)
			return false;

		try {

			psUpdate = conn.prepareStatement("update account set balance=? where accountID=?");

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

		try {

			RandomAccessFile raf = new RandomAccessFile(new File("myBank"), "rw");

			raf.writeInt(bankNumber);

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}

	@Override
	public boolean updateBankName(String bankName) {

		try {

			RandomAccessFile raf = new RandomAccessFile(new File("myBank"), "rw");

			raf.readInt();

			raf.writeUTF(bankName);

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean updateBankLoanInterest(float interest) {

		try {

			RandomAccessFile raf = new RandomAccessFile(new File("myBank"), "rw");

			raf.readInt();
			raf.readUTF();

			raf.writeFloat(interest);

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean updateBankSavingInterest(float interest) {

		try {

			RandomAccessFile raf = new RandomAccessFile(new File("myBank"), "rw");

			raf.readInt();
			raf.readUTF();
			raf.readFloat();

			raf.writeFloat(interest);

			raf.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public Bank getBank() {

		int bankNumber;
		String bankName;
		float loanInterest, savingInterest;

		try {

			RandomAccessFile raf = new RandomAccessFile(new File("myBank"), "r");

			bankNumber = raf.readInt();
			bankName = raf.readUTF();
			loanInterest = raf.readFloat();
			savingInterest = raf.readFloat();

			raf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return new Bank(bankNumber, bankName, loanInterest, savingInterest);

	}

	@Override
	public Account getAccountByID(int id) {

		Account acc;
		float balance;
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn.prepareStatement("select balance from account where accountid=(?)");
			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			if (!rs.next()) {

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
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn.prepareStatement("select name from customer where customerid=(?)");
			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			if (!rs.next()) {

				reportFailure("No rows in ResultSet");
				return null;
			}

			name = rs.getString(1);

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
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn.prepareStatement("select account.accountid, balance from Account, CustomerAccount "
					+ "where customerAccount.customerid=(?) AND CustomerAccount.accountid = Account.accountid");
			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
				acc.add(new Account(rs.getInt(1), rs.getFloat(2)));

		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}

		return acc;
	}

	@Override
	public Set<Customer> getCustomersByAccountID(int id) {

		Set<Customer> cust = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn.prepareStatement("select Customer.customerid, name, surname from Customer, CustomerAccount "
					+ "where customerAccount.accountid=(?) AND CustomerAccount.customerID = Customer.customerID");

			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
				cust.add(new Customer(rs.getInt(1), rs.getString(2) + " " + rs.getString(3)));

		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}

		return cust;
	}

	@Override
	public Set<Loan> getLoansByAccountID(int id) {

		Set<Loan> loan = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn
					.prepareStatement("select Loans.programID, amount, startdate, finaldate from Loans, accountLoans "
							+ "where accountloans.accountid=(?) AND Loans.programID = accountLoans.programID");

			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
				loan.add(new Loan(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
						Date.getDateFromString(rs.getString(4))));

		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}

		return loan;
	}

	@Override
	public Set<Savings> getSavingByAccountID(int id) {

		Set<Savings> savings = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn.prepareStatement(
					"select Savings.programID, monthlyDeposit, startdate, finaldate from Savings, accountSavings "
							+ "where accountSavings.accountid=(?) AND Savings.programID = accountSavings.programID");

			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
				savings.add(new Savings(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
						Date.getDateFromString(rs.getString(4))));

		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}

		return savings;
	}

	@Override
	public Set<Transaction> getHistoryTransactionByAccountID(int id) {

		Set<Transaction> transactions = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		try {

			psGet = conn
					.prepareStatement("Select transactionID from Transactions where sourceID = (?) OR destinationID = (?)"); /// not good the transaction most include bank number

			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
			{
				PreparedStatement preparedStatement = conn.prepareStatement("select Transction.transactionID , amount , )
				transactions.add(new Transaction(rs.getInt(1), rs.getFloat(2),
						rs.getString(3), rs.getInt(4), rs.getInt(5)));
			}

		} catch (SQLException e) {
			printSQLException(e);
			return null;
		}

		return transactions;
	}

}
