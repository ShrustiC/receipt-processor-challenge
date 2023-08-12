package com.fetchRewards.dto;

public class GetPointsResponse {

    private int points;

    public GetPointsResponse(int receiptPoints) {
        this.points = receiptPoints;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
