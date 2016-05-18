package logic;
import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Loan {

	private float amount;
	private Date finalDate;
	private Date firstPaymentDate;
	private int loanId;
	private boolean isRelevant;
	private int accountID; 
	
	public Loan(int accountID,int loanId , float amount, Date firstPaymentDate , Date finalDate , boolean isRelevant) {
		this.accountID=accountID;
		this.loanId = loanId;
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
	}
	
	public Loan(int accountID,float amount, Date finalDate, Date firstPaymentDate) throws Exception {
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
		this.accountID=accountID;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		this.loanId = db.insertLoan(amount, firstPaymentDate, finalDate);
	}
	
	public int getAccountID(){
		return this.accountID;
	}

	public float getAmount() {
		return amount;
	}

	public Date getFinalDate() {
		return finalDate.clone();
	}
	
	public Date getFirstPaymentDate() {
		return firstPaymentDate.clone();
	}
	
	public int getMonthlyPaymentNumber() {
		return finalDate.month + finalDate.year * 12 - firstPaymentDate.month - firstPaymentDate.year * 12;
	}

	public int getLoanId() {
		return loanId;
	}
	
	public void setFirstPaymentDate(Date firstPaymentDate) throws SQLException {
		this.firstPaymentDate = firstPaymentDate.clone();
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateLoan(loanId , amount , firstPaymentDate , finalDate);
	}
	
	@Override
	public String toString() {
		return "Loan [amount:" + amount + ", final Date:" + finalDate + ", first Payment Date:" + firstPaymentDate
				+ ", loan ID:" + loanId + ", " + (isRelevant?"relevant":"irrelevant") + "]";
	}

	public void setAmount(float amount) throws SQLException {
		this.amount = amount;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateLoan(loanId , amount , firstPaymentDate , finalDate);
	}
	
	public void setFinalDate(Date finalDate) throws SQLException {
		this.finalDate = finalDate.clone();
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateLoan(loanId , amount , firstPaymentDate , finalDate);
	}
	
	public void setIsNotRelevant() throws SQLException {
		isRelevant = false;
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.setLoanIrrelevant(loanId);
	}
}
