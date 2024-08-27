package org.example.restlogisticserp.models;

public class BidSummary {
    private int ongoingCount;
    private int expiredCount;
    private int acceptedCount;
    private int rejectedCount;
    private int totalCount;

    // Getters and setters
    public int getOngoingCount() {
        return ongoingCount;
    }

    public void setOngoingCount(int ongoingCount) {
        this.ongoingCount = ongoingCount;
    }

    public int getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(int expiredCount) {
        this.expiredCount = expiredCount;
    }

    public int getAcceptedCount() {
        return acceptedCount;
    }

    public void setAcceptedCount(int acceptedCount) {
        this.acceptedCount = acceptedCount;
    }

    public int getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(int rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "BidSummary{" +
                "ongoingCount=" + ongoingCount +
                ", expiredCount=" + expiredCount +
                ", acceptedCount=" + acceptedCount +
                ", rejectedCount=" + rejectedCount +
                ", totalCount=" + totalCount +
                '}';
    }
}
