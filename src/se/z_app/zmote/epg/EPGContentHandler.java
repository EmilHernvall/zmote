package se.z_app.zmote.epg;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.EPGData;

public class EPGContentHandler implements Runnable {
	
	
	private Thread thread;
	private boolean isRunning;
	
	
	private EPG currentEPG;
//	private long lastEPGTimeWindow;
//	private long lastEPGUpdate;
	private Channel currentChannel; 
//	private long lastChannelTimeWindow;
//	private long lastChannelUpdate;
	
	 private static class SingletonHolder { 
         public static final EPGContentHandler INSTANCE = new EPGContentHandler();
	 }	
	public static EPGContentHandler instance(){
		return SingletonHolder.INSTANCE;
	}
	private EPGContentHandler(){	
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public EPG getEPG(){
		if(currentEPG == null){
			currentEPG = EPGData.instance().getEPG();
			EPGData.instance().populateWithChannelIcon(currentEPG);
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
