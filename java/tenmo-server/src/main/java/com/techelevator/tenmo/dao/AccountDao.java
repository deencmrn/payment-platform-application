package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;


public interface AccountDao {

	List<Account> list();
	
	void create(Account account);
	
	Account getAccountId(long id);
	
	Account getUserId(int userId);

	BigDecimal getBalance(long id); // saying this is going to take in a balance, we want it to
	//take in the acct id

	void updateAcctBalance(BigDecimal balance, long id);

}
