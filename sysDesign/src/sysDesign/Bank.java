package sysDesign;

import java.io.IOException;
import java.sql.SQLException;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class Bank {

	private int bankNumber;
	private String bankName;
	private float loanIntrest , savingIntrest;
	
	private static Bank thisBank = null;
	
	public static Bank getThisBank() throws IOException, SQLException {
		if(thisBank == null)
			thisBank = DataBaseService.getDataBaseService().getBank();
		return thisBank;
	}
	
	@Override
	public String toString() {
		return "Bank [bankNumber=" + bankNumber + ", bankName=" + bankName + ", loanIntrest=" + loanIntrest
				+ ", savingIntrest=" + savingIntrest + "]";
	}

	public Bank(int bankNumber , String bankName , float loanIntrest , float savingIntrest) {
		this.bankName = bankName;
		this.bankName = bankName;
		this.loanIntrest = loanIntrest;
		this.savingIntrest = savingIntrest;
	}
	
	public int getBankNumber() {
		return bankNumber;
	}
	
	public String getBankName(){
		return bankName;
	}

	public float getLoanIntrest() {
		return loanIntrest;
	}

	public float getSavingIntrest() {
		return savingIntrest;
	}

	public void setBankNumber(int bankNumber) throws Exception {
		if(bankNumber <= 0)
			throw new Exception("The Bank number can't be negative");
		this.bankNumber = bankNumber;
		if(thisBank == this)
		{
			DatabaseInterface db = DataBaseService.getDataBaseService();
			db.updateBankNumber(bankNumber);
		}
	}

	public void setBankName(String bankName) throws Exception {
		if(bankName == null || bankName.isEmpty())
			throw new Exception("The Bank name can't be empty");
		this.bankName = bankName;
		if(thisBank == this)
		{
			DatabaseInterface db = DataBaseService.getDataBaseService();
			db.updateBankName(bankName);
		}
	}

	public void setLoanIntrest(float loanIntrest) throws Exception {
		if(loanIntrest <= 0)
			throw new Exception("The loan intrest can't be negative");
		this.loanIntrest = loanIntrest;
		if(thisBank == this)
		{
			DatabaseInterface db = DataBaseService.getDataBaseService();
			db.updateBankLoanInterest(loanIntrest);
		}
	}

	public void setSavingIntrest(float savingIntrest) throws Exception {
		if(savingIntrest <= 0)
			throw new Exception("The bank saving intrest can;t be intrest");
		this.savingIntrest = savingIntrest;
		if(thisBank == this)
		{
			DatabaseInterface db = DataBaseService.getDataBaseService();
			db.updateBankSavingInterest(savingIntrest);
		}
	}
}