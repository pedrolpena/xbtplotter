package xbtplotter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;

public class Profile {

	public static final double NO_POSITION = 0;
	public static final int NO_TIME = 0;
	public static final int NO_DATE = 0;
	
	/**
	 * Longitude of deployment.
	 */
	private double longitude;
	
	/**
	 * Latitude of deployment.
	 */
	private double latitude;
	
	/**
	 * Date and time of deployment. GMT should be used.
	 */
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
	/**
	 * IXDB unique identifier 128-bits.
	 */
	private byte[] id;
	
	/**
	 * String used to produced the 128-bit
	 * MD5 id.
	 */
	private String stringId;
	
	/**
	 * This is the default constructor, and it is important because
	 * profile is the super-class of at least one type of profiles
	 */
	public Profile()
	{
		longitude = NO_POSITION;
		latitude = NO_POSITION;
		year = month = day = NO_DATE;
		hour = minute = NO_TIME;
		generateId();
	}
	
		
	/**
	 * This constructor takes as parameters the position and date
	 * of deployment of the instrument that produced the profile
	 */
	public Profile(double lon,double lat,int year,int month,int day) {
		// set position of deployment
		this.longitude = lon;
		this.latitude = lat;
				
		// set date & time of deployment
		this.year = year; this.month = month; this.day = day;
		this.hour = this.minute = NO_TIME;
		
		// generate id, not time available
		generateId();
	}
	
	/**
	 * This constructor takes as parameters the position, date, and time
	 * of deployment of the instrument that produced the profile
	 */
	public Profile(double lon,double lat,int year,int month,int day,int hour,int minutes) {
		// set position of deployment
		this.longitude = lon;
		this.latitude = lat;
		
		// set date & time of deployment
		this.year = year; this.month = month; this.day = day;
		this.hour = hour; this.minute = minutes;
		
		// generate id
		generateId();
	}
	
	/**
	 * Returns the longitude at which the producing instrument was deployed;
	 * as now we work with longitudes of up to 3 decimal positions, but float
	 * should support up to seven decimal positions.
	 * @return longitude of deployment
	 */
	public double longitude()
	{
		return longitude;
	}
	
	/**
	 * Returns the latitude at which the producing instrument was deployed;
	 * as now we work with latitudes of up to 3 decimal positions, but float
	 * should support up to seven decimal positions.
	 * @return latitude of deployment
	 */
	public double latitude()
	{
		return latitude;
	}
	
	/**
	 * Returns the date of deployment using the following format: MM/DD/YYYY.
	 * @return string containing the date of deployment in the format: MM/DD/YYYY
	 */
	public String getDate()
	{
		return String.format("%02d/%02d/%d", month, day, year);
	}
	
	/**
	 * Returns the time of deployment using the following format: HH:MM.
	 * @return string containing the time of deployment in the format: HH:MM
	 */
	public String getTime()
	{
		return String.format("%02d:%02d", hour, minute);
	}
	
	/**
	 * @return the time of deployment in milliseconds since January 1, 1970, 00:00:00 GMT
	 */
	public long getTimeMillis() {
		// create date object to model date of deployment
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0); // seconds are not supported
		calendar.set(Calendar.MILLISECOND, 0); // milliseconds are not supported
		
		return calendar.getTimeInMillis();
	}
	
	/**
	 * Returns the year of deployment.
	 * @return the year of deployment
	 */
	public int year()
	{
		return year;
	}
	
	/**
	 * Returns the day of deployment.
	 * @return the day of deployment
	 */
	public int day()
	{
		return day;
	}
	
	/**
	 * Returns the month of deployment.
	 * @return the month of deployment
	 */
	public int month()
	{
		return month;
	}
	
	/**
	 * Returns the hour of deployment.
	 * @return the hour of deployment
	 */
	public int hour()
	{
		return hour;
	}
	
	/**
	 * Returns the minutes of deployment.
	 * @return the minutes of deployment
	 */
	public int minutes()
	{
		return minute;
	}
	
	/**
	 * @return the IXDB 128-bits profile id
	 */
	public byte[] id()
	{
		return id;
	}
	
	/**
	 * @return the string used to generate the 128-bit profile id
	 */
	public String stringId() {
		return stringId;
	}
	
	/**
	 * Sets the position of deployment.
	 * @param lon the longitude of deployment
	 * @param lat the latitude of deployment
	 */
	public void setPosition(double lon,double lat) {
		// set position
		this.longitude = lon;
		this.latitude = lat;
		// update id
		generateId();
	}
		
	/**
	 * Sets the time of deployment.
	 * @param hours the hour of deployment
	 * @param minutes the minutes of deployment
	 */
	public void setTime(int hours,int minutes) {
		// set time
		this.hour = hours;
		this.minute = minutes;
		// update id
		generateId();
	}
	
	/**
	 * Sets the date of deployment.
	 * @param year the year of deployment
	 * @param month the month of deployment
	 * @param day the day of deployment
	 */
	public void setDate(int year,int month,int day)	{
		// set date of deployment
		this.year = year;
		this.month = month;
		this.day = day;
		// update id
		generateId();
	}
	
	/**
	 * Resets the date of deployment.
	 */
	public void resetDate()	{
		// reset date & time
		year = month = day = NO_DATE;
		hour = minute = NO_TIME;
		// update id
		generateId();
	}
	
	/**
	 * Uses MD5 to generate an id for the given deployment information. The data stream to use is the 
	 * US-ASCII byte representation of the string "YYYY-MM-DDhh:mmLONLAT".
	 * @param YEAR the year of deployment
	 * @param MONTH the month of deployment
	 * @param DAY the day of deployment
	 * @param HOUR the hour of deployment
	 * @param MINUTE the minute of deployment
	 * @param LON the longitude of deployment
	 * @param LAT the latitude of deployment
	 */
	public static byte[] generateId(int YEAR, int MONTH, int DAY, int HOUR, int MINUTE, double LON, double LAT)
	{	
		// create a "key" profile & return id		
		return (new Profile(LON, LAT, YEAR, MONTH, DAY, HOUR, MINUTE).id());
	}
	
	
	/**
	 * Uses MD5 to generate an id for the this profile based on the deployment information. 
	 * It first generates as US-ASCII idString, and then used the idString
	 * byte representation to generate the id. The idString is of the the form "YYYY-MM-DDhh:mmLONLAT".
	 */
	private void generateId() {
		// convert position to one-digit coarse resolution
		int scale = 1; // one decimal place
		BigDecimal lon = BigDecimal.valueOf(longitude).setScale(scale, RoundingMode.HALF_UP); 
		BigDecimal lat = BigDecimal.valueOf(latitude).setScale(scale, RoundingMode.HALF_UP);
				
		// forge id string
		stringId = String.format("%d-%02d-%02d%02d:%02d%s%s", year(), month(), 
				day(), hour(), minutes(), lon, lat);
		
		try {
			// get data stream & generate MD5 of data stream
			byte[] dataStream = stringId.getBytes("US-ASCII");
			MessageDigest md = MessageDigest.getInstance("MD5");
			id = md.digest(dataStream);
		} catch (Exception shouldnothappen) { throw new Error();} // this should not happen
	}
		
	/**
	 * Returns true if the given object is a Profile, and if has the same date, time, and
	 * position of deployment. There is a tolerance for the position of deployment of 500m.
	 * @return true if the profile has the same date, time, and position of deployment
	 */
	@Override
	public boolean equals(Object o)
	{
		// if this is the same object return true
		if( this == o)
			return true;
		
		// if the given object is null return false
		if(o == null)
			return false;
		
		// if o is not a Profile return false		
		if(!(o instanceof Profile))
			return false;
		
		// cast o to an object of type Profile
		Profile p = (Profile) o;
		
		// test for id
		return Arrays.equals(this.id(), p.id());
	}
	
	/**
	 * Calculates the hashcode of this profile according to the specifications
	 * of the Java class Object equals() method
	 */
	@Override
	public int hashCode()
	{		
		return Arrays.hashCode(this.id());
	}
}
