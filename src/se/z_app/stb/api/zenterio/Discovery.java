
package se.z_app.stb.api.zenterio;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;


/**
 * Object that includes all necessary functions for discovering a Zenterio STB.
 * @author viktordahl
 *
 */
public class Discovery implements DiscoveryInterface {
	private static int timeOutInMs = 30; //Timeout for each ping request when searching for IP addresses in use
	private static int timeOutSTBScannerInMs = 4000; // Timeout for the scan if no boxes are found
	private String subNetAddress;
	public static boolean isRunning, isLoadingBoxes = false;
	
	public Discovery (String subNetAddress) {
		this.subNetAddress = subNetAddress;
	}

	/**
	 * Is initialized in doInBackground(). Finds the IP addresses first then creates a boxes for each IP found. Returns an array of STB's
	 * TODO: Test if the new function (get stb thread) works. Should cut the find() function time pretty much in 2.
	 */
	@Override
	public STB[] find() {
		LinkedList<InetAddress> boxes = null;
		boxes = findSTBIPAddresses();
		STB[] stbs = new STB[boxes.size()];
		
		createSTBObjectThread stbThread;
		try {
			for (int i=0;i<boxes.size();i++) {
				stbThread = new createSTBObjectThread(boxes.get(i));
				stbThread.start();
				while(stbThread.isRunning()) {
					
				}
				stbs[i] = stbThread.getBox();
			}
		}
		catch (RuntimeException e) { e.printStackTrace(); }
		return stbs;
	}

	/**
	 * Checks if all 8 threads are still scanning. 
	 * @param objects
	 * @return
	 */
	private boolean isScanning(LinkedList<ScanObjectThread> objects) {
		int count = 0;
		for (int i=0;i<8;i++) {
			if (objects.get(i).isScanning()) {
				count++;
			}
		}
		if (count == 0) {
			return false;
		}
		return true;
	}
	/**
	 * Finds the IP addresses of any STB boxes in the sub network. Returns a LinkedList with the IP addresses of the boxes
	 * @return
	 */
	private LinkedList<InetAddress> findSTBIPAddresses() {
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		ScanObjectThread scanner;
		LinkedList<ScanObjectThread> objects = new LinkedList<ScanObjectThread>();
		
		isRunning = true;
		for (int j=0;j<8;j++) {
			scanner = new ScanObjectThread(j);
			scanner.start();
			objects.add(scanner);
		}
		
		long t1 = System.nanoTime();
		while(isScanning(objects)) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		long t2 = System.nanoTime();
		System.out.println("Time for scan: " +(t2-t1)/1000000+"ms");
		while (isLoadingBoxes) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { };
		}
		long t3 = System.nanoTime();
		System.out.println("Time for getting boxes: " +(t3-t2)/1000000+"ms");
		for (int i = 0; i<8; i++) {
			if (((boxes = objects.get(i).getBoxes()).size()) > 0) {
				break;
			}
		}
		return boxes;
		
	}
	
	/**
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
	
	/**
	 * Not written yet
	 * @see se.z_app.stb.api.DiscoveryInterface#find(se.z_app.stb.STB)
	 * TODO: Todo.
	 */
	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 	Divides scan into multiple threads for speed. One object for each 32 addresses (0-31, 32-64 etc). Speeds up request
	 * @author viktordahl
	 *
	 */
	private class ScanObjectThread extends Thread{
		private InetAddress addr;
		private String line;
		private LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		private BufferedReader row = null;
		private int rangeIndex;
		private boolean isScanning = true;
		
		public ScanObjectThread (int rangeIndex) {
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
			return boxes;
		}
		public boolean isScanning() {
			return isScanning;
		}
		
		public void run() {
			for (int i = calculateStartRange(rangeIndex);i<calculateEndRange(rangeIndex) && isRunning;i++) {
				try {
					addr = InetAddress.getByName(subNetAddress+Integer.toString(i));
					if(addr.isReachable(timeOutInMs)) {
						if ((row = isZenterioSTB(addr)) != null) {
							isRunning = false;
							isLoadingBoxes = true;
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
			isScanning = false;
		}
	}
	
	/**
	 * Divides the HTTP GET requests into multiple threads for faster accessing the boxes. Should take approx 1 sec no matter how many boxes there are
	 * @author viktordahl
	 *
	 */
	private class createSTBObjectThread extends Thread {
		private InetAddress addr;
		private STB stb;
		private boolean stillRunning = true;
		
		public createSTBObjectThread(InetAddress addr) {
			this.addr = addr;
		}
		public boolean isRunning() {
			return stillRunning;
		}
		public STB getBox() {
			return stb;
		}
		public void run() {
			stb = createSTBObject(addr);
			stillRunning = false;
		}
		/**
		 * Creates an STB object
		 * @param addr
		 * @return
		 */
		private STB createSTBObject(InetAddress addr) {
			STB stb = new STB();
			stb.setIP(addr.getHostAddress()); //Sets IP
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
		    			} else if (str.contains("MAC")) { // Sets the Mac Address
		    				stb.setMAC(str.split(": ", 2)[1]);
		    				break;
		    			}
		    		}
				row.close();
			}
			catch(Exception e) { e.printStackTrace(); }
			return stb;
		}
	}
}
