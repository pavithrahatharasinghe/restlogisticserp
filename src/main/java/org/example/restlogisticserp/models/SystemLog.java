package org.example.restlogisticserp.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class SystemLog {
    private int logId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp logTimestamp;

    private String severity;
    private String source;
    private String ipAddress;
    private String message;
    private int userId;

    public SystemLog() {
    }

    public SystemLog(int logId, Timestamp logTimestamp, String severity, String source, String ipAddress, String message, int userId) {
        this.logId = logId;
        this.logTimestamp = logTimestamp;
        this.severity = severity;
        this.source = source;
        this.ipAddress = ipAddress;
        this.message = message;
        this.userId = userId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
