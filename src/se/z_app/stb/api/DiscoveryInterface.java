package se.z_app.stb.api;

import se.z_app.stb.STB;

public interface DiscoveryInterface {
	public STB[] find();
	public STB[] find(STB stb);
	
}
