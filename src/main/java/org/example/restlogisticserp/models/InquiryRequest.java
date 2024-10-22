package org.example.restlogisticserp.models;

import java.sql.Timestamp;

public class InquiryRequest {

    private int id;
    private long companyId;
    private long inquiryId;
    private boolean accepted;
    private long createdBy;
    private long updatedBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor
    public InquiryRequest() {
    }

    public InquiryRequest(int id, long companyId, long inquiryId, boolean accepted, long createdBy, long updatedBy, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.companyId = companyId;
        this.inquiryId = inquiryId;
        this.accepted = accepted;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(long inquiryId) {
        this.inquiryId = inquiryId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "InquiryRequest{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", inquiryId=" + inquiryId +
                ", accepted=" + accepted +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

