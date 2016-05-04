package sysDesign;

public class Loan {

	private float amount;
	private Date finalDate;
	private Date firstPaymentDate;
	private int paymentNumber;
	
	
	public Loan(float amount, Date finalDate, Date firstPaymentDate, int paymentNumber) {
		super();
		this.amount = amount;
		this.finalDate = finalDate;
		this.firstPaymentDate = firstPaymentDate;
		this.paymentNumber = paymentNumber;
	}
	
	public Loan(float amount, int day,int month, int year, Date firstPaymentDate, int paymentNumber) {
		this( amount,new Date(day,month,year), firstPaymentDate,  paymentNumber);
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

	public int getPaymentNumber() {
		return paymentNumber;
	}
	
	
	
	
}
