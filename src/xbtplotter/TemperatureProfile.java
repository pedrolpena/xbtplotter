package xbtplotter;

import java.util.List;

/**
 * Provides an interface to the standard operations of a Temperature profile. 
 *
 */
public interface TemperatureProfile{
	
	/**
	 * Returns true if the Profile has no TemperatureMeasurements.
	 * @return true if and only if there are no TemperatureMeasurements in the Profile
	 */
	boolean isEmpty();
	
	/**
	 * Returns the number of TemperatureMeasurements in the Profile.
	 * @return the number of TemperatureMeasurements in the Profile
	 */
	int size();
	
	/**
	 * Adds the given TemperatureMeasurement to the Profile. This operation warranties that
	 * the Profile will have it's TemperatureMeasurements in ascending order after insertion.
	 * @param measurement TemperatureMeasurement to be added to the Profile
	 * @return true if the TemperatureMeasurement was added to the Profile; false otherwise
	 * @throws NullPointerException if measurement is null
	 */
	boolean addTemperatureMeasurement(TemperatureMeasurement measurement) throws NullPointerException;
	
	/**
	 * Removes the given TemperatureProfile from the Profile. This operation warranties that
	 * the Profile will have it's TemperatureMeasurements in ascending order after deletion.
	 * @param measurement TemperatureMeasurement to be removed from the Profile
	 * @return true if the TemperatureMeasurement was removed from the Profile; false otherwise
	 * @throws NullPointerException if measurement is null
	 */
	boolean removeTemperatureMeasurement(TemperatureMeasurement measurement) throws NullPointerException;
	
	/**
	 * Returns a read-only list containing all the TemperatureMeasurements in the Profile.
	 * The returned list is in ascending order.
	 * @return read-only list containing all the TemperatureMeasurements in the Profile
	 */
	List<TemperatureMeasurement> getTemperatureMeasurementList();	
}
