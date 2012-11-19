package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;

/**
 *
 * Calls the Discovery.find() for finding the customers box.
 * @author Viktor Dahl
 *
 */
public class STBDiscovery {
	String subNetAddress;
	Discovery disc;
	
	public STBDiscovery(String subNetAddress) {
		this.subNetAddress = subNetAddress;
	}
	
	/**
	 * 
	 * @return An array STB[] with the STB's found.
	 */
	public STB[] find() {
		disc = new Discovery(subNetAddress);
		return disc.find();
	}
	

}
