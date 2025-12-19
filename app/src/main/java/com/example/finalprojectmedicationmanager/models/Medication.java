package com.example.finalprojectmedicationmanager.models;

import java.util.Date;
import java.util.List;

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

    // For notifications
    private List<String> reminderDays; // e.g., ["MON", "TUE", "WED"]
    private String reminderTime; // e.g., "08:00"
    private boolean remindersEnabled;
    private Date createdAt;

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
        this.createdAt = new Date();
        this.remindersEnabled = false;
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

    public List<String> getReminderDays() { return reminderDays; }
    public void setReminderDays(List<String> reminderDays) { this.reminderDays = reminderDays; }

    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }

    public boolean isRemindersEnabled() { return remindersEnabled; }
    public void setRemindersEnabled(boolean remindersEnabled) { this.remindersEnabled = remindersEnabled; }
}