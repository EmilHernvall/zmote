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
	private static int timeoutInMs = 30;
	STBDiscovery stb = new STBDiscovery();
		
	/*
	 * Scans the network for STB's.
	 * Return an object InetAddress with the IP address to the STB.
	 * With this the find() function can retrieve the IP addresses to the other STB's for easier creation of the STB classes
	 * TODO: Better exception handling. Right now it just skips all exceptions altogether
	 */
	private LinkedList<InetAddress> findSTBIPAddresses() {
		URL url;
		
		InetAddress addr;
		String str;
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		
		for (int i = 1; i < 255; i++) { //Scans the subnet (for example 192.168.0) addresses .1 to .254
			try {
				addr = InetAddress.getByName(stb.findSubnet()+Integer.toString(i));
				if(addr.isReachable(timeoutInMs)) {
					url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
					try {
						BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream())); // This one is really slow (4-5 sec)
						if(row.readLine().contains("Zenterio")) {
				    		boxes.add(addr);
				    		System.out.println("Found box at "+addr.getHostAddress().toString());
				    		while ((str = row.readLine()) != null) {
				    			if (str.contains("Box")) { //Adds multiple boxes if found
				    				addr = InetAddress.getByName(str.split(": ", 2)[1].split(";")[0]);
				    				System.out.println(addr.getHostAddress().toString());
				    				boxes.add(addr);
				    			}
				    		}
				    		break;
				    	}
						row.close();
					}
					catch(Exception e) {  /*e.printStackTrace();*/  }
				}
			} catch (Exception e) { /*e.printStackTrace();*/  }
		}
		System.out.println("fin");
		return boxes;
	}
	
	/*
	 * Creates a STB object
	 */
	private STB createSTBObject(InetAddress addr) {
		STB stb = new STB();
		stb.setIP(addr); //Sets IP
		String str;
		try {
			URL url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
			BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream()));
	    		while ((str = row.readLine()) != null) {
	    			if (str.contains("Boxname")) { //Sets BoxName
	    				stb.setBoxName(str.split(": ", 2)[1]);
	    				System.out.println("Boxname:: "+str.split(": ", 2)[1]);
	    				if (str.indexOf("Zenterio") > 0 || str.indexOf("zenterio") > 0) {
	    					stb.setType(STB.STBEnum.ZENTERIO);
	    				} else {
	    					stb.setType(STB.STBEnum.DEFAULT);
	    				}
	    			} else if (str.contains("MAC")) { // Sets Mac Address
	    				stb.setMAC(str.split(": ", 2)[1]);
	    				break;
	    			}
	    		}
			row.close();
		}
		catch(Exception e) { e.printStackTrace(); }
		return stb;
	}
	/*
	 * Uses the functions findSTBAddresses() and createSTBObject() and returning the array of appropriate size
	 */
	@Override
	public STB[] find() {
		LinkedList<InetAddress> boxes = findSTBIPAddresses();
		STB[] stbs = new STB[boxes.size()];
		try {
			for (int i=0;i<boxes.size();i++) {
				stbs[i] = createSTBObject(boxes.get(i));
			}
		}
		catch (RuntimeException e) { e.printStackTrace(); }
		return stbs;
	}

	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

}

	
