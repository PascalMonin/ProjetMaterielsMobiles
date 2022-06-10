package epf.m1.min2.ProjetMaterielsMobiles.api;

import java.util.Date;

public class RetroModel {

    private long station_id;
    private String name;
    private double lat,lon;
    private int capacity;
    private String stationCode;
    private boolean is_installed;
    private boolean is_renting;
    private boolean is_returning;
    private Date last_reported;
    private int numBikesAvailable;
    private int numDocksAvailable;
    private int num_bikes_available;
    private String num_bikes_available_types;

    public boolean isIs_installed() {
        return is_installed;
    }

    public void setIs_installed(boolean is_installed) {
        this.is_installed = is_installed;
    }

    public boolean isIs_renting() {
        return is_renting;
    }

    public void setIs_renting(boolean is_renting) {
        this.is_renting = is_renting;
    }

    public boolean isIs_returning() {
        return is_returning;
    }

    public void setIs_returning(boolean is_returning) {
        this.is_returning = is_returning;
    }

    public Date getLast_reported() {
        return last_reported;
    }

    public void setLast_reported(Date last_reported) {
        this.last_reported = last_reported;
    }

    public int getNumBikesAvailable() {
        return numBikesAvailable;
    }

    public void setNumBikesAvailable(int numBikesAvailable) {
        this.numBikesAvailable = numBikesAvailable;
    }

    public int getNumDocksAvailable() {
        return numDocksAvailable;
    }

    public void setNumDocksAvailable(int numDocksAvailable) {
        this.numDocksAvailable = numDocksAvailable;
    }

    public int getNum_bikes_available() {
        return num_bikes_available;
    }

    public void setNum_bikes_available(int num_bikes_available) {
        this.num_bikes_available = num_bikes_available;
    }

    public String getNum_bikes_available_types() {
        return num_bikes_available_types;
    }

    public void setNum_bikes_available_types(String num_bikes_available_types) {
        this.num_bikes_available_types = num_bikes_available_types;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getStation_id() {
        return station_id;
    }

    public void setStation_id(long station_id) {
        this.station_id = station_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
}
