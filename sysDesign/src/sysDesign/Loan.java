package sysDesign;

public class Loan {

	private float amount;
	private Date finalDate;
	private Date firstPaymentDate;
	private int MonthlyPaymentNumber;
	
	
	public Loan(float amount, Date finalDate, Date firstPaymentDate, int MonthlyPaymentNumber) {
		
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
		this.MonthlyPaymentNumber = MonthlyPaymentNumber;
	}
	
	public Loan(float amount, int day,int month, int year, Date firstPaymentDate, int MonthlyPaymentNumber) {
		this( amount,new Date(day,month,year), firstPaymentDate,  MonthlyPaymentNumber);
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public int getMonthlyPaymentNumber() {
		return MonthlyPaymentNumber;
	}
	
	
	
	
}
