package DBManegment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseInstall {

	 private static Connection conn;
	 private static String dbName;
	 private static String password;
	 private static String userName;
	 private static Properties props;
	 private static String protocol = "jdbc:derby:";
	 private static Statement s;
	
	public DataBaseInstall (String dbName){
		setDbName(dbName);
	}
	 
	public DataBaseInstall (String dbName, String password, String userName){
		
		setDbName(dbName);
		setPassword(password);
		setUserName(userName);
		props = new Properties();
		props.setProperty(userName, password);
		
	}
	
	public void Execute(){
		
		try {
		
		System.out.println("Data-Base installation begins");
		
		conn = null;
		
		if (userName != null && password != null)
	    conn = DriverManager.getConnection(protocol + dbName
	                    + ";create=true");
		else													
	    conn = DriverManager.getConnection(protocol + dbName
	                  + ";create=true", props);
		
		
	    System.out.println("Connected to and created database " + dbName);
	    	
			conn.setAutoCommit(true);
			
			s = conn.createStatement();
			
		    System.out.println("Creates tables");
		    dropTableIfExis("Account" , conn);
		    dropTableIfExis("Customer" , conn);
		    dropTableIfExis("CustomerAccount" , conn);
		    dropTableIfExis("Loans" , conn);
		    dropTableIfExis("Savings" , conn);
		    dropTableIfExis("OtherBankTransfer" , conn);
		    dropTableIfExis("InsideBankTransaction" , conn);
		    s = conn.createStatement();
		    s.execute("create table Account(accountID int NOT NULL, "
		    		+ "balance float, PRIMARY KEY (accountID))");
		    
		    //conn.commit();
		    s = conn.createStatement();
		        
		    s.execute("create table Customer(customerID int NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), "
		    		+ "name varchar(20), surname varchar(20),  PRIMARY KEY (customerID))");
		    
		    //conn.commit();
		    //s = conn.createStatement();
		    
		    s.execute("create table CustomerAccount("
		    		+ "A_Id int NOT NULL , "
		    		+ "C_Id int NOT NULL ,"
		    		+ "CONSTRAINT C_FK "
		    		+ "FOREIGN KEY (A_ID) "
		    		+ "REFERENCES Account(accountID) ,"
		    		+ "CONSTRAINT A_FK "
		    		+ "FOREIGN KEY (C_ID) "
		    		+ "REFERENCES Customer(customerID))");
		    
		    //conn.commit();
		    s = conn.createStatement();
		    
		    s.execute("create table Loans ("
		    		+ "loanID int NOT NULL, "
		    		+ "amount float, "
		    		+ "startDate varchar(12), "
		    		+ "finalDate varchar(12), "
		    		+ "monthlyPayment float, "
		    		+ "PRIMARY KEY loanID, "
		    		+ "FOREIGN KEY (A_Id) REFERENCES Account(accountID)");
		    
		    //conn.commit();
		    s = conn.createStatement();
		    
		    s.execute("create table Savings(savingID int NOT NULL, amount float, startDate varchar(12), "
		    		+ "finalDate varchar(12), monthlyDeposit float, "
		    		+ "PRIMARY KEY savingID, (FOREIGN KEY (A_Id) REFERENCES Account(accountID)");
		    
		    //conn.commit();
		    s = conn.createStatement();
		    
		    s.execute("create table OtherBankTransfer("
		    		+ "transferID NOT NULL, "
		    		+ "amount float, "
		   			+ "date varchar(12), "
		   			+ "destinationID int, "
		   			+ "FOREIGN KEY (source_Id) Account(accountID), PRIMARY KEY transferID");
		   //conn.commit();
		   s = conn.createStatement();
		   
		   s.execute("create table InsideBankTransaction(transactionID NOT NULL, amount float,"
		   		+ "date varchar(12), FOREIGN KEY (destination_Id) Account(accountID), "
		   		+ "FOREIGN KEY (source_Id) Account(accountID), PRIMARY KEY transactionID");
		   
		   //conn.commit();
		   
           System.out.println("Committed the transaction");

           
		   try{
			   
		   DriverManager.getConnection("jdbc:derby:;shutdown=true");
		   
		   }
		   catch(SQLException se){
			   
			   if (( (se.getErrorCode() == 50000)
                       && ("XJ015".equals(se.getSQLState()) ))) {
 
                   System.out.println("Derby shut down normally");
      
               } else {

                   System.err.println("Derby did not shut down normally");
                   printSQLException(se);
           
               }
		   }
		
		} catch (SQLException e) {
			
			 printSQLException(e);
			 
		}  finally {
            // release all open resources to avoid unnecessary memory usage

            //Connection
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
	
	public static void dropTableIfExis(String name , Connection conn) {
		try{
			Statement s = conn.createStatement();
			s.execute("DROP TABLE " + name);
			//conn.commit();
		}catch(Exception e){}
	}
	
	public static void setDbName(String dbName) {
		DataBaseInstall.dbName = dbName;
	}

	public static void setPassword(String password) {
		DataBaseInstall.password = password;
	}

	public static void setUserName(String userName) {
		DataBaseInstall.userName = userName;
	}
	
    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
	
}
