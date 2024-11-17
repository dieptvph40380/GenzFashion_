package fpl.md37.genz_fashion.models;

import com.google.firebase.Timestamp;

public class User {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;

    public User() {
    }

    public User(String phone, String username, Timestamp createdTimestamp, String userId, String fcmToken) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.fcmToken = fcmToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
