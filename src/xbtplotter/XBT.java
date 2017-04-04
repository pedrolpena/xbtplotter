package xbtplotter;

import java.util.Date;

public class XBT {

	/**
	 * Probe's serial number.
	 */
	private long serialNumber;
	
	/**
	 * Probe type according to WMO Code table 1770.
	 */
	private int instrumentType;
	
	/**
	 * Probe instrument name according to WMO Code table 1770.
	 */
	private String instrumentName;
	
	/* Coefficients according to WMO Code table 1770 */
	double a; double b;
	
	/**
	 * Probe DOM.
	 */
	private Date dateOfManufacture;
	
	/**
	 * Numeric value not available.
	 */
	public static final int NaN = -1;
		
	/**
	 * Initialize the XBT with the given instrument type. The instumentField
	 * attribute is pulled from the instrumentField list, if available.
	 * @param instrumentType the instrument type according to WMO Code table 1770
	 */
	public XBT(int instrumentType)
	{
		// initialize not available values
		serialNumber = NaN;
		dateOfManufacture = null;
		
		// initialize code for check
		this.instrumentType = NaN;
		
		// set code, name, and coefficients
		for(WMO1770 probe : WMO1770.values())
		{
			if(instrumentType == probe.code())
			{
				this.instrumentType = probe.code();
				this.instrumentName = probe.instrument();
				this.a = probe.a();
				this.b = probe.b();
				break;
			}
		}
		
		// check instrument OK
		if(this.instrumentType == NaN)
		{
			throw new IllegalArgumentException("The given instrument type is not valid.");
		}
	}
	
	/**
	 * Returns the probe serial number.
	 * @return the probe serial number
	 */
	public long getSerialNumber()
	{
		return serialNumber;
	}

	/**
	 * Sets the probe's serial number
	 * @param serialNumber the probe serial number
	 */
	public void setSerialNumber(long serialNumber) 
	{
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the instrument type according to WMO Code table 1770
	 */
	public int getCode() 
	{
		return instrumentType;
	}

	/**
	 * @param instrumentType the instrument type according to WMO Code table 1770
	 */
	public void setInstrumentType(int instrumentType) 
	{
		this.instrumentType = instrumentType;
	}
	
	/**
	 * Returns the probe coefficient according to WMO Code Table 1770.
	 * @return the coefficient a according to WMO Code Table 1770
	 */
	public double a()
	{
		return a;
	}
	
	/**
	 * Returns the probe coefficient according to WMO Code Table 1770.
	 * @return the coefficient b according to WMO Code Table 1770
	 */
	public double b()
	{
		return b;
	}

	/**
	 * @return the instrument name according to WMO Code table 1770
	 */
	public String getInstrument() 
	{
		return instrumentName;
	}

	/**
	 * @return the date of manufacture
	 */
	public Date getDateOfManufacture()
	{
		return dateOfManufacture;
	}

	/**
	 * @param dateOfManufacture the date of manufacture
	 */
	public void setDateOfManufacture(Date dateOfManufacture)
	{
		this.dateOfManufacture = dateOfManufacture;
	}
	
	/**
	 * Returns true if the given object is an XBT, and the serial number 
	 * and instrument type are the same.
	 */
	public boolean equals(Object o)
	{
		// if this is the same object return true
		if( this == o)
			return true;
		
		// if the given object is null return false
		if(o == null)
			return false;
		
		// if o is not a XBT return false		
		if(!(o instanceof XBT))
			return false;
		
		// cast o into a XBT
		XBT xbt = (XBT) o;

		// equals if serial number and instrument type are the same
		return ((this.getSerialNumber() == xbt.getSerialNumber()) 
				&& (this.getCode() == xbt.getCode()));
	}
	
	/**
	 * Calculates the hash code of this XBT according to the specifications
	 * of the Java class Object equals() method
	 */
	@Override
	public int hashCode()
	{
		int hash = 1;
		
		hash = hash * 31 + (new Long(this.getSerialNumber())).hashCode();
		hash = hash * 31 + (new Integer(this.getCode())).hashCode();
		
		return hash;
	}
	
}
