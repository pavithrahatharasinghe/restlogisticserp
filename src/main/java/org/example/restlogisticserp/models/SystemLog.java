package org.example.restlogisticserp.models;

import java.sql.Timestamp;

public class SystemLog {
    private Long logId;
    private Timestamp logTimestamp;
    private String severity;
    private String source;
    private String ipAddress;
    private String message;
    private User user; // Foreign key to User

    public SystemLog(Long logId, Timestamp logTimestamp, String severity, String source, String ipAddress, String message, User user) {
        this.logId = logId;
        this.logTimestamp = logTimestamp;
        this.severity = severity;
        this.source = source;
        this.ipAddress = ipAddress;
        this.message = message;
        this.user = user;
    }

    public SystemLog() {
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Timestamp getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(Timestamp logTimestamp) {
        this.logTimestamp = logTimestamp;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
