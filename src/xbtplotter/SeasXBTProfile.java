package xbtplotter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class SeasXBTProfile extends Profile implements TemperatureProfile {

	private String transect;
	private String cruise;
	private long seasId;
	private String seasVersion;
	private String callSign;
	private String shipName;
	private int lloydNumber;
	private int resolution;
	private double launcherHeight;
	private double sst;
	
	/**
	 * This is an ordered list holding the profile's data;
	 * the order is done based on the meassurement's depth,
	 * shallower to deeper.
	 */
	protected List<TemperatureMeasurement> data;
	
	/**
	 * Instrument information according to WMO Code table 1770.
	 */
	private XBT xbt;
	
	/**
	 * Recorder information according to WMO Code table 4770.
	 */
	private int recorderType;
	private String recorderField;
	
	/**
	 * Source file information.
	 */
	private String sourceName;
	private byte[] source;
	
	/**
	 * Valid profile resolutions.
	 */
	public static final int FULLRESOLUTION = 1;
	public static final int TWOMETERS = 2;
	public static final int INFLECTIONPOINTS = 3;
	
	/**
	 * Depth of the bottom of the ocean at the position of deployment
	 * given in meters.
	 */
	private double oceanDepth;
	private int cast;
	
	/**
	 * Wind information at the time of deployment.
	 */
	private int windInstrumentType;
	private int windDir; // true direction WMO Code Table 0878
	private double windSpeed; // in m/s
	
	/**
	 * Current information at the time of deployment.
	 */
	private int currentMeasurementMethod;
	private int currentDir; // degrees true WMO Code Table 0877
	private double currentSpeed; // in m/s
	
	/**
	 * Ship speed and direction at time of deployment.
	 */
	private int shipDir; // degrees true WMO Code Table 0877
	private double shipSpeed; // in m/s
	
	/**
	 * Temperature of the bulb before deployment in K.
	 */
	private double dryBulbTemperature;
	
	/**
	 * Numeric value not available.
	 */
	public static final int NaN = -1;
	
	/**
	 * String value not available
	 */
	public static final String NaS = "NaS";
	
	/**
	 * Initializes the local state to a non representative state.
	 */
	private void initializeStatus()
	{
		// initialize local status
		setTransect(NaS);
		setCruise(NaS);
		setSEASId(NaN);
		setSEASVersion(NaS);
		setCallSign(NaS);
		setShipName(NaS);
		setIMO(NaN);
		setOceanDepth(NaN);
		setCast(NaN);
		
		// create an empty list as data
		data = new ArrayList<TemperatureMeasurement>();
		
		// recorder type not available
		setRecorderType(NaN);
		
		// initialize deployment conditions
		setWindInstrumentType(NaN);
		setWindDirection(NaN);
		setWindSpeed(NaN);
		setDryBulbTemperature(NaN);
		setCurrentMeasurementMethod(NaN);
		setCurrentDirection(NaN);
		setCurrentSpeed(NaN);
		setShipDirection(NaN);
		setShipSpeed(NaN);
		
		// initialize source status
		sourceName = null;
		source = null;
	}
	
	/**
	 * Creates an empty profile.
	 */
	public SeasXBTProfile() 
	{
		// construct super
		super();
		// initialize local status
		initializeStatus();
	}

	/**
	 * Creates a profile using the the given date and position of deployment.
	 * @param lon longitude of deployment
	 * @param lat latitude of deployment
	 * @param year year of deployment
	 * @param month month of deployment
	 * @param day day of deployment
	 */
	public SeasXBTProfile(double lon, double lat, int year, int month, int day) 
	{
		// construct super
		super(lon, lat, year, month, day);
		// initialize local status
		initializeStatus();
	}

	/**
	 * Creates a profile using the the given date, time and position of deployment.
	 * @param lon longitude of deployment
	 * @param lat latitude of deployment
	 * @param year year of deployment
	 * @param month month of deployment
	 * @param day day of deployment
	 * @param hour hour of deployment
	 * @param minutes minutes of deployment
	 */
	public SeasXBTProfile(double lon, double lat, int year, int month, int day,
			int hour, int minutes) 
	{
		// construct super
		super(lon, lat, year, month, day, hour, minutes);
		// initialize local status
		initializeStatus();
	}

	/**
	 * @return the transect of deployment according to the SOOP
	 */
	public String getTransect() 
	{
		return transect;
	}

	/**
	 * @param transect the transect of deployment according to the SOOP
	 */
	public void setTransect(String transect) 
	{
		this.transect = transect;
	}

	/**
	 * @return the SEAS id as set by AMVER/SEAS
	 */
	public long getSEASId() 
	{
		return seasId;
	}

	/**
	 * @param seasId the SEAS id as set by AMVER/SEAS
	 */
	public void setSEASId(long seasId) 
	{
		this.seasId = seasId;
	}

	/**
	 * @return the SEAS software version used to record the profile
	 */
	public String getSEASVersion() 
	{
		return seasVersion;
	}

	/**
	 * @param seasVersion the SEAS software version used to record the profile
	 */
	public void setSEASVersion(String seasVersion) 
	{
		this.seasVersion = seasVersion;
	}

	/**
	 * @return the call sign of the ship/platform of deployment
	 */
	public String getCallSign() 
	{
		return callSign;
	}

	/**
	 * @param callSign the call sign of the ship/platform of deployment
	 */
	public void setCallSign(String callSign) 
	{
		this.callSign = callSign;
	}

	/**
	 * @return the name of the ship/platform of deployment
	 */
	public String getShipName() 
	{
		return shipName;
	}

	/**
	 * @param shipName the name of the ship/platform of deployment
	 */
	public void setShipName(String shipName) 
	{
		this.shipName = shipName;
	}

	/**
	 * @return the lloyd's number of the ship of deployment
	 */
	public int getIMO() 
	{
		return lloydNumber;
	}

	/**
	 * @param IMO the lloyd's number of the ship of deployment
	 */
	public void setIMO(int IMO) 
	{
		this.lloydNumber = IMO;
	}

	/**
	 * @return the ocean depth at the position of deployment given in meters
	 */
	public double getOceanDepth() 
	{
		return oceanDepth;
	}

	/**
	 * @param oceanDepth the ocean depth at the position of deployment given in meters
	 */
	public void setOceanDepth(double oceanDepth) 
	{
		this.oceanDepth = oceanDepth;
	}
	
	/**
	 * @return the depth of the deepest data point in the profile
	 */
	public double getMaxDepth() {
		return (data.isEmpty())? 0.0f : data.get(data.size()-1).getDepth();
	}

	/**
	 * @return the sequence of deployment for this profile in a SOOP cruise
	 */
	public int getCast() 
	{
		return cast;
	}

	/**
	 * @param cast the sequence of deployment for this profile in a SOOP cruise
	 */
	public void setCast(int cast) 
	{
		this.cast = cast;
	}
	
	/**
	 * Trims the bottom (tail) of the profile. Removing garbage data that results
	 * from a broken XBT cable.
	 */
	public void trim()
	{
		int i, p;
		double G, v1, v2, v3;
		double tolerance = 0.5;
		
		// init breaking point
		p = data.size(); // no breaking point
		
		// find breaking point
		for(i=1; i<data.size()-1; i++)
		{
			// only remove garbage below 850m
			if(data.get(i).getDepth()<850)
				continue;
			v1 = data.get(i-1).getTemperature();
			v2 = data.get(i).getTemperature();
			v3 = data.get(i+1).getTemperature();
			// calculate spike value
			G = Math.abs(v2 - (v1+v3)/2);
			if(G > tolerance)
			{
				p = i;
				break;
			}
		}
		
		// remove all data from breaking point
		for(i=data.size()-1; i>=p; --i)
		{
			data.remove(i);
		}
	}
	
	/**
	 * Removes all the data points below the given depth from the profile.
	 * @param depth the depth of the cut in meters
	 */
	public void cutProfileAt(double depth) {
		// get list iterator
		ListIterator<TemperatureMeasurement> iterator = data.listIterator(data.size());
		// remove all data points deeper than depth
		while((iterator.hasPrevious())&&(iterator.previous().getDepth()>=depth)) {
			iterator.remove();
		}
	}

	/**
	 * @return the resolution of the profile
	 */
	public int getResolution() {
		return resolution;
	}

	/**
	 * @param resolution the resolution of the profile
	 */
	public void setResolution(int resolution) {
		// validate resolution
		if(resolution!=FULLRESOLUTION && resolution!=TWOMETERS && resolution!=INFLECTIONPOINTS)
			throw new IllegalArgumentException("Invalid resolution");
		
		this.resolution = resolution;
	}

	/**
	 * @return the XBT launcher height above sea surface
	 */
	public double getLauncherHeight() {
		return launcherHeight;
	}

	/**
	 * @param height the XBT launcher height above sea surface
	 */
	public void setLauncherHeight(double height) {
		this.launcherHeight = height;
	}

	/**
	 * @return the sea surface temperature
	 */
	public double getSST() {
		return sst;
	}

	/**
	 * @param temperature the sea surface temperature
	 */
	public void setSST(double temperature) {
		this.sst = temperature;
	}

	/**
	 * @return the name of the source file
	 */
	public String getSourceName() {
		return sourceName;
	}
	
	/**
	 * Returns the size of the source file in bytes.
	 * @return the size of the source file in bytes
	 */
	public long getSourceSize() {
		if(source == null)
			return 0L;
		return source.length;
	}

	/**
	 * @param sourceName the name of the source file
	 */
	public void setSourceName(String sourceName) {
		// validate source
		if(sourceName == null)
			throw new IllegalArgumentException();
		this.sourceName = sourceName;
	}

	/**
	 * @return a copy of the source file content
	 */
	public byte[] getSource() {
		// return a copy
		if(source == null)
			return null;
		
		byte[] sourceCopy = new byte[source.length];
		for(int i=0; i<source.length; ++i)
			sourceCopy[i] = source[i];
		
		return sourceCopy;
	}

	/**
	 * @param the source file content
	 */
	public void setSource(byte[] source) {
		// validate source
		if(source == null)
			throw new IllegalArgumentException();
		
		// create a copy
		byte [] sourceCopy = new byte[source.length];
		for(int i=0; i<source.length; ++i)
			sourceCopy[i] = source[i];
		
		this.source = sourceCopy;
	}
	
	/**
	 * Convenience method for writing this profile's source to the given directory.
	 * If the file already exists it is overwrite.
	 * @param dir the output directory
	 * @throws IOException if there is a problem writing the file
	 * @throws FileNotFoundException if it was not possible to create the output file
	 */
	public void writeSource(File dir) throws IOException, FileNotFoundException {
		// dir can't be null
		if(dir == null)
			throw new IllegalArgumentException();
		
		try {
			File outputFile = new File(dir, this.getSourceName());
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(source);
			outputStream.close();
		} catch (FileNotFoundException e1) {
			throw e1;
		} catch (IOException e2) {
			throw e2;
		}				
	}
	
	
	/**
	 * Writes an XBT simple profile representation to the given file.
	 * @param file the file use to write simple profile representation.
	 * @throws IOException if the file cannot be created
	 */
	public void writeSimpleXBTProfile(File file) throws IOException {
		// file cannot be null
		if(file == null) {
			throw new IllegalArgumentException("Parameter file cannot be null.");
		}
		
		// open file for output
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		
		// output date
		output.write(String.format("Year\t\t:%d\n", this.year()));
		output.write(String.format("Month\t\t:%d\n", this.month()));
		output.write(String.format("Day\t\t\t:%d\n", this.day()));
		
		// output time
		output.write(String.format("Hour\t\t:%d\n", this.hour()));
		output.write(String.format("Minute\t\t:%d\n", this.minutes()));
		
		// output position
		output.write(String.format("Latitude\t:%.3f\n", this.latitude()));
		output.write(String.format("Longitude\t:%.3f\n", this.longitude()));
		
		// output data-meta-data separator
		output.write("==============================");
		
		// output data
		for(TemperatureMeasurement measurement : data) {
			output.write("\n");
			output.write(String.format("%.2f\t%.2f", measurement.getDepth(), measurement.getTemperature()));			
		}
		
		// close output file
		output.close();		
	}
	
	/**
	 * Returns the recorder type used for recording this profile.
	 * @return the recorder type according to WMO Code table 4770
	 */
	public int getRecorderType() 
	{
		return recorderType;
	}
	
	/**
	 * Sets the recorder type used for recording this profile.
	 * @param recorderType the recorder type according to WMO Code table 4770
	 */
	public void setRecorderType(int recorderType) 
	{
		if(recorderType == NaN) {
			this.recorderType = NaN;
			this.recorderField = NaS;
			return;
		}
		
		// initialize code for check
		this.recorderType = NaN;
		
		// set code, and name
		for(WMO4770 recorder : WMO4770.values()) {
			if(recorderType == recorder.code())	{
				this.recorderType = recorder.code();
				this.recorderField = recorder.recorder();
				break;
			}
		}
		
		// check instrument OK
		if(this.recorderType == NaN) {
			throw new IllegalArgumentException("The given recorder type is not valid.");
		}
	}
	
	/**
	 * Sets the instrument used to produced this profile.
	 * @param instrument instrument used to produced this profile
	 */
	public void setInstrument(XBT instrument)
	{
		xbt = instrument;
	}
	
	/**
	 * Returns the instrument that produced this profile.
	 * @return instrument used to produced the profile
	 */
	public XBT getInstrument()
	{
		return xbt;
	}
	
	/**
	 * @return the recorder field according to WMO Code table 4770.
	 */
	public String getRecorderField() 
	{
		return recorderField;
	}
	
	/**
	 * Returns true if the Profile has no TemperatureMeasurements.
	 * @return true if and only if there are no TemperatureMeasurements in the Profile
	 */
	@Override
	public boolean isEmpty() 
	{
		return data.isEmpty();
	}

	/**
	 * Returns the number of TemperatureMeasurements in the Profile.
	 * @return the number of TemperatureMeasurements in the Profile
	 */
	@Override
	public int size() 
	{
		return data.size();
	}

	/**
	 * Adds the given TemperatureMeasurement to the Profile. This operation warranties that
	 * the Profile will have it's TemperatureMeasurements in ascending order after insertion.
	 * @param measurement TemperatureMeasurement to be added to the Profile
	 * @return true if the TemperatureMeasurement was added to the Profile
	 * @throws NullPointerException if measurement is null
	 */
	@Override
	public boolean addTemperatureMeasurement(TemperatureMeasurement measurement) 
			throws NullPointerException	
	{
		// if measurement is null, throw an Exception
		if(measurement == null)
		{
			throw new NullPointerException("A null measurement can not be added to a TemperatureProfile");
		}
		
		// if the measurement is in the profile already, return false
		if(data.contains(measurement))
		{
			return false;
		}
					
		// add the given measurement to the data
		boolean result = data.add(measurement);
		
		// order data in ascending order
		Collections.sort(data);
		
		return result;
	}

	/**
	 * Removes the given TemperatureProfile from the Profile. This operation warranties that
	 * the Profile will have it's TemperatureMeasurements in ascending order after deletion.
	 * @param measurement TemperatureMeasurement to be removed from the Profile
	 * @return true if the TemperatureMeasurement was removed from the Profile
	 * @throws NullPointerException if measurement is null
	 */
	@Override
	public boolean removeTemperatureMeasurement(TemperatureMeasurement measurement) 
			throws NullPointerException
	{
		// if measurement is null, throw Exception
		if(measurement == null)
		{
			throw new NullPointerException("A null measurement can not be added to a TemperatureProfile");
		}
		
		// remove the measurement from data
		return data.remove(measurement);
	}

	
	/**
	 * Returns a read-only list containing all the TemperatureMeasurements in the Profile.
	 * The returned list is in ascending order.
	 * @return read-only list containing all the TemperatureMeasurements in the Profile
	 */
	@Override
	public List<TemperatureMeasurement> getTemperatureMeasurementList() 
	{
		return Collections.unmodifiableList(data);
	}
	
	/**
	 * Returns an string representation of the profile. The representation
	 * is in the normal form.
	 */
	@Override
	public String toString()
	{
		String profile;
		
		// get the platform's new line separator
		String nl = System.getProperty("line.separator");
		
		// print the date
		profile = "Date: " + getDate() + nl;
		
		// print time
		profile += "Time: " + getTime() + nl;
		
		// print the longitude
		profile += "Longitude: " + longitude() + nl;
		
		// print the latitude
		profile += "Latitude: " + latitude() + nl;		
				
		// print the data
		profile += "Data: ";
		for(TemperatureMeasurement measure:data)
		{
			profile += "[" + measure + "]";
		}
		profile += ";" + nl;
		
		return profile;
	}

	
	/**
	 * Returns the code of the instrument used for wind measurement at the time of
	 * deployment. Valid codes are:
	 * 0 - Uncertified Instruments meters/sec
	 * 1 - Certified Instruments meters/sec
	 * 2 - Uncertified Instruments knots
	 * 3 - Certified Instruments knots
	 * 4 - Uncertified Instruments kilometers/hour
	 * 5 - Certified Instruments kilometers/hour
	 * @return the code of the instrument used for wind measurement
	 */
	public int getWindInstrumentType() {
		return windInstrumentType;
	}

	/**
	 * Sets the code of the instrument used for wind measurement at the time
	 * of deployment. Valid codes are:
	 * 0 - Uncertified Instruments meters/sec
	 * 1 - Certified Instruments meters/sec
	 * 2 - Uncertified Instruments knots
	 * 3 - Certified Instruments knots
	 * 4 - Uncertified Instruments kilometers/hour
	 * 5 - Certified Instruments kilometers/hour
	 * @param type the code of the instrument used for wind measurement
	 */
	public void setWindInstrumentType(int type) {
		this.windInstrumentType = type;
	}

	/**
	 * @return the true wind direction WMO Code Table 0878
	 */
	public int getWindDirection() {
		return windDir;
	}

	/**
	 * @param dir the true wind direction WMO Code Table 0878
	 */
	public void setWindDirection(int dir) {
		this.windDir = dir;
	}

	/**
	 * @return the wind speed in m/s
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param speed the wind speed in m/s
	 */
	public void setWindSpeed(double speed) {
		this.windSpeed = speed;
	}

	/**
	 * @return the temperature of the bulb before deployment in K
	 */
	public double getDryBulbTemperature() {
		return dryBulbTemperature;
	}

	/**
	 * @param temperature the temperature of the bulb before deployment in K
	 */
	public void setDryBulbTemperature(double temperature) {
		this.dryBulbTemperature = temperature;
	}

	/**
	 * Returns the code of the method used for current measurement.
	 * Valid codes are:
	 * 0 - Reserved
	 * 1 - ADCP Acoustic Doppler Current Profiler
	 * 2 - GEK Geomagnetic ElectroKinetograph
	 * 3 - Ship's set and drift determined by fixes 3-6 hours apart
	 * 4 - Ship's set and drift determined by fixes > 6 hours but < 12 hours apart
	 * 5 - Drift of buoy
	 * 6 - Reserved
	 * @return the method used for current measurement
	 */
	public int getCurrentMeasurementMethod() {
		return currentMeasurementMethod;
	}

	/**
	 * Sets the code of the method used for current measurement.
	 * Valid codes are:
	 * 0 - Reserved
	 * 1 - ADCP Acoustic Doppler Current Profiler
	 * 2 - GEK Geomagnetic ElectroKinetograph
	 * 3 - Ship's set and drift determined by fixes 3-6 hours apart
	 * 4 - Ship's set and drift determined by fixes > 6 hours but < 12 hours apart
	 * 5 - Drift of buoy
	 * 6 - Reserved
	 * @param method the method used for current measurement
	 */
	public void setCurrentMeasurementMethod(int method) {
		this.currentMeasurementMethod = method;
	}

	/**
	 * @return the current direction at the time of deployment according to WMO Code Table 0877
	 */
	public int getCurrentDirection() {
		return currentDir;
	}

	/**
	 * @param dir the current direction at the time of deployment according to WMO Code Table 0877
	 */
	public void setCurrentDirection(int dir) {
		this.currentDir = dir;
	}

	/**
	 * @return the current speed at the time of deployment in m/s
	 */
	public double getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @param speed the current speed at the time of deployment in m/s
	 */
	public void setCurrentSpeed(double speed) {
		this.currentSpeed = speed;
	}

	/**
	 * @return the ship direction at the time of deployment in degrees true (WMO Code Table 0877)
	 */
	public int getShipDirection() {
		return shipDir;
	}

	/**
	 * @param direction the ship direction at the time of deployment in degrees true (WMO Code Table 0877)
	 */
	public void setShipDirection(int direction) {
		this.shipDir = direction;
	}

	/**
	 * @return the ship speed at the time of deployment in m/s
	 */
	public double getShipSpeed() {
		return shipSpeed;
	}

	/**
	 * @param speed the ship speed at the time of deployment in m/s
	 */
	public void setShipSpeed(double speed) {
		this.shipSpeed = speed;
	}

	/**
	 * @return the cruise name according to the SOOP
	 */
	public String getCruise() {
		return cruise;
	}

	/**
	 * @param cruise the cruise name according to the SOOP
	 */
	public void setCruise(String cruise) {
		this.cruise = cruise;
	}
	
	
}
