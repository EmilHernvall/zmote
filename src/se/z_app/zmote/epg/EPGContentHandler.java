package se.z_app.zmote.epg;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.EPGData;
import se.z_app.stb.api.STBContainer;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Class that contains and controls the current EPG and the current channel
 * @author ?
 *
 */
public class EPGContentHandler implements Runnable, Observer{
	//TODO: Implemnt cacheing of icons
	private Thread thread;
	private boolean isRunning;
	private EPG currentEPG;
	private Channel currentChannel;
	private EPGdbHandler theDatabaseHandler;
	private static Context theContext;
	private long updateIntervalMillis = 3600 * 1000;

	/* Singleton */
	private static class SingletonHolder { 
         public static final EPGContentHandler INSTANCE = new EPGContentHandler();
	 }	
	
	/**
	 * Get the instance of EPGContentHandler
	 * @return the instance
	 */
	public static EPGContentHandler instance(){
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Set the context for the database
	 */
	public static void setContext(Context theContextIn) {
		theContext = theContextIn;
	}
	
	/**
	 * The private constructor
	 */
	private EPGContentHandler(){	
		thread = new Thread(this);
		isRunning = true;
		
		STBContainer.instance().addObserver(this);
		currentEPG = new EPG();
		currentChannel = new Channel();
		thread.start();
	}
	
	/**
	 * Get the current EPG
	 * @return the EPG
	 */
	public EPG getEPG(){	
		synchronized (currentEPG) {
			if(currentEPG == null){
				buildEPG();
			}
			return currentEPG;
		}
	}
	
	/**
	 * Get the current channel
	 * @return the channel
	 */
	public Channel getCurrentChannel(){	
		synchronized (currentChannel) {
			if(currentChannel == null) {
				currentChannel = EPGData.instance().getCurrentChannel();
			}
			return currentChannel;
		}
	
	}

	//TODO: Implement fetching populating EPG with Icons from cache, as well as putting "Missing image" images if a images
	//is missing from the STB or cache
	/**
	 * Build the EPG by getting the icons from the box
	 */
	private void buildEPG() {
		if(theContext != null) {
			EPGdbHandler theHandler = new EPGdbHandler(theContext);
			EPG cachedEPG = theHandler.selectEPG(STBContainer.instance().getActiveSTB());
			if(cachedEPG == null || cachedEPG.getDateOfCreation() < System.currentTimeMillis() + updateIntervalMillis) {
				currentEPG = EPGData.instance().getEPG();
				if(currentEPG != null) {
					EPGData.instance().populateAbsentChannelIcon(currentEPG);
				}
				else{
					currentEPG = new EPG();
				}
				theHandler.updateEPG(STBContainer.instance().getActiveSTB(), currentEPG);
			}
			else {
				currentEPG = cachedEPG;
			}
		}
		else {
			currentEPG = EPGData.instance().getEPG();
			if(currentEPG != null){
				EPGData.instance().populateAbsentChannelIcon(currentEPG);
			}else{
				currentEPG = new EPG();
			}
		}
	}
	
	/**
	 * The main function of the class, a loop that gets
	 *  the current channel and builds the EPG whenever it changes
	 */
	@Override
	public void run() {
		while(isRunning){
			if(STBContainer.instance().getActiveSTB() != null){
				synchronized (currentEPG) {
					synchronized (currentChannel) {
						buildEPG();
						currentChannel = EPGData.instance().getCurrentChannel();
						if(currentChannel == null) {
							currentChannel = new Channel();
						}
					}
				}
				
				//TODO: Implement fetching all channel icons from the STB and cache them for reuse.
				
			}
			
			
			try {
				synchronized (thread) {
					thread.wait();
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	/**
	 * When something changes, update the EPG and current channel if necessary
	 */
	@Override
	public void update(Observable observable, Object data) {
		synchronized (thread) {
			thread.notifyAll();
		}	
		
	}
	
	/**
	 * Get the current time limit for how long the EPG is valid
	 * @return milliseconds
	 */
	public long getUpdateInterval() {
		return updateIntervalMillis;
	}
	
	/**
	 * Set the current time limit for how long the EPG is valid
	 * @param interval in milliseconds
	 */
	public void setUpdateInterval(long intervalIn) {
		updateIntervalMillis = intervalIn;
	}

}
