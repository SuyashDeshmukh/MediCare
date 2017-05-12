package edu.csulb.android.medicare.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Samruddhi on 4/3/2017.
 */

public class Reminder implements Comparable<Reminder>{
    private long id;
    private String medicineName;
    private int hour;
    private int minute;
    private boolean daysOfWeek[] = new boolean[7];
    private String dosageUnit;
    private String dosageQuantity;
    private String instructions;
    private String repeatTime;
    private List<Long> medicineIds = new LinkedList<Long>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
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

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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

    public List<Long> getMedicineIds() {
        return Collections.unmodifiableList(medicineIds);
    }

    public void addMedicineIds(long id) { medicineIds.add(id); }

    public String getAmPm() { return (hour < 12) ? "am" : "pm"; }

    public Reminder(){

    }

    /**
     * A helper method which returns the time of the alarm in string form
     *  hour:minutes am/pm
     */
    public String getStringTime() {
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String min = Integer.toString(minute);
        if (minute < 10)
            min = "0" + minute;
        String time = nonMilitaryHour + ":" + min + " " + getAmPm();
        return time;
    }

    /**
     * Overrides the compareTo() method so that alarms can be sorted by time of day from earliest to
     * latest.
     */
    @Override
    public int compareTo(Reminder reminder) {
        if (hour < reminder.getHour())
            return -1;
        else if (hour > reminder.getHour())
            return 1;
        else {
            if (minute < reminder.getMinute())
                return -1;
            else if (minute > reminder.getMinute())
                return 1;
            else
                return 0;
        }
    }

}
