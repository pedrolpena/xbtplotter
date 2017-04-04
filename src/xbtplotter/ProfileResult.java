package xbtplotter;

import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileResult implements Comparable<ProfileResult> {
//@SuppressWarnings("deprecation")

    String WMO_ID;
    String Obdate;
    String Obtime;
    int ObYear;
    int ObMonth;
    int ObDay;
    int ObHour;
    int ObMinute;
    double lat;
    double lon;
    String File;
    String RecDate;
    String RecTime;
    int RecYear;
    int RecMonth;
    int RecDay;
    int RecHour;
    int RecMinute;
    int Xloc;
    int Yloc;
    Vector Points;

    public void ProfileResult() {
        WMO_ID = new String();
        Obdate = new String();
        Obtime = new String();
        lat = 0;
        lon = 0;
        RecDate = new String();
        RecTime = new String();
        File = new String();
        Points = new Vector(10, 10);
    }
    
    public Date getObservationDate() {
        Date obverseddate = null;
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String stringDate = String.valueOf(ObYear) +
                String.format("%02d", ObMonth) +
                String.format("%02d", ObDay) +
                String.format("%02d", ObHour) +
                String.format("%02d", ObMinute);
        try {
            obverseddate = fileDateFormat.parse(stringDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        return obverseddate;
    }

    @Override
    public int compareTo(ProfileResult t) {
        return getObservationDate().compareTo(t.getObservationDate());
    }
    


}
