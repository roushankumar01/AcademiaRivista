package com.collegeproject.model.staticModel;

public class MarkerData {

    private double latitude;
    private double longitude;
    private String palce;

    public MarkerData(double latitude, double longitude, String palce) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.palce = palce;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPalce() {
        return palce;
    }

    public void setPalce(String palce) {
        this.palce = palce;
    }
}
