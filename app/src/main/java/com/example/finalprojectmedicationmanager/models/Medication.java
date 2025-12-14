package com.example.finalprojectmedicationmanager.models;

public class Medication {
    private String id;
    private String name;
    private String dosage;
    private String frequency;
    private String instructions;
    private String startDate;
    private String endDate;
    private String prescriber;
    private String userId;

    public Medication() {
        // Required for Firebase
    }

    public Medication(String name, String dosage, String frequency, String instructions,
                      String startDate, String endDate, String prescriber, String userId) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.instructions = instructions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prescriber = prescriber;
        this.userId = userId;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getPrescriber() { return prescriber; }
    public void setPrescriber(String prescriber) { this.prescriber = prescriber; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}