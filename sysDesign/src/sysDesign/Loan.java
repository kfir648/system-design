package sysDesign;
import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Loan {

	private float amount;
	private Date finalDate;
	private Date firstPaymentDate;
	private int loanId;
	
	public Loan(int loanId , float amount, Date firstPaymentDate , Date finalDate) {
		this.loanId = loanId;
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
	}
	
	public Loan(float amount, Date finalDate, Date firstPaymentDate) throws Exception {
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		this.loanId = db.insertLoan(amount, firstPaymentDate, finalDate);
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
		return "Loan [amount=" + amount + ", finalDate=" + finalDate + ", firstPaymentDate=" + firstPaymentDate
				+ ", loanId=" + loanId + "]";
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
	
}
