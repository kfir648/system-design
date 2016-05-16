package sysDesign;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class SaivingsSubsystem {
	private static SaivingsSubsystem savingSubsystem=null;
	DatabaseInterface SQLinteface;
	
	public static SaivingsSubsystem getSavingSubsystem() throws SQLException{ ///////
		if (savingSubsystem==null){
			savingSubsystem=new SaivingsSubsystem();
		}
		return savingSubsystem;
	}
	
	
	private SaivingsSubsystem() throws SQLException {
		DatabaseInterface SQLinteface= DataBaseService.getDataBaseService();
	}

	public void createSavings(float amount , Date startDate, Date finalDate,int accountId) throws Exception{
		
		int saivingsId=SQLinteface.insertSaving(amount, startDate, startDate);	
		SQLinteface.insertAccountSavings(accountId, saivingsId);
		}
	
	public Set<sysDesign.Saving> getSavingByAccountID (int id) throws SQLException{
		return SQLinteface.getSavingByAccountID(id);
	}
	
	public Set<sysDesign.Saving> getSavingsBycustomerID (int id) throws SQLException{
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

	
	
}
