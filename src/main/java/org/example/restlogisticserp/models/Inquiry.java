package org.example.restlogisticserp.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Inquiry {

    private int inquiryId;
    private int companyId;
    private String inquiryReference;
    private String shipmentMode;
    private String shipmentType;
    private String fclType;
    private Double cbmVolume;
    private String dimensions;
    private Double grossWeight;
    private String portOfOrigin;
    private String portOfDischarge;
    private String commodity;
    private String cargoDescription;
    private Boolean dangerousGoods;
    private String freightTerm;
    private String incoTerm;
    private Date preferredDateOfArrival;
    private Date preferredDateOfDeparture;
    private String preferredTransitPeriod;
    private Timestamp cutOffTime;    // Change to Timestamp
    private String status;
    private String publishedStatus;
    private Timestamp publishDate;   // Change to Timestamp
    private Boolean bidAccepted;     // Use Boolean for nullable boolean
    private Integer acceptedBidId;   // Changed from Long to Integer
    private int createdBy;           // Changed from Long to int

    public Inquiry() {
    }

    public Inquiry(int inquiryId, int companyId, String inquiryReference, String shipmentMode, String shipmentType, String fclType, Double cbmVolume, String dimensions, Double grossWeight, String portOfOrigin, String portOfDischarge, String commodity, String cargoDescription, Boolean dangerousGoods, String freightTerm, String incoTerm, Date preferredDateOfArrival, Date preferredDateOfDeparture, String preferredTransitPeriod, Timestamp cutOffTime, String status, String publishedStatus, Timestamp publishDate, Boolean bidAccepted, Integer acceptedBidId, int createdBy) {
        this.inquiryId = inquiryId;
        this.companyId = companyId;
        this.inquiryReference = inquiryReference;
        this.shipmentMode = shipmentMode;
        this.shipmentType = shipmentType;
        this.fclType = fclType;
        this.cbmVolume = cbmVolume;
        this.dimensions = dimensions;
        this.grossWeight = grossWeight;
        this.portOfOrigin = portOfOrigin;
        this.portOfDischarge = portOfDischarge;
        this.commodity = commodity;
        this.cargoDescription = cargoDescription;
        this.dangerousGoods = dangerousGoods;
        this.freightTerm = freightTerm;
        this.incoTerm = incoTerm;
        this.preferredDateOfArrival = preferredDateOfArrival;
        this.preferredDateOfDeparture = preferredDateOfDeparture;
        this.preferredTransitPeriod = preferredTransitPeriod;
        this.cutOffTime = cutOffTime;
        this.status = status;
        this.publishedStatus = publishedStatus;
        this.publishDate = publishDate;
        this.bidAccepted = bidAccepted;
        this.acceptedBidId = acceptedBidId;
        this.createdBy = createdBy;
    }

    // Getters and setters (all updated to use int and Integer instead of Long)
    public int getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(int inquiryId) {
        this.inquiryId = inquiryId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getInquiryReference() {
        return inquiryReference;
    }

    public void setInquiryReference(String inquiryReference) {
        this.inquiryReference = inquiryReference;
    }

    public String getShipmentMode() {
        return shipmentMode;
    }

    public void setShipmentMode(String shipmentMode) {
        this.shipmentMode = shipmentMode;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getFclType() {
        return fclType;
    }

    public void setFclType(String fclType) {
        this.fclType = fclType;
    }

    public Double getCbmVolume() {
        return cbmVolume;
    }

    public void setCbmVolume(Double cbmVolume) {
        this.cbmVolume = cbmVolume;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getPortOfOrigin() {
        return portOfOrigin;
    }

    public void setPortOfOrigin(String portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public Boolean getDangerousGoods() {
        return dangerousGoods;
    }

    public void setDangerousGoods(Boolean dangerousGoods) {
        this.dangerousGoods = dangerousGoods;
    }

    public String getFreightTerm() {
        return freightTerm;
    }

    public void setFreightTerm(String freightTerm) {
        this.freightTerm = freightTerm;
    }

    public String getIncoTerm() {
        return incoTerm;
    }

    public void setIncoTerm(String incoTerm) {
        this.incoTerm = incoTerm;
    }

    public Date getPreferredDateOfArrival() {
        return preferredDateOfArrival;
    }

    public void setPreferredDateOfArrival(Date preferredDateOfArrival) {
        this.preferredDateOfArrival = preferredDateOfArrival;
    }

    public Date getPreferredDateOfDeparture() {
        return preferredDateOfDeparture;
    }

    public void setPreferredDateOfDeparture(Date preferredDateOfDeparture) {
        this.preferredDateOfDeparture = preferredDateOfDeparture;
    }

    public String getPreferredTransitPeriod() {
        return preferredTransitPeriod;
    }

    public void setPreferredTransitPeriod(String preferredTransitPeriod) {
        this.preferredTransitPeriod = preferredTransitPeriod;
    }

    public Timestamp getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Timestamp cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishedStatus() {
        return publishedStatus;
    }

    public void setPublishedStatus(String publishedStatus) {
        this.publishedStatus = publishedStatus;
    }

    public Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public Boolean getBidAccepted() {
        return bidAccepted;
    }

    public void setBidAccepted(Boolean bidAccepted) {
        this.bidAccepted = bidAccepted;
    }

    public Integer getAcceptedBidId() {
        return acceptedBidId;
    }

    public void setAcceptedBidId(Integer acceptedBidId) {
        this.acceptedBidId = acceptedBidId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
