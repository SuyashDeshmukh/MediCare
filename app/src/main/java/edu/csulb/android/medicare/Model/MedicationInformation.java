package edu.csulb.android.medicare.Model;

/**
 * Created by Samruddhi on 4/4/2017.
 */

/*
    Medication Information Model for storing all the medicine and reminder information.

 */
public class MedicationInformation {
    private Long id;
    private String medicationName;
    private String dosageUnit;
    private String dosageQuantity;
    private String instructions;
    private String repeatTime;
    private String daysOfWeek;
    private int numberOfDays;
    private int hour;
    private int minute;
    private String startDate;

    public MedicationInformation(){

    }

    public MedicationInformation(Long id, String medicationName, String dosageUnit, String dosageQuantity, String instructions, String repeatTime, String daysOfWeek, int numberOfDays, int hour, int minute, String startDate) {
        this.id = id;
        this.medicationName = medicationName;
        this.dosageUnit = dosageUnit;
        this.dosageQuantity = dosageQuantity;
        this.instructions = instructions;
        this.repeatTime = repeatTime;
        this.daysOfWeek = daysOfWeek;
        this.numberOfDays = numberOfDays;
        this.hour = hour;
        this.minute = minute;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getDosageQuantity() {
        return dosageQuantity;
    }

    public void setDosageQuantity(String dosageQuantity) {
        this.dosageQuantity = dosageQuantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
