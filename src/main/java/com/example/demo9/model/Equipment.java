package com.example.demo9.model;

public class Equipment {
    private int equipmentId;      // EQUIPMENT_ID_ (PK)
    private Integer yardId;       // YARD_ID (FK)
    private String model;         // MODEL
    private int enginePower;      // ENGINE_POWER
    private double hourlyRate;    // HOURLY_RATE
    private String status;        // STATUS_
    private String currentLocation; // CURRENT_LOCATION
    private String equipmentType; // EQUIPMENT_TYPE

    public Equipment(int equipmentId, Integer yardId, String model, int enginePower,
                     double hourlyRate, String status, String currentLocation, String equipmentType) {
        this.equipmentId = equipmentId;
        this.yardId = yardId;
        this.model = model;
        this.enginePower = enginePower;
        this.hourlyRate = hourlyRate;
        this.status = status;
        this.currentLocation = currentLocation;
        this.equipmentType = equipmentType;
    }

    // Getters
    public int getEquipmentId() { return equipmentId; }
    public Integer getYardId() { return yardId; }
    public String getModel() { return model; }
    public int getEnginePower() { return enginePower; }
    public double getHourlyRate() { return hourlyRate; }
    public String getStatus() { return status; }
    public String getCurrentLocation() { return currentLocation; }
    public String getEquipmentType() { return equipmentType; }


    // setters
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }
    public void setYardId(Integer yardId) { this.yardId = yardId; }
    public void setModel(String model) { this.model = model; }
    public void setEnginePower(int enginePower) { this.enginePower = enginePower; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public void setStatus(String status) { this.status = status; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
}