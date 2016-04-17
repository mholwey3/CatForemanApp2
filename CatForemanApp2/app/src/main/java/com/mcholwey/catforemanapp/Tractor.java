package com.mcholwey.catforemanapp;

import com.google.android.gms.maps.model.Marker;


/**
 * Created by Michael, Dakota, Kryzys, and Nathan on 3/8/2016.
 */
public class Tractor {
    private String serialNumber;
    private String name;
    private String model;
    private TractorStateEnum currentState = TractorStateEnum.UNKNOWN;
    private boolean isActive;
    private double longitude;
    private double latitude;
    private Float speed;
    private Marker marker;
    private Statistic stat;
    private String loadStats;

    private String dumpStats;

    public enum TractorStateEnum{
        UNKNOWN,
        MOVING,
        STOPPED,
        LOADING,
        MOVING_DUMP,
        STATIC_DUMP
    }

    public static String[] stateStrings = {
            "Unknown",
            "Moving",
            "Stopped",
            "Loading",
            "Moving Dump",
            "Static Dump"};

    public Tractor(){
        stat = new Statistic();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TractorStateEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(TractorStateEnum currentState) {
        this.currentState = currentState;
    }

    public String getCurrentStateString(TractorStateEnum currentState){
        if(currentState == TractorStateEnum.MOVING){
            return "Moving";
        }
        else if(currentState == TractorStateEnum.STOPPED){
            return "Stopped";
        }
        else if(currentState == TractorStateEnum.LOADING){
            return "Loading";
        }
        else if(currentState == TractorStateEnum.STATIC_DUMP){
            return "Static Dump";
        }
        else if(currentState == TractorStateEnum.MOVING_DUMP){
            return "Moving Dump";
        }
        else{
            return "Unknown";
        }
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getLoadStats() {
        return loadStats;
    }

    public void setLoadStats(String loadStats) {
        this.loadStats = loadStats;
    }

    public String getDumpStats() {
        return dumpStats;
    }

    public void setDumpStats(String dumpStats) {
        this.dumpStats = dumpStats;
    }

    public String getDetails(){
        StringBuilder builder = new StringBuilder();

        builder.append("Serial Number: " + getSerialNumber());
        builder.append("\n\nName: " + getName());
        builder.append("\n\nModel: " + getModel());
        builder.append("\n\nState: " + (getCurrentState()!=null?stateStrings[getCurrentState().ordinal()]:"null"));
        builder.append("\n\nSpeed: " + getSpeed());
        builder.append("\n\nLatitude: " + getLatitude());
        builder.append("\n\nLongitude: " + getLongitude());
        builder.append("\n\n" + stat.getStatisticsDetails());

        return builder.toString();
    }
}
