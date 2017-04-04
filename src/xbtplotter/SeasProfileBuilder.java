package xbtplotter;

import java.io.File;

/**
 * Provides an interface for building SeasXBTProfiles from different
 * file formats. Classes implementing this interface should provide a
 * way for building SeasXBTProfiles from a specific file format.
 * @author Jaime R. Soto
 *
 */
public interface SeasProfileBuilder {

	/**
	 * Builds a SeasXBTProfile based on the given source file.
	 * @param file the source file for building the SeasXBTProfile
	 * @return the built SeasXBTProfile
	 * @throws Exception if there was a problem while building the profile
	 */
	SeasXBTProfile buildSeasProfile(File file) throws Exception;
	
	/**
	 * Returns true if and only if the format of the given file is
	 * supported by this SeasProfileBuilder.
	 * @param file file to be tested for support
	 * @return true if the format of the given file is supported
	 */
	boolean isFileSupported(File file);
}
