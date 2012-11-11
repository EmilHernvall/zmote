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
	
	public STB[] find() {
		disc = new Discovery(subNetAddress);
		return disc.find();
	}
	

}
