package xbtplotter;

/**
 * 
 * Instrument type for temperature, with fall rate equation coefficients for XBT.
 * According to WMO Code Table 1770.
 * 
 */
public enum WMO1770 {

	SIPPICAN_T4_OLD(1, "Sippican T-4", 6.472, -2.16),
	SIPPICAN_T4(2, "Sippican T-4", 6.691, -2.25),
	SIPPICAN_T5(11, "Sippican T-5", 6.828, -1.82),
	SIPPICAN_FAST_DEEP(21, "Sippican Fast Deep", 6.346, -1.82),
	SIPPICAN_T6_OLD(31, "Sippican T-6", 6.472, -2.16),	
	SIPPICAN_T6(32, "Sippican T-6", 6.691, -2.25),
	SIPPICAN_T7_OLD(41, "Sippican T-7", 6.472, -2.16),	
	SIPPICAN_T7(42, "Sippican T-7", 6.691, -2.25),
	SIPPICAN_DEEP_BLUE_OLD(51, "Sippican Deep Blue", 6.472, -2.16),
	SIPPICAN_DEEP_BLUE(52, "Sippican Deep Blue", 6.691, -2.25),	
	SIPPICAN_T10(61, "Sippican T-10", 6.301, -2.16),
	SIPPICAN_T11(71, "Sippican T-11", 1.779, -0.255),	
	SIPPICAN_AXBT(81, "Sippican AXBT (300 m probes)", 1.52, 0.0),
	
	TSK_T4_OLD(201, "TSK T-4", 6.472, -2.16),
	TSK_T4(202, "TSK T-4", 6.691, -2.25),
	TSK_T6_OLD(211, "TSK T-6", 6.472, -2.16),
	TSK_T6(212, "TSK T-6", 6.691, -2.25),
	TSK_T7_OLD(221, "TSK T-7", 6.472, -2.16),
	TSK_T7(222, "TSK T-7", 6.691, -2.25),
	TSK_T5(231, "TSK T-5", 6.828, -1.82),
	TSK_T10(241, "TSK T-10", 6.301, -2.16),
	TSK_DEEP_BLUE_OLD(251, "TSK Deep Blue", 6.472, -2.16),
	TSK_DEEP_BLUE(252, "TSK Deep Blue", 6.691, -2.25),
	
	SPARTON_XBT1(401, "Sparton XBT-1", 6.301, -2.16),
	SPARTON_XBT3(411, "Sparton XBT-3", 5.861, -0.0904),
	SPARTON_XBT4(421, "Sparton XBT-4", 6.472, -2.16),
	SPARTON_XBT5(431, "Sparton XBT-5", 6.828, -1.82),
	SPARTON_XBT5DB(441, "Sparton XBT-5DB", 6.828, -1.82),
	SPARTON_XBT6(451, "Sparton XBT-6", 6.472, -2.16),
	SPARTON_XBT7_OLD(461, "Sparton XBT-7", 6.472, -2.16),
	SPARTON_XBT7(462, "Sparton XBT-7", 6.705, -2.28),
	SPARTON_XBT7DB(471, "Sparton XBT-7DB", 6.472, -2.16),
	SPARTON_XBT10(481, "Sparton XBT-10", 6.301, -2.16),
	SPARTON_XBT20(491, "Sparton XBT-20", 6.472, -2.16),
	SPARTON_XBT20DB(501, "Sparton XBT-20DB", 6.472, -2.16),
	SPARTON_536AXBT(510, "Sparton 536 AXBT", 1.524, 0.0);
	
	
	
	
	/**
	 * Instrument code and coefficients according to
	 * WMO table code 1770
	 */
	private final int code;
	private final String name;
	private final double a;
	private final double b;
	
	private WMO1770(int code, String instrument, double a, double b)
	{
		this.code = code; this.name = instrument; this.a = a; this.b = b;
	}

	/**
	 * @return the a coefficient
	 */
	public double a() {
		return a;
	}

	/**
	 * @return the b coefficient
	 */
	public double b() {
		return b;
	}

	/**
	 * @return the instrument's code
	 */
	public int code() {
		return code;
	}
	
	/**
	 * @return the instrument's name
	 */
	public String instrument() {
		return name;
	}
}
