package sysDesign;

import java.sql.SQLException;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;

public class saivingsSubsystem {

	DatabaseInterface SQLinteface;
	
	
	public saivingsSubsystem() throws SQLException {

		DatabaseInterface SQLinteface= DataBaseService.getDataBaseService();
		
	}

	public void createSavings(float amount , Date startDate, Date finalDate,int accountId) throws Exception{
		
		int saivingsId=SQLinteface.insertSaving(amount, startDate, startDate);	
		SQLinteface.insertAccountSavings(accountId, saivingsId);
		}
	
	public Set<sysDesign.Saving> getSavingByAccountID (int id) throws SQLException{
		return SQLinteface.getSavingByAccountID(id);
	}
	
	
}
