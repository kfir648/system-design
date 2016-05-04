package sysDesign;

public class Savings {

	private int MonthlyPaymentNumber;
	private Date finalSavingsDate;
	
	public Savings(int monthlyPaymentNumber, Date finalSavingsDate) {
		
		MonthlyPaymentNumber = monthlyPaymentNumber;
		this.finalSavingsDate = finalSavingsDate;
	}

	public int getMonthlyPaymentNumber() {
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
