package com.techelevator.tenmo.dao.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {

	JdbcTemplate jdbcTemplate;

	public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfer> findAllTransfers() {
		
		List<Transfer> transferList = new ArrayList<>();
		
		String sql = "SELECT * FROM transfers";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Transfer transfer = mapRowToTransfers(results);
			transferList.add(transfer);
		}
		return transferList;
	}

	@Override
	public List<Transfer> getUsersTransfers() {

		List<Transfer> transferList = new ArrayList<>();
		
		String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Transfer transfer = mapRowToTransfers(results);
			transferList.add(transfer);
		}
		return transferList;

	}

	private Transfer mapRowToTransfers(SqlRowSet results) {
		
		Transfer theTransfer = new Transfer();
		
		theTransfer.setTransferId(results.getLong("transfer_id"));
		theTransfer.setTransferTypeId(results.getInt("transfer_type_id"));
		theTransfer.setTransferStatusId(results.getInt("transfer_status_id"));
		theTransfer.setAccountFrom(results.getInt("account_from"));
		theTransfer.setAccountTo(results.getInt("account_to"));
		theTransfer.setAmount(results.getBigDecimal("amount"));
		return theTransfer;
	}

	@Override
	public Transfer getTransferId(long id) {
		
		Transfer transfer = new Transfer();
		
		String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			transfer = mapRowToTransfers(results);
		}
		return transfer;
	}

	@Override
	public void insertTransferLog(Transfer transfer) {

		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id,"
				+ " account_from, account_to, amount) VALUES (2, 2, ?, ?, ?)";
		jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

	}

}
