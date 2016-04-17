package com.mcholwey.catforemanapp;

/**
 * Created by Michael Holwey on 4/17/2016.
 */
public class Statistic {

    private int numLoads; //The number of loads a unit has had
    private float distanceToLoad; //Total distance driven to arrive at a loading state
    private float avgSpeedLoadToDump; //Average speed when driving from a load site to a dump site
    private float timeFromLoadToDump; //Time taken to go from load to dump site

    private int numDumps; //The number of dumps a unit has had
    private float distanceToDump; //Total distance driven to arrive at a dumping state
    private float avgSpeedDumpToLoad; //Average speed when driving from a dump site to a load site
    private float timeFromDumpToLoad; //Time taken to go from dump to load site

    public int getNumLoads() {
        return numLoads;
    }

    public void setNumLoads(int numLoads) {
        this.numLoads = numLoads;
    }

    public float getDistanceToLoad() {
        return distanceToLoad;
    }

    public void setDistanceToLoad(float distanceToLoad) {
        this.distanceToLoad = distanceToLoad;
    }

    public float getAvgSpeedLoadToDump() {
        return avgSpeedLoadToDump;
    }

    public void setAvgSpeedLoadToDump(float avgSpeedLoadToDump) {
        this.avgSpeedLoadToDump = avgSpeedLoadToDump;
    }

    public float getTimeFromLoadToDump() {
        return timeFromLoadToDump;
    }

    public void setTimeFromLoadToDump(float timeFromLoadToDump) {
        this.timeFromLoadToDump = timeFromLoadToDump;
    }

    public int getNumDumps() {
        return numDumps;
    }

    public void setNumDumps(int numDumps) {
        this.numDumps = numDumps;
    }

    public float getDistanceToDump() {
        return distanceToDump;
    }

    public void setDistanceToDump(float distanceToDump) {
        this.distanceToDump = distanceToDump;
    }

    public float getAvgSpeedDumpToLoad() {
        return avgSpeedDumpToLoad;
    }

    public void setAvgSpeedDumpToLoad(float avgSpeedDumpToLoad) {
        this.avgSpeedDumpToLoad = avgSpeedDumpToLoad;
    }

    public float getTimeFromDumpToLoad() {
        return timeFromDumpToLoad;
    }

    public void setTimeFromDumpToLoad(float timeFromDumpToLoad) {
        this.timeFromDumpToLoad = timeFromDumpToLoad;
    }

    public String getStatisticsDetails(){
        StringBuilder builder = new StringBuilder();

        builder.append("Number of Loads: " + getNumLoads());
        builder.append("\n\nDistance Driven To Load: " + getDistanceToLoad());
        builder.append("\n\nAverage Speed From Load To Dump: " + getAvgSpeedLoadToDump());
        builder.append("\n\nTime Taken Driving From Load To Dump: " + getTimeFromLoadToDump());

        builder.append("\n\nNumber of Dumps: " + getNumDumps());
        builder.append("\n\nDistance Driven To Dump: " + getDistanceToDump());
        builder.append("\n\nAverage Speed From Dump To Load: " + getAvgSpeedDumpToLoad());
        builder.append("\n\nTime Taken Driving From Dump To Load: " + getTimeFromLoadToDump());

        return builder.toString();
    }
}
