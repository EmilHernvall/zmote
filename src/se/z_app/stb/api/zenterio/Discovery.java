package se.z_app.stb.api.zenterio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;

import se.z_app.stb.STB;
import se.z_app.stb.STB.STBEnum;
import se.z_app.stb.api.DiscoveryInterface;

/**
 * This implementation is 5 times as fast as the original implementation of Discovery, this is largely due to not reading the
 * last line from a cgi-bin/zids_discovery since that line contains info about others that seams to be render in real time
 * @author Rasmus Holm
 *
 */
public class Discovery implements DiscoveryInterface{

	private int timeout = 200;
	private int nrOfThreads = 64;
	private String network;
	private static int runningThreads = 0;
	
	public Discovery(String network){
		this.network = network;
	}
	
	@Override
	public STB[] find() {
		LinkedList<STB> stbs = new LinkedList<STB>();
		LinkedList<STBScanner> threads = new LinkedList<STBScanner>();
		
		int addresses = 254;
		int increment = (int)(0.5+((double)addresses/(double)nrOfThreads));
		for(int i = 0; i < nrOfThreads; i++){
			STBScanner thread = new STBScanner(network, i*increment+1, i*increment+increment);
			new Thread(thread).start();
			threads.add(thread);
			runningThreads++;
		}
		
		while(runningThreads > 0){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (STBScanner stbScanner : threads) {
			stbs.addAll(stbScanner.getSTBCollection());
		}
		
		STB stbArray[] = new STB[stbs.size()];
		stbs.toArray(stbArray);
		
		return stbArray;
	}
	
	@Override
	public STB[] find(STB stb) {
		return find();
	}

	
	private class STBScanner implements Runnable {
		String network;
		int start;
		int upto;
		LinkedList<STB> stbs = new LinkedList<STB>();
		
		public STBScanner(String network, int start, int upto){
			this.network = network;
			this.start = start;
			this.upto = upto;
		}
		
		public Collection<STB> getSTBCollection(){
			return stbs;
		}
		
		@Override
		public void run() {
			for(int i = start; i <= upto && i < 255; i++ ){
				try {
					InetAddress address = InetAddress.getByName(network+Integer.toString(i));
					//System.out.println("Scanning " + address.toString());
					if(address.isReachable(timeout)) {
	
						URL url = new URL("http://"+address.getHostAddress().toString()+"/cgi-bin/zids_discovery/");
						BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
						
						String row = in.readLine();
						if(row.contains("Boxname :")) {
							STB stb = new STB();
							stb.setType(STBEnum.ZENTERIO);
							stb.setIP(address.getHostAddress().toString());
							stb.setBoxName(row.split(": ", 2)[1]);
							
							while((row = in.readLine()) != null){
								if (row.contains("MAC")) { // Sets the Mac Address
				    				stb.setMAC(row.split(": ", 2)[1]);
				    				break;
				    			}
							}
							stbs.add(stb);
				    	}
						
						in.close();
						
					}
				} catch (UnknownHostException e) {

				} catch (IOException e) {

				}
			}
			runningThreads--;
		}
		
		
	}
}
