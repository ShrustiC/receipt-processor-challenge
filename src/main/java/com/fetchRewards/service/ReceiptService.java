package com.fetchRewards.service;

import com.fetchRewards.controller.ReceiptController;
import com.fetchRewards.dto.ProcessReceiptResponse;
import com.fetchRewards.pojo.Item;
import com.fetchRewards.pojo.Receipt;
import com.fetchRewards.repository.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Service layer with business logic and application specific rules
 * */

@Service
public class ReceiptService {

    Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }


    public ProcessReceiptResponse processReceipt(Receipt receipt) {

        logger.debug("ProcessReceipt method called");

        //return 200 for valid receipt
        //return 400 for invalid receipt; validation handled in controller

        String receipt_id = generateUniqueId();
        receiptRepository.saveReceipt(receipt_id, receipt);
        ProcessReceiptResponse response = new ProcessReceiptResponse();
        response.setId(receipt_id);

        logger.debug("processReceipt method completed");
        return response;
    }

    public Receipt getReceiptById(String id) {
        logger.debug("getReceiptById method called for ID: {}", id);
        return receiptRepository.getReceiptById(id);
    }

    public int getPointsForReceiptId(Receipt receipt) {
        logger.debug("getPointsForReceiptId method called");
        int totalRewardPoints = 0;

        totalRewardPoints = RetailerNameRewards(receipt.getRetailer())
                + RoundDollarAmountRewards(receipt.getTotal())
                + AmountMultipleRewards(receipt.getTotal())
                + EveryTwoItemRewards(receipt.getItems())
                + ItemDescriptionLengthRewards(receipt.getItems())
                + PurchaseTimeRewards(receipt.getPurchaseTime())
                + OddDatePurchaseRewards(receipt.getPurchaseDate());

        logger.debug("getPointsForReceiptId method complete");
        return totalRewardPoints;
    }



    //Rule : One point for every alphanumeric character in the retailer name
    private int RetailerNameRewards(String retailer){
        // Regex to match alphanumeric characters
        String alphanumericPattern = "[a-zA-Z0-9]";
        int points = 0;

        Pattern pattern = Pattern.compile(alphanumericPattern);
        Matcher matcher = pattern.matcher(retailer);

        // To count the number of matches
        while (matcher.find()) {
            points++;
        }
        logger.debug("Retailer name points: {}", points);
        return points;
    }

    //Rule : 50 points if the total is a round dollar amount with no cents
    private int RoundDollarAmountRewards(String total){

        int points = 0;
        double totalAmount = Double.parseDouble(total);

        // Round dollar amount with no cents
        if (totalAmount != 0 && totalAmount == Math.floor(totalAmount)) {
            points += 50;
        }
        logger.debug("Round dollar amount points: {}", points);
        return points;
    }

    //Rule : 25 points if the total is a multiple of 0.25
    private int AmountMultipleRewards(String total){
        int points = 0;
        double totalAmount = Double.parseDouble(total);
        double remainder = totalAmount % 0.25 ;

        if ( remainder == 0.0) {
            points += 25;
        }
        logger.debug("Amount multiple points: {}", points);
        return points;
    }

    //Rule : 5 points for every two items on the receipt
    private int EveryTwoItemRewards(List<Item> items){

        int points = 0;
        points += (items.size() / 2) * 5;
        logger.debug("Every two item points: {}", points);
        return points;
    }

    /* Rule : If the trimmed length of the item description is a multiple of 3, multiply the price
              by 0.2 and round up to the nearest integer. The result is the number of points earned */

    private int ItemDescriptionLengthRewards(List<Item> items){

        int points = 0;
        for(Item i : items){
            if(i.getShortDescription().trim().length() % 3 == 0){
                double price = Double.parseDouble(i.getPrice());
                points += Math.ceil(price * 0.2 );
            }
        }
        logger.debug("Item description length points: {}", points);
        return points;
    }

    //Rule : 6 points if the day in the purchase date is odd
    private int OddDatePurchaseRewards(String purchaseDate){

        int points = 0;
        LocalDate date =  LocalDate.parse(purchaseDate);
        int day = date.getDayOfMonth();

        if(day % 2 != 0){
            points +=6;
        }

        logger.debug("Odd date purchase points: {}", points);
        return points;
    }

    //Rule : 10 points if the time of purchase is after 2:00pm and before 4:00pm.
    private int PurchaseTimeRewards(String purchaseTime){

        //refered from : https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html
        //assumption from example receipt 24hr clock format
        int points = 0;
        LocalTime time = LocalTime.parse(purchaseTime);

        LocalTime startTime = LocalTime.of(14, 0); // 2:00pm
        LocalTime endTime = LocalTime.of(16, 0);   // 4:00pm

        if (time.isAfter(startTime) && time.isBefore(endTime)) {
            points +=10;
        }

        logger.debug("Purchase time points: {}", points);
        return points;
    }

    public String generateUniqueId() {

        // id generation logic goes here
        UUID uniqueId = UUID.randomUUID();
        return uniqueId.toString();
    }

}
