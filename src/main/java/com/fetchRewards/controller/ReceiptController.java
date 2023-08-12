package com.fetchRewards.controller;

import com.fetchRewards.dto.ErrorResponse;
import com.fetchRewards.dto.GetPointsResponse;
import com.fetchRewards.dto.ProcessReceiptResponse;
import com.fetchRewards.pojo.Receipt;
import com.fetchRewards.service.ReceiptService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/*
* Controller to handle incoming HTTP requests from client
* */
@RestController
@RequestMapping("/receipts")
@Validated
public class ReceiptController {

    Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<ProcessReceiptResponse> processReceipt(@RequestBody  @Valid Receipt receipt) {

        logger.info("POST req received, processing receipt");
        ProcessReceiptResponse response = receiptService.processReceipt(receipt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/points")
    @ResponseBody
    public ResponseEntity<?> getPointsForReceiptId(@PathVariable String id) {
        logger.info("GET req received, calculating points for receipt with id: "+id);

        // Return points for the specific receipt id
        int points = 0;
        Receipt receipt = receiptService.getReceiptById(id);
        if (receipt == null) {

            // returns an empty response body, change if custom message required
            //return ResponseEntity.notFound().build();

            ErrorResponse errorResponse = new ErrorResponse("Receipt not found for ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        }else{
          points = receiptService.getPointsForReceiptId(receipt);
        }

        GetPointsResponse response = new GetPointsResponse(points);
        return ResponseEntity.ok(response);
    }


}
