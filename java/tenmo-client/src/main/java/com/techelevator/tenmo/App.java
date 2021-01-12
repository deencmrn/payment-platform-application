package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AccountServiceException;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID = "View specific transfer by its ID";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private AccountService accountService;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private Account account;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;

	}

	List<Account> accounts = new ArrayList<>();

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_TRANSFER_BY_ID.equals(choice)) {
				viewTransferByID();
			}else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {

		BigDecimal balance;
		balance = accountService.getBalance(currentUser.getToken(), currentUser.getUser());
		System.out.println(balance);

	}

	private void viewTransferHistory() {

		Account[] theAccounts = accountService.getAllAccounts(currentUser.getToken());
		Transfer transferList = new Transfer();

		for (Account accounts : theAccounts) {
			transferList.setAccountFrom(currentUser.getUser().getId());
			transferList.setAccountTo(currentUser.getUser().getId());
		}
		Transfer[] transfers = accountService.getAllUsersTransfers(transferList);
		for (Transfer transfer : transfers)
			System.out.println(String.format("%1s", "   TransID: ") + String.format("%1s", transfer.getTransferId())
					+ String.format("%1s", "   TransStatus: ") + String.format("%1s", transfer.getTransferStatusId())
					+ String.format("%1s", "   TransType: ") + String.format("%1s", transfer.getTransferTypeId())
					+ String.format("%1s", "   AcctFrom: ") + String.format("%1s", transfer.getAccountFrom())
					+ String.format("%1s", "   AcctTo: ") + String.format("%1s", transfer.getAccountTo())
					+ String.format("%1s", "   FinalAmount: ") + String.format("%1s", transfer.getAmount()));
	}

	private void viewTransferByID() {
		System.out.println("Enter the 'Transfer ID' to view the specified transfer");
		Scanner userChoice = new Scanner(System.in);
		String userChoiceSlot = userChoice.nextLine();
		
		
		Transfer transferList = new Transfer();
		transferList.setTransferId(Long.parseLong(userChoiceSlot));
		
		
		
		
		Transfer transfers = accountService.getTransferByID(transferList);
		
			System.out.println(String.format("%1s", "   TransID: ") + String.format("%1s", transfers.getTransferId())
					+ String.format("%1s", "   TransStatus: ") + String.format("%1s", transfers.getTransferStatusId())
					+ String.format("%1s", "   TransType: ") + String.format("%1s", transfers.getTransferTypeId())
					+ String.format("%1s", "   AcctFrom: ") + String.format("%1s", transfers.getAccountFrom())
					+ String.format("%1s", "   AcctTo: ") + String.format("%1s", transfers.getAccountTo())
					+ String.format("%1s", "   FinalAmount: ") + String.format("%1s", transfers.getAmount()));
	
	}
	
	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {

		System.out.println("Please choose the account to send TEBucks: ");

		Account[] theAccounts = accountService.getAllAccounts(currentUser.getToken());
		for (Account accounts : theAccounts) {
			System.out.println("AccountId: " + String.format("%1s", accounts.getUserId()));
		}

		Scanner userChoice = new Scanner(System.in); // taking in the user input from the keyboard
		String userChoiceSlot = userChoice.nextLine();

		System.out.println("You've choosen to send to account id: " + userChoiceSlot);
		System.out.println("How much would you like to send?: ");

		Scanner amtToSend = new Scanner(System.in);
		String amtToSendSlot = amtToSend.nextLine();

		System.out.println("You've choosen to send $" + amtToSendSlot);
		System.out.println("Would you like to send?(Y/N): ");

		Scanner approvedToSendAmt = new Scanner(System.in);
		String approvedToSendAmtSlot = approvedToSendAmt.nextLine();

		if (approvedToSendAmtSlot.equalsIgnoreCase("Y") || approvedToSendAmtSlot.equalsIgnoreCase("N")) { // if input is valid
			if (approvedToSendAmtSlot.equalsIgnoreCase("Y")) {
				BigDecimal balance;
				balance = accountService.getBalance(currentUser.getToken(), currentUser.getUser());
				BigDecimal bgApprovedAmtToSend = new BigDecimal(amtToSendSlot);

				int result = balance.compareTo(bgApprovedAmtToSend);
				if (result == -1) {
					System.out.println("Not enough money in your account to send $" + approvedToSendAmtSlot);
				} else if (result == 1 || result == 0) {

					Transfer transfer = new Transfer();
					for (Account accounts : theAccounts) {

						transfer.setAccountFrom(currentUser.getUser().getId());
						transfer.setAccountTo(Integer.parseInt(userChoiceSlot));
						transfer.setAmount(bgApprovedAmtToSend);

					}
					accountService.sendBucks(transfer);

					balance = balance.subtract(bgApprovedAmtToSend); // money handling
					System.out.println(
							"You have successfully transferred: $" + amtToSendSlot + " to the chosen account.");
					System.out.println("Your balance is now $" + balance + ".");
				}
			} else {
				System.out.println("Transaction Cancelled, GoodBye!");
				System.exit(1);
			}
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
