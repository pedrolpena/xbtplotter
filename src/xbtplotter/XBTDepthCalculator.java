package xbtplotter;

/**
 * 
 * 
 * Calculates the depth of a XBT measurement using the fall rate equation and the
 * instrument coefficients.
 */
public abstract class XBTDepthCalculator {
	

	
	/**
	 * Calculates the depth of a XBT measurement for the given time
	 * using the fall rate equation.
	 * @param instrument the instrument type based on WMO code table 1770
	 * @param time the time after deployment given in seconds
	 * @return the estimated depth in meters
	 */
	public static double calculateDepth(int instrument,double time)
	{	
		for(WMO1770 probe : WMO1770.values())
		{
			if(instrument == probe.code())
				return applyFallRate(probe.a(), probe.b(), time);
		}
		
		return 0.0;
	}
	
	/**
	 * Applies the XBT fall rate equation for the given time
	 * using coefficients a and b.
	 * @param a the a coefficient of the equation
	 * @param b the b coefficient of the equation
	 * @param t the time in seconds
	 * @return the calculated depth in meters
	 */
	private static double applyFallRate(double a, double b, double t)
	{
		return (a*t) + (0.001*b*t*t);
	}
	

}
