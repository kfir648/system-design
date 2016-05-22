package DBManegment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import logic.*;
import logic.Worker.PermissionType;

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
	private static final int BANK_LOAN_INTREST_POS = BANK_SAVING_INTREST_POS + Float.BYTES;
	private static final int BANK_NAME_POS = BANK_LOAN_INTREST_POS + Float.BYTES;

	private static String password;
	private static String userName;
	private static Properties props;
	private Connection conn = null;
	private static final String protocol = "jdbc:derby:";
	private static DataBaseService dbs = null;

	private DataBaseService(String dbName) throws SQLException {

		new DataBaseInstall(dbName).Execute();
		ConnectDataBase(dbName);
		addFirstManager();
	}

	private void addFirstManager() throws SQLException {
		PreparedStatement s = conn.prepareStatement("insert into worker(user_name , passward , permission_type) values(?,?,?)");
		s.setString(1 , "lior knafo");
		s.setString(2, "12345");
		s.setInt(3, 0);
		s.executeUpdate();
		
	}

	public static DataBaseService getDataBaseService() throws SQLException {

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

	@Override
	public int insertAccount(float balance) throws SQLException {

		int newAccId;

		PreparedStatement psInsert = conn.prepareStatement("insert into ACCOUNT(BALANCE) VALUES(" + balance + ")", new String[] { "ACCOUNT_ID" });
		psInsert.executeUpdate();
		ResultSet rs = psInsert.getGeneratedKeys();

		rs.next();
		newAccId = rs.getInt(1);
		conn.commit();
		psInsert.close();
		rs.close();
		return newAccId;
	}

	@Override
	public void insertCustomer(int customerId, String customerName) throws Exception {
		PreparedStatement psInsert = null;

		if (customerId <= 0)
			throw new Exception("customerId is negative");

		psInsert = conn.prepareStatement("insert into CUSTOMER(customer_ID, name) VALUES(?,?)");

		psInsert.setInt(1, customerId);
		psInsert.setString(2, customerName);

		psInsert.executeUpdate();
		psInsert.close();
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
				"insert into loans(amount, start_Date, final_Date , relevant) VALUES (?,?,?,1)", new String[] { "LOAN_ID" });

		psInsert.setFloat(1, amount);
		psInsert.setString(2, startDate.toString());
		psInsert.setString(3, finalDate.toString());

		psInsert.executeUpdate();

		ResultSet rs = psInsert.getGeneratedKeys();

		rs.next();

		loanID = rs.getInt(1);
		psInsert.close();
		conn.commit();
		rs.close();
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
		psInsert.close();
		rs.close();
		return savingID;

	}

	public void insertBindCustomerAccount(int accountId, int customerId) throws Exception {
		if (accountId < 0 || customerId < 0)
			throw new Exception("accountId or customerId is negative");

		PreparedStatement psInsert = conn
				.prepareStatement("insert into Customer_Account(account_Id, customer_Id) VALUES(?,?)");

		psInsert.setInt(1, accountId);
		psInsert.setInt(2, customerId);

		psInsert.executeUpdate();
		psInsert.close();
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
		psInsert.close();
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
		psInsert.close();
		conn.commit();
	}

	@SuppressWarnings("null")
	@Override
	public int insertTransaction(Transaction transaction) throws Exception {
		if (transaction == null)
			throw new Exception("transaction is null");

		int transType = 0;
		PreparedStatement psInsert = null;
		try {
			psInsert = conn.prepareStatement("insert into Transactions(amount, date , transType) VALUES(?,?,?)",
					new String[] { "TRANSACTION_ID" });
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
			psInsert.close();
			rs.next();

			transaction.setId(rs.getInt(1));
			rs.close();

			if (transType == OTHER_BANK_ID) {
				OtherBankTransfer otherBankTransfer = (OtherBankTransfer) transaction;
				psInsert = conn.prepareStatement(
						"insert into other_bank_transfer(transaction_ID , source_accunt_ID , source_bank_id , dest_accunt_id , dest_bank_id , req_id , accepted) VALUES(?,?,?,?,?,?,0)");
				psInsert.setInt(1, otherBankTransfer.getTransId());
				psInsert.setInt(2, otherBankTransfer.getSourceAccuntId());
				psInsert.setInt(3, otherBankTransfer.getSourceBank());
				psInsert.setInt(4, otherBankTransfer.getDestinationAccuntId());
				psInsert.setInt(5, otherBankTransfer.getDestinationBank());
				psInsert.setInt(6, otherBankTransfer.getReqId());
				psInsert.executeUpdate();
			} else if (transType == SAME_BANK_TRANS_ID) {
				SameBankTransfer sameBankTransfer = (SameBankTransfer) transaction;
				psInsert = conn.prepareStatement(
						"insert into same_bank_transfer(transaction_ID , source_Id , dest_Id) VALUES(?,?,?)");
				psInsert.setInt(1, sameBankTransfer.getTransId());
				psInsert.setInt(2, sameBankTransfer.getSourceAccount());
				psInsert.setInt(3, sameBankTransfer.getDestinationAccount());
				psInsert.executeUpdate();
			} else if (transType == LOAN_TRANS_ID) {
				LoanTransaction loanTransaction = (LoanTransaction) transaction;
				psInsert = conn.prepareStatement(
						"insert into loan_transfer(transaction_ID , payment_Number , final_Date , loan_id ) VALUES(?,?,?,?)");
				psInsert.setInt(1, loanTransaction.getPaymentNumber());
				psInsert.setString(2, loanTransaction.getFinalDate().formatDate());
				psInsert.setInt(3, loanTransaction.getLoan());
				psInsert.executeUpdate();
			} else if (transType == SAVING_TRANS_ID) {
				SavingTransaction savingTransaction = (SavingTransaction) transaction;
				psInsert = conn.prepareStatement(
						"insert into saving_transfer(transaction_ID , payment_Number , final_Date , saving_id) VALUES(?,?,?,?)");
				psInsert.setInt(1, savingTransaction.getTransId());
				psInsert.setInt(2, savingTransaction.getPaymentNumber());
				psInsert.setString(3, savingTransaction.getFinalDate().formatDate());
				psInsert.setInt(4, savingTransaction.getSaving());
				psInsert.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if(psInsert != null || !psInsert.isClosed())
				psInsert.close();
		}
		return transaction.getTransId();
	}

	public void updateAccountBalanceByID(float amount, int id) throws Exception {
		if (id < 0)
			throw new Exception("accountId is negative");

		PreparedStatement psUpdate = conn.prepareStatement("update account set balance=? where account_ID=?");

		psUpdate.setFloat(1, amount);
		psUpdate.setInt(2, id);

		psUpdate.executeUpdate();
		psUpdate.close();
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
	public Account getAccountByID(int id) throws Exception {
		float balance;

		PreparedStatement psGet = conn.prepareStatement("select balance from account where account_id=(?)");
		psGet.setInt(1, id);

		ResultSet rs = psGet.executeQuery();

		if (!rs.next()) {
			throw new Exception("No rows in ResultSet");
		}

		balance = rs.getFloat(1);
		psGet.close();
		rs.close();
		return new Account(id, balance);
	}

	@Override
	public Customer getCustomerByID(int id) throws Exception {
		String name;
		ResultSet rs = null;
		PreparedStatement psGet = null;

		psGet = conn.prepareStatement("select name from customer where customerid=(?)");
		psGet.setInt(1, id);

		rs = psGet.executeQuery();

		if (!rs.next()) {
			psGet.close();
			rs.close();
			throw new Exception("No rows in ResultSet");
		}

		name = rs.getString(1);
		
		psGet.close();
		rs.close();
		return new Customer(id, name);
	}

	@Override
	public Set<Account> getAccountsByCustomerID(int id) throws SQLException {

		Set<Account> acc = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		psGet = conn.prepareStatement(" select account.account_id, balance " + " from Account, Customer_Account "
				+ " where Customer_Account.customer_id=(?) AND Customer_Account.account_id = Account.account_id");
		psGet.setInt(1, id);

		rs = psGet.executeQuery();

		while (rs.next())
			acc.add(new Account(rs.getInt(1), rs.getFloat(2)));
		psGet.close();
		rs.close();
		return acc;
	}

	@Override
	public Set<Customer> getCustomersByAccountID(int id) throws Exception {
		Set<Customer> cust = new LinkedHashSet<>();

		PreparedStatement psGet = conn.prepareStatement("select Customer.customer_id, name "
				+ " from Customer, Customer_Account " + " where customer_Account.account_id=(?) AND "
				+ " Customer_Account.customer_ID = Customer.customer_ID");

		psGet.setInt(1, id);

		ResultSet rs = psGet.executeQuery();

		while (rs.next())
			cust.add(new Customer(rs.getInt(1), rs.getString(2)));
		
		psGet.close();
		rs.close();
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
					Date.getDateFromString(rs.getString(4)) , rs.getInt(5) == 1));
		
		psGet.close();
		rs.close();
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
		
		psGet.close();
		rs.close();
		return savings;
	}

	@Override
	public Set<Transaction> getHistoryTransactionByAccountID(int id) throws Exception {

		Set<Transaction> transactions = new LinkedHashSet<>();
		ResultSet rs = null;
		PreparedStatement psGet = null;

		psGet = conn
				.prepareStatement("Select transaction_ID , trans_type " + " from Transactions where account_id = (?)");

		psGet.setInt(1, id);

		rs = psGet.executeQuery();

		while (!rs.next()) {
			transactions.add(getTransactionById(rs.getInt(1), rs.getInt(2)));
		}
		
		psGet.close();
		rs.close();
		return transactions;
	}

	@Override
	public Transaction getTransactionById(int id) throws Exception {
		PreparedStatement statement = conn
				.prepareStatement(" select trans_type from transactions where transaction_id =" + id);

		ResultSet rs = statement.executeQuery();
		rs.next();
		statement.close();
		rs.close();
		return getTransactionById(id, rs.getInt(1));
	}

	private Transaction getTransactionById(int id, int type) throws Exception {
		Transaction trans = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String startStat = "select Transactions.transaction_id , amount , Date , account_id ,";
		String mounthly = " payment_Number , final_Date , ";
		String where = "where transaction_id = ";
		switch (type) {
		case SAVING_TRANS_ID:
			statement = conn.prepareStatement(startStat + mounthly + " saving_id form Saving_transfer " + where + id);
			rs = statement.executeQuery();
			rs.next();
			trans = new SavingTransaction(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					rs.getInt(4), rs.getInt(5), Date.getDateFromString(rs.getString(6)), rs.getInt(7));
			break;

		case LOAN_TRANS_ID:
			statement = conn.prepareStatement(startStat + mounthly + "loan_id from loan_fransfer " + where + id);
			rs = statement.executeQuery();
			rs.next();
			trans = new LoanTransaction(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					rs.getInt(4), rs.getInt(5), Date.getDateFromString(rs.getString(6)), rs.getInt(7));
			break;

		case SAME_BANK_TRANS_ID:
			statement = conn.prepareStatement(startStat + "source_Id , dest_id from same_bank_transfer " + where + id);
			rs = statement.executeQuery();
			rs.next();
			trans = new SameBankTransfer(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					rs.getInt(4), rs.getInt(5), rs.getInt(6));
			break;

		case OTHER_BANK_ID:
			statement = conn.prepareStatement(startStat
					+ "source_accunt_ID , source_bank_id , dest_accunt_id , dest_bank_id from other_bank_transfer , req_id "
					+ where + id);
			rs = statement.executeQuery();
			rs.next();
			trans = new OtherBankTransfer(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
					rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8) , rs.getInt(9));
			break;
		}
		if(statement != null)
			statement.close();
		if(rs != null)
			rs.close();
		return trans;
	}

	@Override
	public Loan getLoanById(int id) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(
				" select loan_id , amount , start_Date , final_Date " + " from loan where loan_id = " + id);
		ResultSet rs = statement.executeQuery();
		rs.next();
		
		statement.close();
		Loan loan = new Loan(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
				Date.getDateFromString(rs.getString(4)) , rs.getInt(5) == 1);
		rs.close();
		return loan;
	}

	@Override
	public Saving getSavingById(int id) throws SQLException {
		PreparedStatement statement = conn
				.prepareStatement(" select saving_id , monthly_Deposit , start_Date, final_Date"
						+ " from saving where saving_id = " + id);

		ResultSet rs = statement.executeQuery();
		rs.next();
		
		statement.close();
		Saving saving = new Saving(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)),
				Date.getDateFromString(rs.getString(4)));
		rs.close();
		return saving;
	}

	@Override
	public void updateCustomer(int customeriD, String customerName) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("update customer set customer_name = " + customerName + " where customer_id = " + customeriD);
		statement.executeQuery();
	}

	@Override
	public void updateLoan(int loanId, float amount, Date firstPaymentDate, Date finalDate) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("update Loan "
				+ " set amount = " + amount
				+ ",start_Date = " + finalDate.formatDate()
				+ ",final_Date = " + finalDate.formatDate()
				+ " where loan_id = " + loanId);
		statement.executeQuery();
		statement.close();
	}

	@Override
	public void updateSaving(int savingId, float monthlyPaymentNumber, Date startSavingDate, Date finalSavingsDate) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("update Saving "
				+ " set monthly_Deposit = " + monthlyPaymentNumber
				+ ",start_Date = " + startSavingDate.formatDate()
				+ ",final_Date = " + finalSavingsDate.formatDate()
				+ " where saving_id = " + savingId);
		statement.executeQuery();	
		statement.close();
	}

	@Override
	public ResultSet queryTest(String sql) throws SQLException {
		PreparedStatement psGet = conn.prepareStatement(sql);
		ResultSet rs = psGet.executeQuery();
		psGet.close();
		return rs;
	}

	public void insertWorker(Worker manager, Worker worker , PermissionType permissionType) throws Exception {
		PermissionType permisType = getPermissions(manager);
		if(!permisType.equals(PermissionType.BANK_MANAGER))
			throw new Exception("Can't add new user without manager permission");
		
		PreparedStatement statement = conn.prepareStatement("insert into Worker(user_name , passward , permission_type) VALUES(?,?,?)");
		statement.setString(1, worker.getUserName());
		statement.setString(2, worker.getPassward());
		statement.setInt(3, permissionType.getValue());
		statement.execute();
		statement.close();
	}

	@Override
	public boolean conformUserName(String userName, String passward) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("select * from worker where user_name = " + userName + " AND passward = " + passward);
		ResultSet rs = statement.executeQuery();
		boolean out = rs.next();
		statement.close();
		rs.close();
		return out;
	}

	@Override
	public PermissionType getPermissions(Worker user) throws Exception {
		PreparedStatement statement = conn.prepareStatement(
				"select permission_type from worker "
				+ " where user_name = ?" 
				+ " And passward = ?");
		statement.setString(1, user.getUserName());
		statement.setString(2, user.getPassward());
		ResultSet rs = statement.executeQuery();
		if(!rs.next())
			throw new Exception("user name does not exists");
		PermissionType permissionType = PermissionType.values()[rs.getInt(1)];
		statement.close();
		rs.close();
		return permissionType;
	}

	@Override
	public void acceptOtherBankTransfer(int transId) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(
				"update Other_Bank_Transfer set accepted = 1 where transaction_id = " + transId);
		statement.executeUpdate();
	}

	@Override
	public void rejectOtherBankTransfer(int transId) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("delete from Other_bank_Transfer where transaction_id = " + transId);
		statement.executeUpdate();
		statement = conn.prepareStatement("delete from transactions where transaction_id = " + transId);
		statement.executeUpdate();
	}

	@Override
	public OtherBankTransfer getOtherBankTransByReqId(int reqId) throws Exception {
		PreparedStatement preparedStatement = conn.prepareStatement("select transaction_id from Other_bank_Transfer where req_id = " + reqId);
		ResultSet rs = preparedStatement.executeQuery();
		if(!rs.next())
			throw new Exception("Transaction is not exists");
		return (OtherBankTransfer) getTransactionById(rs.getInt(1));
	}

	@Override
	public Map<Integer,Loan> getRelevantLoans() throws SQLException {
		PreparedStatement statement = conn.prepareStatement("select * from loan where relevant = 1");
		ResultSet rs = statement.executeQuery();
		Map<Integer,Loan> loans = new LinkedHashMap<>();
		while(rs.next())
		{
			loans.put(rs.getInt(1), new Loan(rs.getInt(1), rs.getFloat(2), Date.getDateFromString(rs.getString(3)), Date.getDateFromString(rs.getString(4)) , true));
		}
		return loans;
	}

	@Override
	public void setLoanIrrelevant(int loanId) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("update loan set relevant = 1 where loan_id = " + loanId);
		statement.execute();
	}

	@Override
	public Map<Integer, Set<LoanTransaction>> getAllRelevantLoanTransaction() throws Exception {
		Map<Integer , Set<LoanTransaction>> out = new LinkedHashMap<>();
		PreparedStatement statement = conn.prepareStatement(
				  " select loan.loan_id , transactions.transaction_id , transactions.amount , account_id , payment_Number , final_Date , loan.loan_id"
				+ " from transactions , Loan_transfer , loan "
				+ " where transactions.transaction_id = transactions.transaction_id "
				+ " and Loan_transfer.loan_id = loan.loan_id "
				+ " and loan.relevant = 1");
		
		ResultSet rs = statement.executeQuery();
		while(rs.next())
		{
			int loanId = rs.getInt(1);
			if(!out.containsKey(rs.getInt(1)))
			{
				out.put(loanId, new LinkedHashSet<>());
			}
			Set<LoanTransaction> loanTransactions = out.get(loanId);
			loanTransactions.add(new LoanTransaction(rs.getInt(2), rs.getFloat(3), Date.getDateFromString(rs.getString(4)), rs.getInt(5), rs.getInt(6),Date.getDateFromString(rs.getString(7)), rs.getInt(8)));
		}
		return out;
	}

}
