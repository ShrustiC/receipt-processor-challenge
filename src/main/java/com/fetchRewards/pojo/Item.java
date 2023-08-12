package com.fetchRewards.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class Item {

    //model class for Item schema based on api.yml

    @NotEmpty
    @Pattern(regexp = "^[\\w\\s\\-]+$")
    private String shortDescription;

    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String price;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
