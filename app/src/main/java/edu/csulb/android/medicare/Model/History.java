package edu.csulb.android.medicare.Model;

/**
 * Created by Samruddhi on 4/29/2017.
 */

public class History {
    private String medicineName;
    private int hourTaken;
    private int minuteTaken;
    private String dateTaken;

    public History(){}

    public History(String medicineName, int hourTaken, int minuteTaken, String dateTaken) {
        this.medicineName = medicineName;
        this.hourTaken = hourTaken;
        this.minuteTaken = minuteTaken;
        this.dateTaken = dateTaken;
    }


    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getHourTaken() {
        return hourTaken;
    }

    public void setHourTaken(int hourTaken) {
        this.hourTaken = hourTaken;
    }

    public int getMinuteTaken() {
        return minuteTaken;
    }

    public void setMinuteTaken(int minuteTaken) {
        this.minuteTaken = minuteTaken;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getAmPm() { return (hourTaken < 12) ? "am" : "pm"; }

    public String getStringTime() {
        int nonMilitaryHour = hourTaken % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String min = Integer.toString(minuteTaken);
        if (minuteTaken < 10)
            min = "0" + minuteTaken;
        String time = nonMilitaryHour + ":" + min + " " + getAmPm();
        return time;
    }

}
