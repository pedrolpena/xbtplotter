package xbtplotter;

public class XBTBinField {

    private int binVersion;
    private int bitSize;
    private int maximum;
    private int minimum;
    private int offset;
    private int positionNumber;
    private int scale;
    private String unit;

    public XBTBinField() {}

    public XBTBinField(int bv, int bs, int max,
            int min, int os, int pn, int s, String u) 
    {
        binVersion = bv;
        bitSize = bs;
        maximum = max;
        minimum = min;
        offset = os;
        positionNumber = pn;
        scale = s;
        unit = u;
    }

    /**
     * Sets the profile's binary version number
     * @param bv binary version integer
     */
    public void setBinVersion(int bv) {
        binVersion = bv;
    }
    
    /**
     * Sets the number of bits allocated in binary file
     * @param bs bit size integer
     */
    public void setBitSize(int bs) {
        bitSize = bs;
    }

    /**
     * Sets the maximum length of characters for string or
     * maximum number for string
     * @param max maximum integer
     */
    public void setMaximum(int max) {
        maximum = max;
    }

    /**
     * Sets the minimum length of characters for string or
     * minimum number for string
     * @param min minimum integer
     */
    public void setMinimum(int min) {
        minimum = min;
    }

    /**
     * Sets the offset number of decimal values
     * @param os offset integer
     */
    public void setOffset(int os) {
        offset = os;
    }

    /**
     * Sets the position number within binary file
     * @param pn position integer
     */
    public void setPositionNumber(int pn) {
        positionNumber = pn;
    }

    /**
     * Sets the scale number of decimal values
     * @param s scale integer
     */
    public void setScale(int s) {
        scale = s;
    }

    /**
     * Sets the unit type of field
     * @param u unit String
     */
    public void setUnit(String u) {
        unit = u;
    }

    /**
     * Returns the profile's binary version number
     * @return binary version
     */
    public int getBinVersion() {
        return binVersion;
    }

    /**
     * Returns the number of bits allocated in binary file
     * @return bit size
     */
    public int getBitSize() {
        return bitSize;
    }

    /**
     * Returns the maximum length of characters for string or
     * maximum number for string
     * @return maximum
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Returns the minimum length of characters for string or
     * minimum number for string
     * @return minimum
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Returns the offset number of decimal values
     * @return bit size
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the position number within binary file
     * @return position number
     */
    public int getPositionNumber() {
        return positionNumber;
    }

    /**
     * Returns the scale number of decimal values
     * @return bit size
     */
    public int getScale() {
        return scale;
    }

    /**
     * Returns the unit type of field
     * @return unit
     */
    public String getUnit() {
        return unit;
    }
    
}