
package se.z_app.stb.api.zenterio;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;

import se.z_app.stb.api.STBDiscovery; // Should this be imported?
import se.z_app.zmote.gui.R;
import se.z_app.zmote.gui.SelectSTBActivity;


/*
 * 
 */
public class Discovery implements DiscoveryInterface {
	private static int timeoutInMs = 30;
	private String ipaddress;
	public static boolean isRunning, isLoadingBoxes = false;
	
	
	public Discovery (String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/* 
	 * The find function that's initialized in doInBackground
	 */
	@Override
	public STB[] find() {
		LinkedList<InetAddress> boxes = null;
		boxes = findSTBIPAddresses();
		STB[] stbs = new STB[boxes.size()];
		try {
			for (int i=0;i<boxes.size();i++) {
				stbs[i] = createSTBObject(boxes.get(i));
			}
		}
		catch (RuntimeException e) { e.printStackTrace(); }
		System.out.println("find() "+stbs);
		return stbs;
	}

	/*
	 * Initiates STB object
	 */
	private STB createSTBObject(InetAddress addr) {
//		System.out.println("createSTBObject()");
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
	private LinkedList<InetAddress> findSTBIPAddresses() {
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		ScanObject scanner;
		LinkedList<ScanObject> objects = new LinkedList<ScanObject>();
		
		isRunning = true;
		for (int j=0;j<8;j++) {
			scanner = new ScanObject(j);
			scanner.start();
			objects.add(scanner);
		}
		int count = 0;
		long t1 = System.nanoTime();
		while(isRunning) {
			count++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { e.printStackTrace(); }
			if (count == 500)  { //Stops scan after 5 sec
				break;
			}	
		}
		
		long t2 = System.nanoTime();
		System.out.println("Time for scan: " +(t2-t1)/1000000+"ms");
		while (isLoadingBoxes) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { };
		}
		long t3 = System.nanoTime();
		System.out.println("Time for boxgetting: " +(t3-t2)/1000000+"ms");
		for (int i = 0; i<8; i++) {
			if (((boxes = objects.get(i).getBoxes()).size()) > 0) {
				break;
			}
		}
//		System.out.println("findSTBIPAddresses "+boxes);
		return boxes;
		
	}
	
	/*
	 * Finds out if the box is a Zenterio box and if so returns the rest of the HTTP GET.
	 */
	private BufferedReader isZenterioSTB(InetAddress addr) {
		BufferedReader row;
		URL url;
		try {
			url = new URL("http://"+addr.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
			row = new BufferedReader(new InputStreamReader(url.openStream()));
			if(row.readLine().contains("Zenterio")) {
				return row;
	    	}
			row.close();
		} catch (Exception e) { /*e.printStackTrace();*/ }
		return null;
	}
	
	/*
	 * Not written yet
	 * @see se.z_app.stb.api.DiscoveryInterface#find(se.z_app.stb.STB)
	 * TODO: Todo.
	 */
	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * Scans in different threads.
	 */
	private class ScanObject extends Thread{
		InetAddress addr;
		String line;
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		BufferedReader row = null;
		int rangeIndex;
		
		public ScanObject (int rangeIndex) {
			this.rangeIndex = rangeIndex;
		}
		
		private int calculateStartRange(int start) {
			if (start == 0) {
				return 1;
			}
			return start*32;
		}
		private int calculateEndRange(int end) {
			end *= 32;
			if (end == 224) {
				return 255;
			} 
			return end+32;
		}
		public LinkedList<InetAddress> getBoxes() {
//			System.out.println("getBoxes()");
			return boxes;
			
		}
		
		public void run() {
			for (int i = calculateStartRange(rangeIndex);i<calculateEndRange(rangeIndex) && isRunning;i++) {
				try {
					addr = InetAddress.getByName(ipaddress+Integer.toString(i));
					if(addr.isReachable(timeoutInMs)) {
						if ((row = isZenterioSTB(addr)) != null) {
							isLoadingBoxes = true;
							isRunning = false;
							boxes.add(addr);
				    		System.out.println("Found box at "+addr.getHostAddress().toString());
				    		while ((line = row.readLine()) != null) {
				    			if (line.contains("Box")) { //Adds multiple boxes if found
				    				addr = InetAddress.getByName(line.split(": ", 2)[1].split(";")[0]);
				    				System.out.println("Other box: "+addr.getHostAddress().toString());
				    				boxes.add(addr);
				    			}
				    		}
				    		row.close();
				    		isLoadingBoxes = false;
				    		break;
						}
					}
				} catch(Exception e) {  /*e.printStackTrace();*/  }
			}
		}
	}
}