package org.example.restlogisticserp.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Bid {
    private Integer bidId;
    private Long inquiryId; // Foreign key to Inquiry
    private Long createdCompanyId; // Foreign key to Company
    private BigDecimal amount;
    private Timestamp bidDate;
    private Timestamp cutOffDate;
    private String status;
    private Boolean isAccepted;
    private Boolean isFinalized;
    private Long createdBy; // Foreign key to User
    private Long acceptedBy; // Foreign key to User (optional)
    private Long inquiryOwnerCompanyId; // Foreign key to Company
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Bid() {
    }

    public Bid(Integer bidId, Long inquiryId, Long createdCompanyId, BigDecimal amount, Timestamp bidDate, Timestamp cutOffDate, String status, Boolean isAccepted, Boolean isFinalized, Long createdBy, Long acceptedBy, Long inquiryOwnerCompanyId, Timestamp createdAt, Timestamp updatedAt) {
        this.bidId = bidId;
        this.inquiryId = inquiryId;
        this.createdCompanyId = createdCompanyId;
        this.amount = amount;
        this.bidDate = bidDate;
        this.cutOffDate = cutOffDate;
        this.status = status;
        this.isAccepted = isAccepted;
        this.isFinalized = isFinalized;
        this.createdBy = createdBy;
        this.acceptedBy = acceptedBy;
        this.inquiryOwnerCompanyId = inquiryOwnerCompanyId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Long getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(Long inquiryId) {
        this.inquiryId = inquiryId;
    }

    public Long getCreatedCompanyId() {
        return createdCompanyId;
    }

    public void setCreatedCompanyId(Long createdCompanyId) {
        this.createdCompanyId = createdCompanyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getBidDate() {
        return bidDate;
    }

    public void setBidDate(Timestamp bidDate) {
        this.bidDate = bidDate;
    }

    public Timestamp getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(Timestamp cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Boolean getIsFinalized() {
        return isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(Long acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Long getInquiryOwnerCompanyId() {
        return inquiryOwnerCompanyId;
    }

    public void setInquiryOwnerCompanyId(Long inquiryOwnerCompanyId) {
        this.inquiryOwnerCompanyId = inquiryOwnerCompanyId;
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
}
