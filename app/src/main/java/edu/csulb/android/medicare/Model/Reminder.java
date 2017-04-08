package edu.csulb.android.medicare.Model;

/**
 * Created by Samruddhi on 4/3/2017.
 */

/*
    Reminder Model class to store reminder details for particular medicine.
 */
public class Reminder {
    private String repeatTime;
    private String daysOfWeek;
    private int numberOfDays;
    private int hour;
    private int minute;
    private String startDate;

    public Reminder(){

    }

    public Reminder(String repeatTime, String daysOfWeek, int numberOfDays, int hour, int minute, String startDate) {
        this.repeatTime = repeatTime;
        this.daysOfWeek = daysOfWeek;
        this.numberOfDays = numberOfDays;
        this.hour = hour;
        this.minute = minute;
        this.startDate = startDate;
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
