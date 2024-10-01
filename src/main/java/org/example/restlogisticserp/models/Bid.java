package org.example.restlogisticserp.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Bid {
    private Integer bidId;
    private Integer inquiryId; // Foreign key to Inquiry
    private Integer createdCompanyId; // Foreign key to Company
    private BigDecimal amount;
    
    private String status;
    private Boolean isAccepted;
    private Boolean isFinalized;
    private Integer createdBy; // Foreign key to User
    private Integer acceptedBy; // Foreign key to User (optional)
    private Integer inquiryOwnerCompanyId; // Foreign key to Company
    

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp bidDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp cutOffDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp updatedAt;

    public Bid() {
    }

    public Bid(Integer bidId, Integer inquiryId, Integer createdCompanyId, BigDecimal amount, Timestamp bidDate, Timestamp cutOffDate, String status, Boolean isAccepted, Boolean isFinalized, Integer createdBy, Integer acceptedBy, Integer inquiryOwnerCompanyId, Timestamp createdAt, Timestamp updatedAt) {
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

    public Integer getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(Integer inquiryId) {
        this.inquiryId = inquiryId;
    }

    public Integer getCreatedCompanyId() {
        return createdCompanyId;
    }

    public void setCreatedCompanyId(Integer createdCompanyId) {
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(Integer acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Integer getInquiryOwnerCompanyId() {
        return inquiryOwnerCompanyId;
    }

    public void setInquiryOwnerCompanyId(Integer inquiryOwnerCompanyId) {
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
