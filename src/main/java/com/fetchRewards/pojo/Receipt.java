package com.fetchRewards.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class Receipt {

    //model class for receipt schema based on api.yml

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_&]+$")
    private String retailer;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") // Regex for YYYY-MM-DD format
    private String purchaseDate;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}$") // Regex for HH:MM format
    private String purchaseTime;

    @Valid
    @NotEmpty
    private List<Item> items;

    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String total;

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
