package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class AccountService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();
	private final User user = new User();
	private final Account account = new Account();
	private final Transfer transfer = new Transfer();

	public AccountService(String url) {
		this.BASE_URL = url;
	}

	public BigDecimal getBalance(String AUTH_TOKEN, User user) {

		HttpEntity entity = new HttpEntity<>(AUTH_TOKEN);

		ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + "accounts/" + user.getId(), HttpMethod.GET,
				entity, Account.class);

		return response.getBody().getBalance();
	}

	public Account[] getAllAccounts(String AUTH_TOKEN) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity(headers);

		ResponseEntity<Account[]> response = restTemplate.exchange(BASE_URL + "accounts/", HttpMethod.GET, entity,
				Account[].class);

		return response.getBody();

	}

	public Transfer[] getAllUsersTransfers(Transfer transfer) {

		ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "transfers/", HttpMethod.GET,
				makeTransferEntity(transfer), Transfer[].class);

		return response.getBody();
	}

	public Transfer getTransferByID(Transfer transfer) {
		
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/" + transfer.getTransferId() , HttpMethod.GET, makeTransferEntity(transfer), Transfer.class);
		
		return response.getBody();
	}
	public Transfer sendBucks(Transfer transfer) {

		Transfer theTransfer = new Transfer();

		try {
			restTemplate.exchange(BASE_URL + "transfers/", HttpMethod.POST, makeTransferEntity(transfer),
					Transfer.class);
		} catch (RestClientException ex) {
			System.out.println("Uh Oh! There's an error in AccountService.java, method sendBucks()" + ex.getMessage());
		}
		return theTransfer;
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

}
