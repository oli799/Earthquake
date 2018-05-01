package com.example.reideroliver.earthquake;

public class Quake {

    private String place;
    private String alert;
    private double longitude;
    private double latitude;
    private double depth;

    public Quake(String place, String alert, double longitude, double latitude, double depth) {
        this.place = place;
        this.alert = alert;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depth = depth;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
