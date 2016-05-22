package logic;

import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Saving {
	private int savingId;
	private float monthlyPaymentNumber;
	private Date startSavingDate;
	private Date finalSavingsDate;
	
	public Saving(int savingId , float monthlyPaymentNumber, Date startSavingDate , Date finalSavingsDate) {
		this.monthlyPaymentNumber = monthlyPaymentNumber;
		this.startSavingDate = startSavingDate;
		this.finalSavingsDate = finalSavingsDate;
		this.savingId = savingId;
	}
	
	public Saving(Date startSavingDate ,Date finalSavingsDate) throws Exception {
		this.startSavingDate = startSavingDate;
		this.finalSavingsDate = finalSavingsDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		savingId = db.insertSaving(monthlyPaymentNumber, startSavingDate, finalSavingsDate);
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

	public float getMonthlyPaymentNumber() {
		return monthlyPaymentNumber;
	}

	public void setMonthlyPaymentNumber(int monthlyPaymentNumber) throws SQLException {
		this.monthlyPaymentNumber = monthlyPaymentNumber;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPaymentNumber , startSavingDate , finalSavingsDate);
	}

	public void setFinalSavingsDate(Date finalSavingsDate) throws SQLException {
		this.finalSavingsDate = finalSavingsDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPaymentNumber , startSavingDate , finalSavingsDate);
	}

	public void setStartSavingDate(Date startSavingDate) throws SQLException {
		this.startSavingDate = startSavingDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		db.updateSaving(savingId , monthlyPaymentNumber , startSavingDate , finalSavingsDate);
	}

	@Override
	public String toString() {
		return "Saving [savingId=" + savingId + ", monthlyPaymentNumber=" + monthlyPaymentNumber + ", startSavingDate="
				+ startSavingDate + ", finalSavingsDate=" + finalSavingsDate + "]";
	}
	
	
	
	
}
