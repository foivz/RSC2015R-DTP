package hr.foi.rsc.model;

/**
 * Created by hrvoje on 21/11/15.
 */
public class Position {
    double lat;
    double lng;

    public Position(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
