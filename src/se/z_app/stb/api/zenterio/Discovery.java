package se.z_app.stb.api.zenterio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;

import se.z_app.stb.api.STBDiscovery; // Should this be imported?

public class Discovery implements DiscoveryInterface {

	STBDiscovery stb = new STBDiscovery();
	
	/*
	 * Scans the network for STB's.
	 * Return an object InetAddress with the IP address to the STB
	 * TODO: Write better exception handling
	 */
	private InetAddress findFirstSTB() {
		URL url;
		int timeoutInMs = 30;
		InetAddress addr;
		for (int i = 1; i < 255; i++) {
			try {
				addr = InetAddress.getByName(stb.findSubnet() + Integer.toString(i));
				if(addr.isReachable(timeoutInMs)) {
					url = new URL("http:/"+addr.toString()+"/cgi-bin/zids_discovery/");
					try {
						BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream()));
				    	if(row.readLine().indexOf("Zenterio") > 0) {
//				    		System.out.println("Found a box at "+addr.getHostAddress().toString()+"!");
				    		row.close();
				    		return addr;
				    	}
						row.close();
					}
					catch(Exception e) {
//						e.printStackTrace();
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
			
		}
		System.out.println("fin");
		return null;
	}
	
	@Override
	public STB[] find() {
		// TODO Auto-generated method stub
//		try {
//			
//			}
//		catch (RuntimeException) {
//			continue;
//		}
		return null;
	}

	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

}
