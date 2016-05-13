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
		return finalDate;
	}
	
	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}
	
	public int getMonthlyPaymentNumber() {
		return finalDate.month + finalDate.year * 12 - firstPaymentDate.month - firstPaymentDate.year * 12;
	}

	public int getLoanId() {
		return loanId;
	}
	
	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}
	
}
