package se.z_app.zmote.epg;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.EPGData;
import se.z_app.stb.api.STBContainer;

public class EPGContentHandler implements Runnable, Observer{
	
	
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
		
		STBContainer.instance().addObserver(this);
		thread.start();
		currentEPG = new EPG();
		currentChannel = new Channel();
		
	}
	
	public EPG getEPG(){	
		synchronized (currentEPG) {
			return currentEPG;
		}
	}
	public Channel getCurrentChannel(){	
		synchronized (currentChannel) {
			return currentChannel;
		}
	
	}

	@Override
	public void run() {
		while(isRunning){
			
			if(STBContainer.instance().getActiveSTB() != null){
				synchronized (currentEPG) {
					currentEPG = EPGData.instance().getEPG();
					EPGData.instance().populateWithChannelIcon(currentEPG);
				}
				synchronized (currentChannel) {
					currentChannel = EPGData.instance().getCurrentChannel();
				}
								
			}
			
			try {
				synchronized (thread) {
					thread.wait();
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		synchronized (thread) {
			thread.notifyAll();
		}	
		
	}

}
