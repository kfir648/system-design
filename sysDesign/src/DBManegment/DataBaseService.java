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
import sysDesign.*;

public class DataBaseService implements DatabaseInterface {

	private static final int SAVING_TRANS_ID = 0;
	private static final int LOAN_TRANS_ID = 1;
	private static final int SAME_BANK_TRANS_ID = 2;
	private static final int OTHER_BANK_ID = 3;

	private static final String BANK_FILE_NAME = "myBank.dat"; // bankId ,
																// savingIntrest
																// , loanIntrest
																// , bankName
	private static final int BANK_ID_POS = 0;
	private static final int BANK_SAVING_INTREST_POS = Integer.BYTES;
	private static final int BANK_LOAN_INTREST_POS = Float.BYTES + Integer.BYTES;
	private static final int BANK_NAME_POS = Integer.BYTES * 2 + Float.BYTES;

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
	public int insertAccount(float balance) throws SQLException {

		int newAccId;
		ResultSet rs = null;
		PreparedStatement psInsert = null;

		psInsert = conn.prepareStatement("insert into account(balance) VALUES(?)", new String[] { "ACCOUNT_ID" });
		psInsert.setFloat(1, balance);
		psInsert.executeUpdate();
		rs = psInsert.getGeneratedKeys();

		rs.next();
		newAccId = rs.getInt(1);
		conn.commit();

		return newAccId;
	}

	@Override
	public void insertCustomer(int customerId, String customerName) throws Exception {
		PreparedStatement psInsert = null;

		if (customerId <= 0)
			throw new Exception("customerId is negative");

		psInsert = conn.prepareStatement("insert into customer(customerID, name) VALUES(?,?)");

		psInsert.setInt(1, customerId);
		psInsert.setString(2, customerName);

		psInsert.executeUpdate();

		conn.commit();
	}

	@Override
	public int insertLoan(float amount, Date startDate, Date finalDate) throws Exception {
		int loanID;

		if (startDate == null || finalDate == null)
			throw new Exception("StartDate or finalDate is null");

		if (amount <= 0)
			throw new Exception("amount is negative");

		PreparedStatement psInsert = conn.prepareStatement(
				"insert into loans(amount, start_Date, final_Date) VALUES (?,?,?)", new String[] { "LOAN_ID" });

		psInsert.setFloat(1, amount);
		psInsert.setString(2, startDate.toString());
		psInsert.setString(3, finalDate.toString());

		psInsert.executeUpdate();

		ResultSet rs = psInsert.getGeneratedKeys();

		rs.next();

		loanID = rs.getInt(1);

		conn.commit();

		return loanID;

	}

	@Override
	public int insertSaving(float monthlyDeposit, Date startDate, Date finalDate) throws Exception {

		int savingID;
		ResultSet rs = null;
		PreparedStatement psInsert = null;

		if (startDate == null || finalDate == null)
			throw new Exception("startDate or finalDate is null");
		if (monthlyDeposit <= 0)
			throw new Exception("mounthlyDeposit is negative");
		psInsert = conn.prepareStatement("insert into saving(monthly_Deposit, start_Date, final_Date) values (?,?,?)",
				new String[] { "saving_ID" });

		psInsert.setFloat(1, monthlyDeposit);
		psInsert.setString(2, startDate.formatDate());
		psInsert.setString(3, finalDate.formatDate());

		psInsert.executeUpdate();

		rs = psInsert.getGeneratedKeys();

		rs.next();

		savingID = rs.getInt(1);

		conn.commit();

		return savingID;

	}

	public void insertBindCustomerAccount(int accountId, int customerId) throws Exception {
		if (accountId < 0 || customerId < 0)
			throw new Exception("accountId or customerId is negative");

		PreparedStatement psInsert = conn
				.prepareStatement("insert into CustomerAccount(accountId, customerId) VALUES(?,?)");

		psInsert.setInt(1, accountId);
		psInsert.setInt(2, customerId);

		psInsert.executeUpdate();

		conn.commit();
	}

	public void insertAccountLoans(int accountID, int loanID) throws Exception {

		PreparedStatement psInsert = null;

		if (accountID < 0 || loanID < 0)
			throw new Exception("accountId or loanId is negative");

		psInsert = conn.prepareStatement("insert into AccountLoans(accountID, programID) VALUES(?,?)");

		psInsert.setInt(1, accountID);
		psInsert.setInt(2, loanID);

		psInsert.executeUpdate();

		conn.commit();
	}

	public void insertAccountSavings(int accountID, int savingID) throws Exception {

		PreparedStatement psInsert = null;

		if (accountID < 0 || savingID < 0)
			throw new Exception("accountId or savingId is negative");

		psInsert = conn.prepareStatement("insert into AccountSavings(accountID, programID) VALUES(?,?)");

		psInsert.setInt(1, accountID);
		psInsert.setInt(2, savingID);

		psInsert.executeUpdate();

		conn.commit();
	}

	@Override
	public int insertTransaction(Transaction transaction) throws Exception {
		if (transaction == null)
			throw new Exception("transaction is null");

		int transactionID;
		int transType = 0;
		PreparedStatement psInsert = null;
		psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)",
				new String[] { "TRANSACTIONID" });
		if (transaction instanceof SavingTransaction) {
			transType = SAVING_TRANS_ID;
		} else if (transaction instanceof LoanTransaction) {
			transType = LOAN_TRANS_ID;
		} else if (transaction instanceof SameBankTransfer) {
			transType = SAME_BANK_TRANS_ID;
		} else if (transaction instanceof OtherBankTransfer) {
			transType = OTHER_BANK_ID;
		}

		psInsert.setFloat(1, transaction.getAmount());
		psInsert.setString(2, transaction.getTransactionDate().formatDate());
		psInsert.setInt(3, transType);

		psInsert.executeUpdate();

		ResultSet rs = psInsert.getGeneratedKeys();

		rs.next();

		transactionID = rs.getInt(1);

		switch (transType) {
		case LOAN_TRANS_ID:
		default:
			OtherBankTransfer otherBankTransfer = (OtherBankTransfer) transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case OTHER_BANK_ID:
			SameBankTransfer sameBankTransfer = (SameBankTransfer) transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case SAME_BANK_TRANS_ID:
			LoanTransaction loanTransaction = (LoanTransaction) transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		case SAVING_TRANS_ID:
			SavingTransaction savingTransaction = (SavingTransaction) transaction;
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)");
			break;
		}

		conn.commit();

		return transactionID;

	}

	public void updateAccountBalanceByID(float amount, int id) throws Exception {
		if (id < 0)
			throw new Exception("accountId is negative");

		PreparedStatement psUpdate = conn.prepareStatement("update account set balance=? where accountID=?");

		psUpdate.setFloat(1, amount);
		psUpdate.setInt(2, id);

		psUpdate.executeUpdate();

		conn.commit();
	}

	@Override
	public void updateBankNumber(int bankNumber) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(BANK_FILE_NAME), "rw");
		raf.seek(BANK_ID_POS);
		raf.writeInt(bankNumber);
		raf.close();
	}

	@Override
	public void updateBankName(String bankName) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(BANK_FILE_NAME), "rw");
		raf.setLength(Integer.BYTES + Float.BYTES * 2);
		raf.seek(BANK_NAME_POS);
		raf.writeUTF(bankName);
		raf.close();
	}

	@Override
	public void updateBankLoanInterest(float interest) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(BANK_FILE_NAME), "rw");
		raf.seek(BANK_LOAN_INTREST_POS);
		raf.writeFloat(interest);

		raf.close();
	}

	@Override
	public void updateBankSavingInterest(float interest) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(BANK_FILE_NAME), "rw");
		raf.seek(BANK_SAVING_INTREST_POS);
		raf.writeFloat(interest);

		raf.close();
	}

	@Override
	public Bank getBank() throws IOException {
		int bankNumber;
		String bankName;
		float loanInterest, savingInterest;
		RandomAccessFile raf = new RandomAccessFile(new File(BANK_FILE_NAME), "r");

		bankNumber = raf.readInt();
		savingInterest = raf.readFloat();
		loanInterest = raf.readFloat();
		bankName = raf.readUTF();
		raf.close();

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
	public Set<Account> getAccountsByCustomerID(int id) throws SQLException {

		Set<Account> acc = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		psGet = conn.prepareStatement(" select account.account_id, balance " + " from Account, Customer_Account "
				+ " where customer_Account.customer_id=(?) AND Customer_Account.account_id = Account.account_id");
		psGet.setInt(1, id);

		rs = psGet.executeQuery();

		while (!rs.next())
			acc.add(new Account(rs.getInt(1), rs.getFloat(2)));

		return acc;
	}

	@Override
	public Set<Customer> getCustomersByAccountID(int id) throws SQLException {
		Set<Customer> cust = new LinkedHashSet<>();

		PreparedStatement psGet = conn.prepareStatement("select Customer.customer_id, name, surname "
				+ " from Customer, CustomerAccount " + " where customerAccount.accountid=(?) AND "
				+ " CustomerAccount.customerID = Customer.customerID");

		psGet.setInt(1, id);

		ResultSet rs = psGet.executeQuery();

		while (!rs.next())
			cust.add(new Customer(rs.getInt(1), rs.getString(2) + " " + rs.getString(3)));

		return cust;
	}

	@Override
	public Set<Loan> getLoansByAccountID(int id) throws SQLException {

		Set<Loan> loan = new LinkedHashSet<>();

		PreparedStatement psGet = conn
				.prepareStatement(" select Loan.loan_ID, amount, start_date, final_date " + " from Loan, account_Loans "
						+ " where account_loans.account_id=(?) AND " + " Loans.loan_ID = account_Loans.loan_ID");

		psGet.setInt(1, id);

		ResultSet rs = psGet.executeQuery();

		while (!rs.next())
			loan.add(new Loan(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					Date.getDateFromString(rs.getString(4))));

		return loan;
	}

	@Override
	public Set<Saving> getSavingByAccountID(int id) throws SQLException {

		Set<Saving> savings = new LinkedHashSet<>();

		PreparedStatement psGet = conn
				.prepareStatement(" select Saving.saving_ID, monthly_Deposit, start_date, final_date "
						+ " from Savings, account_Savings " + " where account_Savings.account_id=(?) "
						+ " AND Savings.saving_ID = account_Savings.saving_ID");

		psGet.setInt(1, id);

		ResultSet rs = psGet.executeQuery();

		while (!rs.next())
			savings.add(new Saving(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					Date.getDateFromString(rs.getString(4))));

		return savings;
	}

	@Override
	public Set<Transaction> getHistoryTransactionByAccountID(int id) {

		Set<Transaction> transactions = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

			psGet = conn
					.prepareStatement("Select transaction_ID , trans_type from Transactions where account_id = (?)"); /// not good the transaction most include bank number

			psGet.setInt(1, id);

			rs = psGet.executeQuery();

			while (!rs.next())
			{
				PreparedStatement preparedStatement = conn.prepareStatement("select Transction.transaction_ID , amount , ")
				transactions.add(new Transaction(rs.getInt(1), rs.getFloat(2), rs.getInt(1)));
			}
			
		return transactions;
	}
	
	@Override
	public Transaction getTransactionById(int id) {
		
	}

	@Override
	public Loan getLoanById(int id) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(
				" select loan_id , amount , start_Date , final_Date " + 
				" from loan where loan_id = " + id);
		ResultSet rs = statement.executeQuery();
		rs.next();
		return new Loan(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)), Date.getDateFromString(rs.getString(4)));
	}

	@Override
	public Saving getSavingById(int id) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(
				" select saving_id , monthly_Deposit , start_Date, final_Date" +
				" from saving where saving_id = " + id);
		
		ResultSet rs = statement.executeQuery();
		rs.next();
		return new Saving(rs.getInt(1), rs.getFloat(2)	, Date.getDateFromString(rs.getString(3)), Date.getDateFromString(rs.getString(4)));
	}

}
