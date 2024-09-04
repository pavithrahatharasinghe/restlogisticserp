package org.example.restlogisticserp.models;

public class InquirySearchParams {
    private String fromDate;
    private String toDate;
    private String mode;
    private String pol;
    private String pod;
    private String inquiryReference;
    private int companyId;

    // Getters and Setters
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getInquiryReference() {
        return inquiryReference;
    }

    public void setInquiryReference(String inquiryReference) {
        this.inquiryReference = inquiryReference;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
