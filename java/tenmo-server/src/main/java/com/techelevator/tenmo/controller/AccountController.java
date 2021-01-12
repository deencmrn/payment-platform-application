package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
//@PreAuthorize("isAuthenticated()")
public class AccountController {

	private AccountDao accountDao;
	private TransferDao transferDao;
	private UserDAO userDao;

	public AccountController(AccountDao accountDao, TransferDao transferDao, UserDAO userDao) {
		this.accountDao = accountDao;
		this.transferDao = transferDao;
		this.userDao = userDao;
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public List<Account> listAccounts() {
		return accountDao.list();
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.POST)
	public void create(@RequestBody Account account) {
		accountDao.create(account);
	}

	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
	public Account listAccountId(@PathVariable long id) {
		return accountDao.getAccountId(id);
	}

	@RequestMapping(path = "/accounts/{id}/{userId}", method = RequestMethod.GET)
	public Account listUserId(@PathVariable long id, @PathVariable int userId) {
		return accountDao.getUserId(userId);
	}

	@RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET) // don't need to have /accounts, but
																					// it's more descriptive
	public BigDecimal balance(@PathVariable long id) {
		return accountDao.getBalance(id);
	}

	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.PUT)
	public void update(@RequestBody BigDecimal account, @PathVariable long id) throws Exception {
		accountDao.updateAcctBalance(account, id);
	}

	@RequestMapping(path = "/transfers", method = RequestMethod.POST) // we will accept any Transfer object
	public Transfer transfer(@RequestBody Transfer transfer) {
		BigDecimal updatedFromBal = accountDao.getBalance(transfer.getAccountFrom()).subtract(transfer.getAmount()); // acctfrom
		BigDecimal updatedToBal = accountDao.getBalance(transfer.getAccountTo()).add(transfer.getAmount());

		accountDao.updateAcctBalance(updatedToBal, transfer.getAccountTo());
		accountDao.updateAcctBalance(updatedFromBal, transfer.getAccountFrom());
		
		transferDao.insertTransferLog(transfer);
		return transfer;
	} 

	@RequestMapping(path = "/transfers", method = RequestMethod.GET)
	public List<Transfer> findAllTransfers() {
		return transferDao.findAllTransfers();
	}

	@RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
	public Transfer getTransferId(@PathVariable long id) {
		return transferDao.getTransferId(id);
	}

}
