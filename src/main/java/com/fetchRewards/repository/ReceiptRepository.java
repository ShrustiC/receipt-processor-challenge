package com.fetchRewards.repository;

import com.fetchRewards.pojo.Receipt;
import com.fetchRewards.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/*
   This is an in memory repository to store receipts against a randomly generated id.

   Code currently redundant as we don't have an id as a receipt field.
   This will be more useful when a database is attached to this project.
    */
@Repository
public class ReceiptRepository {

    Logger logger = LoggerFactory.getLogger(ReceiptRepository.class);

    private final Map<String, Receipt> receiptMap = new HashMap<>();

    public void saveReceipt(String receipt_id, Receipt receipt) {
        logger.debug("Saving receipt with id: "+receipt_id);
        receiptMap.put(receipt_id, receipt);
    }

    public Receipt getReceiptById(String id) {
        logger.debug("Fetching receipt with id: "+id);
        return receiptMap.get(id);
    }
}
