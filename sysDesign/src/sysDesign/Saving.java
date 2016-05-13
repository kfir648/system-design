package sysDesign;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Saving {
	private int savingId;
	private float MonthlyPaymentNumber;
	private Date startSavingDate;
	private Date finalSavingsDate;
	
	public Saving(int savingId , float monthlyPaymentNumber, Date startSavingDate , Date finalSavingsDate) {
		MonthlyPaymentNumber = monthlyPaymentNumber;
		this.startSavingDate = startSavingDate;
		this.finalSavingsDate = finalSavingsDate;
		this.savingId = savingId;
	}
	
	public Saving(int monthlyPaymentNumber, Date startSavingDate ,Date finalSavingsDate) {
		MonthlyPaymentNumber = monthlyPaymentNumber;
		this.startSavingDate = startSavingDate;
		this.finalSavingsDate = finalSavingsDate;
		
		DatabaseInterface db = DataBaseService.getDataBaseService();
		savingId = db.insertSavings(monthlyPaymentNumber, startSavingDate, finalSavingsDate);
	}

	public float getMonthlyPaymentNumber() {
		return MonthlyPaymentNumber;
	}

	public void setMonthlyPaymentNumber(int monthlyPaymentNumber) {
		MonthlyPaymentNumber = monthlyPaymentNumber;
	}

	public Date getFinalSavingsDate() {
		return finalSavingsDate;
	}

	public void setFinalSavingsDate(Date finalSavingsDate) {
		this.finalSavingsDate = finalSavingsDate;
	}
	
	
	
	
}
