package org.example.restlogisticserp.models;

import java.sql.Timestamp;

public class UserAuth {
    private String sessionId;
    private int userId;
    private Timestamp lastActivity;
    private String userAgent;
    private String ipAddress;
    private boolean sessionExpired;  // Changed to boolean to represent session status
    private String sessionToken;
    private Timestamp createdAt;

    public UserAuth(String sessionId, int userId, Timestamp lastActivity, String userAgent,
                    String ipAddress, boolean sessionExpired, String sessionToken, Timestamp createdAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.lastActivity = lastActivity;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.sessionExpired = sessionExpired;
        this.sessionToken = sessionToken;
        this.createdAt = createdAt;
    }

    // Getters
    public String getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getLastActivity() {
        return lastActivity;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isSessionExpired() {
        return sessionExpired;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLastActivity(Timestamp lastActivity) {
        this.lastActivity = lastActivity;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setSessionExpired(boolean sessionExpired) {
        this.sessionExpired = sessionExpired;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserAuth{" +
                "sessionId='" + sessionId + '\'' +
                ", userId=" + userId +
                ", lastActivity=" + lastActivity +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", sessionExpired=" + sessionExpired +
                ", sessionToken='" + sessionToken + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
