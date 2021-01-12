package com.techelevator.tenmo.dao.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDao implements AccountDao {

	JdbcTemplate jdbcTemplate;

	public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Account> list() {

		List<Account> accountList = new ArrayList<>();
		String sql = "SELECT * FROM accounts";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Account theAccount = mapRowToAccount(results);
			accountList.add(theAccount);
		}
		return accountList;
	}

	@Override
	public void create(Account account) {

		String sql = "INSERT INTO accounts (user_id, balance) Values (?, ?)";

		jdbcTemplate.update(sql, account.getUserId(), account.getBalance());
	}

	@Override
	public Account getAccountId(long accountId) {

		Account account = new Account();
		String sql = "SELECT * FROM accounts WHERE account_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

		if (results.next()) {
			account = mapRowToAccount(results);
		}
		return account;
	}

	@Override
	public Account getUserId(int userId) {

		Account users = new Account();
		String sql = "SELECT * FROM accounts WHERE user_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

		if (results.next()) {
			users = mapRowToAccount(results);
		}
		return users;
	}

	@Override
	public BigDecimal getBalance(long id) {

		BigDecimal accBalance = new BigDecimal(0.00);
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			accBalance = results.getBigDecimal("balance"); // took this from below
			// b/c the map method has a return method of Account obj and we want a return of
			// Big Decimal
		}
		// return this.getAccountId(id).getBalance();
		return accBalance;
	}

	@Override
	public void updateAcctBalance(BigDecimal balance, long id) {

		String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";

		jdbcTemplate.update(sql, balance, id);
	}

	private Account mapRowToAccount(SqlRowSet results) {

		Account theAccount = new Account();
		theAccount.setAccountId(results.getLong("account_id"));
		theAccount.setUserId(results.getInt("user_id"));
		theAccount.setBalance(results.getBigDecimal("balance"));
		return theAccount;

	}

}
