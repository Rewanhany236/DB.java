package com.example.demo9.model;

public class Contractor {
    private int contractorId;      // CONTRACTOR_ID (PK, int)
    private String companyName;    // COMPANY_NAME (varchar)
    private double creditLimit;    // CREDIT_LIMIT (float)
    private String address;        // ADDRESS (varchar)
    private String email;          // EMAIL (text)
    private String phone;          // PHONE (text)

    public Contractor(int contractorId, String companyName, double creditLimit, String address, String email, String phone) {
        this.contractorId = contractorId;
        this.companyName = companyName;
        this.creditLimit = creditLimit;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }
    // Getters
    public int getContractorId() { return contractorId; }
    public String getCompanyName() { return companyName; }
    public double getCreditLimit() { return creditLimit; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setters
    public void setContractorId(int contractorId) { this.contractorId = contractorId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }
    public void setAddress(String address) { this.address = address; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}