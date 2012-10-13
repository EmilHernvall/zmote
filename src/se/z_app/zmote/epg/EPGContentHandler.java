package se.z_app.zmote.epg;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.EPGData;

public class EPGContentHandler implements Runnable {
	private static EPGContentHandler instance;
	
	private EPGContentHandler(){	
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	private Thread thread;
	private boolean isRunning;
	private EPG currentEPG;
	private long lastEPGUpdate;
	private Channel currentChannel; 
	private long lastChannelUpdate;
	
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
		if(currentEPG == null){
			currentEPG = EPGData.instance().getEPG();
		}
		return currentEPG;
	}
	public Channel getCurrentChannel(){
		if(currentChannel == null){
			currentChannel = EPGData.instance().getCurrentChannel();
		}
		return currentChannel;
	}

	@Override
	public void run() {
		while(isRunning){
						
		}
	}

}
