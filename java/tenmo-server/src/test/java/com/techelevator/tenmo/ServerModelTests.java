package com.techelevator.tenmo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

class ServerModelTests {

	@Test
	public void accountConstructorHasLong_Int_And_BD() {

		Account account = new Account(1, 5, BigDecimal.valueOf(34.34));

		assertEquals(1, account.getAccountId());
		assertEquals(BigDecimal.valueOf(34.34), account.getBalance());
		assertEquals(5, account.getUserId());
	}

	@Test
	public void transferConstructorHasCorrectVariables() {

		Transfer transfer = new Transfer(1, 3, 5, 7, 6, BigDecimal.valueOf(34.34));

		assertEquals(1, transfer.getTransferId());
		assertEquals(3, transfer.getTransferTypeId());
		assertEquals(5, transfer.getTransferStatusId());
		assertEquals(7, transfer.getAccountFrom());
		assertEquals(6, transfer.getAccountTo());
		assertEquals(BigDecimal.valueOf(34.34), transfer.getAmount());
	}

}
