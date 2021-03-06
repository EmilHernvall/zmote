
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
 * @author Viktor Dahl
 */
public class Remove_DiscoveryOld implements DiscoveryInterface {
	/* Timeout for each ping request when searching for IP addresses in use */
	private static int TIMEOUTINMS = 200; 
	/* Number of threads to scan */
	private static int NUMBEROFSCANTHREADS = 32;
	private static boolean ISRUNNING = false;
	private static int threadCountSTBObj = 0;
	private static int threadCountFindIP = 0;
	private String subNetAddress;
	long t1, t2, t3; //for timing measurement
	
	public Remove_DiscoveryOld (String subNetAddress) {
		this.subNetAddress = subNetAddress;
	}

	/**
	 * Is initialized in STBDiscovery.find(). Finds the IP addresses first
	 * then creates a boxes for each IP found. Returns an array of STB's
	 */
	@Override
	public STB[] find() {
		LinkedList<InetAddress> boxes = null;
		boxes = findSTBIPAddresses(); /* Searches the subnet for addresses of Zenterio boxes */
		for (InetAddress inetAddress : boxes) {
			System.out.println("Boxes to be added: " + inetAddress.toString());
		}
		STB[] stbs = new STB[boxes.size()];
		
		createSTBObjectThread stbThread;
		t2 = System.nanoTime();
		LinkedList<createSTBObjectThread> threads = new LinkedList<Remove_DiscoveryOld.createSTBObjectThread>();
		try { // Creates a thread for every box to gather info
			for (int i=0;i<boxes.size();i++) {
				stbThread = new createSTBObjectThread(boxes.get(i));
				threadCountSTBObj++;
				stbThread.start();
				threads.add(stbThread);
			}
			
			while(threadCountSTBObj > 0) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			for (int i=0;i<boxes.size();i++) {
				stbs[i] = threads.get(i).getBox();
			}
			
		}
		catch (RuntimeException e) { e.printStackTrace(); }
		t3 = System.nanoTime();
		System.out.println("Time for boxgetting: " + ((t3-t2)/1000000)
							+"ms. Total time: " + ((t3-t1)/1000000) + ".");
		return stbs;
	}

	/**
	 * Finds the IP addresses of any STB boxes in the sub network.
	 * Returns a LinkedList with the IP addresses of the boxes.
	 * @return
	 */
	private LinkedList<InetAddress> findSTBIPAddresses() {
		LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		ScanObjectThread scanner;
		LinkedList<ScanObjectThread> objects = new LinkedList<ScanObjectThread>();
		ISRUNNING = true;
		for (int j=0;j<NUMBEROFSCANTHREADS;j++) {
			scanner = new ScanObjectThread(j);
			threadCountFindIP++;
			scanner.start();
			objects.add(scanner);
		}
		t1 = System.nanoTime();
		while(threadCountFindIP > 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
		long t2 = System.nanoTime();
		System.out.println("Time for scan: " +(t2-t1)/1000000+"ms");
		for (int i=0;i<NUMBEROFSCANTHREADS;i++) {
			if (((boxes = objects.get(i).getBoxes()).size()) > 0) {
				break;
			}
		}
		System.out.println("Ending scan");
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
		} catch (Exception e) {}
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
	 * @author Viktor Dahl
	 *
	 */
	private class ScanObjectThread extends Thread{
		private InetAddress addr;
		private String line;
		private LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
		private BufferedReader row = null;
		private int rangeIndex;
		
		public ScanObjectThread (int rangeIndex) {
			this.rangeIndex = rangeIndex;
		}
		private int calculateStartRange(int start) {
			if (start == 0) {
				return 1;
			}
			return start*(256/NUMBEROFSCANTHREADS);
		}
		private int calculateEndRange(int end) {
			end *= (256/NUMBEROFSCANTHREADS);
			if (end == (256-(256/NUMBEROFSCANTHREADS))) {
				return 255;
			} 
			return end+(256/NUMBEROFSCANTHREADS);
		}
		public LinkedList<InetAddress> getBoxes() {
			return boxes;
		}
	
		public void run() {
			for (int i = calculateStartRange(rangeIndex);i<calculateEndRange(rangeIndex) && ISRUNNING;i++) {
				try {
					addr = InetAddress.getByName(subNetAddress+Integer.toString(i));
					System.out.println("Trying: " + addr.toString());
					if(addr.isReachable(TIMEOUTINMS)) {
						if ((row = isZenterioSTB(addr)) != null) {
							ISRUNNING = false;
							
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
				    		break;
						}
					}
				} catch(Exception e) {  /*e.printStackTrace();*/  }
			}
			
			threadCountFindIP--;
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
		
		public createSTBObjectThread(InetAddress addr) {
			this.addr = addr;
		}

		public STB getBox() {
			return stb;
		}
		public void run() {
			stb = createSTBObject(addr);
			threadCountSTBObj--;
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
