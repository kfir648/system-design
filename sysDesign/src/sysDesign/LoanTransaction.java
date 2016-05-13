package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class LoanTransaction extends monthlyTransaction {
	
	int loan;
	
	public LoanTransaction(float amount, Date transactionDate, int sourceAccountNumber, int destenationAccountNumber, int paymentNumber, Date finalDate , int loan) {
		super(amount, transactionDate, sourceAccountNumber, destenationAccountNumber, paymentNumber, finalDate);
		this.loan = loan;
	}
	
	public Loan getLoan() {
		DatabaseInterface db = DataBaseService.getDataBaseService();
		return db.getLoanById(loan);
	}

}
