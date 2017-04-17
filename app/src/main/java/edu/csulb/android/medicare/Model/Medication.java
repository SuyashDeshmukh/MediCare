package edu.csulb.android.medicare.Model;

/**
 * Created by Samruddhi on 4/2/2017.
 */

public class Medication {
    private Long id;
    private String medicationName;
    private String dosageUnit;
    private String dosageQuantity;
    private String instructions;
    private long keyReminder;

    public Medication(){

    }

    public Medication(Long id, String medicationName, String dosageUnit, String dosageQuantity, String instructions, long keyReminder) {
        this.id = id;
        this.medicationName = medicationName;
        this.dosageUnit = dosageUnit;
        this.dosageQuantity = dosageQuantity;
        this.instructions = instructions;
        this.keyReminder = keyReminder;
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

    public long getKeyReminder() {
        return keyReminder;
    }

    public void setKeyReminder(long keyReminder) {
        this.keyReminder = keyReminder;
    }

}
