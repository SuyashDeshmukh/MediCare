package edu.csulb.android.medicare;

/**
 * Created by Samruddhi on 4/1/2017.
 */

public class TimeQuantity {

    private String Time;
    private String Quantity;

    public TimeQuantity(String time, String quantity) {
        Time = time;
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }


}
