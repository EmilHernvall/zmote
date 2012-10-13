package se.z_app.zmote.epg;

import android.net.wifi.p2p.WifiP2pManager.Channel;
import se.z_app.stb.EPG;

public class EPGContentHandler implements Runnable {
	private static EPGContentHandler instance;
	
	private EPGContentHandler(){	
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	private Thread thread;
	private boolean isRunning;
	
	
	public void reset(){
		isRunning = false;
		instance = new EPGContentHandler();
	}
	
	public static synchronized EPGContentHandler instance(){
		if(instance == null)
			instance = new EPGContentHandler();
		return instance;
	}
	
	public EPG getEPG(){
		return null;
	}
	public Channel getCurrentChannel(){
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
