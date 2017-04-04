package xbtplotter;

public class DTPair {

    private double depthMeasurement;
    private double temperatureMeasurement;

    public DTPair() {}

    public DTPair(double dm, double tm) {
        depthMeasurement = dm;
        temperatureMeasurement = tm;
    }

    public double getDepthMeasurement() {
        return depthMeasurement;
    }

    public void setDepthMeasurement(double dm) {
        depthMeasurement = dm;
    }

    public double getTemperatureMeasurement() {
        return temperatureMeasurement;
    }

    public void setTemperatureMeasurement(double tm) {
        temperatureMeasurement = tm;
    }
}

