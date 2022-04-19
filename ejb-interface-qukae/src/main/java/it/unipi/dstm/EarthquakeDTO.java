package it.unipi.dstm;







import java.io.Serializable;

public class EarthquakeDTO implements Serializable {

    private double magnitude;
    private double latitude;
    private  double longitude;
    private double depth;
    //private DateTime date;

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
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

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    /*public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }*/

    @Override
    public String toString() {
        return "EarthquakeDTO{" +
                "magnitude=" + magnitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", depth=" + depth +

                '}';
    }
}
