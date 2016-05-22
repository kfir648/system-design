package logic.subsystem;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import logic.classes.*;

public class SaivingsSubsystem {
	private static SaivingsSubsystem savingSubsystem = null;
	DatabaseInterface SQLinteface;

	public static SaivingsSubsystem getSavingSubsystem() throws SQLException {
		if (savingSubsystem == null) {
			savingSubsystem = new SaivingsSubsystem();
		}
		return savingSubsystem;
	}

	private SaivingsSubsystem() throws SQLException {
		SQLinteface = DataBaseService.getDataBaseService();
	}

	public Set<Saving> getSavingByAccountID(int id) throws SQLException {
		return SQLinteface.getSavingByAccountID(id);
	}

	public Set<Saving> getSavingsByCustomerID(int id) throws SQLException {
		Set<Account> Accounts = SQLinteface.getAccountsByCustomerID(id);
		Set<Saving> savings = new LinkedHashSet<>();
		for (Account acc : Accounts) {
			savings.addAll(acc.getAllSaivingsByAccountID());
		}
		return savings;
	}

	public Saving getSavingsByID(int savingID) throws SQLException {
		return SQLinteface.getSavingById(savingID);
	}

	public void updateSavingTransaction() throws Exception {
		Map<Integer, Saving> savings = SQLinteface.getRelevantSavings();
		Map<Integer, Map<Date, SavingTransaction>> SavingTransactions = SQLinteface.getAllRelevantSavingTransaction();

		for (Integer savingId : savings.keySet()) {
			int i = 0;
			for (Date startDate = savings.get(savingId).getStartSavingDate(); startDate
					.compareTo(Date.getNow()) < 0; startDate = startDate.next(), i++) {

				if (!SavingTransactions.containsKey(startDate)) {
					Saving saving = savings.get(savingId);
					new SavingTransaction(saving.getSavingId(),
							saving.getMonthlyPayment(), startDate, saving.getAccountId(), i,
							saving.getFinalSavingsDate(), saving.getAccountId());

				}
			}
		}
	}

	public float checkSavingAmount(int savingID) throws SQLException {
		float sum = 0;
		Saving save = this.getSavingsByID(savingID);
		Date start = save.getStartSavingDate();
		Date now = Date.getNow();
		int numberOfMounth = now.howManyMonths(start);
		sum = save.getMonthlyPayment() * numberOfMounth;
		return sum;
	}

}
