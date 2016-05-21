package logic;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Saving {
	private int savingId;
	private float monthlyPayment;
	private Date startSavingDate;
	private Date finalSavingsDate;
	private boolean isRelevant;
	private int accountId;
	
	public Saving(int savingId , float monthlyPayment , Date startSavingDate , Date finalSavingsDate , boolean isRelevant , int accountId) {
		this.startSavingDate = startSavingDate;
		this.finalSavingsDate = finalSavingsDate;
		this.monthlyPayment = monthlyPayment;
		this.savingId = savingId;
		this.isRelevant = isRelevant;
	}
	
	public Saving( float monthlyPayment , Date startSavingDate ,Date finalSavingsDate , int accountId) throws Exception {
		this(0 , monthlyPayment , startSavingDate , finalSavingsDate , true , accountId);
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		savingId = db.insertSaving(monthlyPayment, startSavingDate, finalSavingsDate,accountId);
	}
	
	public int getAccountId() {
		return accountId;
	}

	public int getSavingId() {
		return savingId;
	}

	public Date getStartSavingDate() {
		return startSavingDate;
	}
	
	public Date getFinalSavingsDate() {
		return finalSavingsDate;
	}

	public float getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPaymentNumber(int monthlyPaymentNumber) throws SQLException {
		this.monthlyPayment = monthlyPaymentNumber;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPaymentNumber , startSavingDate , finalSavingsDate);
	}

	public void setFinalSavingsDate(Date finalSavingsDate) throws SQLException {
		this.finalSavingsDate = finalSavingsDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPayment , startSavingDate , finalSavingsDate);
	}

	public void setStartSavingDate(Date startSavingDate) throws SQLException {
		this.startSavingDate = startSavingDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPayment , startSavingDate , finalSavingsDate);
	}

	@Override
	public String toString() {
		return "Saving [saving Id:" + savingId + ", monthly Payment Number:" + monthlyPayment + ", start Saving Date:"
				+ startSavingDate + ", final Savings Date:" + finalSavingsDate + ", " + (isRelevant?"relevant":"irrelevant") + " , Account id:" + accountId + "]";
	}

	
	
	
	
	
}
