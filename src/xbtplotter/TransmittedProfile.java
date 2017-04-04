package xbtplotter;

import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import binfileutils.XBTProfile;
import binfileutils.DepthCalculator;

public class TransmittedProfile implements Comparable<TransmittedProfile> {

    private String fileName;
    private double maxDepth;
    private double maxTemperature;
    private double minTemperature;
    private double nextPOS;
    private Date observationDateTime;

    private double previousPOS;
    private XBTProfile profile;
    private Date recoveryDateTime;
    private int recoveryDay;
    private int recoveryHour;
    private int recoveryMinute;
    private int recoveryMonth;
    private int recoveryYear;
    private Vector temperaturePoints;
    private int xLocation;
    private int yLocation;

    /* valid profile resolutions */
    private static final int FULLRESOLUTION = 1;
    private static final int TWOMETERS = 2;
    private static final int INFLECTIONPOINTS = 3;

    /* data & meta-data precision values */
    private static final int TWODEC = 2;

    public TransmittedProfile(XBTProfile binProfile) {
        profile = binProfile;
        maxDepth = 0;
        maxTemperature = -10;
        minTemperature = 99;
        previousPOS = 0;
        nextPOS = 0;
    }

    @Override
    public int compareTo(TransmittedProfile t) {
        return getObservationDateTime().compareTo(t.getObservationDateTime());
    }

    public void setFileNameAndRecoveryDate(String fn) {
        fileName = fn;
        String[] tokens = fn.split("_");
        int underScoreCount = tokens.length;
        String receivedTime = tokens[1];
        if(underScoreCount < 3) {
            setRecoveryYear(0000);
            setRecoveryMonth(0);
            setRecoveryDay(0);
            setRecoveryHour(0);
            setRecoveryMinute(0);
            setRecoveryDateTime();
        } else {
            if(receivedTime.indexOf(".")>=0) {
                receivedTime = tokens[1].replace(".", "") +
                        "-" + tokens[2].replace(".", "");
            }
            constructDateFromFileName(receivedTime);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getNextPOS() {
        return nextPOS;
    }

    public void setNextPOS(double nextPOS) {
        this.nextPOS = nextPOS;
    }

    public Date getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(Date observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    public void setObservationDateTime() {
       observationDateTime =
               profileDateTimeFormat(profile.getYear(),
               profile.getMonth(), profile.getDay(),
               profile.getHour(), profile.getMinute());
    }

    public double getPreviousPOS() {
        return previousPOS;
    }

    public void setPreviousPOS(double previousPOS) {
        this.previousPOS = previousPOS;
    }

    public XBTProfile getProfile() {
        return profile;
    }

    public void setProfile(XBTProfile profile) {
        this.profile = profile;
    }

    public Date getRecoveryDateTime() {
        return recoveryDateTime;
    }

    public void setRecoveryDateTime(Date recoveryDateTime) {
        this.recoveryDateTime = recoveryDateTime;
    }
    
    public void setRecoveryDateTime() {
        recoveryDateTime =
                profileDateTimeFormat(getRecoveryYear(),
                        getRecoveryMonth(), getRecoveryDay(),
                        getRecoveryHour(), getRecoveryMinute());
    }
    
    public int getRecoveryDay() {
        return recoveryDay;
    }

    public void setRecoveryDay(int recoveryDay) {
        this.recoveryDay = recoveryDay;
    }

    public int getRecoveryHour() {
        return recoveryHour;
    }

    public void setRecoveryHour(int recoveryHour) {
        this.recoveryHour = recoveryHour;
    }

    public int getRecoveryMinute() {
        return recoveryMinute;
    }

    public void setRecoveryMinute(int recoveryMinute) {
        this.recoveryMinute = recoveryMinute;
    }

    public int getRecoveryMonth() {
        return recoveryMonth;
    }

    public void setRecoveryMonth(int recoveryMonth) {
        this.recoveryMonth = recoveryMonth;
    }

    public int getRecoveryYear() {
        return recoveryYear;
    }

    public void setRecoveryYear(int recoveryYear) {
        this.recoveryYear = recoveryYear;
    }

    public Vector getTemperaturePoints() {
        return temperaturePoints;
    }

    public void setTemperaturePoints(Vector temperaturePoints) {
        this.temperaturePoints = temperaturePoints;
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public void distanceToNextPOS(TransmittedProfile nextPR) {
        DistanceCalculator dc = new DistanceCalculator(profile.getLatitude(),
                profile.getLongitude(), nextPR.getProfile().getLatitude(),
                nextPR.getProfile().getLongitude());
        setNextPOS(dc.getGreatCircleDistance());
    }

    public void distanceToPreviousPOS(TransmittedProfile prevPR) {
        DistanceCalculator dc = new DistanceCalculator(profile.getLatitude(),
                profile.getLongitude(), prevPR.getProfile().getLatitude(),
                prevPR.getProfile().getLongitude());
        setPreviousPOS(dc.getGreatCircleDistance());
    }

    public void constructDateFromFileName(String rt) {
        setRecoveryYear(
                Integer.parseInt(rt.substring(0, 4)));
        setRecoveryMonth(
                Integer.parseInt(rt.substring(4, 6)));
        setRecoveryDay(
                Integer.parseInt(rt.substring(6, 8)));
        setRecoveryHour(
                Integer.parseInt(rt.substring(9, 11)));
        setRecoveryMinute(
                Integer.parseInt(rt.substring(11)));
        setRecoveryDateTime();
    }

    public Date profileDateTimeFormat(int yr, int mon, int d, int hr, int min) {
        SimpleDateFormat fileDateFormat =
                new SimpleDateFormat("yyyyMMddHHmm");
        String stringDate =
                String.format("%04d", yr) +
                String.format("%02d", mon) +
                String.format("%02d", d) +
                String.format("%02d", hr) +
                String.format("%02d", min);
        try {
            return fileDateFormat.parse(stringDate);
        } catch (ParseException ex) {
            return null;
        }
    }

    public String profileDateTimeFormatted(Date pdt, String timezone) {
        SimpleDateFormat odtf = 
            new SimpleDateFormat ("yyyy-MM-dd HH:mm ");
        return odtf.format(pdt) + timezone;
    }

    public boolean temperatureObservations() {
        int counter = 0;
        profile.getTemperaturePoints();
        DepthCalculator dc = new DepthCalculator(profile);
        double[] depths = dc.getMeasurementDepths();
        
        for (int i = 0; i < profile.getTemperaturePoints().length; i ++) {
            double tm = profile.getTemperaturePoints()[i];
            double d = 0;
            if(profile.getThisDataIs() == FULLRESOLUTION) {
                d = depths[i];
            } else if(profile.getThisDataIs() == TWOMETERS) {
                d = 2 * counter;
            } else if(profile.getThisDataIs() == INFLECTIONPOINTS) {
                System.out.println("Profile using inflection points: " +
                        getFileName());
                return false;
            } else {
                System.out.println("Profile resolution type " +
                        profile.getThisDataIs() + " not found: "
                        + getFileName());
                return false;
            }
            
            if (d > getMaxDepth()) {
                setMaxDepth(d);
            }
            if (tm > getMaxTemperature()) {
                setMaxTemperature(tm);
            }
            if (tm < getMinTemperature()) {
                setMinTemperature(tm);
            }

            DTPair pt = new DTPair(d, tm);
            getTemperaturePoints().addElement(pt);
            counter = counter + 1;
        }
        return true;
    }

    public static class Comparators {
        public static Comparator<TransmittedProfile> MAXDEPTH =
                new Comparator<TransmittedProfile>() {
            @Override
            public int compare(TransmittedProfile pr1, TransmittedProfile pr2) {
                if(pr1.getMaxDepth() < pr2.getMaxDepth()) return -1;
                if(pr1.getMaxDepth() > pr2.getMaxDepth()) return 1;
                return 0;
            }
        };

        public static Comparator<TransmittedProfile> MAXTEMPERATURE =
                new Comparator<TransmittedProfile>() {
            @Override
            public int compare(TransmittedProfile pr1, TransmittedProfile pr2) {
                if(pr1.getMaxTemperature() < pr2.getMaxTemperature()) return -1;
                if(pr1.getMaxTemperature() > pr2.getMaxTemperature()) return 1;
                return 0;
            }
        };

        public static Comparator<TransmittedProfile> MINTEMPERATURE =
                new Comparator<TransmittedProfile>() {
            @Override
            public int compare(TransmittedProfile pr1, TransmittedProfile pr2) {
                if(pr1.getMinTemperature() < pr2.getMinTemperature()) return -1;
                if(pr1.getMinTemperature() > pr2.getMinTemperature()) return 1;
                return 0;
            }
        };
    }

}