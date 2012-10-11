package se.z_app.stb.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;

public class STBDiscovery {
	Discovery disc = new Discovery();
	
	
	public STB[] find() {  
		STB[] stb;
		stb = disc.find();
		
		return null; 
		}
	
	
	/*
	 * Finds the subnet of the devices network and returns a string in the form 192.168.0.
	 * 
	 */
	public String findSubnet() {
	    InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
				return addr.getHostAddress().toString().substring(0, addr.getHostAddress().lastIndexOf('.')+1);
			} catch (UnknownHostException e) { e.printStackTrace(); }
		return null;
	}
	
}
