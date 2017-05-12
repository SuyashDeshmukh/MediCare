package edu.csulb.android.medicare.Model;

/**
 * Created by Suyash on 07-May-17.
 */

public class Doctor {

    private String name;

    public Doctor(String name, String address, String title, String speciality) {
        this.name = name;
        this.address = address;
        this.title = title;
        this.speciality = speciality;
    }

    private String address;
    private String title;
    private String speciality;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
