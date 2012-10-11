package se.z_app.stb.api.zenterio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;

import se.z_app.stb.api.STBDiscovery; // Should this be imported?

public class Discovery implements DiscoveryInterface {

	STBDiscovery stb = new STBDiscovery();
	
	/*
	 * Scans the network for STB's.
	 * Return an object InetAddress with the IP address to the STB.
	 * With this the find() function can retrieve the IP addresses to the other STB's for easier creation of the STB classes
	 * TODO: Write better exception handling. Maybe parallelizes the scan. 30ms=7seconds for 254 addresses unparallelized. 
	 */
	private LinkedList<InetAddress> findSTBIPAddresses() {
		URL url;
		int timeoutInMs = 30;
		InetAddress addr;
		String str;
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		
		for (int i = 1; i < 255; i++) {
			try {
				addr = InetAddress.getByName(stb.findSubnet() + Integer.toString(i));
				if(addr.isReachable(timeoutInMs)) {
					url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
					try {
						BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream()));
						if(row.readLine().indexOf("Zenterio") > 0) {
				    		boxes.add(addr);
				    		while ((str = row.readLine()) != null) {
				    			if (str.indexOf("Box") > 0) { //Adds multiple boxes if found
				    				addr = InetAddress.getByName(str.substring(str.indexOf((':')+2),str.indexOf(';')));
				    				System.out.println(addr.getHostAddress().toString());
				    				boxes.add(addr);
				    			}
				    		}
				    	}
						row.close();
					}
					catch(Exception e) { /* e.printStackTrace(); */ }
				}
			} catch (Exception e) {/* e.printStackTrace(); */ }
			
		}
		System.out.println("fin");
		return boxes;
	}
	
	/*
	 * Creates a STB object
	 * TODO: Test if this actually works.
	 */
	private STB createSTBObject(InetAddress addr) {
		STB stb = new STB();
		stb.setIP(addr); //Sets IP
		String str;
		try {
			URL url = new URL("http:/"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
			BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream()));
	    		while ((str = row.readLine()) != null) {
	    			if (str.indexOf("Boxname") > 0) { //Sets BoxName
	    				stb.setBoxName(str.substring(str.indexOf(':'+2),str.length()));
	    			} else if (str.indexOf("MAC") > 0) { // Sets Mac Address
	    				
	    			} else if (str.indexOf("Product") > 0) {
	    				
	    			} else if (str.indexOf("Box") > 0) {
	    				break;
	    			}
	    		}
			row.close();
		}
		catch(Exception e) { /* e.printStackTrace(); */	}
		return stb;
	}
	/*
	 * Uses the functions for creating STB objects and returning the array of appropriate size
	 * TODO: Test this function
	 * @see se.z_app.stb.api.DiscoveryInterface#find()
	 */
	@Override
	public STB[] find() {
		// TODO Auto-generated method stub
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		boxes = findSTBIPAddresses();
		STB[] stbs = new STB[boxes.size()];
		try {
			for (int i=0;i<boxes.size();i++) {
				stbs[i] = createSTBObject(boxes.get(i));
			}
		}
		catch (RuntimeException e) { /* e.printStackTrace(); */ }
		return stbs;
	}

	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

}
