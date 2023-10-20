package com.example.mongonav.Models;

public class History {

    private String location;
    private String destination;
    private String distance;
    private String time;


    public History(){

    }

    public History(String location, String destination, String distance, String time) {
        this.location = location;
        this.destination = destination;
        this.distance = distance;
        this.time = time;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
