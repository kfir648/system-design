import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.apache.derby.tools.sysinfo;

import DBManegment.DataBaseService;
import DBManegment.DatabaseInterface;
import GUI.LogInDialog;
import sun.java2d.cmm.ProfileActivator;
import sysDesign.*;
import sysDesign.Worker.PermissionType;

public class Main {

	public static Scanner s = new Scanner(System.in);

	public static void main(String[] args) {

		boolean secssed = false;
		Worker worker = null;
		PermissionType permission = null;
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
				System.out.println("[B/b] = Check balance");
				System.out.println("[C/c] = Add customer");
				System.out.println("[T/t] = Add account");
				break;
			}
			char selection = s.next().charAt(0);

			switch (selection) {
			case 'A':
			case 'a':
				addWorker(worker);
				break;

			case 'L':
			case 'l':
				loan();
				break;

			case 'S':
			case 's':
				saiving();
				break;

			case 'D':
			case 'd':
				
				break;

			case 'W':
			case 'w':

				break;

			case 'B':
			case 'b':

				break;

				
			case 'C':
			case 'c':

				break;

			case 'T':
			case 't':

				break;
			}

		}
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

	private static void deposit(){
		System.out.println("Enter account ID:");
	}
	
	
}