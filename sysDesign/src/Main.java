import java.awt.List;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;

import api_otherBank.transf.TransferRequestTuple;
import api_otherBank.transf.TransferResult;
import otherBankTrans.ConformEvent;
import otherBankTrans.ConformTransactionListener;
import otherBankTrans.OtherBankTrans;
import otherBankTrans.TransactionEvent;
import otherBankTrans.TransactionListener;
import sysDesign.*;
import sysDesign.Worker.PermissionType;

public class Main {

	public static Scanner s = new Scanner(System.in);
	public static OtherBankTrans otherBankTrans = OtherBankTrans.getOtherBankTrans();

	public static void main(String[] args) {

		boolean secssed = false;
		Worker worker = null;
		PermissionType permission = null;

		checkCurrentStatus();

		while (!secssed) {
			System.out.println("Welcome!! please enter user name :");
			String userName = s.nextLine();

			System.out.println("Enter passward :");
			String password = s.nextLine();
			try {
				worker = new Worker(userName, password);
				permission = worker.getPermission();
				secssed = true;
			} catch (Exception e) {

				System.out.println("User name dose not exeist!!!");
			}
		}
		boolean toExit = false;
		while (!toExit) {

			switch (permission) {

			case BANK_MANAGER:
				System.out.println("[A/a] = Add worker");

			case LOAN_OFFICER:
				System.out.println("[L/l] = Loan");
				System.out.println("[S/s] = Savings");

			case TELLER:
				System.out.println("[D/d] = Deposite");
				System.out.println("[W/w] = Widrow");
				System.out.println("[R/r] = Check history Transactions");
				System.out.println("[O/o] = Transfer money");
				System.out.println("[B/b] = Check balance");
				System.out.println("[C/c] = Add customer");
				System.out.println("[T/t] = Add account");
				System.out.println("[E/e] = toExit");
				break;
			}
			char selection = s.next().charAt(0);

			switch (selection) {
			case 'A':
			case 'a':
				if (permission == PermissionType.BANK_MANAGER)
					addWorker(worker);
				break;

			case 'L':
			case 'l':
				if (permission.getValue() <= PermissionType.LOAN_OFFICER.getValue())
					loan();
				break;

			case 'S':
			case 's':
				if (permission.getValue() <= PermissionType.LOAN_OFFICER.getValue())
					saiving();
				break;

			case 'D':
			case 'd':
				deposite();
				break;

			case 'W':
			case 'w':
				widrow();
				break;

			case 'R':
			case 'r':
				CheckHistoryTransactions();
				break;

			case 'O':
			case 'o':
				try {
					CreateTransfer();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 'B':
			case 'b':
				checkBalance();
				break;

			case 'C':
			case 'c':
				AddCustomer();
				break;

			case 'T':
			case 't':
				AddAccount();
				break;

			case 'E':
			case 'e':
				toExit = true;
				break;

			default:
				System.out.println("Wrong input");
				break;
			}
			s.close();
			OtherBankTrans.closeConnection();
		}
	}

	private static void CreateTransfer() throws Exception {
		System.out.println("Enter the type of transaction you want to create 1 - same bank , 2 - other bank");
		char c;
		do {
			c = s.next().charAt(0);
			if (c != '1' && c != '2')
				System.out.println("Wrong input");
		} while (c != '1' && c != '2');
		Account src = null;
		Account dest = null;

		float preBalSource = 0;
		float preBalDest = 0;
		try {
			System.out.println("The soure account is:");
			src = getAccount();
			if (c == '1') {
				System.out.println("The destination is:");
				dest = getAccount();
				preBalSource = src.getAccountBalance();
				preBalDest = dest.getAccountBalance();
				System.out.println("Enter the amount to transfer");
				float amount;
				do {
					amount = s.nextFloat();
					if (preBalSource < amount)
						System.out.println("You entered more the you can send");
				} while (preBalSource < amount);
				src.setAccountBalance(preBalSource - amount);
				dest.setAccountBalance(preBalDest + amount);
				new SameBankTransfer(amount, Date.getNow(), dest.getAccountId(), src.getAccountId(),
						dest.getAccountId());
				new SameBankTransfer(amount, Date.getNow(), src.getAccountId(), src.getAccountId(),
						dest.getAccountId());
				System.out.println("Transfer complete");
			} else {
				System.out.println("Enter bank id:");
				int bankId = s.nextInt();
				System.out.println("Enter account id:");
				int destId = s.nextInt();
				float amount;
				do {
					System.out.println("Enter the amount to transfer");
					amount = s.nextFloat();
					if (amount > src.getAccountBalance())
						System.out.println("You entered more money to transfer then you have");
				} while (amount > src.getAccountBalance());
				int reqId = otherBankTrans.sendTransaction(src, new Account(destId, 0), bankId, amount);
				new OtherBankTransfer(amount, Date.getNow(), src.getAccountId(), src.getAccountId(), OtherBankTrans.ID,
						destId, bankId, reqId);
				System.out.println("The transfer sent to the other bank to accept");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (c == '1') {
				if (src != null)
					src.setAccountBalance(preBalSource);
				if (dest != null)
					dest.setAccountBalance(preBalDest);
			}
		}
	}

	private static void CheckHistoryTransactions() {
		try {
			Account account = getAccount();
			Set<Transaction> transactions = account.getAllTransaction();
			System.out.println("Your last 20 Transactions are:");
			Transaction[] transArr = new Transaction[transactions.size()];
			transactions.toArray(transArr);
			Arrays.sort(transArr, new Comparator<Transaction>() {
				@Override
				public int compare(Transaction o1, Transaction o2) {
					return o2.getTransactionDate().compareTo(o1.getTransactionDate());
				}
			});
			int i = 0;
			for (; i < 20 && i < transArr.length; i++) {
				System.out.println(i + " - " + transArr[i].toString());
			}
			if (transArr.length > 20) {
				System.out.println("To show more transctions press Y/y ,other to exit");
				char c = s.next().charAt(0);
				if (c == 'Y' || c == 'y') {
					while (i < transArr.length) {
						int start = i;
						for (; i < 20 + start && i < transArr.length; i++) {
							System.out.println(i + " - " + transArr[i].toString());
						}
					}
				}
			}
			System.out.println("Enter the transaction number to show more information -1 to exit");
			int c = s.nextInt();
			if (c == -1)
				return;
			if (c < 0 || c >= transArr.length)
				throw new Exception("Wrong input");
			System.out.println(transArr[c].showAllDetails());
		} catch (Exception e) {
			System.out.println("ERORR:" + e.getMessage());
		}

	}

	private static void checkCurrentStatus() {

		try {
			LoanSubsystem.getLoanSubsystem();
			AccountManagmentSubsystem.getaccountSubsystem();
			TransactionSubsystem.getTransctionSubsystem();

			otherBankTrans.addNewTransferListener(new TransactionListener() {
				@Override
				public void transactionIncomes(TransactionEvent event) {
					for (TransferRequestTuple requestTuple : event.getTransferRequestTuple()) {
						try {
							Account acc = AccountManagmentSubsystem.getaccountSubsystem()
									.getAccountByID(requestTuple.receiverAccountId);
							if (acc != null) {
								int reqId = otherBankTrans.accept(requestTuple);
								new OtherBankTransfer(requestTuple.amount, Date.getNow(), acc.getAccountId(),
										requestTuple.senderAccountId, requestTuple.senderBankId, acc.getAccountId(),
										OtherBankTrans.ID, reqId);
								acc.setAccountBalance(acc.getAccountBalance() + requestTuple.amount);
							} else {
								otherBankTrans.reject(requestTuple);
							}
						} catch (Exception e) {
							otherBankTrans.reject(requestTuple);
						}
					}
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

		otherBankTrans.addConformTransferListener(new ConformTransactionListener() {

			@Override
			public void conformTransaction(ConformEvent event) {
				for (TransferResult result : event.getTransferResult()) {
					try {
						int outcome = result.getOutcome();
						if (outcome == TransferRequestTuple.ACCEPT) {
							OtherBankTransfer bankTransfer = TransactionSubsystem.getTransctionSubsystem()
									.getOtherBankTransByReqId(result.getRequestId());
							Account account = AccountManagmentSubsystem.getaccountSubsystem()
									.getAccountByID(bankTransfer.getAccuntId());
							account.setAccountBalance(account.getAccountBalance() - bankTransfer.getAmount());
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});

	}

	private static Account AddAccount() {
		System.out.println("Enter start balance");
		int balance = s.nextInt();
		try {
			Account acc = new Account(balance);
			System.out.println("The Account was created number:" + acc.getAccountId());
			return acc;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	private static void AddCustomer() {

		try {
			System.out.println("Enter customer name");
			String name = s.nextLine();
			System.out.println("Enter customer id");
			int custId = s.nextInt();
			Customer customer = new Customer(custId, name);
			System.out.println("The customer is entered to create new Account?");
			Account account = AddAccount();
			customer.addAccount(account);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void checkBalance() {
		Account account;
		try {
			account = getAccount();
			System.out.println("Your balance is: " + account.getAccountBalance());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void deposite() {
		Account account = null;
		while (true) {
			try {
				account = getAccount();
				break;
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}
		System.out.println("Enter the amount to deposite");
		float amount;
		do {
			amount = s.nextFloat();
			if (amount <= 0)
				System.out.println("The amount most be positive number");
		} while (amount <= 0);
		try {
			account.setAccountBalance(account.getAccountBalance() + amount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void widrow() {
		Account acc;
		while (true) {
			try {
				acc = getAccount();
				break;
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}
		float balance = acc.getAccountBalance();
		System.out.println("Enter the amount to deposite(max - " + balance + ":");
		float amount;
		while (true) {
			amount = s.nextFloat();
			if (amount > balance)
				System.out.println("You can't take more then you have");
			else
				break;
		}
		System.out.println("Are you sure?(Y/N");
		char selection = s.next().charAt(0);
		boolean wrongAnswer = false;
		while (!wrongAnswer) {
			switch (selection) {
			case 'Y':
			case 'y':
				try {
					acc.setAccountBalance(balance - amount);
					System.out.println("Your current balance is: " + acc.getAccountBalance());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 'N':
			case 'n':
				break;

			default:
				wrongAnswer = true;
				break;
			}
		}
	}

	private static Account getAccount() throws SQLException, Exception {
		System.out.println("Chose account:" + "\n1 - By Account ID" + "\n2 - By Customer ID");

		char selection = s.next().charAt(0);
		switch (selection) {
		case '1':
			System.out.println("Enter account id");
			int accId = s.nextInt();
			return AccountManagmentSubsystem.getaccountSubsystem().getAccountByID(accId);
		case '2':
			System.out.println("Enter customer id:");
			int customerID = s.nextInt();
			AccountManagmentSubsystem accountManagmentSubsystem = AccountManagmentSubsystem.getaccountSubsystem();
			Set<Account> accounts = accountManagmentSubsystem.getAccountsByCustomerID(customerID);
			Account[] accArr = new Account[accounts.size()];
			accounts.toArray(accArr);
			System.out.println("Choose account");
			for (int i = 0; i < accArr.length; i++) {
				System.out.println(i + accArr[i].toString());
			}
			int accountPos = s.nextInt();
			if (accountPos < 0 | accountPos >= accArr.length)
				throw new Exception("Type wrong account");
			return accArr[accountPos];
		}
		return null;
	}

	private static void saiving() {
		System.out.println("1 - Make new Saivings");
		System.out.println("2 - check saivings");
		int select = s.nextInt();
		switch (select) {
		case 1:
			makeNewSavings();
			break;

		case 2:
			checkSavings();
			break;
		}

	}

	private static void checkSavings() {
		Saving save = getSaving();
		System.out.println(save.toString());

	}

	private static Saving getSaving() {
		System.out.println("1 - Get savings by loan ID ");
		System.out.println("2 - Get savings by customer ID ");
		System.out.println("3 - get savings by account ID ");
		int select = s.nextInt();
		Set<Saving> savings = null;
		switch (select) {
		case 1: /// savings id
			try {
				SaivingsSubsystem savingSub = SaivingsSubsystem.getSavingSubsystem();
				System.out.println("Enter saving id:");
				int savingID = s.nextInt();
				return savingSub.getSavingsByID(savingID);

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				;
			}
			break;

		case 2: /// customer id
			try {
				SaivingsSubsystem savingSub = SaivingsSubsystem.getSavingSubsystem();
				System.out.println("Enter customer id:");
				int customerID = s.nextInt();
				savings = savingSub.getSavingsBycustomerID(customerID);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				;
			}
			break;

		case 3: /// account id
			try {
				SaivingsSubsystem savingSub = SaivingsSubsystem.getSavingSubsystem();
				System.out.println("Enter account id:");
				int accountID = s.nextInt();
				savings = savingSub.getSavingByAccountID(accountID);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				;
			}
			break;
		}

		Object[] saveArry = savings.toArray();
		System.out.println("Select savings:");

		for (int i = 0; i < saveArry.length; i++) {
			System.out.println(i + " = " + saveArry[i].toString());
		}
		int selected = s.nextInt();
		return (Saving) saveArry[selected];

	}

	private static void makeNewSavings() {
		while (true) {

			SaivingsSubsystem saveSub;
			try {
				saveSub = SaivingsSubsystem.getSavingSubsystem();
				System.out.println("Enter amount:");
				float amount = s.nextFloat();
				System.out.println("Start date:");
				Date startDate = getDate();
				System.out.println("Final date:");
				Date finalDate = getDate();
				System.out.println("Enter account ID:");
				int accountId = s.nextInt();
				saveSub.createSavings(amount, startDate, finalDate, accountId);
				break;
			}

			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public static void addWorker(Worker manager) {

		System.out.println("Enter user name:");
		String workerUserName = s.nextLine();
		System.out.println("Enter password:");
		String pass = s.nextLine();
		boolean sec = false;
		while (!sec) {
			System.out.println("what is permission? [0= Teller] [1= Loan Officer] [2= Bank maneger]");
			int permission = s.nextInt();
			if (permission >= 0) {
				if (permission <= 2) {

					Worker worker = new Worker(workerUserName, pass, PermissionType.values()[permission]);
					try {
						worker.addUser(manager);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
					sec = true;
				} else {
					System.out.println("You enterd a WRONG permission code!!");
				}
			} else {
				System.out.println("You enterd a WRONG permission code!!");
			}
		}
	}

	public static void loan() {
		System.out.println("1 - Make new loan");
		System.out.println("2 - check loan");
		int select = s.nextInt();
		switch (select) {
		case 1:
			makeNewLoan();
			break;

		case 2:
			checkLoan();
			break;
		}

	}

	private static void checkLoan() {
		Loan loan = getLoan();
		System.out.println("loan.toString()");
	}

	private static Loan getLoan() {
		System.out.println("1 - Get loan by loan ID ");
		System.out.println("2 - Get loan by customer ID ");
		System.out.println("3 - get loan by account ID ");
		int select = s.nextInt();
		Set<Loan> loans = null;
		switch (select) {
		case 1: /// loan id
			try {
				LoanSubsystem loanSub = LoanSubsystem.getLoanSubsystem();
				System.out.println("Enter loan id:");
				int loanID = s.nextInt();
				return loanSub.getLoansByLoanID(loanID);

			} catch (SQLException e) {

				System.out.println(e.getMessage());
				;
			}
			break;

		case 2: /// customer id
			try {
				LoanSubsystem loanSub = LoanSubsystem.getLoanSubsystem();
				System.out.println("Enter customer id:");
				int customerID = s.nextInt();
				loans = loanSub.getLoansBycustomerID(customerID);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				;
			}
			break;

		case 3: /// account id
			try {
				LoanSubsystem loanSub = LoanSubsystem.getLoanSubsystem();
				System.out.println("Enter account id:");
				int accountID = s.nextInt();
				loans = loanSub.getLoansByAccountID(accountID);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				;
			}
			break;
		}

		Object[] loanArry = loans.toArray();
		System.out.println("Select Loan:");

		for (int i = 0; i < loanArry.length; i++) {
			System.out.println(i + " = " + loanArry[i].toString());
		}
		int selected = s.nextInt();
		return (Loan) loanArry[selected];

	}

	private static void makeNewLoan() {
		while (true) {
			LoanSubsystem loanSub;
			try {
				loanSub = LoanSubsystem.getLoanSubsystem();
				System.out.println("Enter amount:");
				float amount = s.nextFloat();
				System.out.println("Start date:");
				Date startDate = getDate();
				System.out.println("Final date:");
				Date finalDate = getDate();
				System.out.println("Enter account ID:");
				int accountId = s.nextInt();
				loanSub.insertLoan(amount, startDate, finalDate, accountId);
				break;
			}

			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private static Date getDate() {
		Date d = null;
		while (d == null) {
			System.out.println("Enter  date:");
			System.out.println("Enter  day:");
			int day = s.nextInt();
			System.out.println("Enter month:");
			int month = s.nextInt();
			System.out.println("Enter year:");
			int year = s.nextInt();

			d = new Date(day, month, year);
		}
		return d;

	}

	private static void deposit() {
		System.out.println("Enter account ID:");
	}

}