package se.z_app.stb.api.zenterio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.util.Log;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;

import se.z_app.stb.api.STBDiscovery; // Should this be imported?
import se.z_app.zmote.gui.SelectSTBActivity;


/*
 * TODO: Dunno know if the async should be here or its own class.
 * 
 * 
 */
public class Discovery extends AsyncTask<Integer, Integer, STB[]> implements DiscoveryInterface {
	private static int timeoutInMs = 50;
	STBDiscovery stbDisc = new STBDiscovery();
	private String ipaddress;
	public STB[] stbss;
	
	public Discovery (String ipaddress, STB[] stbs) {
		this.ipaddress = ipaddress;
		this.stbss = stbs;
	}
	
	@Override
	protected STB[] doInBackground(Integer... params) {
		System.out.println("Scan started...");
		return find();
	}
	
	protected void onPostExecute(STB[] stb) {
		this.stbss = stb;
		System.out.println(stbss[0].getBoxName());
		System.out.println("Scan finished.");
		
	}
	
	/* 
	 * The find function that's initialized in doInBackground
	 */
	public STB[] find() {
		System.out.println("find()");
		LinkedList<InetAddress> boxes = null;
		boxes = findSTBIPAddresses();
		STB[] stbs = new STB[boxes.size()];
		try {
			for (int i=0;i<boxes.size();i++) {
				stbs[i] = createSTBObject(boxes.get(i));
			}
		}
		catch (RuntimeException e) { e.printStackTrace(); }
		return stbs;
	}

	/*
	 * Initiates STB object
	 */
	private STB createSTBObject(InetAddress addr) {
		System.out.println("createSTBObject()");
		STB stb = new STB();
		stb.setIP(addr); //Sets IP
		String str;
		try {
			URL url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
			BufferedReader row = new BufferedReader(new InputStreamReader(url.openStream()));
	    		while ((str = row.readLine()) != null) {
	    			if (str.contains("Boxname")) { //Sets BoxName
	    				stb.setBoxName(str.split(": ", 2)[1]);
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
	 * Finds the IP addresses of any STB boxes in the sub network.
	 * TODO: Verify if the function for finding more than 1 STB works.
	 * TODO: Exception handling can be somewhat improved...
	 */
	public LinkedList<InetAddress> findSTBIPAddresses() {
		URL url;
		InetAddress addr;
		String str;
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		BufferedReader row = null;
		
		for (int i = 1; i < 255; i++) { //Scans the subnet (for example 192.168.0) addresses .1 to .254
			try {
				addr = InetAddress.getByName(this.ipaddress+Integer.toString(i));
				if(addr.isReachable(timeoutInMs)) {
					url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
					try {
						row = new BufferedReader(new InputStreamReader(url.openStream()));
						if(row.readLine().contains("Zenterio")) {
				    		boxes.add(addr);
				    		System.out.println("Found box at "+addr.getHostAddress().toString());
				    		while ((str = row.readLine()) != null) {
				    			if (str.contains("Box")) { //Adds multiple boxes if found
				    				addr = InetAddress.getByName(str.split(": ", 2)[1].split(";")[0]);
				    				System.out.println("Other box: "+addr.getHostAddress().toString());
				    				boxes.add(addr);
				    			}
				    		}
				    		break;
				    	}
						row.close();
					}
					catch(Exception e) {  /*e.printStackTrace();*/  }
				}
			} catch (Exception e) { /*e.printStackTrace();*/ }
		}
		return boxes;
	}
	
	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

}