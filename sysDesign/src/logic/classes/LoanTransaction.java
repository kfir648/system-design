package logic.classes;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class LoanTransaction extends MonthlyTransaction {
	
	int loan;
	
	public LoanTransaction(float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate , int loan) throws Exception {
		super(0 , amount, transactionDate, accountId , paymentNumber, finalDate);
		this.loan = loan;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.insertTransaction(this);
	}
	
	public LoanTransaction(int transId , float amount, Date transactionDate , int accountId , int paymentNumber, Date finalDate , int loan) throws Exception {
		super(transId , amount, transactionDate, accountId , paymentNumber, finalDate);
		this.loan = loan;
	}
	
	public int getLoan() throws SQLException {
		return loan;
	}

	@Override
	public String toString() {
		return "Loan Transaction " + super.toString();
	}

	@Override
	public String showAllDetails() throws SQLException {
		return super.toString() + "\nLoan Transaction: Loan:"
				+ DataBaseService.getDataBaseService().getLoanById(loan).toString();
	}

}
