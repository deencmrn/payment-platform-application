package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao { 

	List<Transfer> findAllTransfers();
	
	Transfer getTransferId(long id);

	void insertTransferLog(Transfer transfer);

	List<Transfer> getUsersTransfers();
}
