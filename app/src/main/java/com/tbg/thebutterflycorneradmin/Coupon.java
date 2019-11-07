package com.tbg.thebutterflycorneradmin;

public class Coupon {

    String userEmail;
    String timestamp;
    String userId;
    String couponId;

    public Coupon(String userEmail, String timestamp, String userId, String couponId) {
        this.userEmail = userEmail;
        this.timestamp = timestamp;
        this.userId = userId;
        this.couponId = couponId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
