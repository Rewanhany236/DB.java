package com.example.demo9.model;

public class RentalAgreement {
    private int rentalId;
    private int contractorId;
    private String contractorName;     // من الجوين
    private String startDate;
    private String endDate;
    private double totalCost;
    private int totalHours;
    private String rentalStatus;

    // Constructor
    public RentalAgreement(int rentalId, int contractorId, String contractorName,
                           String startDate, String endDate, double totalCost,
                           int totalHours, String rentalStatus) {
        this.rentalId = rentalId;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.totalHours = totalHours;
        this.rentalStatus = rentalStatus;
    }

    // Getters
    public int getRentalId() { return rentalId; }
    public int getContractorId() { return contractorId; }
    public String getContractorName() { return contractorName; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public double getTotalCost() { return totalCost; }
    public int getTotalHours() { return totalHours; }
    public String getRentalStatus() { return rentalStatus; }

    // Setters
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    public void setContractorId(int contractorId) { this.contractorId = contractorId; }
    public void setContractorName(String contractorName) { this.contractorName = contractorName; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
    public void setRentalStatus(String rentalStatus) { this.rentalStatus = rentalStatus; }
}