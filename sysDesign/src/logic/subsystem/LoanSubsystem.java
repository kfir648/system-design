package logic.subsystem;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import logic.classes.*;

public class LoanSubsystem {
	private static LoanSubsystem loanSubsystem = null;
	private DatabaseInterface SQLinteface;

	public static LoanSubsystem getLoanSubsystem() throws SQLException {
		if (loanSubsystem == null) {
			loanSubsystem = new LoanSubsystem();
		}
		return loanSubsystem;
	}

	public void insertLoan(float amount, Date startDate, Date finalDate, int accountId) throws Exception {
		SQLinteface.insertLoan(amount, startDate, finalDate, accountId);
	}

	private LoanSubsystem() throws SQLException {
		SQLinteface = DataBaseService.getDataBaseService();
	}

	public Set<Loan> getLoansByAccountID(int id) throws SQLException {
		return SQLinteface.getLoansByAccountID(id);
	}

	public Loan getLoansByLoanID(int id) throws SQLException {
		return SQLinteface.getLoanById(id);
	}

	public Set<Loan> getLoansBycustomerID(int id) throws SQLException {
		Set<Account> Accounts = SQLinteface.getAccountsByCustomerID(id);
		Set<Loan> loans = new LinkedHashSet<>();
		for (Account acc : Accounts) {
			loans.addAll(acc.getAllLoans());
		}
		return loans;
	}

	public void updateLoanTransaction() throws Exception {
		Map<Integer, Loan> loans = SQLinteface.getRelevantLoans();
		Map<Integer, Map<Date, LoanTransaction>> loanTransactions = SQLinteface.getAllRelevantLoanTransaction();

		for (Integer loanId : loans.keySet()) {
			int i = 0;
			for (Date startDate = loans.get(loanId).getFirstPaymentDate(); startDate
					.compareTo(Date.getNow()) < 0; startDate = startDate.next(), i++) {
				Map<Date, LoanTransaction> transactions = loanTransactions.get(loanId);
				if (!transactions.containsKey(startDate)) {
					new MonthlyTransaction(loanId, loans.get(loanId).getAmount(),
							startDate, loans.get(loanId).getAccountId(), i, loans.get(loanId).getFinalDate());
				}
			}
		}

	}

}
