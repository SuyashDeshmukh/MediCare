package edu.csulb.android.medicare.Model;

/**
 * Created by Samruddhi on 4/16/2017.
 */

public class Pharmacy {
    private String pharmacyName;
    private String address;
    private String image;

    public Pharmacy(String pharmacyName){
        this.pharmacyName = pharmacyName;

    }

    public Pharmacy(String pharmacyName, String address, String image) {
        this.pharmacyName = pharmacyName;
        this.address = address;
        this.image = image;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
