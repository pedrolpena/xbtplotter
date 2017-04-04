package xbtplotter;

public class TemperatureMeasurement implements Comparable<TemperatureMeasurement> {

	protected double depth;
	protected double temperature;
	
	/**
	 * This constructor initializes the depth and temperature values to
	 * the given depth and temperature.
	 */
	public TemperatureMeasurement(double depth,double temperature)
	{
		this.depth = depth;
		this.temperature = temperature;
	}
	
	/**
	 * Default constructor. Initialize depth, temperature to 0.0.
	 */
	public TemperatureMeasurement()
	{
		// initialize internal state
		depth = 0.0;
		temperature = 0.0;
	}
	
	/**
	 * Returns the temperature value of this measurement.
	 * @return the temperature value of this measurement
	 */
	public double getTemperature()
	{
		return temperature;
	}
	
	/**
	 * Returns the depth value of this measurement.
	 * @return the depth value of this measurement
	 */
	public double getDepth()
	{
		return depth;
	}

	/**
	 * Compares this TemperatureMeasurement to the given TemperatureMeasurement. The attribute
	 * to be compare is depth. If both measurement have the same depth compareTo returns 0.
	 * If this measurement has a shallower depth to the given measurement, compareTo returns -1,
	 * and if this measurement has a deeper depth, compareTo returns 1.
	 * @param measurement TemperatureMeasurement to be compareTo
	 * @return 1 if depth is greater, -1 if depth is lesser, 0 if depth is equal
	 */
	@Override
	public int compareTo(TemperatureMeasurement measurement) 
	{
		// can't compare to null
		if(measurement == null)
		{
			throw new NullPointerException("A TemperatureMeasurement can't be compare to null.");
		}
		
		// if depth is lesser return -1
		if(Math.abs(this.getDepth()) < Math.abs(measurement.getDepth()))
		{
			return -1;
		}
		
		// if depth is greater return 1
		if(Math.abs(this.getDepth()) > Math.abs(measurement.getDepth()))
		{
			return 1;
		}
		
		// if depths are equal return 0 
		return 0;
	}
	
	/**
	 * Compares this measurement to the given measurements. Returns true if and only if both measurements
	 * are equals. Two measurements are equals if they have the same depth value.
	 * @param o TemperatureMeasurement to be compare
	 * @return true if and only if both TemperatureMeasurements have the same depth
	 */
	@Override
	public boolean equals(Object o)
	{
		// if o is the same object, return true
		if(this == o)
			return true;
		
		// if o is null return false
		if(o == null)
			return false;
		
		// if o is not a measurement return false
		if(!(o instanceof TemperatureMeasurement))
			return false;
		
		// case of to a TemperatureMeasurement
		TemperatureMeasurement m = (TemperatureMeasurement) o;
		
		// compare depths of both measurements
		return (this.getDepth() == m.getDepth());
	}
	
	/**
	 * Returns a hash code value for this TemperatureMeasurement.
	 * @return a hash code value for this TemperatureMeasurement
	 */
	public int hashCode()
	{
		int hash = 1;
		
		hash = hash * 31 + (new Double(this.depth)).hashCode();
		
		return hash;
	}
	
	
	/**
	 * Returns an string representation of the object
	 */
	@Override
	public String toString()
	{
		return new String(this.getDepth() + "," +this.getTemperature());
	}
}
