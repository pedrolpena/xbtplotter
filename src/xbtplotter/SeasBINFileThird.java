package xbtplotter;

import java.io.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class SeasBINFileThird implements SeasProfileBuilder, FileFilter {

	/**
	 * BIN file extension.
	 */
	public static String BINExtension = new String(".bin");
	
	/* size of a byte in bits */
	private static final int BYTESIZE = 8;
		
	/* holds the value of each bit in the binary file */
	private List<Byte> bits;
	
	/* holds the value of each byte in the binary file */
	private List<Byte> bytes;
	
	
	/* valid profile resolutions */
	private static final int FULLRESOLUTION = 1;
	private static final int TWOMETERS = 2;
	private static final int INFLECTIONPOINTS = 3;
	
	/* data & meta-data precision values */
	private static final int TWODEC = 2;
	
	private static class SEAS2K {

		// element size given in bits
		protected static final int WMO_ID_SIZE = 72;
                protected static final int OLDMESSAGETYPE_SIZE = 6;
                protected static final int NEWMESSAGETYPE_SIZE = 10;
		protected static final int LAT_SIZE = 25;
		protected static final int LON_SIZE = 26;
                protected static final int SOOPLINE_SIZE = 48;
                protected static final int TRANSECTNO_SIZE = 7;
                protected static final int SEQNO_SIZE = 16;
		protected static final int YEAR_SIZE = 12;
		protected static final int MONTH_SIZE = 4;
		protected static final int DAY_SIZE = 6;
		protected static final int HOUR_SIZE = 5;
		protected static final int MINUTE_SIZE = 6;
		protected static final int SHIPNAME_SIZE = 240;
		protected static final int LLOYD_SIZE = 24;
                protected static final int UNIQUETAG_SIZE = 32;
		protected static final int SEASVERSION_SIZE = 10;
                protected static final int PROBESERIAL_SIZE = 24;//20 for version 1
                protected static final int THISDATAIS_SIZE = 3;
                protected static final int DATAQUAL_SIZE = 3;
                protected static final int LAUNCHHEIGHT_SIZE = 13;
                protected static final int SHIPDIR_SIZE = 9;
                protected static final int SHIPSPEED_SIZE = 13;
                protected static final int INSTTYPE_SIZE = 10;
                protected static final int RECORDERTYPE_SIZE = 7;
                protected static final int TYPEWINDINST_SIZE = 4;
                protected static final int WINDDIR_SIZE = 9;
                protected static final int WINDSPEED_SIZE = 12;
                protected static final int DRYBULBTEMP_SIZE = 12;
                protected static final int CURRMEASMETH_SIZE = 3;
                protected static final int CURRENTDIR_SIZE = 9;
                protected static final int CURRENTSPEED_SIZE = 13;
                protected static final int TOTALWATERDEPTH_SIZE = 14;
                protected static final int AGENCYOWNER_SIZE = 20;
                protected static final int XBTLAUNCHERTYPE_SIZE = 8;
                protected static final int XBTRECORDERSERIAL_SIZE = 64;
                protected static final int XBT_REC_MANUF_YEAR_SIZE = 12;
                protected static final int XBT_REC_MANUF_MONTH_SIZE = 4;
                protected static final int XBT_REC_MANUF_DAY_SIZE = 6;
                protected static final int XBT_PROBE_MANUF_YEAR_SIZE = 12;
                protected static final int XBT_PROBE_MANUF_MONTH_SIZE = 4;
                protected static final int XBT_PROBE_MANUF_DAY_SIZE = 6;
                protected static final int NO_RIDERBLOCKS_SIZE = 6;
                protected static final int NO_RIDEREMAILBLOCKS_SIZE = 6;
                protected static final int NO_RIDERPHONEBLOCKS_SIZE = 6;
                protected static final int NO_RIDERINSTBLOCKS_SIZE = 6;
                protected static final int NOREPFIELDS_SIZE = 8;
                protected static final int TIMEREPLICATED_SIZE = 16;
                protected static final int SEATEMP_SIZE = 12;
		protected static final int DEPTH_SIZE = 14;                

		// position of the first bit
		protected static final int WMO_ID_START = 0;
                protected static final int OLDMESSAGETYPE_START = 72;
                protected static final int NEWMESSAGETYPE_START = 78;
		protected static final int LAT_START = 88;
		protected static final int LON_START = 113;
                protected static final int SOOPLINE_START = 139;
                protected static final int TRANSECTNO_START = 187;
                protected static final int SEQNO_START = 194;
		protected static final int YEAR_START = 210;
		protected static final int MONTH_START = 222;
		protected static final int DAY_START = 226;
		protected static final int HOUR_START = 232;
		protected static final int MINUTE_START = 237;
		protected static final int SHIPNAME_START = 243;
		protected static final int LLOYD_START = 483;
                protected static final int UNIQUETAG_START = 507;
		protected static final int SEASVERSION_START = 539;
                protected static final int PROBESERIAL_START = 549;
                protected static final int THISDATAIS_START = 573;
                protected static final int DATAQUAL_START = 576;
                protected static final int LAUNCHHEIGHT_START = 579;
                protected static final int SHIPDIR_START = 592;
                protected static final int SHIPSPEED_START = 601;
                protected static final int INSTTYPE_START = 614;
                protected static final int RECORDERTYPE_START = 624;
                protected static final int TYPEWINDINST_START = 631;
                protected static final int WINDDIR_START = 635;
                protected static final int WINDSPEED_START = 644;
                protected static final int DRYBULBTEMP_START = 656;
                protected static final int CURRMEASMETH_START = 668;
                protected static final int CURRENTDIR_START = 671;
                protected static final int CURRENTSPEED_START = 680;
                protected static final int TOTALWATERDEPTH_START = 693;
                protected static final int AGENCYOWNER_START = 707;
                protected static final int XBTLAUNCHERTYPE_START = 727;
                protected static final int XBTRECORDERSERIAL_START = 735;
                protected static final int XBT_REC_MANUF_YEAR_START = 799;
                protected static final int XBT_REC_MANUF_MONTH_START = 811;
                protected static final int XBT_REC_MANUF_DAY_START = 815;
                protected static final int XBT_PROBE_MANUF_YEAR_START = 821;
                protected static final int XBT_PROBE_MANUF_MONTH_START = 833;
                protected static final int XBT_PROBE_MANUF_DAY_START = 837;
                protected static final int NO_RIDERBLOCKS_START = 843;
                protected static final int NO_RIDEREMAILBLOCKS_START = 849;
                protected static final int NO_RIDERPHONEBLOCKS_START = 855;
                protected static final int NO_RIDERINSTBLOCKS_START = 861;
                protected static final int NOREPFIELDS_START = 867;
                protected static final int TIMEREPLICATED_START = 875;
                protected static final int SEATEMP_START = 891;
                protected static final int SEATEMPS_START = 903;
                protected static final int COMMENT_BLOCKS_START = 18411;
                
                // floating point scaling info
		protected static final int LAT_OFFSET = -9000000;
		protected static final int LAT_SCALE = 5;
		protected static final int LON_OFFSET = -18000000;
		protected static final int LON_SCALE = 5;
                protected static final int SOOPLINE_OFFSET = 0;
                protected static final int SOOPLINE_SCALE = 1;
		protected static final int TEMP_OFFSET = -400;
		protected static final int TEMP_SCALE = 2;
		protected static final int DEPTH_OFFSET = 0;
		protected static final int DEPTH_SCALE = 2;
		protected static final int WSPEED_OFFSET = 0;
		protected static final int WSPEED_SCALE = 1;
		protected static final int BULBTMP_OFFSET = 0;
		protected static final int BULBTMP_SCALE = 1;
		protected static final int CSPEED_OFFSET = 0;
		protected static final int CSPEEDP_SCALE = 2;
                protected static final int SHIPSPEED_OFFSET = 0;
		protected static final int SHIPSPEED_SCALE = 2;
                protected static final int LAUNCHHEIGHT_OFFSET = 0;
		protected static final int LAUNCHHEIGHT_SCALE = 2;

	}
	
	/**
	 * Default constructor
	 */
	public SeasBINFileThird()
	{
		bits = new ArrayList<Byte>();
	}
	
	
	/** 
	 * Returns an integer number constructed using the bits from start 
	 * inclusive to start+size inclusive.
	 * @param start the position of the first bit
	 * @param size the size of the integer in bits
	 * @return an integer of size size starting at start
	 */
	private int getInteger(int start, int size)
	{
	  int mask;
	  int v = 0;
	  
	  for(int i=0; i<size; i++)
	  {
              if ((start+i) < bits.size()) {
		  mask = bits.get(start+i) << size-i-1;
		  v = v | mask;
              }
	  }

	  return v;
	}

	/** 
	 * Returns an long-integer number constituting the SEAS ID of the profile.
	 * The SEAS ID is placed in the profile by SEAS at the time the profile
	 * was recorded. 
	 * @return an long-integer constituting the SEAS ID of the profile
	 */
	private long getSeasId()
	{
	  int mask;
	  int v;
	  
	  byte[] seasId = new byte[4]; // SEAS ID is a 32-bit number
	  
	  for(int i=0; i<4; i++) { // get the 32-bits
		  v = 0;
		  for(int j=0; j<8; j++) {
			  mask = bits.get(SEAS2K.UNIQUETAG_START+(i*8)+j) << 8-j-1;
			  v = v | mask;
		  }
		  seasId[i] = (byte) v;
	  }

	  return (new BigInteger(1, seasId)).longValue();
	}
	
	/**
	 * Returns a char starting at bits.get(start).
	 * @param start the position of the first bit
	 * @return a char starting at start
	 */
	private char getChar(int start)
	{
		int mask;
		int v = 0;
		
		for(int i=0; i<BYTESIZE; i++)
		{
			mask = bits.get(start+i) << BYTESIZE-i-1;
			v = v | mask;
		}

		return (char) v;
	}
        
        private char getChar(int start, int scale)
	{
		int mask;
		int v = 0;
		
		for(int i=0; i<BYTESIZE; i++)
		{
                    System.out.println("- " + start+i + " - " + bits.get(start+i));
			mask = bits.get(start+i) << BYTESIZE-i-1;
			v = v | mask;
		}

		return (char) v;
	}
	
	/**
	 * Rounds up the given number using the given precision.
	 * @param n the number to round up
	 * @param p the decimal places to use
	 * @return the rounded up number
	 */
	private double roundDouble(double n, int p)
	{
		double val, result, precission;
		long number;
		
		// round |n|
		val = Math.abs(n);
		precission = Math.pow(10, p);
		number = Math.round(val * precission);
		result = number/precission;
		
		// determine sign
		if( n < 0)
			result = result * -1;
		
		return result;
	}
	
	/**
	 * Builds a SeasXBTProfile based on the given source file.
	 * @param file file the source file for building the SeasXBTProfile
	 * @return the built SeasXBTProfile
	 * @throws Exception if there was a problem while building the profile
	 */
	@Override
	public synchronized SeasXBTProfile buildSeasProfile(File file) throws Exception 
	{
		/* SeasXBTProfile meta-data */
		double longitude,latitude, launchHeight;
		int year,month,day;
		int hour,minute;
		int dataType, dataQuality;
		int serialNumber, lloydsNumber;
		long seasId;
		String seasVersion, shipName,callSign, soopline;
		int recorderType, instrumentType;
		int typeWindInstrument, windDir;
		double windSpeed;
		double dryBulbTemp;
		int currentMethod, currentDir, shipDir;
		double currentSpeed, shipSpeed, seaTemp;
                int newmessageType, oldmessageType, agencyOwner;
                int transecNumber, seqNumber, totalWaterDepth;
                String recorderSerial, probeManufacturerYear;
                int xbtLauncher, probeManufacturerMonth, probeManufacturerDay;
                int recorderManufacturerYear, recorderManufacturerMonth, recorderManufacturerDay;
                int riderBlockNumber, emailBlockNumber, phoneBlockNumber, institutionBlockNumber;
                int repField;
		
		/* SeasXBTProfile data */
		List<Double> measurements;
		List<Double> depths;
		
		/* total number of measurements */
		int totalMeasurements;
		
		/* reset bits & bytes array */
		bits = new ArrayList<Byte>();
		bytes = new ArrayList<Byte>();
		
		// if not S2K file, throw exception
		if(!accept(file))
		{
			throw new Exception("Error while building SeasXBTProfile. " +
					"Input file format not supported. File: " + file);
		}
				
		// input stream
		FileInputStream inputStream = null;
		
		try	{
			// read binary file
			inputStream = new FileInputStream(file);
                        InputStreamReader fis = new InputStreamReader(inputStream);

                        int count = 0;
                        //fis.ready();
                        
			byte b; int i; int mask; int c;
			while(/*(c=inputStream.read())!=-1*/fis.ready())
			{
                            c=inputStream.read();
				// place byte in bits buffer
                        
				for(i=7; i>=0; i--)
				{
                                    b = (byte) ((c >> i) &00000001);
                                    bits.add(b);

//					mask = (int) Math.pow(2,(7-i));
//					if((c&mask)==mask)
//						b = 1;
//					else
//						b = 0;
//					bits.add(b);
				}
				// place byte in byteBuffer
				bytes.add((byte) c);
			}
		} catch (FileNotFoundException e) {
			throw new Exception("Error while building SeasXBTProfile. "
					+ "Input file can't be open. File: " + file);			
		} catch (IOException e) {
			throw new Exception("Error while reading binary file. File: " + file);			
		} finally {
			// close input
			if(inputStream != null)
				try{inputStream.close();} catch(Exception e) {} // ignore close exception				
		}
System.out.println(file.toString());
                // decode year
		year = getInteger(SEAS2K.YEAR_START, SEAS2K.YEAR_SIZE);
                
		// decode month
		month = getInteger(SEAS2K.MONTH_START, SEAS2K.MONTH_SIZE);

		// decode day
		day = getInteger(SEAS2K.DAY_START, SEAS2K.DAY_SIZE);
		
		// decode hour
		hour = getInteger(SEAS2K.HOUR_START, SEAS2K.HOUR_SIZE);
		
		// decode minute
		minute = getInteger(SEAS2K.MINUTE_START, SEAS2K.MINUTE_SIZE);

		// decode latitude
		latitude = (getInteger(SEAS2K.LAT_START, SEAS2K.LAT_SIZE)+SEAS2K.LAT_OFFSET) / Math.pow(10, SEAS2K.LAT_SCALE);

		// decode longitude
		longitude = (getInteger(SEAS2K.LON_START, SEAS2K.LON_SIZE)+SEAS2K.LON_OFFSET) / Math.pow(10, SEAS2K.LON_SCALE);
                
                		// decode soop line
		char [] sooplineBuffer = new char[SEAS2K.SOOPLINE_SIZE/BYTESIZE];
		for(int i=0; i<sooplineBuffer.length; i++)
		{
			sooplineBuffer[i] = getChar(SEAS2K.SOOPLINE_START+(BYTESIZE*i));
		}
		soopline = new String(sooplineBuffer).trim();

                // decode old message type
		oldmessageType = getInteger(SEAS2K.OLDMESSAGETYPE_START, SEAS2K.OLDMESSAGETYPE_SIZE);

                // decode new message type
		newmessageType = getInteger(SEAS2K.NEWMESSAGETYPE_START, SEAS2K.NEWMESSAGETYPE_SIZE);
                
                // decode transecion number
		transecNumber = getInteger(SEAS2K.TRANSECTNO_START, SEAS2K.TRANSECTNO_SIZE);
           
                // decode sequence number
		seqNumber = getInteger(SEAS2K.SEQNO_START, SEAS2K.SEQNO_SIZE);
                
		// decode ship's lloyds number
		lloydsNumber = getInteger(SEAS2K.LLOYD_START, SEAS2K.LLOYD_SIZE);

		// decode SEASID
		seasId = getSeasId();

		// decode SEAS version
		seasVersion = new Integer(getInteger(SEAS2K.SEASVERSION_START, SEAS2K.SEASVERSION_SIZE)).toString();
		// adjust SEAS version
		seasVersion = String.format("%s.%s", seasVersion.substring(0, seasVersion.length()-2), 
				seasVersion.substring(seasVersion.length()-2, seasVersion.length()));

		// decode XBT serial number
		serialNumber = getInteger(SEAS2K.PROBESERIAL_START, SEAS2K.PROBESERIAL_SIZE);

		// decode data type
		dataType = getInteger(SEAS2K.THISDATAIS_START, SEAS2K.THISDATAIS_SIZE);

		// decode instrument type
		instrumentType = getInteger(SEAS2K.INSTTYPE_START, SEAS2K.INSTTYPE_SIZE);

		// decode recorder type
		recorderType = getInteger(SEAS2K.RECORDERTYPE_START, SEAS2K.RECORDERTYPE_SIZE);

		// decode wind instrument
		typeWindInstrument = getInteger(SEAS2K.TYPEWINDINST_START, SEAS2K.TYPEWINDINST_SIZE);

		// decode wind direction
		windDir = getInteger(SEAS2K.WINDDIR_START, SEAS2K.WINDDIR_SIZE);

		// decode wind speed
		windSpeed = (getInteger(SEAS2K.WINDSPEED_START, SEAS2K.WINDSPEED_SIZE)+SEAS2K.WSPEED_OFFSET) / Math.pow(10, SEAS2K.WSPEED_SCALE);
		
		// decode dry bulb temperature
		dryBulbTemp = (getInteger(SEAS2K.DRYBULBTEMP_START, SEAS2K.DRYBULBTEMP_SIZE)+SEAS2K.BULBTMP_OFFSET) / Math.pow(10, SEAS2K.BULBTMP_SCALE);
		
		// decode current method
		currentMethod = getInteger(SEAS2K.CURRMEASMETH_START, SEAS2K.CURRMEASMETH_SIZE);
                
		// decode launch height
		launchHeight = (getInteger(SEAS2K.LAUNCHHEIGHT_START, SEAS2K.LAUNCHHEIGHT_SIZE)+SEAS2K.LAUNCHHEIGHT_OFFSET) / Math.pow(10, SEAS2K.LAUNCHHEIGHT_SCALE);		
                
		// decode current direction
		currentDir = getInteger(SEAS2K.CURRENTDIR_START, SEAS2K.CURRENTDIR_SIZE);
                
                // decode ship direction
		shipDir = getInteger(SEAS2K.SHIPDIR_START, SEAS2K.SHIPDIR_SIZE);

		// decode current speed
		currentSpeed = (getInteger(SEAS2K.CURRENTSPEED_START, SEAS2K.CURRENTSPEED_SIZE)+SEAS2K.CSPEED_OFFSET) / Math.pow(10, SEAS2K.CSPEEDP_SCALE);

		// decode ship speed
		shipSpeed = (getInteger(SEAS2K.SHIPSPEED_START, SEAS2K.SHIPSPEED_SIZE)+SEAS2K.SHIPSPEED_OFFSET) / Math.pow(10, SEAS2K.SHIPSPEED_SCALE);
                
		// decode number of measurements
		totalMeasurements = getInteger(SEAS2K.TIMEREPLICATED_START, SEAS2K.TIMEREPLICATED_SIZE);
                
		// decode total water depth
		totalWaterDepth = getInteger(SEAS2K.TOTALWATERDEPTH_START, SEAS2K.TOTALWATERDEPTH_SIZE);	
                
		// decode agency owner
		agencyOwner = getInteger(SEAS2K.AGENCYOWNER_START, SEAS2K.AGENCYOWNER_SIZE);

                // decode xbt launcher type
		xbtLauncher = getInteger(SEAS2K.XBTLAUNCHERTYPE_START, SEAS2K.XBTLAUNCHERTYPE_SIZE);
                
                // decode recorder manufacturer year
		recorderManufacturerYear = getInteger(SEAS2K.XBT_REC_MANUF_YEAR_START, SEAS2K.XBT_REC_MANUF_YEAR_SIZE);

                // decode recorder manufacturer month
		recorderManufacturerMonth = getInteger(SEAS2K.XBT_REC_MANUF_MONTH_START, SEAS2K.XBT_REC_MANUF_MONTH_SIZE);

                // decode recorder manufacturer day
		recorderManufacturerDay = getInteger(SEAS2K.XBT_REC_MANUF_DAY_START, SEAS2K.XBT_REC_MANUF_DAY_SIZE);

                // decode probe manufacturer month
		probeManufacturerMonth = getInteger(SEAS2K.XBT_PROBE_MANUF_MONTH_START, SEAS2K.XBT_PROBE_MANUF_MONTH_SIZE);

                // decode probe manufacturer day
		probeManufacturerDay = getInteger(SEAS2K.XBT_PROBE_MANUF_DAY_START, SEAS2K.XBT_PROBE_MANUF_DAY_SIZE);
                
                // decode block number for rider
		riderBlockNumber = getInteger(SEAS2K.NO_RIDERBLOCKS_START, SEAS2K.NO_RIDERBLOCKS_SIZE);

                // decode block number for email
		emailBlockNumber = getInteger(SEAS2K.NO_RIDEREMAILBLOCKS_START, SEAS2K.NO_RIDEREMAILBLOCKS_SIZE);

                // decode block number for phone
		phoneBlockNumber = getInteger(SEAS2K.NO_RIDERPHONEBLOCKS_START, SEAS2K.NO_RIDERPHONEBLOCKS_SIZE);
                
                // decode block number for institution
		institutionBlockNumber = getInteger(SEAS2K.NO_RIDERINSTBLOCKS_START, SEAS2K.NO_RIDERINSTBLOCKS_SIZE);
                
                // decode rep field
		repField = getInteger(SEAS2K.NOREPFIELDS_START, SEAS2K.NOREPFIELDS_SIZE);
                
                // decode data quality
		dataQuality = getInteger(SEAS2K.DATAQUAL_START, SEAS2K.THISDATAIS_SIZE);
                
                // decode xbt recorder serial
		char [] recorderSerialBuffer = new char[SEAS2K.XBTRECORDERSERIAL_SIZE/BYTESIZE];
		for(int i=0; i<recorderSerialBuffer.length; i++)
		{
			recorderSerialBuffer[i] = getChar(SEAS2K.XBTRECORDERSERIAL_START+(BYTESIZE*i));
		}
		recorderSerial = new String(recorderSerialBuffer).trim();
                
                // decode probe manufacturer year
		char [] probeManufacturerYearBuffer = new char[SEAS2K.XBT_PROBE_MANUF_YEAR_SIZE/BYTESIZE];
		for(int i=0; i<probeManufacturerYearBuffer.length; i++)
		{
			probeManufacturerYearBuffer[i] = getChar(SEAS2K.XBT_PROBE_MANUF_YEAR_START+(BYTESIZE*i));
		}
		probeManufacturerYear = new String(probeManufacturerYearBuffer).trim();
                
		// decode call sign
		char [] callBuffer = new char[SEAS2K.WMO_ID_SIZE/BYTESIZE];
		for(int i=0; i<callBuffer.length; i++)
		{
			callBuffer[i] = getChar(SEAS2K.WMO_ID_START+(BYTESIZE*i));
		}
		callSign = new String(callBuffer).trim();
		
		// decode ship name
		char [] shipBuffer = new char[SEAS2K.SHIPNAME_SIZE/BYTESIZE];
		for(int i=0; i<shipBuffer.length; i++)
                {
			shipBuffer[i] = getChar(SEAS2K.SHIPNAME_START+(BYTESIZE*i));
		}
		shipName = new String(shipBuffer).trim();

                // validate resolution
		if(dataType!=FULLRESOLUTION && dataType!=TWOMETERS && dataType!=INFLECTIONPOINTS)
		{
			throw new Exception("Error while building SEAS profile. Data resolution not supported. "
					+ "File: " + file);
		}
                
           	// decode ship speed
		seaTemp = (getInteger(SEAS2K.SEATEMP_START, SEAS2K.SEATEMP_SIZE)+SEAS2K.TEMP_OFFSET) / Math.pow(10, SEAS2K.TEMP_SCALE);

		// decode data
		measurements = new ArrayList<Double>();
		depths = new ArrayList<Double>();
		for(int i=0; i<totalMeasurements; i++)
		{
			int START = SEAS2K.SEATEMPS_START + i * SEAS2K.SEATEMP_SIZE;
			int measurement;
			double depth = 0; double temperature = 0;

			if(dataType == FULLRESOLUTION) {
				// get temperature measurement
				measurement = getInteger(START, SEAS2K.SEATEMP_SIZE);
				// calculate temperature & depth
				temperature = (measurement+SEAS2K.TEMP_OFFSET) / Math.pow(10, SEAS2K.TEMP_SCALE);
				depth = XBTDepthCalculator.calculateDepth(instrumentType, (i+1)*0.1);
			} else if(dataType == TWOMETERS) {
				// get temperature measurement
				measurement = getInteger(START, SEAS2K.SEATEMP_SIZE);
				// calculate temperature & depth
				temperature = (measurement+SEAS2K.TEMP_OFFSET) / Math.pow(10, SEAS2K.TEMP_SCALE);
				depth = 2 * i;
			} else if(dataType == INFLECTIONPOINTS) {
				// TODO fix this block doesn't work!
				// TODO first read depth then temperature
				// get temperature measurement
				measurement = getInteger(START, SEAS2K.SEATEMP_SIZE);
				temperature = (measurement+SEAS2K.TEMP_OFFSET) / Math.pow(10, SEAS2K.TEMP_SCALE);
				// get depth measurement
				measurement = getInteger(START+SEAS2K.SEATEMP_SIZE, SEAS2K.DEPTH_SIZE);
				depth = (measurement+SEAS2K.DEPTH_OFFSET) / Math.pow(10, SEAS2K.DEPTH_SCALE);
			}
			// adjust values
			temperature = roundDouble(temperature, TWODEC);
			depth = roundDouble(depth, TWODEC);
			// assign values
			measurements.add(new Double(temperature));
			depths.add(new Double(depth));
		}
		
		// construct profile using date and position
		SeasXBTProfile profile = new SeasXBTProfile(longitude,latitude,year,month,day,hour,minute);
		
		// add meta-data
		profile.setCallSign(callSign);
		profile.setShipName(shipName);
		profile.setIMO(lloydsNumber);
		profile.setSEASVersion(seasVersion);
		profile.setRecorderType(recorderType);
		
		// add SEAS ID
		profile.setSEASId(seasId);
		
		// construct instrument object
		XBT probe = new XBT(instrumentType);
		probe.setSerialNumber(serialNumber);
		
		// add instrument
		profile.setInstrument(probe);
		
		// set resolution
		profile.setResolution(dataType);
		
		// set wind info
		profile.setWindInstrumentType(typeWindInstrument);
		profile.setWindDirection(windDir);
		profile.setWindSpeed(windSpeed);
		
		// set current info
		profile.setCurrentMeasurementMethod(currentMethod);
		profile.setCurrentDirection(currentDir);
		profile.setCurrentSpeed(currentSpeed);
		
		// set bulb temperature
		profile.setDryBulbTemperature(dryBulbTemp);
		
		// add data
		for(int i=0; i<measurements.size(); i++)
		{
			TemperatureMeasurement measurement = new TemperatureMeasurement(depths.get(i), measurements.get(i));
			profile.addTemperatureMeasurement(measurement);
		}
		
		// add source data
		byte [] source = new byte[bytes.size()];
		for(int i=0; i<bytes.size(); i++)
		{
			source[i] = bytes.get(i);
		}
		profile.setSourceName(file.getName());
		profile.setSource(source);
				
		return profile;
	}

	/**
	 * Returns true if and only if the format of the given file is
	 * supported by this SeasProfileBuilder.
	 * @param file file to be tested for support
	 * @return true if the format of the given file is supported
	 */
	@Override
	public boolean isFileSupported(File file) {
		return accept(file);
	}
	
	/**
	 * Returns true if the given file is valid and has *.BIN extension.
	 */
	@Override
	public boolean accept(File file) 
	{
		// if file is null, not accepted
		if(file == null)
			return false;
		
		// if file is not valid, not accepted
		if(!file.isFile())
			return false;
		
		// return true if the given file is of the form *.bin		
		return file.getName().toLowerCase().endsWith(SeasBINFileThird.BINExtension);
	}

}
