package se.z_app.stb.api;

import se.z_app.stb.STB;

/**
 * The interface for the Discovery mechanism in a STB box
 * @author Rasmus Holm
 *
 */
public interface DiscoveryInterface {
	
	/**
	 * Function that searches the subnet and returns an array of STB's found.
	 * @return Array of STB's.
	 */
	public STB[] find();
	
	/**
	 * Returns the result of the find function.
	 * @param stb
	 * @return Array of STB's.
	 */
	public STB[] find(STB stb);
	
}


