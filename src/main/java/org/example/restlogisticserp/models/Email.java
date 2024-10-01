package org.example.restlogisticserp.models;

public class Email {
    private String toEmail;
    private String subject;
    private String body;

    // No-argument constructor
    public Email() {
    }

    // Constructor with parameters
    public Email(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    // Getters and Setters
    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
