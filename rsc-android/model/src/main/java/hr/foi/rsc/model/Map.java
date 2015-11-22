package hr.foi.rsc.model;

/**
 * Created by tomo on 22.11.15..
 */
public class Map {

    long idMap;
    String name;
    double startLat;
    double endLat;
    double startLng;
    double endLng;
    double flagLat;
    double flagLng;

    public long getIdMap() {
        return idMap;
    }

    public void setIdMap(long idMap) {
        this.idMap = idMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getFlagLat() {
        return flagLat;
    }

    public void setFlagLat(double flagLat) {
        this.flagLat = flagLat;
    }

    public double getFlagLng() {
        return flagLng;
    }

    public void setFlagLng(double flagLng) {
        this.flagLng = flagLng;
    }
}
