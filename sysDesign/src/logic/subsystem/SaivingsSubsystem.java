package logic.subsystem;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import logic.classes.*;

public class SaivingsSubsystem {
	private static SaivingsSubsystem savingSubsystem=null;
	DatabaseInterface SQLinteface;
	
	public static SaivingsSubsystem getSavingSubsystem() throws SQLException{
		if (savingSubsystem==null){
			savingSubsystem=new SaivingsSubsystem();
		}
		return savingSubsystem;
	}
	
	
	private SaivingsSubsystem() throws SQLException {
		SQLinteface = DataBaseService.getDataBaseService();
	}

	public void createSavings(float amount , Date startDate, Date finalDate,int accountId) throws Exception{	
		int saivingsId=SQLinteface.insertSaving(amount, startDate, startDate , accountId);	
	}
	
	public Set<Saving> getSavingByAccountID (int id) throws SQLException{
		return SQLinteface.getSavingByAccountID(id);
	}
	
	public Set<Saving> getSavingsByCustomerID (int id) throws SQLException{
		Set<Account> Accounts =SQLinteface.getAccountsByCustomerID(id);
		Set<Saving> savings= new LinkedHashSet<>();
		for (Account acc : Accounts){
			savings.addAll(acc.getAllSaivingsByAccountID());
		}
		return savings;
	}


	
	public Saving getSavingsByID(int savingID) throws SQLException {
	return SQLinteface.getSavingById(savingID);	
	}
	
	public void updateSavingTransaction() throws Exception {
		Map<Integer , Saving> Savings = SQLinteface.getRelevantSavings();
		Map<Integer,Set<SavingTransaction>> SavingTransactions = SQLinteface.getAllRelevantSavingTransaction();  
		
		for(Entry<Integer, Saving> entry : Savings.entrySet()){
			int numOfPayments=0;
			Date startDate=entry.getValue().getStartSavingDate();
			numOfPayments=startDate.howManyMonths(startDate.getNow());
			for(int i=0;i<numOfPayments;i++){
				MonthlyTransaction transaction= new MonthlyTransaction(  entry.getValue().getSavingId(),entry.getValue().getMonthlyPayment(),startDate,entry.getValue().getAccountId(),i,entry.getValue().getFinalSavingsDate());
				
				startDate=startDate.next();
				savingSubsystem.SQLinteface.insertTransaction(transaction);
			}
		}
		
	}

	public float checkSavingAmount(int savingID) throws SQLException{
		float sum=0;
		  Saving save= this.getSavingsByID(savingID);
		Date start = save.getStartSavingDate();
		Date now= Date.getNow();
		int numberOfMounth=now.howManyMonths(start);
		sum=save.getMonthlyPayment()*numberOfMounth;
		return sum;
	}
	
}
