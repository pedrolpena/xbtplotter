package xbtplotter;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import java.text.DecimalFormat;

public class DistanceCalculator {
    private static final double R = 6371;
    private Position position0;
    private Position position1;

    public DistanceCalculator(double lat0, double lon0,
            double lat1, double lon1) {
        position0 = new Position(lat0, lon0);
        position1 = new Position(lat1, lon1);
    }

    public Position getPosition0() {return position0;}
    public Position getPosition1() {return position1;}
    public void setPosition0(Position p) {position0 = p;}
    public void setPosition1(Position p) {position1 = p;}

    public double getGreatCircleDistance() {
        double latStart = getPosition0().getLatitudeInRadians();
        double lonStart = getPosition0().getLongitudeInRadians();
        double latEnd = getPosition1().getLatitudeInRadians();
        double lonEnd = getPosition1().getLongitudeInRadians();
        double dLat = latEnd - latStart;
        double dLon = lonEnd - lonStart;
        double a = pow(sin(dLat / 2), 2) + cos(latStart) * cos(latEnd) *
                pow(sin(dLon / 2), 2);
        return R * 2 * atan2(sqrt(a), sqrt(1 - a));
    }//end getGreatCircleDistance

    public double getBearing() {
        double latStart = getPosition0().getLatitudeInRadians();
        double lonStart = getPosition0().getLongitudeInRadians();
        double latEnd = getPosition1().getLatitudeInRadians();
        double lonEnd = getPosition1().getLongitudeInRadians();
        double dLon = lonEnd - lonStart;
        double bearing = (180 / PI) * atan2(sin(dLon) * cos(latEnd),
                cos(latStart) * sin(latEnd) - sin(latStart) *
                        cos(latEnd) * cos(dLon));
        if (bearing < 0) {
            bearing += 360;
        }//end if
        return bearing;
    }//end getBearing

    public Position getNextPosition(Position p, double distance,
            double bearing) {
        double lat = p.getLatitudeInRadians();
        double lon = p.getLongitudeInRadians();
        bearing = bearing * PI / 180;
        Position p0 = new Position();
        p0.setLatitude((180 / PI) * asin(sin(lat) * cos(distance / R) +
                cos(lat) * sin(distance / R) * cos(bearing)));
        p0.setLongitude((180 / PI) * (lon + atan2(sin(bearing) *
                sin(distance / R) * cos(lat), cos(distance / R) - sin(lat) *
                        sin(p0.getLatitudeInRadians()))));
        return p0;
    }//end getNextPosition  
}

class Position {
    private double latitude = 0.0;
    private double longitude = 0.0;

    public Position(){}
    public Position(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }//end getLatitude

    public double getLongitude() {
        return longitude;
    }//end getLatitude

    public String getLatitudeDegrees() {
        DecimalFormat df = new DecimalFormat("00");
        return df.format((int) abs(latitude));
    }

    public String getLatitudeMinutes() {
        DecimalFormat df = new DecimalFormat("00.000");
        return df.format( abs(60*(latitude-(int)latitude)));
    }

    public String getLongitudeDegrees() {
        DecimalFormat df = new DecimalFormat("000");
        return df.format((int) abs(longitude));
    }

    public String getLongitudeMiuntes() {
        DecimalFormat df = new DecimalFormat("00.000");
        return df.format( abs(60*(longitude-(int)longitude)));
    }

    public double getLatitudeInRadians() {
        return toRadians(latitude);
    }//end getLatitude

    public double getLongitudeInRadians() {
        return toRadians(longitude);
    }//end getLatitude

    public void setLatitude(double l) {
        latitude = l;
    }//end getLatitude

    public void setLongitude(double l) {
        longitude = l;
    }//end getLatitude

    public void setPosition(Position p){
        latitude=p.getLatitude();
        longitude=p.getLongitude();
    }//end setPosition

    String getLatitudeDirection()
    {
        String direction = "N";
        if(latitude < 0){
            direction = "S";
        }
        return direction;
    }

    String getLongitudeDirection()
    {
        String direction = "E";
        if(longitude < 0){
            direction = "W";
        }
        return direction;
    }

    public String toString(){
        String x;
        x=getLatitudeDegrees() + " " + getLatitudeMinutes() + " " +
                getLatitudeDirection() + " " + getLongitudeDegrees() + " " +
                getLongitudeMiuntes() + " " + getLongitudeDirection();
        return x;
    }//end toString
}