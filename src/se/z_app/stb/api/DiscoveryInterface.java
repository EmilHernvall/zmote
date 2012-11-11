package se.z_app.stb.api;

import se.z_app.stb.STB;

/**
 * The interface for the Discovery mechanism in a STB box
 * @author Rasmus Holm
 *
 */
public interface DiscoveryInterface {
	public STB[] find();
	public STB[] find(STB stb);
	
}


