package org.example.restlogisticserp.models;

public class UserAuthLoginRequest {
    private String email;
    private String password;
    private String userAgent;
    private String ipAddress;

    // Default constructor (no-argument constructor)
    public UserAuthLoginRequest() {
    }

    // Constructor with parameters
    public UserAuthLoginRequest(String email, String password, String userAgent, String ipAddress) {
        this.email = email;
        this.password = password;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "UserAuthLoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
