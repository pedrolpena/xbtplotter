package xbtplotter;

/**
 * Recorder type. This table encodes the various recorders used to log temperatures.
 * According to WMO Code Table 4770.
 * 
 */
public enum WMO4770 {
	
	SIPPICAN_SCR(1, "Sippican strip chart recorder"),
	SIPPICAN_MK2A(2, "Sippican MK2A/SSQ-61"),
	SIPPICAN_MK9(3, "Sippican MK-9"),
	SIPPICAN_MK7(4, "Sippican AN/BHQ-7/MK8"),
	SIPPICAN_MK12(5, "Sippican MK-12"),	
	SIPPICAN_MK21(6, "Sippican MK-21"),
	SIPPICAN_MK8(7, "Sippican MK-8 Linear Recorder"),
	SIPPICAN_MK10(8, "Sippican MK-10"),
	SPARTON_100(10, "Sparton SOC BT/SV Processor Model 100"),
	LOCKHEED_OL5005(11, "Lockheed-Sanders Model OL5005"),
	ARGO_XBTST(20, "Argos XBT-ST"),
	ARGO_M1(21, "CLS-ARGOS / Protecno XBT-ST Model-1"),
	ARGO_M2(22, "CLS-ARGOS / Protecno XBT-ST Model-2"),
	BATHY_SA810(30, "BATHY Systems SA-810"),
	SCRIPPS_METROBYTE(31, "Scripps Metrobyte controller"),
	MURAYAMA_III(32, "Murayama Denki Z-60-16 III"),
	MURAYAMA_II(33, "Murayama Denki Z-60-16 II"),
	PROTECNO_ETSM2(34, "Protecno ETSM2"),
	NAUTILUS_NMSXBT(35, "Nautilus Marine Service NMS-XBT"),
	TSK_MK2A(40, "TSK MK-2A"),
	TSK_MK2S(41, "TSK MK-2S"),
	TSK_MK30(42, "TSK MK-30"),
	TSK_MK30N(43, "TSK MK-30N"),
	TSK_MK100(45, "TSK MK-100"),
	TSK_MK100XCTD(46, "TSK MK-100 Compatible recorder for both XBT and XCTD"),
	TSK_MK1130A(47, "TSK MK-1130A XCTD"),
	TSK_MK300(48, "TSK AXBT Receiver MK-300"), 	
	TSK_MK150(49, "TSK MK-150 Compatible recorder for both XBT and XCTD"),
	JMA_ASTOS(50, "JMA ASTOS"),
	ARGO_UPTRANSIT(60, "ARGOS communications, sampling on up transit"),
	ARGO_DOWNTRANSIT(61, "ARGOS communications, sampling on down transit"),
	ORBCOMM_UPTRANSIT(62, "Orbcomm communications, sampling on up transit"),
	ORBCOMM_DOWNTRANSIT(63, "Orbcomm communications, sampling on down transit"),
	IRIDIUM_UPTRANSIT(64, "Iridium communications, sampling on up transit"), 	
	IRIDIUM_DOWNTRANSIT(65, "Iridium communications, sampling on down transit"), 	
	CSIRO_DEVIL1(70, "CSIRO Devil-1 XBT acquisition system"),
	CSIRO_DEVIL2(71, "CSIRO Devil-2 XBT acquisition system"),
	CSIRO_QUOLL(72, "TURO/CSIRO Quoll XBT acquisition system"),
	MICRO_SVTP(80, "Applied Microsystems Ltd., MICRO-SVT&P"),
	SMRU1(81, "Sea Mammal Research Unit, Univ. St. Andrews, UK, uncorrected salinity from a sea mammal mounted instrument"),
	SMRU2(82, "Sea Mammal Research Unit, Univ. St. Andrews, UK, salinity from a sea mammal mounted instrument"),
	UNKNOWN(99, "Unknown"),
	MISSING(127, "Missing value");
	
	/**
	 * Recorder code according to WMO table code 1770
	 */
	private final int code;
	private final String name;
	
	private WMO4770(int code, String recorder) {
		this.code = code; this.name = recorder;
	}
	
	/**
	 * @return the recorders's code
	 */
	public int code() {
		return code;
	}
	
	/**
	 * @return the recorder's name
	 */
	public String recorder() {
		return name;
	}

}
