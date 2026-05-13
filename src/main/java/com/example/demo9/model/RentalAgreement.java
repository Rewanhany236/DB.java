package com.example.demo9.model;
import java.time.LocalDateTime;

public class RentalAgreement {

    private int rentalId;           // RENTAL_ID_ (PK, int)
    private Integer contractorId;   // CONTRACTOR_ID_ (FK, int)
    private String startDate;       // START_DATE (datetime)
    private String endDate;         // END_DATE (datetime)
    private double totalCost;       // TOTAL_COST (float)
    private Integer totalHours;     // TOTAL_HOURS (int)
    private String rentalStatus;    // RENTAL_STATUS (text)

    public RentalAgreement(int rentalId, Integer contractorId, String startDate, String endDate,
                           double totalCost, Integer totalHours, String rentalStatus) {
        this.rentalId = rentalId;
        this.contractorId = contractorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.totalHours = totalHours;
        this.rentalStatus = rentalStatus;
    }

    public int getRentalId() {
        return rentalId;
    }
    public Integer getContractorId() {
        return contractorId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    // Setters
    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public void setContractorId(Integer contractorId) {
        this.contractorId = contractorId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }
}


